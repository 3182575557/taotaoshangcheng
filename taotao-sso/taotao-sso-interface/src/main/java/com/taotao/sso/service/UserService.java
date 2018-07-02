package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
/**
 * 单点登录
 * @author 剑影随风
 *
 */
public interface UserService {
	TaotaoResult checkDate(String date,int type);
	TaotaoResult register(TbUser user);
	TaotaoResult login(String username,String password);
	TaotaoResult getUserByToken(String token);
}
