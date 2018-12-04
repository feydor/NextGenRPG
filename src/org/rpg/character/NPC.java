package org.rpg.character;

import java.util.ArrayList;
import java.util.Random;

import org.rpg.item.Item;

/* Monster kit/class:
	 
*/
public class NPC extends PlayableCharacter{

	private String type;
	private ArrayList<Item> droppableItems;
	private int droppableXP;	// constant for now, 100
	private int droppableMoney;	// constant for now, 100
	private int dropRate;	// constant for now, 25
	private String dialogue;
	
	private String enemySpriteLoc = "https://i.ibb.co/b2J2vHH/skeleton.png"; // default

	
	// make default enemy, class = Warrior
	public NPC() {
		super();
		name = "Skeleton";
		this.type = "Skeleton";
		sprite = createSprite(enemySpriteLoc);
		droppableItems = new ArrayList<Item>();
		droppableXP = 100;
		droppableMoney = 100;
		dropRate = 25;
		dialogue = "**rattles**";
	}
	
	public NPC(String type, String kit) {
		super(kit);
		this.type = type;
		droppableItems = new ArrayList<Item>();
		droppableXP = 100;
		droppableMoney = 100;
		dropRate = 25;
		dialogue = "Ur 2 slow!";
	}
	
	// full constructor, NOTE: level = 1, XP, money = 0
	public NPC(String type, String kit, int HP, int MP, int offense, int defense, int spirit, 
	int resistence, int speed, int luck) {
		super(kit, HP, MP, offense, defense, spirit, resistence, speed, luck);
		this.type = type;
		droppableItems = new ArrayList<Item>();
		droppableXP = 100;
		droppableMoney = 100;
		dropRate = 25;
		dialogue = "Prepare to die!";
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	// Getters and Setters
	//////////////////////////////////////////////
	
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	public int dropXP() { return droppableXP; }
	public void setDroppableXP(int XP) { droppableXP = XP; }
	
	public int dropMoney() { return droppableMoney; }
	public void setDroppabelMoney(int money) { droppableMoney = money; }
	
	public int getDropRate() { return dropRate; }
	public void setDropRate(int rate) { dropRate = rate; }
	
	public String getDialogue() { return dialogue; }
	public void setDialogue(String dialogue) { this.dialogue = dialogue; }
	
	public void addDroppableItem(Item item) { droppableItems.add(item); }
	public void setDroppableItems(ArrayList<Item> items) { droppableItems = items; }
	
	//////////////////////////////////////////////////////////////////////////////////////
	// Methods
	/////////////////////////////////////////////////
	
	// drops items based on if itemRarity is greater than a random number between 1 and 100
	// NOTE: Will need to call based on dropRate of the NPC
	public ArrayList<Item> dropItems() {
		ArrayList<Item> loot = new ArrayList<Item>();
		Random rand = new Random();
		
		for(Item item : droppableItems) {
			int upperLimit = 100;
			int lowerLimit = 1;
			int random = rand.nextInt(upperLimit - lowerLimit) + lowerLimit;
			
			if(item.getRarity() > random) {
				loot.add(item);
			}
		}
		return loot;
	}
	
	public void printInfo() {
		System.out.println(name + " the " + type + " " + kit + " draws near...");
		System.out.println(" '" + dialogue + "' " );
		System.out.println("Name: " + name);
		System.out.println("Type: " + type);
		System.out.println("Class: " + kit);
		System.out.println("Level: " + level);
		System.out.println("Stats:" + "\n"
				+ "HP: " + this.getHP() + "\n"
				+ "MP: " + this.getMP() + "\n"
				+ "OF: " + this.getOffense() + "\n" 
				+ "DEF: " + this.getDefense() + "\n"
				+ "SPI: " + this.getSpirit() + "\n"
				+ "RES: " + this.getResistence() + "\n"
				+ "SPD: " + this.getSpeed() + "\n"
				+ "LUC: " + this.getLuck() + "\n"
				);
	}
	
	// test
	public static void main(String[] args) {
		// default should be skeleton and warrior
		NPC enemy1 = new NPC();	
		enemy1.setName("Postulio");
		
		String enemy2Kit = "Peace Man";
		String enemy2Type = "Hippie";
		NPC enemy2 = new NPC(enemy2Type, enemy2Kit);
		enemy2.setName("Karl");
		
		String enemy3Kit = "Big Guy";
		String enemy3Type = "Ogre";
		NPC enemy3 = new NPC(enemy3Type, enemy3Kit, 100, 50, 10, 10, 10, 10, 10, 10);
		enemy3.setName("Shrek");
		enemy3.setDialogue("Welcome to me swamp, boyo!");
		
		enemy1.printInfo();
		enemy2.printInfo();
		enemy3.printInfo();		
		
	}
	
}
