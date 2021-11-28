package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import persistence.Databases;
import persistence.HighScore;
import res.ResourceLoader;

public class Game {
    private final ArrayList<GameLevel> gameLevels;
    private GameLevel gameLevel = null;
    private final Databases database;

    public Game() {
        gameLevels = new ArrayList<GameLevel>();
        database = new Databases();
        readLevels();
    }

    // ------------------------------------------------------------------------
    // The 'interesting' part :)
    // ------------------------------------------------------------------------
    
    public ArrayList<HighScore> getHighScores() {
        return database.getHighScores();
    }
    
    public void saveTheScore(String name, int val) {
        database.storeHighScore(name, val);
    }
    
    public void loadGame(int level){
        gameLevel = new GameLevel(gameLevels.get(level-1));
    }
    
    public void printGameLevel(){ gameLevel.printLevel(); }
    
    public boolean step(Direction d){
        return (gameLevel.movePlayer(d));
    }
    
    public boolean stepDragon(Direction d){
        return (gameLevel.moveDragon(d));
    }
    
    public boolean isNeighborDragon() {
        Direction[] dirs = {Direction.RIGHT, Direction.LEFT, Direction.DOWN, Direction.UP};
        Position playerPos = getPlayerPos();
        Position dragonPos = getDragonPos();
        for(int i=0;i<dirs.length;++i) {
            if(playerPos.translate(dirs[i]).isEqual(dragonPos)) {
                //Position newPlayerPos = playerPos.translate(dirs[i]);
                //System.out.println("Player: " + newPlayerPos.x + " and " + newPlayerPos.y + "Dragon: " + dragonPos.x + " and " + dragonPos.x);
                return true;
            }
                
        }
        return false;
    }
    
    // ------------------------------------------------------------------------
    // Getter methods
    // ------------------------------------------------------------------------
    
    public boolean isLevelLoaded(){ return gameLevel != null; }
    public int getLevelRows(){ return gameLevel.rows; }
    public int getLevelCols(){ return gameLevel.cols; }
    public LevelItem getItem(int row, int col){ return gameLevel.level[row][col]; }
    public int getNumSteps(){ return (gameLevel != null) ? gameLevel.getNumSteps(): 0; }
    public boolean hasReachedCorner() { 
        if(getPlayerPos().x == getLevelCols()-1 && getPlayerPos().y == 0) {
            System.out.println("Reached the corner!");
            System.out.println("Player: " + getPlayerPos().x + " " + getPlayerPos().y);
            System.out.println("Corner: " + getLevelRows() + " " + getLevelCols());
            System.out.println("Reached the corner!");
            System.out.println("----------------------------");
            System.out.println();
            return true;
        } else {
            System.out.println("NOT REACHED the corner!");
            System.out.println("Player: " + getPlayerPos().x + " " + getPlayerPos().y);
            System.out.println("Corner: " + getLevelRows() + " " + getLevelCols());
            System.out.println("NOT REACHED the corner!");
            System.out.println("----------------------------");
        }
        return false;
    }

    public Position getPlayerPos(){ // MAKE IT ~IMMUTABLE
        return new Position(gameLevel.player.x, gameLevel.player.y); 
    }
    
    public Position getDragonPos() {
        return new Position(gameLevel.dragon.x, gameLevel.dragon.y);
    }
    
    // ------------------------------------------------------------------------
    // Utility methods to load game levels from res/levels.txt resource file.
    // ------------------------------------------------------------------------

    private void readLevels(){
        //ClassLoader cl = getClass().getClassLoader();
        InputStream is;// = cl.getResourceAsStream("res/levels.txt");
        is = ResourceLoader.loadResource("res/levels.txt");
        
        try (Scanner sc = new Scanner(is)){
            String line = readNextLine(sc);
            ArrayList<String> gameLevelRows = new ArrayList<>();
            
            while (!line.isEmpty()){
                // System.out.println(id.difficulty + " " + id.id);

                gameLevelRows.clear();
                line = readNextLine(sc);
                while (!line.isEmpty() && line.trim().charAt(0) != ';'){
                    gameLevelRows.add(line);                    
                    line = readNextLine(sc);
                }
                addNewGameLevel(new GameLevel(gameLevelRows));
            }
            //if (is != null) is.close();
        } catch (Exception e){
            System.out.println("Ajaj");
        }
        
    }
    
    private void addNewGameLevel(GameLevel gameLevel){
        gameLevels.add(gameLevel);
    }
  
    private String readNextLine(Scanner sc){
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()){
            line = sc.nextLine();
        }
        return line;
    }  
}
