package berlin.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import berlin.setup.Setup;


public class BerlinAi {

    public static void main(String[] args) throws Exception{
    	
    	String port = System.getenv("PORT");
    	if (port == null) {
    		throw new RuntimeException("no PORT environment variable specified");
    	}
    	
        Server server = new Server(Integer.valueOf(port));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(Setup.createServlet()),"/*");
        server.start();
        server.join();
    }
}