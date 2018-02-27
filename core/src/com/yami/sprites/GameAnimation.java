package com.yami.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GameAnimation {
	private Animation<TextureRegion> animation;
	private Vector2 position;
	private float animationTime;
	private boolean isOver;
	private float width, height;
	private float scale;
	private float rotation;
	
	public GameAnimation(Animation<TextureRegion> animation, Vector2 position,
			float width, float height, float scale, float rotation) {
		this.animation = animation;
		this.position = position;
		this.scale = scale;
		animationTime = 0.0f;
		isOver = false;
		this.width = width;
		this.height = height;
		this.rotation = rotation;

	}
	
	public void render(Batch batch) {
		animationTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = animation.getKeyFrame(animationTime);
		frame.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		float w = frame.getRegionWidth() * 0.5f;
		float h = frame.getRegionHeight() * 0.5f;
		Color c = batch.getColor();
		batch.setColor(c.r, c.g, c.b, 1.0f);
		batch.draw(frame, position.x - w + width, position.y - h + height,
				w, h, frame.getRegionWidth(), frame.getRegionHeight(),
				scale, scale, rotation);
		
		if(animation.isAnimationFinished(animationTime)) {
			isOver = true;
		}
	}
	
	public boolean isOver() {return isOver;}
	public void setPosition(Vector2 position) {this.position = position;}
	public void setRotation(float rotation) {this.rotation = rotation;}
}
