package com.no2.preparestatement;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.no1.connection.jdbc1_1;
import com.no3.util.JDBCutils;


public class preparestatementtest {
	//通用的增删改
	@Test
	public void testUpdate_1() {
		//String sql="delete from customers where id=?";
		//update(sql, 3);
		
		String sql="update `order` set order_name=? where order_id=?";
		int insertcount = update(sql, "LL","2");
		if(insertcount>0) {
			System.out.println("执行成功");
		}else {
			System.out.println("执行失败");
		}
	}
	
	//通用的增删改
	public int update(String sql,Object ...args)  {
		//sql占位符个数与可变形参长度相同
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			conn = JDBCutils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				ps.setObject(i+1, args[i]);
			}
			 /*
			  * 如果执行的是结果查询，返回true；如果是增删改，返回false
			  * ps.execute();
			  */
			return ps.executeUpdate();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps);
		}
		return 0;
	}
	
	
	//修改
	@Test
	public void testUpdate() {
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			conn = JDBCutils.getConnection();
			String sql="update customers set name = ? where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, "莫扎特");
			ps.setObject(2, 18);
			ps.execute();
		}  catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps);
		}
	
		
	}
	
	//向customers添加记录
	@Test
	public void testInsert(){
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			InputStream is = preparestatementtest.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			pros.load(is);

			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");

			Class.forName(driverClass); 

			conn = DriverManager.getConnection(url, user, password);
			System.out.println(conn);
			
			//预编译sql语句，返回ps实例
			//?占位符
			String sql= "insert into customers(name,email,birth)values(?,?,?)";
			ps = conn.prepareStatement(sql);
			
			//填充占位符
			ps.setString(1, "哪吒");
			ps.setString(2, "nezha@gmail.com");
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1000-01-01");
			ps.setDate(3, new Date(date.getTime()));
			
			//执行sql
			ps.execute();
		
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			//资源关闭
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
		
		
	}


}
