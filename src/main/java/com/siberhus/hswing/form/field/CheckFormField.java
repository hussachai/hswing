package com.siberhus.hswing.form.field;

import javax.swing.Icon;
import javax.swing.JCheckBox;

public class CheckFormField extends JCheckBox implements FormField{
	
	private static final long serialVersionUID = 1L;
	
	private Object value;
	
	public CheckFormField() {
		super();
	}
	
	public CheckFormField(Icon icon) {
		super(icon);
	}
	
	public CheckFormField(String text, Icon icon) {
		super(text, icon);
	}

	public CheckFormField(String text) {
		super(text);
	}

	@Override
	public Object getFieldValue() {
		if(isSelected()){
			return value;
		}
		return null;
	}
	
	@Override
	public void setFieldValue(Object value) {
		this.value = value;
	}
	
}
