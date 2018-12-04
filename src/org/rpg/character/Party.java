package org.rpg.character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.rpg.combat.Abilities;

public class Party implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8707915666305316561L;
	
	// key, value pairs
	//  <1, player 1 object> 
	//  <2, player 2 object> etc...
	private Map<Integer, Player> party;
	private int numPlayers;
	private boolean inCombat;
	private int fleeChance;
	private ArrayList<NPC> enemies;
	private int PartyXpos;
	private int PartyYpos;
	
	public Party() {
		party = new TreeMap<Integer, Player>();
		inCombat = false;
		fleeChance = 30;
		enemies = new ArrayList<>();
		numPlayers = 4;
		PartyXpos = 1;
		PartyYpos = 1;
	}
	
	public int getPartyXpos() {
		return PartyXpos;
	}

	public void setPartyXpos(int partyXpos) {
		PartyXpos = partyXpos;
	}

	public void incrementXPOS()
    {
    	PartyXpos++;
    }
    public void decrementXPOS()
    {
    	PartyXpos--;
    }
    public void incrementYPOS()
    {
    	PartyYpos++;
    }
    public void decrementYPOS()
    {
    	PartyYpos--;
    }
	
	public int getPartyYpos() {
		return PartyYpos;
	}

	public void setPartyYpos(int partyYpos) {
		PartyYpos = partyYpos;
	}

	public boolean isInCombat() {
		return inCombat;
	}
	
	public void setInCombat(boolean inCombat) {
		this.inCombat = inCombat;
	}
	
	public int getFleeChance() {
		return fleeChance;
	}

	public void setFleeChance(int fleeChance) {
		this.fleeChance = fleeChance;
	}
	
	public Map<Integer, Player> getParty() { return party; }
	
	public void setParty(Map<Integer, Player> party) { this.party = party; }
	
	public void addPartyMember(int index, Player partyMember) { party.put(index, partyMember); }
	
	public int getNumPlayers() { return numPlayers; }
	
	public void setNumPlayers(int numPlayers) {this.numPlayers = numPlayers; }
	
	public void printPartyMembers() {
		System.out.println(party);
	}
	
	public void printPartyStats() {
		for (Map.Entry<Integer,Player> entry : party.entrySet()) {  
			entry.getValue().printStats();
    	} 
		System.out.println();
	}
	
	public void printPartyInventory() {
		for (Map.Entry<Integer,Player> entry : party.entrySet()) {  
			entry.getValue().printInventory();
    	} 
		System.out.println();
	}
	
	public ArrayList<NPC> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<NPC> enemies) {
		this.enemies = enemies;
	}

	public void addtoEnemies(NPC enemy)
	{
		enemies.add(enemy);
	}
	
	public void clearEnemies()
	{
		enemies.clear();
	}
	
    public void instantiateAbilityList()
    {
    	Abilities AllAbilities = new Abilities(); 
    	
    	for (Entry<Integer, Player> member : this.getParty().entrySet()) //goes thru party
    	{
	    	switch (member.getValue().getKit()) //checks players kit
	    	{
	    		case "Warrior":
	    			member.getValue().getSkills().add(AllAbilities.getSkillList().get(0));
	    		case "Archer":
	    			member.getValue().getSkills().add(AllAbilities.getSkillList().get(1));
	    		case "Mage":
	    			member.getValue().getSpells().add(AllAbilities.getSpellList().get(0));
	    		case "Cleric":
	    			member.getValue().getSpells().add(AllAbilities.getSpellList().get(0));
	    	}
    	}
    }
	
	// upon entering a new game, re-construct party members.
	// neccesary to wipe party data to prevent user from exiting to title and reentering, without saving
	/*public void enterNewGame() {
		long i = 0;
		  // using for-each loop for iteration over Map.entrySet() 
        for (Map.Entry<String,Player> entry : party.entrySet()) {  

            entry.getValue().setXpos(1);
            entry.getValue().setYpos(1);
            
            // TODO: reset other player data
    	} 

		
	}*/
	
	
}
