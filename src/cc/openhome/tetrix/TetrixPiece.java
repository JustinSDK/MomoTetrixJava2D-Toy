package cc.openhome.tetrix;

public class TetrixPiece {
	// seven types of pieces
	public static final int pieceTypes[][][] = {
			{ { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },

			{ { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },

			{ { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },

			{ { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },

			{ { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },

			{ { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },

			{ { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } } };

	private int pieceType;
	// four blocks per piece
	// coordinates[i][j]
	// i for number of block, j for x-coord and y-coord respectively
	private int[][] coordinates = new int[4][2];

	public TetrixPiece() {
		initialize((int) (Math.random() * 7 + 1));
	}

	public TetrixPiece(int type) {
		initialize(type);
	}

	public void rotateLeft() {
		// do not rotate square piece
		if (pieceType == 5)
			return;

		int tmp;
		for (int i = 0; i < 4; i++) {
			tmp = getBlockXCoord(i);
			setBlockXCoord(i, getBlockYCoord(i));
			setBlockYCoord(i, -tmp);
		}
	}

	public void rotateRight() {
		// do not rotate square piece
		if (pieceType == 5)
			return;

		int tmp;
		for (int i = 0; i < 4; i++) {
			tmp = getBlockXCoord(i);
			setBlockXCoord(i, -getBlockYCoord(i));
			setBlockYCoord(i, tmp);
		}
	}

	// get the type of piece
	public int getType() {
		return pieceType;
	}

	public int getBlockXCoord(int index) {
		return coordinates[index][0];
	}

	public int getBlockYCoord(int index) {
		return coordinates[index][1];
	}

	public int getMinX() {
		int tmp = coordinates[0][0];

		for (int i = 1; i < 4; i++)
			if (tmp > coordinates[i][0])
				tmp = coordinates[i][0];

		return tmp;
	}

	public int getMaxX() {
		int tmp = coordinates[0][0];

		for (int i = 1; i < 4; i++)
			if (tmp < coordinates[i][0])
				tmp = coordinates[i][0];

		return tmp;
	}

	public int getMinY() {
		int tmp = coordinates[0][1];

		for (int i = 1; i < 4; i++)
			if (tmp > coordinates[i][1])
				tmp = coordinates[i][1];

		return tmp;
	}

	public int getMaxY() {
		int tmp = coordinates[0][1];

		for (int i = 1; i < 4; i++)
			if (tmp < coordinates[i][1])
				tmp = coordinates[i][1];

		return tmp;
	}

	public void setBlockXCoord(int index, int value) {
		coordinates[index][0] = value;
	}

	public void setBlockYCoord(int index, int value) {
		coordinates[index][1] = value;
	}

	public void setBlockCoord(int index, int x, int y) {
		coordinates[index][0] = x;
		coordinates[index][1] = y;
	}

	public void initialize(int type) {
		if (type < 1 || type > 7)
			type = 1;

		// set coord. for each block
		pieceType = type;
		for (int i = 0; i < 4; i++) {
			coordinates[i][0] = pieceTypes[type - 1][i][0];
			coordinates[i][1] = pieceTypes[type - 1][i][1];
		}
	}
}
