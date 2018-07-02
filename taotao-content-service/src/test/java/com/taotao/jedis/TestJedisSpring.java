package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class TestJedisSpring {
	@Test
	public void testjedisclientpool() throws Exception{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient client = applicationContext.getBean(JedisClient.class);
		client.set("test", "hello565");
		String t = client.get("test");
		System.out.println(t);
	}

}
