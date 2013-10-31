package berlin.game;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import berlin.action.request.GameRequest;
import berlin.action.response.GameResponse;

public class GameLibrary {

	private final LinkedHashMap<String, Game> games = new LinkedHashMap<>();
	
	private final int sizeLimit = 5;
	
	private final StrategyFactory strategyFactory;
	
	private int gamesPlayed = 0;

	public GameLibrary(StrategyFactory strategyFactory) {
		super();
		this.strategyFactory = strategyFactory;
	}

	public GameResponse update(GameRequest gameRequest) {
		
		Game game = null;
		if (gameRequest.getGameId() != null) {
			game = games.get(gameRequest.getGameId());
		}
		
		switch (gameRequest.getAction()) {	
		
		case GAME_OVER:			
			if (game != null) {
				game.gameOver(gameRequest);
				gamesPlayed++;
			}
			return null;
		
		case GAME_START:
			if (games.size() == sizeLimit) {
				games.remove(games.keySet().iterator().next());
			}
			games.put(gameRequest.getGameId(), new Game(gameRequest, strategyFactory));			
			return null;
			
		case TURN:
			return game.turn(gameRequest);

		case PING:
		default:
			return null;
		}
	}
	
	public void statusReport(PrintWriter writer) {
		writer.print(gamesPlayed + " games played since startup\n\n");		
		for (Entry<String, Game> libraryEntry : games.entrySet()) {
			libraryEntry.getValue().statusReport(writer);			
			writer.print("\n");
		}
	}
}
