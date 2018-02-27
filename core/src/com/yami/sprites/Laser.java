package com.yami.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.yami.game.Main;
import com.yami.screens.GameScreen;

public class Laser extends GameObject{
	
	private static final float MAX_SPEED = 35;
		
	public Laser(Vector2 position, Vector2 velocity, Sprite sprite, GameScreen gameScreen) {
		super(position, velocity, sprite, gameScreen);
		init(position, velocity);
	}
	
	protected void update(float dt) {
		position = position.add(velocity);
		
		if(position.x > Main.V_WIDTH / 2 + this.getWidth()||
				position.x < -Main.V_WIDTH / 2 - this.getWidth()
				|| position.y > Main.V_HEIGHT / 2 + this.getHeight() ||
				position.y < -Main.V_HEIGHT / 2 - this.getHeight()) {
			this.destroy();
		}
		
		this.setPosition(position.x, position.y);
		collidesWith();
	}
	
	public Vector2 getCenter() {
		Vector2 c = super.getCenter();
		Vector2 v = velocity.cpy().nor();
		return c.add(v.scl(getOriginY() + getOriginX()));
	}
	
	public void init(Vector2 position, Vector2 velocity) {
		this.position = position;
		this.position = position.add(-this.getWidth() / 2, -this.getHeight() / 2);
		this.velocity = velocity;
		this.velocity = velocity.scl(MAX_SPEED);
		this.setRotation(velocity.angle() - 90);
		isDead = false;
	}
}
