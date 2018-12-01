package org.rpg.item;

public abstract class Item {
	
	private String name;
	private String description;
	private int rarity;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public int getRarity() { return rarity; }
	public void setRarity(int rarity) { this.rarity = rarity; }
}
