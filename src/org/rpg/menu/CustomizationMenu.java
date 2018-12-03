package org.rpg.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.rpg.character.Party;
import org.rpg.character.Player;

public class CustomizationMenu implements ActionListener{
	
	private Party party;
	private int numPlayers = 4; // determines # of PlayableCharacter(PC) to be made
	private String[] playerNames; // determines names of each PC
	private String[] playerClasses; // determines class of each PC
	private String[] playerAviURLs; // determine avatar for each PC, change URLs in constructor
	private ImageIcon[] playerAvis; // holds the actual avatar png for each PC
	
	private JFrame customizationScreen;
	private JPanel numPanel;
	private JPanel namePanel;
	private JPanel classPanel;
	private JPanel welcomePanel;
	private ButtonPane buttonPane;
	private JButton submitNames;
	private JButton submitClasses;
	private JButton finalConfirmation;
	private JTextField[] playerNameFields; // indexes are from 0 - 3
	private ButtonGroup[] classRBGroups;
	private Color bgColor;
	private Color windowColor;
	public static boolean customizationComplete = false;
	
	public CustomizationMenu(Party party) {
		playerNameFields = new JTextField[5]; // arbitrary number
		classRBGroups = new ButtonGroup[5];
		playerAvis = new ImageIcon[4];
		windowColor = new Color(18, 1, 113); // Navy Blue
		bgColor = new Color(0, 0, 0);
		// instantiate JFrame
		customizationScreen = new JFrame("Choose your class");
		customizationScreen.setBackground(bgColor); 
		customizationScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		playerAviURLs = new String[4];
		playerAviURLs[0] = "https://i.ibb.co/KyMJLB1/t-oak-150.png"; // p1
		playerAviURLs[1] = "https://i.ibb.co/PW5RkVR/thumbsup-150.jpg"; // p2
		playerAviURLs[2] = "https://i.ibb.co/4SsXgrj/alucard-150.jpg"; // p3
		playerAviURLs[3] = "https://i.ibb.co/093ZHMy/lain-150.png"; // p4
		
		// copy party to private variable party, will return in welcomePanel handler
		// also will instantiate all party members
		this.party = party;
		
		buildNumberPanel();
		customizationScreen.add(numPanel);
		customizationScreen.pack();
		customizationScreen.setVisible(true);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Panel building functions
	// 1. numPanel
	// 2. namePanel
	// 3. classPanel
	// 4. welcomePanel
	////////////////////////////////////////////////////////////////////////////////////////
	private void buildNumberPanel() {
		// first make number of player buttons	
		buttonPane = new ButtonPane("playerNum", windowColor);
		for(JButton button : buttonPane.getNumberButtons()) {
			button.addActionListener(new MenuListener()); // private inner class below
		}
		
		// add label
		JLabel howManyPlayers = new JLabel("How many players?");
		howManyPlayers.setForeground(Color.WHITE);
		howManyPlayers.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		howManyPlayers.setBorder(BorderFactory.createCompoundBorder(
				howManyPlayers.getBorder(), 
		        BorderFactory.createEmptyBorder(10, 10, 10, 50)));
		
		// instantiate numPanel
		numPanel = new JPanel();
		numPanel.setLayout(new GridBagLayout()); // change layout manager here
		numPanel.setBackground(bgColor);
		numPanel.add(howManyPlayers);
		numPanel.add(buttonPane);
	}
	
	private void buildNamePanel() {
		// instantiate namePanel as a vertical grid
		namePanel = new JPanel(new GridBagLayout());
		namePanel.setBackground(bgColor);
		JPanel subNamePanel = new JPanel(new GridLayout(getNumPlayers(), 0));
		subNamePanel.setBackground(bgColor);

		// make Java TextFields for name inputs
		//System.out.println(numPlayers); // debug
		for(int i = 0; i < getNumPlayers(); i++) {
			JLabel playerNameLabel = new JLabel("Player " + (i + 1) + "'s name:");
			playerNameLabel.setForeground(Color.WHITE);
			playerNameLabel.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
			playerNameLabel.setBorder(BorderFactory.createCompoundBorder(
					playerNameLabel.getBorder(), 
			        BorderFactory.createEmptyBorder(10, 10, 10, 30)));
			playerNameFields[i] = new JTextField();
			playerNameFields[i].setForeground(Color.WHITE);
			playerNameFields[i].setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
			playerNameFields[i].setBackground(windowColor);
			playerNameFields[i].setBorder(BorderFactory.createCompoundBorder(
					playerNameFields[i].getBorder(), 
			        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			subNamePanel.add(playerNameLabel);
			subNamePanel.add(playerNameFields[i]);
		}
		// add subNamePanel to namePanel
		GridBagConstraints subNameCons = new GridBagConstraints();
		subNameCons.gridx = 0;
		subNameCons.gridy = 0;
		//subNameCons.fill = GridBagConstraints.BOTH;
		//subNameCons.weightx = 0.25;
		//subNameCons.weighty = 1.0;
		namePanel.add(subNamePanel, subNameCons);
		
		// add submit button, at the bottom
		submitNames = new JButton("END");
		submitNames.setBackground(windowColor);
		submitNames.setForeground(Color.WHITE);
		submitNames.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		submitNames.addActionListener(new MenuListener());
		submitNames.addKeyListener(enter);
		GridBagConstraints nameButtonCons = new GridBagConstraints();
		nameButtonCons.gridx = 0;
		nameButtonCons.gridy = 1;
		nameButtonCons.ipady = 10;
		nameButtonCons.fill = GridBagConstraints.BOTH;
		namePanel.add(submitNames, nameButtonCons);
	}
	
	private void buildClassPanel() {
		classPanel = new JPanel(new GridBagLayout());
		classPanel.setBackground(bgColor);
		JPanel subClassPanel = new JPanel(new GridLayout(3, 0));
		subClassPanel.setBackground(bgColor);
		
 		JButton classScreenTitle = new JButton("Choose your class"); // not really a button
 		classScreenTitle.setBackground(windowColor);
 		classScreenTitle.setForeground(Color.WHITE);
 		classScreenTitle.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
 		classScreenTitle.setPreferredSize(new Dimension(900, 100));
 		classScreenTitle.setEnabled(false);
 		subClassPanel.add(classScreenTitle);
 		
 		JPanel variablePlayerClasses = new JPanel(new GridLayout(0, getNumPlayers()));
 		variablePlayerClasses.setBackground(windowColor);
 		variablePlayerClasses.setBorder(BorderFactory.createCompoundBorder(
 				variablePlayerClasses.getBorder(), 
		        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
 		
 		// create radiobutton groups for each player
 		for(int i = 0; i < getNumPlayers(); i++) {
 		    JRadioButton warriorButton = new JRadioButton("Warrior");
 		    warriorButton.setBackground(windowColor);
 		    warriorButton.setForeground(Color.WHITE);
 		    warriorButton.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 15));
 		    warriorButton.setActionCommand("Warrior");
 		    warriorButton.setSelected(true);
 		    JRadioButton archerButton = new JRadioButton("Archer");
 		    archerButton.setBackground(windowColor);
 		    archerButton.setForeground(Color.WHITE);
 		    archerButton.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 15));
 		    archerButton.setActionCommand("Archer");
		    JRadioButton mageButton = new JRadioButton("Mage");
		    mageButton.setBackground(windowColor);
		    mageButton.setForeground(Color.WHITE);
		    mageButton.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 15));
		    mageButton.setActionCommand("Mage");
		    JRadioButton clericButton = new JRadioButton("Cleric");
		    clericButton.setBackground(windowColor);
		    clericButton.setForeground(Color.WHITE);
		    clericButton.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 15));
		    clericButton.setActionCommand("Cleric");
		    classRBGroups[i] = new ButtonGroup();
		    classRBGroups[i].add(warriorButton);
		    classRBGroups[i].add(archerButton);
		    classRBGroups[i].add(mageButton);
		    classRBGroups[i].add(clericButton);
		    
		    variablePlayerClasses.add(warriorButton);
		    variablePlayerClasses.add(archerButton);
		    variablePlayerClasses.add(mageButton);
		    variablePlayerClasses.add(clericButton);
 		}
 		subClassPanel.add(variablePlayerClasses);
 		
 		submitClasses = new JButton("SUBMIT");
 		submitClasses.setBackground(windowColor);
 		submitClasses.setForeground(Color.WHITE);
 		submitClasses.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
 		submitClasses.addActionListener(new MenuListener());
 		submitClasses.addKeyListener(enter);
 		
 		subClassPanel.add(submitClasses);
 		classPanel.add(subClassPanel);	
	}
	
	// displays name, class, and sprite(?) for each player, then waits for user to continue. 
	private void buildWelcomePanel() {
		welcomePanel = new JPanel(new GridBagLayout());
		welcomePanel.setBackground(bgColor);
		welcomePanel.setBorder(BorderFactory.createCompoundBorder(
				welcomePanel.getBorder(), 
		        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		GridBagConstraints c = new GridBagConstraints();
		
		JButton classScreenTitle = new JButton("Welcome to NEXT GEN RPG"); // not really a button
 		classScreenTitle.setBackground(windowColor);
 		classScreenTitle.setForeground(Color.WHITE);
 		classScreenTitle.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 27));
 		classScreenTitle.setPreferredSize(new Dimension(1000, 100));
 		classScreenTitle.setEnabled(false);
 		c.gridx = 0;
 		c.gridy = 0;
 		c.fill = GridBagConstraints.HORIZONTAL;
 		c.ipady = 20;      //make this component tall
 		c.weightx = 0.0;
 		c.gridwidth = 4;
 		welcomePanel.add(classScreenTitle, c);
 		//subWelcomePanel.add(classScreenTitle);
 		
// 		JPanel playerInfoPanel = new JPanel(new GridLayout(0, numPlayers));
// 		playerInfoPanel.setBackground(windowColor);
// 		playerInfoPanel.setBorder(BorderFactory.createCompoundBorder(
// 				playerInfoPanel.getBorder(), 
//		        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
 		
 		for(int i = 0; i < getNumPlayers(); i++) {
 			//JPanel subInfoPanel = new JPanel();
 			//subInfoPanel.setBackground(windowColor);
 			
 			// create avatar for each player from url field
 			URL url;
 			ImageIcon avi = null;
			try {
				url = new URL(playerAviURLs[i]);
				playerAvis[i] = new ImageIcon(url);
			} catch (MalformedURLException e) { e.printStackTrace(); }
 			JLabel playerAvi = new JLabel();
 			playerAvi.setIcon(playerAvis[i]);
 			playerAvi.setBackground(windowColor);
 			JLabel playerName = new JLabel();
 			playerName.setBackground(windowColor);
 			playerName.setForeground(Color.WHITE);
 			playerName.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
 			JLabel playerClass = new JLabel();
 			playerClass.setBackground(windowColor);
 			playerClass.setForeground(Color.WHITE);
 			playerClass.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
 			// add info to labels
 			playerName.setText(playerName.getText() + playerNames[i]);
 			playerClass.setText(playerClass.getText() + playerClasses[i]);
 			
 			GridBagConstraints aviConst = new GridBagConstraints();
 			aviConst.gridx = i;
 			aviConst.gridy = 1;
 			welcomePanel.add(playerAvi, aviConst);
 			GridBagConstraints nameConst = new GridBagConstraints();
 			nameConst.gridx = i;
 			nameConst.gridy = 2;
 			welcomePanel.add(playerName, nameConst);
 			GridBagConstraints classConst = new GridBagConstraints();
 			classConst.gridx = i;
 			classConst.gridy = 3;
 			welcomePanel.add(playerClass, classConst);
 			//playerInfoPanel.add(subInfoPanel);
 		}
 		//subWelcomePanel.add(playerInfoPanel);
 		
 		finalConfirmation = new JButton("CONTINUE");
 		finalConfirmation.setBackground(windowColor);
 		finalConfirmation.setForeground(Color.WHITE);
 		finalConfirmation.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
 		finalConfirmation.addActionListener(new MenuListener());
 		finalConfirmation.addKeyListener(enter);
		
 		//subWelcomePanel.add(finalConfirmation);
 		//welcomePanel.add(subWelcomePanel);
 		c.gridx = 0;
 		c.gridy = 4;
 		c.fill = GridBagConstraints.HORIZONTAL;
 		c.ipady = 20;      //make this component tall
 		c.weightx = 0.0;
 		c.gridwidth = 4;
 		welcomePanel.add(finalConfirmation, c);
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Panel switching handler functions
	//////////////////////////////////////////////////////////
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton source = (JButton)(event.getSource());
			
			if(source.equals(buttonPane.getNumberButtons()[0])) {
				numPlayers = 4;
				switchToNamePane();
			} else if(source.equals(buttonPane.getNumberButtons()[1])) {
				numPlayers = 3;
				switchToNamePane();
			} else if(source.equals(buttonPane.getNumberButtons()[2])) {
				numPlayers = 2;
				switchToNamePane();
			} else if(source.equals(buttonPane.getNumberButtons()[3])) {
				numPlayers = 1;
				switchToNamePane();
			} else if(source.equals(submitNames)) {
				handleNameEntry();
			} else if(source.equals(submitClasses)) {
				handleClassEntry();
			} else if(source.equals(finalConfirmation)) {
				handleFinalConfirm();
			}
			
		}
		
		// instantiates Party with all party members
		private void handleFinalConfirm() {
			party.setNumPlayers(numPlayers);
			for(int i = 0; i < numPlayers; i++) {
			   Player p = new Player(playerClasses[i]);
 			   p.setName(playerNames[i]);
 			   p.setAvatar(playerAvis[i]);
 			   party.addPartyMember((i + 1), p); // 1, 2, 3, 4
			}
			// now call getParty() in statemachine to copy it back
			
			// destroy JFrame and send signal to change states in statemachine, to overworld
			// also to instantiate the party
			customizationScreen.removeAll();
			customizationScreen.dispose();
			customizationComplete = true;
			
		}

		private void handleClassEntry() {
			playerClasses = new String[getNumPlayers()];
			// get player classes and enter them into playerClasses array
			for(int i = 0; i < getNumPlayers(); i++) { // for each radioButtonGroup
				playerClasses[i] = classRBGroups[i].getSelection().getActionCommand();
				//System.out.println(playerClasses[i]); // debug
			}
			
		    // now remove classPanel and build welcomePanel
			customizationScreen.remove(classPanel);
			buildWelcomePanel();
			customizationScreen.add(welcomePanel);
			customizationScreen.pack();
			customizationScreen.setVisible(true);
		}

		private void handleNameEntry() {
			// instantiate player names string array
			playerNames = new String[getNumPlayers()];
			// get player names and enter them into playerNames array
			for(int i = 0; i < getNumPlayers(); i++) {
				playerNames[i] = playerNameFields[i].getText();
				//System.out.println(playerNames[i]); // debug
			}
			
			// now remove namePanel and build classPanel
			customizationScreen.remove(namePanel);
			buildClassPanel();
			customizationScreen.add(classPanel);
			customizationScreen.pack();
			customizationScreen.setVisible(true);
		}

		private void switchToNamePane() {
			customizationScreen.remove(numPanel);
			buildNamePanel();
			customizationScreen.add(namePanel);
			customizationScreen.pack();
			customizationScreen.setVisible(true);
		}

	}
	
	// enter key handler
	 private KeyListener enter = new KeyAdapter() {
	      @Override
	      public void keyTyped(KeyEvent e) {
	         if (e.getKeyChar() == KeyEvent.VK_ENTER) {
	            ((JButton) e.getComponent()).doClick();
	         }
	      }
	   };
	
	public int getNumPlayers() {
		return numPlayers;
	}
	
	public String[] getPlayerClasses() {
		return playerClasses;
	}
	
	public String[] getPlayerNames() {
		return playerNames;
	}
	
	public ImageIcon[] getPlayerAvis() {
		return playerAvis;
	}
	
	public Party getParty() {
		return party;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
    public static void main(String[] args) {
    	Party p = new Party();
    	CustomizationMenu menu = new CustomizationMenu(p);
    }

	


}
