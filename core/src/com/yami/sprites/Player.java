package com.yami.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yami.game.Main;
import com.yami.screens.GameScreen;

public class Player extends GameObject{
	
	private final float ANGLE_DT = 270f;
	private final float MAX_SPEED = 10;
	private final float ACC = 0.25f;
	private final float FIRE_RATE = 0.2f;
	private final float SHIELD_TIME = 20f;
	private final float DOUBLE_SCORE_TIME = 20f;
	private final float DOUBLE_GUN_TIME = 15f;
	private final float FAST_FIRE_TIME = 20f;
	private static final float FLICK_TIME = 0.15f;
	private static final float SPAWN_TIME = 3.5f; 
	
	private Vector2 heading;
	private Vector2 acceleration;
	private float accelerationForce;
	private float angle;
	private boolean accelerating;
	
	private boolean spawning;
	private boolean visible;
	
	private float fireRateTime;
	private float spawnTime;
	private float flickerTime;
	
	private boolean fastFireOn;
	private float fastFireTime;
	private float fireSpeed;
	
	private boolean shieldOn;
	private float shieldTime;
	private GameAnimation shieldAnimation;
	
	private boolean doubleScoreOn;
	private float doubleScoreTime;
	
	private boolean doubleGunOn;
	private float doubleGunOnTime;
	private Vector2 leftGun, rightGun;
	private TextureAtlas shieldEffectAtlas;
	private TextureRegion playerDoubleGun;
	private TextureRegion propulsion;
	private Sound shoot, loose;
	private Sprite normalPlayer;
	
	public Player(GameScreen gameScreen, Sprite texture) {
		super(new Vector2(-texture.getWidth()/2, -texture.getHeight()/2),
				new Vector2(), texture, gameScreen);
		heading = new Vector2(1, 0);
		acceleration = new Vector2();
		accelerating = false;
		accelerationForce = 0;
		angle = 90;
		fireRateTime = 0;
		spawning = false;
		visible = false;
		shieldOn = false;
		shieldTime = 0;
		normalPlayer = gameScreen.getGame().player;
		
		shieldEffectAtlas = gameScreen.getGame().getManager().get(
				Main.shieldEffectAtlas, TextureAtlas.class);
		
		playerDoubleGun =  gameScreen.getGame().playerDoubleGun;
		
		propulsion =  gameScreen.getGame().propulsionTexture;
		
		shoot = gameScreen.getGame().getManager().get(
				Main.shoot, Sound.class);
		
		loose = gameScreen.getGame().getManager().get(
				Main.loose, Sound.class);
		
		Array<AtlasRegion> shieldRegions = 
				new Array<AtlasRegion>(shieldEffectAtlas.getRegions());
		
		Animation<TextureRegion> shieldEffect = 
				new Animation<TextureRegion>(GameScreen.FRAME_DURATION * 1.5f,
						shieldRegions, PlayMode.LOOP);
		
		shieldAnimation = new GameAnimation(shieldEffect,
				position, getOriginX(), getOriginY(), 2.0f, this.getRotation());
		
		doubleScoreOn = false;
		doubleScoreTime = 0;
		
		doubleGunOn = false;
		doubleGunOnTime = 0;
		leftGun = new Vector2();
		rightGun = new Vector2();
		
		fastFireOn = false;
		fastFireTime = 0;
		fireSpeed = FIRE_RATE;
		
	}
	
	@Override
	protected void update(float delta) {
		
		fireRateTime += delta;
		
		if(shieldOn) shieldTime += delta;
		if(doubleScoreOn) doubleScoreTime += delta;
		if(doubleGunOn) doubleGunOnTime += delta;
		if(fastFireOn) {
			fireSpeed = FIRE_RATE / 2;
			fastFireTime += delta;
		}else {
			fireSpeed = FIRE_RATE;
		}
		
		if(fastFireTime > FAST_FIRE_TIME) {
			fastFireTime = 0;
			fastFireOn = false;
		}
		
		if(doubleGunOnTime > DOUBLE_GUN_TIME) {
			doubleGunOnTime = 0;
			doubleGunOn = false;
			normalPlayer.setRotation(this.getRotation());
			this.set(normalPlayer);
		}
		
		if(doubleScoreTime > DOUBLE_SCORE_TIME) {
			doubleScoreOn = false;
			gameScreen.getHud().setDoubleScoreText(false);
			doubleScoreTime = 0;
		}
			
		
		if(shieldTime > SHIELD_TIME) {
			shieldOn = false;
			shieldTime = 0;
		}
		
		if(spawning) {
			spawnTime += delta;
			flickerTime += delta;
			if(flickerTime > FLICK_TIME) {
				visible = !visible;
				flickerTime = 0;
			}
			if(spawnTime > SPAWN_TIME) {
				spawning = false;
				spawnTime = 0;
			}
		}else {
			visible = true;
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)){
			angle -= ANGLE_DT * delta;
			this.rotate(-ANGLE_DT * delta);
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)){
			this.rotate(ANGLE_DT * delta);
			angle += ANGLE_DT * delta;
		}
		
		Vector2 touchpadDir = gameScreen.getHud().getKnobPercent();
		if(touchpadDir.len2() != 0) {
			accelerationForce = ACC * touchpadDir.len();
			angle = touchpadDir.angle();
			this.setRotation(angle - 90);
		}else {
			accelerationForce = ACC;
		}
		
		heading = heading.setAngle(angle);
		
		if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)
				|| touchpadDir.len2() != 0) {
			acceleration = new Vector2(heading.x * accelerationForce,
					heading.y * accelerationForce);
			accelerating = true;
		}else {
			acceleration = new Vector2(-velocity.x, - velocity.y);
			acceleration = acceleration.nor();
			acceleration = acceleration.scl(ACC / 2);
			accelerating = false;
		}	
		
		int pointer = 0;
		if(touchpadDir.len2() != 0)
			pointer = 1;
		
		if(Gdx.input.isTouched(pointer) && fireRateTime > fireSpeed && !spawning) {
			if(doubleGunOn) {
				
				leftGun = getCenter();
				rightGun = getCenter();
				Vector2 temp = new Vector2(heading.x, heading.y);
				
				temp = temp.setAngle(angle - 30f);
				leftGun = leftGun.add(temp.scl(this.getOriginX()));
				temp = temp.setAngle(angle + 35f);
				temp = temp.nor();
				rightGun = rightGun.add(temp.scl(this.getOriginX()));	
				
				Laser l = gameScreen.getLaserPool().obtain();
				Laser r = gameScreen.getLaserPool().obtain();
				
				l.init(leftGun, new Vector2(heading.x, heading.y));
				r.init(rightGun, new Vector2(heading.x, heading.y));				
				
				gameScreen.getGameObjects().add(l);
				gameScreen.getGameObjects().add(r);				
				
			}else {
				
				Laser laser = gameScreen.getLaserPool().obtain();
				laser.init(new Vector2(getCenter().x + heading.x * getOriginX(),
						getCenter().y + heading.y * getOriginX()),
						new Vector2(heading.x, heading.y));
				
				gameScreen.getGameObjects().add(laser);
				
			}
			
			fireRateTime = 0;
			shoot.play();
		}
		
		velocity = velocity.add(acceleration);
		velocity.limit(MAX_SPEED);
		position = position.add(velocity);
		
		if(position.x >  Main.V_WIDTH /2 + this.getWidth())
			position.x = -Main.V_WIDTH/2 - this.getWidth();
		else if(position.x <  -Main.V_WIDTH/2 - this.getWidth())
			position.x =  Main.V_WIDTH /2 + this.getWidth();
		
		if(position.y >  Main.V_HEIGHT /2 + this.getHeight())
			position.y = -Main.V_HEIGHT/2 - this.getHeight();
		else if(position.y <  -Main.V_HEIGHT/2 - this.getHeight())
			position.y =  Main.V_HEIGHT /2 + this.getHeight();
			
		this.setPosition(position.x, position.y);
		
		if(shieldOn) {
			shieldAnimation.setPosition(position);
			shieldAnimation.setRotation(this.getRotation());
		}
		collidesWith();
	}
	
	@Override
	public void destroy() {
		gameScreen.addExplosions(position, this.getOriginX(), this.getOriginY());
		gameScreen.getHud().addLife(-1, new Vector2(position.x, position.y));
		resetPosition();
		loose.play();
		spawning = true;
		if(gameScreen.getHud().getLives() <= 0) {
			super.destroy();
			gameScreen.gameOver();
		}
	}
	
	@Override
	public void draw(Batch batch, float dt) {
		update(dt);
		if(visible) {			
			if(shieldOn) {
				shieldAnimation.setPosition(position);
				shieldAnimation.render(batch);
			}
			
			if(accelerating) {

				TextureRegion t = propulsion;
				
				float w = t.getRegionWidth();
				float h = t.getRegionHeight();
				float ori_x = -3;
				float ori_y = 40;
				
				batch.draw(t,
						position.x + getOriginX() - ori_x
						, position.y + getOriginY() - ori_y,
						ori_x, ori_y,
						w, h,
						2.5f, 2.5f,
						this.getRotation()
						);
				
				
				
				ori_x = 18;
				ori_y = 40;
				
				batch.draw(t,
						position.x + getOriginX() - ori_x
						, position.y + getOriginY() - ori_y,
						ori_x, ori_y,
						w, h,
						2.5f, 2.5f,
						this.getRotation());				
			}
			super.draw(batch);
		}
	}
	
	public boolean isSpawning() {
		return spawning;
	}
	
	public void resetPosition() {
		position = new Vector2(-getOriginX(), -getOriginY());
		this.setPosition(position.x, position.y);
	}
	
	public void setShield() {
		if(shieldOn)
			shieldTime = 0;
		shieldOn = true;
	}
	public void setDoubleGun() {
		if(doubleGunOn)
			doubleGunOnTime = 0;
		doubleGunOn = true;
		this.setRegion(playerDoubleGun);
	}
	public void setDoubleScore() {
		if(doubleScoreOn)
			doubleScoreTime = 0;
		doubleScoreOn = true;
	}
	public void setFastFire() {
		if(fastFireOn)
			fastFireTime = 0;
		fastFireOn = true;
	}
	
	public boolean isShieldOn() {return shieldOn;}
	public boolean isDoubleScoreOn() {return doubleScoreOn;}
	public void reset() {
		isDead = false;
		velocity = new Vector2();
		acceleration = new Vector2();
		
		fastFireOn = false;
		fastFireTime = 0;
		
		doubleScoreOn = false;
		gameScreen.getHud().setDoubleScoreText(false);
		doubleScoreTime = 0;
		
		doubleGunOn = false;
		doubleGunOnTime = 0;
		this.set(normalPlayer);
		
		shieldOn = false;
		shieldTime = 0;
		
		spawning = false;
		
		angle = 90f;
		this.setRotation(0);
	}
}
