package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
/**
 * 内容管理
 * @author 剑影随风
 *
 */
@Controller
public class ContentController {
	/**
	 * 添加
	 */
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addConten(TbContent cont){
		TaotaoResult result = contentService.addContent(cont);
		return result;
	}
	
	
	
	
}
