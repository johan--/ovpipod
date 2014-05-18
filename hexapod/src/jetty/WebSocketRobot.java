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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import robot.Robot;

/**
 * Classe Declarant la WebSocket
 *
 * @author Jeremy HERGAULT, Jean-Phillipe HAYES
 * @version 3.1
 */
public class WebSocketRobot extends Thread {
	private static boolean clientConnected;

	/**
	 * Thread gestion Websocket Jetty
	 */
	public void run() {
		clientConnected = false;
        Server server = new Server(3842);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(RobotWebSocketHandler.class);
            }
        };
        server.setHandler(wsHandler);
        try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			server.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/**
     * Methode static pour arreter le robot, et prendre en compte la deconnexion du client
     */
	public static void resetRobot() {
		clientConnected = false;
		
		// send origin
        Robot.getHandle().MouvementX(0);
        Robot.getHandle().MouvementY(0);
        Robot.getHandle().MouvementZ(0);
	}
	
	/**
     * Methode static pour prendre en compte la connexion d'un client
     */
	public static void clientConnect() {
		clientConnected = true;
	}
	
	/**
     * Methode static pour savoir si un client est connecte
     */
	public static boolean clientIsConnected() {
		return clientConnected;
	}
}
