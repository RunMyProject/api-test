package com.esabatini.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.annotation.Id;

public class User {
	  
	@Id
	private String id;
	private String firstName;
	private String lastName;

	private static User build;

	@Override
	public String toString() {
	    return String.format(
	        "User [id=%s, firstName='%s', lastName='%s']",
	        getId(), getFirstName(), getLastName());
	}
	  
	public static User builder() {
		build = new User();
		return build;
    }
		
	public User build() {
		User clone = null;
		try {
			clone = (User) BeanUtils.cloneBean(build);
		} catch (InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return clone;
	}

	public User id(String id) {
		build.id=id;
		return build;
	}
	
	public User firstName(String firstName) {
		build.firstName=firstName;
		return build;
	}
	
	public User lastName(String lastName) {
		build.lastName=lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
