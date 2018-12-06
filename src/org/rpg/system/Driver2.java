package org.rpg.system;


import org.rpg.character.*;
import org.rpg.combat.*;
import org.rpg.item.*;
import org.rpg.map.*;
import org.rpg.menu.*;

public class Driver2 {

	public static void main(String[] args) throws InterruptedException {
		StateMachine stateMachine = new StateMachine(state.TITLE_STATE);
		Title titleScreen = new Title();
		Party party = new Party();
		Tile worldFrame = new Tile(party);
		CustomizationMenu customizationMenu = null;
		PartyMenu partyMenu = new PartyMenu(party);
		Combat combat = new Combat();
		

		boolean isRunning = true;
		while(isRunning) {
			stateMachine.Update(titleScreen, worldFrame, customizationMenu, partyMenu, party, combat);
		        //render();
		     Thread.sleep(10); //the timing mechanism
		}
		
	}
}