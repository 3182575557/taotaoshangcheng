package com.taotao.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class testsolrj {
	@Test
	public void addDocument() throws Exception {
		// 第一步：把solrJ的jar包添加到工程中。
		// 第二步：创建一个SolrServer，使用HttpSolrServer创建对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.123:8081/solr");
		// 第三步：创建一个文档对象SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 第四步：向文档中添加域。必须有id域，域的名称必须在schema.xml中定义。
		document.addField("id", "test002");
		document.addField("item_title", "测试商品2");
		document.addField("item_price", "199");
		// 第五步：把文档添加到索引库中。
		System.out.println(document);
		solrServer.add(document);
		// 第六步：提交。
		solrServer.commit();
	}
	
	@Test
	public void deleteDocumentById() throws Exception {
		// 第一步：创建一个SolrServer对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.123:8081/solr");
		// 第二步：调用SolrServer对象的根据id删除的方法。
		solrServer.deleteById("test002");
		// 第三步：提交。
		solrServer.commit();
	}

	
	@Test
	public void deleteDocumentByQuery() throws Exception {
		// 第一步：创建一个SolrServer对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.123:8081/solr");
		// 第二步：调用SolrServer对象的根据查询删除的方法。
		solrServer.deleteByQuery("item_title:测试商品2");
		// 第三步：提交。
		solrServer.commit();
	}
	
	@Test
	public void searchDocumet() throws Exception {
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.123:8081/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件、过滤条件、分页条件、排序条件、高亮
		//query.set("q", "*:*");
		query.setQuery("手机");
		//分页条件
		query.setStart(0);
		query.setRows(10);
		//设置默认搜索域
		query.set("df", "item_keywords");
		//设置高亮
		query.setHighlight(true);
		//高亮显示的域
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<div>");
		query.setHighlightSimplePost("</div>");
		//执行查询，得到一个Response对象
		QueryResponse response = solrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = response.getResults();
		//取查询结果总记录数
		System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			if (list != null && list.size() >0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			System.out.println(itemTitle);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println("=============================================");
		}
		
	}

}
