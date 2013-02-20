package cc.openhome;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import cc.openhome.component.TetrixBox;
import cc.openhome.component.TetrixStackArea;
import cc.openhome.tetrix.TetrixGround;


public class TetrixGroundDemo extends JFrame {
    public TetrixGroundDemo() {
        super("Tetrix Ground Demo");
        
        final TetrixBox tetrixBox = new TetrixBox(4, 4, 25, 25);
        
        Image[] images = new Image[7];
        for(int i = 0; i < images.length; i++)
            images[i] = (new ImageIcon(TetrixPieceDemo.class.getResource((i+1) + ".jpg"))).getImage();
        
        tetrixBox.setTetrixImages(images);
        tetrixBox.generate();
        
        final TetrixGround tetrixGround = new TetrixGround(10, 20);
        tetrixGround.addPieceOfType(tetrixBox.getTetrixPiece().getType());
        
        final TetrixStackArea stackArea = new TetrixStackArea(tetrixBox, tetrixGround);
        //tetrixGround.setObserver(stackArea);
        
        this.getContentPane().add(stackArea);
        
        this.setSize(stackArea.getWidth() + 10, stackArea.getHeight() + 25);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        new Thread(new Runnable() {
            public void run() {
                while(!tetrixGround.isGameover()) {
                    try {
                        if(tetrixGround.isMovable(0, 1)) {
                            tetrixGround.moveTetrixRight();
                            tetrixGround.moveTetrixDown();
                            tetrixGround.updateGround();
                            stackArea.repaint();
                        }
                        else {
                            tetrixGround.addPieceOfType(tetrixBox.getTetrixPiece().getType());
                            tetrixBox.generate();
                        }
                        Thread.sleep(100);
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        new TetrixGroundDemo();
    }
}
