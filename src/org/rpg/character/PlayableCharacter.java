package org.rpg.character;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.rpg.combat.Abilities;
import org.rpg.combat.Skill;
import org.rpg.combat.Spell;


public abstract class PlayableCharacter{
    
	protected String name; // must set when creating Player
	protected ImageIcon avatar; // must set when creating Player
	protected BufferedImage sprite;
	private String spriteLoc = "https://i.ibb.co/fx8wJzP/hero.png"; // default, use setSprite(url) to change
    protected int level;
    protected int XP;
    protected String kit;
    protected int xpos;
	protected int ypos;
    protected int HP;
    protected int currentHP;
    protected int MP;
    protected int currentMP;
    protected int offense;
    protected int defense;
    protected int spirit;
	protected int resistence;
    protected int speed;
    protected int luck;

    protected int money;
    protected ArrayList<Spell> spells;
    protected ArrayList<Skill> skills;
    protected ArrayList<Abilities> usableAbilities; 
    
    
    ///////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////
    
    public PlayableCharacter() {
    	name = "";
    	xpos = 1;
    	ypos = 1;
    	sprite = createSprite(spriteLoc);
    	level = 1;
    	XP = 0;
    	kit = "Warrior";
    	HP = 30;
    	currentHP = HP;
    	MP = 10;
    	currentMP = MP;
    	offense = 2;
    	defense = 2;
    	spirit = 2;
    	resistence = 2;
    	speed = 2;
    	luck = 2;
    	money = 0;
    	spells = new ArrayList<Spell>();
    	skills = new ArrayList<Skill>();
    	usableAbilities = new ArrayList<Abilities>();
    }
    
    public PlayableCharacter(String kit, int HP, int MP, int offense, int defense, int spirit, 
			int resistence, int speed, int luck) {
    	name = "";
    	xpos = 1;
    	ypos = 1;
    	sprite = createSprite(spriteLoc);
    	level = 1;
    	XP = 0;
    	this.kit = kit;
    	this.HP = HP;
    	currentHP = HP;
    	this.MP = MP;
    	currentMP = MP;
    	this.offense = offense;
    	this.defense = defense;
    	this.spirit = spirit;
    	this.resistence = resistence;
    	this.speed = speed;
    	this.luck = luck;
    	money = 0;
      	spells = new ArrayList<Spell>();
    	skills = new ArrayList<Skill>();
    	usableAbilities = new ArrayList<Abilities>();
    }
    
    public PlayableCharacter(String kit) {
    	name = "";
    	xpos = 1;
    	ypos = 1;
    	sprite = createSprite(spriteLoc);
    	level = 1;
    	XP = 0;
    	this.kit = kit;
    	HP = 30;
    	currentHP = HP;
    	MP = 10;
    	currentMP = MP;
    	offense = 2;
    	defense = 2;
    	spirit = 2;
    	resistence = 2;
    	speed = 2;
    	luck = 2;
    	money = 0;
    	spells = new ArrayList<Spell>();
    	skills = new ArrayList<Skill>();
    	usableAbilities = new ArrayList<Abilities>();
    }

    ////////////////////////////////////////////////////////////
    // Getters and Setters
    ////////////////////////////////
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public void setAvatar(ImageIcon avi) { this.avatar = avi; }
	public ImageIcon getAvatar() { return avatar; }
	
	public int getXpos() { return xpos; }
	public void setXpos(int xpos) { this.xpos = xpos; }
	
	public int getYpos() { return ypos; }
	public void setYpos(int ypos) { this.ypos = ypos; }
    public int getLevel() { return level; } 
    public void setLevel(int level) { this.level = level; }

    public int getXP() { return XP; } 
    public void setXP(int exp) { this.XP = exp; }
    
    public String getKit() { return kit; }
    public void setKit(String kit) { this.kit = kit; }

    public int getHP() { return HP; } 

    public void setHP(int HP) { this.HP = HP; }

    public int getCurrentHP() { return currentHP; } 

    public void setCurrentHP(int currentHP) { this.currentHP = currentHP; }
    
    public int getMP() { return MP; } 

    public void setMP(int MP) { this.MP = MP; }

    public int getCurrentMP() { return currentMP; } 

    public void setCurrentMP(int currentMP) { this.currentMP = currentMP; }

    public int getOffense() { return offense; } 

    public void setOffense(int offense) { this.offense = offense; }

    public int getDefense() { return defense; } 

    public void setDefense(int defense) { this.defense = defense; }

    public int getSpirit() { return spirit; } 

    public void setSpirit(int spirit) { this.spirit = spirit; }

    public int getResistence() { return resistence; } 

    public void setResistence(int resistence) { this.resistence = resistence; }

    public int getSpeed() { return speed; } 

    public void setSpeed(int speed) { this.speed = speed; }

    public int getLuck() { return luck; } 

    public void setLuck(int luck) { this.luck = luck; }

    public int getMoney() { return money; }

    public void setMoney(int money) { this.money = money;  }
    
    public BufferedImage createSprite(String spriteLoc) {
    	  try {
    		  BufferedImage sprt = ImageIO.read(new URL(spriteLoc));
    		  return sprt;
    	  } catch (IOException e) {
    		  e.printStackTrace();
    	  }
        
    	  return sprite; // NOTE: Eclipse gives an error if this return statement is not present
  	  				// If loading of sprite goes well, it should NEVER run
       }
    
    
    //////////////////////////////////////////////////////////
    // Spell methods
    ///////////////////////////////////////////

    public ArrayList<Skill> getSkills() {
		return skills;
	}

	public void setSkills(ArrayList<Skill> skills) {
		this.skills = skills;
	}

	public ArrayList<Abilities> getUsableAbilities() {
		return usableAbilities;
	}

	public void setUsableAbilities(ArrayList<Abilities> usableAbilities) {
		this.usableAbilities = usableAbilities;
	}

	public ArrayList<Spell> getSpells() { return spells; }

    public void setSpells(ArrayList<Spell> spells) { this.spells = spells; }

    public void addSpell(Spell spell) { spells.add(spell); }

    public void removeSpell(Spell spell) { spells.remove(spell); }
    

    ///////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////

    
    public void incrementXPOS()
    {
    	xpos++;
    }
    public void decrementXPOS()
    {
    	xpos--;
    }
    public void incrementYPOS()
    {
    	ypos++;
    }
    public void decrementYPOS()
    {
    	ypos--;
    }

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
	public void setSprite(String url) {
		this.sprite = createSprite(url);
	}

    
    public void printStats() {
    	System.out.print("Name: " + name + ", ");
		System.out.print("Class: " + kit + ", ");
		System.out.println(" Level: " + level);
		System.out.println("Money: " + money);
		System.out.println("XP: " + XP);
    	System.out.print("current HP: " + currentHP + ", ");
		System.out.println("current MP: " + currentMP + ", ");
		System.out.println("Stats:" + "\n"
				+ "HP: " + this.getHP() + ", "
				+ "MP: " + this.getMP() + ", "
				+ "OF: " + this.getOffense() + ", " 
				+ "DEF: " + this.getDefense() + "\n"
				+ "SPI: " + this.getSpirit() + ", "
				+ "RES: " + this.getResistence() + ", "
				+ "SPD: " + this.getSpeed() + ", "
				+ "LUC: " + this.getLuck() + "\n"
				);
    }
	    




}