package org.rpg.menu;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class Title {
	
	private String name = "Next Gen RPG";
	private Container c;
	private JFrame menuScreen;
	private JPanel titlePanel;
	private ButtonPane buttonPane;
	private Color bgColor;
	private Color windowColor;
	private BufferedImage logo; // title image
	
	private boolean start = false; // StateMachine will listen for start and load, to draw the next state
	private boolean load = false;
	private boolean debug = false;
	
	private static final int SCREEN_HEIGHT = 800;
	private static final int SCREEN_WIDTH = 1000;
	
	public Title() {
		windowColor = new Color(18, 1, 113); // Navy Blue
		bgColor = new Color(0, 0, 0);
		// instantiate JFrame
		menuScreen = new JFrame("RPG Menu Screen");
		menuScreen.setBackground(bgColor); 
		menuScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		buildTitle();
	}
	
	private void buildTitle() {
		// default look and feel
//	 try {
//		    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
//		 } catch (Exception e) {
//		            e.printStackTrace();
//		 }
	 
	 	// load logo
		 try {                
			 URL url = new URL("https://i.ibb.co/rGSK6TB/title-logo.png");

			 logo = ImageIO.read(url);

		 } catch (IOException ex) {
	           ex.printStackTrace();
	     }
		 
		// first make buttons
		buttonPane = new ButtonPane("title", windowColor);
		for(JButton button : buttonPane.getTitleButtons()) {
			button.addActionListener(new MenuListener()); // private inner class below
		}

		titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setBackground(bgColor);
		titlePanel.setPreferredSize(new Dimension(SCREEN_HEIGHT, SCREEN_WIDTH)); // setting size of JPanel
		JLabel logoLabel = new JLabel(new ImageIcon(logo));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
 		c.gridy = 0;
 		c.fill = GridBagConstraints.HORIZONTAL;
 		c.ipady = 20;
 		c.ipadx = 10;
 		c.weightx = 0.0;
		titlePanel.add(logoLabel, c);
		GridBagConstraints buttonCons = new GridBagConstraints();
		buttonCons.gridx = 0;
		buttonCons.gridy = 1;
		buttonCons.anchor = GridBagConstraints.CENTER;
		titlePanel.add(buttonPane, buttonCons); // add button to JPanel

		menuScreen.add(titlePanel); 
		menuScreen.pack();
		menuScreen.setVisible(true);
	}
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton)(e.getSource());
			
			if(source.equals(buttonPane.getTitleButtons()[0])) { // start
				start = true;
				menuScreen.dispose();
			} else if(source.equals(buttonPane.getTitleButtons()[1])) { // continue
				load = true;
				menuScreen.dispose();
			} else if(source.equals(buttonPane.getTitleButtons()[2])) { // debug
				debug = true;
				menuScreen.dispose();
			}
		}
		
	}
	
	public boolean getStartBool() {
		return start;
	}
	public boolean getLoadBool() {
		return load;
	}
	public boolean getDebugBool() {
		return debug;
	}
	
	// test main
	public static void main(String[] args) {
		Title title = new Title();

	}



	
}
