package org.rpg.combat;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.rpg.character.*;
import org.rpg.map.*;
import org.rpg.menu.ButtonPane;
import org.rpg.system.KeyBinding;

public class Combat extends Tile{
	private JFrame combatScreen;
	private JPanel combatPanel;
	private String bgMusic = "/home/codreanu/Documents/School/Fall2018/ECE373/RPG_proj/music/Xak.wav";
	private Color windowColor;
	private Color bgColor;
	private ButtonPane combatPane;
	
	private Party party;
	
	private int newxpos = 0;
	private int newypos = 0;
	
	private int distPlayers = 4; //closeness of party to their center [friendly and enemy]
	private int distEnemies = 8; //how far apart friendly and enemies centers are
	
	
	public Combat() {
		combatScreen = new JFrame();
		windowColor = new Color(18, 1, 113);
		bgColor = new Color(0, 0, 0);
	}
	
	public void buildCombatPanel() {
		combatPanel = new JPanel(new GridBagLayout());
		combatPanel.setBackground(bgColor);
		
		// combatPanel will contain two panels (1) The map screen and (2) command screen 
		JPanel mapPanel = new JPanel();
		mapPanel.setBackground(bgColor);
		//mapPanel.paint(Graphics graphics); // TODO: somehow need to paint the map screen here
		
		// consists of 3 components (1) the commandPanel, (2) the healthLabel, and (3) the enemyNameLabel
		JPanel commandPanel = new JPanel(new GridBagLayout());
		commandPanel.setBackground(windowColor);
		commandPanel.setForeground(Color.WHITE);
		commandPanel.setBorder(new LineBorder(Color.WHITE));
		commandPanel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
		
		// build combat buttons
		combatPane = new ButtonPane("combat", windowColor, 4);
		for(JButton button : combatPane.getNumberButtons()) {
			button.addActionListener(new CombatListener()); // private inner class below
		}
		
		// build info label
		JLabel healthLabel = new JLabel("", SwingConstants.CENTER);
		healthLabel.setBackground(windowColor);
		healthLabel.setOpaque(true);
		healthLabel.setForeground(Color.WHITE);	
		healthLabel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		healthLabel.setBorder(BorderFactory.createCompoundBorder(
				healthLabel.getBorder(), 
		        BorderFactory.createLineBorder(Color.WHITE, 5)));
		
		String healthLabelString = "";
		for(Map.Entry<Integer, Player> player : party.getParty().entrySet()) {
			healthLabelString += player.getValue().getName() + "          " + player.getValue().getCurrentHP() + 
					"/" + player.getValue().getHP() + "     " + player.getValue().getCurrentMP() + "\n";
		}
		healthLabel.setText("<html>" + healthLabelString.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		
		// build enemyNameLabel
		JLabel enemyNameLabel = new JLabel("", SwingConstants.CENTER);
		enemyNameLabel.setBackground(windowColor);
		enemyNameLabel.setOpaque(true);
		enemyNameLabel.setForeground(Color.WHITE);	
		enemyNameLabel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		enemyNameLabel.setBorder(BorderFactory.createCompoundBorder(
				enemyNameLabel.getBorder(), 
		        BorderFactory.createLineBorder(Color.WHITE, 5)));
		String enemyNames = "";
		
		// for each enemy in enemies attribute of Party, add anme to enemyNameLabel
		// IMPORTANT: enemies arraylist must be instantiated prior to update();
//		for(PlayableCharacter enemy : party.getEnemies()) {
//			enemyNames += enemy.getName() + "\n";
//		}
//		enemyNameLabel.setText("<html>" + enemyNames.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		
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
	
	public void update(Party partyOverWorld) {
		setPartyCombat(partyOverWorld);
    	KeyBinding mainPanel = new KeyBinding(); 
    	combatScreen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    	combatScreen.setTitle("Battle");
    	combatScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	//combatScreen.setContentPane(new Tile(partyOverWorld));
    	
    	buildCombatPanel();
    	combatScreen.add(combatPanel);
    	combatScreen.setVisible(true);	
        
        // open the sound file as a Java input stream
//        BufferedInputStream audioStream;
//        InputStream in;
//        AudioInputStream audioIn;
//        Clip clip;
//		try {
//			in = new FileInputStream(bgMusic);
//			audioStream = new BufferedInputStream(in);
//			audioIn = AudioSystem.getAudioInputStream(audioStream);
//			clip = AudioSystem.getClip();
//			clip.open(audioIn);
//			clip.start();
//		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
//			e1.printStackTrace();
//		}
	}
	

	public void damageAmt(PlayableCharacter chr, int amt)
	{
		chr.setCurrentHP(chr.getCurrentHP() - 2);
	}
	
	private boolean isValidMove(int xpos, int ypos, Space grid[][]) {

		//if (xpos < 1 || ypos < 1 || xpos > maxWidth || ypos > maxHeight) { return false; }
		 if (xpos < 0 || ypos < 0) { return false; }

		if (grid[ypos][xpos].hasWall()) { return false; }
		
		if (grid[ypos][xpos].hasTerrain()) { return false; }
		
		if (grid[ypos][xpos].hasPlayer()) { return false; }
		
		if (grid[ypos][xpos].hasTreasure()) { 
			System.out.println("Get Treasure.");
			return false;
		}
		
		if (grid[ypos][xpos].hasNPC()) { 
			System.out.println("Talk to NPC."); 
			return false;
		}
		return true;
	}
	
	/*public void updateMapforCombat(Party party, Entry<Integer, Player> entry2, Space grid[][], int xpos, int ypos)
	{
		for(int i = 0; i < ROWS; i++) {          
		    for(int j = 0; j < COLS; j++) {
		        grid[i][j] = new Space(FIELD_CHAR);
		        
		        // make walls
		        if(i == 0 || i == maxHeight || j == 0 || j == maxWidth) {
		        	grid[i][j] = new Space(WALLS_CHAR);
		        }
		        
		        // test NPC
		        grid[8][8] = new Space(NPC_CHAR);
		        
		        // test Terrain
		        grid[2][8] = new Space(TERRAIN_CHAR);
		        grid[3][8] = new Space(TERRAIN_CHAR);
		        grid[3][9] = new Space(TERRAIN_CHAR);
		        grid[3][7] = new Space(TERRAIN_CHAR);
		        grid[4][8] = new Space(TERRAIN_CHAR);

		        grid[maxHeight - 1][maxWidth - 1] = new Space(ITEM_CHAR);
		        for(Map.Entry<Integer, Player> entry : party.getParty().entrySet())
		        {	
		        	grid[entry.getValue().getYpos()][entry.getValue().getXpos()] = new Space(entry.getKey());
		        }
		        for(int z = 0; z < party.getEnemies().size(); z++)
		        {
		        	if (party.getEnemies().get(z).getCurrentHP() > 0)
		        	{
		        		grid[party.getEnemies().get(z).getYpos()][party.getEnemies().get(z).getXpos()] = new Space(party.getEnemies().get(z).getSSprite());
		        	}
		        }
		        
		        if (!grid[i][j].hasPlayer())
		        {
		        	System.out.print(PIXEL_DIST + grid[i][j].getSprite());
		        }
		        else
		        {
		        	System.out.print(PIXEL_DIST + grid[i][j].getSSprite());
		        }
		    }
		    System.out.print("\n" + MARGINS);         
		}
		*/
	
	public void instantiateCombat(Party party, int xpos, int ypos, Space grid[][])
	{
		
		for(Map.Entry<Integer, Player> entry : party.getParty().entrySet()) //initialize location of party members
		{	
			newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers);
			newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers);
			
			while (!isValidMove(newxpos, newypos, grid))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers);
				newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers);
			}
			
			entry.getValue().setXpos(newxpos);
			entry.getValue().setYpos(newypos);
			grid[newypos][newxpos].setHasPlayer(true);
			
		}
		
		//int numEnemies = (int)(Math.random() * party.getParty().size() + 1);
		
		for (int i = 0; i < 3; i++) //initialize location of enemies
		{
			NPC enemy = new NPC();
			
			if (isValidMove(xpos + distEnemies, ypos, grid))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers + distEnemies);
				newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers);
				while (!isValidMove(newxpos, newypos, grid))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers + distEnemies);
					newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers);
				}
			}
			else if(isValidMove(xpos - distEnemies, ypos, grid))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers - distEnemies);
				newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers);
				while (!isValidMove(newxpos, newypos, grid))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers - distEnemies);
					newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers);
				}
			}
			else if(isValidMove(xpos, ypos + distEnemies, grid))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers);
				newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers + distEnemies);
				while (!isValidMove(newxpos, newypos, grid))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers);
					newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers + distEnemies);
				}
			}
			else if(isValidMove(xpos, ypos - distEnemies, grid))
			{
				newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers);
				newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers - distEnemies);
				while (!isValidMove(newxpos, newypos, grid))
				{
					newxpos = (int)(Math.random() * distPlayers*2) + (xpos - distPlayers);
					newypos = (int)(Math.random() * distPlayers*2) + (ypos - distPlayers - distEnemies);
				}
			}
			enemy.setXpos(newxpos);
			enemy.setYpos(newypos);
			party.addtoEnemies(enemy);
			grid[newypos][newxpos].setHasPlayer(true);
		}
		
		System.out.print(MARGINS);
		for(int i = 0; i < ROWS; i++) {          
		    for(int j = 0; j < COLUMNS; j++) {
		        grid[i][j] = new Space(FIELD_CHAR);
		        
		        // make walls
		        if(i == 0 || i == maxHeight || j == 0 || j == maxWidth) {
		        	grid[i][j] = new Space(WALLS_CHAR);
		        }
		        
		        // test NPC
		        grid[8][8] = new Space(NPC_CHAR);
		        
		        // test Terrain
		        grid[2][8] = new Space(TERRAIN_CHAR);
		        grid[3][8] = new Space(TERRAIN_CHAR);
		        grid[3][9] = new Space(TERRAIN_CHAR);
		        grid[3][7] = new Space(TERRAIN_CHAR);
		        grid[4][8] = new Space(TERRAIN_CHAR);

		        grid[maxHeight - 1][maxWidth - 1] = new Space(ITEM_CHAR);
		        
		        for(Map.Entry<Integer, Player> entry : party.getParty().entrySet())
		        {
		        	grid[entry.getValue().getYpos()][entry.getValue().getXpos()] = new Space(entry.getKey());
		        }
		        for(int z = 0; z < party.getEnemies().size(); z++)
		        {
		        	switch(z)
		        	{
		        	case 0:
		        		party.getEnemies().get(z).setSSprite("@");
		        		break;
		        	case 1:
		        		party.getEnemies().get(z).setSSprite("$");
		        		break;
		        	case 2:
		        		party.getEnemies().get(z).setSSprite("&");
		        		break;
		        	case 3:
		        		party.getEnemies().get(z).setSSprite("%");
		        		break;
		        	}
		        	grid[party.getEnemies().get(z).getYpos()][party.getEnemies().get(z).getXpos()] = new Space(party.getEnemies().get(z).getSSprite());
		        }
		        
		        if (!grid[i][j].hasPlayer())
		        {
		        	System.out.print(PIXEL_DIST + grid[i][j].getSprite());
		        }
		        else
		        {
		        	System.out.print(PIXEL_DIST + grid[i][j].getSSprite());
		        }
		    }
		    System.out.print("\n" + MARGINS);         
		}
		System.out.print("\n");
				
		System.out.println("----------------------------------------------------------------------------------------------------------\n"+
				"+-----------------------+\n" + 
				"| "+ party.getParty().get("1").getName()+ "'s turn" + " Player: 1" + "\n" + 
				"+-----------------------+\n" + 
				"+-----------------------+  +-----------------------+  +-----------------------+  +-----------------------+\n" + 
				"| Abilities:                       "+ "Enemy @"+"               "+"Enemy $"+"                  "+"Enemy &\n"+
				"+-----------------------+  +-----------------------+  +-----------------------+  +-----------------------+\n" + 
				" # (1) Basic Attack                  HP: "+ party.getEnemies().get(0).getCurrentHP() +"/"+party.getEnemies().get(0).getHP() +"              HP: " + party.getEnemies().get(1).getCurrentHP() +"/" +party.getEnemies().get(1).getHP() +"              HP: " + party.getEnemies().get(2).getCurrentHP() +"/" +party.getEnemies().get(2).getHP() +"\n" +
				" # (flee) Attempt escape\n"+
				" # (skip) Skip turn\n");
	}
	
	public void enterCombat(Party party, int xpos, int ypos, Space grid[][])
	{	
		Boolean enemiesAlive = true;
		Combat combat = new Combat();
		
		instantiateCombat(party, xpos, ypos, grid);
		
		while (enemiesAlive)
		{
			for(Map.Entry<Integer, Player> entry : party.getParty().entrySet())
			{
				Boolean nextTurn = false;
				int currentMoves = 0;
				
				while (!nextTurn)
				{
					updateMapforCombat(party, entry, grid, xpos, ypos);
					System.out.println("What will you do "+ entry.getValue().getName() + "? WASD MOVES LEFT: " + (entry.getValue().getSpeed() - currentMoves)); 
					this.queryUser();
					switch(choice)
					{
					case "w":
						if(entry.getValue().getSpeed() - currentMoves != 0 && isValidMove(entry.getValue().getXpos(), entry.getValue().getYpos() - 1, grid))
						{
							entry.getValue().decrementYPOS();
							currentMoves++;
						}
						break;
					case "s":
						if(entry.getValue().getSpeed() - currentMoves != 0 && isValidMove(entry.getValue().getXpos(), entry.getValue().getYpos() + 1, grid)) 
						{
							entry.getValue().incrementYPOS();
							currentMoves++;
						}
						break;
					case "d":
						if(entry.getValue().getSpeed() - currentMoves != 0 && isValidMove(entry.getValue().getXpos() + 1, entry.getValue().getYpos(), grid)) 
						{
							entry.getValue().incrementXPOS();
							currentMoves++;
						}
						break;
					case "a":
						if(entry.getValue().getSpeed() - currentMoves != 0 && isValidMove(entry.getValue().getXpos() - 1, entry.getValue().getYpos(), grid)) 
						{
							entry.getValue().decrementXPOS();
							currentMoves++;
						}
						break;
					case "1":
						System.out.println("Attack which Enemy? Enter Symbol (@/$/&) or (exit)");
						this.queryUser();
						switch (choice)
						{
							case "@":
								party.getEnemies().get(0).setCurrentHP(party.getEnemies().get(0).getCurrentHP() - 10);
								break;
							case "$":
								if (party.getEnemies().size() >= 2)
								{
									party.getEnemies().get(1).setCurrentHP(party.getEnemies().get(1).getCurrentHP() - 10);
								}
								else
								{
									System.out.println("Invalid Enemy");
								}
								break;
							case "&":
								if (party.getEnemies().size() >= 3)
									party.getEnemies().get(2).setCurrentHP(party.getEnemies().get(2).getCurrentHP() - 10);
								else
								{
									System.out.println("Invalid Enemy");
								}
								break;
							case "%":
								if (party.getEnemies().size() == 4)
									party.getEnemies().get(3).setCurrentHP(party.getEnemies().get(3).getCurrentHP() - 10);
								else
								{
									System.out.println("Invalid Enemy");
								}
								break;
							case "exit":
								break;
							default:
								break;
						}
						
						nextTurn = true;
						break;
					case "flee":
						if ( ((int)(Math.random() * 100) + 1) > party.getFleeChance())
							{
								return;	
							}
						else
						{
							System.out.println("Escape failed! -2 Health to every party member!");
							for(Map.Entry<Integer, Player> member : party.getParty().entrySet())
							{
								combat.damageAmt(member.getValue(), 2);
							}
						}
						break;
					default:
						break;
					}
					if(party.getEnemies().get(0).getCurrentHP() <= 0 && party.getEnemies().get(1).getCurrentHP() <= 0 && party.getEnemies().get(2).getCurrentHP() <= 0)
					{
						enemiesAlive = false;
						break;
					}
				}
			}
		}
		return;
	}
	
	public boolean isInRange(PlayableCharacter p, PlayableCharacter e) {
		//local variables
		int dist = 0;
		int enemyY = 0;
		int enemyX = 0;
		
		//calculate distance from player
		enemyY = e.getYpos() - p.getYpos();
		enemyX = e.getXpos() - p.getXpos();
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
	

	private void setPartyCombat(Party partyOverWorld) {
		party = partyOverWorld;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Combat Menu handling functions
	//////////////////////////////////////////////////////////
	private class CombatListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton source = (JButton)(event.getSource());
			
			if(source.equals(combatPane.getNumberButtons()[0])) {
				// handleFight();
			} else if(source.equals(combatPane.getNumberButtons()[1])) {
				// handleAbilities();
			} else if(source.equals(combatPane.getNumberButtons()[2])) {
				// handleFlee();
			} else if(source.equals(combatPane.getNumberButtons()[3])) {
				// handleSkip();
			} else if(source.equals(combatPane.getNumberButtons()[4])) {
				// ...
			} 
			
		}
		
	}
}
