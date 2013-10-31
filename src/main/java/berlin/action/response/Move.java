package berlin.action.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Move {

	@JsonProperty("from")
	private int from;
	
	@JsonProperty("to")
	private int to;
	
	@JsonProperty("number_of_soldiers")
	private int numberOfSoldiers;

	public Move(int from, int to, int numberOfSoldiers) {
		super();
		this.from = from;
		this.to = to;
		this.numberOfSoldiers = numberOfSoldiers;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getNumberOfSoldiers() {
		return numberOfSoldiers;
	}

	public void setNumberOfSoldiers(int numberOfSoldiers) {
		this.numberOfSoldiers = numberOfSoldiers;
	}

	@Override
	public String toString() {
		return "Move [" +
			numberOfSoldiers + " soldiers from #" + from + " to #" + to +
					"]";
	}
}
