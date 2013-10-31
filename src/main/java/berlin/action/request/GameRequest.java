package berlin.action.request;

import java.util.List;

public class GameRequest {
	
	private final long startNanoTime;

	private final Action action;
	
	private final Infos infos;
	
	private final GameMap gameMap;
	
	private final List<NodeState> states;

	public GameRequest(long startNanoTime, Action action, Infos infos, GameMap gameMap,
			List<NodeState> states) {
		super();
		this.startNanoTime = startNanoTime;
		this.action = action;
		this.infos = infos;
		this.gameMap = gameMap;
		this.states = states;
	}

	public long getStartNanoTime() {
		return startNanoTime;
	}

	public Action getAction() {
		return action;
	}

	public Infos getInfos() {
		return infos;
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	public List<NodeState> getStates() {
		return states;
	}

	public String getGameId() {
		return infos.getGameId();
	}

	public int getCurrentTurn() {
		return infos.getCurrentTurn();
	}

	public int getMaximumTurns() {
		return infos.getMaximumTurns();
	}

	public int getNumberOfPlayers() {
		return infos.getNumberOfPlayers();
	}

	public int getTimeLimitPerTerm() {
		return infos.getTimeLimitPerTerm();
	}

	public int getPlayerId() {
		return infos.getPlayerId();
	}
	
	public boolean isDirected() {
		return infos.isDirected();
	}

	@Override
	public String toString() {
		return "GameRequest [action=" + action + ", infos=" + infos
				+ ", gameMap=" + gameMap + ", states=" + states + "]";
	}
	
}
