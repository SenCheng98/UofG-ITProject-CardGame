package events;

import structures.basic.Board;
import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.Run;
import structures.GameState;
import structures.basic.Card;
import structures.basic.MiniCard;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class TileClicked implements EventProcessor {

	public static boolean unitSelected = false; // if player has selected a unit
	static int[][] moveTiles; // store move range to a 2-dimension array
	int[][] attackTiles;	// store attack range to a 2-dimension array
	static Unit lastUnit; // store the latest selected unit
	boolean lock = false;

	static int[][] allEnemy;

	public static boolean getunitSelected() { return unitSelected; }

	public Unit getlastUnit() { return lastUnit; }
	public boolean getlock() { return lock; }
	public int[][] getmoveTiles() { return moveTiles; }
	public int[][] getattackTiles() { return attackTiles; }
	public int[][] getallEnemy() { return allEnemy; }


	public void setunitSelected(boolean unitSelected) { this.unitSelected = unitSelected; }
	public void setlastUnit(Unit lastUnit) { this.lastUnit = lastUnit; }

	public void setlock(boolean lock) { this.lock = lock; }

	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int x = message.get("tilex").asInt(); // coordinate x of clicked tile
		int y = message.get("tiley").asInt(); // coordinate y of clicked tile

		if (gameState.something == true) {
			// do some logic

			// Only it is human turn, the following can run

			if(EndTurnClicked.turnCount %2 ==0) {

				if (CardClicked.selectedHandCard) {

					clickedACard(out, x, y);	// clicked a hand card before
				} else {

					clickedAUnit(out, x, y);	// didn't click any hand card before
				}
			}

		}

	}

	public void clickedAUnit(ActorRef out, int x, int y) {

		this.lock = false; // a lock to make sure the order must be step1 to step2


		// ★★★ step2 ★★★, move, or attack
		if (unitSelected) {

			for (int[] tile : moveTiles) {
				if (tile != null) {
					BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0); // delete the highlight tiles when click tile
				}
			}

			// ★★★★ different unit has different action ★★★★

			if(this.lastUnit.getId() == 2 || this.lastUnit.getId() == 11) {		// fire splitter

				actionForFireSplitter(out, x, y);

			}else if(this.lastUnit.getId() == 7 || this.lastUnit.getId() == 17) {	// azurite lion

				actionForAzuriteLion(out, x, y);

			}else {

				actionForNormalUnit(out, x, y);		//other units
			}


			unitSelected = false;
			this.lock = true;

		}


		// ★★★ step 1 ★★★，select an ally unit , and show some range highlight
		if (Run.tile[x][y].getHasUnit() && Run.tile[x][y].getUnit().getGroup() == 0
				&& Run.tile[x][y].getUnit().canAct == true && this.lock == false) {


			unitSelected = true;
			this.lastUnit = Run.tile[x][y].getUnit();
			moveTiles = null; // even if we get new data then we click an unit, but it's not bad to clear old data

			moveTiles = Board.getMoveRangeTiles(x, y);
			this.attackTiles = Board.getAttackRangeTiles(x, y);

			if(lastUnit.moveTimes == 0 && lastUnit.attackTimes == 0) {	//has not moved and attack

				for (int[] tile : moveTiles) {		//show move range
					if (tile != null) {
						if (Run.tile[tile[0]][tile[1]].getHasUnit()) {

							if (Run.tile[tile[0]][tile[1]].getUnit().getGroup() == 0) { // it is an ally
								BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0); // red highlight if there is an
																							// unit
							}
							if (Run.tile[tile[0]][tile[1]].getUnit().getGroup() == 1) { // it is an enemy
								BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 2); // red highlight if there is an
																							// unit
							}

						} else {
							BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 1); // white highlight()
						}

					}
				}
			}
			if(lastUnit.moveTimes == 1 && lastUnit.attackTimes == 0
					&& lastUnit.getId() != 7 && lastUnit.getId() != 17) {	// has moved but not attack

				for(int[] tile : attackTiles) {
					if( tile != null) {
						if (Run.tile[tile[0]][tile[1]].getHasUnit()) {
							if(Run.tile[tile[0]][tile[1]].getUnit().getGroup() == 1) {
								BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 2);
							}
						}
					}
				}
			}
			if(lastUnit.moveTimes == 1 && lastUnit.attackTimes <2
					&& (lastUnit.getId() == 7 || lastUnit.getId() == 17)) {	// Azurite Lion can attack 2 times

				System.out.println("movetimes: " + lastUnit.moveTimes + " attack times: " + lastUnit.attackTimes);
				for(int[] tile : attackTiles) {
					if( tile != null) {
						if (Run.tile[tile[0]][tile[1]].getHasUnit()) {
							if(Run.tile[tile[0]][tile[1]].getUnit().getGroup() == 1) {
								BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 2);
							}
						}
					}
				}
			}


			allEnemy = new int[Run.aiUnitOnTile.size()][2]; // max enemy number = 17, size = ai unit number
			if((this.lastUnit.getId() == 2 || this.lastUnit.getId() == 11) && lastUnit.attackTimes == 0) {		// show splitter attack range

				for(int i=0; i< Run.aiUnitOnTile.size(); i++){
					allEnemy[i][0] = Run.aiUnitOnTile.get(i).getPosition().getTilex();
					allEnemy[i][1] = Run.aiUnitOnTile.get(i).getPosition().getTiley();
				}

				for(int[] tile : allEnemy) {
					if(tile != null) {
						BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 2);
					}
				}
			}

		}
	}

	public void clickedACard(ActorRef out, int x, int y) {

		for (int i = 0; i < CardClicked.summonRange.size(); i++) { // delete the highlight whatever
			for (int[] tile : CardClicked.summonRange.get(i)) {
				if (tile != null) {
					BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0);

				}
			}
		}

		if (hasEnoughMana(CardClicked.minicard)) { // ★★★★★★★★★★★★★★★★if player has enough mana ★★★★★★★★★★★★★★


			for (int i = 0; i < CardClicked.summonRange.size(); i++) {

				for (int[] tile : CardClicked.summonRange.get(i)) {

					if (tile != null) {
						if (tile[0] == x && tile[1] == y) {


							if (CardClicked.cardType == 0) { // -------> 1, it is an unit card

								if (!Run.tile[tile[0]][tile[1]].getHasUnit()) { // there is no unit on the tile
									summonOrCast(out, CardClicked.selectedCardID, x, y);

									BasicCommands.deleteCard(out, CardClicked.pos); // delete handcard
									MiniCard.miniCard[CardClicked.pos - 1] = null;

									int mana = Run.humanPlayer.getMana() - CardClicked.minicard.getManacost(); // set
																												// player's
																												// mana
									Run.humanPlayer.setMana(mana);
									BasicCommands.setPlayer1Mana(out, Run.humanPlayer);


								}
							}

							if (CardClicked.cardType == 1) { // -------> 2, it is a spell card

								summonOrCast(out, CardClicked.selectedCardID, x, y);

								BasicCommands.deleteCard(out, CardClicked.pos); // delete handcard
								MiniCard.miniCard[CardClicked.pos - 1] = null;

								int mana = Run.humanPlayer.getMana() - CardClicked.minicard.getManacost(); // set
																											// player's
																											// mana
								Run.humanPlayer.setMana(mana);
								BasicCommands.setPlayer1Mana(out, Run.humanPlayer);
							}

							break;
						}
					}
				}

			}

		} else {	// dont have enough mana

			BasicCommands.addPlayer1Notification(out, "I dont have enough mana!", 2);
		}

		CardClicked.selectedHandCard = false;
		CardClicked.selectedCardID = -1;
		CardClicked.cardType = -1;
		CardClicked.summonRange.clear(); // reset all the original data

	}

	public static void move(ActorRef out, Unit unit, int x, int y) { // (x, y) is the new position of the unit

		BasicCommands.addPlayer1Notification(out, "moving...", 2);

		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.move);
		BasicCommands.moveUnitToTile(out, unit, Run.tile[x][y], true); // move
		Run.tile[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setUnit(null); // delete the information
																								// in the old tile.
		Run.tile[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setHasUnit(false);
		unit.setPositionByTile(Run.tile[x][y]); // set the new position of the unit
	}

	public static void attack(ActorRef out, Unit unit, int x, int y) {

		Unit attacker = unit;
		Unit defender = Run.tile[x][y].getUnit();
		int attackerHealth = unit.getHealth();
		int defenderHealth = Run.tile[x][y].getUnit().getHealth();
		int attackerAtt = unit.getAttack();
		int defenderAtt = Run.tile[x][y].getUnit().getAttack();

		defenderHealth = defenderHealth - attackerAtt;
		attackerHealth = attackerHealth - defenderAtt;

		BasicCommands.addPlayer1Notification(out, "attacking...", 2);
		//BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.move);
		BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.attack);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		//BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.move);
		BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.attack);


		if (defenderHealth <= 0) {

			BasicCommands.addPlayer1Notification(out, "you are dead! :)", 2);
			deleteUnit(out, defender);


		} else {

			reFreshUnit(out, defender, defenderAtt, defenderHealth);

		}

		if (attackerHealth <= 0) {

			BasicCommands.addPlayer1Notification(out, "I will come back!", 2);
			deleteUnit(out, attacker);

			if(EndTurnClicked.turnCount %2 == 1) {
				AITurn.it --;
			}

		} else {

			reFreshUnit(out, attacker, attackerAtt, attackerHealth);

		}

		// connect unit's health to player's health

		if (defender.getId() == 99) {
			Run.humanPlayer.setHealth(defenderHealth); 	// show on tile
			BasicCommands.setPlayer1Health(out, Run.humanPlayer); // show on avatar
		}
		if (defender.getId() == 100) {
			Run.aiPlayer.setHealth(defenderHealth);
			BasicCommands.setPlayer2Health(out, Run.aiPlayer); // show on avatar
		}

		if (attacker.getId() == 99) {
			Run.humanPlayer.setHealth(attackerHealth);
			BasicCommands.setPlayer1Health(out, Run.humanPlayer); // show on avatar
		}
		if (attacker.getId() == 100) {
			Run.aiPlayer.setHealth(attackerHealth);
			BasicCommands.setPlayer2Health(out, Run.aiPlayer); // show on avatar
		}

		// for test
		System.out.println("attackerHealth: " + attackerHealth);
		System.out.println("defenderHealth: " + defenderHealth);

	}

	public static void reFreshUnit(ActorRef out, Unit unit, int attack, int health) {

		BasicCommands.setUnitAttack(out, unit, attack);
		BasicCommands.setUnitHealth(out, unit, health);

	}

	public static void deleteUnit(ActorRef out, Unit unit) {
		BasicCommands.deleteUnit(out, unit);
		Run.tile[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setUnit(null); // delete the information
																								// in the old tile.
		Run.tile[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setHasUnit(false);

		if(unit.getGroup() == 0) {
			Run.myUnitOnTile.remove(unit);
		}else {
			Run.aiUnitOnTile.remove(unit);
		}

	}

	public static int[] preAttackMove(Unit unit, int x, int y) {
		int[] pos = new int[2];
		int x1 = unit.getPosition().getTilex() - x;
		int y1 = unit.getPosition().getTiley() - y;

		if (x1 == 2) {
			pos[0] = unit.getPosition().getTilex() - 1;
			pos[1] = unit.getPosition().getTiley();
		}
		if (x1 == -2) {
			pos[0] = unit.getPosition().getTilex() + 1;
			pos[1] = unit.getPosition().getTiley();
		}
		if (y1 == 2) {
			pos[0] = unit.getPosition().getTilex();
			pos[1] = unit.getPosition().getTiley() - 1;
		}
		if (y1 == -2) {
			pos[0] = unit.getPosition().getTilex();
			pos[1] = unit.getPosition().getTiley() + 1;
		}
		return pos;
	}

	public void summonOrCast(ActorRef out, int cardId, int x, int y) { // handcard postion, tile (x, y)

		switch (cardId) {

		case 0:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			Unit unit0 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger, 0, Unit.class);
			BasicCommands.setAllyOrEnemy(unit0);
			unit0.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit0, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit0, 1);
			BasicCommands.setUnitHealth(out, unit0, 3);
			Run.myUnitOnTile.add(unit0);
			break;

		case 1:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pureblade_enforcer, 1, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit1);
			unit1.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit1, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit1, 1);
			BasicCommands.setUnitHealth(out, unit1, 4);
			Run.myUnitOnTile.add(unit1);
			break;

		case 2:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit2 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 2, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit2);
			unit2.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit2, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit2, 3);
			BasicCommands.setUnitHealth(out, unit2, 2);
			Run.myUnitOnTile.add(unit2);
			break;

		case 3:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit3 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 3, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit3);
			unit3.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit3, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit3, 1);
			BasicCommands.setUnitHealth(out, unit3, 5);
			Run.myUnitOnTile.add(unit3);
			break;

		case 4:
			// spell, true strike
			BasicCommands.addPlayer1Notification(out, "casting truestrike...", 2);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), Run.tile[x][y]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int health = Run.tile[x][y].getUnit().getHealth();

			health = health - 2;
			if (health <= 0) {

				BasicCommands.addPlayer1Notification(out, "I killed u !", 2);
				deleteUnit(out, Run.tile[x][y].getUnit());

			} else {
				BasicCommands.setUnitHealth(out, Run.tile[x][y].getUnit(), health);
			}

			break;

		case 5:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit5 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald, 5, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit5);
			unit5.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit5, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit5, 1);
			BasicCommands.setUnitHealth(out, unit5, 4);
			Run.myUnitOnTile.add(unit5);

			//passive ability

			int h7 = Run.humanPlayer.getHealth() + 3;
			if(h7 > 20)	h7 = 20;
			BasicCommands.setUnitHealth(out,Run.avatar[0], h7); // will show on unit
			BasicCommands.addPlayer1Notification(out, "passive ability is triggered...", 2);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom)
					, Run.tile[Run.avatar[0].getPosition().getTilex()][Run.avatar[0].getPosition().getTiley()]);
			BasicCommands.addPlayer1Notification(out, "healing my king...", 2);
			Run.humanPlayer.setHealth(h7);
			BasicCommands.setPlayer1Health(out, Run.humanPlayer); // will show on avatar
			break;

		case 6:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit6 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian, 6, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit6);
			unit6.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit6, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit6, 3);
			BasicCommands.setUnitHealth(out, unit6, 10);
			Run.myUnitOnTile.add(unit6);
			break;

		case 7:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit7 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azurite_lion, 7, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit7);
			unit7.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit7, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit7, 2);
			BasicCommands.setUnitHealth(out, unit7, 3);
			Run.myUnitOnTile.add(unit7);
			break;

		case 8:
			// spell, sundrop elixir
			BasicCommands.addPlayer1Notification(out, "casting sundrop elixir...", 2);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom), Run.tile[x][y]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int health3 = Run.tile[x][y].getUnit().getHealth() + 5;
			if(health3 >= Run.tile[x][y].getUnit().getMaxHealth()) {
				health3 = Run.tile[x][y].getUnit().getMaxHealth();
			}

			BasicCommands.addPlayer1Notification(out, "healing you...", 2);
			BasicCommands.setUnitHealth(out, Run.tile[x][y].getUnit(), health3);
			break;

		case 9:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit9 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem, 9, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit9);
			unit9.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit9, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit9, 4);
			BasicCommands.setUnitHealth(out, unit9, 6);
			Run.myUnitOnTile.add(unit9);
			break;

		case 10:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit10 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 10, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit10);
			unit10.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit10, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit10, 1);
			BasicCommands.setUnitHealth(out, unit10, 5);
			Run.myUnitOnTile.add(unit10);
			break;

		case 11:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit11 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 11, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit11);
			unit11.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit11, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit11, 3);
			BasicCommands.setUnitHealth(out, unit11, 2);
			Run.myUnitOnTile.add(unit11);
			break;

		case 12:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit12 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger, 12, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit12);
			unit12.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit12, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit12, 1);
			BasicCommands.setUnitHealth(out, unit12, 3);
			Run.myUnitOnTile.add(unit12);
			break;

		case 13:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit13 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pureblade_enforcer, 13, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit13);
			unit13.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit13, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit13, 1);
			BasicCommands.setUnitHealth(out, unit13, 4);
			Run.myUnitOnTile.add(unit13);
			break;
		case 14:

			// spell, true strike
			BasicCommands.addPlayer1Notification(out, "casting truestrike...", 2);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), Run.tile[x][y]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int health2 = Run.tile[x][y].getUnit().getHealth();

			health2 = health2 - 2;

			if (health2 <= 0) {

				BasicCommands.addPlayer1Notification(out, "I killed u !", 2);
				deleteUnit(out, Run.tile[x][y].getUnit());

			} else {
				BasicCommands.setUnitHealth(out, Run.tile[x][y].getUnit(), health2);
			}
			break;

		case 15:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit15 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald, 15, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit15);
			unit15.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit15, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit15, 1);
			BasicCommands.setUnitHealth(out, unit15, 4);
			Run.myUnitOnTile.add(unit15);

			//passive ability
			int h8 = Run.humanPlayer.getHealth() + 3;
			if(h8 > 20)	h8 = 20;
			BasicCommands.setUnitHealth(out,Run.avatar[0], h8); // will show on unit
			BasicCommands.addPlayer1Notification(out, "passive ability is triggered...", 2);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom)
					, Run.tile[Run.avatar[0].getPosition().getTilex()][Run.avatar[0].getPosition().getTiley()]);
			BasicCommands.addPlayer1Notification(out, "healing my king...", 2);
			Run.humanPlayer.setHealth(h8);
			BasicCommands.setPlayer1Health(out, Run.humanPlayer); // will show on avatar
			break;

		case 16:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit16 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian, 16, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit16);
			unit16.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit16, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit16, 3);
			BasicCommands.setUnitHealth(out, unit16, 10);
			Run.myUnitOnTile.add(unit16);
			break;

		case 17:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit17 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azurite_lion, 17, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit17);
			unit17.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit17, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit17, 2);
			BasicCommands.setUnitHealth(out, unit17, 3);
			Run.myUnitOnTile.add(unit17);
			break;

		case 18:
			// spell, sundrop elixir
			BasicCommands.addPlayer1Notification(out, "casting sundrop elixir...", 2);

			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom), Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int health4 = Run.tile[x][y].getUnit().getHealth() + 5;
			if(health4 >= Run.tile[x][y].getUnit().getMaxHealth()) {
				health4 = Run.tile[x][y].getUnit().getMaxHealth();
			}

			BasicCommands.addPlayer1Notification(out, "healing you...", 2);
			BasicCommands.setUnitHealth(out, Run.tile[x][y].getUnit(), health4);
			break;

		case 19:
			BasicCommands.addPlayer1Notification(out, "summoning an unit...", 2);

			Unit unit19 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem, 19, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit19);
			unit19.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit19, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit19, 4);
			BasicCommands.setUnitHealth(out, unit19, 6);
			Run.myUnitOnTile.add(unit19);
			break;

		default:
			// delete handcard,

		}

	}

	public boolean hasEnoughMana(Card minicard) {

		if (minicard.getManacost() > Run.humanPlayer.getMana()) {
			return false;
		} else
			return true;
	}

	//================ action for units who have passive ability ==============//

	public void actionForNormalUnit(ActorRef out, int x, int y) {

		int lock2 = 0;

		if(lastUnit.attackTimes == 0) {
			for (int[] tile : moveTiles) {
				if (tile != null) {
					if (tile[0] == x && tile[1] == y) { // the clicked position is in the move range

						if (Run.tile[x][y].getHasUnit()) { // there is an unit on the position

							if (Run.tile[x][y].getUnit().getGroup() == 1) { // it is an enemy
								for (int[] tile2 : attackTiles) {
									if(tile2 != null) {

										if (tile2[0] == x && tile2[1] == y) { // ★★ 1st situation ---> the clicked ememy is in attack range

											attack(out, lastUnit, x, y); // ★ just attack
											lastUnit.attackTimes ++;
											lock2 = 1;
											break;
										}
									}

								}
								if (lock2 == 1)
									break;

								// ★★ 2nd situation ---> the clicked unit is in move range but not attack range
								if(lastUnit.moveTimes == 0) {

									int[] pos = new int[2];
									pos = preAttackMove(lastUnit, x, y); // get the new position

									if(!Run.tile[pos[0]][pos[1]].getHasUnit()) {
										move(out, lastUnit, pos[0], pos[1]); // ★ move first then attack
										lastUnit.moveTimes ++;
										try { Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();} // ★ give it time to move before attack
										attack(out, lastUnit, x, y);
										lastUnit.attackTimes ++;
									}

									break;
								}

							}

						} else if(lastUnit.moveTimes == 0){
							move(out, lastUnit, x, y); // ★ just move
							lastUnit.moveTimes ++;
							break;
						}
					}
				}
			}
		}else {
			lastUnit.attackTimes = 0;
			lastUnit.moveTimes = 0;
		}

	}

	public void actionForFireSplitter(ActorRef out, int x, int y) {


		for(int[] tile : allEnemy) {
			if(tile != null) {
				BasicCommands.drawTile(out, Run.tile[tile[0]][tile[1]], 0);
			}
		}

		if(lastUnit.attackTimes == 0) {
			for (int[] tile : allEnemy) {
				if (tile != null) {
					if (tile[0] == x && tile[1] == y) {

						attack(out, lastUnit, x, y);
						lastUnit.attackTimes ++;
						break;
					}
				}
			}
		}

		if(lastUnit.moveTimes == 0) {

			for(int[] tile : moveTiles) {
				if(tile != null) {
					if (tile[0] == x && tile[1] == y) {

						if (!Run.tile[x][y].getHasUnit()) {

							move(out, lastUnit, x, y);
							lastUnit.moveTimes ++;
						}
					}
				}
			}
		}

		allEnemy = null;
	}

	public void actionForAzuriteLion(ActorRef out, int x, int y) {

		int lock2 = 0;

		if(lastUnit.attackTimes < 2) {
			for (int[] tile : moveTiles) {
				if (tile != null) {
					if (tile[0] == x && tile[1] == y) { // the clicked position is in the move range

						if (Run.tile[x][y].getHasUnit()) { // there is an unit on the position

							if (Run.tile[x][y].getUnit().getGroup() == 1) { // it is an enemy
								for (int[] tile2 : attackTiles) {
									if(tile2 != null) {
										if (tile2[0] == x && tile2[1] == y) { // ★★ 1st situation ---> the clicked ememy is in attack range

											attack(out, lastUnit, x, y); // ★ just attack
											lastUnit.attackTimes ++;
											lock2 = 1;
											break;
										}
									}
								}
								if (lock2 == 1)
									break;

								// ★★ 2nd situation ---> the clicked unit is in move range but not attack range
								if(lastUnit.moveTimes == 0) {

									int[] pos = new int[2];
									pos = preAttackMove(lastUnit, x, y); // get the new position
									move(out, lastUnit, pos[0], pos[1]); // ★ move first then attack
									lastUnit.moveTimes ++;
									try { Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();} // ★ give it time to move before attack
									attack(out, lastUnit, x, y);
									lastUnit.attackTimes ++;
									break;
								}

							}

						} else if(lastUnit.moveTimes == 0){
							move(out, lastUnit, x, y); // ★ just move
							lastUnit.moveTimes ++;
							break;
						}
					}
				}
			}
		}
	}

}
