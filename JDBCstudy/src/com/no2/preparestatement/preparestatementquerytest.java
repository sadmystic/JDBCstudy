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
 * ʹ��preparestatementʵ������ڲ�ͬ���ͨ�ò�ѯ
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
			//��ȡ�����Ԫ����
			ResultSetMetaData rsmd=rs.getMetaData();
			//ͨ��Rsmd��ȡ����
			int columnCount=rsmd.getColumnCount();
			//�������϶���
			ArrayList<T> list = new ArrayList<T>();
			while(rs.next()) {
				T t=clazz.newInstance();
				//��������һ�������е�ÿһ����;��t����ָ�������Ը�ֵ
				for(int i=0;i<columnCount;i++) {
					//��ȡ��ֵ
					Object columeValue = rs.getObject(i+1);
					//��ȡÿ���е�����getColumnName() --���Ƽ�--
					//��ȡÿ���еı���getColumnLabel()--һ�㶼����--
					//String columnName = rsmd.getColumnName(i+1);
					String columnlabel = rsmd.getColumnLabel(i+1);
					//��t����ָ����ĳ�����ԣ���ֵΪcolumevalue,ͨ������
					Field field = clazz.getDeclaredField(columnlabel);
					field.setAccessible(true);
					field.set(t, columeValue);
					
				}
				list.add(t);
			}
			return list;
		}catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
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
	 * ��Բ�ͬ���һ����¼;
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
			//��ȡ�����Ԫ����
			ResultSetMetaData rsmd=rs.getMetaData();
			//ͨ��Rsmd��ȡ����
			int columnCount=rsmd.getColumnCount();
			
			if(rs.next()) {
				T t=clazz.newInstance();
				//��������һ�������е�ÿһ����
				for(int i=0;i<columnCount;i++) {
					//��ȡ��ֵ
					Object columeValue = rs.getObject(i+1);
					//��ȡÿ���е�����getColumnName() --���Ƽ�--
					//��ȡÿ���еı���getColumnLabel()--һ�㶼����--
					//String columnName = rsmd.getColumnName(i+1);
					String columnlabel = rsmd.getColumnLabel(i+1);
					//��t����ָ����ĳ�����ԣ���ֵΪcolumevalue,ͨ������
					Field field = clazz.getDeclaredField(columnlabel);
					field.setAccessible(true);
					field.set(t, columeValue);
					
				}
				return t;
			}
		}catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps, rs);
		}
		
		return null;
		
		
	}

}
