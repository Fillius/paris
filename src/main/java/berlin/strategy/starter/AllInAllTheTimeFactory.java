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
				
				StringBuilder sb = new StringBuilder();
				
				for (Node place : placesToGo) {
					sb.append(String.format("%d,", place.getNodeId()));
				}
				
				logger.debug("Before sort: " + sb.toString());

				Collections.sort(placesToGo, new Comparator<Node>() {
					public int compare(Node o1, Node o2) {
						
						/* If they're both mine, share the wealth */
						if (myNode(o1) && myNode(o2)) {
							return new BigDecimal(o1.getNumberOfSolders()).compareTo(
									new BigDecimal(o2.getNumberOfSolders()));
						}

						/* If neither node is mine */
						if (!myNode(o1) && !myNode(o2)) {
							/* Prioritze the higher value node */
							if (o1.getSoldiersGrantedPerTurn() > o2.getSoldiersGrantedPerTurn() ||
									o1.getVictoryPointWorth() > o2.getVictoryPointWorth()) {
								return 1;
							}
							
							/* Prioritze the higher value node */
							if (o2.getSoldiersGrantedPerTurn() > o1.getSoldiersGrantedPerTurn() ||
									o2.getVictoryPointWorth() > o1.getVictoryPointWorth()) {
								return -1;
							}

							/* Prioritise the node with fewer soldiers (for a better chance of winning) */
							return new BigDecimal(o2.getNumberOfSolders()).compareTo(
									new BigDecimal(o1.getNumberOfSolders()));
						}
						
						/* Prioritize the node I don't own */
						if (myNode(o2)) {
							return 1;
						} else {
							return -1;
						}
					}
				});
				
				sb = new StringBuilder();
				
				for (Node place : placesToGo) {
					sb.append(String.format("%d,", place.getNodeId()));
				}
				
				logger.debug("After sort: " + sb.toString());

				int nodeTroops = playerNode.getNumberOfSolders();
				
				for (Node place : placesToGo) {
					if (!myNode(place)) {
						if (nodeTroops == 1) {
							gameState.moveTroops(playerNode, place, 1);
							break;
						} else {
							gameState.moveTroops(playerNode, place, nodeTroops - 1);
							nodeTroops = 1;
						}
						continue;
					}
					
					if (nodeTroops - 1 > place.getNumberOfSolders()) {
						gameState.moveTroops(playerNode, place, 1);
						nodeTroops--;
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
