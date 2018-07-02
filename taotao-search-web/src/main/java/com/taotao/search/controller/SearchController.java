package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 查询
 * @author 剑影随风
 *
 */
@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	@RequestMapping("/search")
	public String search(@RequestParam("q")String queryString,
		@RequestParam(defaultValue="1")Integer page,Model model) throws Exception {
		//rows配置为属性文件
		//调用服务查询
//		try {
			//把查询条件进行转码解决get乱码问题
			queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			SearchResult result = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
			//把结果传递给页面
			model.addAttribute("query", queryString);
			model.addAttribute("totalPages", result.getTotalPages());
			model.addAttribute("itemList", result.getItemList());
			model.addAttribute("page", page);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return "search";
	}
}
