package business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class Helper {
	public static List<Tile> createTiles(int numberOfTiles,int numberOfType) {
		List<Tile> tiles=new ArrayList<>(numberOfTiles);
	    for(int i=0;i<=1;i++) {
	    	for(int j=1;j<=numberOfType;j++) {
	    		Tile tile;
		    	if(j<=13) {
		    		tile=new Tile(Color.YELLOW,j,false);
		    	}
		    	else if(j>=14 && j<=26) {
		    		tile=new Tile(Color.BLUE,j-13,false);
		    	}
		    	else if(j>=27 && j<=39) {
		    		tile=new Tile(Color.BLACK,j-26,false);
		    	}
		    	else if(j>=40 && j<=52) {
		    		tile=new Tile(Color.RED,j-39,false);
		    	}
		    	else {
		    		tile=new Tile(Color.FALSE_JOKER,j-1,true);
		    	}
		    	tiles.add(tile);
		    }
	    }
	    return tiles;
	}
	public static void distributeTilesToPlayers(List<Tile> tiles,Player[] players) {
		for(int i=0;i<players.length;i++) { //player4 takes 15 tiles,other players takes 14 tiles
			if(i!=3) 
				distributeTiles(tiles,players[i],14);
			else 
				distributeTiles(tiles,players[i],15);
		}
		
	}
	public static void detectJoker(Player[] players,Tile joker) {
		for(Player p:players) {
	    	if(p.getTiles().removeIf((Tile ts) ->ts.getColor() == joker.getColor() && (int)ts.getNumber()==(int)joker.getNumber())) { 
				p.setNumberOfJoker(1); //determines the jokers in the player's hand
			}
	    }
	}
	public static void convertFalseJokersToTiles(Player[] players,Tile joker) {
		for(Player player:players) {
			falseJokerToTile(player,joker);
		}
	}
	public static int calculateRemainingTiles(Player player) { // returns minimum unused tiles for optimum tile laying
		 return Math.min(calculateForPair(player),calculateForOrdinary(player));
	}
	public static Player tileToFalseJoker(Player player, Tile joker) { //false joker turned into tile back becomes false joker to show the player's tile
		for(int i=0;i<player.getTiles().size();i++) {
			if(player.getTiles().removeIf((Tile os) ->os.getColor() == joker.getColor() && os.getNumber()==joker.getNumber())) {
				Tile fJoker=new Tile(Color.FALSE_JOKER,52,true);
				player.getTiles().add(fJoker);
			}
		}
		return player;
	}
	public static Player addJokerToList(Player player, Tile joker,int numberOfOkey) { //joker added to show player's tile
		for(int i=0;i<numberOfOkey;i++)
			player.getTiles().add(joker);
		return player;
	}
	public static int getMinValue(int[] numbers){ //returns minimum value in array to choose proper tile laying
		  int minValue = numbers[0];
		  for(int i=1;i<numbers.length;i++){
		    if(numbers[i] < minValue)
		      minValue = numbers[i];
		  }
		  return minValue;
	}
	private static Player falseJokerToTile(Player player,Tile joker) { // turns the false joker in the player's hand into the appropriate tiles
		for(int i=0;i<player.getTiles().size();i++) {
			if(player.getTiles().removeIf((Tile os) ->os.getColor() == Color.FALSE_JOKER && os.getNumber()==52 && os.isFalseJoker()))
				player.getTiles().add(joker);
		}
		return player;
	}
	private static void distributeTiles(List<Tile> tiles,Player player,int num) { //distribute tiles to players randomly
		List<Tile> playerTiles=new ArrayList<Tile>();
		Random random = new Random();
		for(int k=0;k<num;k++) {
			int randomNumber=random.nextInt(tiles.size());
			playerTiles.add(tiles.get(randomNumber));
			tiles.remove(randomNumber);
		}
		player.setTiles(playerTiles);
	}
	private static Map<String, List<Tile>> combineSingleTile(List<List<Tile>> arr) { //identify used and unused tiles
		Map<String, List<Tile>> map = new HashMap<String, List<Tile>>();
		List<Tile> singleList=new ArrayList<Tile>();
		List<Tile> multipleList=new ArrayList<Tile>();
		for(List<Tile> listOfTile:arr){
			if(listOfTile.size()==1) {
				singleList.add(listOfTile.get(0));	
			}
			else {
				for(Tile st:listOfTile) 
					multipleList.add(st);
			}
		}
		map.put("isDone", multipleList);
		map.put("isSingle", singleList);
		return map;
	}
	private static List<List<Tile>> combineSameColor(List<Tile> arr){ 
		List<List<Tile>> editedTiles=new ArrayList<List<Tile>>();
		boolean visited[] = new boolean[arr.size()]; 
        for (int i = 0; i < arr.size(); i++)
            visited[i] = false; 
        for (int i = 0; i < arr.size(); i++){ 
        	List<Tile> numberList=new ArrayList<Tile>();
            if (!visited[i]){
            	numberList.add(arr.get(i));
                for (int j = i + 1; j < arr.size(); j++){ 
                    if (arr.get(i).getColor().name().equals(arr.get(j).getColor().name())){ 
                    	numberList.add(arr.get(j));
                        visited[j] = true; 
                    } 
                }
            	editedTiles.add(numberList);
            } 
        }
        return editedTiles;
	}
	private static List<List<Tile>> convertListForConsecutiveNumbers(List<Tile> arr){ //groups 3, 4 and 5 consecutive tiles of the same color.
		List<List<Tile>> numberList = new ArrayList<>(); //Contains consecutively grouped tiles of the same color
        List<Tile> temp = new ArrayList<>();
        temp.add(arr.get(0));
        for (int i = 0; i < arr.size() - 1; i++) {
            if (arr.get(i + 1).getNumber() == arr.get(i).getNumber() + 1) {
                temp.add(arr.get(i + 1));
            } 
            else {
            	numberList.add(temp);
                temp = new ArrayList<>();
                temp.add(arr.get(i+1));
            }
        }
        numberList.add(temp);
        return ignoreTwoTiles(numberList);
       
	}
	private static List<List<Tile>> ignoreTwoTiles(List<List<Tile>> numberList){
		List<List<Tile>> newList = new ArrayList<>();
        Iterator<List<Tile>> iter=numberList.iterator();
        while(iter.hasNext()) {                     //ignored if grouped list has 2 tiles
        	List<Tile> list = iter.next();
        	if(list.size()==2) {
        		List<Tile> list1=new ArrayList<Tile>();
        		list1.add(list.get(0));
        		List<Tile> list2=new ArrayList<Tile>();
        		list2.add(list.get(1));
        		newList.add(list1);
        		newList.add(list2);
        	}
        	else {
        		newList.add(list);
        	}
        }
        return newList;
	}
	private static List<List<Tile>> convertListForTwoConsecutiveNumbers(List<Tile> arr){
		List<List<Tile>> numberList = new ArrayList<>();
        List<Tile> temp = new ArrayList<>();
        temp.add(arr.get(0));
        for (int i = 0; i < arr.size() - 1; i++) {
            if (arr.get(i + 1).getNumber() == arr.get(i).getNumber() + 1) {
                temp.add(arr.get(i + 1));
            } 
            else {
            	numberList.add(temp);
                temp = new ArrayList<>();
                temp.add(arr.get(i+1));
            }
        }
        numberList.add(temp);
		return numberList;
	}
	private static List<List<Tile>> combineSameTile(List<Tile> arr){ //combine the same tiles in the players's hand
		List<List<Tile>> editedTiles=new ArrayList<List<Tile>>();
		boolean visited[] = new boolean[arr.size()]; 
        for (int i = 0; i < arr.size(); i++)
            visited[i] = false; 
        for (int i = 0; i < arr.size(); i++){ 
        	List<Tile> numberList=new ArrayList<Tile>();
            if (!visited[i]){
            	numberList.add(arr.get(i));
                for (int j = i + 1; j < arr.size(); j++){ 
                    if (arr.get(i).getColor().name().equals(arr.get(j).getColor().name()) && (int)arr.get(i).getNumber() == (int)arr.get(j).getNumber()){
                    	numberList.add(arr.get(j));
                        visited[j] = true; 
                    } 
                }
            	editedTiles.add(numberList);
            } 
        }
        return editedTiles;
	}
	private static int calculateForPair(Player player) { //calculates the number of unused tiles that do not have a pair
		List<List<Tile>> sc=combineSameTile(player.getTiles()); 
		int unusedNumber=0;
		for(List<Tile> tiles:sc) {
			if(tiles.size()==1)  
				unusedNumber++;
		}
		return unusedNumber;
	}
	private static int calculateForOrdinary(Player player) {
		List<Tile> multipleTileList=new ArrayList<Tile>(); //indicates used tiles
		List<Tile> singleTileList=new ArrayList<Tile>(); //indicates unused tiles
		toDoForSameColor(singleTileList,multipleTileList,player);
		toDoForSameNumber(singleTileList,multipleTileList);
        putJokerToAppropriatePosition(multipleTileList,singleTileList,player.getNumberOfJoker());  
        //after using the tiles properly, the number of unused tiles will be returned
        return singleTileList.size();
	}
	private static void toDoForSameColor(List<Tile> singleTileList,List<Tile> multipleTileList,Player player) {
		List<List<Tile>> sameColorTiles=combineSameColor(player.getTiles()); //tiles of the same color are combined first
		for(List<Tile> s:sameColorTiles) { 
			s.sort(Comparator.comparingDouble(Tile::getNumber)); // sort tiles to combine consecutive numbers
			List<List<Tile>> consecutiveList=convertListForConsecutiveNumbers(s); 
			Map<String, List<Tile>> updatedList = combineSingleTile(consecutiveList);
			Iterator<Entry<String, List<Tile>>> iterator = updatedList.entrySet().iterator();
	        while (iterator.hasNext()) {
	             Entry<String, List<Tile>> me2 = iterator.next();
	             if(me2.getKey()=="isSingle") {
	            	 for(Tile tile:me2.getValue()) {
	            		 singleTileList.add(tile);
	            	 }
	             }
	             if(me2.getKey()=="isDone" && me2.getValue().size()!=0) {
	            	 for(Tile tile:me2.getValue()) {
	 	            	multipleTileList.add(tile);

	            	 }
	             }
	             
	        }
		}	
	}
	private static void toDoForSameNumber(List<Tile> singleTileList,List<Tile> multipleTileList) {
		List<List<Tile>> sameNumberList=convertListForSameNumbers(singleTileList);
		Map<String, List<Tile>> updatedList2 = combineSingleTile(sameNumberList);
		Iterator<Entry<String, List<Tile>>> iterator2 = updatedList2.entrySet().iterator();
        while (iterator2.hasNext()) {
             Entry<String, List<Tile>> me3 = iterator2.next();
             if(me3.getKey()=="isDone" && me3.getValue().size()!=0) {
            	 for(Tile tile:me3.getValue()) {
	 	            	multipleTileList.add(tile);
	 	            	singleTileList.remove(tile);
	            	 }
             }
        }
	}
	private static void putJokerToAppropriatePosition(List<Tile> multipleTileList,List<Tile> singleTileList, int numberOfJoker) {
		//first consider the possibility of creating 4/5 tiles and places it if player has joker
		int jokerNum=checkForConsecutiveFiveOrFourTiles(multipleTileList,singleTileList,numberOfJoker); 
		// consider the possibility of creating 3 tiles and places it if player has joker
        if (jokerNum>0)                                                                      
    		jokerNum=checkForConsecutiveThreeTiles(multipleTileList,singleTileList,jokerNum);
        if(jokerNum>0)
        	jokerNum=checkForThreeTilesWithSameNumber(multipleTileList,singleTileList,jokerNum);
	}
	private static int checkForThreeTilesWithSameNumber(List<Tile> multipleTileList,List<Tile> singleTileList, int numberOfJoker) {
		List<List<Tile>> cc=convertListForSameTwoNumbers(singleTileList); 
		for(int i=0;i<cc.size();i++) {
			if(numberOfJoker>0) {
				if(cc.get(i).size()==2) { //joker is used there if the players has tiles such as 4(YELLOW) and 4(BLUE)
					numberOfJoker--;
					for(Tile tileS:cc.get(i)) {
						multipleTileList.add(tileS);
						singleTileList.remove(tileS);
					}						
				}
			
			}	
		}
		return numberOfJoker;
	}
	private static List<List<Tile>> convertListForSameTwoNumbers(List<Tile> arr) { //combines different tiles of same color
		List<List<Tile>> editedTiles=new ArrayList<List<Tile>>();
		boolean visited[] = new boolean[arr.size()]; 
        for (int i = 0; i < arr.size(); i++)
            visited[i] = false; 
        for (int i = 0; i < arr.size(); i++){ 
        	List<Tile> numberList=new ArrayList<Tile>();
            if (!visited[i]){
            	numberList.add(arr.get(i));
                for (int j = i + 1; j < arr.size(); j++){ 
                    if(arr.get(i).getNumber()==arr.get(j).getNumber() && arr.get(i).getColor()!=arr.get(j).getColor()){ 
                    	numberList.add(arr.get(j));                  
                        visited[j] = true; 
                    } 
                }
            	editedTiles.add(numberList);
            } 
        }
        
        return editedTiles;
	}
	private static int checkForConsecutiveThreeTiles(List<Tile> multipleTileList,List<Tile> singleTileList, int numberOfJoker) {
		List<List<Tile>> sc = combineSameColor(singleTileList);
		for(List<Tile> s:sc) {
			s.sort(Comparator.comparingDouble(Tile::getNumber));
			List<List<Tile>> cc=convertListForTwoConsecutiveNumbers(s);
			for(int i=0;i<cc.size();i++) {
				if(numberOfJoker>0) { //joker is used there if the players has tiles such as 3-5 or 4-6
					if(cc.get(i).size()==2) {
						numberOfJoker--;
						for(Tile tileS:cc.get(i)) {
							multipleTileList.add(tileS);
							singleTileList.remove(tileS);
						}						
					}
				
				}	
			}
		}
		return numberOfJoker;
	}
	private static int checkForConsecutiveFiveOrFourTiles(List<Tile> multipleTileList,List<Tile> singleTileList, int numberOfJoker) {
		List<List<Tile>> sc = combineSameColor(singleTileList);
		for(List<Tile> s:sc) {
			s.sort(Comparator.comparingDouble(Tile::getNumber));
			List<List<Tile>> cc=convertListForTwoConsecutiveNumbers(s);
			for(int i=0;i<cc.size()-1;i++) {
				if(numberOfJoker>0) { //joker is used there if the players has tiles such as 3-5-6 or 4-5-7-8
					if(cc.get(i).get(cc.get(i).size() - 1).getNumber()+2 == cc.get(i+1).get(0).getNumber() && (cc.get(i).size()==2 || cc.get(i+1).size()==2)){
						for(Tile tileS:cc.get(i)) {
							multipleTileList.add(tileS);
							singleTileList.remove(tileS);
						}
						for(Tile tileS:cc.get(i+1)) {
							multipleTileList.add(tileS);
							singleTileList.remove(tileS);
						}
						numberOfJoker--;
					}
				}
			}
		}
		return numberOfJoker;
	
	}
	private static List<List<Tile>> convertListForSameNumbers(List<Tile> arr) { //groups 3, 4 and 5  tiles of the different color.
		List<List<Tile>> editedTiles=new ArrayList<List<Tile>>(); //contains grouped tiles of the different color
		boolean visited[] = new boolean[arr.size()]; 
        for (int i = 0; i < arr.size(); i++)
            visited[i] = false; 
        for (int i = 0; i < arr.size(); i++){ 
        	List<Tile> numberList=new ArrayList<Tile>();
            if (!visited[i]){
            	numberList.add(arr.get(i));
                for (int j = i + 1; j < arr.size(); j++){ 
                    if(arr.get(i).getNumber()==arr.get(j).getNumber() && arr.get(i).getColor()!=arr.get(j).getColor()){ 
                    	numberList.add(arr.get(j));                  
                        visited[j] = true; 
                    } 
                }
            	editedTiles.add(numberList);
            } 
        }
        return ignoreTwoTiles(editedTiles);
	}
	public static Tile returnIndicator(List<Tile> tiles) {
		Random random = new Random();
		int randomNumber=random.nextInt(tiles.size());
		Tile indicator=tiles.get(randomNumber);
		tiles.remove(randomNumber);
		return indicator;
	}
	public static Tile returnJoker(Tile indicator) {
		Tile joker;
		if(indicator.getNumber()==52 && indicator.getColor().equals(Color.FALSE_JOKER))
			joker=new Tile(indicator.getColor(),52,true);
		else if(indicator.getNumber()==13)
			joker=new Tile(indicator.getColor(),1,false);
		else
			joker=new Tile(indicator.getColor(),indicator.getNumber()+1,false);
		return joker;
	}
}
