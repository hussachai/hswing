package com.siberhus.hswing.form.field;

import javax.swing.JSlider;

public class SliderFormField extends JSlider implements FormField {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public Integer getFieldValue() {
		return getValue();
	}
	
	@Override
	public void setFieldValue(Object value) {
		if( !(value instanceof Number) ){
			throw new IllegalArgumentException("Value must be number");
		}
		setValue(((Number)value).intValue());
	}
	
}
