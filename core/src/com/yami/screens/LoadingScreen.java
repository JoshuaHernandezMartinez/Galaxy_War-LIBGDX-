package com.yami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yami.game.Main;

public class LoadingScreen extends DefaultScreen{	
	
	private Texture progressBarImg, progressBarBaseImg, background1, logo;
	private Vector2 pbPos, logoPos;
	
	public LoadingScreen(Main game) {
		super(game);
		progressBarImg = game.getManager().get("progress_bar.png");
		progressBarBaseImg = game.getManager().get("progress_bar_base.png");
		background1 = game.getManager().get("background1.png");
		logo = game.getManager().get("logo.png");
		
		logoPos = new Vector2();
		logoPos.set((Gdx.graphics.getWidth()-logo.getWidth())>>1,
				Gdx.graphics.getHeight()>>1);
		
		pbPos = new Vector2();
		pbPos.set(logoPos.x, logoPos.y - (logo.getHeight()));
		
	}
	
	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		game.batch.draw(background1, 0, 0);
		game.batch.draw(logo, logoPos.x, logoPos.y);
		game.batch.draw(progressBarBaseImg, pbPos.x, pbPos.y);
		game.batch.draw(progressBarImg, pbPos.x, pbPos.y,
				progressBarImg.getWidth() * game.getManager().getProgress(),
				progressBarImg.getHeight());
		game.batch.end();
		
		if(game.getManager().update()) {
			game.initializeStuff();
			game.setScreen(new MainMenuScreen(game));
		}	
	}	
}
