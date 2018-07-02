package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.fabric.xmlrpc.base.Data;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemdescmapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;	
	
	@Autowired
	private JedisClient jedisClient;
	
	@Resource(name="itemAddtopic")
	private Destination destination;

	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	
	@Value("${ITEM_EXPIRE}")
	private Integer ITEM_EXPIRE;
	
	/**
	 * 按ID查询
	 */
	@Override
	public TbItem getItemById(long itemId) {
		//查询数据库之前先查询缓存，缓存没有再查数据库并添加到缓存
		//查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(json)){
				//把json转成pojo
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			//添加缓存
			jedisClient.set(ITEM_INFO+":"+itemId+":BASE", JsonUtils.objectToJson(item));
			//设置过期时间提高缓存利用率
			jedisClient.expire(ITEM_INFO+":"+itemId+":BASE", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * 分页显示
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		//返回结果
		return result;
	}

	/**
	 * 添加
	 */
	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		//获得商品ID
		long ItemID = IDUtils.genItemId();
		//补全item属性
		item.setId(ItemID);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//执行插入
		itemMapper.insert(item);
		//创建一个商品描述表对应pojo
		TbItemDesc itemdesc = new TbItemDesc();
		//补全商品描述表
		itemdesc.setItemId(ItemID);
		itemdesc.setItemDesc(desc);
		itemdesc.setCreated(new Date());
		itemdesc.setUpdated(new Date());
		//商品描述表提交
		itemdescmapper.insert(itemdesc);
		//向activemq发送商品添加消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				//发送商品ID
				TextMessage textMessage = session.createTextMessage(ItemID+"");
				return textMessage;
			}
		});
		return TaotaoResult.ok();
	}

	/**
	 * 商品描述内容
	 * @param itemId
	 * @return
	 */
	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询数据库之前先查询缓存，缓存没有再查数据库并添加到缓存
		//查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)){
				//把json转成pojo
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemdescmapper.selectByPrimaryKey(itemId);
		try {
			//添加缓存
			jedisClient.set(ITEM_INFO+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间提高缓存利用率
			jedisClient.expire(ITEM_INFO+":"+itemId+":DESC", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
