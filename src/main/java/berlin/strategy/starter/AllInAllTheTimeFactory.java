package berlin.strategy.starter;

import java.util.ArrayList;
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
		
		private boolean neutralNode(Node node) {
			return node.getOwner() == null;
		}

		private boolean enemyNode(Node node) {
			return !myNode(node) && !neutralNode(node);
		}

		@Override
		public void move() {
			for (Node playerNode : gameState.getPlayerNodes()) {
				
				List<Node> enemyNodes = new ArrayList<>();
				List<Node> neutralNodes = new ArrayList<>();
				List<Node> myNodes = new ArrayList<>();
				
				List<Node> placesToGo = Lists.newArrayList(playerNode.getOutboundNeighbours());
				
				for (Node place : placesToGo) {
					if (enemyNode(place)) {
						enemyNodes.add(place);
						continue;
					}
					
					if (neutralNode(place)) {
						neutralNodes.add(place);
						continue;
					}
					
					if (myNode(place)) {
						myNodes.add(place);
						continue;
					}
				}
				
				List<Node> otherNodes = new ArrayList<>();
				
				otherNodes.addAll(enemyNodes);
				otherNodes.addAll(neutralNodes);

				String log = "";
				
				for (Node place : otherNodes) {
					log += String.format("%d,", place.getNodeId());
				}
				
				logger.info("Before sort: " + log);

				Collections.sort(otherNodes, new Comparator<Node>() {
					public int compare(Node o1, Node o2) {
						/* Prioritise the higher value node */
						if (o1.getSoldiersGrantedPerTurn() > o2.getSoldiersGrantedPerTurn() ||
								o1.getVictoryPointWorth() > o2.getVictoryPointWorth()) {
							return 1;
						}
						
						/* Prioritise the higher value node */
						if (o2.getSoldiersGrantedPerTurn() > o1.getSoldiersGrantedPerTurn() ||
								o2.getVictoryPointWorth() > o1.getVictoryPointWorth()) {
							return -1;
						}
						
						/* If the nodes are both enemies, prioritise the node with the highest soldiers */
						if (enemyNode(o1) && enemyNode(o2)) {
							return new Integer(o1.getNumberOfSolders()).compareTo(new Integer(o2.getNumberOfSolders()));
						}
						
						/* Always prioritise the enemy node */
						if (enemyNode(o1)) {
							return 1;
						} else if (enemyNode(o2)) {
							return -1;
						}
						
						return 0;
					}
				});
				
				log = "";
				
				for (Node place : otherNodes) {
					log += String.format("%d,", place.getNodeId());
				}
				
				logger.info("After sort: " + log);

				int nodeTroops = playerNode.getNumberOfSolders();
				
				for (Node place : otherNodes) {
					if (nodeTroops <= 0) {
						break;
					}
					
					gameState.moveTroops(playerNode, place, nodeTroops);
					nodeTroops = 0;
				}
				
				for (Node place : myNodes) {
					if (nodeTroops <= 0) {
						break;
					}
					
					if (place.getNumberOfSolders() < nodeTroops) {
						int move = nodeTroops - place.getNumberOfSolders();
						gameState.moveTroops(playerNode, place, move);
						nodeTroops = nodeTroops - move;
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
