package berlin.strategy.starter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import berlin.game.GameState;
import berlin.game.Node;
import berlin.game.Strategy;
import berlin.game.StrategyFactory;

import com.google.common.collect.Lists;

public class AllInAllTheTimeFactory implements StrategyFactory {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

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
		
		private boolean myNode(Node node) {
			return node.getOwner() == gameState.getPlayerId();
		}

		@Override
		public void move() {
			for (Node playerNode : gameState.getPlayerNodes()) {
				List<Node> placesToGo = Lists.newArrayList(playerNode.getOutboundNeighbours());
				
				Collections.sort(placesToGo, new Comparator<Node>() {
					public int compare(Node o1, Node o2) {
						if (myNode(o1) && myNode(o2)) {
							return new BigDecimal(o2.getNumberOfSolders()).compareTo(
									new BigDecimal(o1.getNumberOfSolders()));
						}
						
						if (myNode(o1)) {
							return -1;
						}
						
						if (myNode(o2)) {
							return 1;
						}
						
						return 0;
					}
				});

				int nodeTroops = playerNode.getNumberOfSolders();
				
				for (Node place : placesToGo) {
					if (place.getOwner() != gameState.getPlayerId()) {
						gameState.moveTroops(playerNode, place, playerNode.getNumberOfSolders());
						break;
					}
					
					if (nodeTroops - 1 > place.getNumberOfSolders()) {
						nodeTroops--;
						gameState.moveTroops(playerNode, place, 1);
					}
				}
				
// double troopsTotal = playerNode.getNumberOfSolders();
//
// for (Node place : placesToGo) {
// troopsTotal += place.getNumberOfSolders();
// }
//
// Collections.sort(placesToGo, new Comparator<Node>() {
// public int compare(Node o1, Node o2) {
// return new BigDecimal(o1.getNumberOfSolders()).compareTo(new BigDecimal(o2.getNumberOfSolders()));
// }
// });
//
// double nodeAverage = troopsTotal / (placesToGo.size() + 1);
//
// logger.debug(String.format("playerNode: %d, troopsTotal: %.02f, nodeAverage: %.02f",
// playerNode.getNodeId(), troopsTotal, nodeAverage));
//
// for (Node place : placesToGo) {
// int placeTroops = place.getNumberOfSolders();
//
// if (placeTroops > nodeAverage) {
// gameState.moveTroops(place, playerNode, placeTroops - (int)nodeAverage);
// } else if (placeTroops < nodeAverage) {
// gameState.moveTroops(playerNode, place, (int) nodeAverage - placeTroops);
// }
// }
			}
		}
	}

}
