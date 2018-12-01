package org.rpg.item;

import org.rpg.character.PlayableCharacter;

public abstract class EquipableItem extends Item {

	//fields
	private int slot;
	private String forClass;
	protected boolean canEquip;
	
	//default constructor
	public EquipableItem() {
		this.slot = 0;
		this.forClass = "";
		this.canEquip = false;
	}
	
	//setters and getters
	public int getSlot() {
		return this.slot; 
	}
	
	public void setSlot(int newSlot) {
		this.slot = newSlot;
	}
	
	public String getForWhatClass() {
		return this.forClass;
	}
	
	public void setForWhatClass(String forThisClass) {
		this.forClass = forThisClass;
	}
	
	//abstract methods
	public void canEquip(PlayableCharacter char1) {
		if (char1.getKit() == "Warrior") {
			if (this.getForWhatClass() == "Warrior") {
				this.canEquip = true;
			}
			this.canEquip = false;
		}
		else if (char1.getKit() == "Archer") {
			if (this.getForWhatClass() == "Archer") {
				this.canEquip = true;
			}
			this.canEquip = false;
		}
		else if (char1.getKit() == "Wizard") {
			if (this.getForWhatClass() == "Wizard") {
				this.canEquip = true;
			}
			this.canEquip = false;
		}
		else if (char1.getKit() == "Cleric") {
			if (this.getForWhatClass() == "Cleric") {
				this.canEquip = true;
			}
			this.canEquip = false;
		}
		//any other case
		else {
			this.canEquip = false;
		}
	}
	
}
