package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.Run;
import structures.GameState;

/**
 * In the user’s browser, the game is running in an infinite loop, where there is around a 1 second delay 
 * between each loop. Its during each loop that the UI acts on the commands that have been sent to it. A 
 * heartbeat event is fired at the end of each loop iteration. As with all events this is received by the Game 
 * Actor, which you can use to trigger game logic.
 * 
 * { 
 *   String messageType = “heartbeat”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Heartbeat implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		if(Run.humanPlayer.getHealth() <= 0 || Run.aiPlayer.getHealth() <= 0) {	//game over
			gameState.something = false;
		}
		if (gameState.something == false) {
			
			if(Run.humanPlayer.getHealth() <= 0 && Run.aiPlayer.getHealth() <= 0) {
				
				BasicCommands.addPlayer1Notification(out, "Game Over! Draw!", 2);
				System.out.println("Game Over! Draw!");
			}else {
				
				if(Run.aiPlayer.getHealth() <= 0) {
					
					BasicCommands.addPlayer1Notification(out, "Game Over! Human win!", 2);
					System.out.println("Game Over! Human win!");
				}
				if(Run.humanPlayer.getHealth() <= 0) {
					
					BasicCommands.addPlayer1Notification(out, "Game Over! AI win!", 2);
					System.out.println("Game Over! AI win!");
				}
			}
			
		}
	}

}
