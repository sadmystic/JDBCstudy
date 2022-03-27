package com.no4.bulkoperation;
/*
 * 批量数据操作
 * 
 * update\delete本身具有批量操作的效果
 * 此时批量操作，主要指的是批量插入 
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.no3.util.JDBCutils;

public class InsertTest {
	
	@Test
	public void testInsert1(){
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutils.getConnection();
			String sql="insert into goods(name) values(?)";
			ps = conn.prepareStatement(sql);
			for(int i=1;i<=20000;i++) {
				ps.setObject(1, "name_"+i);
				
				ps.execute();
			}
			long end = System.currentTimeMillis();
			System.out.println("花费时间:"+(end-start));
		}  catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		finally {
		JDBCutils.closeResource(conn, ps);
	
		}
	}
	
	/*
	 * 批量操作方式二:
	 * 1.addBatch(),excuteBatch(),clearBatch()
	 * 2.mysql服务器默认关闭批处理,我们需要通过一个参数，让mysql开启批处理支持
	 * ?rewriteBatchedStatements=true 写在配置文件的url后面
	 * 3.
	 */
	
	@Test
	public void testInsert2(){
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutils.getConnection();
			String sql="insert into goods(name) values(?)";
			ps = conn.prepareStatement(sql);
			for(int i=1;i<=1000000;i++) {
				ps.setObject(1, "name_"+i);
				//1."攒"sql
				ps.addBatch();
				
				if(i%500==0) {
					//2.执行
					ps.executeBatch();
					//3.清空Batch
					ps.clearBatch();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("花费时间:"+(end-start));
		}  catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		finally {
		JDBCutils.closeResource(conn, ps);
	
		}
	}
	

	/*
	 * 批量操作方式三:
	 * 不允许自动提交数据
	 * 手动提交
	 * 
	 */
	
	@Test
	public void testInsert3(){
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutils.getConnection();
			
			//设置不能允许自动提交数据
			conn.setAutoCommit(false);
			
			String sql="insert into goods(name) values(?)";
			ps = conn.prepareStatement(sql);
			for(int i=1;i<=1000000;i++) {
				ps.setObject(1, "name_"+i);
				//1."攒"sql
				ps.addBatch();
				
				if(i%500==0) {
					//2.执行
					ps.executeBatch();
					//3.清空Batch
					ps.clearBatch();
				}
			}
			
			//提交数据
			conn.commit();
			
			long end = System.currentTimeMillis();
			System.out.println("花费时间:"+(end-start));
		}  catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		finally {
		JDBCutils.closeResource(conn, ps);
	
		}
	}
}
