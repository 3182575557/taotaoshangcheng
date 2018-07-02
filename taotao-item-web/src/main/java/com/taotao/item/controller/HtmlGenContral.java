package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 网页静态化处理controller
 * @author 剑影随风
 *
 */
@Controller
public class HtmlGenContral {
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception {
		//生成静态页面
		Configuration configuration = freeMarkerConfig.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		Map date = new HashMap<>();
		date.put("hello", "spring freemaker test");
		FileWriter out = new FileWriter(new File("F:/Java-ee/taotao2017/freemaker/test.html"));
		template.process(date, out);
		out.close();
		//返回处理结果
		return "ok";
	}
	
}
