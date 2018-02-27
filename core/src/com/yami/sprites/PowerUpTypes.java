package com.yami.sprites;

public enum PowerUpTypes {
	SHIELD("SHIELD"),
	LIFE("+1 LIFE"),
	GUN_X2("DOUBLE GUN"),
	SCORE_X2("SCORE x2"),
	FASTER_FIRE("FAST FIRE"),
	SCORE_STACK("+300 SCORE");
	
	// fields
	public String text;
	
	private PowerUpTypes(String text){
		this.text = text;
	}
}
