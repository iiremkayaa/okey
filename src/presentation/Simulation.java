package presentation;

import java.util.List;

import business.Helper;
import business.Player;
import business.Tile;

public class Simulation {
	final static int number_of_tiles = 106;
	final static int number_of_type = 53;
	public void run() {
		List<Tile> tiles = Helper.createTiles(number_of_tiles,number_of_type);
	    Tile indicator=Helper.returnIndicator(tiles);
	    Tile joker=Helper.returnJoker(indicator);
	    System.out.println("Joker: "+joker);
	    Player[] players=new Player[4];
	    for(int i=0;i<4;i++) 
	    	players[i]=new Player();
	    
	    Helper.distributeTilesToPlayers(tiles,players);
	    
	    Helper.detectJoker(players, joker);	    

	    Helper.convertFalseJokersToTiles(players,joker);
	
	    int[] numberOfUnusedTiles=new int[4];
	    for(int j=0;j<numberOfUnusedTiles.length;j++)
	    	numberOfUnusedTiles[j]=Helper.calculateRemainingTiles(players[j]);
	    int bestCase=Helper.getMinValue(numberOfUnusedTiles); //The player with a minimum of unused tiles wins
	    
	    for(int j=0;j<numberOfUnusedTiles.length;j++) 
	    	players[j]=Helper.tileToFalseJoker(players[j],joker);
	    for(int k=0;k<numberOfUnusedTiles.length;k++) {
	    	if(numberOfUnusedTiles[k]==bestCase) {
	    		System.out.println("\nPlayer"+(k+1)+" won!");
	    		System.out.println("Tiles: "+players[k].getTiles());
	    		if(players[k].getNumberOfJoker()>0)
		    		System.out.println("Also "+"Player"+(k+1)+" has "+players[k].getNumberOfJoker()+" joker:" + joker);
	    		System.out.println("There are " +numberOfUnusedTiles[k]+" unused tiles.\n");
	    	}
	    }
	}
}