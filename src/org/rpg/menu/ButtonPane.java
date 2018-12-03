package org.rpg.menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.rpg.item.Item;

public class ButtonPane extends JPanel {

   private JButton[][] buttonsGrid;
   private JButton[] buttons;
   private int numPlayers = 4; // default of 4

   public ButtonPane(int row, int col, Color fgColor) {
      super(new GridLayout(row, col));
      buttonsGrid = new JButton[row][col];
      for (int i = 0; i < buttonsGrid.length; i++) {
         for (int j = 0; j < buttonsGrid[i].length; j++) {
            final int curRow = i;
            final int curCol = j;
            buttonsGrid[i][j] = new JButton(i + ", " + j);
            buttonsGrid[i][j].setBackground(fgColor);
            buttonsGrid[i][j].setForeground(Color.WHITE);
            buttonsGrid[i][j].setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
            buttonsGrid[i][j].addKeyListener(enter);
            buttonsGrid[i][j].addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_UP:
                     if (curRow > 0)
                    	 buttonsGrid[curRow - 1][curCol].requestFocus();
                     break;
                  case KeyEvent.VK_DOWN:
                     if (curRow < buttonsGrid.length - 1)
                    	 buttonsGrid[curRow + 1][curCol].requestFocus();
                     break;
                  case KeyEvent.VK_LEFT:
                     if (curCol > 0)
                    	 buttonsGrid[curRow][curCol - 1].requestFocus();
                     break;
                  case KeyEvent.VK_RIGHT:
                     if (curCol < buttonsGrid[curRow].length - 1)
                    	 buttonsGrid[curRow][curCol + 1].requestFocus();
                     break;
                  default:
                     break;
                  }
               }
            });
            add(buttonsGrid[i][j]);
         }
      }
   }
   
   public ButtonPane(String option, Color fgColor, int number) {
	   super(new GridLayout(number, 0));
	   this.setBackground(Color.BLACK);
	   switch(option) {
  		case "playerNum": 
  			createNumPlayersButtons(fgColor);
  			break;
  		case "title":
  			createTitleButtons(fgColor);
  			break;
  		case "partyMenu":
  			createPartyMenuButtons(fgColor);
  			break;
  		case "combat":
  			createCombatButtons(fgColor);
  			break;
	   }
   }
   
   public ButtonPane(String option, Color fgColor) {
	   this(option, fgColor, 6);
   }
   
   // for inventory buttons
   public ButtonPane(Color fgColor, ArrayList<Item> totalInventory) {
	   super(new GridLayout(totalInventory.size(), 0));
	   this.setBackground(Color.BLACK);
	   createInventoryButtons(fgColor, totalInventory);
   }
   


private void createNumPlayersButtons(Color fgColor) {
	   
	   buttons = new JButton[4];
	   buttons[0] = new JButton("4 (Normal)");
	   buttons[1] = new JButton("3 (Hard)");
	   buttons[2] = new JButton("2 (Extreme)");
	   buttons[3] = new JButton("1 (SADISTIC)");
	   
	   for (int i = 0; i < buttons.length; i++) {
            final int curRow = i;
            final int curCol = 0;
            buttons[i].setBackground(fgColor);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
            buttons[i].setBorder(BorderFactory.createCompoundBorder(
            		buttons[i].getBorder(), 
			        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            buttons[i].addKeyListener(enter);
            buttons[i].addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_UP:
                      if (curRow > 0)
                    	  buttons[curRow - 1].requestFocus();
                      break;
                   case KeyEvent.VK_DOWN:
                      if (curRow < buttons.length - 1)
                    	  buttons[curRow + 1].requestFocus();
                      break;
                  default:
                     break;
                  }
               }
            });
            add(buttons[i]);
	      }
   }

	private void createTitleButtons(Color fgColor) {
		buttons = new JButton[3];
		buttons[0] = new JButton("START");
	    buttons[1] = new JButton("CONTINUE");
	    buttons[2] = new JButton("DEBUG");
	    
	    for (int i = 0; i < buttons.length; i++) {
            final int curRow = i;
            final int curCol = 0;
            buttons[i].setBackground(fgColor);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("Monaco", Font.TRUETYPE_FONT, 20));
            buttons[i].setBorder(BorderFactory.createCompoundBorder(
            		buttons[i].getBorder(), 
			        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            buttons[i].addKeyListener(enter);
            buttons[i].addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_UP:
                      if (curRow > 0)
                    	  buttons[curRow - 1].requestFocus();
                      break;
                   case KeyEvent.VK_DOWN:
                      if (curRow < buttons.length - 1)
                    	  buttons[curRow + 1].requestFocus();
                      break;
                  default:
                     break;
                  }
               }
            });
            add(buttons[i]);
	      }
		
	}
	
	private void createPartyMenuButtons(Color fgColor) {
		buttons = new JButton[6];
		buttons[0] = new JButton("PARTY");
	    buttons[1] = new JButton("INVENTORY");
	    buttons[2] = new JButton("EQUIPMENT MENU");
	    buttons[3] = new JButton("SAVE");
	    buttons[4] = new JButton("EXIT");
	    buttons[5] = new JButton("QUIT GAME");
	    
	    for (int i = 0; i < buttons.length; i++) {
            final int curRow = i;
            final int curCol = 0;
            buttons[i].setBackground(fgColor);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBorder(new LineBorder(Color.WHITE));
            buttons[i].setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
            buttons[i].addKeyListener(enter);
            buttons[i].addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_UP:
                      if (curRow > 0)
                    	  buttons[curRow - 1].requestFocus();
                      break;
                   case KeyEvent.VK_DOWN:
                      if (curRow < buttons.length - 1)
                    	  buttons[curRow + 1].requestFocus();
                      break;
                  default:
                     break;
                  }
               }
            });
            add(buttons[i]);
	      }
		
	}
	
	private void createInventoryButtons(Color fgColor, ArrayList<Item> totalInventory) {
		buttons = new JButton[totalInventory.size()]; 
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			String itemName = totalInventory.get(i).getName() + " : " + totalInventory.get(i).getDescription();
			buttons[i].setText(buttons[i].getText() + itemName);
            final int curRow = i;
            final int curCol = 0;
            buttons[i].setBackground(fgColor);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBorder(new LineBorder(Color.WHITE));
            buttons[i].setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
            buttons[i].addKeyListener(enter);
            buttons[i].addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_UP:
                      if (curRow > 0)
                    	  buttons[curRow - 1].requestFocus();
                      break;
                   case KeyEvent.VK_DOWN:
                      if (curRow < buttons.length - 1)
                    	  buttons[curRow + 1].requestFocus();
                      break;
                  default:
                     break;
                  }
               }
            });
            add(buttons[i]);
	      }
	}
	
	private void createCombatButtons(Color fgColor) {
		buttons = new JButton[4];
		buttons[0] = new JButton("FIGHT");
	    buttons[1] = new JButton("ABILITIES");
	    buttons[2] = new JButton("FLEE");
	    buttons[3] = new JButton("SKIP TURN");

	    for (int i = 0; i < buttons.length; i++) {
            final int curRow = i;
            final int curCol = 0;
            buttons[i].setBackground(fgColor);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBorder(new LineBorder(Color.WHITE));
            buttons[i].setFont(new Font("Monaco", Font.TRUETYPE_FONT, 14));
            buttons[i].addKeyListener(enter);
            buttons[i].addKeyListener(new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  switch (e.getKeyCode()) {
                  case KeyEvent.VK_UP:
                      if (curRow > 0)
                    	  buttons[curRow - 1].requestFocus();
                      break;
                   case KeyEvent.VK_DOWN:
                      if (curRow < buttons.length - 1)
                    	  buttons[curRow + 1].requestFocus();
                      break;
                  default:
                     break;
                  }
               }
            });
            add(buttons[i]);
	      }
		
	}
   
   public JButton[] getNumberButtons() {
	   return buttons;
   }
   
   public JButton[] getTitleButtons() {
		return buttons;
	}

   private KeyListener enter = new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
         if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            ((JButton) e.getComponent()).doClick();
         }
      }
   };

   public static void main(String[] args) {
      JFrame f = new JFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.add(new ButtonPane(4, 4, new Color(18, 1, 113)));
      f.pack();
      f.setVisible(true);
   }


}