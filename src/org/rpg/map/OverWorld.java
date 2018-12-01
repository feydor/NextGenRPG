package org.rpg.map;

import java.util.Map;
import java.util.Scanner;

import org.rpg.character.Party;
import org.rpg.character.Player;
import org.rpg.combat.Combat;
import org.rpg.menu.Input;
import org.rpg.menu.MainMenu;

public class OverWorld extends Input implements Sprite {
	
	// Space class has methods such as hasTreasure, hasNPC, etc
	private Space grid[][];
	
	// change ROWS and COLUMNS here
	// ROWS X COLUMNS
	protected static final int ROWS = 20;
	protected static final int COLUMNS = 30;
	
	// change margins and pixel distance here
	protected static final String MARGINS = "                     "; // 20 spaces
	protected static final String PIXEL_DIST = " "; // 1 space
	private static final int SCREEN_WIDTH = 110;
	
	// player color
	//private static final String charCode = "\033[32;47;1m";
	
	// used to get keyboard input
    //private Scanner move;
    
    // used to store keyboard input
	//private String dir;
	
	protected int maxHeight;
	protected int maxWidth;
	
	//private int xpos;
	//private int ypos;

	
	
	public OverWorld(){
		grid = new Space[ROWS][COLUMNS];
    	//move = new Scanner(System.in);
		//dir = "";
		maxHeight = ROWS - 1;
		maxWidth = COLUMNS - 1;
	}

	public int getMaxHeight() { return maxHeight; }
	public int getMaxWidth() { return maxWidth; }
	
	// Provides move validation for the intended xpos and ypos on the grid
	// input: a string containing the inputted keypress e.g.(dir = move.nextLine();)
	//		  and the player's INTENDED x and y position
	// output: isValid = true, if there is no move validation errors, else isValid = false
	public boolean isValidMove(String dir, int xpos, int ypos) {

		boolean isValid = true;

		if(grid[ypos][xpos].hasWall()) { isValid = false; }
		
		if(grid[ypos][xpos].hasTreasure()) { 
			/* get Treasure */ 
			System.out.println("Get Treasure.");
		}
		
		if(grid[ypos][xpos].hasNPC()) { 
			/* talk to NPC */ 
			isValid = false;
			System.out.println("Talk to NPC."); 
		}
		
		if(grid[ypos][xpos].hasTerrain()) { isValid = false; }
		
		if(grid[ypos][xpos].hasTrap()) { /* handle the trap */ }
		
		if(isValid == false) {
			System.out.println("Invalid move.");
		}
		
		return isValid;
	}
	
	// updates the world map with the positions of Playable Characters, Items, and NPCs 
	// inputs: TODO: the x and y positions of any Playable Characters and Items on the map
	// output: the updated world map
	// NOTE: This is done for each move, so each turn there is a chance of a monster encounter
	public void updateMap(Party party) {
		// NOTE: This only works on non-Eclipse terminal console.
		System.out.print("\033[H\033[2J");  
		System.out.flush();
		
		
		
		System.out.println("   +-----------+                                                                   +---------------------+\n" + 
				"   |  Menu(m)  |                               THE WORLD                           |          †          |\n" + 
				"   +-----------+                            Use WASD to move                       +---------------------+\n" + 
				" ");
    	
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
		        grid[party.getPartyYpos()][party.getPartyXpos()] = new Space(PARTY_CHAR);
		        System.out.print(PIXEL_DIST + grid[i][j].getSprite());
		    }
		    System.out.print("\n" + MARGINS);         
		}
		
		// print bottom ascii ui
		System.out.print("\n\n");
		for (int i = 0; i < SCREEN_WIDTH; i++) {
			System.out.print("-");
		}
		System.out.println();
		
		// print name and lvl section
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print("+-----------------------+" + "    ");
		}
		System.out.println();
		
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print("| " + party.getParty().get(i + "").getName()
					+ " | Lv" + party.getParty().get(i + "").getLevel() + "          |    ");
		}
		System.out.println();
		
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print("+-----------------------+" + "    ");
		}
		System.out.println();
		
		// print class section
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print("+-----------------------+" + "    ");
		}
		System.out.println();
		
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print("| Class: " + party.getParty().get(i + "").getKit()
					+ "        |    ");
		}
		System.out.println();
		
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print("+-----------------------+" + "    ");
		}
		System.out.println();
		
		// print currentHP
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print(" HP: " + party.getParty().get(i + "").getCurrentHP() + " / " + party.getParty().get(i + "").getHP()
					+ "                 ");
		}
		System.out.println();
		
		// print currentMP
		for(int i = 1; i <= party.getParty().size(); i++) {
			System.out.print(" MP: " + party.getParty().get(i + "").getCurrentMP() + " / " + party.getParty().get(i + "").getMP()
					+ "                 ");
		}
		System.out.println();
		
		System.out.println("Current player position is: ");
		System.out.println("(" + party.getPartyXpos() + "," + party.getPartyYpos() + ")");
		
		// now check for monster encounter, except if at initial starting point
				if ((Math.random() < 0.05) && !grid[party.getPartyYpos()][party.getPartyXpos()].hasTreasure() && (party.getPartyYpos() != 1 || party.getPartyXpos() != 1)) 
				{
					Combat combat = new Combat();
					party.setInCombat(true);			
					combat.enterCombat(party, party.getPartyXpos(), party.getPartyYpos(), grid);
					party.clearEnemies();
					party.setInCombat(false);
				}
		
		
	
	
	}
	
	// handles a PlayerControlledCharacter's x and y position as a function of wasd input
	// input: a PlayerControlledCharacter's xpos and ypos
	// output: a change in xpos/ypos and a call to updateMap(), if the move is valid
	public String move(Party party){
		// get the user's keypress
		this.queryUser();
		
		switch(choice){
		case "w":
			if(isValidMove(choice, party.getPartyXpos(), party.getPartyYpos() - 1)) {
				party.decrementYPOS();
			}	
			break;
		case "s":
			if(isValidMove(choice, party.getPartyXpos(), party.getPartyYpos() + 1)) {
				party.incrementYPOS();
			}
			break;
		case "d":
			if(isValidMove(choice, party.getPartyXpos() + 1, party.getPartyYpos())) {
				party.incrementXPOS();
			}
			break;
		case "a":
			if(isValidMove(choice, party.getPartyXpos() - 1, party.getPartyYpos())) {
				party.decrementXPOS();
			}
			break;
		default: 
			// not a movement, return it
			System.out.println("The user inputted: " + choice);
			break;
		}
		
		// returns user input to StateMachine
		return choice;
	}
	
public static void main(String[] args) {
		
		OverWorld world = new OverWorld();
		//Player player = new Player();
		
		//world.updateMap();
	
		while(1 > 0) {
			// wait for keyboard input
			//world.move();
		}
	
	}
	
}


