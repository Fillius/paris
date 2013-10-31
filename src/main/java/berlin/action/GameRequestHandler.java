package berlin.action;

import java.io.PrintWriter;

import berlin.action.request.GameRequest;
import berlin.action.response.GameResponse;

/**
 * 
 * Called by the BerlinAiServlet, this is most fundamental point at which strategy
 * can be modified -- no analysis has been done at all on the game request, not even
 * to determine friendly or enemy nodes. Only the request and response formats have been
 * handled.
 * 
 * Implementations are expected to be thread-safe.
 * 
 * @author rob
 *
 */
public interface GameRequestHandler {

	/**
	 * 
	 * Provides a status report, which can contain any useful representation of the game(s)
	 * as appropriate to the AI strategy. This is made accessible by a GET request.
	 * 
	 * @param printWriter
	 */
	public void statusReport(PrintWriter printWriter);
	
	/**
	 * 
	 * Handles an action request from the game server (well, a client to us, really)
	 * The response can be null -- no moves will be made.
	 * 
	 * @param gameRequest
	 * @return
	 */
	public GameResponse action(GameRequest gameRequest);
}