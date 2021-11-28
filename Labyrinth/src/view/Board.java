package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;
import res.ResourceLoader;

public class Board extends JPanel {
    private Game game;
    private final Image dragon, destination, player, wall, empty;
    private double scale;
    private int scaled_size;
    private final int tile_size = 32;
    
    public Board(Game g) throws IOException{
        game = g;
        scale = 1.0;
        scaled_size = (int)(scale * tile_size);
        dragon = ResourceLoader.loadImage("res/dragonImage.png");
        destination = ResourceLoader.loadImage("res/destination.png");
        player = ResourceLoader.loadImage("res/playerImage.png");
        wall = ResourceLoader.loadImage("res/grassImage.jpg");
        empty = ResourceLoader.loadImage("res/empty.png");
    }
    
    public boolean setScale(double scale){
        this.scale = scale;
        scaled_size = (int)(scale * tile_size);
        return refresh();
    }
    
    public boolean refresh(){
        if (!game.isLevelLoaded()) return false;
        Dimension dim = new Dimension(game.getLevelCols() * scaled_size, game.getLevelRows() * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }
    
    private boolean isIntersectY(Position p, int x, int y) {
        for(int i=0;i<=2;++i) {
            if(x == p.x && y-i == p.y) return true;
            if(x == p.x && y+i == p.y) return true;
        }
        return false;
    }
    
    private boolean isIntersectX(Position p, int x, int y) {
        for(int i=0;i<=2;++i) {
            if(x-i == p.x && y == p.y) return true;
            if(x+i == p.x && y == p.y) return true;
        }
        return false;
    }
    
    private boolean isVisible(Position p, int x, int y) {
        boolean isHided = false;
        isHided = isHided || (p.x == x && p.y == y);
        for(int i=0;i<=2;++i) {
            isHided = isHided || isIntersectX(p, x, y-i);
            isHided = isHided || isIntersectY(p, x+i, y);
            isHided = isHided || isIntersectX(p, x, y+i);
            isHided = isHided || isIntersectY(p, x-i, y);
        }
        return isHided;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (!game.isLevelLoaded()) return;
        Graphics2D gr = (Graphics2D)g;
        int w = game.getLevelCols();
        int h = game.getLevelRows();
        Position p = game.getPlayerPos();
        Position d = game.getDragonPos();
        for (int y = 0; y < h; y++){
            for (int x = 0; x < w; x++){
                Image img = null;
                LevelItem li = game.getItem(y, x);
                switch (li) {
                    case DESTINATION: img = destination; break;
                    case WALL: img = wall; break;
                    case EMPTY: img = empty; break;
                }
                if (p.x == x && p.y == y) img = player;
                if (d.x == x && d.y == y) img = dragon;
                if (img == null) continue;
                if(isVisible(p, x, y)) gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null); 
                else gr.fill3DRect(x * scaled_size, y * scaled_size, scaled_size, scaled_size, true);
            }
        }
    }
    
}
