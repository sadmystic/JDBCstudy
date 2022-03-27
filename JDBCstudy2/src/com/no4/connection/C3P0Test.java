package com.no4.connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import org.junit.jupiter.api.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class C3P0Test {
	//��ʽһ
	@Test
	public void testGetConnection() throws Exception {
		//��ȡc3p0���ݿ����ӳ�
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" );           
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
		cpds.setUser("root");                                  
		cpds.setPassword("123456");
		//ͨ��������صĲ��������ݿ����ӳع���
		//��ʼʱ���ݿ����ӳص�������
		cpds.setInitialPoolSize(10);
		
		Connection conn=cpds.getConnection();
		System.out.println(conn);
		
		//����c3p0���ӳ�
		//DataSources.destroy(cpds);
	}
	
	//��ʽ��  �����ļ�
	@Test
	public void testGetConnection1() throws Exception {
		ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
		Connection conn = cpds.getConnection();
		System.out.println(conn);
	}
}
