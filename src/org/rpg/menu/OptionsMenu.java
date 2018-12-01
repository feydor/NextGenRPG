package org.rpg.menu;

public class OptionsMenu extends Input{
	
	public void draw() {
		// NOTE: This only works on non-Eclipse terminal console.
		System.out.print("\033[H\033[2J");  
		System.out.flush();
		
		System.out.print("                                                                                                                \n" + 
				"                      +------------------------------------------------------------------+                      \n" + 
				"                      |                                                                  |                      \n" + 
				"                      |  +-------------------+        +-----------------------------+    |                      \n" + 
				"                      |  | Party Stats(p)    |        |           Legend            |    |                      \n" + 
				"                      |  +-------------------+        |    ---------------------    |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |  +-------------------+        |    P: Player                |    |                      \n" + 
				"                      |  | Inventory(i)      |        |                             |    |                      \n" + 
				"                      |  +-------------------+        |    T: Treasure              |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |  +-------------------+        |    #: Wall                  |    |                      \n" + 
				"                      |  | Equipment(e)      |        |                             |    |                      \n" + 
				"                      |  +-------------------+        |    N: NPC                   |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |  +-------------------+        |    #: Terrain               |    |                      \n" + 
				"                      |  | Save(s)           |        |                             |    |                      \n" + 
				"                      |  +-------------------+        |                             |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |  +-------------------+        |                             |    |                      \n" + 
				"                      |  | Quit Game(Q)      |        |                             |    |                      \n" + 
				"                      |  +-------------------+        |                             |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |                               |                             |    |                      \n" + 
				"                      |  Use q to exit the menu.      +-----------------------------+    |                      \n" + 
				"                      |                                                                  |                      \n" + 
				"                      +------------------------------------------------------------------+                      \n" + 
				"                                                                                                                \n" + ">>");
	}
	
	// returns the option selected, the string will contain "null" if invalid option
	public String getUserChoice() {
		//validateOptionsInput(this.queryUser());
		this.queryUser();
		
		String optionSelected = "";
		
		switch(choice) {
			case "p":
			case "P":
			case "party":
			case "Party":
			case "stats":
				optionSelected = "party";
				break;
			case "i":
			case "I":
			case "inventory":
			case "Inventory":
			case "items":
				optionSelected = "inventory";
				break;
			case "e":
			case "E":
			case "equipment":
			case "Equipment":
			case "equip":
				optionSelected = "equipment";
				break;
			case "s":
			case "S":
			case "save":
			case "Save":
				optionSelected = "save";
				break;
			case "Q":
			case "quit":
			case "Quit":
				optionSelected = "quitGame";
				break;
			case "q":
			case "exit":
			case ".":
				optionSelected = "exit";
				break;
			default:
				optionSelected = "null";
				break;
		}
		
		return optionSelected;
	}

	
}
