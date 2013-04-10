package com.siberhus.hswing.form.field;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class ArrayFormField extends JComponent implements FormField{
	
	private static final long serialVersionUID = 1L;
	
	private List<FormField> fieldList = new ArrayList<FormField>();
	
	public void add(FormField field){
		((Component)field).setName(field.getName()+"_"+fieldList.size());
		fieldList.add(field);
	}
	
	@Override
	public Object[] getFieldValue() {
		Object values[] = new Object[fieldList.size()];
		for(int i=0;i<fieldList.size();i++){
			FormField field = fieldList.get(i);
			values[i] = field.getFieldValue();
		}
		return values;
	}
	
	@Override
	public void setFieldValue(Object value) {
		throw new UnsupportedOperationException("This method should not be invoked");
	}
	
	public static void main(String[] args) {
		
	}
}
