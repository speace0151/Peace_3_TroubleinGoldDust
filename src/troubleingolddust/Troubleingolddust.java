package troubleingolddust;

import org.newdawn.slick.state.*;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;

import org.newdawn.slick.AppGameContainer;

import org.newdawn.slick.BasicGame;

import org.newdawn.slick.Font;

import org.newdawn.slick.GameContainer;

import org.newdawn.slick.Graphics;

import org.newdawn.slick.Image;

import org.newdawn.slick.Input;

import org.newdawn.slick.SlickException;

import org.newdawn.slick.SpriteSheet;

import org.newdawn.slick.TrueTypeFont;

import org.newdawn.slick.geom.Rectangle;

import org.newdawn.slick.geom.Shape;

import org.newdawn.slick.state.BasicGameState;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import org.newdawn.slick.tiled.TiledMap;

public class Troubleingolddust extends BasicGameState {
    
    public Item healthpotion, healthpotion1;
    public Item1 speedpotion, speedpotion1;
    public Itemwin gold;
    public Orb orb;
    public Enemy wrangler, wrangler1;
    public Orb magic8ball;
    public ArrayList<Orb> orbz = new ArrayList();
    public ArrayList<Enemy> enemiez = new ArrayList();
    public ArrayList<Item> stuff = new ArrayList();
    public Trap traps;
    public ArrayList<Item1> stuff1 = new ArrayList();
    
public ArrayList<Item> trapz = new ArrayList();
    public ArrayList<Itemwin> stuffwin = new ArrayList();

    private boolean[][] hostiles;
    public static Player player;
    boolean isvisible = true;
    private static TiledMap grassMap;

    private static AppGameContainer app;

    private static Camera camera;

    public static int counter = 0;
    
    // player stuff
    // private Animation sprite, up, down, left, right, wait;
    /**
     *
     * The collision map indicating which tiles block movement - generated based
     *
     * on tile properties
     */
    // changed to match size of sprites & map
    private static final int SIZE = 64;

    // screen width and height won't change
    private static final int SCREEN_WIDTH = 1000;

    private static final int SCREEN_HEIGHT = 750;

    //  private Animation attack;
// public static Player player;
    public Troubleingolddust(int xSize, int ySize) {

    }

    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {

        gc.setTargetFrameRate(60);

        gc.setShowFPS(false);

        // *******************
        // Scenerey Stuff
        // ****************
        grassMap = new TiledMap("res/d4.tmx");

        // Ongoing checks are useful
        System.out.println("Tile map is this wide: " + grassMap.getWidth());

        camera = new Camera(gc, grassMap);

        // *********************************************************************************
        // player stuff --- these things should probably be chunked into methods
        // and classes
        // *********************************************************************************
        // System.out.println("Horizontal count: "
        // +runningSS.getHorizontalCount());
        // System.out.println("Vertical count: " +runningSS.getVerticalCount());
        // *****************************************************************
        // Obstacles etc.
        // build a collision map based on tile properties in the TileD map
        Blocked.blocked = new boolean[grassMap.getWidth()][grassMap.getHeight()];

        // System.out.println("Map height:" + grassMap.getHeight());
        // System.out.println("Map width:" + grassMap.getWidth());
        // There can be more than 1 layer. You'll check whatever layer has the
        // obstacles.
        // You could also use this for planning traps, etc.
        // System.out.println("Number of tile layers: "
        // +grassMap.getLayerCount());
        for (int xAxis = 0; xAxis < grassMap.getWidth(); xAxis++) {

            for (int yAxis = 0; yAxis < grassMap.getHeight(); yAxis++) {

                // int tileID = grassMap.getTileId(xAxis, yAxis, 0);
                // Why was this changed?
                // It's a Different Layer.
                // You should read the TMX file. It's xml, i.e.,human-readable
                // for a reason
                int tileID = grassMap.getTileId(xAxis, yAxis, 1);

                String value = grassMap.getTileProperty(tileID, "Blocked", "false");

                if ("true".equals(value)) {

                    Blocked.blocked[xAxis][yAxis] = true;

                }

            }

        }

        // A remarkably similar process for finding hostiles
        for (int xAxis = 0; xAxis < grassMap.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < grassMap.getHeight(); yAxis++) {
                int xBlock = (int) xAxis;
                int yBlock = (int) yAxis;
                if (!Blocked.blocked[xBlock][yBlock]) {
                    if (yBlock % 7 == 0 && xBlock % 15 == 0) {
                        Item i = new Item(xAxis * SIZE, yAxis * SIZE);
                        stuff.add(i);
                        //stuff1.add(h);

                    }
                }
            }
        }

        for (int xAxis = 0; xAxis < grassMap.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < grassMap.getHeight(); yAxis++) {
                int xBlock = (int) xAxis;
                int yBlock = (int) yAxis;
                if (!Blocked.blocked[xBlock][yBlock]) {
                    if (xBlock % 9 == 0 && yBlock % 25 == 0) {
                        Item1 h = new Item1(xAxis * SIZE, yAxis * SIZE);
                        //	stuff.add(i);
                        stuff1.add(h);

                    }
                }
            }
        }
        player = new Player();
        orb = new Orb((int) player.x, (int) player.y);

        healthpotion = new Item(100, 100);
        healthpotion1 = new Item(450, 480);
        stuff.add(healthpotion);
        stuff.add(healthpotion1);

        speedpotion = new Item1(100, 150);
        speedpotion1 = new Item1(450, 100);
        stuff1.add(speedpotion);
        stuff1.add(speedpotion1);

        gold = new Itemwin(2474, 829);
        stuffwin.add(gold);
        wrangler = new Enemy(200, 300);
        wrangler1 = new Enemy(96, 1261);
        enemiez.add(wrangler);
        enemiez.add(wrangler1);
//traps = new Trap(200, 200);
//traps.add(traps);
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {

        camera.centerOn((int) player.x, (int) player.y);

        camera.drawMap();

        camera.translateGraphics();

        // it helps to add status reports to see what's going on
        // but it gets old quickly
        // System.out.println("Current X: " +player.x + " \n Current Y: "+ y);
        player.sprite.draw((int) player.x, (int) player.y);

        //g.drawString("x: " + (int) player.x + " y: " + (int) player.y, player.x, player.y - 10);
        g.drawString("Health: " + player.health / 1000, camera.cameraX + 10,
                camera.cameraY + 10);

        g.drawString("speed: " + (int) (player.speed * 10), camera.cameraX + 10,
                camera.cameraY + 25);
        
        g.drawString("score: " + (int) (player.score * 15), camera.cameraX + 15,
                camera.cameraY + 40);

        //g.draw(player.rect);
        //g.drawString("time passed: " + counter / 1000, camera.cameraX + 600, camera.cameraY);
        // moveenemies();

        if (gold.isvisible) {
            gold.currentImage.draw(gold.x, gold.y);
        }
        for (Item i : stuff) {
            if (i.isvisible) {
                i.currentImage.draw(i.x, i.y);
                // draw the hitbox
                //g.draw(i.hitbox);

            }

        }

        for (Item1 h : stuff1) {
            if (h.isvisible) {
                h.currentImage.draw(h.x, h.y);
                // draw the hitbox
                //g.draw(h.hitbox);

            }
        }

        for (Enemy e : enemiez) {
            if (e.isvisible) {
                e.currentanime.draw(e.Bx, e.By);
                // draw the hitbox
                g.draw(e.rect);

            }
        }

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        counter += delta;
        wrangler.move();
        Input input = gc.getInput();

        float fdelta = delta * player.speed;

        player.setpdelta(fdelta);

        double rightlimit = (grassMap.getWidth() * SIZE) - (SIZE * 0.75);

        // System.out.println("Right limit: " + rightlimit);
        float projectedright = player.x + fdelta + SIZE;

        boolean cangoright = projectedright < rightlimit;

        // there are two types of fixes. A kludge and a hack. This is a kludge.
        if (input.isKeyDown(Input.KEY_UP)) {

            player.sprite = player.up;

            float fdsc = (float) (fdelta - (SIZE * .15));

            if (!(isBlocked(player.x, player.y - fdelta) || isBlocked((float) (player.x + SIZE + 1.5), player.y - fdelta))) {

                player.sprite.update(delta);

                player.y -= fdelta;

            }
            for (Itemwin w : stuffwin) {

            }
        } else if (input.isKeyDown(Input.KEY_DOWN)) {
            player.sprite = player.down;

            if (!isBlocked(player.x, player.y + SIZE + fdelta)
                    || !isBlocked(player.x + SIZE - 1, player.y + SIZE + fdelta)) {

                player.sprite.update(delta);
                {
                    player.y += fdelta;

                }

            } else if (input.isKeyDown(Input.KEY_SPACE)) {
                orb.setX((int) player.x);
                orb.setY((int) player.y);
                orb.hitbox.setX(orb.getX());
                orb.hitbox.setY(orb.getY());
                orb.setIsVisible(!orb.isIsVisible());
                orb.setIsVisible(true);
            }
        } else if (input.isKeyDown(Input.KEY_LEFT)) {
            player.setdirection = 2;
            player.sprite = player.left;

            if (!(isBlocked(player.x - fdelta, player.y) || isBlocked(player.x
                    - fdelta, player.y + SIZE - 1))) {

                player.sprite.update(delta);

                player.x -= fdelta;

            }

        } else if (input.isKeyDown(Input.KEY_RIGHT)) {

            player.sprite = player.right;

            if (cangoright
                    && (!(isBlocked(player.x + SIZE + fdelta,
                            player.y) || isBlocked(player.x + SIZE + fdelta, player.y
                            + SIZE - 1)))) {

                wrangler = new Enemy(2, 2);
                player.sprite.update(delta);

                player.x += fdelta;

            }

        }

        player.rect.setLocation(player.getplayershitboxX(),
                player.getplayershitboxY());

        for (Item i : stuff) {

            if (player.rect.intersects(i.hitbox)) {

                if (i.isvisible) {
                    player.score += 7;
                    player.health += 10000;
                    i.isvisible = false;
                }
                
            }
        }
        for (Enemy e : enemiez) {
            if (orb.hitbox.intersects(e.rect)) {
                e.isvisible = false;

            }
        }

        for (Itemwin w : stuffwin) {

            if (player.rect.intersects(w.hitbox)) {
                if (w.isvisible) {
                    w.isvisible = false;
                    makevisible();
                    sbg.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));

                }

            }
        }

        for (Item1 h : stuff1) {

            if (player.rect.intersects(h.hitbox)) {

                if (h.isvisible) {

                    player.speed += .1f;
                    h.isvisible = false;
                }

            }
        }
        for (Enemy e : enemiez) {
           if (player.rect.intersects(e.rect)) {
               player.health-=400;
               e.isvisible=false;
               player.score+= 4;
           }
            e.move();
        }
        if (player.rect.intersects(gold.hitbox)) {
            gold.isvisible = false;
            
        }
        g.drawString("speed: " + (int) (player.speed * 10), camera.cameraX + 10,
                camera.cameraY + 25);

        if (player.health <= 0) {
            makevisible();
            sbg.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
        if (orb.getTimeExists() > 0) {
            if (player.getdirection() == 0) {
                orb.setX((int) player.x);
                orb.setY((int) orb.getY() + 5);

            } else if (player.getdirection() == 2) {

            }
        }
    }

    public int getID() {

        return 1;

    }

    public void makevisible() {
        for (Item1 h : stuff1) {

            h.isvisible = true;
        }

        for (Item i : stuff) {

            i.isvisible = true;
        }
    }

    private boolean isBlocked(float tx, float ty) {

        int xBlock = (int) tx / SIZE;

        int yBlock = (int) ty / SIZE;

        return Blocked.blocked[xBlock][yBlock];

    }

}
