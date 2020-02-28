package com.esabatini.model;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

import org.springframework.data.annotation.Id;

public class Product {

	@Id
	private String id;
	private String title;
	private Double price;

	private static Product build;
	
	@Override
	public String toString() {
	    return String.format(
	        "Product [id=%s, title='%s', price='%s']",
	        getId(), getTitle(), getPrice());
	}

	public static Product builder() {
		build = new Product();
		return build;
    }
	
	public Product build() {
    	Product clone = null;
		try {
			clone = (Product) BeanUtils.cloneBean(build);
		} catch (InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	public Product id(String id) {
		build.id=id;
		return build;
	}

	public Product price(Double price) {
		build.price=price;
		return build;
	}

	public Product title(String title) {
		build.title=title;
		return build;
	}

	// get / set
	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}	
}