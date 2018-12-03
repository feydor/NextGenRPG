package org.rpg.item;

public abstract class Item {
	
	protected String name;
	protected String description;
	protected int rarity;
	
	public Item() {
		name = "test";
		description = "a test item";
		rarity = 0;
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public int getRarity() { return rarity; }
	public void setRarity(int rarity) { this.rarity = rarity; }
}
