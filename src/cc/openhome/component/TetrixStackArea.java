package cc.openhome.component;

import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JComponent;

import cc.openhome.tetrix.TetrixGround;

public class TetrixStackArea extends JComponent {
	private TetrixBox tetrixBox;
	private TetrixGround tetrixGround;
	private Color backgroundColor;
	private boolean isBlockBorder;

	public TetrixStackArea(TetrixBox tetrixBox, TetrixGround tetrixGround) {
		this.tetrixBox = tetrixBox;
		this.tetrixGround = tetrixGround;
		backgroundColor = Color.white;
		setSize(tetrixGround.getGroundWidthInBlk() * tetrixBox.getBlockWidth(),
				tetrixGround.getGroundHeightInBlk()
						* tetrixBox.getBlockHeight());
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

	private void drawBlock(Graphics g, Image image, int x, int y) {
		g.drawImage(image, x * tetrixBox.getBlockWidth(),
				y * tetrixBox.getBlockHeight(), tetrixBox.getBlockWidth(),
				tetrixBox.getBlockHeight(), this);

		if (isBlockBorder) {
			g.setColor(Color.lightGray);
			g.drawRect(x * tetrixBox.getBlockWidth(),
					y * tetrixBox.getBlockHeight(), tetrixBox.getBlockWidth(),
					tetrixBox.getBlockHeight());
		}
	}

	public void paint(Graphics g) {
		// clean previous screen
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		// draw current piece
		for (int i = 0; i < 4; i++) {
			drawBlock(g, tetrixBox.getTetrixImages()[tetrixGround
					.getTetrixPiece().getType() - 1],
					tetrixGround.getTetrixPiece().getBlockXCoord(i)
							+ tetrixGround.getXOffset(),
					tetrixGround.getTetrixPiece().getBlockYCoord(i)
							+ tetrixGround.getYOffset());
		}

		// draw stack of pieces
		for (int i = 0; i < tetrixGround.getGroundWidthInBlk(); i++) {
			for (int j = 0; j < tetrixGround.getGroundHeightInBlk(); j++) {
				if (tetrixGround.getGroundInfo()[i][j] == 0)
					continue;

				drawBlock(g,
						tetrixBox.getTetrixImages()[tetrixGround
								.getGroundInfo()[i][j] - 1], i, j);
			}
		}

		if (tetrixGround.isGameover()) {
			g.setFont(new Font("Arial Black", Font.ITALIC | Font.BOLD,
					tetrixBox.getBlockWidth()));
			g.setColor(Color.black);
			g.drawString("Game Over", getWidth() / 6, getHeight() / 2);
			g.setColor(Color.red);
			g.drawString("Game Over", getWidth() / 6 + 2, getHeight() / 2 + 2);

		}
	}
}
