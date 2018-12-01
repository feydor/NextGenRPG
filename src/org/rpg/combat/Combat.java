package org.rpg.combat;

import java.util.Map;
import java.util.Map.Entry;

import org.rpg.character.*;
import org.rpg.map.*;

public class Combat extends OverWorld
{
	private int newxpos = 0;
	private int newypos = 0;
	
	private int distPlayers = 4; //closeness of party to their center [friendly and enemy]
	private int distEnemies = 8; //how far apart friendly and enemies centers are
	
	public Combat() {}
	
	public void damageAmt(PlayableCharacter chr, int amt)
	{
		chr.setCurrentHP(chr.getCurrentHP() - 2);
	}
	
	private boolean isValidMove(int xpos, int ypos, Space grid[][]) {

		if (xpos < 1 || ypos < 1 || xpos > maxWidth || ypos > maxHeight) { return false; }

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
	
	public void updateMapforCombat(Party party, Entry<Integer, Player> entry2, Space grid[][], int xpos, int ypos)
	{
		System.out.print("\033[H\033[2J");  
		System.out.flush();
		System.out.println(" _   .-')                     .-') _   .-')    .-') _     ('-.  _  .-')                                       ('-. .-. .-') _    \n" + 
				"( '.( OO )_                  ( OO ) ) ( OO ). (  OO) )  _(  OO)( \\( -O )                                     ( OO )  /(  OO) )   \n" + 
				" ,--.   ,--.).-'),-----. ,--./ ,--,' (_)---\\_)/     '._(,------.,------.          ,------.,-.-')   ,----.    ,--. ,--./     '._  \n" + 
				" |   `.'   |( OO'  .-.  '|   \\ |  |\\ /    _ | |'--...__)|  .---'|   /`. '      ('-| _.---'|  |OO) '  .-./-') |  | |  ||'--...__) \n" + 
				" |         |/   |  | |  ||    \\|  | )\\  :` `. '--.  .--'|  |    |  /  | |      (OO|(_\\    |  |  \\ |  |_( O- )|   .|  |'--.  .--' \n" + 
				" |  |'.'|  |\\_) |  |\\|  ||  .     |/  '..`''.)   |  |  (|  '--. |  |_.' |      /  |  '--. |  |(_/ |  | .--, \\|       |   |  |    \n" + 
				" |  |   |  |  \\ |  | |  ||  |\\    |  .-._)   \\   |  |   |  .--' |  .  '.'      \\_)|  .--',|  |_.'(|  | '. (_/|  .-.  |   |  |    \n" + 
				" |  |   |  |   `'  '-'  '|  | \\   |  \\       /   |  |   |  `---.|  |\\  \\         \\|  |_)(_|  |    |  '--'  | |  | |  |   |  |    \n" + 
				" `--'   `--'     `-----' `--'  `--'   `-----'    `--'   `------'`--' '--'         `--'    `--'     `------'  `--' `--'   `--' ");
		
		System.out.println("   +-----------+                                                                   +---------------------+\n" + 
				"   |  Menu(m)  |                               THE WORLD                           |          †          |\n" + 
				"   +-----------+                            Use WASD to move                       +---------------------+\n" + 
				" ");


		/*System.out.println("");
		System.out.println("------------The World------------");
    	System.out.println("---------------------------------");
    	System.out.println("");*/
		
		
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
		System.out.print("\n");
		System.out.println("----------------------------------------------------------------------------------------------------------\n" +
				"+-----------------------+\n" + 
				"| "+ entry2.getValue().getName() + "'s turn - Player: " + entry2.getKey() +"\n" + 
				"+-----------------------+\n" + 
				"+-----------------------+  +-----------------------+  +-----------------------+  +-----------------------+\n" + 
				"| Abilities:                       "+ "Enemy @"+"               "+"Enemy $"+"                  "+"Enemy &\n"+
				"+-----------------------+  +-----------------------+  +-----------------------+  +-----------------------+\n" + 
				" # (1) Basic Attack                  HP: "+ party.getEnemies().get(0).getCurrentHP() +"/"+party.getEnemies().get(0).getHP() +"              HP: " + party.getEnemies().get(1).getCurrentHP() +"/" +party.getEnemies().get(1).getHP() +"              HP: " + party.getEnemies().get(2).getCurrentHP() +"/" +party.getEnemies().get(2).getHP() +"\n" +
				" # (flee) Attempt escape\n"+
				" # (skip) Skip turn\n");
	}
	
	public void instantiateCombat(Party party, int xpos, int ypos, Space grid[][])
	{
		System.out.print("\033[H\033[2J");  
		System.out.flush();
		
		//just for fun
		System.out.println(
				"+-------------------------------------------------------------------------------------------------------------------------+\n"+
				"  _   .-')                     .-') _   .-')    .-') _     ('-.  _  .-')                                       ('-. .-. .-') _    \n" + 
				" ( '.( OO )_                  ( OO ) ) ( OO ). (  OO) )  _(  OO)( \\( -O )                                     ( OO )  /(  OO) )   \n" + 
				"  ,--.   ,--.).-'),-----. ,--./ ,--,' (_)---\\_)/     '._(,------.,------.          ,------.,-.-')   ,----.    ,--. ,--./     '._  \n" + 
				"  |   `.'   |( OO'  .-.  '|   \\ |  |\\ /    _ | |'--...__)|  .---'|   /`. '      ('-| _.---'|  |OO) '  .-./-') |  | |  ||'--...__) \n" + 
				"  |         |/   |  | |  ||    \\|  | )\\  :` `. '--.  .--'|  |    |  /  | |      (OO|(_\\    |  |  \\ |  |_( O- )|   .|  |'--.  .--' \n" + 
				"  |  |'.'|  |\\_) |  |\\|  ||  .     |/  '..`''.)   |  |  (|  '--. |  |_.' |      /  |  '--. |  |(_/ |  | .--, \\|       |   |  |    \n" + 
				"  |  |   |  |  \\ |  | |  ||  |\\    |  .-._)   \\   |  |   |  .--' |  .  '.'      \\_)|  .--',|  |_.'(|  | '. (_/|  .-.  |   |  |    \n" + 
				"  |  |   |  |   `'  '-'  '|  | \\   |  \\       /   |  |   |  `---.|  |\\  \\         \\|  |_)(_|  |    |  '--'  | |  | |  |   |  |    \n" + 
				"  `--'   `--'     `-----' `--'  `--'   `-----'    `--'   `------'`--' '--'         `--'    `--'     `------'  `--' `--'   `--' \n" +
				"+-------------------------------------------------------------------------------------------------------------------------+");
	
		System.out.println("Monster Encounter! - Prepare to Fight!! [Press any key to begin]");
		this.queryUser();
		
		
		System.out.println("   +-----------+                                                                   +---------------------+\n" + 
				"   |  Menu(m)  |                               THE WORLD                           |          †          |\n" + 
				"   +-----------+                            Use WASD to move                       +---------------------+\n" + 
				" ");


		/*System.out.println("");
		System.out.println("------------The World------------");
    	System.out.println("---------------------------------");
    	System.out.println("");*/
		
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
	
}
