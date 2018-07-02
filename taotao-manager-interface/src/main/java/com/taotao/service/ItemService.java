package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
/**
 * 商品管理
 * @author 剑影随风
 *
 */
import com.taotao.pojo.TbItemDesc;
public interface ItemService {
	//按ID查
	TbItem getItemById(long itemId);
	//分页显示
	EasyUIDataGridResult getItemList(int page, int rows);
	//添加
	TaotaoResult addItem(TbItem item, String desc);
	
	TbItemDesc getItemDescById(long itemId);
}
