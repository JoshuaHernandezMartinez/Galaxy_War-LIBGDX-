package com.yami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yami.game.Main;

public class MainMenuScreen extends DefaultScreen{
	
	private Camera camera;
	
	private final float ACTOR_ALPHA = 0.7f;
	
	private Stage stage;
	
	private TextButton playButton, exitButton, optionsButton, creditsButton;
	private CheckBox audioCheckBox;
	private Slider slider;
	private Label title;	
	private Table table;
	
	private BitmapFont font;
	
	private TextureRegion buttonUp, buttonDown, buttonOver;
	private TextureRegion sliderTexture, knob;
	private TextureRegion checkBoxOn, checkBoxOff;
	
	private Music menuMusic;
	
	private float lastVolume;
	
	private MainMenuScreen instance;
	
	public MainMenuScreen(final Main game) {
		super(game);
		
		camera = new OrthographicCamera();
		
		instance = this;
		
		viewport = new FitViewport(Main.V_WIDTH*0.7f,
				Main.V_HEIGHT*0.7f, camera);
		
		stage = new Stage(viewport, game.batch);	
		
		font = game.getManager().get(Main.futureFont, BitmapFont.class);
		
		buttonUp = game.buttonUp;
		buttonDown = game.buttonDown;
		buttonOver = game.buttonOver;
		
		sliderTexture = game.sliderText;
		knob = game.knobText;
		
		checkBoxOn = game.audioOnText;
		checkBoxOff = game.audioOffText;
		
		Label.LabelStyle ls = new Label.LabelStyle();
		ls.font = font;
		ls.fontColor = Color.CHARTREUSE;
		title = new Label("GALAXY WAR", ls);
		title.getColor().a = 1.0f;
		
		TextButton.TextButtonStyle tbs =  new TextButton.TextButtonStyle();
		tbs.font = font;
		tbs.up   = new TextureRegionDrawable(new TextureRegion(buttonUp));
		tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDown));
		tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOver));
		tbs.downFontColor = Color.RED;
		tbs.overFontColor = Color.GREEN;
		tbs.fontColor 	  = Color.BLUE;
		
		playButton 		= new TextButton("PLAY", tbs);
		exitButton 		= new TextButton("EXIT", tbs);
		optionsButton = new TextButton("OPTIONS", tbs);
		creditsButton = new TextButton("CREDITS", tbs);
		
		playButton.getColor().a = ACTOR_ALPHA;
		exitButton.getColor().a = ACTOR_ALPHA;
		optionsButton.getColor().a = ACTOR_ALPHA;
		creditsButton.getColor().a = ACTOR_ALPHA;
		
		Slider.SliderStyle ss = new Slider.SliderStyle();
		ss.background = new TextureRegionDrawable(new TextureRegion(sliderTexture));
		ss.knob = new TextureRegionDrawable(new TextureRegion(knob));
		slider = new Slider(0.0f, 1f, 0.01f, false, ss);
		slider.setValue(0.5f);
		slider.getColor().a = ACTOR_ALPHA;
		
		CheckBox.CheckBoxStyle cbs =  new CheckBox.CheckBoxStyle();
		cbs.checkboxOn = new TextureRegionDrawable( new TextureRegion(
		checkBoxOff));
		cbs.checkboxOff = new TextureRegionDrawable( new TextureRegion(
		checkBoxOn));
		cbs.font = font;
		cbs.fontColor = Color.WHITE;
		audioCheckBox = new CheckBox("", cbs);
		
		menuMusic = game.getManager().get(Main.backgroundMusic, Music.class);
		menuMusic.play();
		menuMusic.setVolume(0.5f);
		menuMusic.setLooping(true);
		
		table = new Table();
		table.setFillParent(true);
		table.pack();
		
		table.row();
		table.add(title).padTop(30f).expand().colspan(2);
		table.row();
		table.add(playButton).padTop(30f).expand().colspan(2);
		table.row();
		table.add(optionsButton).padTop(30f).expand().colspan(2);
		table.row();
		table.add(creditsButton).padTop(30f).expand().colspan(2);
		table.row();
		table.add(exitButton).padTop(30f).expand().colspan(2);
		table.row();
		table.add(audioCheckBox).align(Align.left).padLeft(100f).padBottom(50f);
		table.add(slider).align(Align.left).padRight(1000f).padBottom(50f);
		
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game, menuMusic));
			};
		});
		
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			};
		});
		
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new HowToPlayScreen(game, instance));
			};
		});
		
		creditsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new CreditScreen(game, instance));
			};
		});
		
		slider.addListener(new InputListener() {
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				menuMusic.setVolume(slider.getValue());
				if(slider.getValue() == 0)
					audioCheckBox.setChecked(true);
				else
					audioCheckBox.setChecked(false);
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			};
		});
		
		audioCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean checked = audioCheckBox.isChecked();
				if(checked) {
					lastVolume = menuMusic.getVolume();
					slider.setValue(0);
					menuMusic.setVolume(0);
				}else {
					slider.setValue(lastVolume);
					menuMusic.setVolume(lastVolume);
				}
					
			};
		});
		
		stage.addActor(table);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void render(float delta) {		
        game.batch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
        
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public void dispose() {
		stage.dispose();
	}
}
