/**
 * This file is part of OVPiPod.
 *
 * OVPiPod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OVPiPod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OVPiPod.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    	WebSocketRobot.resetRobot();
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    /**
     * Methode appele lors d'une erreur du la socket
     *
     * @param t
     * 			exception sur la socket
     */
    @OnWebSocketError
    public void onError(Throwable t) {
    	WebSocketRobot.resetRobot();
        System.out.println("Error: " + t.getMessage());
    }

    /**
     * Methode appele lors de la connexion de la socket
     *
     * @param session
     * 			session associe a la socket ouverte avec le client
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
    	if(!WebSocketRobot.clientIsConnected())
    	{
    		WebSocketRobot.clientConnect();
	        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
	        try {
	            session.getRemote().sendString("OVPIPOD V3.0");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    	else
    		session.close();
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
    	
    	if(coord.length != 2)
    		System.out.println("MSG : " + message);
    	else
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
	    		case "bt":
	    			// boutons gamepads
	    			if(Integer.parseInt(coord[1]) == 8)
	    			{
	    				System.out.println("RESET Robot");
	    				Robot.getHandle().originRobot();
	    			}
	    			// TODO
	    			break;
	    		case "l2":
	    			//
	    			// TODO
	    			break;
	    		case "r2":
	    			//
	    			// TODO
	    			break;
	    		default:
	    			System.out.println("La donnee recu n'est pas un mouvement correct : " + coord[0]);
	    	}
    }
}
