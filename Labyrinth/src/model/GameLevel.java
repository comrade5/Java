package model;

import java.util.ArrayList;

public class GameLevel {
    public final int           rows, cols;
    public final LevelItem[][] level;
    public Position            player = new Position(0, 0);
    public Position            dragon = new Position(0, 0);
    private int                numSteps;
    
    public GameLevel(ArrayList<String> gameLevelRows){
        int c = 0;
        for (String s : gameLevelRows) if (s.length() > c) c = s.length();
        rows = gameLevelRows.size();
        cols = c;
        level = new LevelItem[rows][cols];
        numSteps = 0;
        
        for (int i = 0; i < rows; i++){
            String s = gameLevelRows.get(i);
            for (int j = 0; j < s.length(); j++){
                switch (s.charAt(j)){
                    case '&': dragon = new Position(j, i);
                              level[i][j] = LevelItem.EMPTY; break;
                    case '@': player = new Position(j, i);
                              level[i][j] = LevelItem.EMPTY; break;
                    case '#': level[i][j] = LevelItem.WALL; break;
                    case '.': level[i][j] = LevelItem.DESTINATION; break;
                    default:  level[i][j] = LevelItem.EMPTY; break;
                }
            }
            for (int j = s.length(); j < cols; j++){
                level[i][j] = LevelItem.EMPTY;
            }
        }
    }

    public GameLevel(GameLevel gl) {
        rows = gl.rows;
        cols = gl.cols;
        numSteps = gl.numSteps;
        level = new LevelItem[rows][cols];
        player = new Position(gl.player.x, gl.player.y);
        dragon = new Position(gl.dragon.x, gl.dragon.y);
        for (int i = 0; i < rows; i++){
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
    }

    public boolean isValidPosition(Position p){
        return (p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows);
    }
    
    public boolean isFree(Position p){
        if (!isValidPosition(p)) return false;
        LevelItem li = level[p.y][p.x];
        return (li == LevelItem.EMPTY || li == LevelItem.DESTINATION);
    }
    
    public boolean moveDragon(Direction d) {
        Position curr = dragon;
        Position next = curr.translate(d);
        if (isFree(next)) {
            dragon = next;
            return true;
        } 
        return false;
    }
    
    public boolean movePlayer(Direction d){
        Position curr = player;
        Position next = curr.translate(d);
        if (isFree(next)) {
            player = next;
            numSteps++;
            return true;
        } 
        return false;
    }
    
    public void printLevel(){
        int x = player.x, y = player.y;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (i == y && j == x)
                    System.out.print('@');
                else 
                    System.out.print(level[i][j].representation);
            }
            System.out.println("");
        }
    }

    public int getNumSteps() {
        return numSteps;
    }

    
}
