package berlin.game;

import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import berlin.action.GameRequestHandler;
import berlin.action.request.GameRequest;
import berlin.action.response.GameResponse;

/**
 * 
 * An implementation of the GameRequestHandler which provides the added functionality of
 * ensuring all requests are handled sequentially (no need worry about thread safety in
 * stateful AIs), and to time out the strategy if it takes too long (returning a 
 * 
 * @author rob
 *
 */
public class TimeoutGameRequestHandler implements GameRequestHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	private final GameLibrary gameLibrary;
	
	private final int gracePeriod = 0;
	
	public TimeoutGameRequestHandler(StrategyFactory strategyFactory) {
		this.gameLibrary = new GameLibrary(strategyFactory); 
	}
	
	
	public void statusReport(final PrintWriter printWriter) {
		final Future<?> future = executor.submit(new Runnable() {

			@Override
			public void run() {
				gameLibrary.statusReport(printWriter);
			}
		});
		
		try {
			future.get();
		}
		catch (Exception ex) {
			printWriter.print("an exception was thrown will attempting to obtain the status report");
			printWriter.print(ex);
		}
	}
	
	public GameResponse action(GameRequest gameRequest) {		

		long alreadySpent = (System.nanoTime() - gameRequest.getStartNanoTime()) / 1000000l;
		long timeout = gameRequest.getTimeLimitPerTerm() - (alreadySpent + gracePeriod);
		
		if (timeout < 0) {
			logger.warn("negative timeout {} [already spent {}, grace period {}, time limit per term {}] -- giving up in despair",
					timeout,
					alreadySpent,
					gracePeriod,
					gameRequest.getTimeLimitPerTerm());
			return null;
		}
		
		final Future<GameResponse> future = executor.submit(new GameUpdater(gameLibrary, gameRequest));
		try {
			return future.get(timeout, TimeUnit.MILLISECONDS);
		}
		catch (ExecutionException ex) {
			logger.warn("execution exception caught during game task", ex);
		}
		catch (InterruptedException ex) {
			logger.warn("interruption during game task", ex);
		}
		catch (TimeoutException ex) {
			logger.warn("timed out game task");
		}
		return null;
	}
	
	protected class GameUpdater implements Callable<GameResponse> {

		private final GameLibrary gameLibrary;
		
		private final GameRequest gameRequest;

		public GameUpdater(GameLibrary gameLibrary, GameRequest gameRequest) {
			super();
			this.gameLibrary = gameLibrary;
			this.gameRequest = gameRequest;
		}

		@Override
		public GameResponse call() throws Exception {
			return gameLibrary.update(gameRequest);
		}
	}
}
