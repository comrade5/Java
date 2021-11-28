package view;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import model.Direction;
import model.Game;
import javax.swing.Timer;

public class MainWindow extends JFrame{
    private final Game game;
    private Board board;
    private final JLabel gameStatLabel; 
    private Timer newFrameTimer;
    private final int FPS = 240;
    public int level = 1;
    private JLabel timeLabel;
    private long startTime;
    private Timer timer;
    private Direction dragonDirection = getRandomDirection();
    
    public MainWindow() throws IOException{
        game = new Game();
        
        setTitle("Labyrinth");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("res/dragonImage.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        JMenu menuGameScale = new JMenu("Zoom");
        createScaleMenuItems(menuGameScale, 1.0, 2.0, 0.5);
        
        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Highscores table") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(game.getHighScores(), MainWindow.this);
            }
        });
        
        JMenuItem menuRestart = new JMenuItem(new AbstractAction("Restart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });

        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuGame.add(menuGameScale);
        menuGame.add(menuHighScores);
        menuGame.add(menuRestart);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);
        
        setLayout(new BorderLayout(0, 10));
        gameStatLabel = new JLabel("label");

        add(gameStatLabel, BorderLayout.NORTH);
        try { add(board = new Board(game), BorderLayout.CENTER); } catch (IOException ex) {}
        
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke); 
                if (!game.isLevelLoaded()) return;
                int kk = ke.getKeyCode();
                Direction d = null;
                switch (kk){
                    case KeyEvent.VK_LEFT:  d = Direction.LEFT; break;
                    case KeyEvent.VK_RIGHT: d = Direction.RIGHT; break;
                    case KeyEvent.VK_UP:    d = Direction.UP; break;
                    case KeyEvent.VK_DOWN:  d = Direction.DOWN; break;
                }
                board.refresh();
                refreshGameStatLabel();
                board.repaint();
                if (d != null && game.step(d)){
                    if(game.hasReachedCorner()) {
                        nextLevel();
                    }
                } 
            }
        });
        
        timeLabel = new JLabel(" ");
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        setTimer();
        
        getContentPane().add(getTimeLabel(), BorderLayout.SOUTH);

        setResizable(false);
        setLocationRelativeTo(null);
        game.loadGame(1);
        board.refresh();
        pack();
        refreshGameStatLabel();
        setVisible(true);
        newFrameTimer = new Timer(1000 / 2, new NewFrameListener());
        newFrameTimer.start();
    }
    
    public void setTimer() {
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(elapsedTime() + " ms");
            }
        });
        startTime = System.currentTimeMillis();
        timer.start();
    }
    
    private Direction getRandomDirection() {
         Direction[] dirs = {Direction.RIGHT, Direction.LEFT, Direction.DOWN, Direction.UP};
         Random rand = new Random();
         return dirs[rand.nextInt(dirs.length)];
    }
    
    private void refreshGameStatLabel(){
        String s = "Steps: " + game.getNumSteps();
        s += "  Level: " + level;
        gameStatLabel.setText(s);
    }
    
    public void nextLevel() {
        level++;
        if(level > 10) {
            String name = (String)JOptionPane.showInputDialog(MainWindow.this, "You won all the levels!\nPlease, write your name: ", "Player's name", JOptionPane.QUESTION_MESSAGE);
            if(name != null) game.saveTheScore(name, level-1);
            restart();
            return;
        }
        game.loadGame(level);
        board.refresh();
        pack();
        refreshGameStatLabel();
    }
    
    public void restart() {
        level = 1;
        game.loadGame(1);
        board.refresh();
        pack();
        refreshGameStatLabel();
        setTimer();
    }
    
    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    public JLabel getTimeLabel() {
        return timeLabel;
    }
    
    private void createScaleMenuItems(JMenu menu, double from, double to, double by){
        while (from <= to){
            final double scale = from;
            JMenuItem item = new JMenuItem(new AbstractAction(from + "x") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (board.setScale(scale)) pack();
                }
            });
            menu.add(item);
            
            if (from == to) break;
            from += by;
            if (from > to) from = to;
        }
    }
    
    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {}
    }    
    
    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //System.out.println(dragonDirection);
            if(!game.stepDragon(dragonDirection)) {
               dragonDirection = getRandomDirection(); 
            }
            board.repaint();
            
            if(game.isNeighborDragon()) {
                String name = (String)JOptionPane.showInputDialog(MainWindow.this, "Game Over!\nPlease, write your name: ", "Player's name", JOptionPane.QUESTION_MESSAGE);
                if(name != null) game.saveTheScore(name, level);
                restart();
            }
        }

    }
}
