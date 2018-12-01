package org.rpg.system;

import java.awt.event.KeyEvent;

public enum Dir {
	
	// change key input here (default: WASD)
    LEFT("Left", KeyEvent.VK_A, -1, 0),
    RIGHT("Right", KeyEvent.VK_D, 1, 0),
    UP("Up", KeyEvent.VK_W, 0, -1),
    DOWN("Down", KeyEvent.VK_S, 0, 1);
							
    private String name; // "UP"
    private int keyCode; // KeyEvent.VK_W
    private int deltaX; // 0
    private int deltaY; // -1
    
    private Dir(String name, int keyCode, int deltaX, int deltaY) {
       this.name = name;
       this.keyCode = keyCode;
       this.deltaX = deltaX;
       this.deltaY = deltaY;
    }
    
    public String getName() { return name; }
    
    public int getKeyCode() { return keyCode; }
    
    public int getDeltaX() { return deltaX; }
    public int getDeltaY() { return deltaY; }      
 }
