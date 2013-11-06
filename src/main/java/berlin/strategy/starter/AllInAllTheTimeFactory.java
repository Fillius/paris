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

// private boolean enemyNode(Node node) {
// return !myNode(node);
// }
		
		private Integer nodeValue(Node node) {
			int value = 1;
			
			if (node.getSoldiersGrantedPerTurn() > 0) {
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
				List<Node> myNodes = new ArrayList<>();
				
				List<Node> placesToGo = Lists.newArrayList(playerNode.getOutboundNeighbours());
				
				String log = "";
				
				for (Node place : placesToGo) {
					log += "{" + place.getNodeId() + "," + place.getOwner() + "},";
				}
				
				logger.info("Node " + playerNode.getNodeId() + " neighbors: " + log);

				for (Node place : placesToGo) {
					if (myNode(place)) {
						myNodes.add(place);
					} else {
						enemyNodes.add(place);
					}
				}
				
				log = "";
				
				for (Node place : enemyNodes) {
					log += "e" + place.getNodeId() + ",";
				}
				
				for (Node place : myNodes) {
					log += place.getNodeId() + ",";
				}
				
				logger.info("Node " + playerNode.getNodeId() + " neighbors: " + log);
				
				log = "";

				for (Node place : enemyNodes) {
					log += String.format("%d,", place.getNodeId());
				}
				
				logger.info("Node " + playerNode.getNodeId() + " before sort: " + log);

				Collections.sort(enemyNodes, new Comparator<Node>() {
					public int compare(Node o1, Node o2) {
						int nodeValueComparison = nodeValue(o1).compareTo(nodeValue(o2));
						
						/* Prioritise the higher value node */
						if (nodeValueComparison != 0) {
							return nodeValueComparison;
						}
						
						/* Prioritise the node with the highest soldiers */
						return new Integer(o1.getNumberOfSolders()).compareTo(new Integer(o2.getNumberOfSolders()));
					}
				});
				
				Collections.sort(myNodes, new Comparator<Node>() {
					public int compare(Node o1, Node o2) {
						return nodeValue(o1).compareTo(nodeValue(o2));
					}
				});
				
				/* Reverse the list into priority descending order */
				Collections.reverse(enemyNodes);
				Collections.reverse(myNodes);

				log = "";
				
				for (Node place : enemyNodes) {
					log += String.format("%d,", place.getNodeId());
				}
				
				logger.info("Node " + playerNode.getNodeId() + " after sort: " + log);

				int nodeTroops = playerNode.getNumberOfSolders();

				int average = nodeTroops / enemyNodes.size();
				
				for (Node place : enemyNodes) {
					if (nodeTroops <= 1) {
						break;
					}
					
					int move = nodeTroops - 1;
					
					int optimalMove = place.getNumberOfSolders() + 2;
					
					if (gameState.getCurrentTurn() < 6) {
						move += 1;
						optimalMove = average;
					}
					
					if (move > optimalMove) {
						move = optimalMove;
					}
					
					gameState.moveTroops(playerNode, place, move);
					nodeTroops = nodeTroops - move;
				}
				
				for (Node place : myNodes) {
					if (nodeTroops <= 1) {
						break;
					}
					
					if (nodeValue(place) > nodeValue(playerNode) && place.getNumberOfSolders() < nodeTroops) {
						int move = nodeTroops - place.getNumberOfSolders();
						gameState.moveTroops(playerNode, place, move);
						nodeTroops = nodeTroops - move;
					} else if (nodeTroops >= place.getNumberOfSolders() + 5) {
						gameState.moveTroops(playerNode, place, 2);
						nodeTroops -= 2;
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
