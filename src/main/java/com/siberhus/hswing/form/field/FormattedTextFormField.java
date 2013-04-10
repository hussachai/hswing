package com.siberhus.hswing.form.field;

import java.text.Format;

import javax.swing.JFormattedTextField;

public class FormattedTextFormField extends JFormattedTextField implements FormField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public FormattedTextFormField() {
		super();
	}

	public FormattedTextFormField(AbstractFormatter formatter) {
		super(formatter);
	}

	public FormattedTextFormField(AbstractFormatterFactory factory,
			Object currentValue) {
		super(factory, currentValue);
	}

	public FormattedTextFormField(AbstractFormatterFactory factory) {
		super(factory);
	}

	public FormattedTextFormField(Format format) {
		super(format);
	}

	public FormattedTextFormField(Object value) {
		super(value);
	}

	@Override
	public Object getFieldValue() {
		return getValue();
	}

	@Override
	public void setFieldValue(Object value) {
		setValue(value);
	}

}
