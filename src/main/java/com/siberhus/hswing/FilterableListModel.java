package com.siberhus.hswing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class FilterableListModel extends DefaultListModel 
	implements DocumentListener{
	
	private static final long serialVersionUID = 1L;
	
	private List<Object> list;
	private List<Object> filteredList;
	private String lastFilter = "";
	private boolean regexFilter;
	
	public FilterableListModel() {
		list = new ArrayList<Object>();
		filteredList = new ArrayList<Object>();
	}

	@Override
	public void addElement(Object element) {
		list.add(element);
		filter(lastFilter);
	}

	@Override
	public int getSize() {
		return filteredList.size();
	}
	
	@Override
	public Object getElementAt(int index) {
		Object returnValue;

		if (index < filteredList.size()) {
			returnValue = filteredList.get(index);
		} else {
			returnValue = null;
		}

		return returnValue;
	}

	// DocumentListener Methods
	@Override
	public void insertUpdate(DocumentEvent event) {
		Document doc = event.getDocument();

		try {
			lastFilter = doc.getText(0, doc.getLength());
			filter(lastFilter);
		} catch (BadLocationException ble) {
			System.err.println("Bad location: " + ble);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent event) {
		Document doc = event.getDocument();

		try {
			lastFilter = doc.getText(0, doc.getLength());
			filter(lastFilter);
		} catch (BadLocationException ble) {
			System.err.println("Bad location: " + ble);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent event) {
	}
	
	private void filter(String search) {
		filteredList.clear();

		for (Object element : list) {
			if (element.toString().indexOf(search, 0) != -1) {
				filteredList.add(element);
			}
		}
		
		fireContentsChanged(this, 0, getSize());
	}
	
}
