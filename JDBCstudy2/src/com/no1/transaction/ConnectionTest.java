package com.no1.transaction;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import com.no1.util.JDBCutils;

public class ConnectionTest {
 
	@Test
	public void TestGetConnectionTest() throws Exception {
		Connection connection = JDBCutils.getConnection();
		
	}
}
