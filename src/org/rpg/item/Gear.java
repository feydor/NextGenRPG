package org.rpg.item;

public class Gear extends EquipableItem {

	//fields
	private int defVal;
	private int resVal;
	private int spdVal;
	
	//constructor
	public Gear() {
		defVal = 0;
		resVal = 0;
		spdVal = 0;
	}
	
	public Gear(String name, String description, int slot) {
		setName(name);
		setSlot(slot);
		setDescription(description);
	}
	
	//setters and getters
	public int getDef() {
		return this.defVal;
	}
	
	public void setDef(int newDef) {
		this.defVal = newDef;
	}
	
	public int getRes() {
		return this.resVal;
	}
	
	public void setRes(int newRes) {
		this.resVal = newRes;
	}
	
	public int getSpd() {
		return this.spdVal;
	}
	
	public void setSpd(int newSpd) {
		this.spdVal = newSpd;
	}
	
}
