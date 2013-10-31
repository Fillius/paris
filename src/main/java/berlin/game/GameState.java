package berlin.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import berlin.action.request.GameMap;
import berlin.action.request.GameMapNode;
import berlin.action.request.GameRequest;
import berlin.action.request.Infos;
import berlin.action.request.NodeState;
import berlin.action.request.NodeType;
import berlin.action.request.Path;
import berlin.action.response.GameResponse;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;

public class GameState {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final FramedGraph<Graph> graph;
	
	private final Infos info;

	public List<Node> getPlayerNodes(int player) {
		List<Node> nodes = getNodes();
		
		List<Node> playerNodes = new ArrayList<>();
		for (Node node : nodes) {
			if (node.getOwner() == player) {
				playerNodes.add(node);
			}
		}
		return playerNodes;
	}
	
	public List<Node> getPlayerNodes() {
		return getPlayerNodes(info.getPlayerId());
	}
	
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<>();
		for (Vertex v: graph.getVertices()) {
			nodes.add(graph.getVertex(v.getId(), Node.class));
		}
		return nodes;
	}
	
	public List<Node> getNodesSortedById() {
		List<Node> nodes = getNodes();
		Collections.sort(nodes, new Comparator<Node>() {
			@Override
			public int compare(Node node1, Node node2) {
				return Integer.compare(node1.getNodeId(), node2.getNodeId());
			}
		});
		return nodes;
	}

	//
	// Path movement routines
	//
	
	public void moveTroops(int from, int to, int numberToMove) {
		moveTroops(getNode(from), getNode(to), numberToMove);
	}
	
	public void moveTroops(Node from, Node to, int numberToMove) {
		if (numberToMove == 0) {
			return; // moving zero is a silent no-op, for convenience
		}
		if (numberToMove < 0) {
			logger.error("Attempt to move negative number of troops {}", numberToMove);
			return;
		}
		if (from.getOwner() != this.getPlayerId()) {
			logger.error("Attempt to move {} troops from non-player node {}", numberToMove, from.getNodeId());
			return;
		}
		if (from.getNumberOfSolders() < numberToMove) {
			logger.error("Attempt to move {} troops from node {} but there are only {} ",
					numberToMove,
					from.getNodeId(),
					from.getNumberOfSolders());
			numberToMove = from.getNumberOfSolders(); // move all we can
		}
		
		Edge edge = graph.getEdge(edgeId(from, to));
		if (edge == null) {
			logger.error("Could not find edge from node {} to node {}", from.getNodeId(), to.getNodeId());
			return;
		}
		edge.setProperty("move", numberToMove);
	}
	
	public String edgeId(int fromId, int toId) {
		return fromId + "." + toId;
	}
	
	public String edgeId(Node from, Node to) {
		return edgeId(from.getNodeId(), to.getNodeId());
	}
	
	public Edge getEdge(Node node1, Node node2) {
		return graph.getEdge(edgeId(node1, node2));
	}
	
	//
	// Game information
	//

	public String getGameId() {
		return info.getGameId();
	}

	public Integer getCurrentTurn() {
		return info.getCurrentTurn();
	}

	public int getMaximumTurns() {
		return info.getMaximumTurns();
	}

	public int getNumberOfPlayers() {
		return info.getNumberOfPlayers();
	}

	public int getTimeLimitPerTerm() {
		return info.getTimeLimitPerTerm();
	}

	public boolean isDirected() {
		return info.isDirected();
	}

	public int getPlayerId() {
		return info.getPlayerId();
	}

	//
	// Delegate methods for Tinkerpop graph
	//
	
	public Node getNode(int id) {
		return graph.getVertex(id, Node.class);
	}

	public Vertex getVertex(int id) {
		return graph.getVertex(id);
	}

	public Edge getEdge(int id) {
		return graph.getEdge(id);
	}

	public Iterable<Vertex> getVertices() {
		return graph.getVertices();
	}

	public Iterable<Vertex> getVertices(String key, Object value) {
		return graph.getVertices(key, value);
	}

	public Iterable<Edge> getEdges() {
		return graph.getEdges();
	}

	public Iterable<Edge> getEdges(String key, Object value) {
		return graph.getEdges(key, value);
	}
	
	public Graph getBaseGraph() {
		return graph.getBaseGraph();
	}
	
	//
	// Constructors and Methods called by Game class to update game state
	//
	
	protected GameState(GameRequest gameRequest) {
		super();
		
		this.info = gameRequest.getInfos();
		
		FramedGraphFactory factory = new FramedGraphFactory(new GremlinGroovyModule());
		Graph baseGraph = new TinkerGraph();
		graph = factory.create(baseGraph);
		
		// This isn't true of TinkerGraph, but in case somebody changes the graph implementation...
		if (graph.getFeatures().ignoresSuppliedIds) {
			throw new RuntimeException("We're not handling Blueprints implementations which ignore supplied ids");
		}
		
		GameMap gameMap = gameRequest.getGameMap();
		
		List<NodeType> nodeTypesList = gameMap.getNodeTypes();
		Map<String, NodeType> nodeTypes = new HashMap<>();
		for (NodeType nodeType : nodeTypesList) {
			nodeTypes.put(nodeType.getName(), nodeType);
		}
		
		for (GameMapNode node : gameMap.getNodes()) {
			Vertex v = graph.addVertex(node.getId());
			v.setProperty("nodeId", node.getId());

			NodeType nodeType = nodeTypes.get(node.getNodeType());
			if (nodeType != null) {
				v.setProperty("nodeType", nodeType.getName());
				v.setProperty("victoryPointWorth", nodeType.getVictoryPointWorth());
				v.setProperty("soldiersGrantedPerTurn", nodeType.getSoldiersGrantedPerTurn());
			}
		}
		
		for (Path path : gameMap.getPaths()) {
			Vertex from = graph.getVertex(path.getFrom());
			Vertex to = graph.getVertex(path.getTo());
			graph.addEdge(path.getFrom() + "." + path.getTo(), from, to, "path");
			if (!gameRequest.isDirected()) {
				graph.addEdge(path.getTo() + "." + path.getFrom(), to, from, "path");
			}
		}
		
		updateState(gameRequest.getStates());
	}
	
	protected void updateState(List<NodeState> nodeStates) {
		for (NodeState nodeState : nodeStates) {
			Vertex v = graph.getVertex(nodeState.getNodeId());
			v.setProperty("owner", nodeState.getPlayerId() == null ? -1 : nodeState.getPlayerId());
			v.setProperty("soldiers", nodeState.getNumberOfSolders());
		}
		for (Edge edge : graph.getEdges()) {
			edge.setProperty("move", 0);
		}
	}
	
	protected void gameOver(List<NodeState> nodeStates) {
		updateState(nodeStates);
		graph.shutdown();
	}
	
	protected GameResponse getMoves() {
		GameResponse response = new GameResponse();
		for (Edge edge : graph.getEdges()) {
			int move = edge.getProperty("move");
			if (move > 0) {
				response.addMove((int)edge.getVertex(Direction.OUT).getProperty("nodeId"),
						(int)edge.getVertex(Direction.IN).getProperty("nodeId"),
						move);
			}
		}
		return response;
	}
}
