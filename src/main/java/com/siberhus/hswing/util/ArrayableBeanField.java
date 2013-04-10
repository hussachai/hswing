package com.siberhus.hswing.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ArrayableBeanField implements ArrayableBean{
	
	private List<Field> fieldCache = new ArrayList<Field>();
	
	public ArrayableBeanField(Class<?> beanClass, String... fieldNames) {
		for (String fieldName : fieldNames) {
			try {
				Field field = beanClass.getDeclaredField(fieldName);
				if(!field.isAccessible()){
					field.setAccessible(true);
				}
				fieldCache.add(field);
			} catch (java.lang.NoSuchFieldException e) {
				throw new NoSuchFieldException("No such field: "+fieldName
						+" in class: "+beanClass);
			}			
		}
	}
	
	@Override
	public Object[] toArray(Object bean) {
		int index = 0;
		Object values[] = new Object[fieldCache.size()];
		try {
			for (Field field : fieldCache) {
				values[index] = field.get(bean);
				index++;
			}
			return values;
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e).getTargetException();
			}
			throw new ReflectionException(e.getMessage(), e);
		}
	}
	
}
