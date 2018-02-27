package com.yami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yami.game.Main;

public class GameOverScreen extends DefaultScreen{
	
	private final String gameOver = "GAME OVER";
	
	private Stage stage;
	
	private Label scoreLabel;
	private Label timeLabel;
	private TextButton menuButton;
	private TextButton retryButton;
	
	private BitmapFont font;
	private GlyphLayout glyphLayout;
	private float width;
	private float scale;
	
	private float r, g, b;

	private TextureRegion buttonDown, buttonOver, buttonUp;
	
	private Camera camera;
	
	private Viewport viewport2;
	
	public GameOverScreen(final Main game, final GameScreen gs, int finalScore, int finalTime) {
		super(game);
		
		camera = new OrthographicCamera();
		
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT);
		viewport2 = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
		
		stage = new Stage(viewport, game.batch);
		
		font = game.getManager().get(Main.futureFont, BitmapFont.class);
		buttonUp = game.buttonUp;
		buttonDown = game.buttonDown;
		buttonOver = game.buttonOver;
		
		glyphLayout = new GlyphLayout();
		width = 0;
		scale = 2.0f;
		
		r = MathUtils.random();
		g = MathUtils.random();
		b = MathUtils.random();
		
		scoreLabel = new Label("SCORE: "+finalScore,
				new Label.LabelStyle(font,
						Color.WHITE));
		
		timeLabel = new Label("TIME: "+finalTime,
				new Label.LabelStyle(font, Color.WHITE));
		
		TextButton.TextButtonStyle tbs =  new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up   = new TextureRegionDrawable(new TextureRegion(buttonUp));
		tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDown));
		tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOver));
		
		tbs.downFontColor = Color.RED;
		tbs.overFontColor = Color.GREEN;
		tbs.fontColor 	  = Color.BLUE;
		
		retryButton = new TextButton("RETRY", tbs);
		menuButton = new TextButton("MENU", tbs);
		
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenuScreen(game));
			};
		});
		
		retryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(gs);
			};
		});
		
		Table table = new Table();
		table.getColor().a = 0.7f;
		table.setFillParent(true);
		table.pack();
		
		table.row();
		table.add(scoreLabel).padTop(10f).colspan(2).expandX();
		table.row();
		table.add(timeLabel).padTop(10f).colspan(2).expandX();
		table.row();
		table.add(retryButton).padTop(70f).expandX();
		table.add(menuButton).padTop(70f).expandX();
		
		
		stage.addActor(table);
		
	}
	
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void render(float dt) {
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		if(font.getScaleX() != scale) {
			font.getData().setScale(scale);
			glyphLayout.setText(font, gameOver);
			width = glyphLayout.width;
		}
		
		if(r > 0.0f)
			r += 0.005f;
		
		if (r > 1.0f)
			r = MathUtils.random();
		
		if(g > 0.0f)
			g += 0.005f;
		if (g > 1.0f)
			g = MathUtils.random();
		
		if(b > 0.0f)
			b += 0.005f;
		if (b > 1.0f)
			b = MathUtils.random();
		
		font.setColor(r, g, b, 0.5f);
		
		font.draw(game.batch, gameOver, -width/2, 200);
		font.getData().setScale(1f);
		game.batch.end();
		
		game.batch.setProjectionMatrix(stage.getCamera().combined);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport2.update(width, height);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
