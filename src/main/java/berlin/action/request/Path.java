package berlin.action.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Path {

	@JsonProperty("from")
	private int from;
	
	@JsonProperty("to")
	private int to;

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

	@Override
	public String toString() {
		return "path[" + from + " to " + to + "]";
	}
	
	
}
