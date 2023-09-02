package events;

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

public class AITurn {

	public static int it;

	public static void allAction(ActorRef out, GameState gameState) {

		selectCard(out);

		unitAction(out);

		endTurn(out, gameState);

	}


	public static void selectCard(ActorRef out) {

		int times = EndTurnClicked.setAiCard;
		int mana;
		int sum = 0;
		int aiCardId;
		int[] location = new int[2];

		if(times > Run.aiCard.size()) {
			times = Run.aiCard.size();
		}

		for(int i=0; i<times; i++) {

			if(Run.aiCard != null && Run.aiUnitOnTile.size() < 8) {
				mana = Run.aiPlayer.getMana() - Run.aiCard.get(i - sum).getManacost();
				if(mana >= 0) {
					Run.aiPlayer.setMana(mana);
					BasicCommands.setPlayer2Mana(out, Run.aiPlayer);

					aiCardId = Run.aiCard.get(i - sum).getId();
					location = preSummon(aiCardId);
					summonAi(out, aiCardId, location[0], location[1]);
					try {Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace();}
					Run.aiCard.remove(i - sum);
					sum ++;
				}
			}

		}

	}

	public static void summonAi(ActorRef out, int cardId, int x, int y) {


		switch (cardId) {

		case 20:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit20 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_rock_pulveriser, 20, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit20);
			unit20.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit20, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit20, 1);
			BasicCommands.setUnitHealth(out, unit20, 4);
			Run.aiUnitOnTile.add(unit20);
			break;

		case 21:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit21 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_bloodshard_golem, 21, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit21);
			unit21.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit21, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit21, 4);
			BasicCommands.setUnitHealth(out, unit21, 6);
			Run.aiUnitOnTile.add(unit21);
			break;

		case 22:
			//spell,Staff of Y’Kir, Add +2 attack to your avatar

			BasicCommands.addPlayer1Notification(out, "Staff of Y’Kir...avatar attack + 2", 2);

			int avtX = Run.avatar[1].getPosition().getTilex();
			int avtY = Run.avatar[1].getPosition().getTiley();
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), Run.tile[avtX][avtY]);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int attack = Run.avatar[1].getAttack() + 2;

			BasicCommands.setUnitAttack(out, Run.avatar[1], attack);

			break;

		case 23:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit23 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_blaze_hound, 23, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit23);
			unit23.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit23, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit23, 4);
			BasicCommands.setUnitHealth(out, unit23, 3);
			Run.aiUnitOnTile.add(unit23);
			break;

		case 24:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit24 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_windshrike, 24, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit24);
			unit24.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit24, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit24, 4);
			BasicCommands.setUnitHealth(out, unit24, 3);
			Run.aiUnitOnTile.add(unit24);
			break;

		case 25:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit25 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 25, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit25);
			unit25.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit25, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit25, 2);
			BasicCommands.setUnitHealth(out, unit25, 1);
			Run.aiUnitOnTile.add(unit25);
			break;

		case 26:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit26 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti, 26, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit26);
			unit26.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit26, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit26, 7);
			BasicCommands.setUnitHealth(out, unit26, 4);
			Run.aiUnitOnTile.add(unit26);
			break;

		case 27:
			//spell

			if(Run.myUnitOnTile.size() > 1) {

				BasicCommands.addPlayer1Notification(out, "Entropic Decay...kill an unit", 2);
				BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), Run.tile[x][y]);

				try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

				TileClicked.deleteUnit(out, Run.tile[x][y].getUnit());
			}else {

				BasicCommands.addPlayer1Notification(out, "Entropic Decay...", 2);
				try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
				BasicCommands.addPlayer1Notification(out, "There is no non-avatar unit on board", 2);
			}

			break;

		case 28:
			//Can be summoned anywhere on the board

			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit28 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_planar_scout, 28, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit28);
			unit28.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit28, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit28, 2);
			BasicCommands.setUnitHealth(out, unit28, 1);
			Run.aiUnitOnTile.add(unit28);
			break;

		case 29:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit29 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golemR, 29, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit29);
			unit29.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit29, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit29, 4);
			BasicCommands.setUnitHealth(out, unit29, 6);
			Run.aiUnitOnTile.add(unit29);
			break;

		case 30:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit30 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_rock_pulveriser, 30, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit30);
			unit30.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit30, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit30, 1);
			BasicCommands.setUnitHealth(out, unit30, 4);
			Run.aiUnitOnTile.add(unit30);
			break;

		case 31:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit31 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_bloodshard_golem, 31, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit31);
			unit31.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit31, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit31, 4);
			BasicCommands.setUnitHealth(out, unit31, 6);
			Run.aiUnitOnTile.add(unit31);
			break;

		case 32:

			//spell,Staff of Y’Kir, Add +2 attack to your avatar

			BasicCommands.addPlayer1Notification(out, "Staff of Y’Kir...avatar attack + 2", 2);

			int avtX2 = Run.avatar[1].getPosition().getTilex();
			int avtY2 = Run.avatar[1].getPosition().getTiley();
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), Run.tile[avtX2][avtY2]);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int attack2 = Run.avatar[1].getAttack() + 2;

			BasicCommands.setUnitAttack(out, Run.avatar[1], attack2);

			break;

		case 33:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit33 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_blaze_hound, 33, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit33);
			unit33.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit33, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit33, 4);
			BasicCommands.setUnitHealth(out, unit33, 3);
			Run.aiUnitOnTile.add(unit33);
			break;

		case 34:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit34 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_windshrike, 34, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit34);
			unit34.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit34, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit34, 4);
			BasicCommands.setUnitHealth(out, unit34, 3);
			Run.aiUnitOnTile.add(unit34);
			break;

		case 35:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit35 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 35, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit35);
			unit35.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit35, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit35, 2);
			BasicCommands.setUnitHealth(out, unit35, 1);
			Run.aiUnitOnTile.add(unit35);
			break;

		case 36:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit36 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti, 36, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit36);
			unit36.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit36, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit36, 7);
			BasicCommands.setUnitHealth(out, unit36, 4);
			Run.aiUnitOnTile.add(unit36);
			break;

		case 37:
			//spell

			if(Run.myUnitOnTile.size() > 1) {

				BasicCommands.addPlayer1Notification(out, "Entropic Decay...kill an unit", 2);
				BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), Run.tile[x][y]);

				try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

				TileClicked.deleteUnit(out, Run.tile[x][y].getUnit());
			}else {

				BasicCommands.addPlayer1Notification(out, "Entropic Decay...", 2);
				try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
				BasicCommands.addPlayer1Notification(out, "There is no non-avatar unit on board", 2);
			}

			break;

		case 38:
			//Can be summoned anywhere on the board
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit38 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_planar_scout, 38, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit38);
			unit38.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit38, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit38, 2);
			BasicCommands.setUnitHealth(out, unit38, 1);
			Run.aiUnitOnTile.add(unit38);
			break;

		case 39:
			BasicCommands.addPlayer1Notification(out, "summoning an ai unit...", 2);

			Unit unit39 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golemR, 39, Unit.class);
			BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon), Run.tile[x][y]);
			BasicCommands.setAllyOrEnemy(unit39);
			unit39.setPositionByTile(Run.tile[x][y]);
			BasicCommands.drawUnit(out, unit39, Run.tile[x][y]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BasicCommands.setUnitAttack(out, unit39, 4);
			BasicCommands.setUnitHealth(out, unit39, 6);
			Run.aiUnitOnTile.add(unit39);
			break;
		}
	}

	public static int[] preSummon(int cardId) {

		int a = Run.myUnitOnTile.size() + Run.aiUnitOnTile.size();

		int[][] location1 = new int[47 - a][2]; //max number, 45 - a
		int[][] allAlly = new int[Run.aiUnitOnTile.size()][2]; //max number, 45 - a
		int[][] location2 = null;

		int[] summonLoc = new int[2];

		if(cardId == 28 || cardId == 38) {

			int q= 0;
			for(int x=0;x<9;x++) {
				for(int y=0;y<5;y++) {
					if(!Run.tile[x][y].getHasUnit()) {

						location1[q][0] = x;
						location1[q][1] = y;
						q++;
					}
				}
			}

			summonLoc = bestLoc(location1);
		}else if(cardId == 27 || cardId == 37) {	// kill anyone unit

			for(Unit a2 : Run.myUnitOnTile) {
				if(a2 != null) {
					if(a2.getId() != 99) {
						summonLoc[0] = a2.getPosition().getTilex();
						summonLoc[1] = a2.getPosition().getTiley();
						break;
					}
				}

			}

		}else {

			for(int i=0;i<Run.aiUnitOnTile.size();i++) {
				allAlly[i][0] = Run.aiUnitOnTile.get(i).getPosition().getTilex();
				allAlly[i][1] = Run.aiUnitOnTile.get(i).getPosition().getTiley();
				location2 = Board.getAttackRangeTiles(allAlly[i][0], allAlly[i][1]);
			}

			summonLoc = bestLoc(location2);
		}
		return summonLoc;
	}



	public static int[] bestLoc(int[][] location){

		int[] bestLoc = new int[2];
		int avatarX = 0;
		int avatarY = 0;
		int bestX = 0;
		int bestY = 0;

		int bestDistance = 150;

		for(Unit a : Run.myUnitOnTile) {
			if(a.getId() == 99) {

				avatarX = a.getPosition().getTilex();
				avatarY = a.getPosition().getTiley();
				break;
			}else {
				System.err.println("dont get human avatar!");
				break;
			}
		}

		for(int[] loc : location) {
			if(loc != null) {

				if(!Run.tile[loc[0]][loc[1]].getHasUnit()) {
					int distance = (loc[0] - avatarX) * (loc[0] - avatarX) + (loc[1] - avatarY) * (loc[1] - avatarY);
					if(distance < bestDistance) {
						bestDistance = distance;
						bestX = loc[0];
						bestY = loc[1];
					}
				}

			}
		}
		bestLoc[0] = bestX;
		bestLoc[1] = bestY;
		return bestLoc;
	}

	public static int[] SurroundTrendLoc(int[][] location){
		//Which randomly spread the unit to the distance beyond one move & attack of player
		int[] bestLoc = new int[2];

		int avatarX = 0;
		int avatarY = 0;


		int moveAttackRangeOfUnit = 10;

		for (Unit a : Run.myUnitOnTile) {
			if (a.getId() == 99) {
					avatarX = a.getPosition().getTilex();
					avatarY = a.getPosition().getTiley();
					break;
			} else {
					System.err.println("cant get human avatar!");
					break;
			}
		}

		for (int[] loc : location) {
				if (loc != null) {
					if (!Run.tile[loc[0]][loc[1]].getHasUnit()) {
							int distance = (loc[0] - avatarX) * (loc[0] - avatarX) + (loc[1] - avatarY) * (loc[1] - avatarY);
							if (distance < moveAttackRangeOfUnit) {
								bestLoc[0] = loc[0];
								bestLoc[1] = loc[1];
								return bestLoc;
							}
					}

			}
		}
		return bestLoc;
	}


	public static int[] RadicalSurroundLoc(int[][] location){
		//Which randomly spread the unit to the distance beyond one move & attack of player
		int[] bestLoc = new int[2];

		int avatarX = 0;
		int avatarY = 0;


		int moveAttackRangeOfUnit = 3;

		for (Unit a : Run.myUnitOnTile) {
			if (a.getId() == 99) {
					avatarX = a.getPosition().getTilex();
					avatarY = a.getPosition().getTiley();
					break;
			} else {
					System.err.println("cant get human avatar!");
					break;
			}
		}

		for (int[] loc : location) {
				if (loc != null) {
					if (!Run.tile[loc[0]][loc[1]].getHasUnit()) {
							int distance = (loc[0] - avatarX) * (loc[0] - avatarX) + (loc[1] - avatarY) * (loc[1] - avatarY);
							if (distance < moveAttackRangeOfUnit) {
								bestLoc[0] = loc[0];
								bestLoc[1] = loc[1];
								return bestLoc;
							}
					}

			}
		}
		return bestLoc;
	}

	public static void unitAction(ActorRef out) {

		int[][] attackRange = null;
		int[][] moveRange = null;

		int moveX = -1;
		int moveY = -1;

		boolean inAtt = false;
		Unit a;
		for(it=0;it<Run.aiUnitOnTile.size();it++) {

			if(it < 0)	break;
			a = Run.aiUnitOnTile.get(it);
			if(a != null) {
				if(a.canAct) {

					// different unit has different passive ability, we should classify them

					if(a.getId() == 25 || a.getId() ==35) {		// Pyromancer,Can attack any enemy on the board

						TileClicked.attack(out, a, Run.avatar[0].getPosition().getTilex(), Run.avatar[0].getPosition().getTiley());


					}else if(a.getId() == 24 || a.getId() ==34) {	//Windshrike,  Can move anywhere on the board

						for(int x=0;x<9;x++) {
							for(int y=0;y<5;y++) {
								if(!Run.tile[x][y].getHasUnit()) {
									moveX = x;
									moveY = y;
									break;
								}
							}
						}
						TileClicked.move(out, a, moveX, moveY);
						try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
						attackRange = Board.getAttackRangeTiles(moveX, moveY);
						for(int[] a1 : attackRange) {

							if(a1 != null) {
								if(Run.tile[a1[0]][a1[1]].getHasUnit() && Run.tile[a1[0]][a1[1]].getUnit().getGroup() == 0) {
									TileClicked.attack(out, a, a1[0], a1[1]);
									break;
								}
							}

						}

					}else if(a.getId() == 26 || a.getId() ==36) { //Serpenti, Can attack twice per turn

						moveRange = Board.getMoveRangeTiles(a.getPosition().getTilex(), a.getPosition().getTiley());
						attackRange = Board.getAttackRangeTiles(a.getPosition().getTilex(), a.getPosition().getTiley());

							for(int[] a1 : moveRange) {

								if(a1 != null) {

									if(Run.tile[a1[0]][a1[1]].getHasUnit() && Run.tile[a1[0]][a1[1]].getUnit().getGroup() == 0) {

										for(int[] a2 : attackRange) {
											if(a2 != null) {

												if(a1[0] == a2[0] && a1[1] == a2[1]) {
													TileClicked.attack(out, a, a2[0], a2[1]);
													try { Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
													System.out.println("a....t....t.....k");
													inAtt = true;
													break;
												}
											}
										}

										if(!inAtt) {

											int[] pos = new int[2];
											pos = TileClicked.preAttackMove(a, a1[0], a1[1]); // get the new position

											if(!Run.tile[pos[0]][pos[1]].getHasUnit()) {
												TileClicked.move(out, a, pos[0], pos[1]); // ★ move first then attack
												a.moveTimes ++;
												try { Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();} // ★ give it time to move before attack
												TileClicked.attack(out, a, a1[0], a1[1]);
												a.attackTimes ++;

												System.out.println("m....o....v.....e2");
												break;
											}
										}

										if(a.attackTimes < 2) {
											continue;
										}else {
											break;
										}


									}
								}

							}

						a.attackTimes = 0;
						a.moveTimes = 0;


					}else { // normal units

						moveRange = Board.getMoveRangeTiles(a.getPosition().getTilex(), a.getPosition().getTiley());
						attackRange = Board.getAttackRangeTiles(a.getPosition().getTilex(), a.getPosition().getTiley());

						for(int[] a1 : moveRange) {

							if(a1 != null) {

								if(Run.tile[a1[0]][a1[1]].getHasUnit() && Run.tile[a1[0]][a1[1]].getUnit().getGroup() == 0) {

									for(int[] a2 : attackRange) {
										if(a2 != null) {

											if(a1[0] == a2[0] && a1[1] == a2[1]) {
												TileClicked.attack(out, a, a2[0], a2[1]);
												try { Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
												System.out.println("a....t....t.....k");
												inAtt = true;
												break;
											}
										}
									}

									if(!inAtt) {

										int[] pos = new int[2];
										pos = TileClicked.preAttackMove(a, a1[0], a1[1]); // get the new position

										if(!Run.tile[pos[0]][pos[1]].getHasUnit()) {
											TileClicked.move(out, a, pos[0], pos[1]); // ★ move first then attack
											a.moveTimes ++;
											try { Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();} // ★ give it time to move before attack
											TileClicked.attack(out, a, a1[0], a1[1]);
											a.attackTimes ++;

											System.out.println("m....o....v.....e2");
											break;
										}
									}


								}
							}

						}

						a.attackTimes = 0;
						a.moveTimes = 0;

					}

				}
			}

		}

		attackRange = null;
		moveRange = null;

	}

	public static void endTurn(ActorRef out, GameState gameState) {

		BasicCommands.addPlayer1Notification(out, "ai turn end...", 2);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

		if(Run.humanPlayer.getHealth() <= 0 || Run.aiPlayer.getHealth() <= 0) {
			gameState.something = false;
		}
		if (gameState.something) {

			EndTurnClicked.turnCount++; // times of click
			EndTurnClicked.manaCount(out);
			EndTurnClicked.cardNumber(out, gameState);

		}
	}

}
