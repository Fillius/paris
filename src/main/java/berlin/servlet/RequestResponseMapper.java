package berlin.servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import berlin.action.request.Action;
import berlin.action.request.GameMap;
import berlin.action.request.GameRequest;
import berlin.action.request.Infos;
import berlin.action.request.NodeState;
import berlin.action.response.GameResponse;

public class RequestResponseMapper {

	private ObjectMapper mapper = new ObjectMapper();
	
	private final boolean stateless;

	/**
	 * 
	 * Maps JSON from request and to response strings 
	 * 
	 * @param stateless - true if we should always parse the game map and parameters
	 */
	protected RequestResponseMapper(boolean stateless) {
		super();
		this.stateless = stateless;
	}
	
	public static RequestResponseMapper statelessRequestResponseMapper() {
		return new RequestResponseMapper(true);
	}
	
	public static RequestResponseMapper statefulRequestResponseMapper() {
		return new RequestResponseMapper(false);
	}

	public GameRequest mapRequest(long startNanoTime, String actionParameter, String infos, String map, String state)
			throws JsonMappingException, JsonParseException, IOException {
		
		Action action = Action.fromRequestString(actionParameter);
		
		Infos info = mapper.readValue(infos, Infos.class);
		GameMap gameMap	= null;
		if (stateless || action == Action.GAME_START) {
			
			gameMap = mapper.readValue(map, GameMap.class);
		}
		List<NodeState> states = mapper.readValue(state, mapper.getTypeFactory().constructCollectionType(List.class, NodeState.class));
		
		return new GameRequest(
				startNanoTime,
				action,
				info,
				gameMap,
				states);				
	}
	
	public String mapResponse(GameResponse gameResponse) throws JsonProcessingException {
		return mapper.writeValueAsString(gameResponse.getMoves());
	}
}
