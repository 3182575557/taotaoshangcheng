package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;
/**
 * 用户处理service
 * @author 剑影随风
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	
	/**
	 * 校验
	 */
	@Override
	public TaotaoResult checkDate(String date, int type) {
		// 1、从tb_user表中查询数据
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 2、查询条件根据参数动态生成。
		//1、2、3分别代表username、phone、email
		if(type==1){
			criteria.andUsernameEqualTo(date);
		}else if(type==2){
			criteria.andPhoneEqualTo(date);
		}else if(type==3){
			criteria.andEmailEqualTo(date);
		}else{
			return TaotaoResult.build(400, "非法参数");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		// 3、判断查询结果，如果查询到数据返回false。
		if(list==null||list.size()==0){
			// 4、如果没有返回true。
			return TaotaoResult.ok(true);
		}
		// 5、使用TaotaoResult包装，并返回。
		return TaotaoResult.ok(false);
	}
	
	/**
	 * 注册
	 */
	@Override
	public TaotaoResult register(TbUser user) {
		// 1、使用TbUser接收提交的请求。
		if(StringUtils.isBlank(user.getUsername())){
			return TaotaoResult.build(400, "用户名不能为空！");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return TaotaoResult.build(400, "密码不能为空");
		}
		//校验数据是否可用
		TaotaoResult result = checkDate(user.getUsername(), 1);
		if(!(boolean)result.getData()){
			return TaotaoResult.build(400, "此用户名已被使用！");
		}
		//校验电话是否可以
		if(StringUtils.isNotBlank(user.getPhone())){
			result = checkDate(user.getPhone(), 2);
			if(!(boolean)result.getData()){
				return TaotaoResult.build(400, "此电话已被使用！");
			}
		}
		//校验email是否可用
		if(StringUtils.isNotBlank(user.getEmail())){
			result = checkDate(user.getEmail(), 3);
			if(!(boolean)result.getData()){
				return TaotaoResult.build(400, "该邮箱已被使用");
			}
		}
		// 2、补全TbUser其他属性。
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 3、密码要进行MD5加密。
		String md5pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5pass);
		// 4、把用户信息插入到数据库中。
		userMapper.insert(user);
		// 5、返回TaotaoResult。
		return TaotaoResult.ok();
	}
	
	/**
	 * 登录
	 */
	@Override
	public TaotaoResult login(String username, String password) {
	//判断用户名和密码是否正确
	TbUserExample example = new TbUserExample();
	Criteria criteria = example.createCriteria();
	criteria.andUsernameEqualTo(username);
	List<TbUser> list = userMapper.selectByExample(example);
	if(list==null||list.size()==0){
		//返回登录失败
		return TaotaoResult.build(400, "用户名或密码错误");
	}
	TbUser user = list.get(0);
	//密码要进行md5加密然后再校验
	if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
		//返回登录失败
		return TaotaoResult.build(400, "用户名或密码错误");
	}
	//生成token，使用uuid
	String token = UUID.randomUUID().toString();
	//清空密码
	user.setPassword(null);
	//把用户信息保存到redis，key就是token，value就是用户信息
	jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
	//设置key的过期时间
	jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
	//返回登录成功，其中要把token返回。
	return TaotaoResult.ok(token);
	}

	/**
	 * 根据token查询用户信息
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get(USER_SESSION + ":" + token);
		if(StringUtils.isBlank(json)){
			return TaotaoResult.build(400, "登录信息已过期！");
		}
		//重置session过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		//把json装换为tbuser对象
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
	}
	
}
