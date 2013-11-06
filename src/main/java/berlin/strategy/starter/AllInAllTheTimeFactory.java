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
		
		private Integer nodeValue(Node node) {
			int value = 1;
			
			if (node.getNumberOfSolders() > 0) {
				if (gameState.getCurrentTurn() < gameState.getMaximumTurns() / 2) {
					value += 1;
				} else {
					value += 2;
				}
			}
			
			if (node.getVictoryPointWorth() > 0) {
				if (gameState.getCurrentTurn() < gameState.getMaximumTurns() / 2) {
					value += 1;
				} else {
					value += 2;
				}
			}
			
			return value;
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
						int nodeValueComparison = nodeValue(o1).compareTo(nodeValue(o2));
						
						/* Prioritise the higher value node */
						if (nodeValueComparison != 0) {
							return nodeValueComparison;
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
				
				Collections.sort(myNodes, new Comparator<Node>() {
					public int compare(Node o1, Node o2) {
						return nodeValue(o1).compareTo(nodeValue(o2));
					}
				});
				
				/* Reverse the list into priority descending order */
				Collections.reverse(otherNodes);
				Collections.reverse(myNodes);

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
					
					int move = nodeTroops - 1;
					
					if (move == 0) {
						move = 1;
					} else if (move > place.getNumberOfSolders() * 2) {
						move = place.getNumberOfSolders() * 2;
					}
					
					gameState.moveTroops(playerNode, place, move);
					nodeTroops = nodeTroops - move;
				}
				
				for (Node place : myNodes) {
					if (nodeTroops <= 0) {
						break;
					}
					
					if (nodeValue(place) > nodeValue(playerNode) && place.getNumberOfSolders() < nodeTroops) {
						int move = nodeTroops - place.getNumberOfSolders();
						gameState.moveTroops(playerNode, place, move);
						nodeTroops = nodeTroops - move;
					} else if (place.getNumberOfSolders() == 0) {
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
