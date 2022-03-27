package com.no4.dbutils;
/*
 * DBUtils�ǿ�ԴJDBC�����࣬��װ��CRUD
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
	
	//���Բ���
	@Test
	public void testInesert(){
		Connection conn=null;
		try {
			QueryRunner runner=new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="insert into customers(name,email,birth)values(?,?,?)";
			int insertcount = runner.update(conn, sql, "������","caixukun@162.com","1997-07-05");
			System.out.println("�����"+insertcount);
		}  catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	
	//���Բ�ѯ
	/*
	 * BeanHander:��ResultSetHandler�ӿڵ�ʵ���࣬��װ���е�һ����¼
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);	
		}
	}
	
	
	/*
	 * BeanListHander:��ResultSetHandler�ӿڵ�ʵ���࣬��װ���еĶ�����¼���ɵļ���
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}
	
	
	
		/*
		 * MapHander:��ResultSetHandler�ӿڵ�ʵ���࣬��Ӧ���е�һ����¼
		 * ���ֶμ���Ӧ�ֶε�ֵ��Ӧmap�е�key��value
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
		
		
		/*
		 * MapListHander:��ResultSetHandler�ӿڵ�ʵ���࣬��Ӧ���еĶ�����¼
		 * ���ֶμ���Ӧ�ֶε�ֵ��Ӧmap�е�key��value,����Щmap��ӵ�List
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
		
		
		
		/*
		 * ScalarHandler��ѯ����ֵ
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
		
		/*
		 * �Զ���ResultSetHandler��ʵ����
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
						// TODO �Զ����ɵķ������
						//System.out.println("handle");
						//return null;
						
						//return new Customers(12,"����","Jackey@123.com",new Date(1221333L));
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(conn, null);	
			}
		}
}
