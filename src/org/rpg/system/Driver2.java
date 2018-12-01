package org.rpg.system;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
		

		boolean isRunning = true;
		while(isRunning) {
			stateMachine.Update(titleScreen, worldFrame, customizationMenu, party);
		        //render();
		     Thread.sleep(10); //the timing mechanism
		}


//        SwingUtilities.invokeLater(new Runnable() {
//        	
//        	public void run() {
//            	//worldFrame.updateFrame();
//            	stateMachine.Update();
//            }
//         });
		
	}
}