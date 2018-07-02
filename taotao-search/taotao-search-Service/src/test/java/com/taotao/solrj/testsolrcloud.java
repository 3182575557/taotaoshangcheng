package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class testsolrcloud {
	@Test
	public void testSolrCloudAddDocument() throws Exception {
		// 第一步：把solrJ相关的jar包添加到工程中。
		// 第二步：创建一个SolrServer对象，需要使用CloudSolrServer子类。构造方法的参数是zookeeper的地址列表。
		//参数是zookeeper的地址列表，使用逗号分隔
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.123:2182,192.168.25.123:2183,192.168.25.123:2184");
		// 第三步：需要设置DefaultCollection属性。
		solrServer.setDefaultCollection("collection2");
		// 第四步：创建一SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 第五步：向文档对象中添加域
		document.addField("item_title", "测试商品1");
		document.addField("item_price", "100");
		document.addField("id", "test001");
		// 第六步：把文档对象写入索引库。
		solrServer.add(document);
		// 第七步：提交。
		solrServer.commit();

	}
	
	@Test
	public void deleteDocumentByQuery() throws Exception {
		// 第一步：把solrJ相关的jar包添加到工程中。
		// 第二步：创建一个SolrServer对象，需要使用CloudSolrServer子类。构造方法的参数是zookeeper的地址列表。
		//参数是zookeeper的地址列表，使用逗号分隔
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.123:2182,192.168.25.123:2183,192.168.25.123:2184");
		// 第三步：需要设置DefaultCollection属性。
		solrServer.setDefaultCollection("collection2");
		// 第四步：调用SolrServer对象的根据查询删除的方法。
		solrServer.deleteById("test001");
		// 第五步：提交。
		solrServer.commit();
	}

}
