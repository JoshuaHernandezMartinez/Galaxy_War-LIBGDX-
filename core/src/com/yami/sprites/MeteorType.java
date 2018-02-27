package com.yami.sprites;

public enum MeteorType {
	BIG(2),
	MED(2),
	SMALL(2),
	TINY(0);
	
	public int size;
	
	MeteorType(int size) {
		this.size = size;
	}
}