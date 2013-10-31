package berlin.action.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameMapNode {

	@JsonProperty("id")
	private int id;
	
	@JsonProperty("type")
	private String nodeType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	@Override
	public String toString() {
		return "{" +
				(nodeType != null ? nodeType : "untyped node") +
				" " + id + "}";
	}
}
