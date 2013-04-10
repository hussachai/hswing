package com.siberhus.hswing.framework;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface Page {
	
	public void initComponents();
	
	public JPanel buildPanel();
	
	public void initForm()throws Exception;
	
	public void refreshForm()throws Exception;
	
	public boolean isVisible();
	
	public void setVisible(boolean visible);
	
	public Application getApplication();
	
	public void setApplication(Application app);
	
	public Dimension getSize();
	
	public void setSize(Dimension size);
	
}
