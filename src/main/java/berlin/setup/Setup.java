package berlin.setup;

import javax.servlet.http.HttpServlet;

import berlin.game.StrategyFactory;
import berlin.game.TimeoutGameRequestHandler;
import berlin.servlet.BerlinAiServlet;
import berlin.servlet.RequestResponseMapper;
import berlin.strategy.starter.AllInAllTheTimeFactory;

public abstract class Setup {

	public static StrategyFactory createStrategyFactory() {
		return new AllInAllTheTimeFactory();
	}
	
	public static HttpServlet createServlet() {
		return new BerlinAiServlet(
				"WOLVERINE",
				RequestResponseMapper.statefulRequestResponseMapper(),
				new TimeoutGameRequestHandler(createStrategyFactory()));
	}
	
}
