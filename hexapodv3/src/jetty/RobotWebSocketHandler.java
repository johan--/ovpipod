package jetty;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import robot.Robot;

/**
 * Classe contenant le masquage des methode de la WebSocket
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
@WebSocket
public class RobotWebSocketHandler {

    /**
     * Methode appele lors de la fermeture d'une socket
     *
     * @param statusCode
     * 			code retour de la raison de la fermeture de la socket
     * @param reason
     * 			Message descriptif de la fermeture
     */
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);

        // send origin
        Robot.getHandle().MouvementX(0);
        Robot.getHandle().MouvementY(0);
        Robot.getHandle().MouvementZ(0);
    }

    /**
     * Methode appele lors d'une erreur du la socket
     *
     * @param t
     * 			exception sur la socket
     */
    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
        
        // send origin
        Robot.getHandle().MouvementX(0);
        Robot.getHandle().MouvementY(0);
        Robot.getHandle().MouvementZ(0);
    }

    /**
     * Methode appele lors de la connexion de la socket
     *
     * @param session
     * 			session associe a la socket ouverte avec le client
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        try {
            session.getRemote().sendString("OVPIPOD V3.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode appele lors de la reception d'un message du client
     *
     * @param message
     * 			Message envoye par le client
     */
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
    	
    	switch(coord[0])
    	{
    		case "xl":
    			// joystick horizontal gauche
        		Robot.getHandle().MouvementX(Integer.parseInt(coord[1]));
        		break;
    		case "yl":
    			// joystick vertical gauche
        		Robot.getHandle().MouvementY(Integer.parseInt(coord[1]));
        		break;
    		case "xr":
    			// joystick horizontal droit
        		Robot.getHandle().MouvementZ(Integer.parseInt(coord[1]));
    			break;
    		case "yr":
    			// joystick vertical droit
        		//Robot.getHandle().Mouvement?(Integer.parseInt(coord[1]));
    			break;
    		default:
    			System.out.println("La donnee recu n'est pas un mouvement correct : " + coord[0]);
    	}
    }
}
