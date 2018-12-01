package org.rpg.menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.rpg.character.Party;
import org.rpg.character.Player;

// TODO: GameFile does not work...
public class GameFile implements Serializable {

	private static final long serialVersionUID = 3792545087701375132L;
	
	public static void saveEntireParty(Party party) {
		System.out.println("Before saving....");
		  for(Map.Entry<Integer, Player> entry : party.getParty().entrySet()) {
			  entry.getValue().printStats();  
		   }
	    
		
		// first save party object
		/*try {
	         FileOutputStream fileOut =
	         new FileOutputStream("party.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(party);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved as party.ser in current directory." + "\n");
	      } catch (IOException i) {
	         i.printStackTrace();
	      }*/
		
		
		// then for each player in party, save it to a file
		/*for(int i = 1; i <= party.getParty().size(); i++) {
			try {
		         FileOutputStream fileOut =
		         new FileOutputStream(i + ".ser");
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(party.getParty().get("p" + i));
		         out.close();
		         fileOut.close();
		         System.out.printf("Serialized data is saved as " + i 
		        		 + ".ser in current directory." + "\n");
		      } catch (IOException io) {
		         io.printStackTrace();
		      }
		}*/
		
		// save Map object 
		  try
	        {
	            FileOutputStream fos = new FileOutputStream("partyData");
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            oos.writeObject(party.getParty());
	            oos.close();
	            fos.close();
	        }
	        catch (IOException ioe)
	        {
	            ioe.printStackTrace();
	        }
		  
		  System.out.println("After saving....");
		  for(Map.Entry<Integer, Player> entry : party.getParty().entrySet()) {
 			  entry.getValue().printStats();  
 		   }
	    }
		
	
	public static Party loadEntireParty() {
	   // delete party object and players
 	   // we will atttempt to load it from file here
		  /*for(int i = 1; i <= party.getParty().size(); i++) {
			  party.getParty().put("p" + i, null);
		  }*/
		  Party loadedParty = new Party();
		  
		  System.out.println("BEFORE loading, loadedParty contains:");
		  for(Map.Entry<Integer, Player> entry : loadedParty.getParty().entrySet()) {
			  System.out.println(entry.getKey());
			  entry.getValue().printStats(); 
			  
		  }
		  
		  Map<Integer, Player> party = new TreeMap();
		  
		  System.out.println("Before loading:");
		  // iterate through map loaded from file
		  for(Map.Entry<Integer, Player> _entry : party.entrySet()) {
			  System.out.println(_entry.getKey());
			  _entry.getValue().printStats(); 
		  }
			  
		  try
	        {
	            FileInputStream fis = new FileInputStream("partyData");
	            ObjectInputStream ois = new ObjectInputStream(fis);
	 
	            party = (Map<Integer, Player>) ois.readObject();
	 
	            ois.close();
	            fis.close();
	        }
	        catch (IOException ioe)
	        {
	            ioe.printStackTrace();
	           // return;
	        }
	        catch (ClassNotFoundException c)
	        {
	            System.out.println("Class not found");
	            c.printStackTrace();
	            //return;
	        }
		  
		  System.out.println("Printing Map<String, Player> party loaded from file:");
		  System.out.println(party);
		  
		  System.out.println("After loading:");
		  // iterate through map loaded from file
		  for(Map.Entry<Integer, Player> entry : party.entrySet()) {
			  System.out.println(entry.getKey());
			  entry.getValue().printStats(); 
			  System.out.println("player positons are: ");
			  System.out.println(entry.getValue().getXpos());
			  System.out.println(entry.getValue().getYpos());
			  
			  
		  }
		  
		  // add map of party to loadedParty
		  loadedParty.setParty(party);
		  
		  System.out.println("After loading, loadedParty contains:");
		  // iterate through map loaded from file
		  for(Map.Entry<Integer, Player> entry : loadedParty.getParty().entrySet()) {
			  System.out.println(entry.getKey());
			  entry.getValue().printStats(); 
			  
		  }
		  
		  
		  // then load each player
	 	     /*for(int i = 1; i <= party.size(); i++) {
	 	    	
	 	    	 Player player = null;
	 	    	 try {
	 	 	         FileInputStream fileIn = new FileInputStream(i + ".ser");
	 	 	         ObjectInputStream in = new ObjectInputStream(fileIn);
	 	 	         player = (Player) in.readObject();
	 	 	         in.close();
	 	 	         fileIn.close();
	 	 	      } catch (IOException io) {
	 	 	         io.printStackTrace();
	 	 	      } catch (ClassNotFoundException c) {
	 	 	         System.out.println( "player"+ i + " class not found");
	 	 	         c.printStackTrace();
	 	 	      } 
	 	    	 
	 	    	 System.out.println("p" + i + " name is " + player.getName());
		 
 	    	
 	    	 }*/
	 	     
	 		  return loadedParty;
 	 
 	     
 	     
 	     
 	     
 	     
 	    

	}
	
	


}

