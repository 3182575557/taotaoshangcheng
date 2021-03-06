package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面展示Controller
 * <p>Title: PageController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class PageController {
	/**
	 * 拦截无匹配访问    进入首页
	 * @return
	 */
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}
	/**
	 * 显示页
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
	
}
