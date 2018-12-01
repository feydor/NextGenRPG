package org.rpg.menu;

import java.util.Scanner;

public abstract class Input {
	// used to get keyboard input
    private Scanner input;
    
    // used to store keyboard input
	protected String choice;
	
	public Input() {
		input = new Scanner(System.in);
		choice = "";
	}
	
	public String queryUser() {
		choice = input.nextLine();
		return choice;
	}
	
	public String getChoiceAttr() {
		return choice;
	}
	
	
	
	
}
