package impossible_mission.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import impossible_mission.engine.Handler;
import impossible_mission.graphics.Animation;
import impossible_mission.graphics.SpriteSheet;

public class Hero extends GameObject {
	public int ammo = 100;
	public int hp = 100;
	private Handler handler;

	private BufferedImage[] heroImages = new BufferedImage[3];
	private Animation anim;

	public void setData(int i, int j, ID player, Handler handler2, SpriteSheet ss) {
		super.x = i;
		super.y = j;
		super.id = player;
		super.ss = ss;
		this.handler = handler2;

		heroImages[0] = ss.grabImage(1, 1, 32, 48);
		heroImages[1] = ss.grabImage(2, 1, 32, 48);
		heroImages[2] = ss.grabImage(3, 1, 32, 48);

		anim = new Animation(3, heroImages[0], heroImages[1], heroImages[2]);
	}

	public void tick() {
		x += velX;
		y += velY;

		collision();

		//movement
		if (handler.isUp())
			velY = -5;
		else if (!handler.isDown())
			velY = 0;

		if (handler.isDown())
			velY = 5;
		else if (!handler.isUp())
			velY = 0;

		if (handler.isRight())
			velX = 5;
		else if (!handler.isLeft())
			velX = 0;

		if (handler.isLeft())
			velX = -5;
		else if (!handler.isRight())
			velX = 0;

		anim.runAnimation();
	}

	private void collision() {
		for (int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);

			if (tempObject.getId() == ID.Block) {
				if (getBounds().intersects(tempObject.getBounds())) {
					x += velX * -1;
					y += velY * -1;
				}
			}

			if (tempObject.getId() == ID.Crate) {
				if (getBounds().intersects(tempObject.getBounds())) {
					ammo += 10;
					handler.removeObject(tempObject);
				}
			}

			if (tempObject.getId() == ID.Enemy) {
				if (getBounds().intersects(tempObject.getBounds())) {
					hp--;
				}
			}
		}
	}

	public void render(Graphics g) {
		if (velX == 0 && velY == 0)
			g.drawImage(heroImages[0], x, y, null);
		else
			anim.drawAnimation(g, x, y, 0);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 48);
	}
}
