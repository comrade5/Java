/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bli
 */
public class Databases {
    private HighScores highScores;

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        String[] names = new String[] {"Peter", "Adrienne", "Ethan", "Jane", "Paul", "Geoffrey", "Joe", "Laura"};                
//        try {
//            Random random = new Random();
//            HighScores highScores = new HighScores();
//            
//            for(int i=0;i<10;++i) {
//                highScores.putHighScore(names[random.nextInt(names.length)], random.nextInt(9)+1);
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Databases.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public Databases() {
        try { highScores = new HighScores(); }
        catch (SQLException ex) {
            Logger.getLogger(Databases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<HighScore> getHighScores() {
        try {
            List<HighScore> l = highScores.getHighScores();
            return new ArrayList<HighScore>(l.subList(0, l.size() > 10 ? 10 : l.size())); 
        }
        catch (SQLException ex) {
            return null;
        }
    }
    
    public void storeHighScore(String name, int level) {
        try { highScores.putHighScore(name, level); }
        catch (SQLException ex) {
            Logger.getLogger(Databases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
