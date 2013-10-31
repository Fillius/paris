package berlin.action.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeState {

	@JsonProperty("node_id")
	private int nodeId;
	
	@JsonProperty("player_id")
	private Integer playerId;
	
	@JsonProperty("number_of_soldiers")
	private int numberOfSolders;

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public int getNumberOfSolders() {
		return numberOfSolders;
	}

	public void setNumberOfSolders(int numberOfSolders) {
		this.numberOfSolders = numberOfSolders;
	}

	@Override
	public String toString() {
		return "(" + nodeId + (playerId == null ? ", neutral" : (", owned by " + playerId)) +
				", soldiers " + numberOfSolders + ")";
	}
	
}
