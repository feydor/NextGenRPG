package org.rpg.map;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class World {

	public World() {
		JFrame f = new JFrame();
        f.setSize(1300, 1000);
        f.setTitle("The World");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new Tile());
        f.setVisible(true);
	}
}
