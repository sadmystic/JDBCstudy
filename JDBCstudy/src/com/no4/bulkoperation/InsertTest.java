package com.no4.bulkoperation;
/*
 * �������ݲ���
 * 
 * update\delete�����������������Ч��
 * ��ʱ������������Ҫָ������������ 
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
			System.out.println("����ʱ��:"+(end-start));
		}  catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		finally {
		JDBCutils.closeResource(conn, ps);
	
		}
	}
	
	/*
	 * ����������ʽ��:
	 * 1.addBatch(),excuteBatch(),clearBatch()
	 * 2.mysql������Ĭ�Ϲر�������,������Ҫͨ��һ����������mysql����������֧��
	 * ?rewriteBatchedStatements=true д�������ļ���url����
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
				//1."��"sql
				ps.addBatch();
				
				if(i%500==0) {
					//2.ִ��
					ps.executeBatch();
					//3.���Batch
					ps.clearBatch();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("����ʱ��:"+(end-start));
		}  catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		finally {
		JDBCutils.closeResource(conn, ps);
	
		}
	}
	

	/*
	 * ����������ʽ��:
	 * �������Զ��ύ����
	 * �ֶ��ύ
	 * 
	 */
	
	@Test
	public void testInsert3(){
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutils.getConnection();
			
			//���ò��������Զ��ύ����
			conn.setAutoCommit(false);
			
			String sql="insert into goods(name) values(?)";
			ps = conn.prepareStatement(sql);
			for(int i=1;i<=1000000;i++) {
				ps.setObject(1, "name_"+i);
				//1."��"sql
				ps.addBatch();
				
				if(i%500==0) {
					//2.ִ��
					ps.executeBatch();
					//3.���Batch
					ps.clearBatch();
				}
			}
			
			//�ύ����
			conn.commit();
			
			long end = System.currentTimeMillis();
			System.out.println("����ʱ��:"+(end-start));
		}  catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		finally {
		JDBCutils.closeResource(conn, ps);
	
		}
	}
}
