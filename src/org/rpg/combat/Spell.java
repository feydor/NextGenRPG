package org.rpg.combat;

public class Spell extends Abilities {

	//fields
		private String name;
		private String desc;
		private int cost;
		private int duration;
		private int range;
		
		//constructor
		public Spell() {
			this.name = "";
			this.desc = "";
			this.cost = 0;
			this.duration = 0;
			this.range = 0;
		}
		
		//overloaded constructor
		public Spell(String aName, String aDesc, int aCost, int aDuration, int aRange) {
			this.name = aName;
			this.desc = aDesc;
			this.cost = aCost;
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
		
		public int getMPCost() {
			return this.cost;
		}
		
		public void setMPCost(int aCost) {
			this.cost = aCost;
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
