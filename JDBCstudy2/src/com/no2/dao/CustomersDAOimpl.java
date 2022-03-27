package com.no2.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.no2.bean.Customers;

public class CustomersDAOimpl extends BaseDAO implements CustomerDAO{

	@Override
	public void insert(Connection conn, Customers cust) {
		String sql="insert into customers(name,email,birth)values(?,?,?)";
		update1(conn, sql, cust.getName(),cust.getEmail(),cust.getBirth());;
	}

	@Override
	public void deleteById(Connection conn, int id) {
		String sql="delete from customers where id=?";
		update1(conn, sql, id);
	}

	@Override
	public void update(Connection conn, Customers cust) {
		String sql="update customers set name=?,email=?,birth=? where id=?";
		update1(conn, sql,cust.getName(),cust.getEmail(),cust.getBirth(),cust.getId());
	}

	@Override
	public Customers getCustomerById(Connection conn, int id) {
		String sql="select id,name,email,birth from customers where id=?";
		Customers customers = getInstance(conn, Customers.class, sql, id);
		return customers;
	}

	@Override
	public List<Customers> getAll(Connection conn) {
		String sql="select id,name,email,birth from customers";
		List<Customers> list = getForlist(conn, Customers.class, sql);
		return list;
	}

	@Override
	public Long getCount(Connection conn) {
		String sql="select count(*) from customers";
		return getValue(conn, sql);
	}

	@Override
	public Date getMaxBirth(Connection conn) {
		String sql="select max(birth) from customers";
		return getValue(conn, sql);
	}
	
}
