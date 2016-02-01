/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slickexample;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author speace0151
 */
public class Orb {
    private int x,y,width,height;
    private int dmg,hitboxX,hitboxY;
    private boolean isVisible;
    Image orbpic;
    Shape hitbox;
public Orb(int a, int b) throws SlickException{
    this.x =a;
    this.y =b;
    isVisible = false;
    this.orbpic = new Image ("res/orbs/Ninja_12.png");
    
    this.hitbox = new Rectangle (a,b, 32, 32 ); 
 } 
//getters and setters are a common concept in java. A design guideline in java and object oreinted programming 
//Getters are methods used to query the value of instance variables.
//Setters- methods that set values for instance variables  
}
