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
		Customers customers = queryForCustomers(sql1, "�ܽ���");
		System.out.println(customers);
	}
	/*
	 * ͨ�ò�ѯCustomers
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
			//��ȡ�����Ԫ����
			ResultSetMetaData rsmd=rs.getMetaData();
			//ͨ��Rsmd��ȡ����
			int columnCount=rsmd.getColumnCount();
			
			if(rs.next()) {
				Customers cust=new Customers();
				//��������һ�������е�ÿһ����
				for(int i=0;i<columnCount;i++) {
					//��ȡ��ֵ
					Object columeValue = rs.getObject(i+1);
					//��ȡÿ���е�����
					String columnName = rsmd.getColumnName(i+1);
					
					//��cust����ָ����ĳ�����ԣ���ֵΪcolumevalue,ͨ������
					Field field = Customers.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(cust, columeValue);
					
				}
				return cust;
			}
		}catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
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
		//ִ��,������
		ResultSet resultSet=null;
		try {
			conn = JDBCutils.getConnection();
			String sql="select id,name,email,birth from customers where id=?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			resultSet = ps.executeQuery();
			//��������
			//next�жϽ��������һ���Ƿ��У�true����ָ�����ƣ�false
			if(resultSet.next()) {
				int id=resultSet.getInt(1);
				String name=resultSet.getString(2);
				String email=resultSet.getString(3);
				Date birth=resultSet.getDate(4);
				
				//Object[]data=new Object[] {id,name,email,birth};
				
				//��װ����
				Customers customers=new Customers(id, name, email, birth);
				System.out.println(customers);
			}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
		//�ر�
		JDBCutils.closeResource(conn, ps, resultSet);
		}
		
	}

}
