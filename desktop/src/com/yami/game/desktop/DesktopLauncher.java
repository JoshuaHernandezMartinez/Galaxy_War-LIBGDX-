package com.yami.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yami.game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Galaxy War";
		config.width = 960;
		config.height = 540;
		config.addIcon("icon1.png", Files.FileType.Internal);
		config.addIcon("icon2.png", Files.FileType.Internal);
		config.addIcon("icon3.png", Files.FileType.Internal);
		new LwjglApplication(new Main(), config);
	}
}
