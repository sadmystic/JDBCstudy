package com.no4.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

public class DBCPTest {
	/*
	 * 测试DBCP的数据库连接池技术
	 * 
	 */
	//方式一:不推荐
	@Test
	public void testGetConnection() throws Exception {
		//创建DBCP连接池
		BasicDataSource source=new BasicDataSource(); 
		
		//设置基本信息
		source.setDriverClassName("com.mysql.cj.jdbc.Driver");
		source.setUrl("jdbc:mysql:///test");
		source.setUsername("root");
		source.setPassword("123456");
		
		//设置数据库连接池管理的相关属性
		source.setInitialSize(10);
		source.setMaxActive(10);
		
		Connection conn=source.getConnection();
		System.out.println(conn);
	}
	
	//方式二:配置文件法
	@Test
	public void testGetConnection1() throws Exception {
		Properties pros=new Properties();
		//方式一
		//InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		//方式二
		FileInputStream is=new FileInputStream(new File("src/dbcp.properties"));
		pros.load(is);
		DataSource source = BasicDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
}
