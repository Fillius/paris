package berlin.action.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * "infos": {
    "game_id": "7c7905c6-2423-4a91-b5e7-44ff10cddd5d", // Unique ID of the game
    "current_turn": null,	                           // The current turn number
    "maximum_number_of_turns": 10,                     // After this number of turns, the game ends
    "number_of_players": 2,                            // Total number of AIs in the game
    "time_limit_per_turn": 5000,                       // Time allowed before the turn times out
    "directed": false,                                 // Indicated that the paths are one-way only
    "player_id": 1                                     // The ID of your AI for this game
}
 * @author rob.platt
 *
 */
public class Infos {

	@JsonProperty("game_id")
	private String gameId;
	
	@JsonProperty("current_turn")
	private Integer currentTurn;
	
	@JsonProperty("maximum_number_of_turns")
	private int maximumTurns;
	
	@JsonProperty("number_of_players")
	private int numberOfPlayers;
	
	@JsonProperty("time_limit_per_turn")
	private int timeLimitPerTerm;
	
	@JsonProperty("directed")
	private boolean directed;
	
	@JsonProperty("player_id")
	private int playerId;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Integer getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(Integer currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getMaximumTurns() {
		return maximumTurns;
	}

	public void setMaximumTurns(int maximumTurns) {
		this.maximumTurns = maximumTurns;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public int getTimeLimitPerTerm() {
		return timeLimitPerTerm;
	}

	public void setTimeLimitPerTerm(int timeLimitPerTerm) {
		this.timeLimitPerTerm = timeLimitPerTerm;
	}

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		return "Infos [gameId=" + gameId + ", currentTurn=" + currentTurn
				+ ", maximumTurns=" + maximumTurns + ", numberOfPlayers="
				+ numberOfPlayers + ", timeLimitPerTerm=" + timeLimitPerTerm
				+ ", directed=" + directed + ", playerId=" + playerId + "]";
	}

}
