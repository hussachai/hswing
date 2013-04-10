package com.siberhus.hwing.form;

import java.awt.IllegalComponentStateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

import model.Dog;
import ognl.ClassResolver;
import ognl.DefaultClassResolver;
import ognl.Ognl;
import ognl.TypeConverter;

import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.ConvertUtils;


import com.siberhus.hswing.form.validator.Validator;
import com.siberhus.hswing.util.CommonsOgnlTypeConverter;
import com.siberhus.hswing.util.OgnlUtils;

public class OgnlBeanForm<T> {
	
	private static final TypeConverter DEFAULT_TYPE_CONVERTER = new CommonsOgnlTypeConverter();
	private static final ClassResolver DEFAULT_CLASS_RESOLVER = new DefaultClassResolver();

	private TypeConverter typeConverter = DEFAULT_TYPE_CONVERTER;
	private ClassResolver classResolver = DEFAULT_CLASS_RESOLVER;
	
	private List<FormField> fieldList = new ArrayList<FormField>();
	private Map<?, ?> ognlContext;
	private T bean;
	private Map<String, JComponent> fieldMap;
	
	private static final ContextClassLoaderLocal RULE_REG_BY_CLASSLOADER = new ContextClassLoaderLocal() {
		// Creates the default instance used when the context classloader is
		// unavailable
		protected Object initialValue() {
			return new RuleRegistry();
		}
	};
	
	public OgnlBeanForm(){}
	
	public OgnlBeanForm(Map<String, JComponent> fieldMap) {
		this.fieldMap = fieldMap;
	}
	
	public void setFieldMap(Map<String, JComponent> fieldMap){
		this.fieldMap = fieldMap;
	}
	
	public void setTypeConverter(TypeConverter typeConverter) {
		this.typeConverter = typeConverter;
	}
	
	public void setClassResolver(ClassResolver classResolver) {
		this.classResolver = classResolver;
	}
	
	public void fill(T bean) throws Exception{
		this.bean = bean;
		this.ognlContext = Ognl.createDefaultContext(bean, classResolver,
				typeConverter);
		for(FormField field : fieldList){
			JComponent component = field.getComponent();
			String ognlExpr = field.getOgnlExpression();
			Validator validators[] = field.getValidators();
			String validatorConstraints[][] = field.getValidatorConstraints();
			OgnlUtils.ensureObjectPathNotNull(bean, ognlExpr);
			Object value = OgnlUtils.getValueOgnl(bean, ognlExpr);
			if(component instanceof JTextComponent){
				String formattedValue = (String)ConvertUtils.convert(value, String.class);
				((JTextComponent)component).setText(formattedValue);
			}else if(component instanceof JToggleButton){
				if(value instanceof Boolean){
//					Boolean booleanValue = 
				}else if(value instanceof Number){
					
				}else{
					
				}
			}
//			OgnlUtils.setValueOgnl(bean, ognlExpr, value)
		}
		
	}
	
	public void submit(SwingWorker<?,?> task){
		
		//do data conversion
		
		//do data validation
		
		task.execute();
	}
	
	public void add(String fieldName, String expr, String rules){
		if(fieldMap==null){
			throw new IllegalComponentStateException("To use this method you must provide fieldMap to BeanForm"); 
		}
		JComponent component = fieldMap.get(fieldName);
		add(component, expr, rules);
	}
	
	public void add(JTextComponent textComponent, String expr, String rules) {
		//void setText(String);
		//String getText();
		//JEditorPane, JTextArea, JTextField, JFormattedTextField, JPasswordField
		add(textComponent, expr, rules);
	}
	
	public void add(JToggleButton toggleButton, String expr, String rules) {
		//void setSelected(boolean);
		//boolean isSelected();
		//JCheckBox, JRadioButton
		add(toggleButton, expr, rules);
	}
	
	public void add(ButtonGroup buttonGroup, String expr, String rules) {
		//Group of JToggleButton
		add(buttonGroup, expr, rules);
	}
	
	public void add(JList list, String expr, String rules) {
		//void setSelectedIndices(int[] indices);
		//int[] getSelectedIndices();
		//JList
		add(list, expr, rules);
	}
	
	public void add(JComboBox comboBox, String expr, String rules) {
		//void setSelectedItem(Object anObject);
		//Object getSelectedItem();
		//JList
		add(comboBox, expr, rules);
	}
	
	protected void add(JComponent component, String expr, String rules) {
		FormField field = new FormField();
		field.setComponent(component);
		field.setOgnlExpression(expr);
		if (rules != null) {
			RuleRegistry ruleReg = (RuleRegistry)RULE_REG_BY_CLASSLOADER.get();
			String ruleArray[] = rules.split("\\|");
			Validator validators[] = new Validator[ruleArray.length];
			String[] validatorConstraints[] = new String[ruleArray.length][]; 
			for (int i=0;i< ruleArray.length;i++) {
				String rule = ruleArray[i];
				String ruleName = null, constraints = null;
				int p1Idx = rule.indexOf('[');
				if(p1Idx!=-1){
					ruleName = rule.substring(0, p1Idx);
					int p2Idx = rule.indexOf(']');
					if(p2Idx!=-1){
						constraints = rule.substring(p1Idx+1, p2Idx);
					}else{
						//throw invalid rule parameters 
					}
				}else{
					ruleName = rule;
				}
				Validator validator = ruleReg.lookup(ruleName);
				validators[i] = validator;
				if(constraints!=null){
					validatorConstraints[i] = constraints.split(",");
				}				
			}
			field.setValidators(validators);
			field.setValidatorConstraints(validatorConstraints);
		}
		fieldList.add(field);
	}
	
	protected static class FormField {
		
		private JComponent component;
		private String ognlExpression;
		private Validator validators[];
		private String validatorConstraints[][];
		
		public JComponent getComponent() {
			return component;
		}

		public void setComponent(JComponent component) {
			this.component = component;
		}

		public String getOgnlExpression() {
			return ognlExpression;
		}

		public void setOgnlExpression(String ognlExpression) {
			this.ognlExpression = ognlExpression;
		}

		public Validator[] getValidators() {
			return validators;
		}

		public void setValidators(Validator[] validators) {
			this.validators = validators;
		}
		
		public String[][] getValidatorConstraints() {
			return validatorConstraints;
		}
		
		public void setValidatorConstraints(String[][] validatorConstraints) {
			this.validatorConstraints = validatorConstraints;
		}
		
	}

	public static void main(String[] args) {

		OgnlBeanForm bf = new OgnlBeanForm();

		Dog dog = new Dog();
	}
}
