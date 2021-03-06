package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;


/**
 * 拦截器--判断用户是否登录
 * @author 剑影随风
 *生效需在SpringMVC里配置
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserService userSevice;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//执行handler之前先执行此方法
		//1.从cookie中取token信息,取不到则未登录
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		
		//2.如果取不到token，跳转到sso的登录页面，需要把当前请求的url做为参数传递给sso，sso登录成功之后跳转回请求的页面。
		if(StringUtils.isBlank(token)){
			//取当前请求的url
			String requestURL = request.getRequestURL().toString();
			//跳转到登录页面
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//拦截
			return false;
		}
		
		//3.取到token，调用sso系统的服务判断用户是否登录
		TaotaoResult taotaoResult = userSevice.getUserByToken(token);
		
		//4.如果用户未登录，即没取到用户信息。跳转到sso的登录页面
		if(taotaoResult.getStatus()!=200){
			//取当前请求的url
			String requestURL = request.getRequestURL().toString();
			//跳转到登录页面
			response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
			//拦截
			return false;
		}
		
		//5.如果取到用户信息。放行。把用户信息放到request中
		TbUser user = (TbUser) taotaoResult.getData();
		request.setAttribute("user", user);
		//返回值true：放行 	返回false：拦截
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// 在ModelAndView返回之后，异常处理

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		//handler执行之后，modelAndView返回之前

	}

}
