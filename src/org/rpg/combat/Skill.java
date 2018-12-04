package org.rpg.combat;

public class Skill{ 
	
	//fields
	private String name;
	private String desc;
	private int cd;
	private int duration;
	private int range;
	
	//constructor
	public Skill() {
		this.name = "";
		this.desc = "";
		this.cd = 0;
		this.duration = 0;
		this.range = 0;
	}
	
	//overloaded constructor
	public Skill(String aName, String aDesc, int aCD, int aDuration, int aRange) {
		this.name = aName;
		this.desc = aDesc;
		this.cd = aCD;
		this.duration = aDuration;
		this.range = aRange;
	}
	
	//setters and getters
	
	public String getDescription() {
		return this.desc;
	}
	
	public void setDescription(String aDesc) {
		this.desc = aDesc;
	}
	
	public int getCooldown() {
		return this.cd;
	}
	
	public void setCooldown(int aCD) {
		this.cd = aCD;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public void setDuration(int aDuration) {
		this.duration = aDuration;
	}
	
	public int getRange() {
		return this.range;
	}
	
	public void setRange(int aRange) {
		this.range = aRange;
	}
	public String getName() {
		return this.name;
	}
	
	public void setName(String aName) {
		this.name = aName;
	}
}
