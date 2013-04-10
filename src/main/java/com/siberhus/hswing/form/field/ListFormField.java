package com.siberhus.hswing.form.field;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class ListFormField extends JList implements FormField {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Object getFieldValue() {
		if(getSelectionMode()==ListSelectionModel.SINGLE_SELECTION){
			return getModel().getElementAt(getSelectedIndex());
		}else{
			int indices[] = getSelectedIndices();
			Object values[] = new Object[indices.length];
			for(int i=0;i<indices.length;i++){
				values[i] = getModel().getElementAt(indices[i]);
			}
			return values;
		}
	}
	
	@Override
	public void setFieldValue(Object value) {
		if(getSelectionMode()==ListSelectionModel.SINGLE_SELECTION){
			if(value.getClass().isArray()){
				throw new IllegalArgumentException("Cannot set multiple values to Single SelectionMode List");
			}
		}else{
			if(value.getClass().isArray()){
				
			}else{
				
			}
		}
		
		for(int i=0;i<getModel().getSize();i++){
			getModel().getElementAt(i);
		}
	}
	
}
