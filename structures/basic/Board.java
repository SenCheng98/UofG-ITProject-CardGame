package structures.basic;

import java.util.Arrays;

public class Board { // calculate attack / move range of a unit
	// 9 * 5;
	static int maxX = 8; // the number of tiles in one row is 9
	static int maxY = 4; // the number of tiles in one colum is 5;

	public static int[][] getMoveRangeTiles(int x, int y) { // input the coordinate of the unit, show the move range

		//     8
		//   0 1 2
		// 9 3   4 10
		//   5 6 7
		//    11

		int[][] tiles = Arrays.copyOf(getAttackRangeTiles(x, y), 12); // max=12

		if (y > 1) {
			tiles[8] = new int[2]; // tile8
			tiles[8][0] = x;
			tiles[8][1] = y - 2;
		}

		if (y < maxY - 1) {
			tiles[11] = new int[2]; // tile11
			tiles[11][0] = x;
			tiles[11][1] = y + 2;
		}

		if (x > 1) {
			tiles[9] = new int[2]; // tile9
			tiles[9][0] = x - 2;
			tiles[9][1] = y;
		}

		if (x < maxX - 1) {
			tiles[10] = new int[2]; // tile10
			tiles[10][0] = x + 2;
			tiles[10][1] = y;
		}

		return tiles;
	}

	public static int[][] getAttackRangeTilesByLoops(int x, int y) {
			int[][] tiles = new int[8][]; // max=8
			int count = -1;    //label the tiles

			for (int x0 = -1; x0 <= 1; x0++) {
				for (int y0 = -1; y0 <= 1; y0++) {
						if ((x0 == 0) && (y0 == 0)) continue;
						count++;
						if ((x + x0 >= 0) && (x + x0 <= maxX) && (y + y0 >= 0) && (y + y0 <= maxY)) {
								tiles[count] = new int[2];
								tiles[count][0] = x + x0;
								tiles[count][1] = y + y0;
						}
				}
			}

			return tiles;
	}

	public static int[][] getAttackRangeTiles(int x, int y) { // input the coordinate of the unit, show the attack range
		//
		// 0 1 2
		// 3   4
		// 5 6 7
		//

		int[][] tiles = new int[8][]; // max=8

		if (y > 0) {
			tiles[1] = new int[2]; // tile1
			tiles[1][0] = x;
			tiles[1][1] = y - 1;
			if (x > 0) {
				tiles[0] = new int[2]; // tile0
				tiles[0][0] = x - 1;
				tiles[0][1] = y - 1;
			}
			if (x < maxX) {
				tiles[2] = new int[2]; // tile2
				tiles[2][0] = x + 1;
				tiles[2][1] = y - 1;
			}
		}

		if (y < maxY) {
			tiles[6] = new int[2]; // tile2
			tiles[6][0] = x;
			tiles[6][1] = y + 1;
			if (x > 0) {
				tiles[5] = new int[2]; // tile5
				tiles[5][0] = x - 1;
				tiles[5][1] = y + 1;
			}
			if (x < maxX) {
				tiles[7] = new int[2]; // tile7
				tiles[7][0] = x + 1;
				tiles[7][1] = y + 1;
			}
		}

		if (x > 0) {
			tiles[3] = new int[2]; // tile3
			tiles[3][0] = x - 1;
			tiles[3][1] = y;
		}
		if (x < maxX) {
			tiles[4] = new int[2]; // tile4
			tiles[4][0] = x + 1;
			tiles[4][1] = y;
		}
		return tiles;
	}
}
