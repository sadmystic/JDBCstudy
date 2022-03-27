package com.no2.preparestatement;
/*
 * 
 * order表的
 */

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.no3.bean.Customers;
import com.no3.bean.Order;
import com.no3.util.JDBCutils;

public class orderforquery {
	/*
	 * 针对表的字段名和类的属性名不一致
	 * 
	 */
	@Test
	public void testqueryfororder() throws Exception {
		String sql1 = "select order_id orderid,order_name ordername,order_date orderdate from `order` where order_id=?";
		Order order = queryForOrder(sql1, 1);
		System.out.println(order);
	}
	
	
	/*
	 * 通用查询
	 * 
	 */
	public Order queryForOrder(String sql,Object ...args) throws Exception {
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
				Order order=new Order();
				//处理结果集一行数据中的每一个列
				for(int i=0;i<columnCount;i++) {
					//获取列值
					Object columeValue = rs.getObject(i+1);
					//获取每个列的列名getColumnName() --不推荐--
					//获取每个列的别名getColumnLabel()--一般都可用--
					//String columnName = rsmd.getColumnName(i+1);
					String columnlabel = rsmd.getColumnLabel(i+1);
					//给cust对象指定的某个属性，赋值为columevalue,通过反射
					Field field = Order.class.getDeclaredField(columnlabel);
					field.setAccessible(true);
					field.set(order, columeValue);
					
				}
				return order;
			}
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps, rs);
		}
		
		return null;
	}
	
	
	
	
	/*
	 * 查询
	 */
	@Test
	public void testquery1(){
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			conn = JDBCutils.getConnection();
			String sql="select order_id,order_name,order_date from `order` where order_id=?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				int id=(int)rs.getObject(1);
				String name=(String)rs.getObject(2);
				Date date=(Date)rs.getObject(3);
				
				Order order=new Order(id,name,date);
				System.out.println(order);
				
			}
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
		JDBCutils.closeResource(conn, ps, rs);
		}
	}
}
