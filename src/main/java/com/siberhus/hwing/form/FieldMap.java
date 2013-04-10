package com.siberhus.hwing.form;

import java.awt.Component;
import java.util.LinkedHashMap;

import com.siberhus.hswing.form.field.ArrayFormField;
import com.siberhus.hswing.form.field.FormField;

public class FieldMap extends LinkedHashMap<String, FormField>{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public FormField put(String name, FormField field){
//		if(field instanceof Component){
//			((Component)field).setName(name);
//		}		
		if(super.containsKey(name)){
			FormField prevField = super.get(name);
			if(prevField instanceof ArrayFormField){
				((ArrayFormField)prevField).add(field);
				return prevField;
			}else{
				ArrayFormField arrayField = new ArrayFormField();
				((Component)arrayField).setName(name);
				arrayField.add(prevField);
				arrayField.add(field);
				return super.put(name, arrayField);
			}
		}
		
		return super.put(name, field);
	}
	
}
