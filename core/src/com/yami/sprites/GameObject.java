package com.yami.sprites;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yami.screens.GameScreen;

public abstract class GameObject extends Sprite implements Disposable{
	
	protected Vector2 position;
	protected Vector2 velocity;
	protected boolean isDead;
	protected GameScreen gameScreen;
	
	public GameObject(Vector2 position, Vector2 velocity,
			Sprite sprite, GameScreen gameScreen) {
		this.position = position;
		this.velocity = velocity;
		this.gameScreen = gameScreen;
		this.set(sprite);
		this.setPosition(position.x, position.y);
		isDead = false;
	}
	
	protected abstract void update(float dt);
	
	protected void collidesWith() {
		
		Array<GameObject> obs = gameScreen.getGameObjects();
		
		for(int i = 0; i < obs.size; i++) {
			GameObject ob = obs.get(i);
			
			if(ob.equals(this)) {
				continue;
			}
			
			double distance = (getCenter().sub(ob.getCenter())).len();
			
			if(distance < getRadius() + ob.getRadius()
					&& obs.contains(this, false)) {
				gameObjectCollision(this, ob);
				return;
			}
			
		}
		
	}
	
	private void gameObjectCollision(GameObject a, GameObject b) {
		
		if(a.isDead || b.isDead)
			return;
		
		Player p = null;
		
		if(a instanceof Player)
			p = (Player)a;
		else if(b instanceof Player)
			p = (Player)b;
		
		if(p != null && p.isSpawning())
			return;
		
		if(a instanceof Meteor && b instanceof Meteor) {
			return;
		}
		
		if(!(a instanceof PowerUp || b instanceof PowerUp)){
			a.destroy();
			b.destroy();
			return;
		}
		
		
		
		if(p != null){
			if(a instanceof Player){
				((PowerUp)b).executeAction();
				b.destroy();
			}else if(b instanceof Player){
				((PowerUp)a).executeAction();
				a.destroy();
			}
		}
		
	}
	
	protected void destroy() {
		isDead = true;
	}
	
	public float getRadius() {
		return getOriginX() * getScaleX();
	}
	
	public Vector2 getCenter() {return new Vector2(position.x + getOriginX(),
			position.y + getOriginY());}
	
	@Override
	public void draw(Batch batch, float dt) {
		update(dt);
		super.draw(batch);
	}
	
	public Vector2 getPosition() {return position;}
	public Vector2 getVelocity() {return velocity;}
	public boolean isDead() 	 {return isDead  ;}
	
	@Override
	public void dispose() {
		this.getTexture().dispose();
	}
	
}
