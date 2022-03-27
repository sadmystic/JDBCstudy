package com.no2.preparestatement;
/*
 * 
 * order���
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
	 * ��Ա���ֶ����������������һ��
	 * 
	 */
	@Test
	public void testqueryfororder() throws Exception {
		String sql1 = "select order_id orderid,order_name ordername,order_date orderdate from `order` where order_id=?";
		Order order = queryForOrder(sql1, 1);
		System.out.println(order);
	}
	
	
	/*
	 * ͨ�ò�ѯ
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
			//��ȡ�����Ԫ����
			ResultSetMetaData rsmd=rs.getMetaData();
			//ͨ��Rsmd��ȡ����
			int columnCount=rsmd.getColumnCount();
			
			if(rs.next()) {
				Order order=new Order();
				//��������һ�������е�ÿһ����
				for(int i=0;i<columnCount;i++) {
					//��ȡ��ֵ
					Object columeValue = rs.getObject(i+1);
					//��ȡÿ���е�����getColumnName() --���Ƽ�--
					//��ȡÿ���еı���getColumnLabel()--һ�㶼����--
					//String columnName = rsmd.getColumnName(i+1);
					String columnlabel = rsmd.getColumnLabel(i+1);
					//��cust����ָ����ĳ�����ԣ���ֵΪcolumevalue,ͨ������
					Field field = Order.class.getDeclaredField(columnlabel);
					field.setAccessible(true);
					field.set(order, columeValue);
					
				}
				return order;
			}
		}catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps, rs);
		}
		
		return null;
	}
	
	
	
	
	/*
	 * ��ѯ
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
		JDBCutils.closeResource(conn, ps, rs);
		}
	}
}
