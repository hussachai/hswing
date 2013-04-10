package com.siberhus.hswing.framework;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class AbstractPage implements Page{
	
	private Application application;
	private Dimension size;
	private boolean visible;
	private JPanel panel;
	private boolean flag;
	
	public JPanel getPanel(){
		if(panel == null){
			//Initializing components
			initComponents();
			//Building panel
			panel = buildPanel();
			//Filling form
			try {
				initForm();
			} catch (Exception e) {
				// TODO handle this exception with exception handler
				e.printStackTrace();
			}
			flag = true;
		}
		if(flag==false){
			//Refreshing form
			try{
				refreshForm();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		flag = false;
		return panel;
	}
	
	@Override
	public void refreshForm() throws Exception{}
	
	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public Application getApplication() {
		return application;
	}

	@Override
	public void setApplication(Application app) {
		this.application = app;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}
	
	
}
