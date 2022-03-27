package com.no3.bean;

import java.util.Date;

public class Order {

	private int orderid;
	private String ordername;
	private Date orderdate;	
	public Order() {
		super();
	}
	public Order(int orderid, String ordername, Date orderdate) {
		super();
		this.orderid = orderid;
		this.ordername = ordername;
		this.orderdate = orderdate;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public String getOrdername() {
		return ordername;
	}
	public void setOrdername(String ordername) {
		this.ordername = ordername;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	@Override
	public String toString() {
		return "Order [orderid=" + orderid + ", ordername=" + ordername + ", orderdate=" + orderdate + "]";
	}

}
