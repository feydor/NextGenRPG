package org.rpg.combat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.rpg.character.*;
import org.rpg.map.*;
import org.rpg.menu.ButtonPane;
import org.rpg.system.AudioPlayer;

@SuppressWarnings("serial")
public class Combat extends JPanel{
	private JFrame combatScreen;
    private JPanel combatPanel; 
    private JPanel mapPanel; // will be handled by private inner class MapScreen
    private JLabel healthLabel;
    private JLabel enemyNameLabel;
	private String bgMusic = "firstCampaign.wav";
	//private String bgMusic = "https://s3-us-west-1.amazonaws.com/nextgenrpgassets/firstCampaign.wav";
    private Color windowColor;
	private Color bgColor;
	private ButtonPane combatPane;
	private AudioPlayer fightMusic;
	private Graphics mapGraphics;
	
	private Party party;
	private Space[][] world;
	private int currentMoves; // Determines how many moves a player has made so far, for their turn
	private int enemyTurn; // on currentPlayerTurn, move ai, set to be party.numPlayers + 1
	
	protected static final int SCREEN_WIDTH = 1024; // px
    protected static final int SCREEN_HEIGHT = 1024;
    protected final int ROWS = 32; // number of tiles per row
	protected final int COLS = 32; // number of tiles per columns
	public static final int SPRITE_WIDTH = 32; // Same as TILE_WIDTH/HEIGHT
    public static final int SPRITE_HEIGHT = SPRITE_WIDTH;
	public final int TILE_WIDTH = 32; // e.g. 64x64, 32x32, 16x16, etc
	public final int TILE_HEIGHT = 32;
	
	private int newxpos = 0;
	private int newypos = 0;
	
	private int distPlayers = 4; //closeness of party to their center [friendly and enemy]
	private int distEnemies = 8; //how far apart friendly and enemies centers are
	public static boolean endCombat;
	
	////////////////////////////////////////////////////////////////////////////////////////
	// Combat.java - logical flow
	// 1. Instantiate a new Combat object with Combat(), sets up final variables
	// 2. Call Combat.update(Party party, Space[][] world).
	// 		2.1. Sets party and world private variables; sets p1 location, enemyTurn variable, etc.
	//		2.2. Draws combatScreen frame and starts up music.
	//  	2.3. Button actionListener is set up here (private inner class CombatListener)
	// 3. User clicks on options and begins movement, attacks, skipping turns, or fleeing.
	//		3.1. Each party member can both move and fight in each of their turns. 
	//		3.2. Pressing "END TURN", ends the current player's turn and begins the next.
	//		3.3. When all party member's have taken their turn (currentMoves == party.getNumPLayers()),
	//			 The enemy takes their turn. See doEnemyTurn() and doEnenmyAttack().
	//      3.4. doEnemyTurn() makes each enemy increase their xBlock and yBlock (y position has priority),
	//           to that of the nearest player that turn.
	//	    3.5. If enemy is in range of the closest player, that is, the enemy's xBlock and yBlock are equal to the 
	//			 closest player AND there is no valid move within movement range (the speed stat), then attack the player.
	// 		3.6. See doEnemyAttack() and tryFightCommand() for the damage formula.
	//		3.7. tryFightCommand() checks for enemy deaths and if so:
	//			 					(1). sets their isDead boolean to true,
	//								(2). removes enemy from party.enemies list,
	//								(3). returns 2 if all enemies are dead (check handleFight() for other return values)
	// 4. Combat continues until either: (1). the enemies are all dead (isDead boolean), (2). the entire party is dead, or (3). user presses flee
	//		4.1. See handleMove(), handleFight(), and handleFLee() for exit conditions.
	//		4.2. After all damage calculatons, healthLabel is updated with updateHealthLabel() and enemyNameLabel with updateEnemyNameLabel().
	
	public Combat() 
	{
		windowColor = new Color(18, 1, 113);
		bgColor = new Color(0, 0, 0);
		currentMoves = 0;

		endCombat = false;
	}
	
	// the main setup function
	public void update(Party partyOverWorld, Space[][] worldTile) {
		setPartyCombat(partyOverWorld);
		enemyTurn = party.getNumPlayers() + 1; // sets the enemy turn number (always 1 more than number of Players)
	   	party.getParty().get(1).setXpos(party.getPartyXpos()); // instantiate p1 position to be at party position
    	party.getParty().get(1).setYpos(party.getPartyYpos());
		setWorldGrid(worldTile);
    	
    	instantiateCombat(party, party.getPartyXpos(), party.getPartyYpos());	
    	
    	combatScreen = new JFrame();
    	combatScreen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    	combatScreen.setTitle("Battle");
    	combatScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	buildCombatPanel();
    	combatScreen.add(combatPanel);
    	combatScreen.setVisible(true);	
    	try {
			fightMusic = new AudioPlayer(bgMusic); // NOTE: remove NET to stream from local file at bgMusic
			fightMusic.play();								// also uncomment bgMusic above
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
    	
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Paint functions
	/////////////////////////////////////////////////////////////
	public void buildCombatPanel() {
		combatPanel = new JPanel(new GridBagLayout());
		combatPanel.setBackground(bgColor);
		
		// combatPanel will contain two panels (1) The map screen and (2) command screen 
		mapPanel = new MapPanel();
		mapPanel.setBackground(bgColor);
		
		// consists of 3 components (1) the commandPanel, (2) the healthLabel, and (3) the enemyNameLabel
		JPanel commandPanel = new JPanel(new GridBagLayout());
		commandPanel.setBackground(windowColor);
		commandPanel.setForeground(Color.WHITE);
		commandPanel.setBorder(new LineBorder(Color.WHITE));
		commandPanel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
		
		// build combat buttons
		combatPane = new ButtonPane("combat", windowColor, 5);
		for(JButton button : combatPane.getNumberButtons()) {
			button.addActionListener(new CombatListener()); // private inner class below
		}
		
		// build info label
		healthLabel = new JLabel("", SwingConstants.CENTER);
		healthLabel.setBackground(windowColor);
		healthLabel.setOpaque(true);
		healthLabel.setForeground(Color.WHITE);	
		healthLabel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		healthLabel.setBorder(BorderFactory.createCompoundBorder(
				healthLabel.getBorder(), 
		        BorderFactory.createLineBorder(Color.WHITE, 5)));
		
		String healthLabelString = "";
		for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
			if(player.getKey() == party.getCurrentPlayerTurn()) { // highlight currentPlayerTurn, starts with player 1
				healthLabelString += "---> ";
			} 
			if(!player.getValue().isDead()) { // normal player info
				healthLabelString += player.getValue().getName() + "          HP: " + player.getValue().getCurrentHP() + 
						"/" + player.getValue().getHP() + "     MP: " + player.getValue().getCurrentMP() + "\n";
			} else { // display DEAD status
				healthLabelString += player.getValue().getName() + " - DEAD" + "\n";
			}
			
		}
		healthLabel.setText("<html>" + healthLabelString.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		
		// build enemyNameLabel
		enemyNameLabel = new JLabel("", SwingConstants.CENTER);
		enemyNameLabel.setBackground(windowColor);
		enemyNameLabel.setOpaque(true);
		enemyNameLabel.setForeground(Color.WHITE);	
		enemyNameLabel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		enemyNameLabel.setBorder(BorderFactory.createCompoundBorder(
				enemyNameLabel.getBorder(), 
		        BorderFactory.createLineBorder(Color.WHITE, 5)));
		String enemyNames = "";
		
		// for each enemy in enemies attribute of Party, add anme to enemyNameLabel
		//IMPORTANT: enemies arraylist must be instantiated prior to update();
		for(PlayableCharacter enemy : party.getEnemies()) {
			enemyNames += enemy.getName() + "     HP: " + enemy.getCurrentHP() + "/" + enemy.getHP() +"\n";
		}
		enemyNameLabel.setText("<html>" + enemyNames.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		
		// now put all components together, first build up commandPanel
		GridBagConstraints enemyNamesCons = new GridBagConstraints();
		enemyNamesCons.gridx = 0;
		enemyNamesCons.gridy = 0;
		enemyNamesCons.fill = GridBagConstraints.BOTH;
		enemyNamesCons.weightx = 0.25;
		enemyNamesCons.weighty = 1.0;
		commandPanel.add(enemyNameLabel, enemyNamesCons);
		
		GridBagConstraints combatPaneCons = new GridBagConstraints();
		combatPaneCons.gridx = 1;
		combatPaneCons.gridy = 0;
		combatPaneCons.fill = GridBagConstraints.BOTH;
		combatPaneCons.weightx = 0.25;
		combatPaneCons.weighty = 1.0;
		commandPanel.add(combatPane, combatPaneCons);
		
		GridBagConstraints healthLabelCons = new GridBagConstraints();
		healthLabelCons.gridx = 2;
		healthLabelCons.gridy = 0;
		healthLabelCons.fill = GridBagConstraints.BOTH;
		healthLabelCons.weightx = 0.50;
		healthLabelCons.weighty = 1.0;
		commandPanel.add(healthLabel, healthLabelCons);
		
		// combine commandPanel with mapPanel into combatPanel
		GridBagConstraints mapPanelCons = new GridBagConstraints();
		mapPanelCons.gridx = 0;
		mapPanelCons.gridy = 0;
		mapPanelCons.fill = GridBagConstraints.BOTH;
		mapPanelCons.weightx = 1.0;
		mapPanelCons.weighty = 0.66;
		combatPanel.add(mapPanel, mapPanelCons);
		
		GridBagConstraints commandPanelCons = new GridBagConstraints();
		commandPanelCons.gridx = 0;
		commandPanelCons.gridy = 1;
		commandPanelCons.fill = GridBagConstraints.BOTH;
		commandPanelCons.weightx = 1.0;
		commandPanelCons.weighty = 0.33;
		combatPanel.add(commandPanel, commandPanelCons);
	}
	
	// call after damage calculations to update Player health label
	public void updateHealthLabel() {
		String healthLabelString = "";
		for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
			if(player.getKey() == party.getCurrentPlayerTurn()) { // highlight currentPlayerTurn, starts with player 1
				healthLabelString += "---> ";
			} 
			if(!player.getValue().isDead()) { // normal player info
				healthLabelString += player.getValue().getName() + "          HP: " + player.getValue().getCurrentHP() + 
						"/" + player.getValue().getHP() + "     MP: " + player.getValue().getCurrentMP() + "\n";
			} else { // display DEAD status
				healthLabelString += player.getValue().getName() + " - DEAD" + "\n";
			}
			
		}
		healthLabel.setText("<html>" + healthLabelString.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
	}
	
	public void updateEnemyLabel() {
		String enemyNames = "";
		
		// for each enemy in enemies attribute of Party, add anme to enemyNameLabel
		//IMPORTANT: enemies arraylist must be instantiated prior to update();
		for(PlayableCharacter enemy : party.getEnemies()) {
			enemyNames += enemy.getName() + "     HP: " + enemy.getCurrentHP() + "/" + enemy.getHP() +"\n";
		}
		enemyNameLabel.setText("<html>" + enemyNames.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		
	}
	
	 @Override
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
	 }
	
	// paints the above mapPanel, but with the range of the specified player shown in blue
	public void showPlayerRange(Player player) {
		mapGraphics = mapPanel.getGraphics();
		mapGraphics.setColor(Color.BLUE);
		mapGraphics.drawOval(64, 64, distEnemies, distEnemies);
		mapPanel.revalidate();
		mapPanel.repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// Combat functions
	///////////////////////////////////////////////////////////////////
	
	 // assumes that xpos and ypos are in block units, not pixel units
	 private boolean isValidMove(int xpos, int ypos) {
 		boolean isValid = true;
 		
 		if (xpos < 0 || ypos < 0) { return false; } // if negative values, return false immediately
 		
 		if(world[ypos][xpos].hasWall()) { isValid = false; }
 		
 		if (world[ypos][xpos].hasPlayer()) { isValid = false; }
 		
 		if(world[ypos][xpos].hasTreasure()) { isValid = false; }
 		
 		if(world[ypos][xpos].hasNPC()) { 
 			/* talk to NPC */ 
 			isValid = false;
 			System.out.println("Talk to NPC."); 
 		}
 		
 		if(world[ypos][xpos].hasTerrain()) { isValid = false; }
 		
 		return isValid;
 	}

	// instantiates player positions and converts from pixel units to block units
	public void instantiateCombat(Party party, int xposPx, int yposPx)
	{
		// divide pixels into ROWS and COLS, NOTE: Any decimal is cut off so 3.84 -> 3
	 	// forces player to be in neat 32x32 boxes for boundary checking purposes
		int xBlocks = party.getParty().get(1).getXpos() / (SCREEN_WIDTH / COLS);
		int yBlocks = party.getParty().get(1).getYpos() / (SCREEN_HEIGHT / ROWS);
		party.getParty().get(1).setXposBlock(xBlocks);
		party.getParty().get(1).setYposBlock(yBlocks);
		
		// initialize location of party members
 		for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
 		
 			xBlocks = player.getValue().getXpos() / (SCREEN_WIDTH / COLS);
			yBlocks = player.getValue().getYpos() / (SCREEN_HEIGHT / ROWS);
			
			// set other party member positions
			if(player.getKey() == 2) { // p2
				xBlocks = party.getParty().get(1).getXposBlock() + 1;
				yBlocks = party.getParty().get(1).getYposBlock() + 1;
			} else if(player.getKey() == 3) { // p3
				xBlocks = party.getParty().get(1).getXposBlock() - 2;
				yBlocks = party.getParty().get(1).getYposBlock() + 1;	
			} else if(player.getKey() == 4) { // p4
				xBlocks = party.getParty().get(1).getXposBlock() + 2;
				yBlocks = party.getParty().get(1).getYposBlock() - 1;	
			}
			
			int xPixels = 64;
			int yPixels = 64;
			if(isValidMove(xBlocks, yBlocks)) {
				xPixels = xBlocks * (SCREEN_WIDTH / COLS); 
				yPixels = yBlocks * (SCREEN_HEIGHT / ROWS);
			} else if(isValidMove(xBlocks + distPlayers, yBlocks)){
				xPixels = (xBlocks + distPlayers) * (SCREEN_WIDTH / COLS); 
				yPixels = yBlocks * (SCREEN_HEIGHT / ROWS);
			} else if(isValidMove(xBlocks, yBlocks + distPlayers)) {
				xPixels = xBlocks * (SCREEN_WIDTH / COLS); 
				yPixels = (yBlocks + distPlayers) * (SCREEN_HEIGHT / ROWS);
			} else if(isValidMove(xBlocks - distPlayers, yBlocks)){
				xPixels = (xBlocks - distPlayers) * (SCREEN_WIDTH / COLS); 
				yPixels = yBlocks * (SCREEN_HEIGHT / ROWS);
			} else if(isValidMove(xBlocks, yBlocks - distPlayers)){
				xPixels = xBlocks * (SCREEN_WIDTH / COLS); 
				yPixels = (yBlocks - distPlayers) * (SCREEN_HEIGHT / ROWS);
			} else if(isValidMove(xBlocks + distPlayers, yBlocks + distPlayers)){
				xPixels = (xBlocks + distPlayers) * (SCREEN_WIDTH / COLS); 
				yPixels = (yBlocks + distPlayers) * (SCREEN_HEIGHT / ROWS);
			}
			// set both pixel and block positions for each player
			player.getValue().setXpos(xPixels);
			player.getValue().setYpos(yPixels);
			player.getValue().setXposBlock(xPixels / (SCREEN_WIDTH / COLS)); // make sure these are correct
			player.getValue().setYposBlock(yPixels / (SCREEN_HEIGHT / ROWS));
			
			// only setHasPlayer if the player is not dead
			if(!player.getValue().isDead()) {
				// set hasPlayer boolean to each player's spot, must update upon moving
				world[player.getValue().getYposBlock()][player.getValue().getXposBlock()].setHasPlayer(true);
			} else { // FIXME: Not sure if here, but apparently dead players are still taking up space and preventing others
				// from moving on their tiles
				world[player.getValue().getYposBlock()][player.getValue().getXposBlock()].setHasPlayer(false);
			}
	
 		}
		
		//int numEnemies = (int)(Math.random() * party.getParty().size() + 1);
		
 		// reset xBlocks and yBlocks variables to be equal to P1 xBlocks and yBlocks
 		xBlocks = party.getParty().get(1).getXposBlock();
		yBlocks = party.getParty().get(1).getYposBlock();
		for (int i = 0; i < 3; i++) //initialize location of enemies
		{
			NPC enemy = new NPC();
			
			if (isValidMove(xBlocks + distEnemies, yBlocks))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers + distEnemies);
				newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers);
				while (!isValidMove(newxpos, newypos))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers + distEnemies);
					newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers);
				}
			}
			else if(isValidMove(xBlocks - distEnemies, yBlocks))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers - distEnemies);
				newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers);
				while (!isValidMove(newxpos, newypos))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers - distEnemies);
					newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers);
				}
			}
			else if(isValidMove(xBlocks, yBlocks + distEnemies))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers);
				newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers + distEnemies);
				while (!isValidMove(newxpos, newypos))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers);
					newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers + distEnemies);
				}
			}
			else if(isValidMove(xBlocks, yBlocks - distEnemies))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers);
				newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers - distEnemies);
				while (!isValidMove(newxpos, newypos))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xBlocks - distPlayers);
					newypos = (int)(Math.random() * distPlayers*2) + (yBlocks - distPlayers - distEnemies);
				}
			}
			// set both pixel and block positions for enemies
			// NOTE: newxpos is the block unit
			enemy.setXpos(newxpos * (SCREEN_WIDTH / COLS));
			enemy.setXposBlock(newxpos);
			enemy.setYpos(newypos * (SCREEN_HEIGHT / ROWS));
			enemy.setYposBlock(newypos);
			party.addtoEnemies(enemy);
			world[newypos][newxpos].setHasPlayer(true);
			
		}
	}
	
	public boolean movePlayerCharacter(PlayableCharacter player)
	{	
		Boolean turnsRemaining = true;
		currentMoves = 0;
		
		while (turnsRemaining)
		{
			JTextField movementField = new JTextField(5);
			movementField.setBackground(Color.WHITE);
			movementField.setForeground(Color.BLACK);
			movementField.setFocusable(true);
			movementField.requestFocusInWindow();

		    JPanel movementChoicePanel = new JPanel();
		    movementChoicePanel.setLayout(new GridBagLayout());
		    movementChoicePanel.setPreferredSize(new Dimension(300, 150)); // change dimension here
		    movementChoicePanel.setBackground(windowColor);
		    movementChoicePanel.setForeground(Color.WHITE);
		    movementChoicePanel.setBorder(new LineBorder(Color.WHITE));
		    movementChoicePanel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
		    JLabel prompt = new JLabel();
		    prompt.setBackground(windowColor);
		    prompt.setForeground(Color.WHITE);
		    prompt.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
		    prompt.setText("Player " + party.getCurrentPlayerTurn() + ", " 
		    		+ (player.getSpeed() - currentMoves) + " moves left: ");
		    
		    movementChoicePanel.add(prompt);
		    movementChoicePanel.add(movementField);
		    JOptionPane.showMessageDialog(null, movementChoicePanel);

	    	String choice = movementField.getText();
	    	System.out.println("Your choice is: " + choice);
	    	
	    	System.out.println("BEFORE: Player " + player.getName() + " xBlock = " + player.getXposBlock());
	    	System.out.println("Player " + player.getName() + " yBlock = " + player.getYposBlock());

	    	// convert from pixel values (player.getXpos()) to block values (for isValid())
	    	int xBlocks = player.getXposBlock();
	    	int yBlocks = player.getYposBlock();
	    	
	    	switch(choice) {
	    	case "w":
	    		if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks, yBlocks - 1))
					{
	    				world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false); // set old space to be movable
	    				player.setYpos((yBlocks - 1) * (SCREEN_HEIGHT / ROWS));
	    				player.setYposBlock(yBlocks - 1);
						world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true); // set new space to be taken
						currentMoves++;
						combatScreen.getContentPane().repaint();
					}
	    		break;
			case "s":
				if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks, yBlocks + 1)) 
				{
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false);
					player.setYpos((yBlocks + 1) * (SCREEN_HEIGHT / ROWS));	
					player.setYposBlock(yBlocks + 1);
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true);
					currentMoves++;
					combatScreen.getContentPane().repaint();
				}
				break;
			case "d":
				if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks + 1, yBlocks)) 
				{
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false);
					player.setXpos((xBlocks + 1) * (SCREEN_WIDTH / COLS));
					player.setXposBlock(xBlocks + 1);
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true);
					currentMoves++;
					combatScreen.getContentPane().repaint();
				}
				break;
			case "a":
				if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks - 1, yBlocks)) 
				{
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false);
					player.setXpos((xBlocks - 1) * (SCREEN_WIDTH / COLS));
					player.setXposBlock(xBlocks - 1);
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true);
					currentMoves++;
					combatScreen.getContentPane().repaint();
				}
				break;
			case "wd":
				if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks + 1, yBlocks - 1))
				{
    				world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false); // set old space to be movable
    				player.setYpos((yBlocks - 1) * (SCREEN_HEIGHT / ROWS));
    				player.setXpos((xBlocks + 1) * (SCREEN_WIDTH / COLS));
    				player.setYposBlock(yBlocks - 1);
    				player.setXposBlock(xBlocks + 1);    				
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true); // set new space to be taken
					currentMoves++;
					combatScreen.getContentPane().repaint();
				}
				break;
			case "wa":
				if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks - 1, yBlocks - 1))
				{
    				world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false); // set old space to be movable
    				player.setYpos((yBlocks - 1) * (SCREEN_HEIGHT / ROWS));
    				player.setXpos((xBlocks - 1) * (SCREEN_WIDTH / COLS));
    				player.setYposBlock(yBlocks - 1);
    				player.setXposBlock(xBlocks - 1);    				
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true); // set new space to be taken
					currentMoves++;
					combatScreen.getContentPane().repaint();
				}
				break;
			case "sd":
				if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks + 1, yBlocks + 1))
				{
    				world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false); // set old space to be movable
    				player.setYpos((yBlocks + 1) * (SCREEN_HEIGHT / ROWS));
    				player.setXpos((xBlocks + 1) * (SCREEN_WIDTH / COLS));
    				player.setYposBlock(yBlocks + 1);
    				player.setXposBlock(xBlocks + 1);    				
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true); // set new space to be taken
					currentMoves++;
					combatScreen.getContentPane().repaint();
				}
				break;
			case "sa":
				if(player.getSpeed() - currentMoves != 0 && isValidMove(xBlocks - 1, yBlocks + 1))
				{
    				world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(false); // set old space to be movable
    				player.setYpos((yBlocks + 1) * (SCREEN_HEIGHT / ROWS));
    				player.setXpos((xBlocks - 1) * (SCREEN_WIDTH / COLS));
    				player.setYposBlock(yBlocks + 1);
    				player.setXposBlock(xBlocks - 1);    				
					world[player.getYposBlock()][player.getXposBlock()].setHasPlayer(true); // set new space to be taken
					currentMoves++;
					combatScreen.getContentPane().repaint();
				}
				break;
			case "e":
			case "q":
			case "stop":
			case "0":
				currentMoves = player.getSpeed();
				break;
			default:
				break;
	    		
	    	}
	    	System.out.println("AFTER: Player " + player.getName() + " xBlock = " + player.getXposBlock());
	    	System.out.println("Player " + player.getName() + " yBlock = " + player.getYposBlock());
	    	
	    	if(player.getSpeed() - currentMoves == 0) {
	    		turnsRemaining = false;
	    		//System.out.println("NEXT MOVE");
	    		
	    		int count = 0;
	    		for(Space[] space : world) {
	    			for(Space tile : space) {
	    				if(tile.hasPlayer()) {
	    					count++;
	    				}
	    			}
	    		}
	    		System.out.println(count + " tiles have players.");
	    	}
		}

		return true;
	}
	
	 // fightResult key 
	 // 0 = normal round, fight command worked
	 // 1 = continue in same round, fight failed (out of range)
	 // 2 = all enemies are dead, quit combat
	public int tryFightCommand(PlayableCharacter player) {
		int fightResult = 0;
		currentMoves = 0;
		
		int closestEnemyIndex = party.getClosestEnemy(player);
		
		// if is in range, take damage
		if(isInRange(player, party.getEnemies().get(closestEnemyIndex))) {
			int A = player.getLevel(); // attacker's level
			int B = player.getOffense(); // attacker's offense stat
			int C = 2; // attack power
			int D = party.getEnemies().get(closestEnemyIndex).getDefense(); // defender's defense stat
			int min = 1;
		    int max = 3;
		    int Z = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
			int damage = ((((( 2*A + 2) * B * C) / D) + 8) / Z); // with level 1 and all stats at 2, values are 16, 8, and 5
			
			party.getEnemies().get(closestEnemyIndex).takeDamage(damage);
			
			JPanel damagePanel = new JPanel();
			damagePanel.setLayout(new GridBagLayout());
			damagePanel.setPreferredSize(new Dimension(300, 150)); // change dimension here
			damagePanel.setBackground(windowColor);
			damagePanel.setForeground(Color.WHITE);
			damagePanel.setBorder(new LineBorder(Color.WHITE));
			damagePanel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
		    JLabel prompt = new JLabel();
		    prompt.setBackground(windowColor);
		    prompt.setForeground(Color.WHITE);
		    prompt.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
		    
		    String damageText = "Player " + player.getName() + " (p" + party.getCurrentPlayerTurn() + ")" 
		    + " used FIGHT. " + "\n" +
		    "Enemy " + party.getEnemies().get(closestEnemyIndex).getName() + " took " + 
		    damage + " damage!" + "\n" + "It has " + party.getEnemies().get(closestEnemyIndex).getCurrentHP() + " HP remaining.";
		    prompt.setText("<html>" + damageText.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		    damagePanel.add(prompt);
		    JOptionPane.showMessageDialog(null, damagePanel);
		    
		    // check if enemy is kill and if so, kill it
		    if(party.getEnemies().get(closestEnemyIndex).getCurrentHP() == 0) {
				System.out.println("Enemy is dead.");
				party.getEnemies().get(closestEnemyIndex).setIsDead(true); // set as dead
				// remove from world (world[y][x].hasPlayer(false))
				world[party.getEnemies().get(closestEnemyIndex).getYposBlock()][party.getEnemies().get(closestEnemyIndex).getXposBlock()].setHasPlayer(false);
				// move enemy off screen
				party.getEnemies().get(closestEnemyIndex).setXposBlock(100); // FIXME:
				party.getEnemies().get(closestEnemyIndex).setYposBlock(100);
				// remove enemy sprite 
				party.getEnemies().get(closestEnemyIndex).setSprite(party.getEnemies().get(closestEnemyIndex).getDeadEnemySpriteLoc()); // change to deadEnemy sprite
				
				// remove enemy from enemies list
				party.removeEnemy(party.getEnemies().get(closestEnemyIndex));
				combatScreen.getContentPane().repaint(); // repaint
			}
		    updateEnemyLabel();
		    combatScreen.getContentPane().repaint(); // repaint
		} else {
			 	JPanel panel = new JPanel();
			 	panel.setLayout(new GridBagLayout());
			 	panel.setPreferredSize(new Dimension(400, 200)); // change dimension here
			 	panel.setBackground(windowColor);
			 	panel.setForeground(Color.WHITE);
			 	panel.setBorder(new LineBorder(Color.WHITE));
			 	panel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
			    JLabel prompt = new JLabel();
			    prompt.setBackground(windowColor);
			    prompt.setForeground(Color.WHITE);
			    prompt.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
			    String noRangeText = "Player " + player.getName() + " (p" + party.getCurrentPlayerTurn() + "):" + "\n" + 
			    "There are no enemies in range...";
			    prompt.setText("<html>" + noRangeText.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
			    panel.add(prompt);
			    JOptionPane.showMessageDialog(null, panel);
			    fightResult = 1;
		}
		

		if(party.getEnemies().size() == 0)
		{
			fightResult = 2;
		}
		return fightResult;
	}
	
	public Boolean doEnemyTurn(ArrayList<NPC> enemies) {
		boolean isPartyAlive = true;
		boolean didCombat = false;
		// setup panel for display of combat information at end of enemy Turn
		String damageText = "";
		JPanel damagePanel = new JPanel();
		damagePanel.setLayout(new GridBagLayout());
		damagePanel.setPreferredSize(new Dimension(400, 400)); // change dimension here
		damagePanel.setBackground(windowColor);
		damagePanel.setForeground(Color.WHITE);
		damagePanel.setBorder(new LineBorder(Color.WHITE));
		damagePanel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
	    JLabel prompt = new JLabel();
	    prompt.setBackground(windowColor);
	    prompt.setForeground(Color.WHITE);
	    prompt.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
	    
		for(NPC enemy : enemies) {
			Boolean turnsRemaining = true;
			currentMoves = 0;
			
			while (turnsRemaining)
			{
		    	int xBlocks = enemy.getXposBlock();
		    	int yBlocks = enemy.getYposBlock();
		    	
		    	int closestPlayerIndex = 1;
		    	closestPlayerIndex = enemy.getNearestPlayer(party);
		    	System.out.println("Closest Player Index = " + closestPlayerIndex);
		    	
		    	System.out.println("player yBlock = " + party.getParty().get(closestPlayerIndex).getYposBlock());
		    	System.out.println("enemy yBlock = " + enemy.getYposBlock());
		    	System.out.println("player xBlock = " + party.getParty().get(closestPlayerIndex).getXposBlock());
		    	System.out.println("enemy xBlock = " + enemy.getXposBlock());
		    	
		    	// rudimentary AI
		    	// Tries to match closest player's yPosBlock, then tries to match their xPosBlock
		    	if(party.getParty().get(closestPlayerIndex).getYposBlock() > enemy.getYposBlock()) {
		    		// go DOWN towards player
		    		if(enemy.getSpeed() - currentMoves != 0 && isValidMove(xBlocks, yBlocks + 1)) 
					{
						world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(false);
						enemy.setYpos((yBlocks + 1) * (SCREEN_HEIGHT / ROWS));	
						enemy.setYposBlock(yBlocks + 1);
						world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(true);
						currentMoves++;
						combatScreen.getContentPane().repaint();
					} else { // do an attack on the player
						doEnemyAttack(enemy, closestPlayerIndex, damageText, prompt);
						didCombat = true;
					    turnsRemaining = false; // end enemy turn immediately
					    updateHealthLabel();
					    combatScreen.getContentPane().repaint(); // repaint
					}
		    	} else if(party.getParty().get(closestPlayerIndex).getYposBlock() < enemy.getYposBlock()) {
		    		// go UP towards player
		    		if(enemy.getSpeed() - currentMoves != 0 && isValidMove(xBlocks, yBlocks - 1))
					{
	    				world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(false); // set old space to be movable
	    				enemy.setYpos((yBlocks - 1) * (SCREEN_HEIGHT / ROWS));
	    				enemy.setYposBlock(yBlocks - 1);
						world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(true); // set new space to be taken
						currentMoves++;
						combatScreen.getContentPane().repaint();
					} else { // do an attack on the player
						doEnemyAttack(enemy, closestPlayerIndex, damageText, prompt);
						didCombat = true;
					    turnsRemaining = false; // end enemy turn immediately
					    updateHealthLabel();
					    combatScreen.getContentPane().repaint(); // repaint
					}
		    	
		    	}else if(party.getParty().get(closestPlayerIndex).getXposBlock() > enemy.getXposBlock()) {
		    		// go RIGHT
		    		if(enemy.getSpeed() - currentMoves != 0 && isValidMove(xBlocks + 1, yBlocks)) 
					{
						world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(false);
						enemy.setXpos((xBlocks + 1) * (SCREEN_WIDTH / COLS));
						enemy.setXposBlock(xBlocks + 1);
						world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(true);
						currentMoves++;
						combatScreen.getContentPane().repaint();
					} else { // do an attack on the player
						doEnemyAttack(enemy, closestPlayerIndex, damageText, prompt);
						didCombat = true;
					    turnsRemaining = false; // end enemy turn immediately
					    updateHealthLabel();
					    combatScreen.getContentPane().repaint(); // repaint
					}
		    	} else if(party.getParty().get(closestPlayerIndex).getXposBlock() < enemy.getXposBlock()) {
		    		// go LEFT
		    		if(enemy.getSpeed() - currentMoves != 0 && isValidMove(xBlocks - 1, yBlocks)) 
					{
						world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(false);
						enemy.setXpos((xBlocks - 1) * (SCREEN_WIDTH / COLS));
						enemy.setXposBlock(xBlocks - 1);
						world[enemy.getYposBlock()][enemy.getXposBlock()].setHasPlayer(true);
						currentMoves++;
						combatScreen.getContentPane().repaint();
					} else {
						doEnemyAttack(enemy, closestPlayerIndex, damageText, prompt);
						didCombat = true;
					    turnsRemaining = false; // end enemy turn immediately
					    updateHealthLabel();
					    combatScreen.getContentPane().repaint(); // repaint
					}
		    	} 
		    	
		    	// ends current enemy turn
				if(enemy.getSpeed() - currentMoves == 0) {
					System.out.println("END ENEMY TURN");
		    		turnsRemaining = false;
		    	}
			}
		}
		if(didCombat) { // display damage info
			damagePanel.add(prompt);
			JOptionPane.showMessageDialog(null, damagePanel);
		    
		    // check if ENTIRE party is kill, if so, set isPartyAlive = false
			int deadCount = 0;
			for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
				if(player.getValue().isDead()) {
					deadCount++;
				}
			} // entire party is dead, return false to handleFight() and exit game
			if(deadCount == party.getNumPlayers()) {
				party.setIsPartyAlive(false); // set boolean isPartyAlive in party to false
				isPartyAlive = false;
			}
		}
		
		return isPartyAlive;
	}
	
	public void doEnemyAttack(NPC enemy, int closestPlayerIndex, String damageText, JLabel prompt) {
		System.out.println("ENEMY enters combat!");
		// do an attack on the closest player
		int A = enemy.getLevel(); // attacker's level
		int B = enemy.getOffense(); // attacker's offense stat
		int C = 2; // attack power
		int D = party.getParty().get(closestPlayerIndex).getDefense(); // defender's defense stat
		int min = 1;
	    int max = 3;
	    int Z = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
		int damage = ((((( 2*A + 2) * B * C) / D) + 8) / Z); // with level 1 and all stats at 2, values are 16, 8, and 5
		
		party.getParty().get(closestPlayerIndex).takeDamage(damage);
		
	    damageText += "Enemy " + enemy.getName()  + " used FIGHT. " + "\n" +
	    "Player " + party.getParty().get(closestPlayerIndex).getName() + " took " + 
	    damage + " damage!" + "\n" + party.getParty().get(closestPlayerIndex).getName() +" has " +
	    party.getParty().get(closestPlayerIndex).getCurrentHP() + " HP remaining." + "\n";
	    prompt.setText("<html>" + damageText.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
	    
	    // check if player is kill and if so, kill it
	    if(party.getParty().get(closestPlayerIndex).getCurrentHP() == 0) {
			System.out.println("PLAYER is dead.");
			party.getParty().get(closestPlayerIndex).setIsDead(true); // set as dead
			// remove from world (world[y][x].hasPlayer(false))
			world[party.getParty().get(closestPlayerIndex).getYposBlock()][party.getParty().get(closestPlayerIndex).getXposBlock()].setHasPlayer(false);
			// move player off screen (neccesary for repaint)
			party.getParty().get(closestPlayerIndex).setXposBlock(100); // FIXME:
			party.getParty().get(closestPlayerIndex).setYposBlock(100);
			// remove enemy sprite 
			//party.getEnemies().get(closestEnemyIndex).setSprite(party.getEnemies().get(closestEnemyIndex).getDeadEnemySpriteLoc()); // change to deadEnemy sprite
			
			// remove player from party list
			//party.removePlayer(closestPlayerIndex);
			combatScreen.getContentPane().repaint(); // repaint
		}

	}
	
	// for flee attempt
	public static void damageAmt(PlayableCharacter chr, int amt)
	{
		chr.setCurrentHP(chr.getCurrentHP() - 2);
	}
	
	public boolean isInRange(PlayableCharacter p, PlayableCharacter e) {
		//local variables
		int dist = 0;
		int enemyY = 0;
		int enemyX = 0;
		
		//calculate distance from player
		enemyY = e.getYposBlock() - p.getYposBlock();
		enemyX = e.getXposBlock() - p.getXposBlock();
		dist = (int) Math.sqrt(Math.pow(enemyX, 2) + Math.pow(enemyY, 2));
		
		if (p.getKit() == "Warrior") {
			if (dist <= 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (p.getKit() == "Archer" || p.getKit() == "Mage" || p.getKit() == "Cleric") {
			if (dist <= 5) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Getters and Setters
	//////////////////////////////////////////////////////////////////////
	private void setPartyCombat(Party partyOverWorld) {
		party = partyOverWorld;
	}
	
	private void setWorldGrid(Space[][] worldTile) {
		world = worldTile;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Combat Menu handling functions (Buttons set in ButtonPane class)
	//////////////////////////////////////////////////////////
	private class CombatListener implements ActionListener {
		    
		@Override
		public void actionPerformed(ActionEvent event) {
			JButton source = (JButton)(event.getSource());
			
			if(source.equals(combatPane.getNumberButtons()[0])) {
				handleMove();
			} else if(source.equals(combatPane.getNumberButtons()[1])) {
				handleFight();
				
			} else if(source.equals(combatPane.getNumberButtons()[2])) {
				handleAbilities();
				
			} else if(source.equals(combatPane.getNumberButtons()[3])) {
				handleFlee();
				
			} else if(source.equals(combatPane.getNumberButtons()[4])) {
				handleSkip();
			} 
		}	
		
		
		private void handleMove() {
			// TODO: REPAINT player ranges functionality
			//combatPanel.remove(mapPanel); // remove mapPanel
			//showPlayerRange(party.getParty().get(party.getCurrentPlayerTurn())); // repaint mapPanel with player range
																				// adjust for each player's current location
			//combatPanel.add(mapPanel); // add mapPanel back on
			
			// do enemy turn, movement
			if(party.getCurrentPlayerTurn() == enemyTurn && 
					party.isPartyAlive()) {
				doEnemyTurn(party.getEnemies());
				party.nextPlayerTurn();
				updateHealthLabel();
			} else {
				if(!party.isPartyAlive()) { // end game criteria
					// quit entire game
					System.exit(0);
				}
				// turn does not end with move, can still use FIGHT
				if(party.getParty().get(party.getCurrentPlayerTurn()).canMove() && 
						!party.getParty().get(party.getCurrentPlayerTurn()).isDead()) { // if player is dead, disallows move, maybe diable buttons
					movePlayerCharacter(party.getParty().get(party.getCurrentPlayerTurn()));
					party.getParty().get(party.getCurrentPlayerTurn()).setCanMove(false);
				}
				currentMoves = 0;
			}
			
		}
		
		// Each player's turn ends in FIGHT command
		private void handleFight() {
			// do enemy turn, movement
			if(party.getCurrentPlayerTurn() == enemyTurn && party.isPartyAlive()) {
				
				Boolean isPartyAlive = doEnemyTurn(party.getEnemies());
				if(!isPartyAlive) {
					// quit entire game
					System.exit(0);
				}
				party.nextPlayerTurn();
				updateHealthLabel();
			} else {
				// either not enemy turn and so do nothing 
				// OR the party is not alive and so we must QUIT
				if(!party.isPartyAlive()) { // end game criteria
					// quit entire game
					System.exit(0);
				}
			}
						
			int fightResult = 0; // 0 = normal round, fight command worked
								 // 1 = continue in same round, fight failed (out of range)
								 // 2 = all enemies are dead, quit combat
			if (!party.getParty().get(party.getCurrentPlayerTurn()).isDead()) {
				fightResult = tryFightCommand(party.getParty().get(party.getCurrentPlayerTurn()));
			} else {
				// if dead, skip turn
				fightResult = 3;
			}

			if(fightResult == 0) {
				party.getParty().get(party.getCurrentPlayerTurn()).setCanMove(true);
				party.nextPlayerTurn();
				updateHealthLabel();
				currentMoves = 0;
			} else if(fightResult == 1) {
				currentMoves = 0;
				// stay with same player
			}
			else if(fightResult == 2) {
				// set all players canMove() to true for next time
				for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
					player.getValue().setCanMove(true);
				}

				// quit combat
				combatScreen.remove(combatPanel);
				combatScreen.dispose();
				
				// change party isInCombat bool to false
				party.setInCombat(false);
				// remove enemies from enemies list in party
				party.clearEnemies();
				
				endCombat = true; // end combat, switch to OVERWORLD
				try {
					fightMusic.stop();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			} else if(fightResult == 3) {
				party.nextPlayerTurn();
				updateHealthLabel();
				currentMoves = 0;
			}
		}
		
		private void handleSkip() {
			// do enemy turn, movement
			if(party.getCurrentPlayerTurn() == enemyTurn && party.isPartyAlive()) {
				doEnemyTurn(party.getEnemies());
				party.nextPlayerTurn();
				updateHealthLabel();
			} else {
				// either not enemy turn and so do nothing 
				// OR the party is not alive and so we must QUIT
				if(!party.isPartyAlive()) { // end game criteria
					// quit entire game
					System.exit(0);
				}
			}
			
			party.getParty().get(party.getCurrentPlayerTurn()).setCanMove(true);
			party.nextPlayerTurn();
			updateHealthLabel();
		}
		
		private void handleFlee() {
			// do enemy turn, movement
			if(party.getCurrentPlayerTurn() == enemyTurn && party.isPartyAlive()) {
				doEnemyTurn(party.getEnemies());
				party.nextPlayerTurn();
				updateHealthLabel();
			} else {
				// either not enemy turn and so do nothing 
				// OR the party is not alive and so we must QUIT
				if(!party.isPartyAlive()) { // end game criteria
					// quit entire game
					System.exit(0);
				}
			}
			
			if ( ((int)(Math.random() * 100) + 1) > party.getFleeChance())
			{
				// set all players canMove() to true for next time
				for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
					player.getValue().setCanMove(true);
				}
				// quit combat
				combatScreen.remove(combatPanel);
				combatScreen.dispose();
				
				// change party isInCombat bool to false
				party.setInCombat(false);
				// remove enemies from enemies list in party
				party.clearEnemies();
				
				endCombat = true; // end combat, switch to OVERWORLD
				try {
					fightMusic.stop();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
						
			}
		else
		{
			// display Escape failed message, -2 health to all party members
			System.out.println("Escape failed! -2 Health to every party member!");
			for(Map.Entry<Integer, Player> member : party.getParty().entrySet()) {
				Combat.damageAmt(member.getValue(), 2);
			}
		}
		}
		
		// does nothing for now, but advance enemy turn
		private void handleAbilities() {
			if(party.getCurrentPlayerTurn() == enemyTurn) {
				doEnemyTurn(party.getEnemies());
				party.nextPlayerTurn();
				updateHealthLabel();
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// MapPanel to be painted on (the main combat screen)
	/////////////////////////////////////////////////////////////
	private class MapPanel extends JPanel {

	    public MapPanel() {
	        setBorder(BorderFactory.createLineBorder(Color.WHITE));
	        setBackground(bgColor);
	        setForeground(Color.WHITE);	
			setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
	    }

	    public Dimension getPreferredSize() {
	        return new Dimension(800, 800);
	    }

	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);  
	     // layer 1 - background
	    	for (int i = 0; i < ROWS; i++) {
	    	    for (int j = 0; j < COLS; j++) {

	    	    	g.drawImage(world[i][j].getImage(), i*TILE_WIDTH, j*TILE_HEIGHT, null);
	    	    	
	                //NOTE: draw optional borders here
	                g.setColor(Color.DARK_GRAY);
	                g.drawRect(i * TILE_WIDTH, j * TILE_HEIGHT, TILE_WIDTH ,TILE_HEIGHT);
	    	    } 
	    	}	
	    	
	    	// layer 2 - objects, foilage, terrain
	        // ...

	    	//Layer 3 - sprites
	    	
	    	// The rationale behind this for loop: party sprite painting
	    	// first the party position is converted into block units by dividing by a factor of pixel densisty (32x32) 
	    	//       and dropping the decimal
	    	// next the block units are reconverted into pixel units by multiplying by a factor of pixel density (32x32)
	    	// this ensures that the sprites and players will be locked into single blocks
	    	
	    	for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
	    
	    		int xBlocks = player.getValue().getXposBlock();
	     		int yBlocks = player.getValue().getYposBlock();

	     		int xPixels = xBlocks * (SCREEN_WIDTH / COLS);
	     		int yPixels = yBlocks * (SCREEN_HEIGHT / ROWS);
	     		
	     		if(!player.getValue().isDead()) {
	     			g.drawImage(player.getValue().getSprite(), xPixels, yPixels, null);
	     		}
	    		
	    	}
	    	
	    	// party positions are already in pixel units divisible by 32 (done in instantiateCombat())
	    	for(NPC enemy : party.getEnemies()) {
	    		
	    		// Block unit to pixels
	     		int xpos = enemy.getXposBlock() * (SCREEN_WIDTH / COLS); // xposPX / 32
	     		int ypos = enemy.getYposBlock() * (SCREEN_HEIGHT / ROWS); 
	  		
	    		g.drawImage(enemy.getSprite(), xpos, ypos, null);
	    	}
	    }  
	}

	

}
