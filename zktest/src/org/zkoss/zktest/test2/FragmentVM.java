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

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * 
 * @author wenninghsu
 */
public class FragmentVM {

	private String tbValue = "before";

	private Customer customer;

	private Student[] students;

	public FragmentVM() {
		this.customer = new Customer("Jumper", 30, "Taiwan");
		this.students = new Student[]{new Student("Wenning", 10, 80), new Student("Chris", 10, 70)};
	}

	public String getTbValue() {
		return tbValue;
	}

	public void setTbValue(String tbValue) {
		this.tbValue = tbValue;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		System.out.println("setCustomer");
		this.customer = customer;
	}

	public Student[] getStudents() {
		return students;
	}

	public void setStudents(Student[] students) {
		this.students = students;
	}

	@Command
	@NotifyChange("*")
	public void clickBtn1() {
		System.out.println("clickBtn1");
		getCustomer().setName("Jumper 2.0");
	}
	@Command
	@NotifyChange("*")
	public void clickBtn2() {
		System.out.println("clickBtn2");
		getCustomer().setName("Jumper 3.0");
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

	class Student {

		private String name;

		private int age;

		private int score;

		public Student(String name, int age, int score) {
			this.name = name;
			this.age = age;
			this.score = score;
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

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}
	}
}
