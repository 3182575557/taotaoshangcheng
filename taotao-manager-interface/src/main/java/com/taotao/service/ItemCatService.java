package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
/**
 * 商品类目管理接口
 * @author 剑影随风
 *
 */

public interface ItemCatService {
	//查询商品
	List<EasyUITreeNode> getItemCatList(long parentId);
}
