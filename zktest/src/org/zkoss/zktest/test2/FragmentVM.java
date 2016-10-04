/* FragmentVM.java

	Purpose:
		
	Description:
		
	History:
		Fri Sep 30 17:35:58 CST 2016, Created by wenninghsu

Copyright (C) 2016 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zktest.test2;

/**
 * 
 * @author wenninghsu
 */
public class FragmentVM {

	private String inputValue;

	private Customer customer;

	public FragmentVM() {
		this.inputValue = "";
		this.customer = new Customer("Jumper", 30, "Taiwan");
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	class Customer {

		private String name;

		private int age;

		private String nation;

		public Customer(String name, int age, String nation) {
			this.name = name;
			this.age = age;
			this.nation = nation;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getNation() {
			return nation;
		}

		public void setNation(String nation) {
			this.nation = nation;
		}
	}
}
