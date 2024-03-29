package com.siberhus.hswing.table;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;

//A larger table model that performs "paging" of its data. This model
//reports a small number of rows (like 100 or so) as a "page" of data. You
//can switch pages to view all of the rows as needed using the pageDown()
//and pageUp() methods. Presumably, access to the other pages of data is
//dictated by other GUI elements such as up/down buttons, or maybe a text
//field that allows you to enter the page number you want to display.
//
public class PagingTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_PAGE_SIZE = 50;
	
	private TableDataQuery tableDataQuery;
	
	private int dataOffset = 0;
	
	private int pageOffset = 0;
	
	private List<Object[]> data = new ArrayList<Object[]>();
	
	private int pageSize = DEFAULT_PAGE_SIZE;// number of items per page
	
	public PagingTableModel(){}
	
	public PagingTableModel(TableDataQuery tableDataQuery) {
		this.tableDataQuery = tableDataQuery;
	}
	
	public void setTableDataQuery(TableDataQuery tableDataQuery){
		this.tableDataQuery = tableDataQuery;
	}
	
	// Return values appropriate for the visible table part.
	@Override
	public int getRowCount() {
		
		return Math.min(pageSize, tableDataQuery.getRowCount());
	}
	
	@Override
	public int getColumnCount() {
		return tableDataQuery.getColumnNames().length;
	}
	
	// Work only on the visible part of the table.
	@Override
	public Object getValueAt(int row, int col) {
		int pageIndex = row;
		if ((pageIndex < 0) || (pageIndex >= data.size())) {
			return "..";
		}
		return data.get(pageIndex)[col];
	}
	
	public Object[] getValuesAt(int row) {
		return data.get(row);
	}
	
	@Override
	public String getColumnName(int col) {
		return tableDataQuery.getColumnNames()[col];
	}
	
	// Use this method to figure out which page you are on.	
	public int getPageOffset() {
		return pageOffset;
	}
	
	public int getPageCount() {
		return (int) Math.ceil((double) tableDataQuery.getRowCount() / pageSize);
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int s) {
		if (s == pageSize) {
			return;
		}
		int oldPageSize = pageSize;
		pageSize = s;
		pageOffset = (oldPageSize * pageOffset) / pageSize;
//		fireTableDataChanged();
		/*
		 * if (pageSize < oldPageSize) { fireTableRowsDeleted(pageSize,
		 * oldPageSize - 1); } else { fireTableRowsInserted(oldPageSize,
		 * pageSize - 1); }
		 */
	}
	
	public void gotoFirstPage() {
		pageOffset = 0;
		load(pageOffset);		
	}
	
	public void gotoLastPage() {
		pageOffset = getPageCount() - 1;
		load(pageOffset);		
	}
	
	// Update the page offset and fire a data changed (all rows).
	public void gotoNextPage() {
		if (pageOffset < getPageCount() - 1) {
			pageOffset++;			
			load(pageOffset);
//			fireTableDataChanged();
		}
	}
	
	// Update the page offset and fire a data changed (all rows).
	public void gotoPreviousPage() {
		if (pageOffset > 0) {
			pageOffset--;
			load(pageOffset);
//			fireTableDataChanged();
		}
	}

	// We provide our own version of a scrollpane that includes
	// the page up and page down buttons by default.
	public static JScrollPane createPagingScrollPaneForTable(JTable jt) {
		JScrollPane jsp = new JScrollPane(jt);
		TableModel tmodel = jt.getModel();
		
		// Don't choke if this is called on a regular table . . .
		if (!(tmodel instanceof PagingTableModel)) {
			return jsp;
		}
		
		// Okay, go ahead and build the real scrollpane
		final PagingTableModel model = (PagingTableModel) tmodel;
		final JButton upButton = new JButton(new ArrowIcon(ArrowIcon.UP));
		upButton.setEnabled(false); // starts off at 0, so can't go up
		final JButton downButton = new JButton(new ArrowIcon(ArrowIcon.DOWN));
		if (model.getPageCount() <= 1) {
			downButton.setEnabled(false); // One page...can't scroll down
		}
		
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				model.gotoPreviousPage();
				
				// If we hit the top of the data, disable the up button.
				if (model.getPageOffset() == 0) {
					upButton.setEnabled(false);
				}
				downButton.setEnabled(true);
			}
		});
		
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				model.gotoNextPage();

				// If we hit the bottom of the data, disable the down button.
				if (model.getPageOffset() == (model.getPageCount() - 1)) {
					downButton.setEnabled(false);
				}
				upButton.setEnabled(true);
			}
		});
		
		// Turn on the scrollbars; otherwise we won't get our corners.
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		// Add in the corners (page up/down).
		jsp.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, upButton);
		jsp.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, downButton);
		
		return jsp;
	}
	
	public void clear(){
		this.data.clear();
	}
	
	private void load(int pageOffset) {
		
		this.dataOffset = (pageOffset*pageSize);
		this.data.clear();
		Runnable fetch = new Runnable() {
			public void run() {
				try {					
					final List<Object[]> page = tableDataQuery.fetchRows(
							dataOffset, pageSize);
					// done loading -- make available on the event dispatch thread
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {							
							setData(dataOffset, page);
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
					return;
				}
			}
		};
		// run on another thread
		new Thread(fetch).start();
	}
	
	private void setData(int offset, List<Object[]> newData) {
		// This method must be called from the event dispatch thread.
		int lastRow = (offset + newData.size()) - 1;
		this.dataOffset = offset;
		data = newData;
		fireTableRowsUpdated(0, lastRow);
	}
	
	static class MyTableDataQuery implements TableDataQuery{
		private int rowCount = 100;		
		@Override
		public String[] getColumnNames(){
			return new String[]{"#", "Data1","Data2", "Data3"};
		}
		@Override
		public int getRowCount() {
			return rowCount;
		}
		
		public void setRowCount(int rowCount) {
			this.rowCount = rowCount;
		}
		@Override
		public List<Object[]> fetchRows(int offset, int limit)throws Exception {				
			List<Object[]> rows = new ArrayList<Object[]>();
			Thread.sleep(700);
			for(int i=0;i<limit;i++){
				if(offset+i >= rowCount){
					break;
				}
				Object[] row = new Object[4];
				row[0] = offset+i;
				row[1] = RandomStringUtils.randomAlphabetic(10);
				row[2] = RandomStringUtils.randomAlphabetic(10);
				row[3] = RandomStringUtils.randomNumeric(5);					
				rows.add(row);
			}
			return rows;
		}
	}
	public static void main(String[] argv) {
		final MyTableDataQuery dq = new MyTableDataQuery();
		final PagingTableModel tabModel = new PagingTableModel(dq);
		tabModel.gotoFirstPage();
		final JTable tab = new JTable(tabModel);
		final JFrame f = new JFrame("PagingTableModel");
		JPanel cmdPanel = new JPanel();
		cmdPanel.setLayout(new FlowLayout());
		JButton getBtn = new JButton("Get");
		getBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object values[] = tabModel.getValuesAt(tab.getSelectedRow());
				JOptionPane.showMessageDialog(f, ArrayUtils.toString(values));
			}
		});
		cmdPanel.add(getBtn);
		JButton addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dq.setRowCount(dq.getRowCount()+1);
				tabModel.gotoLastPage();
			}
		});
		cmdPanel.add(addBtn);
		JScrollPane sp = PagingTableModel.createPagingScrollPaneForTable(tab);
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(sp, BorderLayout.CENTER);		
		f.getContentPane().add(cmdPanel, BorderLayout.SOUTH);
		JComboBox cb = new JComboBox(new Integer[]{10,20,30,40,50});
		cb.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				tabModel.setPageSize((Integer)e.getItem());
			}
		});
		f.getContentPane().add(cb, BorderLayout.NORTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		f.setSize(400, 300);
		f.setVisible(true);
	}
}
