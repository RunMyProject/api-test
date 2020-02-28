package com.esabatini.model;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class OrderParams {
	
	private String title;
	private List<Product> products;
	private User user;
	
	private static OrderParams build;
	
	@Override
	public String toString() {
	    return String.format("OrderParams [title='%s', products='%s', user='%s']",
	    		title, 
	    		products.toArray().toString(), 
	    		user);
	}

	public static OrderParams builder() {
		build = new OrderParams();
		return build;
    }
	
	public OrderParams build() {
		OrderParams clone = null;
		try {
			clone = (OrderParams) BeanUtils.cloneBean(build);
		} catch (InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	public OrderParams title(String title) {
		build.title=title;
		return build;
	}

	public OrderParams products(List<Product> products) {
		build.products=products;
		return build;
	}

	public OrderParams user(User user) {
		build.user=user;
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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}