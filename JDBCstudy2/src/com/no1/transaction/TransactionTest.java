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
	 * 针对于数据表user—_table来说：
	 * AA用户给BB用户转账100
	 * update user_table set balance = balance-100 where user='AA';
	 * update user_table set balance = balance+100 where user='BB';
	 */
	@Test
	public void testupdate() {
		String sql1="update user_table set balance = balance-100 where user=?";
		update(sql1, "AA");
		
		//模拟网络异常
		System.out.println(10/0);
		
		String sql2="update user_table set balance = balance+100 where user=?";
		update(sql2, "BB");
		
		System.out.println("转账成功");
		
	}
	
	
	/*
	 * 未考虑事务操作：执行语句后自动关闭了连接
	 */
	//通用增删改-----version 1.0
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
	
	@Test
	public void testupdatewithTX(){
		Connection conn=null;
		try {
			conn = JDBCutils.getConnection();
			
			//1.设置取消数据自动提交
			conn.setAutoCommit(false);
			
			String sql1="update user_table set balance = balance-100 where user=?";
			update(conn,sql1, "AA");
			
			//模拟网络异常
			System.out.println(10/0);
			
			String sql2="update user_table set balance = balance+100 where user=?";
			update(conn,sql2, "BB");
			
			System.out.println("转账成功");
			
			//2.提交数据
			conn.commit();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			//3.回滚数据
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}finally {
		
		JDBCutils.closeResource(conn, null);
		}
	}
	
	
	
	/*
	 * 考虑事务操作
	 */
	//通用增删改-----version 2.0
	public int update(Connection conn,String sql,Object ...args)  {
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
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
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
		
		//获取当前连接的隔离级别
		System.out.println(conn.getTransactionIsolation());
		//设置数据库的隔离级别
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		//取消自动提交数据
		conn.setAutoCommit(false);
		
		String sql="select user,password,balance from user_table where user = ?";
		User user = getInstance(conn, User.class, sql,"CC");
		System.out.println(user);
		
	}
	
	@Test
	public void testTransactionUpdate() throws Exception {
		Connection conn = JDBCutils.getConnection();
		
		//取消自动提交数据
		conn.setAutoCommit(false);
		
		String sql="update user_table set balance = ? where user = ?";
		update1(conn, sql,5000,"CC");
		
		Thread.sleep(15000);
		System.out.println("修改结束，并且未提交，回滚");
	}
	
	
	//通用的查询操作，用于返回数据表中的一条记录(version 2.0)考虑事务
	public <T>T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args) {
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
	
	//和上面那个update不同的是自动提交
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
	

	
}
