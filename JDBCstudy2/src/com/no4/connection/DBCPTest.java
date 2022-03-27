package com.no4.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

public class DBCPTest {
	/*
	 * ����DBCP�����ݿ����ӳؼ���
	 * 
	 */
	//��ʽһ:���Ƽ�
	@Test
	public void testGetConnection() throws Exception {
		//����DBCP���ӳ�
		BasicDataSource source=new BasicDataSource(); 
		
		//���û�����Ϣ
		source.setDriverClassName("com.mysql.cj.jdbc.Driver");
		source.setUrl("jdbc:mysql:///test");
		source.setUsername("root");
		source.setPassword("123456");
		
		//�������ݿ����ӳع�����������
		source.setInitialSize(10);
		source.setMaxActive(10);
		
		Connection conn=source.getConnection();
		System.out.println(conn);
	}
	
	//��ʽ��:�����ļ���
	@Test
	public void testGetConnection1() throws Exception {
		Properties pros=new Properties();
		//��ʽһ
		//InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		//��ʽ��
		FileInputStream is=new FileInputStream(new File("src/dbcp.properties"));
		pros.load(is);
		DataSource source = BasicDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
}
