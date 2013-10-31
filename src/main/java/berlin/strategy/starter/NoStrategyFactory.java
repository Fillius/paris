package berlin.strategy.starter;

import berlin.game.GameState;
import berlin.game.Strategy;
import berlin.game.StrategyFactory;

public class NoStrategyFactory implements StrategyFactory {

	@Override
	public Strategy pickStrategy(GameState gameState) {
		return new NoStrategy();
	}
	
	private class NoStrategy implements Strategy {
		@Override
		public void move() {
		}
	}

}
