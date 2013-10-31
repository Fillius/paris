package berlin.game;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;

public interface Node {

	@Property("nodeId")
	int getNodeId();
	
	@Property("nodeType")
	String getNodeType();
	
	@Property("victoryPointWorth")
	int getVictoryPointWorth();
	
	@Property("soldiersGrantedPerTurn")
	int getSoldiersGrantedPerTurn();
	
	@Property("owner")
	int getOwner();
	
	@Property("soldiers")
	int getNumberOfSolders();
	
	@Adjacency(label="path", direction=Direction.BOTH)
	Iterable<Node> getNeighbours();
	
	@Adjacency(label="path", direction=Direction.IN)
	Iterable<Node> getInboundNeighbours();
	
	@Adjacency(label="path", direction=Direction.OUT)
	Iterable<Node> getOutboundNeighbours();
	
}
