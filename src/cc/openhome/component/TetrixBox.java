package cc.openhome.component;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import javax.swing.JComponent;

import cc.openhome.tetrix.TetrixPiece;

public class TetrixBox extends JComponent {
	private int blockWidth, blockHeight;
	private TetrixPiece[] tetrixPieces;
	private TetrixPiece currentPiece;
	private Image tetrixImages[];
	private Image currentImage;
	private Color backgroundColor;
	private boolean isBlockBorder;

	public TetrixBox() {
		this(4, 4, 25, 25);
	}

	public TetrixBox(int boxWidthInBlk, int boxHeightInBlk, int blockWidth,
			int blockHeight) {
		super();
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;

		initTetrix();

		backgroundColor = Color.white;
		setSize(boxWidthInBlk * blockWidth, boxHeightInBlk * blockHeight);
	}

	private void initTetrix() {
		isBlockBorder = false;
		tetrixPieces = new TetrixPiece[7];
		for (int i = 0; i < tetrixPieces.length; i++) {
			tetrixPieces[i] = new TetrixPiece(i + 1);
		}
	}

	public void setTetrixImages(Image[] tetrixImages) {
		this.tetrixImages = tetrixImages;
	}

	public Image[] getTetrixImages() {
		return tetrixImages;
	}

	public void generate() {
		int index = (int) (Math.random() * 7);
		currentPiece = tetrixPieces[index];
		currentImage = tetrixImages[index];
		repaint();
	}

	public TetrixPiece getTetrixPiece() {
		return currentPiece;
	}

	public Image getTetrixImage() {
		return currentImage;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBlockBorder(boolean isBlockBorder) {
		this.isBlockBorder = isBlockBorder;
	}

	public boolean isBlockBorder() {
		return isBlockBorder;
	}

	public int getBlockWidth() {
		return blockWidth;
	}

	public int getBlockHeight() {
		return blockHeight;
	}

	private void drawBlock(Graphics g, int x, int y) {
		g.drawImage(currentImage, x * blockWidth, y * blockHeight, blockWidth,
				blockHeight, this);
		if (isBlockBorder) {
			g.setColor(Color.lightGray);
			g.drawRect(x * blockWidth, y * blockHeight, blockWidth, blockHeight);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		// draw box background
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0; i < 4; i++) {
			drawBlock(g, currentPiece.getBlockXCoord(i) + 1,
					currentPiece.getBlockYCoord(i) + 1);
		}
	}
}
