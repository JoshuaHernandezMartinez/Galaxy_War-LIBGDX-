package com.yami.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yami.game.Main;
import com.yami.screens.GameScreen;

public class Ufo extends GameObject{
	
	private static final float NODE_RADIUS = 60f;
	private static final float MAX_VEL = 6f;
	private static final float ANGLE_DELTA = 5f;
	private static final float MASS = 80f;
	private static final float ANGLE_RANGE = 90f;
	private static final int UFO_SCORE = 40;
	
	private Array<Vector2> path;
	private int index;
	private Vector2 currentNode;
	private boolean following;
	private float time;
	
	private Sound shoot2, explosion;
	
	public Ufo(Vector2 position, Vector2 velocity, Sprite sprite, GameScreen gameScreen) {
		super(position, velocity, sprite, gameScreen);
		index = 0;
		following = true;
		this.velocity = new Vector2(MAX_VEL, MAX_VEL);
		time = 0;
		
		shoot2 =  gameScreen.getGame().getManager().get(
				Main.shoot2, Sound.class);
		
		explosion =  gameScreen.getGame().getManager().get(
				Main.explosion, Sound.class);
		
		definePath();
	}
	
	private void definePath() {
		
		path = new Array<Vector2>();
		
		Vector2 node = new Vector2(MathUtils.random(-Main.V_WIDTH/2, 0),
				MathUtils.random(0, Main.V_HEIGHT/2));
		path.add(node);
		node = new Vector2(MathUtils.random(0, Main.V_WIDTH/2),
				MathUtils.random(0, Main.V_HEIGHT/2));
		path.add(node);
		node = new Vector2(MathUtils.random(0, Main.V_WIDTH/2),
				MathUtils.random(0, -Main.V_HEIGHT/2));
		path.add(node);
		node = new Vector2(MathUtils.random(-Main.V_WIDTH/2, 0),
				MathUtils.random(-Main.V_HEIGHT/2, 0));

		path.add(node);
	}
	
	private Vector2 pathFollowing() {
		currentNode = path.get(index);
		Vector2 tmp = new Vector2(currentNode.x, currentNode.y);
		
		float distanceToNode = Vector2.dst(tmp.x, tmp.y, getCenter().x, getCenter().y);
		
		if(distanceToNode < NODE_RADIUS) {
			index++;
			if(index >= path.size)
				following = false;
		}

		return seekTarget(tmp);
	}
	
	private Vector2 seekTarget(Vector2 target) {
		Vector2 desiredVelocity =  target.sub(getCenter());
		desiredVelocity =  desiredVelocity.nor();
		desiredVelocity = desiredVelocity.scl(MAX_VEL);
		return desiredVelocity.sub(velocity);
	}
	
	@Override
	protected void update(float dt) {
		time += dt;
		
		if(time > 1f) {
			
			Vector2 playerCenter = gameScreen.getPlayer().getCenter();
			
			Vector2 toPlayer = playerCenter.sub(getCenter());
			toPlayer = toPlayer.nor();
			float currentAngle = toPlayer.angle();
			float randomAngle = MathUtils.random(ANGLE_RANGE) - ANGLE_RANGE / 2 + currentAngle;
			toPlayer = toPlayer.setAngle(randomAngle);
			
			Laser l = gameScreen.getLaserPool().obtain();
			l.init(new Vector2(position.x + getOriginX() + toPlayer.x * getOriginX(),
					position.y + getOriginY() + toPlayer.y * getOriginY()), toPlayer);
			
			gameScreen.getGameObjects().add(l);
			
			shoot2.play();
			time --;
		}
		
		
		Vector2 pathFollowingForce = new Vector2();
		if(following)
			pathFollowingForce = pathFollowing();
		
		pathFollowingForce = pathFollowingForce.scl(1f/MASS);
		velocity = velocity.add(pathFollowingForce);
		velocity = velocity.limit(MAX_VEL);
		position = position.add(velocity);
		
		if(!following && (position.x + this.getWidth() < -Main.V_WIDTH/2 ||
				position.x > Main.V_WIDTH/2 ||
				position.y + this.getHeight() < -Main.V_HEIGHT/2 ||
				position.y > Main.V_HEIGHT/2))
			destroy();
		
		this.rotate(ANGLE_DELTA);
		this.setPosition(position.x, position.y);
		
		collidesWith();
		
	}
	
	@Override
	public void destroy() {
		gameScreen.addExplosions(position, this.getOriginX(), this.getOriginY());
		int score = UFO_SCORE;
		Color color = Color.WHITE;
		if(gameScreen.getPlayer().isDoubleScoreOn()) {
			score = score * 2;
			color = Color.YELLOW;
		}
		gameScreen.getHud().addScore(score, position, color);
		explosion.play();
		super.destroy();
	}
	
	public Array<Vector2> getPath(){return path;}
	
}
