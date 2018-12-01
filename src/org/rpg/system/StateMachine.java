package org.rpg.system;

import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.rpg.character.Party;
import org.rpg.character.Player;
import org.rpg.map.Tile;
import org.rpg.menu.CustomizationMenu;
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
	public boolean Update(Title titleScreen, Tile worldFrame, CustomizationMenu customizationMenu, Party party) {
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
		    		   party.setNumPlayers(4);
		    		   for(int i = 0; i < party.getNumPlayers(); i++) {
		    			   Player p = new Player("Warrior");
		    			   p.setName("DEBUG");
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
		    	   System.out.println("In PARTY_MENU_STATE");
		    	   worldFrame.showMenu = false;
		    	   currentState = state.OVERWORLD_STATE;
		    	   break;

		    	      
		    }
		    return isRunning;
		}
		// if currentState is null, quit the game
		return false;
	    
	}
}
