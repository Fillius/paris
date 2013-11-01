package berlin.strategy.starter;

import java.util.List;
import java.util.Random;

import berlin.game.GameState;
import berlin.game.Node;
import berlin.game.Strategy;
import berlin.game.StrategyFactory;

import com.google.common.collect.Lists;

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

				List<Node> placesToGo = Lists.newArrayList(playerNode.getOutboundNeighbours());
				
				int nodeAverage = troopsLeft / placesToGo.size();

				for (Node destination : placesToGo) {
					if (troopsLeft <= 0) {
						break;
					}
					
					gameState.moveTroops(playerNode, destination, nodeAverage);
				}
			}
		}
	}

}
