package berlin.strategy.starter;

import java.util.Random;

import berlin.game.GameState;
import berlin.game.Node;
import berlin.game.Strategy;
import berlin.game.StrategyFactory;

public class AllInAllTheTimeFactory implements StrategyFactory {

	private final Random random = new Random();
	
	@Override
	public Strategy pickStrategy(GameState gameState) {
		return new AllInAllTheTimeStrategy(gameState);
	}
	
	private class AllInAllTheTimeStrategy implements Strategy {
		
		private final GameState gameState;
		
		public AllInAllTheTimeStrategy(GameState gameState) {
			super();
			this.gameState = gameState;
		}

		@Override
		public void move() {
			for (Node playerNode : gameState.getPlayerNodes()) {
				int troopsLeft = playerNode.getNumberOfSolders();

				Iterable<Node> placesToGo = playerNode.getOutboundNeighbours();

				for (Node destination : placesToGo) {
					if (troopsLeft <= 0) {
						break;
					}
					
					int moving = 0;
					
					if (destination.getNumberOfSolders() < troopsLeft) {
						moving = troopsLeft - destination.getNumberOfSolders();
					}

					gameState.moveTroops(playerNode, destination, moving);
				}
			}
		}
	}

}
