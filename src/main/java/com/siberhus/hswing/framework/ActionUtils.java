package com.siberhus.hswing.framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ActionUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ActionUtils.class);
	
	private static final ExceptionHandler DEFAULT_EXCEPTION_HANDLER = new RethrowExceptionHandler();
	
	public static void addAction(final AbstractButton button, final Task<?,?> task){
		addAction(button, task);
	}
	
	public static void addAction(final AbstractButton button, final Task<?,?> task, final ExceptionHandler exceptionHandler){
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				task.setSource(button);
				try{
					task.execute();
				}catch(Throwable e){
					if(exceptionHandler!=null){
						exceptionHandler.handle(e);
					}else{
						DEFAULT_EXCEPTION_HANDLER.handle(e);
					}
				}
			}
		});
	}
	
	public static void addAction(final AbstractButton button,final Object targetBean
			,final String targetMethodName){
		addAction(button,targetBean,targetMethodName,null);
	}
	
	public static void addAction(final AbstractButton button,final Object targetBean
			,final String targetMethodName, final ExceptionHandler exceptionHandler){
		
		final Class<?> clazz = targetBean.getClass();
		ActionListener actionListener = new ActionListener(){
			boolean hasParam = false;
			@Override
			public void actionPerformed(ActionEvent evt) {
				Method m = null;
				try{
					logger.trace("Getting method: {}(ActionEvent e) from {} ",targetMethodName,clazz);
					m = clazz.getDeclaredMethod(targetMethodName, ActionEvent.class);
					hasParam = true;
				}catch(Exception e){
					logger.trace("Method {}(ActionEvent e) not found",targetMethodName);
					logger.trace("Getting method: {}() from {}",targetMethodName,clazz);
					try{
						m = clazz.getDeclaredMethod(targetMethodName);
						if(!m.isAccessible()){
							m.setAccessible(true);
						}
					}catch(Exception e2){
						//re-throw exception
						throw new IllegalArgumentException(e2);
					}
				}
				if(!m.isAccessible()){
					m.setAccessible(true);
				}
				try{
					if(hasParam){
						m.invoke(targetBean, evt);
					}else{
						m.invoke(targetBean);
					}
				}catch(Throwable t){
					if(t instanceof InvocationTargetException){
						t = t.getCause();
					}
					if(exceptionHandler!=null){
						exceptionHandler.handle(t);
					}else{
						DEFAULT_EXCEPTION_HANDLER.handle(t);
					}
				}
			}
		};
		
		button.addActionListener(actionListener);
		
	}
}
