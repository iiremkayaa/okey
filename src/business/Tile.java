package business;


public class Tile {
	private Color color;
	private int number;
	private boolean isFalseJoker;
	public Tile(Color color, int number,boolean isFalseJoker) {
		super();
		this.color = color;
		this.number = number;
		this.isFalseJoker=isFalseJoker;
	}
	public Tile(boolean isFalseJoker) {
		super();
		this.isFalseJoker=isFalseJoker;
		
	}
	public boolean isFalseJoker() {
		return isFalseJoker;
	}

	public void setFalseJoker(boolean isFalseJoker) {
		this.isFalseJoker = isFalseJoker;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public String toString() {
		if(isFalseJoker==true)
			return " " + color +" ";
		return " " + color + number +" ";	
	}
}
