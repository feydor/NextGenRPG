package org.rpg.menu;

import java.util.Map;

import org.rpg.character.Party;
import org.rpg.character.Player;

public class EquipmentMenu extends Input{
	
	//private final static int WEAPON_SLOT = 6;
	
	public void draw(Party party) {
		System.out.print("\033[H\033[2J");  
		System.out.flush();
		
		int index = 1;
		System.out.println("Choose which player's equipment to modify:");
		for(Map.Entry<Integer, Player> entry : party.getParty().entrySet()) {
			  System.out.println(index + ": " + entry.getValue().getName());
			  index++;
		}
		System.out.print(">>");
		// will get valid user input between 1 and 4,
		// if there is invalid input, 1 is returned
		int playerChoice = getValidPlayer(this.queryUser(), party);
		
		System.out.println("+------------------------------------------+\n" + 
				"| Weapon: none" + /*party.getParty().get("p" + playerChoice).getEquipment().get(6)*/ "\n" + 
				"|\n" + 
				"| Head: none\n" + 
				"|\n" + 
				"| Body: none\n" + 
				"|\n" + 
				"| Arms: none\n" + 
				"|\n" + 
				"| Legs: none\n" + 
				"|\n" + 
				"| Other: none\n" + 
				"+\n" + 
				"");

	}
	
	// makes sure the passed in String is anumber between 1 and 4 
	// and that the player corresponsing to that number is in party
	private int getValidPlayer(String queryUser, Party party) {
		int playerNumber = 1;
		
		switch(queryUser) {
		case "1":
			playerNumber = 1;
			break;
		case "2":
			playerNumber = 2;
			break;
		case "3":
			playerNumber = 3;
			break;
		case "4": 
			playerNumber = 4;
			break;
		default:
			// anything else, set playerNumber to 1
			System.out.println("Invalid input. Selecting player 1.");
			playerNumber = 1;
			break;
		}
		
		// checks to see if choice is in party
		// playerNumbers range from 1 - 4, 
		// party.getParty().size() ranges from 0 - 4
		if(playerNumber <= party.getParty().size()) {
			return playerNumber;
		} else {
			System.out.println("Invalid player. Selecting player 1.");
			return 1;
		}
		
	}
	

}