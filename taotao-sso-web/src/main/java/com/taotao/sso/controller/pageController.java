package com.taotao.sso.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录和注册
 * @author 剑影随风
 *
 */
@Controller
public class pageController {
	@RequestMapping("/page/register")
	public String ShowRegister(){
		return "register";
	}
	
	@RequestMapping("/page/login")
	public String ShowLogin(String url,Model model){
		//System.out.println(url);
		if(url!=null&&url!=""){
			model.addAttribute("redirect", url);
		}
		return "login";
	}
}
