package com.siberhus.hswing.util;


import java.awt.Component;

import javax.swing.JOptionPane;

import com.siberhus.hswing.framework.ExceptionHandler;

public class ErrorPopupExceptionHandler implements ExceptionHandler{
	
	private Component parentComp;
	
	public ErrorPopupExceptionHandler(){}
	
	public ErrorPopupExceptionHandler(Component parentComp){
		this.parentComp = parentComp;
	}
	
	@Override
	public void handle(Throwable e) {
		JOptionPane.showMessageDialog(parentComp
				, e.toString(), "ERROR",JOptionPane.ERROR_MESSAGE);
	}
	
}
