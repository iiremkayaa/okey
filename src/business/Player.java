package business;

import java.util.List;


public class Player {
	private List<Tile> tiles;
	private int numberOfJoker;
	public Player() {
		
	}
	public Player(List<Tile> tiles) {
		this.tiles = tiles;
		this.numberOfJoker=0;
	}
	public int getNumberOfJoker() {
		return numberOfJoker;
	}
	public void setNumberOfJoker(int numberOfJoker) {
		this.numberOfJoker+=1;
	}
	@Override
	public String toString() {
		return "Player [tiles=" + tiles + ", numberOfJoker=" + numberOfJoker + "]";
	}
	public List<Tile> getTiles() {
		return tiles;
	}
	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}
}