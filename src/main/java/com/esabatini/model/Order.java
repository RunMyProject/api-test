package com.esabatini.model;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.annotation.Id;

public class Order {
	
	@Id
	private String id;
	private String title;
	private Double amount;
	private String date;
	private List<Product> products;
	private String id_user;
	private Status status;
	private String closedAt;

	private static Order build;
	
	@Override
	public String toString() {
	    return String.format("Order [id=%s, title='%s', amount='%s'"
	    		+ ", date='%s', products='%s', id_user='%s', status='%s', closedAt='%s']",
	    		id, title, amount, date, products, id_user, status, closedAt);
	}

	public static Order builder() {
		build = new Order();
		return build;
    }
	
	public Order build() {
		Order clone = null;
		try {
			clone = (Order) BeanUtils.cloneBean(build);
		} catch (InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	public Order id(String id) {
		build.id=id;
		return build;
	}

	public Order title(String title) {
		build.title=title;
		return build;
	}

	public Order amount(Double amount) {
		build.amount=amount;
		return build;
	}

	public Order date(String date) {
		build.date=date;
		return build;
	}

	public Order products(List<Product> products) {
		build.products=products;
		return build;
	}

	public Order id_user(String id_user) {
		build.id_user=id_user;
		return build;
	}

	public Order status(Status status) {
		build.status=status;
		return build;
	}

	public Order closedAt(String closedAt) {
		build.closedAt=closedAt;
		return build;
	}	

	// get / set
	//	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getId_user() {
		return id_user;
	}

	public void setId_user(String id_user) {
		this.id_user = id_user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(String closedAt) {
		this.closedAt = closedAt;
	}

}
