package com.siberhus.hswing.framework;


public class RethrowExceptionHandler implements ExceptionHandler{

	@Override
	public void handle(Throwable e){
		throw new ActionInvocationException(e);
	}
	
}
