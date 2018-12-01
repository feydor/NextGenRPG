package org.rpg.system;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class KeyBinding extends JPanel {
	
   public static final int TIMER_DELAY = 10;
   public static final int DELTA_X = 2;
   public static final int DELTA_Y = DELTA_X;
   public static final int SPRITE_WIDTH = 32;    // 32 x 32 px
   public static final int SPRITE_HEIGHT = SPRITE_WIDTH;
   
   private static final String PRESSED = "pressed";
   private static final String RELEASED = "released";
   //private static final int PREF_W = 800;
   //private static final int PREF_H = 650;
   private Map<Dir, Boolean> dirMap = new EnumMap<>(Dir.class);
   private  int spriteX = 0;
   private int spriteY = 0;
   private BufferedImage sprite;
   private String spriteLoc = "/home/codreanu/Documents/School/Fall2018/ECE373/RPG_proj/sprites/sonic.png";
   private Timer animationTimer = new Timer(TIMER_DELAY, new AnimationListener());

   public KeyBinding() {
      for (Dir dir : Dir.values()) {
         dirMap.put(dir, Boolean.FALSE);
      }
      sprite = createSprite();
      setKeyBindings();
      animationTimer.start();
   }

   public BufferedImage createSprite() {
	  try {
		  BufferedImage sprt = ImageIO.read(new File(spriteLoc));
		  return sprt;
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
    
	return sprite;
   }

 /*  @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (sprite != null) {
         g.drawImage(sprite, spriteX, spriteY, this);
      }
   }*/

   public void setKeyBindings() {
      int condition = WHEN_IN_FOCUSED_WINDOW;
      InputMap inputMap = getInputMap(condition);
      ActionMap actionMap = getActionMap();

      for (Dir dir : Dir.values()) {
         KeyStroke keyPressed = KeyStroke.getKeyStroke(dir.getKeyCode(), 0, false);
         KeyStroke keyReleased = KeyStroke.getKeyStroke(dir.getKeyCode(), 0, true);

         inputMap.put(keyPressed, dir.toString() + PRESSED);
         inputMap.put(keyReleased, dir.toString() + RELEASED);

         actionMap.put(dir.toString() + PRESSED, new DirAction(dir, PRESSED));
         actionMap.put(dir.toString() + RELEASED, new DirAction(dir, RELEASED));
      }

   }

   private class AnimationListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         int newX = spriteX;
         int newY = spriteY;
         for (Dir dir : Dir.values()) {
            if (dirMap.get(dir)) {
               newX += dir.getDeltaX() * DELTA_X;
               newY += dir.getDeltaY() * DELTA_Y;
            }
         }
         if (newX < 0 || newY < 0) {
            return;
         }
         if (newX + SPRITE_WIDTH > getWidth() || newY + SPRITE_HEIGHT > getHeight()) {
            return;
         }
         spriteX = newX;
         spriteY = newY;
         repaint();         
      }
   }

   private class DirAction extends AbstractAction {

      private String pressedOrReleased;
      private Dir dir;

      public DirAction(Dir dir, String pressedOrReleased) {
         this.dir = dir;
         this.pressedOrReleased = pressedOrReleased;
      }

      @Override
      public void actionPerformed(ActionEvent evt) {
         if (pressedOrReleased.equals(PRESSED)) {
            dirMap.put(dir, Boolean.TRUE);
         } else if (pressedOrReleased.equals(RELEASED)) {
            dirMap.put(dir, Boolean.FALSE);
         }
      }

   }

   private static void createAndShowGui() {
      KeyBinding mainPanel = new KeyBinding();

      JFrame frame = new JFrame("KeyBindingEg");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }
   
   public Map<Dir, Boolean> getDirMap() { return dirMap; }
   public Timer getAnimationTimer() { return animationTimer; }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
 



}

