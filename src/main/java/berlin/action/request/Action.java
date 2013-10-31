package berlin.action.request;

/**
 * 	private static final String PING = ;
	private static final String TURN = ;
	private static final String GAME_START = ;
	private static final String GAME_OVER = ;
 * @author rob
 *
 */

public enum Action {

	PING("ping"),
	TURN("turn"),
	GAME_START("game_start"),
	GAME_OVER("game_over");
	
	private final String requestName;
	
	Action(String requestName) {
		this.requestName = requestName;
	}
	
	public static Action fromRequestString(String action) {
		for(Action a : Action.values()) {
			if (a.requestName.equals(action)) {
				return a;
			}
		}
		// default to PING:
		return PING;
 	}
	
	@Override
	public String toString() {
		return requestName;
	}
}
