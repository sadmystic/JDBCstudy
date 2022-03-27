package com.no2.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.no2.bean.Customers;

/*
 * �˽ӿ����ڹ淶�����customers��ĳ��ò���
 */
public interface CustomerDAO {
	/*
	 * ��cust������ӵ����ݿ���
	 */
	void insert(Connection conn,Customers cust);
	/*
	 * ���ָ����id��ɾ�����е�һ����¼
	 */
	void deleteById(Connection conn,int id);
	/*
	 * ����ڴ��е�cust�����޸ı��е�һ����¼
	 */
	void update(Connection conn,Customers cust);
	/*
	 * ���ָ����id��ѯ�õ���Ӧ��Customer����
	 */
	Customers getCustomerById(Connection conn,int id);
	/*
	 * ��ѯ���е����м�¼���ɵļ���
	 */
	List<Customers> getAll(Connection conn);
	/*
	 * �������ݱ��е����ݵ���Ŀ��
	 */
	Long getCount(Connection conn);
	/*
	 * �������ݱ��е���������
	 */
	Date getMaxBirth(Connection conn);
}
