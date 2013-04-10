package com.siberhus.hswing.form.field;

import javax.swing.Icon;
import javax.swing.JRadioButton;

public class RadioFormField extends JRadioButton implements FormField{
	
	private static final long serialVersionUID = 1L;
	
	private Object value;
	
	public RadioFormField(){}
	
	public RadioFormField(String text){
		super(text);
	}
	
	public RadioFormField(Icon icon){
		super(icon);
	}
	
	public RadioFormField(String text, Icon icon){
		super(text, icon);
	}

	@Override
	public Object getFieldValue() {
		return value;
	}
	
	@Override
	public void setFieldValue(Object value) {
		this.value = value;
	}
	
}
