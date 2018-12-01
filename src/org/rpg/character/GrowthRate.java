package org.rpg.character;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class GrowthRate {
	
	// change stat growth rates here
	private static final int WARRIOR_HP = 40;
	private static final int WARRIOR_MP = 1;
	private static final int WARRIOR_OF = 50;
	private static final int WARRIOR_DEF = 35;
	private static final int WARRIOR_SPI = 2;
	private static final int WARRIOR_RES = 10;
	private static final int WARRIOR_SPD = 20;
	private static final int WARRIOR_LUC = 10;
	
	private static final int RANGED_HP = 30;
	private static final int RANGED_MP = 1;
	private static final int RANGED_OF = 20;
	private static final int RANGED_DEF = 30;
	private static final int RANGED_SPI = 2;
	private static final int RANGED_RES = 10;
	private static final int RANGED_SPD = 45;
	private static final int RANGED_LUC = 35;
	
	private static final int MAGE_HP = 15;
	private static final int MAGE_MP = 35;
	private static final int MAGE_OF = 2;
	private static final int MAGE_DEF = 15;
	private static final int MAGE_SPI = 50;
	private static final int MAGE_RES = 40;
	private static final int MAGE_SPD = 25;
	private static final int MAGE_LUC = 10;
	
	private static final int CLERIC_HP = 25;
	private static final int CLERIC_MP = 30;
	private static final int CLERIC_OF = 15;
	private static final int CLERIC_DEF = 15;
	private static final int CLERIC_SPI = 2;
	private static final int CLERIC_RES = 50;
	private static final int CLERIC_SPD = 25;
	private static final int CLERIC_LUC = 25;
	
	private Map<String, Integer> warriorRate;
	private Map<String, Integer> rangedRate;
	private Map<String, Integer> mageRate;
	private Map<String, Integer> clericRate;
	
	// will hold a Map for each class/kit,
	// each Map will hold the growth rates for that class
	private Map<String, Map<String, Integer>> growthRates;
	
	public GrowthRate() {
		warriorRate = new TreeMap<>();
		rangedRate = new TreeMap<>();
		mageRate = new TreeMap<>();
		clericRate = new TreeMap<>();
		
		growthRates = new TreeMap<>();
		
		setGrowthRates();
	}
	
	public GrowthRate(String kit) {
		warriorRate = new TreeMap<>();
		rangedRate = new TreeMap<>();
		mageRate = new TreeMap<>();
		clericRate = new TreeMap<>();
		
		growthRates = new TreeMap<>();
		
		setGrowthRates(kit);
	}
	
	/*public Map<String, Map<String, ArrayList<Integer>>> getGrowthTable() {
		return growthRates;
	}*/
	
	// if this does not work, it means that growthRate Map does not have a Map that matches
	// the kit passed in
	public Map<String, Integer> getGrowthRateByKit(String kit) {
			return growthRates.get(kit);
	}
	
	// sets by Kit/Class
	public void setGrowthRates(String kit) {
		switch(kit) {
		case "Warrior":
			warriorRate.put("HP", WARRIOR_HP); // HP
			warriorRate.put("MP", WARRIOR_MP);  // MP
			warriorRate.put("Offense", WARRIOR_OF); // Offense
			warriorRate.put("Defense", WARRIOR_DEF); // Defense
			warriorRate.put("Spirit", WARRIOR_SPI);  // Spirit
			warriorRate.put("Resistence", WARRIOR_RES); // Resistence
			warriorRate.put("Speed", WARRIOR_SPD); // Speed
			warriorRate.put("Luck", WARRIOR_LUC); // Luck
			growthRates.put("Warrior", warriorRate);
			break;
		
		case "Archer":
			rangedRate.put("HP", RANGED_HP);
			rangedRate.put("MP", RANGED_MP);
			rangedRate.put("Offense", RANGED_OF);
			rangedRate.put("Defense", RANGED_DEF);
			rangedRate.put("Spirit", RANGED_SPI);
			rangedRate.put("Resistence", RANGED_RES);
			rangedRate.put("Speed", RANGED_SPD);
			rangedRate.put("Luck", RANGED_LUC);
			growthRates.put("Archer", rangedRate);
			break;
			
		case "Mage":
			mageRate.put("HP", MAGE_HP);
			mageRate.put("MP", MAGE_MP);
			mageRate.put("Offense", MAGE_OF);
			mageRate.put("Defense", MAGE_DEF);
			mageRate.put("Spirit", MAGE_SPI);
			mageRate.put("Resistence", MAGE_RES);
			mageRate.put("Speed", MAGE_SPD);
			mageRate.put("Luck", MAGE_LUC);
			growthRates.put("Mage", mageRate);
			break;
		
		case "Cleric":
			clericRate.put("HP", CLERIC_HP);
			clericRate.put("MP", CLERIC_MP);
			clericRate.put("Offense", CLERIC_OF);
			clericRate.put("Defense", CLERIC_DEF);
			clericRate.put("Spirit", CLERIC_SPI);
			clericRate.put("Resistence", CLERIC_RES);
			clericRate.put("Speed", CLERIC_SPD);
			clericRate.put("Luck", CLERIC_LUC);
			growthRates.put("Cleric", clericRate);
			break;
		default:
			System.out.println("Class " + kit + " does not exist. Available Classes are:");
			System.out.println("Warrior, Ranged, Mage, Cleric");
		}
	}
	
	public void setGrowthRates() {
		// HP, MP, Offense, Defense, Spirit, Resistence, Speed, Luck 
		// based on scale of 1 - 50
		// TODO: Speed may simply grow by item and not stat TBD
		warriorRate.put("HP", WARRIOR_HP); // HP
		warriorRate.put("MP", WARRIOR_MP);  // MP
		warriorRate.put("Offense", WARRIOR_OF); // Offense
		warriorRate.put("Defense", WARRIOR_DEF); // Defense
		warriorRate.put("Spirit", WARRIOR_SPI);  // Spirit
		warriorRate.put("Resistence", WARRIOR_RES); // Resistence
		warriorRate.put("Speed", WARRIOR_SPD); // Speed
		warriorRate.put("Luck", WARRIOR_LUC); // Luck
		
		rangedRate.put("HP", RANGED_HP);
		rangedRate.put("MP", RANGED_MP);
		rangedRate.put("Offense", RANGED_OF);
		rangedRate.put("Defense", RANGED_DEF);
		rangedRate.put("Spirit", RANGED_SPI);
		rangedRate.put("Resistence", RANGED_RES);
		rangedRate.put("Speed", RANGED_SPD);
		rangedRate.put("Luck", RANGED_LUC);
		
		mageRate.put("HP", MAGE_HP);
		mageRate.put("MP", MAGE_MP);
		mageRate.put("Offense", MAGE_OF);
		mageRate.put("Defense", MAGE_DEF);
		mageRate.put("Spirit", MAGE_SPI);
		mageRate.put("Resistence", MAGE_RES);
		mageRate.put("Speed", MAGE_SPD);
		mageRate.put("Luck", MAGE_LUC);
		
		clericRate.put("HP", CLERIC_HP);
		clericRate.put("MP", CLERIC_MP);
		clericRate.put("Offense", CLERIC_OF);
		clericRate.put("Defense", CLERIC_DEF);
		clericRate.put("Spirit", CLERIC_SPI);
		clericRate.put("Resistence", CLERIC_RES);
		clericRate.put("Speed", CLERIC_SPD);
		clericRate.put("Luck", CLERIC_LUC);
		
		growthRates.put("Warrior", warriorRate);
		growthRates.put("Archer", rangedRate);
		growthRates.put("Mage", mageRate);
		growthRates.put("Cleric", clericRate);
	}
	
	// test main
	public static void main(String[] args) {
		GrowthRate rates = new GrowthRate();
		
		System.out.println("Growth Rates for warrior:");
		System.out.println("HP/MP/Offense/Defense/Spirit/Resistence/Speed/Luck");
		System.out.println(rates.getGrowthRateByKit("Warrior"));
		
		System.out.println("Growth Rates for Archer:");
		System.out.println("HP/MP/Offense/Defense/Spirit/Resistence/Speed/Luck");
		System.out.println(rates.getGrowthRateByKit("Archer"));
	}
}
	
