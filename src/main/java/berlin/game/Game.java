package berlin.game;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import berlin.action.request.GameRequest;
import berlin.action.response.GameResponse;

public class Game {

	private static final Logger logger = LoggerFactory.getLogger(Game.class);
	
	private final Date startedAt = new Date();
	
	private final GameState gameState;
	private final Strategy strategy;
	
	private GameRequest gameStartRequest;	
	private GameRequest gameOverRequest;

	private final List<GameRequest> turnRequests = new ArrayList<>();

	public Game(GameRequest gameRequest, StrategyFactory strategyFactory) {
		gameStartRequest = gameRequest;
		
		this.gameState = new GameState(gameRequest);		
		this.strategy = strategyFactory.pickStrategy(gameState);
		logger.info("New game created with id {}", gameRequest.getGameId());
	}

	public GameResponse turn(GameRequest gameRequest) {
		logger.info("Turn {} of {}", gameRequest.getCurrentTurn(), gameRequest.getMaximumTurns());
		turnRequests.add(gameRequest);
		
		gameState.updateState(gameRequest.getStates());
		strategy.move();
		
		return gameState.getMoves();
	}
	
	public void gameOver(GameRequest gameRequest) {		
		gameOverRequest = gameRequest;
		gameState.gameOver(gameRequest.getStates());
		logger.info("Game over");
	}
	
	public boolean isGameOver() {
		return gameOverRequest != null;
	}

	public Date getStartedAt() {
		return startedAt;
	}
	
	public void statusReport(PrintWriter writer) {
		
		// Game has ended and graph shut down
		if (gameOverRequest != null) {
			if (gameStartRequest != null) {
				writer.print("[GAME START]: " + gameStartRequest + "\n");
			}

			for (GameRequest request : turnRequests) {
				writer.print("[TURN " + request.getCurrentTurn() + "]: "
						+ request.getStates() + "\n");
			}
			writer.print("[GAME OVER]: " + gameOverRequest.getStates() + "\n");
		}
		else {
			for (Node node : gameState.getNodesSortedById()) {
				writer.print("Node " + node.getNodeId() + "\n");
				writer.print("  type: " + node.getNodeType() + "\n");
				writer.print("  vp worth: " + node.getVictoryPointWorth() + "\n");
				writer.print("  solders per turn: " + node.getSoldiersGrantedPerTurn() + "\n");
				writer.print("  owner: " + node.getOwner() + "\n");
				writer.print("  soldiers present: " + node.getNumberOfSolders() + "\n");
				writer.print("  inbound neighbouring nodes:  ");
				for (Node neighbour: node.getInboundNeighbours()) {
					writer.print(neighbour.getNodeId() + " ");
				}
				writer.print("\n");
				writer.print("  outbound neighbouring nodes: ");
				for (Node neighbour: node.getOutboundNeighbours()) {
					writer.print(neighbour.getNodeId() + " ");
				}
				writer.print("\n");
			}
		}
	}
}
