package cc.openhome;

import java.awt.Image;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import cc.openhome.component.TetrixBox;


public class TetrixPieceDemo extends JFrame {
    public TetrixPieceDemo() {
        super("Tetrix Piece Demo");
        
        final TetrixBox tetrixBox = new TetrixBox(4, 4, 25, 25);
        
        Image[] images = new Image[7];
        for(int i = 0; i < images.length; i++)
            images[i] = (new ImageIcon(TetrixPieceDemo.class.getResource((i+1) + ".jpg"))).getImage();
        
        tetrixBox.setTetrixImages(images);
        tetrixBox.setBackgroundColor(Color.white);
        tetrixBox.setBlockBorder(false);
        
        this.getContentPane().add(tetrixBox);
        
        this.setSize(tetrixBox.getWidth(), tetrixBox.getHeight() + 20);
        
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        tetrixBox.generate();
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
        new TetrixPieceDemo();
    }
}
