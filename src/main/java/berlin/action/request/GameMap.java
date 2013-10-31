package berlin.action.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameMap {

	@JsonProperty("types")
	private List<NodeType> nodeTypes;
	
	@JsonProperty("nodes")
	private List<GameMapNode> nodes;
	
	@JsonProperty("paths")
	private List<Path> paths;

	public List<NodeType> getNodeTypes() {
		return nodeTypes;
	}

	public void setNodeTypes(List<NodeType> nodeTypes) {
		this.nodeTypes = nodeTypes;
	}

	public List<GameMapNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<GameMapNode> nodes) {
		this.nodes = nodes;
	}

	public List<Path> getPaths() {
		return paths;
	}

	public void setPaths(List<Path> paths) {
		this.paths = paths;
	}

	@Override
	public String toString() {
		return "GameMap [nodeTypes=" + nodeTypes +
				", nodes=" + nodes +
				", paths=" + paths + "]";
	}

}
