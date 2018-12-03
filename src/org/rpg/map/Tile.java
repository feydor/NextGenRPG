package org.rpg.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.rpg.character.Party;
import org.rpg.character.Player;
import org.rpg.combat.Combat;
import org.rpg.system.Dir;
import org.rpg.system.KeyBinding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public class Tile extends JPanel implements TileIndex{
	
		JFrame frame;
	    // FIXME: need to find a way of changing this url
		private String sheetLocation = "/home/codreanu/Documents/School/Fall2018/ECE373/RPG_proj/tiles/dark.png";
		private BufferedImage tileSheet; // tileSheet for splitting, laoded from file at sheetLocation
		private BufferedImage[] tiles; // contains individual tiles after splitting the tileSheet
    	private Space[][] world; // contains the overworld map
    	private Party partyOverWorld;
    	public static Boolean showMenu; // listening to this boolean (StateMachine) will determine if menu pops up
    	
    	private BufferedImage sprite;
    	private int spriteX = 64; // need to be changed to (or made to work with) party Xpos and Ypos
    	private int spriteY = 64;
    	private String spriteLoc = "https://i.ibb.co/f1NkqtH/hero.png";
    	private Map<Dir, Boolean> dirMap = new EnumMap<>(Dir.class); // directions map
    	private Timer animationTimer = new Timer(TIMER_DELAY, new AnimationListener()); 
        String bgMusic = "/home/codreanu/Documents/School/Fall2018/ECE373/RPG_proj/music/Xak.wav";
    	 
		private final int TILE_WIDTH = 32; // e.g. 64x64, 32x32, 16x16, etc
    	private final int TILE_HEIGHT = 32;
    	private final int WINDOW_HEIGHT = 1000;
    	private final int WINDOW_WIDTH = 1300;
    	
		// e.g. for a tilesheet of 1000x1300 (HxW), 
		// rows = 1000 / TILE_HEIGHT(32) = 31.25 => 31
		// cols = 1300 / TILE_WIDTH(32) = 40.625 => 40
		// NOTE: tilesheet resolution is only for example
    	final int rows = 20;
    	final int cols = 20;
    	
        protected final int ROWS = 40; // number of tiles per row
    	protected final int COLS = 40; // number of tiles per columns
    	
    	public static final int TIMER_DELAY = 10;
	    public static final int DELTA_X = 2; // speed = 3
	    public static final int DELTA_Y = DELTA_X;
	    public static final int SPRITE_WIDTH = 32; // Same as TILE_WIDTH/HEIGHT
	    public static final int SPRITE_HEIGHT = SPRITE_WIDTH;
	    private static final String PRESSED = "pressed";
	    private static final String RELEASED = "released";
	    
	    private static final int SCREEN_WIDTH = 1280; // px
	    private static final int SCREEN_HEIGHT = 1024;
    	
    public Tile(Party party) {
    	frame = new JFrame();
    	partyOverWorld = party;
    	world = new Space[ROWS][COLS];
    	// load with integers indexes and corresponding bg image 
		instantiateWorldArray();
		
    	// done to ensure the tileSheet is read only once
    	if(tileSheet == null) {
    		try {
				tileSheet = ImageIO.read(new File(sheetLocation)); // load tile sheet from file
				tiles = new BufferedImage[rows * cols];
				getTilesFromSheet(); //split tileSheet into tiles[]
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	//set up keybinding
		for (Dir dir : Dir.values()) {
			dirMap.put(dir, Boolean.FALSE);
		}
		sprite = createSprite();
		setKeyBindings();
		animationTimer.start();

		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		showMenu = false;
		this.setFocusable(true);
		this.addKeyListener(menuEnter);
    }
    
    public Tile() {} // needed for Combat extension of Tile
    
    // NOTE: Not used right now. Instead each image is manually loaded from file in Space class.
    public void getTilesFromSheet() {
    	// FIXME: Fix proper spritesheet splitting, currently loading images manually
    	// goes through the sheet and adds ROWSxCOLS blocks to sprites 2d array.
    	for (int i = 0; i < rows; i++) {
    	    for (int j = 0; j < cols; j++) {
    	    	tiles[(i * cols) + j] = tileSheet.getSubimage(
    	            j * TILE_WIDTH,
    	            i * TILE_HEIGHT,
    	            TILE_WIDTH,
    	            TILE_HEIGHT
    	        );
    	    }
    	}
    }
    
    // overworld map is made here
	// FIXME: FInd a way to load a map from file (csv or xcf?)
    public void instantiateWorldArray() {
    	for (int i = 0; i < ROWS; i++)
    	{
    	    for (int j = 0; j < COLS; j++)
    	    {
    	    	// grass field
    	    	world[i][j] = new Space(GRASS_INDEX);
    	    	
    	    	if((i > 11) & (i < 18) & (j > 10) & (j < 15)) {
    	    		world[i][j] = new Space(DIRT_INDEX);
    	    	}
    	    	
    	    	// rock border
    	    	if(i == 0 || i == ROWS || j == 0 || j == COLS || 
    	    			i == 1 || i == ROWS-2 || j == 1 || j == COLS-2) {
    	    		world[i][j] = new Space(ROCK_INDEX);
    	    	}
				
				// a pond of water
//				if( ((i > 5) & (i < 10)) && ((j > 8) & (j < 16)) ) {
//    	    		world[i][j] = new Space(WATER_INDEX);
//				}
				
				// draw a path
				/*if( (i > 3) & (i < 24) & (j == 7) ) {
					world[i][j] = new Space(PATH_INDEX);
				}*/
    	    	 
    	    }
    	}
    }
     
    // draws the map here, the bg image of each Space depends on the image attribute of each Space
	// Drawing will be done in layers,
	// layer 1 - background
	// layer 2 - objects, foilage, terrain
	// layer 3 - sprites/characters
	// ...
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	// layer 1 - background
    	for (int i = 0; i < ROWS; i++) {
    	    for (int j = 0; j < COLS; j++) {

    	    	g.drawImage(world[i][j].getImage(), i*TILE_WIDTH, j*TILE_HEIGHT, null);
    	    	
                //NOTE: draw optional borders here
                //g.setColor(Color.DARK_GRAY);
                //g.drawRect(i * TILE_WIDTH, j * TILE_HEIGHT, TILE_WIDTH ,TILE_HEIGHT);
    	    } 
    	}	
    	
    	// layer 2 - objects, foilage, terrain
        // ...

    	//layer 3 - sprites
    	  if (sprite != null) {
    	         g.drawImage(sprite, spriteX, spriteY, this);
    	      }
    }
    
    public BufferedImage createSprite() {
  	  try {
  		  BufferedImage sprt = ImageIO.read(new URL(spriteLoc));
  		  return sprt;
  	  } catch (IOException e) {
  		  e.printStackTrace();
  	  }
      
  	  return sprite; // NOTE: Eclipse gives an error if this return statement is not present
	  				// If loading of sprite goes well, it should NEVER run
     }
    
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
		   // movement is linear, x = dx * x0 & y = dy * y0
		   // change dx and dy in attributes above (DELTA_X, DELTA_Y)
		   for (Dir dir : Dir.values()) {
              if (dirMap.get(dir)) {
                 newX += dir.getDeltaX() * DELTA_X;
                 newY += dir.getDeltaY() * DELTA_Y;
              }
           }
		   /* boundary checking */
           if (newX < 0 || newY < 0) {
              return;
           }
           if (newX + SPRITE_WIDTH > getWidth() || newY + SPRITE_HEIGHT > getHeight()) {
              return;
           }
           if(!isValidMove(newX, newY)) {
        	   return;
           }
           if(spriteX != newX || spriteY != newY) {
        	   // Random Encounters here
        	   if (Math.random() < 0.01) {
           	    System.out.println("Prepare to fight!");
   				//Combat combat = new Combat();
   				partyOverWorld.setInCombat(true);			
   				//combat.enterCombat(partyOverWorld, partyOverWorld.getPartyXpos(), partyOverWorld.getPartyYpos(), world);
   				//partyOverWorld.clearEnemies();
   				partyOverWorld.setInCombat(false);
   			}
           }
           spriteX = newX;
           spriteY = newY;
           partyOverWorld.setPartyXpos(spriteX);
           partyOverWorld.setPartyYpos(spriteY);
           //System.out.println("(" + partyOverWorld.getPartyXpos() + ", " + partyOverWorld.getPartyYpos() + ")");
           repaint();         
        }
        
        private boolean isValidMove(int xposPx, int yposPx) {
    		boolean isValid = true;
    		
    		// divide pixels into ROWS and COLS, NOTE: Any decimal is cut off so 3.84 -> 3
    		// forces player to be in neat 32x32 boxes for boundary checking purposes
    		int xpos = xposPx / (SCREEN_WIDTH / COLS); // xposPX / 32
    		int ypos = yposPx / (SCREEN_HEIGHT / ROWS); 
    		
    		if(world[ypos][xpos].hasWall()) { isValid = false; }
    		
    		if(world[ypos][xpos].hasTreasure()) { 
    			/* get Treasure */ 
    			System.out.println("Get Treasure.");
    		}
    		
    		if(world[ypos][xpos].hasNPC()) { 
    			/* talk to NPC */ 
    			isValid = false;
    			System.out.println("Talk to NPC."); 
    		}
    		
    		if(world[ypos][xpos].hasTerrain()) { isValid = false; }
    		
    		if(world[ypos][xpos].hasTrap()) { /* handle the trap */ }
    		
    		return isValid;
    	}
     }

    // private inner class neccesary to extends AbstractAction 
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
    
    public void updateFrame() {
    	//frame = new JFrame();
    	KeyBinding mainPanel = new KeyBinding(); 
    	frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    	frame.setTitle("The World");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setContentPane(new Tile(partyOverWorld));
    	frame.setVisible(true);	
        
        // open the sound file as a Java input stream
        BufferedInputStream audioStream;
        InputStream in;
        AudioInputStream audioIn;
        Clip clip;
		try {
			in = new FileInputStream(bgMusic);
			audioStream = new BufferedInputStream(in);
			audioIn = AudioSystem.getAudioInputStream(audioStream);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
    }
    
    private KeyListener menuEnter = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
           if (e.getKeyChar() == KeyEvent.VK_ENTER) {
        	   // Enter the Party Menu
        	   //frame.dispose();
        	   showMenu = true;
           }
        }
     };
     
     public boolean isShowingMenu() {
    	 return showMenu;
     }
     public void setParty(Party party) {
    	 partyOverWorld = party;
 	}
     public Party getParty() {
    	 return partyOverWorld;
     }
    
    public static void main(String avg[]) throws IOException {
    	Player p1 = new Player("Warrior");
    	Party p = new Party();
    	p.addPartyMember(1, p1);
    	Tile worldFrame = new Tile(p);
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	worldFrame.updateFrame();
            }
         });
	}

	
}
