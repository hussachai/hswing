package com.siberhus.hswing.framework;

import java.awt.Button;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.SwingWorker;


public abstract class Task<T,V>{
	
	private boolean blockSource = true;
	private Component source;
	private SwingWorker<T,V> worker;
	
	public void execute(){
		if(!validate()){
			if(isBlockSource()){
				source.setEnabled(true);
			}
			return;
		}
		
		worker = new SwingWorker<T, V>(){
			@Override
			protected T doInBackground() throws Exception {
				
				return Task.this.doInBackground();
			}
			@Override
			protected void done() {
				try{
					T result = get();
					Task.this.succeeded(result);
				}catch(Throwable t){
					if(t instanceof InvocationTargetException){
						Task.this.failed(t.getCause());
					}else{
						Task.this.failed(t);
					}
				}finally{
					Task.this.finished();
					if(isBlockSource()){
						source.setEnabled(true);
					}
				}
			}
			@Override
			protected void process(List<V> chunks) {
				Task.this.process(chunks);
			}
			
		};
		worker.execute();
	}
	
	public SwingWorker<T,V> getWorker(){
		return worker;
	}
	
	public void setSource(Component source){
		this.source = source;
		if(isBlockSource()){
			source.setEnabled(false);
		}
	}
	
	public boolean isBlockSource() {
		return blockSource;
	}

	public Task<T,V> setBlockSource(boolean blockSource) {
		this.blockSource = blockSource;
		return this;
	}
	
	
	protected abstract T doInBackground()throws Exception;
	
	protected boolean validate(){
		return true;
	}
	
	/* process method is invoked on the Event Dispatch Thread.*/
	protected void process(List<V> chunks){}
	
	protected void succeeded(T result){}
	
	protected void failed(Throwable cause){}
	
	protected void finished(){}
	
	
	public static void main(String[] args) {
		A a = new A();
		a.setSource(new Button());
		a.execute();
		a.execute();
	}
}
class A extends Task{
	
	@Override
	protected Object doInBackground() throws Exception {
		throw new Exception();
//		return "AA";
	}
	
	@Override
	protected void succeeded(Object result) {
		System.out.println("SUCCEEDED => "+result);
	}

	@Override
	protected void process(List chunks) {
		// TODO Auto-generated method stub
		
	}
	protected void failed(Throwable cause){
		System.out.println(cause);
	}
}