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
 * 封装针对于数据表的通用操作
 * DAO: data(base) access object
 */
public abstract class BaseDAO<T> {
	
	private Class<T> clazz=null;
		
	//public BaseDAO() {
	//	
	//}
	
	{
		//获取当前BaseDAO的子类继承父类中的泛型
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramaType=(ParameterizedType) genericSuperclass;
		
		Type[] typeArguments = paramaType.getActualTypeArguments();//获取了父类的泛型参数
		clazz =(Class<T>) typeArguments[0];//泛型的第一个参数
	}
	
	
	//通用的增删改操作(version 2.0)考虑事务
	public int update1(Connection conn,String sql,Object ...args)  {
		//sql占位符个数与可变形参长度相同
		PreparedStatement ps=null;
		try {
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
		}
		finally {
			//修改回自动提交
			
			JDBCutils.closeResource(null, ps);
		}
		return 0;
	}
	
	
	//通用的查询操作，用于返回数据表中的一条记录(version 2.0)考虑事务
	public T getInstance(Connection conn,String sql,Object ...args) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
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
			JDBCutils.closeResource(null, ps, rs);
		}
		
		return null;
		
	}
	
	//通用的查询操作，用于返回数据表中的多条记录构成的集合(version 2.0)考虑事务
	public  List<T> getForlist(Connection conn,String sql,Object ...args) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
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
			JDBCutils.closeResource(null, ps, rs);
		}
		
		return null;
	}
	
	//用于查询特殊值的方法
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		finally {
			JDBCutils.closeResource(null, ps,rs);
		}
		return null;
	}
}
