/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

/**
 *
 * @author bli
 */
public class HighScore {
    
    private final String name;
    private final int level;

    public HighScore(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "HighScore{" + "name=" + name + ", level=" + level + '}';
    }
    

}
