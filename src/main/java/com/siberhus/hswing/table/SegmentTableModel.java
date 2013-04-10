package com.siberhus.hswing.table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.siberhus.hswing.LazyViewport;

public class SegmentTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_MAX_PAGE_SIZE = 50;
	private int dataOffset = 0;
	private List<Object[]> data = new ArrayList<Object[]>();
	private SortedSet<Segment> pending = new TreeSet<Segment>();
	private TableDataQuery tableDataQuery;
	private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;
	
	public SegmentTableModel(){}
	
	public SegmentTableModel(TableDataQuery tableDataQuery) {
		this.tableDataQuery = tableDataQuery;
	}
	
	public void setTableDataQuery(TableDataQuery tableDataQuery) {
		this.tableDataQuery = tableDataQuery;
	}
	
	@Override
	public int getColumnCount() {
		return tableDataQuery.getColumnNames().length;
	}
	
	@Override
	public String getColumnName(int col) {
		return tableDataQuery.getColumnNames()[col];
	}
	
	@Override
	public int getRowCount() {
		return tableDataQuery.getRowCount();
	}
	
	public int getDataOffset(){
		return dataOffset;
	}
	
	public void setMaxPageSize(int maxPageSize) {
		this.maxPageSize = maxPageSize;
	}
	
	@Override
	public Object getValueAt(int row, int col) {

		// check if row is in current page, schedule if not
		List<Object[]> page = data;
		int pageIndex = row - dataOffset;
		
		if ((pageIndex < 0) || (pageIndex >= page.size())) {
			// not loaded
			System.out.println("object at " + row + " isn't loaded yet");
			schedule(row);
			
			return "..";
		}
		
		Object rowObjects[] = page.get(pageIndex);
		return rowObjects[col];
	}
	
	public Object[] getValuesAt(int row) {
		int pageIndex = row - dataOffset;
		return data.get(pageIndex);
	}
	
	public void clear(){
		this.data.clear();
	}
	
	private void schedule(int offset) {
		// schedule the loading of the neighborhood around offset (if not
		// already scheduled)
		if (isPending(offset)) {
			// already scheduled -- do nothing
			return;
		}
		
		int startOffset = Math.max(0, offset - (maxPageSize / 2));
		int length = (offset + (maxPageSize / 2)) - startOffset;
		load(startOffset, length);
	}
	
	private boolean isPending(int offset) {
		int sz = pending.size();

		if (sz == 0) {
			return false;
		}

		if (sz == 1) {
			// special case (for speed)
			Segment seg = pending.first();

			return seg.contains(offset);
		}

		Segment lo = new Segment(offset - maxPageSize, 0);
		Segment hi = new Segment(offset + 1, 0);

		// search pending segments that may contain offset
		for (Segment seg : pending.subSet(lo, hi)) {
			if (seg.contains(offset)) {
				return true;
			}
		}

		return false;
	}

	private void load(final int startOffset, final int length) {
		System.out.println("offset = "+startOffset+", limit="+length);
		final Segment seg = new Segment(startOffset, length);
		pending.add(seg);
		
		// set up code to run in another thread
		Runnable fetch = new Runnable() {
			public void run() {
				try {
					
					final List<Object[]> page = tableDataQuery.fetchRows(
							startOffset, length);
					// done loading -- make available on the event dispatch thread
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							System.out.println("** loaded " + startOffset
									+ " through "
									+ ((startOffset + length) - 1));
							setData(startOffset, page);
							pending.remove(seg);
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
					pending.remove(seg);
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
		fireTableRowsUpdated(offset, lastRow);
	}
	
	public static void main(String[] argv) {
		TableDataQuery dq = new TableDataQuery() {
			@Override
			public String[] getColumnNames(){
				return new String[]{"#", "Data1","Data2", "Data3"};
			}
			@Override
			public int getRowCount() {
				return 1000;
			}
			@Override
			public List<Object[]> fetchRows(int offset, int limit)throws Exception {				
				List<Object[]> rows = new ArrayList<Object[]>();
				Thread.sleep(2000);
				for(int i=0;i<limit;i++){
					Object[] row = new Object[4];
					row[0] = offset+i;
					row[1] = RandomStringUtils.randomAlphabetic(10);
					row[2] = RandomStringUtils.randomAlphabetic(10);
					row[3] = RandomStringUtils.randomNumeric(100);					
					rows.add(row);
				}
				return rows;
			}
		};
		final SegmentTableModel tabModel = new SegmentTableModel();
		tabModel.setTableDataQuery(dq);
		final JTable tab = new JTable(tabModel);
		JScrollPane sp = LazyViewport.createLazyScrollPaneFor(tab);
		final JFrame f = new JFrame("PagingTableModel");
		JButton btn = new JButton("Get");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object values[] = tabModel.getValuesAt(tab.getSelectedRow());
				JOptionPane.showMessageDialog(f, ArrayUtils.toString(values));
			}
		});
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(sp, BorderLayout.CENTER);
		f.getContentPane().add(btn, BorderLayout.SOUTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 300);
		f.setVisible(true);
	}

}