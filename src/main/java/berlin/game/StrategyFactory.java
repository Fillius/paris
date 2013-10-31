package berlin.game;

public interface StrategyFactory {

	Strategy pickStrategy(GameState gameState);

}
