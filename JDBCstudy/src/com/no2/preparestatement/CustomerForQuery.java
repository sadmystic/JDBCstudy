package com.no2.preparestatement;

import java.lang.reflect.Field;

import com.no3.bean.Customers;
import com.no3.util.*;
import java.sql.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class CustomerForQuery {
	@Test
	public void testqueryforcustomers() throws Exception {
		//String sql="select id,name,email from customers where id=?";
		String sql1 = "select name,email from customers where name=?";
		//Customers customers = queryForCustomers(sql, 13);
		Customers customers = queryForCustomers(sql1, "周杰伦");
		System.out.println(customers);
	}
	/*
	 * 通用查询Customers
	 * 
	 */
	public Customers queryForCustomers(String sql,Object ...args) throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			conn = JDBCutils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				ps.setObject(i+1, args[i]);
			}
			
			rs = ps.executeQuery();
			//获取结果集元数据
			ResultSetMetaData rsmd=rs.getMetaData();
			//通过Rsmd获取列数
			int columnCount=rsmd.getColumnCount();
			
			if(rs.next()) {
				Customers cust=new Customers();
				//处理结果集一行数据中的每一个列
				for(int i=0;i<columnCount;i++) {
					//获取列值
					Object columeValue = rs.getObject(i+1);
					//获取每个列的列名
					String columnName = rsmd.getColumnName(i+1);
					
					//给cust对象指定的某个属性，赋值为columevalue,通过反射
					Field field = Customers.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(cust, columeValue);
					
				}
				return cust;
			}
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps, rs);
		}
		
		return null;
	}
	
	@Test
	public void testQuery()  {
		Connection conn=null;
		PreparedStatement ps=null;
		//执行,并返回
		ResultSet resultSet=null;
		try {
			conn = JDBCutils.getConnection();
			String sql="select id,name,email,birth from customers where id=?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			resultSet = ps.executeQuery();
			//处理结果集
			//next判断结果集的下一条是否有，true并且指针下移，false
			if(resultSet.next()) {
				int id=resultSet.getInt(1);
				String name=resultSet.getString(2);
				String email=resultSet.getString(3);
				Date birth=resultSet.getDate(4);
				
				//Object[]data=new Object[] {id,name,email,birth};
				
				//封装对象
				Customers customers=new Customers(id, name, email, birth);
				System.out.println(customers);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
		//关闭
		JDBCutils.closeResource(conn, ps, resultSet);
		}
		
	}

}
