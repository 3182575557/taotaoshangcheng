package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 索引库维护
 * @author 剑影随风
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
@Controller
public class IndexManagerContrall {
	@Autowired
	private SearchItemService searchItemService;
	@RequestMapping("/index/import")
	@ResponseBody
	public TaotaoResult importIndex(){
		TaotaoResult result = searchItemService.importItemsToIndex();
		return result;
	}
	
}
