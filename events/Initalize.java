package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import demo.CheckMoveLogic;
import demo.CommandDemo;
import demo.Run;
import structures.GameState;
import structures.User;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 *
 * {
 *   messageType = “initalize”
 * }
 *
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		// hello this is a change

		gameState.gameInitalised = true;

		gameState.something = true;

		// User 1 makes a change

		Run.executeRun(out);
	}

}
