package org.rpg.system;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import org.rpg.character.Party;
import org.rpg.character.Player;
import org.rpg.item.EquipableItem;
import org.rpg.item.Gear;
import org.rpg.item.UsableItem;
import org.rpg.item.Weapon;
import org.rpg.map.Tile;
import org.rpg.menu.CustomizationMenu;
import org.rpg.menu.PartyMenu;
import org.rpg.menu.Title;

public class StateMachine {
	
	private state currentState;
	
	public StateMachine(state state) {
		 currentState = state;
	}
	
	public void setState(state state) { currentState = state; }
	
	// Update will continue running, unless the game is ended and isRunning is set to false
	// NOTE: To switch to a new frame, dispose of the current frame using frame.dispose() 
	// and create the new frame before switching to the next state, 
	// see the transition between TITLE_STATE and OVERWORLD_STATE
	public boolean Update(Title titleScreen, Tile worldFrame, CustomizationMenu customizationMenu, PartyMenu partyMenu,
			Party party) {
		boolean isRunning = true;
		
		if (currentState != null) {
			 // handle update
		    switch(currentState) {
		       case TITLE_STATE:
		    	  
		          if(titleScreen.getStartBool()) {
		        	  currentState = state.CUSTOMIZATION_STATE;
		        	  // instantiate customization state
			          customizationMenu = new CustomizationMenu(party);
		        	  
		          } else if(titleScreen.getLoadBool()) {
		        	  currentState = state.OVERWORLD_STATE;
		        	  
		        	  // create next frame here
		        	  worldFrame.updateFrame();
		        	  
		          } else if(titleScreen.getDebugBool()) {
		        	  currentState = state.OVERWORLD_STATE;
		        	  // NOTE: Instantiate party here with the Debug/Default values
		        	  UsableItem testItem = new UsableItem();
		    		   party.setNumPlayers(4);
		    		   for(int i = 0; i < party.getNumPlayers(); i++) {
		    			   Player p = new Player("Warrior");
		    			   p.setName("DEBUG");
		    			   p.addItem(testItem);
		    			   try {
		    				   p.setAvatar(new ImageIcon(new URL("https://i.ibb.co/2WgZzh4/t-oak.gif")));
		    			   } catch (MalformedURLException e) {
		    				   e.printStackTrace();
						   }
		    			   party.addPartyMember((i + 1), p); // 1, 2, 3, 4
		    		   }
		        	  // create next frame here
		        	  worldFrame.setParty(party);
		        	  worldFrame.updateFrame();
		        	  
		          } else {currentState = state.TITLE_STATE; }
		          
		          break;
		       case CUSTOMIZATION_STATE:
		           
		    	   if(customizationMenu.customizationComplete) { 
		    		   System.out.println("changing state to OVERWORLD");
		    		   currentState = state.OVERWORLD_STATE;
		    		   System.out.println("Begin new frame");
		    		   // add starter inventory to p1
		    		   UsableItem potion = new UsableItem("Potion", "Heals 10 HP.");
		    		   UsableItem levelUp = new UsableItem("Secret Sauce", "One packet of Secret Sauce (tm). Levels up your character.");
		    		   Gear helmet = new Gear("Iron Helm", "Durable, but tough helmet.", 0);
		    		   Gear armor = new Gear("Chainmail", "Protects from arrows.", 1);
		    		   Gear gloves = new Gear("Iron Gloves", "Durable, but tough gloves", 2);
		    		   Gear shoes = new Gear("Iron Boots", "To stomp the enemy with.", 3);
		    		   Weapon weapon = new Weapon("Iron Sword", "A standard sword.", 4);
		    		   party.getParty().get(1).addItem(potion);
		    		   party.getParty().get(1).addItem(potion);
		    		   party.getParty().get(1).addItem(levelUp);
		    		   party.getParty().get(1).addEquipment(helmet);
		    		   party.getParty().get(1).addEquipment(armor);
		    		   party.getParty().get(1).addEquipment(gloves);
		    		   party.getParty().get(1).addEquipment(shoes);
		    		   party.getParty().get(1).addEquipment(weapon);
		    		   worldFrame.setParty(party);
		    		   System.out.println("worldframe complete");
		    		   worldFrame.updateFrame();
		    	   } else {
		    		   currentState = state.CUSTOMIZATION_STATE;
		    	   }
		    	   
		    	   break;
		       case OVERWORLD_STATE:
		    	  if(worldFrame.showMenu) {
		    		  currentState = state.PARTY_MENU_STATE;
		    		  partyMenu.setParty(party);
			    	  partyMenu.update();	 
		    	  } else {
		    		  currentState = state.OVERWORLD_STATE;
		    	  }
		          /*if(player.isAttacked()) {
		              currentState = state.COMBAT_STATE;
		          } else if (User opens options menu) {
		        	  currentState = state.OPTIONS_STATE;
		          }*/

		          break;
		       case PARTY_MENU_STATE:
		    	   
		    	   if(partyMenu.stopMenu) {
		    		   worldFrame.showMenu = false;
			    	   currentState = state.OVERWORLD_STATE;
		    	   } else {
		    		   currentState = state.PARTY_MENU_STATE;
		    	   }
		    	  
		    	   break;

		    	      
		    }
		    return isRunning;
		}
		// if currentState is null, quit the game
		return false;
	    
	}
}
