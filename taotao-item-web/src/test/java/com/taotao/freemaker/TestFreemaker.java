package com.taotao.freemaker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemaker {
	
	@Test
	public void testFreemaker() throws Exception{
	//创建一个模板文件
	//第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
	Configuration configuration = new Configuration(Configuration.getVersion());
	//第二步：设置模板文件所在的路径。
	configuration.setDirectoryForTemplateLoading(new File("F:/Java-ee/taotao2017/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
	//第三步：设置模板文件使用的字符集。一般就是utf-8.
	configuration.setDefaultEncoding("utf-8");
	//第四步：使用Configuration对象加载一个模板，创建一个模板对象。
	//Template template = configuration.getTemplate("hello.ftl");
	Template template = configuration.getTemplate("student.ftl");
	//第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map比较灵活。
	Map date = new HashMap<>();
	student Student = new student(1, "颠倒", 5, "北京");
	List<student> stulist = new ArrayList<>();
	stulist.add(new student(2, "颠倒", 55, "北京1"));
	stulist.add(new student(3, "颠倒", 55, "北京2"));
	stulist.add(new student(4, "颠倒", 55, "北京3"));
	stulist.add(new student(5, "颠倒", 55, "北京4"));
	stulist.add(new student(6, "颠倒", 55, "北京5"));
	stulist.add(new student(7, "颠倒", 55, "北京6"));
	stulist.add(new student(8, "颠倒", 55, "北京7"));
	//测试日期
	Date Date1 = new java.util.Date();
	//第一个key与student.ftl里对应，第二个value与new出来的Student对应
	date.put("student", Student);
	date.put("stuList", stulist);
	date.put("date", Date1);
	date.put("NULL", "空值");
	date.put("hello", "hello freemaker");
	//第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
	//Writer out = new FileWriter("F:/Java-ee/taotao2017/freemaker/hello.txt");
	Writer out = new FileWriter("F:/Java-ee/taotao2017/freemaker/student.html");
	//第七步：调用模板对象的process方法输出文件。
	template.process(date, out);
	//第八步：关闭流。
	out.close();
	}
	
}
