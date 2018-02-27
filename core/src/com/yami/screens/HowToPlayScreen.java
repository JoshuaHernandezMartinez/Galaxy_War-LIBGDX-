package com.yami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yami.game.Main;

public class HowToPlayScreen extends DefaultScreen{
	
	private Stage stage;
	private TextButton returnButton;	
	private Label title, move, shoot, fastFire,
					pill, scoreStack, shield, doubleGun, life;
	
	private TextureRegion buttonUp, buttonDown, buttonOver;
	private Texture background;
	private BitmapFont font;
	private OrthographicCamera camera;
	
	public HowToPlayScreen(final Main game, final MainMenuScreen mainMenu) {
		super(game);
		
		background = game.getManager().get(Main.howToPlayScreen, Texture.class);
		
		camera = new OrthographicCamera();
		
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
		
		stage = new Stage(viewport, game.batch);
		
		font = game.getManager().get(Main.futureFont, BitmapFont.class);
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
				game.setScreen(mainMenu);
			};
		});
		stage.addActor(returnButton);
		
		Label.LabelStyle ls = new Label.LabelStyle();
		ls.font = font;
		ls.fontColor = Color.CHARTREUSE;
		
		title = new Label("INSTRUCCIONS", ls);
		title.setPosition(Main.V_WIDTH/2 - 200, Main.V_HEIGHT/2 + 300);
		title.setFontScale(2.0f);
		stage.addActor(title);
		
		move = new Label("MOVE SHIP", ls);
		move.setPosition(Main.V_WIDTH/2 - 250, Main.V_HEIGHT/2 + 100);
		stage.addActor(move);
		
		shoot = new Label("SHOOT LASER", ls);
		shoot.setPosition(Main.V_WIDTH/2 - 270, Main.V_HEIGHT/2 - 75);
		stage.addActor(shoot);
		
		fastFire = new Label("SHOOT TWICE FASTER", ls);
		fastFire.setPosition(Main.V_WIDTH/2 - 350, Main.V_HEIGHT/2 - 225);
		stage.addActor(fastFire);
		
		pill = new Label("GET DOUBLE SCORE", ls);
		pill.setPosition(Main.V_WIDTH/2 - 320, Main.V_HEIGHT/2 - 375);
		stage.addActor(pill);
		
		scoreStack = new Label("500 SCORE", ls);
		scoreStack.setPosition(Main.V_WIDTH/2 + 460, Main.V_HEIGHT/2 + 75);
		stage.addActor(scoreStack);
		
		shield = new Label("PUSH ASTEROIDS AWAY", ls);
		shield.setPosition(Main.V_WIDTH/2 + 400, Main.V_HEIGHT/2 - 75);
		stage.addActor(shield);
		
		doubleGun = new Label("DOUBLE GUNS", ls);
		doubleGun.setPosition(Main.V_WIDTH/2 + 450, Main.V_HEIGHT/2 - 225);
		stage.addActor(doubleGun);
		
		life = new Label("GET A LIFE", ls);
		life.setPosition(Main.V_WIDTH/2 + 460, Main.V_HEIGHT/2 - 350);
		stage.addActor(life);
		
		
	}
	
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	public void render(float dt) {
		
		game.batch.begin();
		game.batch.draw(background, -Main.V_WIDTH/2, -Main.V_HEIGHT/2,
				Main.V_WIDTH, Main.V_HEIGHT, 0, 0,
				background.getWidth(), background.getHeight(), false, false);
		game.batch.end();
		
		game.batch.setProjectionMatrix(camera.combined);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public void dispose() {stage.dispose();}
	
}
