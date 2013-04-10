package com.siberhus.hswing.form.field;

import javax.swing.JPasswordField;

public class PasswordFormField extends JPasswordField implements FormField{
	
	private static final long serialVersionUID = 1L;

	
	public PasswordFormField() {
		super();
	}

	public PasswordFormField(int columns) {
		super(columns);
	}

	public PasswordFormField(String text, int columns) {
		super(text, columns);
	}

	public PasswordFormField(String text) {
		super(text);
	}

	@Override
	public String getFieldValue() {
		return new String(getPassword());
	}
	
	@Override
	public void setFieldValue(Object value) {
		setText(value!=null?value.toString():"");
	}
	
}
