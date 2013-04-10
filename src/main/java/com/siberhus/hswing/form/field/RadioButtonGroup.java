package com.siberhus.hswing.form.field;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

public class RadioButtonGroup extends JComponent implements FormField {
	
	private static final long serialVersionUID = 1L;
	
	private Class<?> type;
	private ButtonGroup delegate = new ButtonGroup();
	
	public RadioButtonGroup(Class<?> type){
		this.type = type;
	}
	
	public ButtonGroup getDelegate(){
		return delegate;
	}
	
	public void add(RadioFormField radio){		
		if(type != radio.getFieldValue().getClass()){
			throw new IllegalArgumentException("All radios in the same group must have the same type of value");
		}
		delegate.add(radio);
	}
	
	@Override
	public Object getFieldValue(){
		Enumeration<AbstractButton> buttonEnum = delegate.getElements();
		while(buttonEnum.hasMoreElements()){
			RadioFormField field = (RadioFormField)buttonEnum.nextElement();
			if(field.isSelected()){
				return field.getFieldValue();
			}
		}
		return null;
	}
	
	@Override
	public void setFieldValue(Object value){
		Enumeration<AbstractButton> buttonEnum = delegate.getElements();
		if(value==null){
			while(buttonEnum.hasMoreElements()){
				RadioFormField field = (RadioFormField)buttonEnum.nextElement();
				field.setSelected(false);
			}
		}else{
			while(buttonEnum.hasMoreElements()){
				RadioFormField field = (RadioFormField)buttonEnum.nextElement();
				if(field.getFieldValue().equals(value)){
					field.setSelected(true);
					return;
				}			
			}
		}
	}
}
