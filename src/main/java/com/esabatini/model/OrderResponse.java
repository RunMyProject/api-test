package com.esabatini.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class OrderResponse {
	
	private ControlCode kook;
	private String message;
	private Order order;

	private static OrderResponse build;
	
	@Override
	public String toString() {
	    return String.format("OrderResponse [kook=%s, message='%s', order='%s']",
	    		kook, message, order);
	}

	public static OrderResponse builder() {
		build = new OrderResponse();
		return build;
    }
	
	public OrderResponse build() {
		OrderResponse clone = null;
		try {
			clone = (OrderResponse) BeanUtils.cloneBean(build);
		} catch (InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	public OrderResponse kook(ControlCode kook) {
		build.kook=kook;
		return build;
	}

	public OrderResponse message(String message) {
		build.message=message;
		return build;
	}
	
	public OrderResponse order(Order order) {
		build.order=order;
		return build;
	}
	
	public OrderResponse changeMessage(String message) {
		this.message=message;
		return this;
	}

	// get / set
	//	
	public ControlCode getKook() {
		return kook;
	}
	public void setKook(ControlCode kook) {
		this.kook = kook;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
