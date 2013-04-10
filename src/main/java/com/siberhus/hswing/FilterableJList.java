package com.siberhus.hswing;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;


/**
 *
 * @author Hussachai
 */
public class FilterableJList extends JList {
	
	private static final long serialVersionUID = 1L;
	
	private FilterableListModel model;
	
	public FilterableJList() {
		setModel(new FilterableListModel());
	}
	
	/* ListModel copies the object instance instead of refer to object reference
	 * So I remove 2 below constructors because it may be confused.
	 * See populateItem(Collection data) if you want to populate list data
	 * with external source
	 * */
//	public JDefaultList(Vector listData) {
//		this(listData.toArray());
//	}
//	
//	public JDefaultList(Object[] listData) {
//		defaultListModel = new DefaultListModel();
//		if(listData!=null){
//			for(Object data : listData){
//				defaultListModel.addElement(data);
//			}
//		}
//		setModel(defaultListModel);
//	}
	
	public FilterableJList(FilterableListModel model) {
		if(! (model instanceof DefaultListModel)){
			throw new IllegalArgumentException("model must be DefaultListModel");
		}
		setModel(model);
	}
	
	@Override
	public void setModel(ListModel model){
		if(! (model instanceof FilterableListModel)){
			throw new IllegalArgumentException("model must be FilterableListModel");
		}
		super.setModel(model);
		this.model = (FilterableListModel)model;
	}
	
	@Override
	public FilterableListModel getModel(){
		return model;
	}
	
	/**
	 * Associates filtering document listener to text
	 * component.
	 */
	public void registerFilterField(JTextField filterField) {
		if (filterField != null) {
			FilterableListModel model = (FilterableListModel) getModel();
			filterField.getDocument().addDocumentListener(model);
		}
	}
	
	/**
	 * Disassociates filtering document listener from text
	 * component.
	 */
	public void unregisterFilterField(JTextField filterField) {
		if (filterField != null) {
			FilterableListModel model = (FilterableListModel) getModel();
			filterField.getDocument().removeDocumentListener(model);
		}
	}
	
	public void populateItem(Collection<Object> data){
		for(Object o : data){
			getModel().addElement(o);
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		FilterableJList l = new FilterableJList(new FilterableListModel());
		l.getModel().addElement("jelly");
		l.getModel().addElement("catty");
		l.getModel().addElement("babe");
		l.getModel().addElement("illusion");
		frame.getContentPane().add(l,BorderLayout.CENTER);
		JTextField f = new JTextField();
		l.registerFilterField(f);
		frame.getContentPane().add(f,BorderLayout.NORTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,200);
		frame.setVisible(true);
	}
}
