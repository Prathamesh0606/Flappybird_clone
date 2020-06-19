package com.phoenix.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

import sun.rmi.runtime.Log;

public class flappyBirdClone extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	Texture birdrotated;

	Texture pausebutton;
	ImageButton pause;




	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 2f;
	Circle birdCircle ;

	Texture topTube;
	Texture bottomTube;
	Texture gameover;
	float gap = 450;
	float maxTubeOffset;
	Random random;
	float tubeVelocity = 4;
	int noofTubes = 4;
	float[] tubeOffset = new float[noofTubes];
	float[] tubX = new float[noofTubes];
	float distanceBetweentubes;
	Rectangle[] toptubeRectangles;
	Rectangle[] bottomtubeRectangles;
	int score = 0;
	int scoringTube = 0;
	int finalscore = 0;

	private Stage stage;







	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameoverremovebgpreview.png");
		birds = new Texture[2];
		birds[0] = new Texture(("bird.png"));
		birds[1] = new Texture(("bird2.png"));
		birdrotated = new Texture("birdroteted.png");
		pausebutton = new Texture("pause.png");




		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		random = new Random();
		distanceBetweentubes = Gdx.graphics.getWidth()*0.7f;
		birdCircle = new Circle();
		shapeRenderer = new ShapeRenderer();
		toptubeRectangles = new Rectangle[noofTubes];
		bottomtubeRectangles = new Rectangle[noofTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		startgame();


		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);







	}

	public  void startgame() {
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for(int i=0;i < noofTubes;i++) {

			tubX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweentubes;
			toptubeRectangles[i] = new Rectangle();
			bottomtubeRectangles[i] = new Rectangle();


		}
	}

	@Override
	public void render() {

	    batch.begin();
		stage.act();
		stage.draw();

		//stage.addActor(pause);


		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//batch.draw(pausebutton,100,Gdx.graphics.getHeight()-100,100,100);


		if (gameState == 1) {
			if(tubX[scoringTube] < Gdx.graphics.getWidth()/2-topTube.getWidth()) {
				score++;
				if(scoringTube<noofTubes-1) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}

			}
			if (Gdx.input.justTouched()) {
				velocity = -25;


			}
			for(int i=0;i < noofTubes;i++) {


				if (tubX[i] < -topTube.getWidth()) {
					tubX[i] += noofTubes * distanceBetweentubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() -gap-200);

				}
				else {
					tubX[i] -= tubeVelocity;


				}

					batch.draw(topTube, tubX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
					batch.draw(bottomTube, tubX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
					toptubeRectangles[i] = new Rectangle(tubX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
					bottomtubeRectangles[i] = new Rectangle(tubX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			}
			if (birdY > 0 ) {
				velocity = velocity + gravity;
				birdY -= velocity;
			} else {
				gameState = 2;
			}
		} else if(gameState == 0){

			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}


		else if(gameState == 2) {
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
			if(birdY > 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}
			batch.draw(birdrotated, Gdx.graphics.getWidth() / 2 - birdrotated.getWidth() / 2, birdY+50);
			font.setColor(Color.RED);
			if(finalscore <= 5) {
				finalscore++;
				font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight());
			}
			else {
				finalscore = 0;
			}
			if (Gdx.input.justTouched()) {

				gameState = 1;
				startgame();

				score = 0;
				scoringTube = 0;
				velocity = 0;

			}
		}


			if (flapState == 0) {
				flapState = 1;

			} else {
				flapState = 0;
			}

			if(gameState !=2) {
				batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
				font.draw(batch,String.valueOf(score),100,200);
			}


			birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.RED);
			//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for(int i=0;i < noofTubes;i++) {
			//shapeRenderer.rect(tubX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle,toptubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomtubeRectangles[i])) {
				gameState = 2;
			}
		}


			shapeRenderer.end();
		batch.end();
		}
	}



