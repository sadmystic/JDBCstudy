package com.no1.transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.no1.util.JDBCutils;

public class TransactionTest {
	
	/*
	 * ��������ݱ�user��_table��˵��
	 * AA�û���BB�û�ת��100
	 * update user_table set balance = balance-100 where user='AA';
	 * update user_table set balance = balance+100 where user='BB';
	 */
	@Test
	public void testupdate() {
		String sql1="update user_table set balance = balance-100 where user=?";
		update(sql1, "AA");
		
		//ģ�������쳣
		System.out.println(10/0);
		
		String sql2="update user_table set balance = balance+100 where user=?";
		update(sql2, "BB");
		
		System.out.println("ת�˳ɹ�");
		
	}
	
	
	/*
	 * δ�������������ִ�������Զ��ر�������
	 */
	//ͨ����ɾ��-----version 1.0
	public int update(String sql,Object ...args)  {
		//sqlռλ��������ɱ��βγ�����ͬ
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			conn = JDBCutils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				ps.setObject(i+1, args[i]);
			}
			 /*
			  * ���ִ�е��ǽ����ѯ������true���������ɾ�ģ�����false
			  * ps.execute();
			  */
			return ps.executeUpdate();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps);
		}
		return 0;
	}
	
	@Test
	public void testupdatewithTX(){
		Connection conn=null;
		try {
			conn = JDBCutils.getConnection();
			
			//1.����ȡ�������Զ��ύ
			conn.setAutoCommit(false);
			
			String sql1="update user_table set balance = balance-100 where user=?";
			update(conn,sql1, "AA");
			
			//ģ�������쳣
			System.out.println(10/0);
			
			String sql2="update user_table set balance = balance+100 where user=?";
			update(conn,sql2, "BB");
			
			System.out.println("ת�˳ɹ�");
			
			//2.�ύ����
			conn.commit();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			//3.�ع�����
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}finally {
		
		JDBCutils.closeResource(conn, null);
		}
	}
	
	
	
	/*
	 * �����������
	 */
	//ͨ����ɾ��-----version 2.0
	public int update(Connection conn,String sql,Object ...args)  {
		//sqlռλ��������ɱ��βγ�����ͬ
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				ps.setObject(i+1, args[i]);
			}
			 /*
			  * ���ִ�е��ǽ����ѯ������true���������ɾ�ģ�����false
			  * ps.execute();
			  */
			return ps.executeUpdate();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		finally {
			//�޸Ļ��Զ��ύ
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			
			JDBCutils.closeResource(null, ps);
		}
		return 0;
	}
	
	
	//*****************************************************************
	@Test
	public void testTransactionSelect() throws Exception {
		Connection conn = JDBCutils.getConnection();
		
		//��ȡ��ǰ���ӵĸ��뼶��
		System.out.println(conn.getTransactionIsolation());
		//�������ݿ�ĸ��뼶��
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		//ȡ���Զ��ύ����
		conn.setAutoCommit(false);
		
		String sql="select user,password,balance from user_table where user = ?";
		User user = getInstance(conn, User.class, sql,"CC");
		System.out.println(user);
		
	}
	
	@Test
	public void testTransactionUpdate() throws Exception {
		Connection conn = JDBCutils.getConnection();
		
		//ȡ���Զ��ύ����
		conn.setAutoCommit(false);
		
		String sql="update user_table set balance = ? where user = ?";
		update1(conn, sql,5000,"CC");
		
		Thread.sleep(15000);
		System.out.println("�޸Ľ���������δ�ύ���ع�");
	}
	
	
	//ͨ�õĲ�ѯ���������ڷ������ݱ��е�һ����¼(version 2.0)��������
	public <T>T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
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
			JDBCutils.closeResource(null, ps, rs);
		}
		
		return null;
		
		
	}
	
	//�������Ǹ�update��ͬ�����Զ��ύ
	public int update1(Connection conn,String sql,Object ...args)  {
		//sqlռλ��������ɱ��βγ�����ͬ
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				ps.setObject(i+1, args[i]);
			}
			 /*
			  * ���ִ�е��ǽ����ѯ������true���������ɾ�ģ�����false
			  * ps.execute();
			  */
			return ps.executeUpdate();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		finally {
			//�޸Ļ��Զ��ύ
			
			JDBCutils.closeResource(null, ps);
		}
		return 0;
	}
	

	
}
