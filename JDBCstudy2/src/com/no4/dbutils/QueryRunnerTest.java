package com.no4.dbutils;
/*
 * DBUtils是开源JDBC工具类，封装了CRUD
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.Test;

import com.no2.bean.Customers;
import com.no4.util.JDBCutils;

public class QueryRunnerTest {
	
	//测试插入
	@Test
	public void testInesert(){
		Connection conn=null;
		try {
			QueryRunner runner=new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="insert into customers(name,email,birth)values(?,?,?)";
			int insertcount = runner.update(conn, sql, "蔡徐坤","caixukun@162.com","1997-07-05");
			System.out.println("添加了"+insertcount);
		}  catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	
	//测试查询
	/*
	 * BeanHander:是ResultSetHandler接口的实现类，封装表中的一条记录
	 */
	@Test
	public void testQuery1(){
		Connection conn=null;
		try {
			QueryRunner runner=new QueryRunner();
			conn=JDBCutils.getConnection();
			String sql="select id,name,email,birth from customers where id=?";
			BeanHandler<Customers> handler=new BeanHandler<>(Customers.class);
			Customers customers = runner.query(conn, sql,handler,23);
			System.out.println(customers);
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);	
		}
	}
	
	
	/*
	 * BeanListHander:是ResultSetHandler接口的实现类，封装表中的多条记录构成的集合
	 */
	@Test
	public void testQuery2(){
		Connection conn=null;
		try {
			QueryRunner runner=new QueryRunner();
			conn=JDBCutils.getConnection();
			String sql="select id,name,email,birth from customers where id<?";
			
			BeanListHandler<Customers> handler=new BeanListHandler<>(Customers.class);
			
			List<Customers> list = runner.query(conn, sql,handler,23);
			list.forEach(System.out::println);
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	
	
	
		/*
		 * MapHander:是ResultSetHandler接口的实现类，对应表中的一条记录
		 * 将字段及相应字段的值对应map中的key和value
		 */
		@Test
		public void testQuery3(){
			Connection conn=null;
			try {
				QueryRunner runner=new QueryRunner();
				conn=JDBCutils.getConnection();
				String sql="select id,name,email,birth from customers where id=?";
				MapHandler handler=new MapHandler();
				Map<String, Object> map = runner.query(conn, sql,handler,23);
				System.out.println(map);
			}catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
		
		
		/*
		 * MapListHander:是ResultSetHandler接口的实现类，对应表中的多条记录
		 * 将字段及相应字段的值对应map中的key和value,将这些map添加到List
		 */
		@Test
		public void testQuery4(){
			Connection conn=null;
			try {
				QueryRunner runner=new QueryRunner();
				conn=JDBCutils.getConnection();
				String sql="select id,name,email,birth from customers where id<?";
				MapListHandler handler=new MapListHandler();
				List<Map<String, Object>> list = runner.query(conn, sql,handler,23);
				list.forEach(System.out::println);
			}catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
		
		
		
		/*
		 * ScalarHandler查询特殊值
		 */
		@Test
		public void testQuery5(){
			Connection conn=null;
			try {
				QueryRunner runner=new QueryRunner();
				conn=JDBCutils.getConnection();
				String sql="select count(*) from customers";
				
				ScalarHandler handler = new ScalarHandler();
				
				Long count =(Long)runner.query(conn, sql,handler);
				
				System.out.println(count);
			}catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
		
		/*
		 * 自定义ResultSetHandler的实现类
		 */
		@Test
		public void testQuery7(){
			Connection conn=null;
			try {
				QueryRunner runner=new QueryRunner();
				conn=JDBCutils.getConnection();
				
				String sql="select id,name,email,birth from customers where id=?";
				ResultSetHandler<Customers> handler=new ResultSetHandler<Customers>() {

					@Override
					public Customers handle(ResultSet rs) throws SQLException {
						// TODO 自动生成的方法存根
						//System.out.println("handle");
						//return null;
						
						//return new Customers(12,"成龙","Jackey@123.com",new Date(1221333L));
						if(rs.next()) {
							int id=rs.getInt("id");
							String name=rs.getString("name");
							String email=rs.getString("email");
							Date birth=rs.getDate("birth");
							Customers customers=new Customers(id,name,email,birth);
							return customers;
						}
						return null;
					}
				};
				
				Customers customers = runner.query(conn, sql,handler,23);
				System.out.println(customers);
			}catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
}
