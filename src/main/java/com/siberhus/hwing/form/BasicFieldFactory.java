package com.siberhus.hwing.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.siberhus.hswing.form.field.CheckFormField;
import com.siberhus.hswing.form.field.ComboFormField;
import com.siberhus.hswing.form.field.FieldNotFoundException;
import com.siberhus.hswing.form.field.FormField;
import com.siberhus.hswing.form.field.FormattedTextFormField;
import com.siberhus.hswing.form.field.LabelFormField;
import com.siberhus.hswing.form.field.PasswordFormField;
import com.siberhus.hswing.form.field.RadioButtonGroup;
import com.siberhus.hswing.form.field.RadioFormField;
import com.siberhus.hswing.form.field.TextAreaFormField;
import com.siberhus.hswing.form.field.TextFormField;

public class BasicFieldFactory {
	
	private FieldMap fieldMap;
	
	public BasicFieldFactory(){
		this.fieldMap = new FieldMap();
	}
	
	public void add(String name, FormField field){
		fieldMap.put(name, field);
	}
	
	public FieldMap getFieldMap(){
		return fieldMap;
	}
	
	public boolean hasField(String name){
		FormField field = fieldMap.get(name);
		if(field==null){
			return false;
		}
		return true;
	}
	
	public FormField getField(String name){
		FormField field = fieldMap.get(name);
		if(field==null){
			throw new FieldNotFoundException("Field name: "+name+" not found");
		}
		return field;
	}
	
	public Object getFieldValue(String name){
		return getField(name).getFieldValue();
	}
	
	public boolean isBlank(String name){
		Object value = getFieldValue(name);
		if(value==null){
			return true;
		}else if(value instanceof String){
			return StringUtils.isBlank(ObjectUtils.toString(value));
		}else if(value instanceof Collection){
			return ((Collection)value).size()==0;
		}else if(value instanceof Map){
			return ((Map)value).size()==0;
		}
		return false;
	}
	
	public void setFieldValue(String name, Object value){
		getField(name).setFieldValue(value);
	}
	
	public JLabel createLabel(String name) {
		return createLabel(name, null, SwingConstants.LEADING);
	}
	
	public JLabel createLabel(String name, String value) {
		return createLabel(name, value, SwingConstants.LEADING);
	}
	
	public JLabel createLabel(String name, String value, int align) {
		final LabelFormField label = new LabelFormField(value, align);
		fieldMap.put(name, label);
		return label;
	}
	
	public JComboBox createComboBox(String name){
		return createComboBox(name, (Collection<?>)null);
	}
	
	public JComboBox createComboBox(String name, Collection<?> items){
		return createComboBox(name, items, null);
	}
	
	public JComboBox createComboBox(String name, Collection<?> items, Object value) {
		ComboFormField combo = null;
		if(items!=null){
			combo = new ComboFormField(items.toArray(new Object[0]));
		}else{
			combo = new ComboFormField();
		}
		combo.setFieldValue(value);
		fieldMap.put(name, combo);
		return combo;
	}
	
	public JComboBox createComboBox(String name, Object[] items) {
		return createComboBox(name, items, null);
	}
	
	public JComboBox createComboBox(String name, Object[] items, Object value) {
		ComboFormField combo = new ComboFormField(items);
		combo.setFieldValue(value);
		fieldMap.put(name, combo);
		return combo;
	}
	
	public JTextField createTextField(String name) {
		return createTextField(name, "", 0);
	}
	
	public JTextField createTextField(String name, int cols) {
		return createTextField(name, "", cols);
	}
	
	public JTextField createTextField(String name, String value) {
		return createTextField(name, value, 0);
	}
	
	public JTextField createTextField(String name, String value, int cols) {
		TextFormField textField = new TextFormField(value, cols);
		fieldMap.put(name, textField);
		return textField;
	}
	
	public JFormattedTextField createTextField(String name, Format format){
		FormattedTextFormField field = new FormattedTextFormField(format);
		fieldMap.put(name, field);
		return field;
	}
	
	public JFormattedTextField createTextField(String name, Format format, Object value){
		FormattedTextFormField field = new FormattedTextFormField(format);
		field.setFieldValue(value);
		fieldMap.put(name, field);
		return field;
	}
	
	public JPasswordField createPasswordField(String name, String value){
		PasswordFormField passwordField = new PasswordFormField(value);
		fieldMap.put(name, passwordField);
		return passwordField;
	}
	
	
	public JTextArea createTextArea(String name, String value){
		return createTextArea(name, value, 0, 0);
	}
	
	public JTextArea createTextArea(String name, String value, int rows, int cols){
		TextAreaFormField textAreaField = new TextAreaFormField(value, rows, cols);
		fieldMap.put(name, textAreaField);
		return textAreaField;
	}
	
//	public JButton createButton(String name, String text) {
//		return createButton(name, text, null);
//	}
//	
//	public JButton createButton(String name, Icon icon) {
//		return createButton(name, null, icon);
//	}
//	
//	public JButton createButton(String name, String text, Icon icon) {		
//		
//		JButton button = null;		
//		if(text!=null && icon!=null){
//			button = new JButton(text, icon);
//		}else if(text!=null){
//			button = new JButton(text);
//		}else if(icon!=null){
//			button = new JButton(icon);
//		}else{
//			button = new JButton("");
//		}
//		
//		fieldMap.put(name, button);		
//		return button;		
//	}
	
	public JCheckBox createCheckBox(String name, Object value, String text) {
		return createCheckBox(name, value, text, null);
	}
	
	public JCheckBox createCheckBox(String name, Object value, Icon icon) {
		return createCheckBox(name, value, null, icon);
	}
	
	public JCheckBox createCheckBox(String name, Object value, String text, Icon icon) {
		CheckFormField checkbox = null;
		if(text!=null && icon!=null){
			checkbox = new CheckFormField(text, icon);
		}else if(text!=null){
			checkbox = new CheckFormField(text);
		}else if(icon!=null){
			checkbox = new CheckFormField(icon);
		}else{
			checkbox = new CheckFormField();
		}
		checkbox.setFieldValue(value);
		checkbox.setOpaque(true); // Or window's checkboxes will have strange border
		fieldMap.put(name, checkbox);
		return checkbox;
	}
	
	public JRadioButton createRadioButton(String name, Object value, String text){
		return createRadioButton(name, value, text, null);
	}
	
	public JRadioButton createRadioButton(String name, Object value, Icon icon){
		return createRadioButton(name, value, null, icon);
	}
	
	public JRadioButton createRadioButton(String name, Object value, String text, Icon icon){
		
		if(value==null){
			throw new IllegalArgumentException("Radio value cannot be null");
		}
		RadioFormField radio = null;
		if(text!=null && icon!=null){
			radio = new RadioFormField(text, icon);
		}else if(text!=null){
			radio = new RadioFormField(text);
		}else if(icon!=null){
			radio = new RadioFormField(icon);
		}else{
			radio = new RadioFormField();
		}
		radio.setFieldValue(value);
		RadioButtonGroup buttonGroup = null;
		FormField component = fieldMap.get(name);
		if(component!=null){
			if( !(component instanceof RadioButtonGroup) ){
				throw new RuntimeException("Field name: "+name+" 's already existed and " +
						"it's not RadioButtonGroup but "+ component.getName());
			}
			buttonGroup = (RadioButtonGroup)component;
		}else{
			buttonGroup = new RadioButtonGroup(value.getClass());
			fieldMap.put(name, buttonGroup);
		}
		buttonGroup.add(radio);
		
		return radio;
	}
	
	public static void main(String[] args) {
		final BasicFieldFactory ff = new BasicFieldFactory();
		final JFrame f = new JFrame("TEST");
		f.setLayout(new MigLayout("inset 20", "[para][][][]", ""));
		f.add(ff.createRadioButton("gender", "Male", "M"), "span 2");
		f.add(ff.createRadioButton("gender", "Female", "F"), "span 2, wrap");
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
//				ff.getField("gender").setValue("M");
				JOptionPane.showMessageDialog(f, ff.getFieldValue("gender"));
				JOptionPane.showMessageDialog(f, Arrays.toString((Object[])ff.getFieldValue("a")));
			}
		});
		f.add(submitBtn, "span, wrap");
		f.add(ff.createLabel("a", "Hello1"));
		f.add(ff.createLabel("a", "Hello2"));
		f.add(ff.createLabel("a", "Hello3"));
		f.add(ff.createLabel("a", "Hello4"),"wrap");
		f.setSize(400, 300);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}

