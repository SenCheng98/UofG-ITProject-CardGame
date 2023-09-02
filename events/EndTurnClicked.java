package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.Run;
import structures.GameState;
import structures.basic.MiniCard;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case the end-turn button.
 *
 * { messageType = “endTurnClicked” }
 *
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor { // mana control & handcard number control & turn control??

	public static int turnCount = 0;


	public static int getturnCount() { return turnCount; }

	public void setturnCount(int turnCount) { this.turnCount = turnCount; }

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		if(TileClicked.unitSelected) {	// clear all range data in Tile

			for (int[] tile : TileClicked.moveTiles) {
				if (tile != null) {
					BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0); // delete the highlight tiles when click card

				}
			}
			for (int[] tile : TileClicked.allEnemy) {

					BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0); // delete the highlight tiles when click card
			}
			TileClicked.unitSelected = false;	//clear all data last round
			TileClicked.moveTiles = null;
			TileClicked.allEnemy = null;
		}


		for(int i=0;i<Run.myUnitOnTile.size();i++){ //refresh all data last round

			Run.myUnitOnTile.get(i).attackTimes = 0;
			Run.myUnitOnTile.get(i).moveTimes = 0;
			Run.myUnitOnTile.get(i).canAct = true;
		}

		if(CardClicked.selectedHandCard) {

			CardClicked.selectedHandCard = false;

			for(int i=0;i<CardClicked.summonRange.size();i++) {
				for (int[] tile : CardClicked.summonRange.get(i)) {
					if (tile != null) {
						BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0); // delete the highlight tiles when click card

					}
				}
			}
		}



		if(Run.humanPlayer.getHealth() <= 0 || Run.aiPlayer.getHealth() <= 0) {
			gameState.something = false;
		}
		if (gameState.something) {

			turnCount++; // times of click
			manaCount(out);
			cardNumber(out, gameState);

		}

	}


public static int setHumanMana = 2;	// have 2 mana at the beginning
public static int setAiMana = 2;

public static void manaCount(ActorRef out) { // ★★★ it should be clear when reload game!!!
	if (setHumanMana > 9) { // human mana limitation
		setHumanMana = 9;
	}
	if (setAiMana > 9) { // ai mana limitation
		setAiMana = 9;
	}

	if (EndTurnClicked.turnCount > 0) { // human first
		if (EndTurnClicked.turnCount % 2 == 0) {	// human turn
			//Run.aiPlayer.setMana(0);
			Run.humanPlayer.setMana(setHumanMana);
			setAiMana = setAiMana + 1;
			BasicCommands.setPlayer1Mana(out, Run.humanPlayer);
			BasicCommands.setPlayer2Mana(out, Run.aiPlayer);
		}
		if (EndTurnClicked.turnCount % 2 == 1) {	//ai turn
			//Run.humanPlayer.setMana(0);
			Run.aiPlayer.setMana(setAiMana);
			setHumanMana = setHumanMana + 1;
			BasicCommands.setPlayer1Mana(out, Run.humanPlayer);
			BasicCommands.setPlayer2Mana(out, Run.aiPlayer);
		}

	}

}



	public static int setHumanCard = 3;	//have 3 cards at the beginning
	public static int setAiCard = 3;

	public static void cardNumber(ActorRef out, GameState gameState) {


		if(setHumanCard > 6) {		//handcards limitation
			setHumanCard = 6;
		}
		if(setAiCard > 6) {
			setAiCard = 6;
		}

		if(EndTurnClicked.turnCount > 0) {
			if(EndTurnClicked.turnCount % 2 == 0) {	//human turn

				BasicCommands.addPlayer1Notification(out, "It is human turn...", 2);

				for(int i=0;i<setHumanCard;i++) {

					if(MiniCard.miniCard[i] == null) {		// if there is no card on one position, insert a card to it

						if(Run.humanCard.size() == 0)	break;

						// 6 steps in setting hand cards (sequential extraction, not randomly)
						// draw minicards
						// store cards to minicard[] + set a position of minicard + set minicard id
						// delete deck card
						BasicCommands.drawCard(out, Run.humanCard.get(0), i+1, 1);
						MiniCard.miniCard[i] = Run.humanCard.get(0).getMiniCard();
						MiniCard.miniCard[i].setPosition(i+1);
						MiniCard.miniCard[i].setId(Run.humanCard.get(0).getId());
						MiniCard.miniCard[i].setManacost(Run.humanCard.get(0).getManacost());
						Run.humanCard.remove(0);
					}

				}

				if(EndTurnClicked.turnCount > 1) {

					for(int i=0;i<Run.aiUnitOnTile.size();i++){ //refresh all data last round

						Run.aiUnitOnTile.get(i).attackTimes = 0;
						Run.aiUnitOnTile.get(i).moveTimes = 0;
						Run.aiUnitOnTile.get(i).canAct = true;
					}
				}


				setAiCard = setAiCard + 1;
			}


			if(EndTurnClicked.turnCount % 2 == 1) {	//ai turn, need a flag used in other class ,when jump into this, human cannot operate

				BasicCommands.addPlayer1Notification(out, "It is AI turn...", 2);
				try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
				setHumanCard = setHumanCard + 1;

				AITurn.allAction(out, gameState);


			}
		}

	}
}
