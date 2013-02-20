package cc.openhome.tetrix;

public class TetrixGround {
	private TetrixPiece tetrixPiece;
	private TetrixPiece testPiece;
	private int[][] ground;
	private int groundWidthInBlk, groundHeightInBlk;
	private int xOffset, yOffset;

	private int emptyLines;
	private int removedLines;
	private int score;

	private boolean isGameover;

	private boolean isOperable = true;

	public TetrixGround() {
		this(10, 20);
	}

	public TetrixGround(int groundWidthInBlk, int groundHeightInBlk) {
		this.groundWidthInBlk = groundWidthInBlk;
		this.groundHeightInBlk = groundHeightInBlk;

		tetrixPiece = new TetrixPiece();
		testPiece = new TetrixPiece();
		ground = new int[groundWidthInBlk][groundHeightInBlk];

		xOffset = groundWidthInBlk / 2 - 1;
		yOffset = 1;
		emptyLines = groundHeightInBlk;
	}

	public void addPieceOfType(int pieceType) {
		this.tetrixPiece.initialize(pieceType);
		this.testPiece.initialize(pieceType);

		xOffset = groundWidthInBlk / 2 - 1;
		if (tetrixPiece.getMinY() < 0)
			yOffset = Math.abs(tetrixPiece.getMinY());
		else
			yOffset = 0;

		for (int i = 0; i < 4; i++) {
			int x = tetrixPiece.getBlockXCoord(i) + xOffset;
			int y = tetrixPiece.getBlockYCoord(i) + yOffset;

			if (ground[x][y] != 0) {
				isGameover = true;
			}
		}
	}

	public void reset() {
		emptyLines = groundHeightInBlk;
		score = 0;
		removedLines = 0;
		isGameover = false;
		isOperable = true;

		xOffset = groundWidthInBlk / 2 - 1;
		if (tetrixPiece.getMinY() < 0)
			yOffset = Math.abs(tetrixPiece.getMinY());
		else
			yOffset = 0;

		for (int i = 0; i < groundWidthInBlk; i++)
			for (int j = 0; j < groundHeightInBlk; j++)
				ground[i][j] = 0;
	}

	public void moveTetrixLeft() {
		if (isMovable(-1, 0)) {
			xOffset--;
		}
	}

	public void moveTetrixRight() {
		if (isMovable(1, 0)) {
			xOffset++;
		}
	}

	public void moveTetrixDown() {
		if (isMovable(0, 1) && isOperable) {
			yOffset++;
		}
	}

	public void dropTetrix() {
		while (isMovable(0, 1) && isOperable)
			yOffset++;
	}

	public void rotateTetrix(boolean clockwise) {
		for (int i = 0; i < 4; i++) {
			testPiece.setBlockCoord(i, tetrixPiece.getBlockXCoord(i),
					tetrixPiece.getBlockYCoord(i));
		}

		if (clockwise)
			testPiece.rotateRight();
		else
			testPiece.rotateLeft();

		for (int i = 0; i < 4; i++) {
			int x = testPiece.getBlockXCoord(i) + xOffset;
			int y = testPiece.getBlockYCoord(i) + yOffset;

			if (x < 0 || x >= groundWidthInBlk || y >= groundHeightInBlk
					|| y < 0)
				return;

			if (ground[x][y] != 0)
				return;

		}

		for (int i = 0; i < 4; i++) {
			tetrixPiece.setBlockCoord(i, testPiece.getBlockXCoord(i),
					testPiece.getBlockYCoord(i));
		}
	}

	public boolean isMovable(int xStep, int yStep) {
		int x, y;
		for (int i = 0; i < 4; i++) {
			x = tetrixPiece.getBlockXCoord(i) + xOffset + xStep;
			y = tetrixPiece.getBlockYCoord(i) + yOffset + yStep;

			if (x < 0 || x >= groundWidthInBlk || y >= groundHeightInBlk)
				return false;

			if (ground[x][y] != 0)
				return false;
		}

		return true;
	}

	public void updateGround() {
		isOperable = false;

		if (!isGameover && !isMovable(0, 1)) {
			// touch the bottom of playground or the top of one piece
			for (int i = 0; i < 4; i++) {
				int x = tetrixPiece.getBlockXCoord(i) + xOffset;
				int y = tetrixPiece.getBlockYCoord(i) + yOffset;
				ground[x][y] = tetrixPiece.getType();
			}

			int pieceTop = tetrixPiece.getMinY() + yOffset;
			if (pieceTop < emptyLines)
				emptyLines = pieceTop;

			removeFullLines();
		}

		isOperable = true;
	}

	private void removeFullLines() {
		int count = 0;
		for (int j = groundHeightInBlk - 1; j > emptyLines; j--) {
			boolean isFullLine = true;
			for (int i = 0; i < groundWidthInBlk; i++) {
				if (ground[i][j] == 0) {
					isFullLine = false;
					break;
				}
			}

			if (isFullLine) {
				count++;
				for (int k = j; k > emptyLines; k--) {
					for (int i = 0; i < groundWidthInBlk; i++) {
						if (ground[i][k] != ground[i][k - 1]) {
							ground[i][k] = ground[i][k - 1];
						}
					}
				}

				for (int i = 0; i < groundWidthInBlk; i++) {
					ground[i][emptyLines] = 0;
				}
				emptyLines++;
				j++;

				removedLines++;
			}
		}

		score = score + (int) Math.pow(2, count);
	}

	public int getGroundHeightInBlk() {
		return groundHeightInBlk;
	}

	public int getGroundWidthInBlk() {
		return groundWidthInBlk;
	}

	public TetrixPiece getTetrixPiece() {
		return tetrixPiece;
	}

	public void setTetrixPiece(TetrixPiece tetrixPiece) {
		this.tetrixPiece = tetrixPiece;
	}

	public int[][] getGroundInfo() {
		return ground;
	}

	public boolean isGameover() {
		return isGameover;
	}

	public int getScore() {
		return score;
	}

	public int getRemovedLines() {
		return removedLines;
	}

	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}
}
