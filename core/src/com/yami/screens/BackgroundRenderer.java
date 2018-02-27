package com.yami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.yami.game.Main;
import com.yami.sprites.Meteor;
import com.yami.sprites.MeteorType;

public class BackgroundRenderer {
	
	private final int SIZE = 10;
	
	private Meteor[] meteors;
	
	private Sprite background;
	
	private Main game;
	
	public BackgroundRenderer(Main game) {
		this.game = game;
		float x, y;
		meteors = new Meteor[SIZE];
		
		for(int i = 0; i < SIZE; i++)
		{
			x = MathUtils.random(-Main.V_WIDTH/2, Main.V_WIDTH/2);
			y = (MathUtils.random(-Main.V_HEIGHT/2, Main.V_HEIGHT/2));
			
			Sprite sprite = game.meteorGrayBigs[MathUtils.random(3)];
			
			meteors[i] = new Meteor(
					new Vector2(x, y),
					MeteorType.BIG,
					sprite,
					null);
		}
		
		background = new Sprite(game.getManager().get(Main.background, Texture.class));
		background.setPosition(-Main.V_WIDTH/2, -Main.V_HEIGHT/2);
		
	}
	
	public void render(SpriteBatch batch, float dt) {
		Gdx.gl20.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		background.draw(batch);
		if(!(game.getScreen() instanceof GameScreen) &&
				!(game.getScreen() instanceof CreditScreen)) {
			for(int i = 0; i < SIZE; i++)
				meteors[i].draw(batch, dt);
		}
			
		
	}
	
}
