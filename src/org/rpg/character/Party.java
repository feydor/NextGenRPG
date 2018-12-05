package org.rpg.character;

import java.awt.geom.Point2D;
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
	private int currentPlayerTurn;
	private int partyTurns; // how many turns a party has made, after each party turn, the enemies move
	
	public Party() {
		party = new TreeMap<Integer, Player>();
		inCombat = false;
		fleeChance = 30;
		enemies = new ArrayList<>();
		numPlayers = 4;
		PartyXpos = 1;
		PartyYpos = 1;
		currentPlayerTurn = 1;
		partyTurns = 0;
	}
	
	public int getPartyTurns() {
		return partyTurns;
	}
	
	public void incrementPartyTurns() {
		partyTurns++;
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
	
	public int getCurrentPlayerTurn() {
		return currentPlayerTurn;
	}
	
	// makes sure valid player turns are 1 - 5 (5 is the enemy ai)
	public void nextPlayerTurn() { // FIXME:
		if((currentPlayerTurn + 1) <= numPlayers + 1 ) { 
			currentPlayerTurn++;
		} else {
			currentPlayerTurn = 1;
		}
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
	
	// Uses Point2D to find the distance between every enemy on the party enemyList and the specified player
	// returns the closest one (its index)
	public int getClosestEnemy(PlayableCharacter player) {
		int smallestDistance = 1000; // high number to make sure that first enemy picked will be set to smallestDistance
		int closestEnemyIndex = 0;
		
		for(int i = 0; i < enemies.size(); i++) {
			int sampleDistance = (int) Point2D.distance(player.getXposBlock(), player.getYposBlock(),
					enemies.get(i).getXposBlock(), enemies.get(i).getYposBlock());
			// checks to see if it found a new smallestDistance,
			// if so save it and the index of the enemy
			if(sampleDistance < smallestDistance) {
				smallestDistance = sampleDistance;
				closestEnemyIndex = i;
			}
		}

		return closestEnemyIndex;
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

	public void removeEnemy(NPC npc) {
		enemies.remove(npc);
	}

	public void removePlayer(Integer playerIndex) {
		party.remove(playerIndex);
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
