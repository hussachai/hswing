package com.siberhus.hswing;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListModel;

public class SortableJList extends JList{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int from;
	private DefaultListModel model;
	
	private MouseAdapter itemDragAndDropMouseAdapter = new MouseAdapter() {
		
		@Override
		public void mousePressed(MouseEvent m) {
			from = getSelectedIndex();
		}
	};
	
	private MouseMotionAdapter itemDragAndDropMouseMotionAdapter = new MouseMotionAdapter() {
		
		@Override
			public void mouseDragged(MouseEvent m) {
				int to = getSelectedIndex();

				if (to == from) {
					return;
				}	
				Object s = getModel().remove(from);
				getModel().add(to, s);
				from = to;
			}
	};
	
	public SortableJList() {
		this(new DefaultListModel());
	}
	
	public SortableJList(DefaultListModel model) {
		setModel(model);
		addMouseListener(itemDragAndDropMouseAdapter);
		addMouseMotionListener(itemDragAndDropMouseMotionAdapter);
	}
	
	@Override
	public void setModel(ListModel model){
		if(! (model instanceof DefaultListModel)){
			throw new IllegalArgumentException("model must be DefaultListModel");
		}
		super.setModel(model);
		this.model = (DefaultListModel)model;
	}
	
	@Override
	public DefaultListModel getModel(){
		
		return model;
	}
	
	public void populateItem(Collection<Object> data){
		for(Object o : data){
			getModel().addElement(o);
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		SortableJList l = new SortableJList();
		l.getModel().addElement("jelly");
		l.getModel().addElement("catty");
		l.getModel().addElement("babe");
		l.getModel().addElement("illusion");
		frame.getContentPane().add(l,BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,200);
		frame.setVisible(true);
	}
}
