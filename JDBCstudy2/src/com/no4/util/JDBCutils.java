package com.no4.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCutils {
	
	//传统连接
	public static Connection getConnection() throws Exception{
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		//preparestatementtest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);

		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");

		Class.forName(driverClass); 

		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
		return conn;
		
	}
	//关闭
	public static void  closeResource(Connection conn,Statement ps) {
		try {
			if(ps!=null)
			ps.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			if(conn!=null)
			conn.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
	}
	/*
	 * 使用dbutils提供的butils工具类，实现资源关闭
	 */
	public static void  closeResource1(Connection conn,Statement ps,ResultSet rs) {
		/*
		try {
			DbUtils.close(conn);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			DbUtils.close(ps);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			DbUtils.close(rs);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		*/
		DbUtils.closeQuietly(conn);
		DbUtils.closeQuietly(ps);
		DbUtils.closeQuietly(rs);
	}
	
	
	
	/*
	 * 使用c3p0数据库连接池技术	
	 * 
	 */
	//数据库连接池提供一个即可
	
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
	public static Connection getConnection1() throws Exception {
		//ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
		Connection conn = cpds.getConnection();
		
		return conn; 
	}
	
	
	
	
	
	
	/*
	 * 使用DBCP数据库连接池技术	
	 * 
	 */
	//创建一个DBCP连接池

	private static DataSource source;
	static {
		try {
			Properties pros=new Properties();
			FileInputStream is=new FileInputStream(new File("src/dbcp.properties"));
				
			pros.load(is);
			source = BasicDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static Connection getConnection2() throws Exception {

		Connection conn = source.getConnection();
		
		return conn;
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * 使用DRUID数据库连接池技术	
	 * 
	 */
	private static DataSource source1;
	static {
		try {
			Properties pros=new Properties();
			
			InputStream is = ClassLoader.getSystemClassLoader().getSystemResourceAsStream("druid.properties");
			
			pros.load(is);
			
			source1 = DruidDataSourceFactory.createDataSource(pros);
		}  catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static Connection getConnection3() throws Exception {
		
		Connection conn = source1.getConnection();
		return conn;
	}
}
