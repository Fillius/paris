package berlin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import berlin.action.GameRequestHandler;
import berlin.action.request.GameRequest;
import berlin.action.response.GameResponse;
import berlin.logging.FixedMemoryLogbackAppender;

public class BerlinAiServlet extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public final String codename; // = "JOHN CRAWFORD";
	private final RequestResponseMapper requestResponseMapper;
	private final GameRequestHandler gameRequestHandler;

	public BerlinAiServlet(String codename,
			RequestResponseMapper requestResponseMapper,
			GameRequestHandler gameRequestHandler) {
		super();
		this.codename = codename;
		this.requestResponseMapper = requestResponseMapper;
		this.gameRequestHandler = gameRequestHandler;
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		PrintWriter writer = response.getWriter();
		
		writer.print("______   _______  _______  _       _________ _       \n");
		writer.print("(  ___ \\ (  ____ \\(  ____ )( \\      \\__   __/( (    /|\n");
		writer.print("| (   ) )| (    \\/| (    )|| (         ) (   |  \\  ( |\n");
		writer.print("| (__/ / | (__    | (____)|| |         | |   |   \\ | |\n");
		writer.print("|  __ (  |  __)   |     __)| |         | |   | (\\ \\) |\n");
		writer.print("| (  \\ \\ | (      | (\\ (   | |         | |   | | \\   |\n");
		writer.print("| )___) )| (____/\\| ) \\ \\__| (____/\\___) (___| )  \\  |\n");
		writer.print("|/ \\___/ (_______/|/   \\__/(_______/\\_______/|/    )_)\n");
		writer.print("\n");
		writer.print("CODENAME: " + codename + "\n");
		writer.print("\n");
		
		gameRequestHandler.statusReport(writer);
		FixedMemoryLogbackAppender.writeList(writer);
    }
	
	@Override
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		long startNanoTime = System.nanoTime();
		
		try {
			GameRequest gameRequest = requestResponseMapper.mapRequest(
					startNanoTime,
					httpRequest.getParameter("action"),
					httpRequest.getParameter("infos"),
					httpRequest.getParameter("map"),
					httpRequest.getParameter("state"));
			
			GameResponse gameResponse = gameRequestHandler.action(gameRequest);
			writeResponse(httpResponse, gameResponse);
			long endNanoTime = System.nanoTime();
			logger.info("request-response cycle took {} ms", (endNanoTime-startNanoTime)/1000000l);
		}
		catch (Exception ex) {
			logger.error("unhandled exception -- returning empty move list", ex);
			writeResponse(httpResponse, null);
		}
	}
	
	protected void writeResponse(HttpServletResponse servletResponse, GameResponse gameResponse) throws IOException {
		if (gameResponse != null) {
			String responseString = requestResponseMapper.mapResponse(gameResponse);
			logger.info("response: {}", responseString);
			servletResponse.getWriter().print(responseString);
			servletResponse.setStatus(HttpStatus.OK_200);	
		}
		else {
			servletResponse.getWriter().print("[]");
			servletResponse.setStatus(HttpStatus.OK_200);
		}
	}
	


}
