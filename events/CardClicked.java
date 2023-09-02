package events;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.Run;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.MiniCard;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case a card. The event returns the position in the player's hand the card
 * resides within.
 *
 * { messageType = “cardClicked” position = <hand index position [1-6]> }
 *
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor {

	public static ArrayList<int[][]> summonRange = new ArrayList<int[][]>(); // store the summon range coordinates
	public static int selectedCardID; // will pass it to TileClicked class
	public static int cardType = -1; // 0--> unit, 1--> spell
	public static boolean selectedHandCard = false; // a flag will pass it to TileClicked class

	public static MiniCard minicard;
	public static int pos = -1;

	public int getselectedCardID() { return selectedCardID; }
	public int getcardType() { return cardType; }
	public boolean getselectedHandCard() { return selectedHandCard; }
	public MiniCard getminicard() { return minicard; }

	public void setselectedCardID(int selectedCardID) { this.selectedCardID = selectedCardID; }
	public void setcardType(int cardType) { this.cardType = cardType; }
	public void setselectedHandCard(boolean selectedHandCard) {
			this.selectedHandCard = selectedHandCard;
	}


	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int handPosition = message.get("position").asInt();

		pos = handPosition;		// need it in TileClicked, when summon a unit, it should be deleted in miniCard[];

		// it is human turn

		if (EndTurnClicked.getturnCount() % 2 == 0) {


			if(TileClicked.getunitSelected()) {	// clear all range data in Tile

				for (int[] tile : TileClicked.moveTiles) {
					if (tile != null) {
						BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0); // delete the highlight tiles when click card

					}
				}
				TileClicked.unitSelected = false;
				TileClicked.moveTiles = null;
			}
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
			cardSelect(out, handPosition);

		}


	}

	public void cardSelect(ActorRef out, int handPos) { // summon a unit or cast a spell

		for (int i = 0; i < 6; i++) {

			if (MiniCard.miniCard[i] != null) {

				if (MiniCard.miniCard[i].getPosition() == handPos) {


					selectedCardID = MiniCard.miniCard[i].getId();
					minicard = MiniCard.miniCard[i];

					if (MiniCard.miniCard[i].getId() == 4 || MiniCard.miniCard[i].getId() == 14
							|| MiniCard.miniCard[i].getId() == 8 || MiniCard.miniCard[i].getId() == 18) {

						cardType = 1;
						selectedHandCard = true;
						System.out.println("This is a spell card!");

						// show the units can be cast
						showCardRange(out, cardType, MiniCard.miniCard[i].getId());

					} else {

						cardType = 0;
						selectedHandCard = true; // need to be false in tileClicked class
						System.out.println("This is an unit card!");

						// show the attack range
						showCardRange(out, cardType, 0);		// not very good

					}
					break;
				}
			}

		}
	}

	public void showCardRange(ActorRef out, int cardType, int cardId) {

		if (cardType == 0) {

			for (int i = 0; i < Run.myUnitOnTile.size(); i++) {

				int x = Run.myUnitOnTile.get(i).getPosition().getTilex();
				int y = Run.myUnitOnTile.get(i).getPosition().getTiley();
				summonRange.add(Board.getAttackRangeTiles(x, y)); // need to be clear in tileClilked class

			}

			for (int i = 0; i < summonRange.size(); i++) {

				for (int[] range : summonRange.get(i)) {
					if (range != null) {
						if (Run.tile[range[0]][range[1]].getHasUnit()) {
							BasicCommands.drawTile(out, Run.tile[range[0]][range[1]], 0); // red highlight if there is
																							// an unit in a tile
						} else {
							BasicCommands.drawTile(out, Run.tile[range[0]][range[1]], 1); // white highlight
						}
					}
				}
			}
		}

		if (cardType == 1) {

			int[][] enemy = new int[Run.aiUnitOnTile.size()][2];	// !!!!! limit size == aiUnitOnTile.size()
			int[][] ally = new int[Run.myUnitOnTile.size()][2];	// !!!!! limit size == aiUnitOnTile.size()

			if(cardId == 4 || cardId == 14) {		// true strike

	 			for (int i = 0; i < Run.aiUnitOnTile.size(); i++) {

					int x = Run.aiUnitOnTile.get(i).getPosition().getTilex();
					int y = Run.aiUnitOnTile.get(i).getPosition().getTiley();
					enemy[i][0] = x;
					enemy[i][1] = y;
				}
	 			summonRange.add(enemy); // need to be clear in tileClilked class


				for (int i = 0; i < summonRange.size(); i++) {

					for (int[] range : summonRange.get(i)) {
						if (range != null) {

							BasicCommands.drawTile(out, Run.tile[range[0]][range[1]], 2); // red highlight if there is an unit in a tile
							System.out.println("..." + range[0] +"..." +range[1]);

						}
					}
				}
			}

			if(cardId == 8 || cardId == 18) {		// sun drop

	 			for (int i = 0; i < Run.myUnitOnTile.size(); i++) {

					int x = Run.myUnitOnTile.get(i).getPosition().getTilex();
					int y = Run.myUnitOnTile.get(i).getPosition().getTiley();
					ally[i][0] = x;
					ally[i][1] = y;
				}
	 			summonRange.add(ally); // need to be clear in tileClilked class

				for (int i = 0; i < summonRange.size(); i++) {

					for (int[] range : summonRange.get(i)) {
						if (range != null) {

							BasicCommands.drawTile(out, Run.tile[range[0]][range[1]], 1); // red highlight if there is an unit in a tile

						}
					}
				}
			}




		}
	}


}
