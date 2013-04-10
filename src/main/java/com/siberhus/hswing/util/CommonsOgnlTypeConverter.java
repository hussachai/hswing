package com.siberhus.hswing.util;

import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import ognl.DefaultTypeConverter;

public class CommonsOgnlTypeConverter extends DefaultTypeConverter {
	
	@Override
	public Object convertValue(Map context, Object value, Class toType) {
		
		return ConvertUtils.convert(value, toType);
	}
	
	
	
}
