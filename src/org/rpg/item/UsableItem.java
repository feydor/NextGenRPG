package org.rpg.item;

public class UsableItem extends Item {
	public UsableItem() {
		super();
	}
	
	public UsableItem(String name, String description) {
		super();
		setName(name);
		setDescription(description);
	}
	
	public UsableItem(String name) {
		super();
		setName(name);
	}
}
