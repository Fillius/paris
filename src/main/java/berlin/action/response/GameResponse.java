package berlin.action.response;

import java.util.ArrayList;
import java.util.List;

public class GameResponse {

	private List<Move> moves = new ArrayList<>();

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	
	public void addMove(int from, int to, int numberOfTroops) {
		moves.add(new Move(from, to, numberOfTroops));
	}
	
	public int numberOfMoves() {
		return moves.size();
	}

}
