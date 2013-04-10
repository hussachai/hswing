package com.siberhus.hswing.form.field;

import javax.swing.Icon;
import javax.swing.JLabel;

public class LabelFormField extends JLabel implements FormField{
	
	private static final long serialVersionUID = 1L;
	
	public LabelFormField() {
		super();
	}

	public LabelFormField(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}
	
	public LabelFormField(Icon image) {
		super(image);
	}

	public LabelFormField(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public LabelFormField(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public LabelFormField(String text) {
		super(text);
	}

	@Override
	public String getFieldValue() {		
		return getText();
	}
	
	@Override
	public void setFieldValue(Object value) {
		setText(value!=null?value.toString():"");
	}

}
