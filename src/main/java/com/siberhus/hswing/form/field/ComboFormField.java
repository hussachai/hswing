package com.siberhus.hswing.form.field;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.siberhus.hswing.util.KeyLabel;

public class ComboFormField extends JComboBox implements FormField{
	
	private static final long serialVersionUID = 1L;
	
	
	public ComboFormField() {
		super();
	}

	public ComboFormField(ComboBoxModel aModel) {
		super(aModel);
	}
	
	public ComboFormField(Object[] items) {
		super(items);
	}
	
	public ComboFormField(Vector<?> items) {
		super(items);
	}
	
	@Override
	public Object getFieldValue() {
		return getSelectedItem();
	}
	
	@Override
	public void setFieldValue(Object value) {
		if(getModel().getSize()>0){
			Object fo = getModel().getElementAt(0);
			if(fo instanceof KeyLabel){
				setSelectedItem(new KeyLabel(value,null));
			}else{
				setSelectedItem(value);
			}
		}
	}
	
}
