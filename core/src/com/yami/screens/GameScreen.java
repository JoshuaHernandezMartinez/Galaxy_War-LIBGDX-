package com.yami.screens;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yami.game.Main;
import com.yami.sprites.Action;
import com.yami.sprites.GameAnimation;
import com.yami.sprites.GameObject;
import com.yami.sprites.Laser;
import com.yami.sprites.Message;
import com.yami.sprites.Meteor;
import com.yami.sprites.MeteorType;
import com.yami.sprites.Player;
import com.yami.sprites.PowerUp;
import com.yami.sprites.PowerUpTypes;
import com.yami.sprites.Ufo;

public class GameScreen extends DefaultScreen{
	
	private final float POWER_UP_SPAWN_TIME = 10f;
	private final float UFO_SPAWN_TIME = 10f;
	private final float GAME_OVER_TIME = 4f;
	private final int SCORE_STACK = 500;
	public  static final float FRAME_DURATION = 0.04f;
	
	private OrthographicCamera camera;
	
	private Player player;
	
	private Array<GameObject> gameObjects;
	private Array<GameAnimation> explosions;
	private Array<Message> messages;
	
	private Comparator<GameObject> comparator;
	
	// game variables
	
	private int meteors;
	
	private int wavesCount;
	
	private Hud hud;
	
	private float powerUpSpawnTimer;
	private float ufoSpawnTime;
	private float gameOverTime;
	
	private boolean gameOver;
	
	private Sprite playerTexture, blueUfo, powerUpOrb;
	private Sprite blueLaser;
	
	private Music gameMusic, menuMusic;
	
	private TextureAtlas explosionAtlas;
	
	private int size;
	
	private Pool<Laser> laserPool;
	private Pool<Meteor> meteorPool;
	
	private GameScreen gameScreen;
	
	public GameScreen(final Main game, Music menuMusic) {
		super(game);
		camera = new OrthographicCamera();
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
		
		blueLaser =  game.blueLaserSprite;
		
		gameScreen = this;
		
		laserPool = new Pool<Laser>() {
			@Override
			protected Laser newObject() {
				return new Laser(new Vector2(), new Vector2(),
						blueLaser, gameScreen);
			}
		};
		
		meteorPool = new Pool<Meteor>() {
			@Override
			protected Meteor newObject() {
				return new Meteor(new Vector2(), null,
						game.meteorBrownBigs[0], gameScreen);
			}
		};
		
		comparator = new Comparator<GameObject>() {

			@Override
			public int compare(GameObject a, GameObject b) {
				if(a instanceof PowerUp)
					return -1;
				return 1;
			}
			
		};
		
		playerTexture = game.player;
		blueUfo = game.ufo;
		powerUpOrb = game.powerUpOrbSprite;
		
		explosionAtlas = game.getManager().get(Main.explosionAtlas, TextureAtlas.class);
		
		gameMusic = game.getManager().get(Main.backgroundMusic2, Music.class);
		this.menuMusic = menuMusic;
		
		explosions = new Array<GameAnimation>();
		gameObjects = new Array<GameObject>();
		messages = new Array<Message>();
		player = new Player(this, playerTexture);
		gameObjects.add(player);
		gameMusic.play();
		gameMusic.setLooping(true);
		gameMusic.setVolume(menuMusic.getVolume());
		
		powerUpSpawnTimer = 0;
		ufoSpawnTime = 0;
		gameOverTime = 0;
		wavesCount = 0;
		meteors = 0;
		
		gameOver = false;
		
		hud = new Hud(game.batch, this);
		startWave();
	}
	
	private void createMeteor(Vector2 pos, MeteorType type, Sprite texture) {
		Meteor m = meteorPool.obtain();
		m.init(pos, type, texture);
		gameObjects.add(m);
	}
	
	public void divideMeteor(Meteor meteor){
		Sprite[] textures = null;
		MeteorType type = meteor.getType();
		int size = type.size;
		MeteorType newType = null;
		boolean brown = wavesCount % 2 == 0;
		
		
		switch(type) {
			case BIG:
				newType = MeteorType.MED;
				if(brown)
					textures = game.meteorBrownMeds;
				else
					textures = game.meteorGrayMeds;
				break;
			case MED:
				newType = MeteorType.SMALL;
				if(brown)
					textures = game.meteorBrownSmalls;
				else
					textures = game.meteorGraySmalls;
				break;
			case SMALL:
				newType = MeteorType.TINY;
				if(brown)
					textures = game.meteorBrownTinies;
				else
					textures = game.meteorGrayTinies;
				break;
			case TINY:
				return;
		}
		
		for(int i = 0; i < size; i++){
			createMeteor(new Vector2(meteor.getCenter().x, meteor.getCenter().y),
					newType,
					textures[MathUtils.random(textures.length-1)]);
		}
	}
	
	private void startWave() {
		gameObjects.clear();
		gameObjects.add(player);
		player.resetPosition();
		meteors ++;
		wavesCount ++;
		addMessage("WAVE "+wavesCount, new Vector2(), Color.WHITE, 2f, true);
		
		Sprite[] meteorsBig = wavesCount % 2 == 0 ?
				game.meteorBrownBigs : game.meteorGrayBigs;
		
		float x, y;
		for(int i = 0; i < meteors; i++)
		{
			x = i % 2 == 0 ? (MathUtils.random(-Main.V_WIDTH/2, Main.V_WIDTH/2))
					: -Main.V_WIDTH/2;
			y = i % 2 == 0 ? -Main.V_HEIGHT/2
					: (MathUtils.random(-Main.V_HEIGHT/2, Main.V_HEIGHT/2));
			
			
			Sprite texture = meteorsBig[MathUtils.random(3)];
			
			createMeteor(new Vector2(x, y), MeteorType.BIG, texture);
		}
	}
	
	private void spawnUfo() {
		int i = MathUtils.random(1, 2);
		float y = MathUtils.random(-Main.V_HEIGHT, Main.V_HEIGHT);;
		float x = i == 1 ? -Main.V_WIDTH : Main.V_WIDTH;
		
		gameObjects.add(new Ufo(new Vector2(x, y),
				new Vector2(),
				blueUfo,
				this));
	}
	
	private void updateWorld(float delta) {
		powerUpSpawnTimer += delta;
		ufoSpawnTime += delta;
		
		if(gameOver) {
			gameOverTime += delta;
			if(gameOverTime > GAME_OVER_TIME) {
				game.setScreen(new GameOverScreen(game, this, hud.getScore(), hud.getTime()));
				gameOverTime = 0;
			}
		}
		
		if(powerUpSpawnTimer > POWER_UP_SPAWN_TIME) {
			powerUpSpawnTimer = 0;
			spawnPowerUp();
		}
		
		if(ufoSpawnTime > UFO_SPAWN_TIME) {
			ufoSpawnTime = 0;
			spawnUfo();
		}
		
		hud.update(delta);
		
		size = gameObjects.size;
		
		for(int i = 0; i < size; i++) 
			if(gameObjects.get(i) instanceof Meteor)
				return;
		
		startWave();
		
	}
	
	private void spawnPowerUp() {
		final float x = MathUtils.random(-Main.V_WIDTH / 2,
				Main.V_WIDTH / 2 - powerUpOrb.getWidth());
		final float y = MathUtils.random(-Main.V_HEIGHT / 2,
				Main.V_HEIGHT / 2 - powerUpOrb.getHeight());
		
		int index = (int)(MathUtils.random(PowerUpTypes.values().length-1));
		
		PowerUpTypes p = PowerUpTypes.values()[index];		
		
		final String text = p.text;
		Action action = null;
		Sprite t = null;
		
		switch(p) {
			case SHIELD:
				t = game.shieldPowerUp;
				action = new Action() {
					@Override
					public void doAction() {
						player.setShield();
						addMessage(text, new Vector2(x, y), Color.GREEN, 1f, false);
					}
				};
				break;
			case LIFE:
				t = game.playerLife;
				action = new Action() {
					@Override
					public void doAction() {
						hud.addLife(1, new Vector2(x, y));
					}
				};
				break;
			case GUN_X2:
				t = game.doubleGunPowerUp;
				action = new Action() {
					@Override
					public void doAction() {
						player.setDoubleGun();
						addMessage(text, new Vector2(x, y), Color.GREEN, 1f, false);
					}
				};
				break;
			case SCORE_X2:
				t = game.doubleScorePowerUp;
				action = new Action() {
					@Override
					public void doAction() {
						player.setDoubleScore();
						hud.setDoubleScoreText(true);
						addMessage(text, new Vector2(x, y), Color.GREEN, 1f, false);
					}
				};
				break;
			case FASTER_FIRE:
				t = game.fastFirePowerUp;
				action = new Action() {
					@Override
					public void doAction() {
						player.setFastFire();
						addMessage(text, new Vector2(x, y), Color.GREEN, 1f, false);
					}
				};
				break;
			case SCORE_STACK:
				t = game.scoreStackPowerUp;
				action = new Action() {
					@Override
					public void doAction() {
						hud.addScore(SCORE_STACK, new Vector2(x, y), Color.GREEN);
					}
				};
				break;
			default:
				break;	
		}

		gameObjects.add(new PowerUp(
				new Vector2(x, y),
				t,
				this,
				action
				));
		
	}
	
	@Override
	public void render(float delta) {	
		updateWorld(delta);
		
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		
		for(int i = 0; i < gameObjects.size; i++) {
			GameObject gameObject = gameObjects.get(i);
			if(gameObject.isDead()) {
				gameObjects.removeIndex(i);
				if(gameObject instanceof Laser) {
					laserPool.free((Laser)gameObject);
				}
				if(gameObject instanceof Meteor) {
					meteorPool.free((Meteor)gameObject);
				}
			}
			gameObject.draw(game.batch, delta);
		}
		
		gameObjects.sort(comparator);
		
		for(int i = 0; i < explosions.size; i++) {
			GameAnimation ga = explosions.get(i);
			ga.render(game.batch);
			if(ga.isOver())
				explosions.removeIndex(i);
			
		}
		
		for(int i = 0; i < messages.size; i++) {
			Message m = messages.get(i);
			m.draw(game.batch);
			if(m.isOver())
				messages.removeIndex(i);
			
		}
		game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.act(Gdx.graphics.getDeltaTime());
        hud.stage.draw();
        
		
	}
	
	public void addExplosions(Vector2 position, float width, float height) {
		Array<AtlasRegion> explosionRegions = 
				new Array<AtlasRegion>(explosionAtlas.getRegions());
		
		Animation<TextureRegion> anim = 
				new Animation<TextureRegion>(FRAME_DURATION, explosionRegions, PlayMode.NORMAL);
		
		explosions.add(new GameAnimation(anim, position, width, height, 1.0f, 0.0f));
		
	}
	
	public void addMessage(String text, Vector2 position, Color color, float scale, boolean centered) {
		messages.add(new Message(text, position, color, scale, centered, game));
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public void gameOver() {
		addMessage("GAME OVER", new Vector2(), Color.WHITE, 2f, true);
		gameOver = true;
	}
	
	@Override
	public void hide() {
		gameMusic.stop();
		menuMusic.play();
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(hud.stage);
		hud.reset();
		meteors = 0;
		wavesCount = 0;
		gameOver = false;
		player.reset();
		startWave();
		gameMusic.play();
		menuMusic.stop();
	}
	
	@Override
	public void dispose() {
		hud.dispose();
	}
	
	public Array<GameObject> getGameObjects(){return gameObjects;}
	public Player getPlayer() {return player;}
	public Hud getHud() {return hud;}
	public Main getGame() {return game;}
	public Pool<Laser> getLaserPool(){return laserPool;}
	
	
}
