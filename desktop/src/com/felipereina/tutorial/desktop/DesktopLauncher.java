package com.felipereina.tutorial.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.felipereina.tutorial.MarioBros;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "MarioBros";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new MarioBros(), config);
	}
}
