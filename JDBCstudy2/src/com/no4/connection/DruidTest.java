package com.no4.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DruidTest {

	@Test
	public void getConnection() throws Exception {
		Properties pros=new Properties();
		
		InputStream is = ClassLoader.getSystemClassLoader().getSystemResourceAsStream("druid.properties");
		
		pros.load(is);
		
		DataSource source = DruidDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		System.out.println(conn);
		
	}
}
