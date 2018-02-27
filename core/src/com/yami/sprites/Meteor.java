package com.yami.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.yami.game.Main;
import com.yami.screens.GameScreen;

public class Meteor extends GameObject{
	private static final float INIT_VEL = 4.0f;
	private static final float MAX_VEL = 7.0f;
	private static final float DELTA_ANGLE = 5f;
	private static final float SHIELD_DISTANCE = 300f;
	private static final int METEOR_SCORE = 20;
	
	private float initialVel;
	private MeteorType type;
	
	private Sound explosion;
	
	public Meteor(Vector2 position,
			MeteorType type, Sprite sprite, GameScreen gameScreen) {
		super(position, new Vector2(1,0), sprite, gameScreen);
		
		init(position, type, sprite);
		
		if(gameScreen != null)
			explosion =  gameScreen.getGame().getManager().get(
				Main.explosion, Sound.class);
	}
	
	@Override
	protected void update(float dt) {
		
		if(gameScreen != null && gameScreen.getPlayer().isShieldOn()){
			float distanceToPlayer = Vector2.dst(gameScreen.getPlayer().getCenter().x,
					gameScreen.getPlayer().getCenter().y, getCenter().x, getCenter().y);
			
			if(distanceToPlayer < SHIELD_DISTANCE) {
				Vector2 fleeForce = fleeForce();
				velocity = velocity.add(fleeForce);
			}

		}else if(velocity.len() >= initialVel) {
			Vector2 reversedVelocity = new Vector2(-velocity.x, -velocity.y);
			velocity = velocity.add(reversedVelocity.nor().scl(0.05f));
		}
		
		velocity = velocity.limit(MAX_VEL);
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
		this.rotate(DELTA_ANGLE);
		if(gameScreen != null)
			collidesWith();
	}
	
	private Vector2 fleeForce() {
		Vector2 desiredVelocity = gameScreen.getPlayer().getCenter().sub(getCenter());
		desiredVelocity = (desiredVelocity.nor()).scl(MAX_VEL);
		Vector2 v = new Vector2(velocity.x, velocity.y);
		return v.sub(desiredVelocity);
	}
	
	public MeteorType getType() {return type;}
	
	public void destroy() {
		gameScreen.divideMeteor(this);
		gameScreen.addExplosions(position, this.getOriginX(), this.getOriginY());
		int score = METEOR_SCORE;
		Color color = Color.WHITE;
		if(gameScreen.getPlayer().isDoubleScoreOn()) {
			score = score * 2;
			color = Color.YELLOW;
		}
		gameScreen.getHud().addScore(score, position, color);
		explosion.play();
		super.destroy();
	}
	
	public void init(Vector2 position, MeteorType m, Sprite t) {
		this.position = position;		
		this.type = m;
		isDead = false;
		this.set(t);
		
		this.initialVel = MathUtils.random(INIT_VEL) + 1f;
		this.velocity = velocity.scl(initialVel);
		this.velocity = velocity.setAngle(MathUtils.random(360f));
	}
	
	public float getInitialVel() {
		return initialVel;
	}
	
}
