package com.no1.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;



/*
 * �������ݿ⹤����
 * 
 * 
 */

public class JDBCutils {
	//����
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
	//�ر�
	public static void  closeResource(Connection conn,Statement ps) {
		try {
			if(ps!=null)
			ps.close();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		try {
			if(conn!=null)
			conn.close();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}	
	}
	
	public static void  closeResource(Connection conn,Statement ps,ResultSet rs) {
		try {
			if(ps!=null)
			ps.close();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		try {
			if(conn!=null)
			conn.close();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		try {
			if(rs!=null)
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}

}
