package impossible_mission;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import impossible_mission.control.KeyInput;
import impossible_mission.control.MouseInput;
import impossible_mission.engine.Camera;
import impossible_mission.engine.Handler;
import impossible_mission.engine.Window;
import impossible_mission.graphics.BufferedImageLoader;
import impossible_mission.graphics.SpriteSheet;
import impossible_mission.level.Level;
import impossible_mission.objects.Hero;
import impossible_mission.objects.ID;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private boolean isRunning = false;
	private Thread thread;
	private Handler handler;
	private Camera camera;
	private SpriteSheet ss;

	private BufferedImage level = null;
	private BufferedImage spriteSheet = null;
	private BufferedImage floor = null;

	public Game() {
		Hero hero = new Hero();
		new Window(1000, 563, "Impossible Mission", this);
		start();

		handler = new Handler();
		camera = new Camera(0, 0);
		this.addKeyListener(new KeyInput(handler));

		BufferedImageLoader loader = new BufferedImageLoader();

		spriteSheet = loader.loadImage("/sprite_sheet.png");
		ss = new SpriteSheet(spriteSheet);
		floor = ss.grabImage(4, 2, 32, 32);

		Level lev = new Level()
				.setHandler(handler)
				.setHero(hero)
				.setSs(ss);
		lev.loadImage("/game_level.png");
		this.addMouseListener(new MouseInput(handler, camera, hero, ss));
		lev.loadLevel();
	}

	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		//int frames = 0;
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				//updates++; 
				delta--;
			}
			render();
			//frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//frames = 0;
				//updates = 0;
			}
		}
		stop();
	}

	public void tick() {
		for (int i = 0; i < handler.object.size(); i++) {
			if (handler.object.get(i).getId() == ID.Player) {
				camera.tick(handler.object.get(i));
			}
		}
		handler.tick();
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		////////////////////////////////

		g2d.translate(-camera.getX(), -camera.getY());

		for (int xx = 0; xx < 30 * 72; xx += 32) {
			for (int yy = 0; yy < 30 * 72; yy += 32) {
				g.drawImage(floor, xx, yy, null);
			}
		}

		//////////////

		handler.render(g);

		g2d.translate(camera.getX(), camera.getY());

		// TODO
		//		g.setColor(Color.gray);
		//		g.fillRect(5, 5, 200, 32);
		//		g.setColor(Color.green);
		//		g.fillRect(5, 5, hp * 2, 32);
		//		g.setColor(Color.black);
		//		g.drawRect(5, 5, 200, 32);
		//
		//		g.setColor(Color.white);
		//		g.drawString("Ammo = " + ammo, 5, 50);

		/////////////
		g.dispose();
		bs.show();
	}

	public static void main(String args[]) {
		new Game();
	}
}
