package com.no2.preparestatement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.no3.bean.Customers;
import com.no3.bean.Order;
import com.no3.util.JDBCutils;


/*
 * 使用preparestatement实现针对于不同表的通用查询
 * 
 * 
 */
public class preparestatementquerytest {
	
	@Test
	public void testgetForlist() {
		String sql="select id,name,email from customers where id < ?";
		List<Customers> list = getForlist(Customers.class,sql,12);
		list.forEach(System.out::println);
		
		String sql1="select order_id orderid,order_name ordername from `order` where order_id<?";
		List<Order> orderlist = getForlist(Order.class,sql1,5);
		orderlist.forEach(System.out::println);
	}
	
	public <T> List<T> getForlist(Class<T> clazz,String sql,Object ...args) {
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
			//创建集合对象
			ArrayList<T> list = new ArrayList<T>();
			while(rs.next()) {
				T t=clazz.newInstance();
				//处理结果集一行数据中的每一个列;给t对象指定的属性赋值
				for(int i=0;i<columnCount;i++) {
					//获取列值
					Object columeValue = rs.getObject(i+1);
					//获取每个列的列名getColumnName() --不推荐--
					//获取每个列的别名getColumnLabel()--一般都可用--
					//String columnName = rsmd.getColumnName(i+1);
					String columnlabel = rsmd.getColumnLabel(i+1);
					//给t对象指定的某个属性，赋值为columevalue,通过反射
					Field field = clazz.getDeclaredField(columnlabel);
					field.setAccessible(true);
					field.set(t, columeValue);
					
				}
				list.add(t);
			}
			return list;
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps, rs);
		}
		
		return null;
	}
	
	
	@Test
	public void testGetInstance() {
		//String sql="select id,name,email from customers where id=?";
		//Customers customers = getInstance(Customers.class,sql,12);
		//System.out.println(customers);
		
		String sql1="select order_id orderid,order_name ordername from `order` where order_id=?";
		Order order = getInstance(Order.class,sql1,1);
		System.out.println(order);
	}
	/*
	 * 针对不同表的一条记录;
	 * 
	 */
	public <T>T getInstance(Class<T> clazz,String sql,Object ...args) {
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
				T t=clazz.newInstance();
				//处理结果集一行数据中的每一个列
				for(int i=0;i<columnCount;i++) {
					//获取列值
					Object columeValue = rs.getObject(i+1);
					//获取每个列的列名getColumnName() --不推荐--
					//获取每个列的别名getColumnLabel()--一般都可用--
					//String columnName = rsmd.getColumnName(i+1);
					String columnlabel = rsmd.getColumnLabel(i+1);
					//给t对象指定的某个属性，赋值为columevalue,通过反射
					Field field = clazz.getDeclaredField(columnlabel);
					field.setAccessible(true);
					field.set(t, columeValue);
					
				}
				return t;
			}
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps, rs);
		}
		
		return null;
		
		
	}

}
