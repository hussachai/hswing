package com.siberhus.hswing.form.field;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class TextFormField extends JTextField implements FormField{
	
	private static final long serialVersionUID = 1L;
	
	public TextFormField() {
		super();
	}
	
	public TextFormField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	public TextFormField(int columns) {
		super(columns);
	}

	public TextFormField(String text, int columns) {
		super(text, columns);
	}

	public TextFormField(String text) {
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
