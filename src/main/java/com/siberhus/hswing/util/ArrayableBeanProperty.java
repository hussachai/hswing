package com.siberhus.hswing.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ArrayableBeanProperty implements ArrayableBean{
	
	private List<Method> methodCache = new ArrayList<Method>();
	
	public ArrayableBeanProperty(Class<?> beanClass, String... propNames) {
		for (String propName : propNames) {
			try {
				Method method = beanClass.getDeclaredMethod("get"
						+ StringUtils.capitalize(propName));
				if(!method.isAccessible()){
					throw new ReflectionException("Unable to access property: "+propName);
				}
				methodCache.add(method);
			} catch (NoSuchMethodException e) {
				throw new NoSuchPropertyException("No such property: "+propName
						+" in class: "+beanClass);
			}
			
		}
	}
	
	@Override
	public Object[] toArray(Object bean) {
		int index = 0;
		Object values[] = new Object[methodCache.size()];
		try {
			for (Method method : methodCache) {
				values[index] = method.invoke(bean);
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
