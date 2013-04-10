package com.siberhus.hswing.form.validator;

import java.util.Arrays;

public class EmailValidator implements Validator{
	
	@Override
	public void validate(Object value, String constraints[]) {
		System.out.println("Value = "+value);
		System.out.println("Constraints = "+Arrays.toString(constraints));
	}

	

}
