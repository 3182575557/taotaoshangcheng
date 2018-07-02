package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;
/**
 * 商品类目
 * @author 剑影随风
 *
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemcatservice;
	/**
	 * 查询
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0")long parentId){
		List<EasyUITreeNode> result = itemcatservice.getItemCatList(parentId);
		return result;
	}
	
	
}
