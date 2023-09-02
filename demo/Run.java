package demo;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.CardClicked;
import events.EndTurnClicked;
import events.TileClicked;
import structures.basic.Card;
import structures.basic.MiniCard;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

public class Run {

	public static Tile[][] tile = new Tile[9][5];
	public static Player humanPlayer;
	public static Player aiPlayer;
	public static List<Card> humanCard = OrderedCardLoader.getPlayer1Cards(); // only load once
	public static List<Card> aiCard = OrderedCardLoader.getPlayer2Cards();

	public static ArrayList<Unit> myUnitOnTile = new ArrayList<Unit>();	// store all my unit on the tile, which is used in summon method in CardClicked
	public static ArrayList<Unit> aiUnitOnTile = new ArrayList<Unit>();	// store all my unit on the tile, which is used in summon method in CardClicked

	public static Unit[] avatar = new Unit[2];

	public static void executeRun(ActorRef out) {

		// Draw tiles
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 5; y++) {
					tile[x][y] = BasicObjectBuilders.loadTile(x, y);
					BasicCommands.drawTile(out, tile[x][y], 0);
			}
		}

		//set health and mana
		humanPlayer = new Player(20, 2);
		aiPlayer = new Player(20, 2);


		//Draw health and mana
		BasicCommands.setPlayer1Health(out, humanPlayer);
		BasicCommands.setPlayer2Health(out, aiPlayer);
		
		BasicCommands.setPlayer1Mana(out, humanPlayer);
		BasicCommands.setPlayer2Mana(out, aiPlayer);


		// Draw a human player
		Unit unit99 = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 99, Unit.class);
		BasicCommands.setAllyOrEnemy(unit99);
		unit99.setPositionByTile(tile[1][2]);
		BasicCommands.drawUnit(out, unit99, tile[1][2]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit99, 5);
		BasicCommands.setUnitHealth(out, unit99, 20);
		myUnitOnTile.add(unit99);


		// Draw a AI player
		Unit unit100 = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 100, Unit.class);
		BasicCommands.setAllyOrEnemy(unit100);
		unit100.setPositionByTile(tile[7][2]);
		BasicCommands.drawUnit(out, unit100, tile[7][2]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit100, 5);
		BasicCommands.setUnitHealth(out, unit100, 20);
		aiUnitOnTile.add(unit100);


		avatar[0] = unit99;	//store human and ai boss in an array,  these 2 units are used in tileClicked
		avatar[1] = unit100;


		// 6 steps in setting hand cards (sequential extraction, not randomly)
		// draw minicards
		// store cards to minicard[] + set a position of minicard + set minicard id + set minicard manacost
		// delete deck card

		for(int i=0;i<3;i++) {
			BasicCommands.drawCard(out, Run.humanCard.get(0), i+1, 1);
			Run.humanCard.get(0).getMiniCard().setPosition(i+1);;
			MiniCard.miniCard[i] = Run.humanCard.get(0).getMiniCard();
			MiniCard.miniCard[i].setId(Run.humanCard.get(0).getId());
			MiniCard.miniCard[i].setManacost(Run.humanCard.get(0).getManacost());			
			Run.humanCard.remove(0);
			
		}	
		
		BasicCommands.addPlayer1Notification(out, "It is human turn...", 2);
		
	}

}
