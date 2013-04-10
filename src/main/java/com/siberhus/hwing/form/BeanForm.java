package com.siberhus.hwing.form;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import model.Person;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.commons.lang.StringUtils;


import com.siberhus.hswing.form.field.FormField;
import com.siberhus.hswing.util.KeyLabel;

public class BeanForm {
	
	private FieldMap fieldMap;
	private static final ContextClassLoaderLocal RULE_REG_BY_CLASSLOADER = new ContextClassLoaderLocal() {
		// Creates the default instance used when the context classloader is
		// unavailable
		protected Object initialValue() {
			return new RuleRegistry();
		}
	};
	private Map<String, Object> modelMap;
	
	public BeanForm(){}
	
	public BeanForm(FieldMap fieldMap){
		this.fieldMap = fieldMap;
	}
	
	public void setFieldMap(FieldMap fieldMap){
		this.fieldMap = fieldMap;
	}
	
	public void add(String name, FormField field){
		fieldMap.put(name, field);
	}
	
	public void clear(){
		for(String name : fieldMap.keySet()){
			FormField field = fieldMap.get(name);
			field.setFieldValue(null);
		}
	}
	
	public void fill(Map<String, Object> modelMap) throws Exception{
		JexlContext context = JexlHelper.createContext();		
		context.setVars(modelMap);
		this.modelMap = modelMap;
		for(String fieldName: fieldMap.keySet()){
			Expression expression = ExpressionFactory.createExpression(fieldName);
			Object result = expression.evaluate(context);
			FormField field = fieldMap.get(fieldName);
			if(result!=null){
				field.setFieldValue(result);
			}else{
				field.setFieldValue(null);
			}
		}
	}
	
	public SwingWorker<?,?> submit(SwingWorker<?,?> task, boolean execute){
		for(String modelName: modelMap.keySet()){
			Object model = modelMap.get(modelName);
			
			for(String fieldName: fieldMap.keySet()){
				if(fieldName.startsWith(modelName)){
					String propertyPath = StringUtils.substringAfter(fieldName, modelName+".");
					FormField field = fieldMap.get(fieldName);
					try{
						Object value = field.getFieldValue();
						if(value!=null && !"".equals(value)){
							if(value instanceof KeyLabel){
								value = ((KeyLabel)value).getKey();
							}
							BeanUtils.setProperty(model, propertyPath, value);
						}
					}catch(ConversionException e){
						throw e;
					}catch(Exception e){
						throw new UnknownPropertyException("Unkown property: "+propertyPath
								+" in model:"+model, e);
					}
				}
			}			
			Field field = null;
			try{
				field = task.getClass().getDeclaredField(modelName);
				if(!field.isAccessible()){
					field.setAccessible(true);
				}
				field.set(task, model);
			}catch(Exception e){
				String setterName = "set"+StringUtils.capitalize(modelName);
				try {
					Method method = task.getClass().getDeclaredMethod(setterName, model.getClass());
					method.invoke(task, model);
				} catch (Exception e1) {
					throw new UnknownPropertyException("Cannot set property:"+model
							+" to SwingWorker");
				}
			}
		}
		//do data validation
		
		if(execute){
			task.execute();
		}
		return task;
	}
	
	public SwingWorker<?,?> submit(SwingWorker<?,?> task) {
		return submit(task, false);
	}
	
	public static void main(String[] args) throws Exception{
		DateTimeConverter dateConverter = new DateTimeConverter() {			
			@Override
			protected Class<Date> getDefaultType() {
				return Date.class;
			}
		};
		dateConverter.setPattern("dd/MM/yyyy");
		ConvertUtils.register(dateConverter, Date.class);
		
		Person spouse = new Person();
		spouse.setFirstName("Sirimon");
		spouse.setLastName("Sangkittipaiboon");
		Person p = new Person();
		p.setFirstName("Hussachai");
		p.setLastName("Puripunpinyo");
		p.setEmail("hussachai@gmail.com");
		p.setSalary(10000000);
		p.setSpouse(spouse);
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("person", p);
		
		JFrame f = new JFrame("Test BeanForm");
		f.setLayout(new FlowLayout());
		BasicFieldFactory ff = new BasicFieldFactory();		
		f.add(ff.createTextField("person.firstName",15));
		f.add(ff.createTextField("person.lastName",15));
		f.add(ff.createTextField("person.email",15));
		f.add(ff.createTextField("person.salary",15));
		f.add(ff.createTextField("person.birthdate",15));
		f.add(ff.createTextField("person.spouse.firstName",15));
		final BeanForm form = new BeanForm();
		form.setFieldMap(ff.getFieldMap());
		
		JButton button = new JButton("Click Me!");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					form.submit(new SwingWorker<Object, String>(){
						private Person person;
						@Override
						protected Object doInBackground() throws Exception {
							System.out.println("Person => "+person);
							return person;
						}
						
					},true);
				} catch (Exception ex) {
					System.out.println("Exception = "+ex.getMessage());
				}
			}
		});
		f.add(button);
		
		form.fill(modelMap);
		f.setSize(400,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
