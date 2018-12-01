package org.rpg.combat;

import java.util.*;

public class Abilities {
	protected String name;
	//set two array lists to hold collection of gear and weapons
	private ArrayList<Skill> skillList;
	private ArrayList<Spell> spellList;
	
	//constructor
	public Abilities() {
		this.skillList = new ArrayList<Skill>();
		this.spellList = new ArrayList<Spell>();
		
		//create a collection of abilities
		//start creating some skills
		Skill basicMelee = new Skill(); //0
			//set up the fields
			basicMelee.setName("Basic Melee Attack");
			basicMelee.setDescription("Attack the target enemy.");
			basicMelee.setCooldown(0);
			basicMelee.setDuration(0);
			basicMelee.setRange(1);
			skillList.add(basicMelee);
		Skill basicRanged = new Skill(); //1
			//set up the fields
			basicRanged.setName("Basic Ranged Attack");
			basicRanged.setDescription("Attack the target enemy.");
			basicRanged.setCooldown(0);
			basicRanged.setDuration(0);
			basicRanged.setRange(5);
			skillList.add(basicRanged);
//		Skill taunt = new Skill(); //2
//			//set up the fields
//			taunt.setName("Taunt");
//			taunt.setDescription("Force a target enemy to attack the Warrior on their next turn.");
//			taunt.setCooldown(0);
//			taunt.setDuration(1);
//			taunt.setRange(6);
//			skillList.add(taunt);
//		Skill resolve = new Skill(); //3
//			//set up the fields
//			resolve.setName("Steel Resolve");
//			resolve.setDescription("reduce damage taken by 40% for 3 turns.");
//			resolve.setCooldown(4);
//			resolve.setDuration(3);
//			resolve.setRange(0);
//			skillList.add(resolve);
//		Skill bloodBath = new Skill(); //4
//			//set up the fields
//			bloodBath.setName("Bloodbath");
//			bloodBath.setDescription("heal for 20% of all damage dealt for 3 turns.");
//			bloodBath.setCooldown(5);
//			bloodBath.setDuration(3);
//			bloodBath.setRange(0);
//			skillList.add(bloodBath);
//		Skill thorn = new Skill(); //5
//			//set up the fields
//			thorn.setName("Thornmail");
//			thorn.setDescription("Return 30% of physical damage taken to the attacker for 3 turns.");
//			thorn.setCooldown(4);
//			thorn.setDuration(3);
//			thorn.setRange(0);
//			skillList.add(thorn);
//		Skill shout = new Skill(); //6
//			//set up the fields
//			shout.setName("War Cry");
//			shout.setDescription("Raise the party's damage dealt by 15% for 3 turns.");
//			shout.setCooldown(5);
//			shout.setDuration(3);
//			shout.setRange(0);
//			skillList.add(shout);			
//		Skill bite = new Skill(); //7
//			//set up the fields
//			bite.setName("Serpent Bite");
//			bite.setDescription("Poison the target enemy, making them take damage for 5 turns.");
//			bite.setCooldown(0);
//			bite.setDuration(5);
//			bite.setRange(5);
//			skillList.add(bite);
//		Skill trueshot = new Skill(); //8
//			//set up the fields
//			trueshot.setName("Trueshot");
//			trueshot.setDescription("Land a guaranteed critical shot.");
//			trueshot.setCooldown(5);
//			trueshot.setDuration(0);
//			trueshot.setRange(5);
//			skillList.add(trueshot);
//		Skill trap = new Skill(); //9
//			//set up the fields
//			trap.setName("Fire Trap");
//			trap.setDescription("Set a trap on a tile that erupts when stepped on and leaves a patch of fire for 5 turns.");
//			trap.setCooldown(1);
//			trap.setDuration(5);
//			trap.setRange(7);
//			skillList.add(trap);
//		Skill explosive = new Skill(); //10
//			//set up the fields
//			explosive.setName("Explosive Shot");
//			explosive.setDescription("Fire a blast in a cone, hitting all enemies within.");
//			explosive.setCooldown(0);
//			explosive.setDuration(0);
//			explosive.setRange(2);
//			skillList.add(explosive);
//		Skill disarm = new Skill(); //11
//			//set up the fields
//			disarm.setName("Disarm");
//			disarm.setDescription("Separate the enemy with their weapon to prevent an attack on their next turn.");
//			disarm.setCooldown(1);
//			disarm.setDuration(0);
//			disarm.setRange(5);
//			skillList.add(disarm);
			
		//start creating some spells	
		Spell basic = new Spell(); //0
			//set up the fields
			basic.setName("Basic Attack");
			basic.setDescription("Attack the target enemy.");
			basic.setMPCost(0);
			basic.setDuration(0);
			basic.setRange(5);
			spellList.add(basic);
//		Spell fireball = new Spell(); //1
//			//set up the fields
//			fireball.setName("Fireball");
//			fireball.setDescription("Launche a ball of fire that explodes and hits nearby enemies.");
//			fireball.setMPCost(8);
//			fireball.setDuration(0);
//			fireball.setRange(5);
//			spellList.add(fireball);
//		Spell bolt = new Spell(); //2
//			//set up the fields
//			bolt.setName("Bolt");
//			bolt.setDescription("Summon a bolt of lightning to hit a faraway enemy.");
//			bolt.setMPCost(10);
//			bolt.setDuration(0);
//			bolt.setRange(10);
//			spellList.add(bolt);
//		Spell snap = new Spell(); //3
//			//set up the fields
//			snap.setName("Cold Snap");
//			snap.setDescription("Snare a target enemy in place for one turn.");
//			snap.setMPCost(8);
//			snap.setDuration(1);
//			snap.setRange(5);
//			spellList.add(snap);
//		Spell scathe = new Spell(); //4
//			//set up the fields
//			scathe.setName("Scathe");
//			scathe.setDescription("Fire a blast of concentrated energy in a line.");
//			scathe.setMPCost(20);
//			scathe.setDuration(0);
//			scathe.setRange(5);
//			spellList.add(scathe);
//		Spell cure = new Spell(); //5
//			//set up the fields
//			cure.setName("Heal");
//			cure.setDescription("Restore a friendly target's health.");
//			cure.setMPCost(6);
//			cure.setDuration(0);
//			cure.setRange(5);
//			spellList.add(cure);
//		Spell restore = new Spell(); //6
//			//set up the fields
//			restore.setName("Restore");
//			restore.setDescription("Remove a negative effect from a friendly target.");
//			restore.setMPCost(5);
//			restore.setDuration(0);
//			restore.setRange(5);
//			spellList.add(restore);
//		Spell medica = new Spell(); //7
//			//set up the fields
//			medica.setName("Medica");
//			medica.setDescription("Restore health to allies in target area.");
//			medica.setMPCost(10);
//			medica.setDuration(0);
//			medica.setRange(5);
//			spellList.add(medica);
//		Spell rez = new Spell(); //8
//			//set up the fields
//			rez.setName("Ressurect");
//			rez.setDescription("Return a fallen ally to life.");
//			rez.setMPCost(25);
//			rez.setDuration(0);
//			rez.setRange(5);
//			spellList.add(rez);
//		Spell sanctify = new Spell(); //9
//			//set up the fields
//			sanctify.setName("Sanctify");
//			sanctify.setDescription("Unleash a blast of holy power, damaging enemies nearby.");
//			sanctify.setMPCost(8);
//			sanctify.setDuration(0);
//			sanctify.setRange(3);
//			spellList.add(sanctify);
	}
	
	//setters and getters
	public ArrayList<Skill> getSkillList() {
		return this.skillList;
	}
	
	public void addToSkillList(Skill newSkill) {
		this.skillList.add(newSkill);
	}
	
	public void removeFromSkillList(Skill removeSkill) {
		this.skillList.remove(removeSkill);
	}

	public ArrayList<Spell> getSpellList() {
		return this.spellList;
	}
	
	public void addToSpellList(Spell newSpell) {
		this.spellList.add(newSpell);
	}
	
	public void removeFromSpellList(Spell removeSpell) {
		this.spellList.remove(removeSpell);
	}

}
