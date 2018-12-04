package org.rpg.system;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

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
		    		   Player p = new Player("Warrior");
		    		   Player p2 = new Player("Cleric");
		    		   Player p3 = new Player("Archer");
		    		   Player p4 = new Player("Mage");
		    		   p.setName("TREE");	    			   
	    			   p2.setName("WAH");		    			   
	    			   p3.setName("DRACULA");		    			   
	    			   p4.setName("SEL");
	    			   p.addItem(testItem);
	    			   try {
	    				   p.setAvatar(new ImageIcon(new URL("https://i.ibb.co/2WgZzh4/t-oak.gif")));
	    				   p2.setAvatar(new ImageIcon(new URL("https://i.ibb.co/PW5RkVR/thumbsup-150.jpg")));
	    				   p3.setAvatar(new ImageIcon(new URL("https://i.ibb.co/4SsXgrj/alucard-150.jpg")));
	    				   p4.setAvatar(new ImageIcon(new URL("https://i.ibb.co/093ZHMy/lain-150.png")));
	    			   } catch (MalformedURLException e) {
	    				   e.printStackTrace();
					   }
		    		   party.addPartyMember(1, p); 
		    		   party.addPartyMember(2, p2); 
		    		   party.addPartyMember(3, p3); 
		    		   party.addPartyMember(4, p4);
		    		   
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
				   Skill attack = new Skill("Melee Attack", "Basic melee attack", 0, 0, 1);
		    		   party.getParty().get(1).addItem(potion);
		    		   party.getParty().get(1).addItem(potion);
		    		   party.getParty().get(1).addItem(levelUp);
		    		   party.getParty().get(1).addEquipment(helmet);
		    		   party.getParty().get(1).addEquipment(armor);
		    		   party.getParty().get(1).addEquipment(gloves);
		    		   party.getParty().get(1).addEquipment(shoes);
		    		   party.getParty().get(1).addEquipment(weapon);
				   party.getParty().get(1).addSkill(attack);
		    		   worldFrame.setParty(party);
		    		   System.out.println("worldframe complete");
		    		   worldFrame.updateFrame();
		    	   } else {
		    		   currentState = state.CUSTOMIZATION_STATE;
		    	   }
		    	   
		    	   break;
		       case OVERWORLD_STATE:
		    	  // set sprites for each party member, only displayed in Combat
		    	  for(int i = 0; i < party.getParty().size(); i++) {
		    		  String spriteUrl = "https://i.ibb.co/2gHDX0p/alucard.png";
		    		  if((i + 1) == 2) {
		    			  spriteUrl = "https://i.ibb.co/hHK4nDB/oak-mancer.png";
		    		  } else if((i + 1) == 3) { // p2
		    			  spriteUrl = "https://i.ibb.co/1Yc1L1Y/wario.png";
		    		  } else if((i + 1) == 4) { // p3
		    			  spriteUrl = "https://i.ibb.co/hdzBgVj/alucard-2.png";
		    		  } else if((i + 1) == 5) { // p4
		    			  spriteUrl = "https://i.ibb.co/LrBn4Vx/lain-bear.png";
		    		  }
		    		  party.getParty().get(i + 1).setSprite(spriteUrl);
		    	  }

		    	  if(worldFrame.showMenu) {
		    		  currentState = state.PARTY_MENU_STATE;
		    		  partyMenu.setParty(party);
			    	  partyMenu.update();	 
		    	  } else {
		    		  worldFrame.setParty(party);
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
