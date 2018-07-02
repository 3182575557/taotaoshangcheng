package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
/**
 * 商品详情界面展示control
 * @author 剑影随风
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String ShowItem(@PathVariable Long itemId,Model model){
		//取商品基本信息
		System.out.println(itemId);
		TbItem tbItem = itemService.getItemById(itemId);
		System.out.println(tbItem);
		Item item = new Item(tbItem);
		//取商品详情
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		//把数据传递给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		//返回逻辑视图
		return "item";
	}
	
}
