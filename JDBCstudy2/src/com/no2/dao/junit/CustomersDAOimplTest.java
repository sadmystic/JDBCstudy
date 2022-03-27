package com.no2.dao.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.no1.util.JDBCutils;
import com.no2.bean.Customers;
import com.no2.dao.CustomersDAOimpl;

class CustomersDAOimplTest {

	CustomersDAOimpl dao=new CustomersDAOimpl(); 
	
	@Test
	void testInsert() {
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			Customers cust = new Customers(1,"于小飞","xiaofei@126.com",new Date(43534644));
			dao.insert(conn, cust);
			System.out.println("添加成功");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
		
		
	}

	@Test
	void testDeleteById() {
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			dao.deleteById(conn, 13);
			
			System.out.println("删除成功");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}

	@Test
	void testUpdate() {
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			Customers cust = new Customers(18,"贝多芬","123@qq.com",new Date(43534644));
			dao.update(conn,cust);
			
			System.out.println("修改成功");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}

	@Test
	void testGetCustomerById() {
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			
			Customers cust=dao.getCustomerById(conn, 19);
			
			
			System.out.println(cust);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}

	@Test
	void testGetAll() {
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			
			List<Customers>list=dao.getAll(conn);
			list.forEach(System.out::println);
			
			System.out.println("");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}

	@Test
	void testGetCount() {
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			
			Long count=dao.getCount(conn); 
			
			System.out.println("表中的记录数为:"+count);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}

	@Test
	void testGetMaxBirth() {
		Connection conn = null;
		try {
			conn = JDBCutils.getConnection();
			
			Date maxBirth=dao.getMaxBirth(conn);
			
			System.out.println("最大生日:"+maxBirth);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}

}
