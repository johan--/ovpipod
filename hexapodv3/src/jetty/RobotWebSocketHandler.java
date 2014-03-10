package jetty;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import robot.Robot;

@WebSocket
public class RobotWebSocketHandler {

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try {
            session.getRemote().sendString("Hello Webbrowser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
    	
    	String[] coord = message.split("[:]");
    	
    	if(coord.length == 2)
    	{
    		System.out.println("COORD " + coord[0] + ": " + coord[1]);
    	}
    	else
    	{
    		System.out.println("MSG : " + message);
    	}
    	
    	// TODO : switch case
    	if(coord[0] == "xl")
    	{
    		// joystick horizontal gauche
    		Robot.getHandle().MouvementX(Integer.parseInt(coord[1]));
    	}
    	else if(coord[0] == "yl")
    	{
    		// joystick vertical gauche
    		Robot.getHandle().MouvementY(Integer.parseInt(coord[1]));
    	}
    	else if(coord[0] == "xr")
    	{
    		// joystick horizontal droit
    		//Robot.getHandle().Mouvement?(Integer.parseInt(coord[1]));
    	}
    	else if(coord[0] == "yr")
    	{
    		// joystick vertical droit
    		Robot.getHandle().MouvementZ(Integer.parseInt(coord[1]));
    	}
    	else
    	{
    		System.out.println("La donnee recu n'est pas un mouvement correct");
    	}
    }
}
