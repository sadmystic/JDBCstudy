package com.no1.connection;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import org.junit.Test;

public class jdbc1_1 {

	// 方式一
	@Test
	public void testConnection1() throws SQLException {

		Driver driver = new com.mysql.cj.jdbc.Driver();
		// jdbc:mysql://localhost:3306/databaseName?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
		String url = "jdbc:mysql://localhost:3306/test";
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "123456");

		Connection conn = (Connection) driver.connect(url, info);

		System.out.println(conn);
	}

	// 方式二,对方式一的迭代
	@Test
	public void testConnection2() throws Exception {
		Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
		Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();
		String url = "jdbc:mysql://localhost:3306/test";
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "123456");

		Connection conn = (Connection) driver.connect(url, info);

		System.out.println(conn);
	}

	// 方式三
	@Test
	public void testConnection() throws Exception {
		Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
		Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

		DriverManager.registerDriver(driver);

		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123456";
		Connection conn = DriverManager.getConnection(url, user, password);

		System.out.println(conn);
	}

	// 方式四
	@Test
	public void testConnection4() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		// Driver driver= (Driver) clazz.getDeclaredConstructor().newInstance();
		// DriverManager.registerDriver(driver);

		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123456";

		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}

	// 方式五 配置文件法
	@Test
	public void testConnection5() throws Exception {
		InputStream is = jdbc1_1.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);

		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");

		Class.forName(driverClass); 

		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}

}
