package org.zkoss.zktest.test2;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.ListModelList;

/**
 * Created by wenninghsu on 9/7/16.
 */
public class FragmantVM {

//	private String zkBtnValue = "before change!";
	private String fragmentInputValue = "before change input";

	private String fragmentBtnValue = "before change";

	private Validator validator;

	private ListModelList model;

	private ListModelList<Customer> customers;

	private boolean show = false;

	private String tbValue = "before";

	@Init
	public void init() {
		validator = new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
			System.out.println("validate");
			String name = (String) ctx.getProperty().getValue();
			if (name != null) {
				addInvalidMessage(ctx, "name", "Mandatory field!");
			}
			}
		};
		this.customers = new ListModelList<Customer>();
		for (int i = 0; i < 3; i++)
			this.customers.add(new Customer("Name " + i, "Name " + i, "Name " + i));

		model = new ListModelList();
		model.add("aaa");
		model.add("bbb");
		model.add("ccc");
		model.add("ddd");
	}

	public String getTbValue() {
		return tbValue;
	}

	public void setTbValue(String tbValue) {
		this.tbValue = tbValue;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public ListModelList<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(ListModelList<Customer> customers) {
		this.customers = customers;
	}

	public String getFragmentInputValue() {
		return fragmentInputValue;
	}

	public void setFragmentInputValue(String fragmentInputValue) {
		this.fragmentInputValue = fragmentInputValue;
	}

	public ListModelList getModel() {
		return model;
	}

	public void setModel(ListModelList model) {
		this.model = model;
	}

//	public String getZkBtnValue() {
//		return zkBtnValue;
//	}
//
//	public void setZkBtnValue(String zkBtnValue) {
//		this.zkBtnValue = zkBtnValue;
//	}

	public String getFragmentBtnValue() {
		return fragmentBtnValue;
	}

	public void setFragmentBtnValue(String fragmentBtnValue) {
		this.fragmentBtnValue = fragmentBtnValue;
	}

	public Validator getValidator() {
		return validator;
	}

	@Command
	@NotifyChange("*")
	public void change(BindContext ctx) {
//		zkBtnValue = "changed";
		Event event = ctx.getTriggerEvent();
		fragmentInputValue = "changed";
		fragmentBtnValue = "changed";
	}

	public static class Customer
	{
		private String name0;

		private String name1;

		private String name2;

		public Customer() {}

		public Customer(String name0, String name1, String name2)
		{
			this.name0 = name0;
			this.name1 = name1;
			this.name2 = name2;
		}

		public String getName0()
		{
			return name0;
		}

		public void setName0(String name0)
		{
			this.name0 = name0;
		}

		public String getName1()
		{
			return name1;
		}

		public void setName1(String name1)
		{
			this.name1 = name1;
		}

		public String getName2()
		{
			return name2;
		}

		public void setName2(String name2)
		{
			this.name2 = name2;
		}

	}

}
