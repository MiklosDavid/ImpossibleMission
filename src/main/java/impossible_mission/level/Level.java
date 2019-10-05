package impossible_mission.level;

import impossible_mission.engine.Handler;
import impossible_mission.graphics.BufferedImageLoader;
import impossible_mission.graphics.SpriteSheet;
import impossible_mission.objects.Block;
import impossible_mission.objects.Crate;
import impossible_mission.objects.Enemy;
import impossible_mission.objects.Hero;
import impossible_mission.objects.ID;

public class Level extends BufferedImageLoader {
	private Handler handler;
	private SpriteSheet ss;
	private Hero hero;

	public Level setHandler(Handler handler) {
		this.handler = handler;
		return this;
	}

	public Level setSs(SpriteSheet ss) {
		this.ss = ss;
		return this;
	}

	public Level setHero(Hero hero) {
		this.hero = hero;
		return this;
	}

	public void loadLevel() {
		int w = image.getWidth();
		int h = image.getHeight();

		for (int xx = 0; xx < w; xx++) {
			for (int yy = 0; yy < h; yy++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				System.out.println("");
				if (red == 255)
					handler.addObject(new Block(xx * 32, yy * 32, ID.Block, ss));
				if (blue == 255 && green == 0) {
					hero.setData(xx * 32, yy * 32, ID.Player, handler, ss);
					handler.addObject(hero);
				}
				if (green == 255 && blue == 0)
					handler.addObject(new Enemy(xx * 32, yy * 32, ID.Enemy, handler, ss));
				if (green == 255 && blue == 255)
					handler.addObject(new Crate(xx * 32, yy * 32, ID.Crate, ss));
			}
		}
	}
}
