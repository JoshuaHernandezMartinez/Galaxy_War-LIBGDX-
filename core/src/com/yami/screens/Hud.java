package com.yami.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yami.game.Main;

public class Hud implements Disposable{
	
	private Viewport viewport;
	public Stage stage;
	
	private Image lifeImage;
	private Label lifeLabel;
	private Label scoreLabel;
	private Label timeLabel;
	
	private Integer time;
	private Integer life;
	private Integer score;
	
	private GameScreen gameScreen;
	
	private float acumulatedTime;
	
	private boolean doubleScore;
	
	private BitmapFont font;
	
	// input
	
	private Touchpad touchpad;
	
	private TextureRegion touchpadBackground, knob, lifeTexture;
	
	private float knobPercentX, knobPercentY;
	
	public Hud(SpriteBatch batch, GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		
		time  = 0;
		life  = 5;
		score = 0;
		acumulatedTime = 0;
		knobPercentX = 0;
		knobPercentY = 0;
		
		touchpadBackground = gameScreen.getGame().touchpad_backgroundText;
		knob = gameScreen.getGame().touchpad_knobText;
		
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
		
		stage = new Stage(viewport, batch);
		
		lifeTexture = gameScreen.getGame().playerLife;
		font = gameScreen.getGame().getManager().get(Main.futureFont, BitmapFont.class);
		
		lifeImage = new Image(lifeTexture);
		lifeLabel = new Label(life+"", new Label.LabelStyle(font, Color.WHITE));
		scoreLabel = new Label("SCORE  " + score, new Label.LabelStyle(font, Color.WHITE));
		timeLabel = new Label("TIME  " + time, new Label.LabelStyle(font, Color.WHITE));
		
		lifeImage.scaleBy(0.7f);
		
		Touchpad.TouchpadStyle ts = new Touchpad.TouchpadStyle();
		ts.background = new TextureRegionDrawable(new TextureRegion(touchpadBackground));
		ts.knob = new TextureRegionDrawable(new TextureRegion(knob));
		
		touchpad = new Touchpad(10f, ts);
		touchpad.setBounds(100, 100, 250, 250);
		touchpad.getColor().a = 0.5f;
		
		touchpad.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	knobPercentX = touchpad.getKnobPercentX();
		    	knobPercentY = touchpad.getKnobPercentY();
		    }
		});
		
		Table table = new Table();
		
		table.top();
		table.setFillParent(true);
		table.add(lifeImage).expandX().padTop(70f);
		table.add(lifeLabel).expandX().padTop(50f).align(Align.left);
		table.add(scoreLabel).expandX().padTop(50f);
		table.add(timeLabel).expandX().padTop(50f);
		
		stage.addActor(table);
		stage.addActor(touchpad);
	}
	
	public void update(float dt) {
		
		acumulatedTime += dt;
		if(acumulatedTime >= 1.0f) {
			time ++;
			acumulatedTime --;
			timeLabel.setText("TIME  " + time);
		}
	}
	
	public void addLife(int n, Vector2 position) {
		life += n;
		lifeLabel.setText(life+"");
		Color color = n > 0 ? Color.GREEN : Color.RED;
		String sign = "";
		if(n > 0)
			sign = "+";
		gameScreen.addMessage(sign+n+" LIFE", position, color, 1f, false);
	}
	public void addScore(int n, Vector2 position, Color color) {	
		score += n;
		gameScreen.addMessage("+"+n+" SCORE", position, color, 1f, false);
		if(doubleScore) {
			scoreLabel.setText("SCORE ( X2 ) : " + score);
			scoreLabel.setColor(Color.YELLOW);
		}else {
			scoreLabel.setText("SCORE " + score);
			scoreLabel.setColor(Color.WHITE);
		}
	}
	
	public void setDoubleScoreText(boolean doubleScoreOn) {
		doubleScore = doubleScoreOn;
		if(doubleScore) {
			scoreLabel.setText("SCORE ( X2 ) : " + score);
			scoreLabel.setColor(Color.YELLOW);
		}else {
			scoreLabel.setText("SCORE " + score);
			scoreLabel.setColor(Color.WHITE);
		}
	}
	
	public int getLives() {
		return life;
	}
	
	public int getScore() {
		return score;
	}
	public int getTime() {
		return time;
	}
	
	public void reset() {
		time = 0;
		score = 0;
		life = 5;
		scoreLabel.setText("SCORE " + score);
		timeLabel.setText("TIME  " + time);
		lifeLabel.setText(life+"");
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	public Vector2 getKnobPercent() {return new Vector2(knobPercentX, knobPercentY);}
}
