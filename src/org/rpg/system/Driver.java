package org.rpg.system;

import org.rpg.character.*;
import org.rpg.combat.*;
import org.rpg.item.*;
import org.rpg.map.*;
import org.rpg.menu.*;

public class Driver {

	public static void main(String[] args) {
		
		// start state machine at title screen state
		StateMachine stateMachine = new StateMachine(state.TITLE_STATE);
		
		// initialize menus and world objects
		MainMenu title = new MainMenu();
		CustomizationMenu customize = new CustomizationMenu();
		OptionsMenu optionsMenu = new OptionsMenu();
		EquipmentMenu equipMenu = new EquipmentMenu();
		OverWorld world = new OverWorld();
		
		// TODO: moved player instantiation to CustomizationMenu.java
		
		// initialize player objects and add to Party
		/*Player player1 = new Player();
		Player player2 = new Player();
		Player player3 = new Player();
		Player player4 = new Player();*/
		Party party = new Party();
		
		// key, value pairs for Map<String, Player>
		/*party.addPartyMember("p1", player1);
		party.addPartyMember("p2", player2);
		party.addPartyMember("p3", player3);
		party.addPartyMember("p4", player4);*/
		
		// while the game is running, run Update function
		while(stateMachine.Update(title, customize, optionsMenu, equipMenu, world, party)) {
			
			/*main.draw();
			main.getUserInput();
			
			world.updateMap();
			// wait for keyboard input
			world.move();*/
		}
	
	}
}
