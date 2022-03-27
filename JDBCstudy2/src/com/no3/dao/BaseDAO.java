package com.no3.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.no1.util.JDBCutils;


/*
 * ��װ��������ݱ��ͨ�ò���
 * DAO: data(base) access object
 */
public abstract class BaseDAO<T> {
	
	private Class<T> clazz=null;
		
	//public BaseDAO() {
	//	
	//}
	
	{
		//��ȡ��ǰBaseDAO������̳и����еķ���
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramaType=(ParameterizedType) genericSuperclass;
		
		Type[] typeArguments = paramaType.getActualTypeArguments();//��ȡ�˸���ķ��Ͳ���
		clazz =(Class<T>) typeArguments[0];//���͵ĵ�һ������
	}
	
	
	//ͨ�õ���ɾ�Ĳ���(version 2.0)��������
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
	
	
	//ͨ�õĲ�ѯ���������ڷ������ݱ��е�һ����¼(version 2.0)��������
	public T getInstance(Connection conn,String sql,Object ...args) {
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
	
	//ͨ�õĲ�ѯ���������ڷ������ݱ��еĶ�����¼���ɵļ���(version 2.0)��������
	public  List<T> getForlist(Connection conn,String sql,Object ...args) {
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
			JDBCutils.closeResource(null, ps, rs);
		}
		
		return null;
	}
	
	//���ڲ�ѯ����ֵ�ķ���
	public <E>E getValue(Connection conn,String sql,Object...args){
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i=0;i<args.length;i++) {
				ps.setObject(i+1, args[i]);
			}
			rs = ps.executeQuery();
			if(rs.next()) {
				return (E) rs.getObject(1);	
			}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		finally {
			JDBCutils.closeResource(null, ps,rs);
		}
		return null;
	}
}
