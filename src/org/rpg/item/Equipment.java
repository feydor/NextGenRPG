package org.rpg.item;

import java.util.*;

public class Equipment {
	
	//set two array lists to hold collection of gear and weapons
	private ArrayList<Gear> gearList;
	private ArrayList<Weapon> weaponList;
	
	//constructor
	public Equipment() {
		//construct the arrayLists
		this.gearList = new ArrayList<Gear>();
		this.weaponList = new ArrayList<Weapon>();
		
		//Create a collection of armor pieces
		//Hats
		Gear startWarHead = new Gear();
			//set it up
			startWarHead.setName("Basic Helm");
			startWarHead.setSlot(1);
			startWarHead.setDescription("Standard issue helm.");
			startWarHead.setDef(5);
			startWarHead.setRes(5);
			startWarHead.setSpd(0);
			startWarHead.setForWhatClass("Warrior");
			gearList.add(startWarHead);
		Gear startArcHead = new Gear();
			//set it up
			startArcHead.setName("Basic Cap");
			startArcHead.setSlot(1);
			startArcHead.setDescription("A basic archer's cap.");
			startArcHead.setDef(3);
			startArcHead.setRes(3);
			startArcHead.setSpd(0);
			startArcHead.setForWhatClass("Archer");
			gearList.add(startArcHead);
		Gear startWizHead = new Gear();
			//set it up
			startWizHead.setName("Pointy Cap");
			startWizHead.setSlot(1);
			startWizHead.setDescription("A wizard's first hat.");
			startWizHead.setDef(1);
			startWizHead.setRes(1);
			startWizHead.setSpd(0);
			startWizHead.setForWhatClass("Wizard");
			gearList.add(startWizHead);
		Gear startCleHead = new Gear();
			//set it up
			startCleHead.setName("Cleric Cap");
			startCleHead.setSlot(1);
			startCleHead.setDescription("A simple cleric hat.");
			startCleHead.setDef(2);
			startCleHead.setRes(2);
			startCleHead.setSpd(0);
			startCleHead.setForWhatClass("Cleric");
			gearList.add(startCleHead);	
		//Clothes - Top
		Gear startWarChest = new Gear();
			//set it up
			startWarChest.setName("Chainmail Vest");
			startWarChest.setSlot(2);
			startWarChest.setDescription("Standard issue armor.");
			startWarChest.setDef(5);
			startWarChest.setRes(5);
			startWarChest.setSpd(0);
			startWarChest.setForWhatClass("Warrior");
			gearList.add(startWarChest);
		Gear startArcChest = new Gear();
			//set it up
			startArcChest.setName("Simple Tunic");
			startArcChest.setSlot(2);
			startArcChest.setDescription("Comfortable cloth tunic.");
			startArcChest.setDef(3);
			startArcChest.setRes(3);
			startArcChest.setSpd(0);
			startArcChest.setForWhatClass("Archer");
			gearList.add(startArcChest);
		Gear startWizChest = new Gear();
			//set it up
			startWizChest.setName("Apprentic Robe");
			startWizChest.setSlot(2);
			startWizChest.setDescription("A basic robe for a basic mage.");
			startWizChest.setDef(2);
			startWizChest.setRes(2);
			startWizChest.setSpd(0);
			startWizChest.setForWhatClass("Wizard");
			gearList.add(startWizChest);
		Gear startCleChest = new Gear();
			//set it up
			startCleChest.setName("Acolyte Robe");
			startCleChest.setSlot(2);
			startCleChest.setDescription("A simple robe worn by clergy.");
			startCleChest.setDef(2);
			startCleChest.setRes(2);
			startCleChest.setSpd(0);
			startCleChest.setForWhatClass("Cleric");
			gearList.add(startCleChest);
		//Clothes - Bottom	
		Gear startWarLegs = new Gear();
			//set it up
			startWarLegs.setName("Chainmail Pants");
			startWarLegs.setSlot(3);
			startWarLegs.setDescription("Standard issue pants.");
			startWarLegs.setDef(5);
			startWarLegs.setRes(5);
			startWarLegs.setSpd(0);
			startWarLegs.setForWhatClass("Warrior");
			gearList.add(startWarLegs);
		Gear startArcLegs = new Gear();
			//set it up
			startArcLegs.setName("Cloth Leggings");
			startArcLegs.setSlot(3);
			startArcLegs.setDescription("Comfortable cloth pants.");
			startArcLegs.setDef(2);
			startArcLegs.setRes(2);
			startArcLegs.setSpd(0);
			startArcLegs.setForWhatClass("Archer");
			gearList.add(startArcLegs);
		Gear startWizLegs = new Gear();
			//set it up
			startWizLegs.setName("Apprentice Pantaloons");
			startWizLegs.setSlot(3);
			startWizLegs.setDescription("An apprentice's first pants.");
			startWizLegs.setDef(1);
			startWizLegs.setRes(1);
			startWizLegs.setSpd(0);
			startWizLegs.setForWhatClass("Wizard");
			gearList.add(startWizLegs);	
		Gear startCleLegs = new Gear();
			//set it up
			startCleLegs.setName("Cleric Leggings");
			startCleLegs.setSlot(3);
			startCleLegs.setDescription("Standard clergy leggings");
			startCleLegs.setDef(1);
			startCleLegs.setRes(1);
			startCleLegs.setSpd(0);
			startCleLegs.setForWhatClass("Cleric");
			gearList.add(startCleLegs);
		//Gloves
		Gear startWarHands = new Gear();
			//set it up
			startWarHands.setName("Chainmail Gloves");
			startWarHands.setSlot(4);
			startWarHands.setDescription("Standard issue gloves.");
			startWarHands.setDef(3);
			startWarHands.setRes(3);
			startWarHands.setSpd(0);
			startWarHands.setForWhatClass("Warrior");
			gearList.add(startWarHands);
		Gear startArcHands = new Gear();
			//set it up
			startArcHands.setName("Fletcher Gloves");
			startArcHands.setSlot(4);
			startArcHands.setDescription("Simple arhcer's gloves.");
			startArcHands.setDef(2);
			startArcHands.setRes(2);
			startArcHands.setSpd(0);
			startArcHands.setForWhatClass("Archer");
			gearList.add(startArcHands);
		Gear startWizHands = new Gear();
			//set it up
			startWizHands.setName("Apprentice Gloves");
			startWizHands.setSlot(4);
			startWizHands.setDescription("Fledgling mage's gloves.");
			startWizHands.setDef(1);
			startWizHands.setRes(1);
			startWizHands.setSpd(0);
			startWizHands.setForWhatClass("Wizard");
			gearList.add(startWizHands);
		Gear startCleHands = new Gear();
			//set it up
			startCleHands.setName("Clergy Gloves");
			startCleHands.setSlot(4);
			startCleHands.setDescription("Simple cleric's gloves.");
			startCleHands.setDef(1);
			startCleHands.setRes(1);
			startCleHands.setSpd(0);
			startCleHands.setForWhatClass("Cleric");
			gearList.add(startCleHands);
		//Shoes
		Gear startWarFeet = new Gear();
			//set it up
			startWarFeet.setName("Chainmail Boots");
			startWarFeet.setSlot(5);
			startWarFeet.setDescription("Standard issue boots.");
			startWarFeet.setDef(3);
			startWarFeet.setRes(3);
			startWarFeet.setSpd(0);
			startWarFeet.setForWhatClass("Warrior");
			gearList.add(startWarFeet);
		Gear startArcFeet = new Gear();
			//set it up
			startArcFeet.setName("Leather Boots");
			startArcFeet.setSlot(5);
			startArcFeet.setDescription("Snazzy boots for an archer.");
			startArcFeet.setDef(2);
			startArcFeet.setRes(2);
			startArcFeet.setSpd(1);
			startArcFeet.setForWhatClass("Archer");
			gearList.add(startArcFeet);
		Gear startWizFeet = new Gear();
			//set it up
			startWizFeet.setName("Apprentice Shoes");
			startWizFeet.setSlot(5);
			startWizFeet.setDescription("Apprentice's first shoes.");
			startWizFeet.setDef(1);
			startWizFeet.setRes(1);
			startWizFeet.setSpd(0);
			startWizFeet.setForWhatClass("Wizard");
			gearList.add(startWizFeet);
		Gear startCleFeet = new Gear();
			//set it up
			startCleFeet.setName("Cleric Slippers");
			startCleFeet.setSlot(5);
			startCleFeet.setDescription("Basic Clergy footwear.");
			startCleFeet.setDef(1);
			startCleFeet.setRes(1);
			startCleFeet.setSpd(0);
			startCleFeet.setForWhatClass("Cleric");
			gearList.add(startCleFeet);
			
		//create a collection of weapons
		Weapon startWarSword = new Weapon();
			startWarSword.setName("Bronze Sword");
			startWarSword.setSlot(6);
			startWarSword.setDescription("A basic melee weapon");
			startWarSword.setAtk(5);
			startWarSword.setForWhatClass("Warrior");
			weaponList.add(startWarSword);
		Weapon startArcBow = new Weapon();
			startArcBow.setName("Oak Bow");
			startArcBow.setSlot(6);
			startArcBow.setDescription("A basic ranged weapon");
			startArcBow.setAtk(8);
			startArcBow.setForWhatClass("Archer");
			weaponList.add(startArcBow);
		Weapon startWizStaff = new Weapon();
			startWizStaff.setName("Apprentice Wand");
			startWizStaff.setSlot(6);
			startWizStaff.setDescription("A basic conjurer's weapon");
			startWizStaff.setAtk(6);
			startWizStaff.setForWhatClass("Wizard");
			weaponList.add(startWizStaff);
		Weapon startCleStaff = new Weapon();
			startCleStaff.setName("Cleric Staff");
			startCleStaff.setSlot(6);
			startCleStaff.setDescription("A basic healer's weapon");
			startCleStaff.setAtk(2);
			startCleStaff.setForWhatClass("Cleric");
			weaponList.add(startCleStaff);	
	}

	//setters and getters
	public ArrayList<Gear> getGearList() {
		return this.gearList;
	}
	
	public void addToGearList(Gear newGear) {
		this.gearList.add(newGear);
	}
	
	public void removeFromGearList(Gear removeGear) {
		this.gearList.remove(removeGear);
	}

	public ArrayList<Weapon> getWeaponList() {
		return this.weaponList;
	}
	
	public void addToWeaponList(Weapon newWeapon) {
		this.weaponList.add(newWeapon);
	}
	
	public void removeFromWeaponList(Weapon removeWeapon) {
		this.weaponList.remove(removeWeapon);
	}
	
}
