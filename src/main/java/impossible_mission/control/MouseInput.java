package impossible_mission.control;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import impossible_mission.engine.Camera;
import impossible_mission.engine.Handler;
import impossible_mission.graphics.SpriteSheet;
import impossible_mission.objects.Bullet;
import impossible_mission.objects.Hero;
import impossible_mission.objects.ID;

public class MouseInput extends MouseAdapter {
	private Handler handler;
	private Camera camera;
	private SpriteSheet ss;
	private Hero hero;

	public MouseInput(Handler handler, Camera camera, Hero hero, SpriteSheet ss) {
		this.handler = handler;
		this.camera = camera;
		this.hero = hero;
		this.ss = ss;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = (int) (e.getX() + camera.getX());
		int my = (int) (e.getY() + camera.getY());

		if (hero.ammo >= 1) {
			handler.addObject(
					new Bullet(hero.getX() + 16, hero.getY() + 24, ID.Bullet, handler, mx, my, ss));
			hero.ammo--;
		}
	}
}
