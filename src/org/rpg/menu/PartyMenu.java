package org.rpg.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.rpg.character.Party;
import org.rpg.character.Player;
import org.rpg.item.EquipableItem;
import org.rpg.item.Item;
import org.rpg.map.Tile;
import org.rpg.system.KeyBinding;

public class PartyMenu {
	private Party party;
	
	private JFrame menuScreen;
	private ButtonPane optionPane;
	private ButtonPane itemsPane;
	private ButtonPane equipmentPane;
	private JPanel menuPanel;
	private JPanel infoPanel;
	private JButton returnButtonInfo;
	private JPanel inventoryPanel;
	private JButton returnButtonItem;
	private JPanel equipmentPanel;
	private JButton returnButtonEquip;

	private Color bgColor;
	private Color windowColor;
	
	public static boolean stopMenu = false;
	private static final int SCREEN_WIDTH = 1280; // px
    private static final int SCREEN_HEIGHT = 1024;
	
	public PartyMenu(Party party) {
		windowColor = new Color(18, 1, 113); // Navy Blue
		bgColor = new Color(0, 0, 0);
	
		// copy party to private variable party, return on exit
		this.party = party;
	
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Panel building functions
	// 1. menuPanel
	// 2. infoPanel
	// 3. itemPanel
	// 4. equipmentPanel
	////////////////////////////////////////////////////////////////////////////////////////
	private void buildMenuPanel() {
		// first make options buttons
		optionPane = new ButtonPane("partyMenu", windowColor);
		for(JButton button : optionPane.getNumberButtons()) {
			button.addActionListener(new MenuListener()); // private inner class below
		}
		
		// build PartyMenuTitle label
		JLabel PartyMenuTitle = new JLabel("PARTY MENU", SwingConstants.CENTER);
		PartyMenuTitle.setBackground(windowColor);
		PartyMenuTitle.setOpaque(true);
		PartyMenuTitle.setForeground(Color.WHITE);	
		PartyMenuTitle.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		PartyMenuTitle.setBorder(BorderFactory.createCompoundBorder(
				PartyMenuTitle.getBorder(), 
		        BorderFactory.createLineBorder(Color.WHITE, 5)));
		
		// build money label
		JLabel moneyDisplay = new JLabel("", SwingConstants.CENTER);
		moneyDisplay.setBackground(windowColor);
		moneyDisplay.setOpaque(true);
		moneyDisplay.setForeground(Color.WHITE);	
		moneyDisplay.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		moneyDisplay.setBorder(BorderFactory.createCompoundBorder(
				moneyDisplay.getBorder(), 
		        BorderFactory.createLineBorder(Color.WHITE, 5)));
		String money = "MONEY " + "\n" + Integer.toString(party.getParty().get(1).getMoney()); // party money = p1 money
		moneyDisplay.setText("<html>" + money.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		
		// build partyPanel
		JPanel partyPanel = new JPanel(new GridLayout(party.getNumPlayers(), 0));
		partyPanel.setBorder(BorderFactory.createCompoundBorder(
				partyPanel.getBorder(), 
		        BorderFactory.createLineBorder(Color.BLACK, 1)));
		partyPanel.setBackground(windowColor);
		
		// build and add player info to partyPanel
		for(int i = 0; i < party.getNumPlayers(); i++) {
			JPanel playerInfo = new JPanel(new FlowLayout());
			playerInfo.setBackground(windowColor);
			JLabel playerAvi = new JLabel();
 			playerAvi.setIcon(party.getParty().get(i + 1).getAvatar());
 			//playerAvi.setBackground(windowColor);
 			playerInfo.add(playerAvi);
 			
 			JLabel playerNameAndLVL = new JLabel();
 			playerNameAndLVL.setBackground(windowColor);
 			playerNameAndLVL.setForeground(Color.WHITE);
 			playerNameAndLVL.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 13));
 			String namelvl = party.getParty().get(i + 1).getName() + "\n" + "LV " + 
 								party.getParty().get(i + 1).getLevel();
 			playerNameAndLVL.setText("<html>" + namelvl.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
 			playerInfo.add(playerNameAndLVL);
 			
 			JLabel playerClassHPMP = new JLabel();
 			playerClassHPMP.setBackground(windowColor);
 			playerClassHPMP.setForeground(Color.WHITE);
 			playerClassHPMP.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 13));
 			String classHPMP = " " + party.getParty().get(i+1).getKit() + "\n" + " HP " + party.getParty().get(i+1).getCurrentHP() + 
 					"/" + party.getParty().get(i+1).getHP() + "\n" + " MP " + party.getParty().get(i+1).getCurrentMP() + 
 					"/" + party.getParty().get(i+1).getMP();
 			playerClassHPMP.setText("<html>" + classHPMP.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
 			playerInfo.add(playerClassHPMP);
 			
 			// add each playerInfo to partyPanel, should go down vertically
 			partyPanel.add(playerInfo);
		}
		
		// instantiate menuPanel and add components
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout()); 
		menuPanel.setBackground(bgColor);
		
		GridBagConstraints partyPanelCons = new GridBagConstraints();
		partyPanelCons.gridx = 0;
		partyPanelCons.gridy = 0;
		partyPanelCons.gridwidth = 2;
		partyPanelCons.gridheight = 3;
		partyPanelCons.fill = GridBagConstraints.BOTH;
		partyPanelCons.weightx = 0.66;
		partyPanelCons.weighty = 1.0;
		menuPanel.add(partyPanel, partyPanelCons);
		
		GridBagConstraints optionPaneCons = new GridBagConstraints();
		optionPaneCons.gridx = 2;
		optionPaneCons.gridy = 0;
		optionPaneCons.gridheight = 1;
		optionPaneCons.gridwidth = 1;
		optionPaneCons.fill = GridBagConstraints.BOTH;
		optionPaneCons.weightx = 0.33;
		optionPaneCons.weighty = 0.66;
		menuPanel.add(optionPane, optionPaneCons);
		
		GridBagConstraints titleCons = new GridBagConstraints();
		titleCons.gridx = 2;
		titleCons.gridy = 1;
		titleCons.fill = GridBagConstraints.BOTH;
		titleCons.weightx = 0.33;
		titleCons.weighty = 0.33;
		menuPanel.add(PartyMenuTitle, titleCons);
		
		GridBagConstraints moneyLabelCons = new GridBagConstraints();
		moneyLabelCons.gridx = 2;
		moneyLabelCons.gridy = 2;
		moneyLabelCons.gridheight = 1;
		moneyLabelCons.gridwidth = 1;
		moneyLabelCons.fill = GridBagConstraints.BOTH;
		moneyLabelCons.weightx = 0.33;
		moneyLabelCons.weighty = 0.33;
		menuPanel.add(moneyDisplay, moneyLabelCons);
	}
	
	private void buildInfoPanel() {
		
		// first build partyPanel
		JPanel partyPanel = new JPanel(new GridLayout(party.getNumPlayers(), 0));
		partyPanel.setBorder(BorderFactory.createCompoundBorder(
				partyPanel.getBorder(), 
		        BorderFactory.createLineBorder(Color.BLACK, 1)));
		partyPanel.setBackground(windowColor);
		
		// build and add player info to partyPanel
		for(int i = 0; i < party.getNumPlayers(); i++) {
			JPanel playerInfo = new JPanel(new FlowLayout());
			playerInfo.setBackground(windowColor);
			JLabel playerAvi = new JLabel();
 			playerAvi.setIcon(party.getParty().get(i + 1).getAvatar());
 			//playerAvi.setBackground(windowColor);
 			playerInfo.add(playerAvi);
 			
 			JLabel playerMainInfo = new JLabel();
 			playerMainInfo.setBackground(windowColor);
 			playerMainInfo.setForeground(Color.WHITE);
 			playerMainInfo.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 13));
 			String mainInfo = party.getParty().get(i + 1).getName() + "                    " + "EXP: " +
 					party.getParty().get(i + 1).getXP() + "\n" + "LV " + 
 								party.getParty().get(i + 1).getLevel() + " " + party.getParty().get(i+1).getKit();
 			playerMainInfo.setText("<html>" + mainInfo.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
 			playerInfo.add(playerMainInfo);
 			
 			JLabel playerClassHPMP = new JLabel();
 			playerClassHPMP.setBackground(windowColor);
 			playerClassHPMP.setForeground(Color.WHITE);
 			playerClassHPMP.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 13));
 			String classHPMP = "HP " + party.getParty().get(i+1).getCurrentHP() + 
 					"/" + party.getParty().get(i+1).getHP() + "\n" + "MP " + party.getParty().get(i+1).getCurrentMP() + 
 					"/" + party.getParty().get(i+1).getMP();
 			playerClassHPMP.setText("<html>" + classHPMP.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
 			playerInfo.add(playerClassHPMP);
 			
 			JLabel playerStats = new JLabel();
 			playerStats.setBackground(windowColor);
 			playerStats.setForeground(Color.WHITE);
 			playerStats.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 13));
 			String statsInfo = "Offense    " + party.getParty().get(i+1).getOffense() + "\n" +
 					"Defense    " + party.getParty().get(i+1).getDefense() + "\n" +
 					"Spirit    " + party.getParty().get(i+1).getSpirit() + "\n" +
 					"Resistence    " + party.getParty().get(i+1).getResistence() + "\n" +
 					"Speed    " + party.getParty().get(i+1).getSpeed() + "\n" +
 					"Luck    " + party.getParty().get(i+1).getLuck();
 			playerStats.setText("<html>" + statsInfo.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
 			playerInfo.add(playerStats);
 			
 			// add each playerInfo to partyPanel, should go down vertically
 			partyPanel.add(playerInfo);
		}
		
		// build returnButton
		returnButtonInfo = new JButton("RETURN");
		returnButtonInfo.setBackground(windowColor);
		returnButtonInfo.setForeground(Color.WHITE);
		returnButtonInfo.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		//returnButton.setPreferredSize(new Dimension(900, 100));
		returnButtonInfo.setBorder(new LineBorder(Color.WHITE));
		returnButtonInfo.addActionListener(new MenuListener());
		
		// instantiate info panel and add components (party panel & returnButton)
		infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBackground(windowColor);
	
		GridBagConstraints partyPanelCons = new GridBagConstraints();
		partyPanelCons.gridx = 0;
		partyPanelCons.gridy = 0;
		partyPanelCons.gridwidth = 1;
		partyPanelCons.gridheight = 1;
		partyPanelCons.fill = GridBagConstraints.BOTH;
		partyPanelCons.weightx = 0.66;
		partyPanelCons.weighty = 0.75;
		infoPanel.add(partyPanel, partyPanelCons);
		
		GridBagConstraints returnButtonCons = new GridBagConstraints();
		returnButtonCons.gridx = 0;
		returnButtonCons.gridy = 1;
		returnButtonCons.gridwidth = 1;
		returnButtonCons.gridheight = 1;
		returnButtonCons.fill = GridBagConstraints.BOTH;
		returnButtonCons.weightx = 0.66;
		returnButtonCons.weighty = 0.25;
		infoPanel.add(returnButtonInfo, returnButtonCons);
	}
	
	// FIXME: Implement item usage functionality in ItemListener private class below
	private void buildInventoryPanel() {
		// first make items buttons
		ArrayList<Item> totalInventory = new ArrayList<Item>(10); // contains all party items
		for(Map.Entry<Integer, Player> p : party.getParty().entrySet()) {
			for(Item item : p.getValue().getInventory()) {
				totalInventory.add(item);
			}
		}
		itemsPane = new ButtonPane(windowColor, totalInventory);
		for(JButton button : itemsPane.getNumberButtons()) {
			button.addActionListener(new ItemsListener()); // private inner class below
		}
		
		// build returnButton
		returnButtonItem = new JButton("RETURN");
		returnButtonItem.setBackground(windowColor);
		returnButtonItem.setForeground(Color.WHITE);
		returnButtonItem.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		returnButtonItem.setBorder(new LineBorder(Color.WHITE));
		returnButtonItem.addActionListener(new MenuListener());
		
		// initialize inventoryPanel, add button pane and return button
		inventoryPanel = new JPanel();
		inventoryPanel.setLayout(new GridBagLayout()); 
		inventoryPanel.setBackground(bgColor);
		
		GridBagConstraints itemsCons = new GridBagConstraints();
		itemsCons.gridx = 0;
		itemsCons.gridy = 0;
		itemsCons.gridwidth = 1;
		itemsCons.gridheight = 1;
		itemsCons.fill = GridBagConstraints.BOTH;
		itemsCons.weightx = 0.66;
		itemsCons.weighty = 0.75;
		inventoryPanel.add(itemsPane, itemsCons);
		
		GridBagConstraints returnButtonCons = new GridBagConstraints();
		returnButtonCons.gridx = 0;
		returnButtonCons.gridy = 1;
		returnButtonCons.gridwidth = 1;
		returnButtonCons.gridheight = 1;
		returnButtonCons.fill = GridBagConstraints.BOTH;
		returnButtonCons.weightx = 0.66;
		returnButtonCons.weighty = 0.25;
		inventoryPanel.add(returnButtonItem, returnButtonCons);
	}
	
	private void buildEquipmentPanel() {
		// first make equipment buttons
		ArrayList<Item> totalInventory = new ArrayList<Item>(10); // contains all party items
		for(Map.Entry<Integer, Player> p : party.getParty().entrySet()) {
			for(EquipableItem item : p.getValue().getEquipment()) {
				totalInventory.add(item);
			}
		}
		equipmentPane = new ButtonPane(windowColor, totalInventory);
		for(JButton button : equipmentPane.getNumberButtons()) {
			button.addActionListener(new ItemsListener()); // private inner class below
		}
		
		// build returnButton
		returnButtonEquip = new JButton("RETURN");
		returnButtonEquip.setBackground(windowColor);
		returnButtonEquip.setForeground(Color.WHITE);
		returnButtonEquip.setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
		returnButtonEquip.setBorder(new LineBorder(Color.WHITE));
		returnButtonEquip.addActionListener(new MenuListener());
		
		// initialize inventoryPanel, add button pane and return button
		equipmentPanel = new JPanel();
		equipmentPanel.setLayout(new GridBagLayout()); 
		equipmentPanel.setBackground(bgColor);
		
		GridBagConstraints itemsCons = new GridBagConstraints();
		itemsCons.gridx = 0;
		itemsCons.gridy = 0;
		itemsCons.gridwidth = 1;
		itemsCons.gridheight = 1;
		itemsCons.fill = GridBagConstraints.BOTH;
		itemsCons.weightx = 0.66;
		itemsCons.weighty = 0.75;
		equipmentPanel.add(equipmentPane, itemsCons);
		
		GridBagConstraints returnButtonCons = new GridBagConstraints();
		returnButtonCons.gridx = 0;
		returnButtonCons.gridy = 1;
		returnButtonCons.gridwidth = 1;
		returnButtonCons.gridheight = 1;
		returnButtonCons.fill = GridBagConstraints.BOTH;
		returnButtonCons.weightx = 0.66;
		returnButtonCons.weighty = 0.25;
		equipmentPanel.add(returnButtonEquip, returnButtonCons);
	}
	

	////////////////////////////////////////////////////////////////////////////
	// Panel switching handler functions
	//////////////////////////////////////////////////////////
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton source = (JButton)(event.getSource());
			
			if(source.equals(optionPane.getNumberButtons()[0])) {
				switchToInfoPane();
			} else if(source.equals(optionPane.getNumberButtons()[1])) {
				switchToInventoryPane();
			} else if(source.equals(optionPane.getNumberButtons()[2])) {
				switchToEquipmentPane();
			} else if(source.equals(optionPane.getNumberButtons()[3])) {
				//handleSave();
			} else if(source.equals(optionPane.getNumberButtons()[4])) {
				handleExitMenu();
			} else if(source.equals(optionPane.getNumberButtons()[5])) {
				handleQuitGame();
			} else if(source.equals(returnButtonInfo)) { // return to menuPanel
				menuScreen.remove(infoPanel);
				menuScreen.add(menuPanel);
				menuScreen.pack();	
				menuScreen.setVisible(true);
			} else if(source.equals(returnButtonItem)) { 
				menuScreen.remove(inventoryPanel);
				menuScreen.add(menuPanel);
				menuScreen.pack();	
				menuScreen.setVisible(true);
			} else if(source.equals(returnButtonEquip)) { 
				menuScreen.remove(equipmentPanel);
				menuScreen.add(menuPanel);
				menuScreen.pack();	
				menuScreen.setVisible(true);
			}
			
		}
		
		private void switchToInfoPane() {	
			menuScreen.remove(menuPanel);
			buildInfoPanel();
			menuScreen.add(infoPanel);
			menuScreen.pack();		
		}
		
		private void switchToInventoryPane() {
			menuScreen.remove(menuPanel);
			buildInventoryPanel();
			menuScreen.add(inventoryPanel);
			menuScreen.pack();
		}
		
		private void switchToEquipmentPane() {
			menuScreen.remove(menuPanel);
			buildEquipmentPanel();
			menuScreen.add(equipmentPanel);
			menuScreen.pack();
		}
		
		private void handleExitMenu() {
			menuScreen.removeAll();
			menuScreen.dispose();
			stopMenu = true;
		}
		
		private void handleQuitGame() {
			menuScreen.removeAll();
			menuScreen.dispose();
			System.exit(0);
		}

	}
	
	private class ItemsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			JButton source = (JButton)(event.getSource());
			
		}
		
		
	
	}
	
	//////////////////////////////////////////////////////////
	// Update function
	//////////////////////////////////////////////////////
	public void update() {
		// instantiate JFrame
		menuScreen = new JFrame("Options Menu");
		menuScreen.setBackground(bgColor); 
		menuScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		menuScreen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		buildMenuPanel();
		menuScreen.add(menuPanel);
		menuScreen.pack();
		menuScreen.setVisible(true);
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
	   
	public void setParty(Party party) {
		this.party = party;	
	}
	
	public Party getParty() {
		return party;
	}

	
    public static void main(String[] args) {
    	Player p1 = new Player("Warrior");
    	Player p2 = new Player("Cleric");
    	Player p3 = new Player("Archer");
    	Player p4 = new Player("Mage");
    	Party party = new Party();
    	party.addPartyMember(1, p1);
    	party.addPartyMember(2, p2);
    	party.addPartyMember(3, p3);
    	party.addPartyMember(4, p4);
    	
    	PartyMenu partyMenu = new PartyMenu(party);
    	partyMenu.update();
    }

	
	
	

	
}
