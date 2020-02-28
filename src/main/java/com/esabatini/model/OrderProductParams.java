package com.esabatini.model;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class OrderProductParams {
	
	private Order order;
	private List<Product> products;
	
	private static OrderProductParams build;
	
	@Override
	public String toString() {
	    return String.format("OrderProductParams [order='%s', products='%s']",
	    		order,
	    		products.toArray().toString());
	}

	public static OrderProductParams builder() {
		build = new OrderProductParams();
		return build;
    }
	
	public OrderProductParams build() {
		OrderProductParams clone = null;
		try {
			clone = (OrderProductParams) BeanUtils.cloneBean(build);
		} catch (InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	public OrderProductParams order(Order order) {
		build.order=order;
		return build;
	}

	public OrderProductParams products(List<Product> products) {
		build.products=products;
		return build;
	}

	// get / set
	//	
	public Order getOrder() {
		return order;
	}	
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}