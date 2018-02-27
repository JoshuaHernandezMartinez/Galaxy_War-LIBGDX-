package com.yami.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yami.screens.BackgroundRenderer;
import com.yami.screens.LoadingScreen;

public class Main extends Game {
	
	public static final float V_WIDTH = 1920f;
	public static final float V_HEIGHT = 1080f;
	
	public SpriteBatch batch;
	
	private AssetManager manager;
	
	public LoadingScreen loadingScreen;
	
	public static final String gameAssets = "GalaxyWarSheet.atlas";
	
	public static final String blueShip = "player";
	public static final String blueShipDoubleGun = "playerDoubleGun";
	public static final String propulsion = "propulsion";
	public static final String blueLaser = "laserBlue";
	public static final String background = "background.png";
	public static final String howToPlayScreen= "instructions.png";
	public static final String blueUfo = "ufoBlue";
	
	public static final String meteorBrown_big1 = "meteorBrown_big1";
	public static final String meteorBrown_big2 = "meteorBrown_big2";
	public static final String meteorBrown_big3 = "meteorBrown_big3";
	public static final String meteorBrown_big4 = "meteorBrown_big4";
	
	public static final String meteorGrey_big1 = "meteorGrey_big1";
	public static final String meteorGrey_big2 = "meteorGrey_big2";
	public static final String meteorGrey_big3 = "meteorGrey_big3";
	public static final String meteorGrey_big4 = "meteorGrey_big4";
	
	public static final String meteorBrown_med1 = "meteorBrown_med1";
	public static final String meteorBrown_med2 = "meteorBrown_med2";
	
	public static final String meteorGrey_med1 = "meteorGrey_med1";
	public static final String meteorGrey_med2 = "meteorGrey_med2";
	
	public static final String meteorBrown_small1 = "meteorBrown_small1";
	public static final String meteorBrown_small2 = "meteorBrown_small2";
	
	public static final String meteorGrey_small1 = "meteorGrey_small1";
	public static final String meteorGrey_small2 = "meteorGrey_small2";
	
	public static final String meteorBrown_tiny1 = "meteorBrown_tiny1";
	public static final String meteorBrown_tiny2 = "meteorBrown_tiny2";
	
	public static final String meteorGrey_tiny1 = "meteorGrey_tiny1";
	public static final String meteorGrey_tiny2 = "meteorGrey_tiny2";
	
	public static final String shield_silver = "shield_silver";
	public static final String playerLife1_blue = "playerLife1_blue";
	public static final String things_silver = "things_silver";
	public static final String pill_blue = "pill_blue";
	public static final String bold_silver = "bold_silver";
	public static final String star_silver = "star_silver";
	public static final String powerUpOrb = "powerUp";
	
	public static final String button1 = "button0";
	public static final String button2 = "button1";
	public static final String button3 = "button2";
	
	public static final String knob = "knob";
	public static final String slider = "slider";
	public static final String audioOn = "audioOn";
	public static final String audioOff = "audioOff";
	public static final String touchpad_background = "touchpad_background";
	public static final String touchpad_knob = "touchpad_knob";
	
	public static final String explosionAtlas = "explosion.atlas";
	public static final String shieldEffectAtlas = "shieldEffect.atlas";
	
	public static final String shoot = "shoot.wav";
	public static final String shoot2 = "shoot2.wav";
	public static final String powerUp = "powerUp.wav";
	public static final String loose = "loose.wav";
	public static final String explosion = "explosion.wav";
	public static final String backgroundMusic = "backgroundMusic.ogg";
	public static final String backgroundMusic2 = "backgroundMusic2.ogg";
	
	public static final String futureFont = "futureFont.fnt";
	
	private Camera camera;
	private Viewport viewport;
	
	/*
	 *  Game Sprites
	 */
	
	public Sprite[] meteorBrownBigs;
	public Sprite[] meteorGrayBigs;
	
	public Sprite[] meteorBrownMeds;
	public Sprite[] meteorGrayMeds;
	
	public Sprite[] meteorBrownSmalls;
	public Sprite[] meteorGraySmalls;
	
	public Sprite[] meteorBrownTinies;
	public Sprite[] meteorGrayTinies;
	
	public Sprite player;
	public Sprite blueLaserSprite;
	public Sprite ufo;
	public TextureRegion playerDoubleGun;
	public TextureRegion propulsionTexture;
	
	public Sprite shieldPowerUp;
	public Sprite doubleGunPowerUp;
	public Sprite fastFirePowerUp;
	public Sprite scoreStackPowerUp;
	public Sprite doubleScorePowerUp;
	public Sprite playerLife;
	public Sprite powerUpOrbSprite;	
	
	// background renderer
	
	public TextureRegion buttonUp;
	public TextureRegion buttonOver;
	public TextureRegion buttonDown;
	
	public TextureRegion knobText;
	public TextureRegion sliderText;
	public TextureRegion audioOnText;
	public TextureRegion audioOffText;
	
	public TextureRegion touchpad_backgroundText;
	public TextureRegion touchpad_knobText;
	
	private  BackgroundRenderer backgroundRenderer;
	
	private TextureAtlas gameAtlas;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		
		manager.load("progress_bar.png", Texture.class);
		manager.load("progress_bar_base.png", Texture.class);
		manager.load("background1.png", Texture.class);
		manager.load("logo.png", Texture.class);
		
		manager.finishLoading();
		
		loadingScreen = new LoadingScreen(this);
		
		this.setScreen(loadingScreen);
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
		
		manager.load(gameAssets, TextureAtlas.class);
		
		manager.load(background, Texture.class);
		manager.load(howToPlayScreen, Texture.class);
		
		manager.load(explosionAtlas, TextureAtlas.class);
		manager.load(shieldEffectAtlas, TextureAtlas.class);
		
		// sounds
		
		manager.load(shoot, Sound.class);
		manager.load(shoot2, Sound.class);
		manager.load(powerUp, Sound.class);
		manager.load(loose, Sound.class);
		manager.load(explosion, Sound.class);
		
		manager.load(backgroundMusic, Music.class);
		manager.load(backgroundMusic2, Music.class);
		
		// fonts
		
		manager.load(futureFont, BitmapFont.class);
		
	}

	@Override
	public void render () {	
		
		if(backgroundRenderer != null) {
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			backgroundRenderer.render(batch, Gdx.graphics.getDeltaTime());
			batch.end();
		}
		
		super.render();	
	}
	
	public void initializeStuff() {		
		
		gameAtlas = manager.get(gameAssets,
				TextureAtlas.class);
		
		player = new Sprite(gameAtlas.findRegion(blueShip));
		
		blueLaserSprite = new Sprite(gameAtlas.findRegion(blueLaser));
		blueLaserSprite.scale(0.5f);
		
		ufo = new Sprite(gameAtlas.findRegion(blueUfo));
		
		playerDoubleGun = gameAtlas.findRegion(blueShipDoubleGun);
		
		propulsionTexture = gameAtlas.findRegion(propulsion);
		
		shieldPowerUp = new Sprite(gameAtlas.findRegion(shield_silver));
		shieldPowerUp.scale(0.5f);
		doubleGunPowerUp = new Sprite(gameAtlas.findRegion(things_silver));
		doubleGunPowerUp.scale(0.5f);
		fastFirePowerUp = new Sprite(gameAtlas.findRegion(bold_silver));
		fastFirePowerUp.scale(0.5f);
		scoreStackPowerUp = new Sprite(gameAtlas.findRegion(star_silver));
		scoreStackPowerUp.scale(0.5f);
		doubleScorePowerUp = new Sprite(gameAtlas.findRegion(pill_blue));
		doubleScorePowerUp.scale(0.5f);
		playerLife = new Sprite(gameAtlas.findRegion(playerLife1_blue));
		playerLife.scale(0.5f);
		powerUpOrbSprite = new Sprite(gameAtlas.findRegion(powerUpOrb));
		powerUpOrbSprite.scale(1.0f);
		
		buttonUp = gameAtlas.findRegion(button1);
		buttonOver = gameAtlas.findRegion(button2);
		buttonDown = gameAtlas.findRegion(button3);
		
		knobText = gameAtlas.findRegion(knob);
		sliderText = gameAtlas.findRegion(slider);
		audioOnText = gameAtlas.findRegion(audioOn);
		audioOffText = gameAtlas.findRegion(audioOff);
		touchpad_backgroundText = gameAtlas.findRegion(touchpad_background);
		touchpad_knobText = gameAtlas.findRegion(touchpad_knob);
		
		meteorBrownBigs = new Sprite[4];
		meteorBrownBigs[0] = new Sprite(gameAtlas.findRegion(meteorBrown_big1));
		meteorBrownBigs[1] = new Sprite(gameAtlas.findRegion(meteorBrown_big2));
		meteorBrownBigs[2] = new Sprite(gameAtlas.findRegion(meteorBrown_big3));
		meteorBrownBigs[3] = new Sprite(gameAtlas.findRegion(meteorBrown_big4));
		
		for(int i = 0; i < meteorBrownBigs.length; i++)
			meteorBrownBigs[i].scale(1.0f);
		
		meteorGrayBigs = new Sprite[4];
		meteorGrayBigs[0] = new Sprite(gameAtlas.findRegion(meteorGrey_big1));
		meteorGrayBigs[1] = new Sprite(gameAtlas.findRegion(meteorGrey_big2));
		meteorGrayBigs[2] = new Sprite(gameAtlas.findRegion(meteorGrey_big3));
		meteorGrayBigs[3] = new Sprite(gameAtlas.findRegion(meteorGrey_big4));
		
		for(int i = 0; i < meteorGrayBigs.length; i++)
			meteorGrayBigs[i].scale(1.0f);
		
		meteorBrownMeds = new Sprite[2];
		meteorBrownMeds[0] = new Sprite(gameAtlas.findRegion(meteorBrown_med1));
		meteorBrownMeds[1] = new Sprite(gameAtlas.findRegion(meteorBrown_med2));
		
		for(int i = 0; i < meteorBrownMeds.length; i++)
			meteorBrownMeds[i].scale(1.0f);
		
		meteorGrayMeds = new Sprite[2];
		meteorGrayMeds[0] = new Sprite(gameAtlas.findRegion(meteorGrey_med1));
		meteorGrayMeds[1] = new Sprite(gameAtlas.findRegion(meteorGrey_med2));
		
		for(int i = 0; i < meteorGrayMeds.length; i++)
			meteorGrayMeds[i].scale(1.0f);
		
		meteorBrownSmalls = new Sprite[2];
		meteorBrownSmalls[0] = new Sprite(gameAtlas.findRegion(meteorBrown_small1));
		meteorBrownSmalls[1] = new Sprite(gameAtlas.findRegion(meteorBrown_small2));
		
		for(int i = 0; i < meteorBrownSmalls.length; i++)
			meteorBrownSmalls[i].scale(1.0f);
		
		meteorGraySmalls = new Sprite[2];
		meteorGraySmalls[0] = new Sprite(gameAtlas.findRegion(meteorGrey_small1));
		meteorGraySmalls[1] = new Sprite(gameAtlas.findRegion(meteorGrey_small2));
		
		for(int i = 0; i < meteorGraySmalls.length; i++)
			meteorGraySmalls[i].scale(1.0f);
		
		meteorBrownTinies = new Sprite[2];
		meteorBrownTinies[0] = new Sprite(gameAtlas.findRegion(meteorBrown_tiny1));
		meteorBrownTinies[1] = new Sprite(gameAtlas.findRegion(meteorBrown_tiny2));
		
		for(int i = 0; i < meteorBrownTinies.length; i++)
			meteorBrownTinies[i].scale(1.0f);
		
		meteorGrayTinies = new Sprite[2];
		meteorGrayTinies[0] = new Sprite(gameAtlas.findRegion(meteorGrey_tiny1));
		meteorGrayTinies[1] = new Sprite(gameAtlas.findRegion(meteorGrey_tiny2));		
		
		for(int i = 0; i < meteorGrayTinies.length; i++)
			meteorGrayTinies[i].scale(1.0f);
		
		Array<Texture> out = new Array<Texture>();
		
		out = manager.getAll(Texture.class, out);
		
		for(int i = 0; i < out.size; i++) {			
			Texture t = out.get(i);		
			t.setFilter(TextureFilter.Linear,
					TextureFilter.Linear);		
		}
		
		backgroundRenderer = new BackgroundRenderer(this);
		
	}
	
	@Override
	public void dispose() {
		manager.dispose();
		batch.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		this.getScreen().resize(width, height);
	}
	
	public AssetManager getManager() {return manager;}
}
