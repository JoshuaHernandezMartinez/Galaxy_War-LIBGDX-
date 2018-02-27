package com.yami.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.yami.game.Main;

public class Message {
	
	private String text;
	private Vector2 position;
	private Color color;
	private float alpha;
	private boolean isOver;
	private boolean fadeIn;
	private float delta;
	private float scale;
	private float width;
	private GlyphLayout glyphLayout;
	private boolean centered;
	
	private BitmapFont font;
	
	public Message(String text, Vector2 position, Color color,
			float scale, boolean centered, Main game) {
		this.text = text;
		this.position= position;
		this.color = color;
		this.scale = scale;
		this.centered = centered;
		isOver = false;
		alpha = 0;
		delta = 0.015f;
		fadeIn = true;
		glyphLayout = new GlyphLayout();
		font = game.getManager().get(Main.futureFont, BitmapFont.class);
	}
	
	public void draw(Batch batch) {
		BitmapFont f = font;
		f.setColor(color);
		if(f.getScaleX() != scale)
			f.getData().setScale(scale);
		if(centered) {
			glyphLayout.setText(f, text);
			width = glyphLayout.width;
		}else {
			width = 0;
		}
		if(alpha < 1.0f && alpha > 0f)
			f.getColor().a = alpha;
		f.draw(batch, text, position.x - width*0.5f, position.y);
		f.getData().setScale(1.0f);
		f.getColor().a = 1;
		if(fadeIn)
			alpha += delta;
		else
			alpha -= delta;
		position.y += 2;
		
		if(alpha > 1.0f)
			fadeIn = false;
		
		if(alpha < 0 && !fadeIn)
			isOver = true;
	}
	
	public boolean isOver() {return isOver;}
	
}
