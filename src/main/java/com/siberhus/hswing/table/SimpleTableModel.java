package com.siberhus.hswing.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class SimpleTableModel extends DefaultTableModel{
	
	private static final long serialVersionUID = 1L;
	
	private List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
	
	public SimpleTableModel(){}
	
	public void removeAllRows(){
		for(int i=0;i<getRowCount();i++){
			removeRow(i);
		}
	}
	
	@Override
	public int getColumnCount() {
		return columnInfos.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return columnInfos.get(column).getColumnName();
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		return columnInfos.get(column).getColumnClass();
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return columnInfos.get(column).isEditable();
	}
	
	public SimpleTableModel addColumn(String columnName){
		return addColumn(columnName, String.class, true);
	}
	
	public SimpleTableModel addColumn(String columnName, Class<?> columnClass){
		return addColumn(columnName, columnClass, true);
	}
	
	public SimpleTableModel addColumn(String columnName, Class<?> columnClass, boolean editable){
		ColumnInfo ci = new ColumnInfo();
		ci.setColumnName(columnName);
		ci.setColumnClass(columnClass);
		ci.setEditable(editable);
		columnInfos.add(ci);
		return this;
	}
	
	static class ColumnInfo {
		private String columnName;
		private Class<?> columnClass;
		boolean editable;
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		public Class<?> getColumnClass() {
			return columnClass;
		}
		public void setColumnClass(Class<?> columnClass) {
			this.columnClass = columnClass;
		}
		public boolean isEditable() {
			return editable;
		}
		public void setEditable(boolean editable) {
			this.editable = editable;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((columnName == null) ? 0 : columnName.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColumnInfo other = (ColumnInfo) obj;
			if (columnName == null) {
				if (other.columnName != null)
					return false;
			} else if (!columnName.equals(other.columnName))
				return false;
			return true;
		}		
	}
}
