package com.siberhus.hswing.util;

import java.io.Serializable;

public class KeyLabel <K, L> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private K key;
	private L label;
	
	public KeyLabel(){}
	
	public KeyLabel(K key, L label){
		this.key = key;
		this.label = label;
	}
	
	@Override
	public String toString(){
		return label==null?null:label.toString();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		KeyLabel<K,L> other = (KeyLabel<K,L>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public L getLabel() {
		return label;
	}

	public void setLabel(L label) {
		this.label = label;
	}
}
