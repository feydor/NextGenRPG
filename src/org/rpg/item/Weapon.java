package org.rpg.item;

public class Weapon extends EquipableItem{
	
	//fields
	private int atkVal;
	
	//constructor
	public Weapon() {
		this.atkVal = 0;
	}
	
	public Weapon(String name, String description, int slot) {
		setName(name);
		setSlot(slot);
		setDescription(description);
	}
	
	//setters and getters
	public int getAtk() {
		return this.atkVal;
	}
	
	public void setAtk(int newAtk) {
		this.atkVal = newAtk;
	}
}