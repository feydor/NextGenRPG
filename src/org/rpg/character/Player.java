package org.rpg.character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.rpg.combat.Skill;
import org.rpg.item.*;

import java.util.Random; 

public class Player extends PlayableCharacter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<EquipableItem> equipment;
	private ArrayList<Item> inventory;
	private transient GrowthRate growthRate;
	
	// make default player
	public Player() {
		super();
		inventory = new ArrayList<Item>();
		// NOTE: Default kit is Warrior.
		growthRate = new GrowthRate(kit);
	}
	
	// full constructor, NOTE: level = 1, XP, money = 0
	public Player(String kit, int HP, int MP, int offense, int defense, int spirit, 
			int resistence, int speed, int luck) {
		super(kit, HP, MP, offense, defense, spirit, resistence, speed, luck);
		equipment = new ArrayList<EquipableItem>();
		inventory = new ArrayList<Item>();
		growthRate = new GrowthRate(kit);
	}
	
	// kit/class constructor, default stats (2)
	public Player(String kit) {
		super(kit);
		equipment = new ArrayList<EquipableItem>();
		inventory = new ArrayList<Item>();
		growthRate = new GrowthRate(kit);
	}
	
	public ArrayList<EquipableItem> getEquipment() { return equipment; }
	
	public void setEquipment(ArrayList<EquipableItem> equipment) { this.equipment = equipment; }
	
	public ArrayList<Item> getInventory() { return inventory; }
	
	public Map<String, Integer> getGrowthRate() { return growthRate.getGrowthRateByKit(kit); }
	
	public void addItem(Item item) { inventory.add(item); }
	
	public void addXP(int monsterXP) { XP += monsterXP; }
	
	
	// FIXME: Slot can be overwritten right now.
	public void addEquipment(EquipableItem eq) {
		equipment.add(eq.getSlot(), eq);
		// if equipment arraylist already has equipment in index[eq.getSlot()]
		// replace it
		/*if(equipment.get(eq.getSlot()) == null) {
			// arrayList slot is empty, add equipment
			equipment.add(eq.getSlot(), eq);
		} else {
			// arrayList slot is occupied, do not get equipment
			System.out.println("Equipment slot taken by: " + equipment.get(eq.getSlot()));
		}*/
		
	}
	
	// levels up the player
	// NOTE: Only call if player has the required XP.
	// Equation: Stat gain = ((growth rate * old level) - ((stat-2) * 10)) * r/50
	// reads growth rates from a Map<String, Integer> om GrowthRate.java
	public void levelUp() {
		
		// utilizing temp variables for simple printing of stat gains
		int HPIncrease = (growthRate.getGrowthRateByKit(kit).get("HP") * 2^(level/10)/100) 
				+ (this.getHP() - 30);
		// random number between 4 and 10
		HPIncrease *= (double) addRandomVariation(HPIncrease) / 50;
		
		int MPIncrease = (growthRate.getGrowthRateByKit(kit).get("MP") * 2^(level/10)/100)
				+ (this.getMP() - 10);
		MPIncrease *= (double) addRandomVariation(MPIncrease) / 50;
		
		int offenseIncrease = (growthRate.getGrowthRateByKit(kit).get("Offense") * 2^(level/10)/100) 
				+ (this.getOffense() - 2);
		offenseIncrease *= (double) addRandomVariation(offenseIncrease) / 50;
		
		int defenseIncrease = (growthRate.getGrowthRateByKit(kit).get("Defense") * 2^(level/10)/100) 
				+ (this.getDefense() - 2);
		defenseIncrease *= (double) addRandomVariation(defenseIncrease) / 50;
		
		int spiritIncrease = (growthRate.getGrowthRateByKit(kit).get("Spirit") * 2^(level/10)/100) 
				+ (getSpirit() - 2);
		spiritIncrease *= (double) addRandomVariation(spiritIncrease) / 50;
		
		int resistenceIncrease = (growthRate.getGrowthRateByKit(kit).get("Resistence") * 2^(level/10)/100) 
				+ (this.getResistence() - 2);
		resistenceIncrease *= (double) addRandomVariation(resistenceIncrease) / 50;
		
		int speedIncrease = (growthRate.getGrowthRateByKit(kit).get("Speed") * 2^(level/10)/100) 
				+ (this.getSpeed() - 2);
		speedIncrease *= (double) addRandomVariation(speedIncrease) / 50;
		
		int luckIncrease = (growthRate.getGrowthRateByKit(kit).get("Luck") * 2^(level/10)/100) 
				+ (this.getLuck() - 2);
		luckIncrease *= (double) addRandomVariation(luckIncrease) / 50;
		
	    //System.out.print("\033[H\033[2J");  
		//System.out.flush();
		System.out.println("\n");
		System.out.println(this.getName() + " leveled up to level " + (level + 1) + "!");
		System.out.println("Previous stats:" + "\n"
				+ "HP: " + this.getHP() + "\n"
				+ "MP: " + this.getMP() + "\n"
				+ "OF: " + this.getOffense() + "\n" 
				+ "DEF: " + this.getDefense() + "\n"
				+ "SPI: " + this.getSpirit() + "\n"
				+ "RES: " + this.getResistence() + "\n"
				+ "SPD: " + this.getSpeed() + "\n"
				+ "LUC: " + this.getLuck() + "\n"
				);
		System.out.println("Stat increases:" + "\n"
				+ "HP: +" + HPIncrease + "\n"
				+ "MP: +" + MPIncrease + "\n"
				+ "OF: +" + offenseIncrease + "\n" 
				+ "DEF: +" + defenseIncrease + "\n"
				+ "SPI: +" + spiritIncrease + "\n"
				+ "RES: +" + resistenceIncrease + "\n"
				+ "SPD: +" + speedIncrease + "\n"
				+ "LUC: +" + luckIncrease + "\n"
				);
		
		// Finalize stat increases here:
		HP += HPIncrease;
		currentHP = HP;
		MP += MPIncrease;
		currentMP = MP;
		offense += offenseIncrease;
		defense += defenseIncrease;
		spirit += spiritIncrease;
		resistence += resistenceIncrease;
		speed += speedIncrease;
		luck += luckIncrease;
		
		System.out.println("New stats:" + "\n"
				+ "HP: " + this.getHP() + "\n"
				+ "MP: " + this.getMP() + "\n"
				+ "OF: " + this.getOffense() + "\n" 
				+ "DEF: " + this.getDefense() + "\n"
				+ "SPI: " + this.getSpirit() + "\n"
				+ "RES: " + this.getResistence() + "\n"
				+ "SPD: " + this.getSpeed() + "\n"
				+ "LUC: " + this.getLuck() + "\n"
				);
		
		level++;
	}
	
	// returns an int between 4 and 10 inclusive 
	public int addRandomVariation(int num) {
		Random rand =  new Random();
		return (rand.nextInt(10) + 4);
	}
	
	public void printInventory() {
		System.out.println("---------- " + name.toUpperCase() + " "+ "INVENTORY ----------");
		for(Item item : inventory) {
			System.out.println(item.getName() + ": " + item.getDescription());
		}
		System.out.println("--------------------------------------");
	}
	
	
	// test main
	public static void main(String[] args) {
		// default player test
		Player dPlayer = new Player();
		
		Player magePlayer = new Player("Mage");
		Player warPlayer = new Player("Warrior");
		Player rangePlayer = new Player("Archer");
		Player clericPlayer = new Player("Cleric");
		dPlayer.setName("Hiro");
		magePlayer.setName("WizKid");
		warPlayer.setName("Simon Belmont");
		rangePlayer.setName("Archer_Dan");
		clericPlayer.setName("Tankie");
		
		magePlayer.printStats();
		
		System.out.println(magePlayer.getGrowthRate());
		
		for(int i = 1; i < 99; i++) {
			magePlayer.levelUp();
		}
		
		magePlayer.printStats();

		
	}



	

}
