package com.yami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yami.game.Main;

public class CreditScreen extends DefaultScreen{
	
	private BitmapFont font;
	private GlyphLayout glyphLayout;
	private float width;
	
	private final String programming = "DESIGNED && PROGRAMMED";
	private final String programner = "JOSHUA HERNANDEZ";
	private final String art = "GAME ART";
	private final String artist = "KENNEY";
	private final String menuMusic = "DREAMY AMBIENT BACKGROUND MUSIC";
	private final String gameMusic = "PSYCHEDELIC TRIP - ELECTRONIC MUSIC BEAT";
	private final String musician = "ALEXANDER BLUE";
	private final String license = "CREATIVE COMMONS ATTRIBUTION"
			+ " 4.0 INTERNATIONAL LICENSE";
	private final String BY = "BY";
	private final String MUSIC = "MUSIC";
	
	private Vector2 position;
	private final float HEIGHT_DISTANCE = 80f;
	
	private Stage stage;
	private TextButton returnButton;
	private OrthographicCamera camera;
	private TextureRegion buttonUp, buttonDown, buttonOver;
	
	public CreditScreen(final Main game, final MainMenuScreen mainMenuScreen) {
		super(game);
		glyphLayout = new GlyphLayout();
		font = game.getManager().get(Main.futureFont, BitmapFont.class);
		width = 0;
		position = new Vector2(0, Main.V_HEIGHT / 2);
		
		camera = new OrthographicCamera();
		
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
		
		stage = new Stage(viewport, game.batch);
		
		buttonUp = game.buttonUp;
		buttonDown = game.buttonDown;
		buttonOver = game.buttonOver;
		
		TextButton.TextButtonStyle tbs =  new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up   = new TextureRegionDrawable(new TextureRegion(buttonUp));
		tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDown));
		tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOver));
		tbs.downFontColor = Color.RED;
		tbs.overFontColor = Color.GREEN;
		tbs.fontColor 	  = Color.BLUE;
		
		returnButton = new TextButton("RETURN", tbs);
		returnButton.setPosition(100, 50);
		

		
		returnButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(mainMenuScreen);
			};
		});
		
		stage.addActor(returnButton);
		
	}
	
	@Override
	public void render(float dt) {
		
		game.batch.begin();
		
		font.setColor(Color.WHITE);
		
		glyphLayout.setText(font, programming);
		width = glyphLayout.width;
		position.set(-width/2, Main.V_HEIGHT / 2 - 100f);
		font.draw(game.batch, programming, position.x, position.y);
		
		glyphLayout.setText(font, BY);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, BY, position.x, position.y);
		
		glyphLayout.setText(font, programner);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, programner, position.x, position.y);
		
		glyphLayout.setText(font, art);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, art, position.x, position.y);
		
		glyphLayout.setText(font, BY);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, BY, position.x, position.y);
		
		glyphLayout.setText(font, artist);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, artist, position.x, position.y);
		
		glyphLayout.setText(font, MUSIC);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, MUSIC, position.x, position.y);
		
		glyphLayout.setText(font, menuMusic);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, menuMusic, position.x, position.y);
		
		glyphLayout.setText(font, gameMusic);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, gameMusic, position.x, position.y);
		
		glyphLayout.setText(font, musician);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, musician, position.x, position.y);
		
		glyphLayout.setText(font, license);
		width = glyphLayout.width;
		position.set(-width/2, position.y - HEIGHT_DISTANCE);
		font.draw(game.batch, license, position.x, position.y);
		
		game.batch.end();
		
		game.batch.setProjectionMatrix(camera.combined);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}
	
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public void dispose() {stage.dispose();}
	
}
