package com.siberhus.hwing.form;

import java.util.HashMap;
import java.util.Map;

import com.siberhus.hswing.form.validator.Validator;

public class RuleRegistry {
	
	private Map<String, Validator> validatorMap = new HashMap<String, Validator>();
	
	public RuleRegistry(){
		//register standard rules
		
	}
	
	public Validator lookup(String rule){
		return validatorMap.get(rule);
	}
	
	public void register(String rule, Validator validator){
		validatorMap.put(rule, validator);
	}
	
	public void unregister(String rule){
		validatorMap.remove(rule);
	}
	
}
