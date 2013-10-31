package berlin.action.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeType {

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("points")
	private int victoryPointWorth;
	
	@JsonProperty("soldiers_per_turn")
	private int soldiersGrantedPerTurn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVictoryPointWorth() {
		return victoryPointWorth;
	}

	public void setVictoryPointWorth(int victoryPointWorth) {
		this.victoryPointWorth = victoryPointWorth;
	}

	public int getSoldiersGrantedPerTurn() {
		return soldiersGrantedPerTurn;
	}

	public void setSoldiersGrantedPerTurn(int soldiersGrantedPerTurn) {
		this.soldiersGrantedPerTurn = soldiersGrantedPerTurn;
	}

	@Override
	public String toString() {
		return "NodeType [name=" + name + ", victoryPointWorth="
				+ victoryPointWorth + ", soldiersGrantedPerTurn="
				+ soldiersGrantedPerTurn + "]";
	}

}
