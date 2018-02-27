package com.yami.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.yami.game.Main;
import com.yami.screens.GameScreen;

public class PowerUp extends GameObject{
	
	private final float POWER_UP_DURATION = 10f;
	
	private float time;
	private Action action;
	private Sound powerUpPick;
	private Sprite orb;
	
	public PowerUp(Vector2 position,
			Sprite texture, GameScreen gameScreen, Action action) {
		super(position, null, texture, gameScreen);
		this.action = action;
		orb = gameScreen.getGame().powerUpOrbSprite;
		orb.setPosition(position.x - getOriginX(),
				position.y - getOriginY());
		powerUpPick = gameScreen.getGame().getManager().get(Main.powerUp, Sound.class);
		time = 0;
	}

	@Override
	protected void update(float dt) {
		this.rotate(5f);
		time += dt;
		if(time > POWER_UP_DURATION) {
			this.destroy();
		}
		
		collidesWith();
	}
	
	@Override
	public void draw(Batch batch, float dt) {
		update(dt);
		orb.draw(batch);
		super.draw(batch);
	}
	
	public void executeAction() {
		action.doAction();
		powerUpPick.play(1.0f);
	}
	
	@Override
	public float getRadius() {
		return orb.getWidth() - getOriginX();
	}	
}
