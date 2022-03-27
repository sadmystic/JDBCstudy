package com.no2.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.no2.bean.Customers;

/*
 * 此接口用于规范针对于customers表的常用操作
 */
public interface CustomerDAO {
	/*
	 * 将cust对象添加到数据库中
	 */
	void insert(Connection conn,Customers cust);
	/*
	 * 针对指定的id，删除表中的一条记录
	 */
	void deleteById(Connection conn,int id);
	/*
	 * 针对内存中的cust对象，修改表中的一条记录
	 */
	void update(Connection conn,Customers cust);
	/*
	 * 针对指定的id查询得到对应的Customer对象
	 */
	Customers getCustomerById(Connection conn,int id);
	/*
	 * 查询表中的所有记录构成的集合
	 */
	List<Customers> getAll(Connection conn);
	/*
	 * 返回数据表中的数据的条目数
	 */
	Long getCount(Connection conn);
	/*
	 * 返回数据表中的最大的生日
	 */
	Date getMaxBirth(Connection conn);
}
