package com.siberhus.hswing.form.field;

import javax.swing.JTextArea;

public class TextAreaFormField extends JTextArea implements FormField{
	
	private static final long serialVersionUID = 1L;

	public TextAreaFormField() {
		super();
	}

	public TextAreaFormField(int rows, int columns) {
		super(rows, columns);
	}

	public TextAreaFormField(String text, int rows, int columns) {
		super(text, rows, columns);
	}

	public TextAreaFormField(String text) {
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
