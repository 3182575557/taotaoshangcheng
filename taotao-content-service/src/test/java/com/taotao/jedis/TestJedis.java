package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {

	@Test
	public void testjedis(){
		//创建一个Jedis对象需要服务地址和IP端口
		Jedis jedis = new Jedis("192.168.25.123", 6379);
		//直接操作数据库
		jedis.set("str", "123456");
		String result = jedis.get("str");
		System.out.println(result);
		//关闭连接
		jedis.close();
		
	}
	@Test
	public void testjedispool(){
		//创建一个数据库连接池对象（单例），它需要指定服务的IP和端口
		JedisPool jedisPool = new JedisPool("192.168.25.123", 6379);
		//从连接池中获得连接
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作数据库（方法级别使用）
		String string = jedis.get("str");
		System.out.println(string);
		//关闭连接
		jedis.close();
		//关闭连接池
		jedisPool.close();
		
	}
	@Test
	public void testjediscluster() throws Exception{
		
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.123", 7001));
		nodes.add(new HostAndPort("192.168.25.123", 7002));
		nodes.add(new HostAndPort("192.168.25.123", 7003));
		nodes.add(new HostAndPort("192.168.25.123", 7004));
		nodes.add(new HostAndPort("192.168.25.123", 7005));
		nodes.add(new HostAndPort("192.168.25.123", 7006));
		
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("qqqq", "qqqqq");
		String ts = jedisCluster.get("qqqq");
		System.out.println(ts);
		jedisCluster.close();
	}
	
}
