package cc.openhome;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import cc.openhome.component.TetrixBox;
import cc.openhome.component.TetrixStackArea;
import cc.openhome.tetrix.*;

public class MomoTetrix extends JFrame {
    private TetrixBox tetrixBox;
    private TetrixGround tetrixGround;
    private JLabel levelLabel, lineLabel, scoreLabel;
    private JLabel topLevelLabel, topLineLabel, topScoreLabel;
    private TetrixStackArea stackArea;
    private JTextField keyFocus;
    private JButton newGameBtn, pauseBtn;
    private Icon logoIcon;
    
    private boolean isRestart = true;
    private boolean isPause;
    
    private int level = 1;
    private int speed = 1000;
    
    private Thread gameThread;

    public MomoTetrix() {
        super("Momo Tetrix");
        initResource();
        setupUIComponent();
        setupEventListener();
        setVisible(true);
        
    }

    public void initResource() {
        logoIcon = new ImageIcon(MomoTetrix.class.getResource("logo.jpg"));
        
        Image[] images = new Image[7];
        for(int i = 0; i < images.length; i++)
            images[i] = (new ImageIcon(MomoTetrix.class.getResource((i+1) + ".jpg"))).getImage();
        
        tetrixBox = new TetrixBox(4, 4, 25, 25);
        tetrixBox.setTetrixImages(images);
        // generate the first piece
        tetrixBox.generate();
        
        tetrixGround = new TetrixGround(10, 20);
        tetrixGround.addPieceOfType(tetrixBox.getTetrixPiece().getType());
        
        stackArea = new TetrixStackArea(tetrixBox, tetrixGround);
        
        tetrixBox.generate();
    }
    
    public void setupUIComponent() {
        this.getContentPane().setLayout(null);
        
        JPanel nextPiecePanel = new JPanel();
        nextPiecePanel.setLayout(null);
        nextPiecePanel.setBorder(BorderFactory.createTitledBorder("Next Piece")); 
        nextPiecePanel.setLocation(2, 2);
        nextPiecePanel.setSize(tetrixBox.getWidth() + 20, tetrixBox.getHeight() + 30);
        
        tetrixBox.setLocation(10, 20); 
        nextPiecePanel.add(tetrixBox);
        
        this.getContentPane().add(nextPiecePanel);
        
        JPanel scorePanel = new JPanel();
        scorePanel.setLocation(2, 150);
        scorePanel.setSize(tetrixBox.getWidth() + 20, tetrixBox.getHeight());
        scorePanel.setBorder(BorderFactory.createTitledBorder("Oh..."));
        scorePanel.add(new JLabel("　Level　"));
        scorePanel.add(levelLabel = new JLabel(level+""));
        scorePanel.add(new JLabel("　Line　  "));
        scorePanel.add(lineLabel = new JLabel(tetrixGround.getRemovedLines()+""));
        scorePanel.add(new JLabel("　Score　"));
        scorePanel.add(scoreLabel = new JLabel(tetrixGround.getScore()+""));
        
        this.getContentPane().add(scorePanel);
        
        JPanel btnPanel = new JPanel();
        btnPanel.setSize(tetrixBox.getWidth() + 20, tetrixBox.getHeight());
        btnPanel.setBorder(BorderFactory.createTitledBorder("Fun..."));
        btnPanel.setLocation(2, 270);
        
        newGameBtn = new JButton("New Game");
        newGameBtn.setSize(60, 30);
        pauseBtn = new JButton("Pause");
        pauseBtn.setSize(60, 30);
        
        btnPanel.add(newGameBtn);
        btnPanel.add(pauseBtn);
        
        this.getContentPane().add(btnPanel);
        
        JPanel topPanel = new JPanel();
        topPanel.setSize(tetrixBox.getWidth() + 20, tetrixBox.getHeight());
        topPanel.setBorder(BorderFactory.createTitledBorder("Top One"));
        topPanel.setLocation(2, 390);
        
        topPanel.add(new JLabel("　Level　"));
        topPanel.add(topLevelLabel = new JLabel(level+""));
        topPanel.add(new JLabel("　Line　  "));
        topPanel.add(topLineLabel = new JLabel(tetrixGround.getRemovedLines()+""));
        topPanel.add(new JLabel("　Score　"));
        topPanel.add(topScoreLabel = new JLabel(tetrixGround.getScore()+""));
        
        this.getContentPane().add(topPanel);
        
        JPanel stackAreaPanel = new JPanel();
        stackAreaPanel.setLayout(null); 
        stackAreaPanel.setBorder(BorderFactory.createTitledBorder(""));
        stackAreaPanel.setLocation(130, 10);
        stackAreaPanel.setSize(stackArea.getWidth() + 20, stackArea.getHeight() + 20);
        
        stackArea.setLocation(10, 10);
        stackAreaPanel.add(stackArea);
        
        this.getContentPane().add(stackAreaPanel);
        
        keyFocus = new JTextField(); 
        keyFocus.setEditable(false);
        keyFocus.setLocation(130, 10);
        
        this.getContentPane().add(keyFocus);
        
        this.setSize(420, 570);
    }
    
    private void setupEventListener() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        keyFocus.addKeyListener(
                new KeyListener() {
                    public void keyPressed(KeyEvent e) {
                        if(isRestart || tetrixGround.isGameover() || isPause)
                            return;
                        
                        switch(e.getKeyCode()) {
                            case KeyEvent.VK_RIGHT:
                                tetrixGround.moveTetrixRight();
                                break;
                            case KeyEvent.VK_LEFT:
                                tetrixGround.moveTetrixLeft();                                
                                break;
                            case KeyEvent.VK_UP:
                                tetrixGround.rotateTetrix(false);
                                break;
                            case KeyEvent.VK_DOWN:
                                tetrixGround.rotateTetrix(true);
                                break;
                            case KeyEvent.VK_SPACE:
                                tetrixGround.dropTetrix();
                                break;
                        }
                        
                        stackArea.repaint();
                    }

                    public void keyTyped(KeyEvent e) {}
                    public void keyReleased(KeyEvent e) {}
                }
            );
        
        newGameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isRestart = true;
                keyFocus.requestFocus();
                
                // wait for 1 seconds to end the previous game
                try {
                    Thread.sleep(1000);
                }
                catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
                
                gameThread = new Thread(new Runnable() {
                    public void run() {
                        while(!tetrixGround.isGameover() && !isRestart) {
                            if(!isPause) {
                                try {
                                    tetrixGround.moveTetrixDown();
                                    tetrixGround.updateGround();
                                    
                                    if(!tetrixGround.isMovable(0, 1)){ 
                                        tetrixGround.addPieceOfType(tetrixBox.getTetrixPiece().getType());
                                        tetrixBox.generate();
                                        lineLabel.setText(tetrixGround.getRemovedLines()+"");
                                        scoreLabel.setText(tetrixGround.getScore()+"");
                                    }

                                    stackArea.repaint();
                                    
                                    if((tetrixGround.getScore() / 50) > (level - 1)) {
                                        level++;
                                        if(speed > 200)
                                            speed -= 100;
                                        
                                        levelLabel.setText(level+"");
                                    }
                                    
                                    Thread.sleep(speed);
                                }
                                catch(InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            else {
                                try {
                                    synchronized(this) {
                                        wait();
                                    }
                                }
                                catch(InterruptedException ex) {

                                }
                            }
                        }
                    }
                });
                
                level = 1;
                speed = 1000;
                tetrixGround.reset();
                levelLabel.setText(level+"");
                lineLabel.setText(tetrixGround.getRemovedLines()+"");
                scoreLabel.setText(tetrixGround.getScore()+"");
                
                isRestart = false;
                gameThread.start();
            }
        });
        
        pauseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPause = true;
                JOptionPane.showOptionDialog(null,
                        "It's only for fun.... :)\n" +
                        "http://openhome.cc",
                        "About Momo Tetrix",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        logoIcon, null, null);
                keyFocus.requestFocus();
                
                isPause = false;
                
                gameThread.interrupt();
            }
        });
    }
    
    public static void main(String[] args) {
        new MomoTetrix();
    }
}
