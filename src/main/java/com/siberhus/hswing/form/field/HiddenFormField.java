package com.siberhus.hswing.form.field;

import javax.swing.JComponent;


public class HiddenFormField extends JComponent implements FormField {
	
	private static final long serialVersionUID = 1L;
	
	private Object value;
	
	@Override
	public Object getFieldValue() {
		return value;
	}

	@Override
	public void setFieldValue(Object value) {
		this.value = value;
	}
}
