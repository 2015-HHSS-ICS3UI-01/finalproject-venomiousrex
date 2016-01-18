package game;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hadik9595
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

// make sure you rename this class if you are doing a copy/paste
public class Lode extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 1250;
    static final int HEIGHT = 1000;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    int gravity = 1;
    int playerx = 50;
    int playery = 300;
    int score = 0;

    // Player's spawn basically
    Rectangle player = new Rectangle(50, 300, 40, 50);
    Rectangle enemy = new Rectangle(100, 300, 50, 50);
    Rectangle enemy2 = new Rectangle(100, 400, 50, 50);

    ArrayList<Rectangle> blocks = new ArrayList<Rectangle>();
    ArrayList<Rectangle> ladder = new ArrayList<Rectangle>();
    ArrayList<Rectangle> dust = new ArrayList<Rectangle>();
    ArrayList<Rectangle> enemies = new ArrayList<Rectangle>();
    // a variable only made to check if user climbs block
    int dy = 0;
    int ay = 0;
    boolean right = false;
    boolean left = false;
    boolean up = false;
    boolean jump = false;
    boolean jumping = false;
    boolean climbing = false;
    boolean down = false;
    // Images I imported for textures like wall blocks.. etc
    BufferedImage blockImg = ImageHelper.loadImage("block.png");
    BufferedImage blockImg2 = ImageHelper.loadImage("block2.png");
    BufferedImage blockImgWall = ImageHelper.loadImage("Wall.png");
    BufferedImage ladderImg = ImageHelper.loadImage("Ladderz.png");
    BufferedImage gold = ImageHelper.loadImage("Dust.png");
    BufferedImage title = ImageHelper.loadImage("title.png");
     BufferedImage gameOver = ImageHelper.loadImage("GAMEOVER.jpg");
    BufferedImage enemyR = ImageHelper.loadImage("Enemy1.png");
    // animation aspect of the game
    BufferedImage[] LodeRunR = new BufferedImage[3];
    BufferedImage[] LodeRunL = new BufferedImage[3];
    BufferedImage[] facing = new BufferedImage[2];
    BufferedImage[] Climbs = new BufferedImage[3];
    BufferedImage[] JumpR = new BufferedImage[2];
    BufferedImage[] JumpL = new BufferedImage[2];
    BufferedImage[] Enemy = new BufferedImage[3];
    
    boolean pick = true;
    int spawn;
    //Which way he faces frames
    int fFrame = 0;
    // falling frames
    int fallR = 0;
    int fallL = 0;
    // Climbing frames
    int CFrame = 0;
    //Walking frame count
    int frameCount = 0;
    long startTime = 0;
    int levels = 0;
    int x = 300;
    int camX = 0;
    boolean playerDetected = false;
    int count = 0;
    int enemyx = 50;
    boolean enemyFlip = false;
    int previous = WIDTH - 50;   
    int[] array = new int[WIDTH/50];

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!

        g.clearRect(0, 0, WIDTH, HEIGHT);
        if (levels == 0) {

            g.drawImage(title, 0, 0, this);
        }
    

        if (levels >= 1) {

            //Making wall
            for (int o = 0; o < 20; o++) {
                for (int i = 0; i < 30; i++) {
                    g.drawImage(blockImgWall, i * 50, o * 50, this);

                }

            }// Making lines

            for (int i = 0; i <= 30; i++) {
                g.drawLine(i * 50, 0, i * 50, HEIGHT);
                g.drawLine(0, i * 50, WIDTH, i * 50);
            }
            // Ladders 
            for (Rectangle blocks : ladder) {

                g.drawImage(ladderImg, blocks.x, blocks.y, this);

            }

            // GAME DRAWING GOES HERE 
            //If he's standing still
            if (!right && !left && !climbing && !(dy > 0) && !jump) {
                g.drawImage(facing[fFrame], player.x, player.y, 50, 50, this);

                // He moves right animation
            }
            if (right && !left) {
                g.drawImage(LodeRunR[frameCount], player.x, player.y, 50, 50, this);
                // he moves left animation
            }
            if (left && !right) {
                g.drawImage(LodeRunL[frameCount], player.x, player.y, 50, 50, this);
                // if he's climbing 
            }
            if (climbing && !right && !left) {
                g.drawImage(Climbs[CFrame], player.x, player.y, 50, 50, this);

            }
            if (jump && !climbing) {
                g.drawImage(JumpR[fallR], player.x, player.y, 50, 50, this);
            }
            if (dy > 0 && !jump && fFrame == 0) {
                g.drawImage(JumpL[fallL], player.x, player.y, 50, 50, this);

            }
            if (dy > 0 && !jump && fFrame == 1) {
                g.drawImage(JumpR[fallR], player.x, player.y, 50, 50, this);
            }
            if (right && left) {
                g.drawImage(facing[fFrame], player.x, player.y, 50, 50, this);
            }
            for (Rectangle block : blocks) {
                //g.fill3DRect(block.x, block.y, block.width, block.height, jump);
                g.drawImage(blockImg, block.x, block.y, null);

            }// visual aspect of dust 
            for (Rectangle block : dust) {

                g.drawImage(gold, block.x, block.y, null);

            }
            g.drawImage(Enemy[0], enemy.x, enemy.y, 50, 50, this);
            for (Rectangle block : enemies) {
                g.drawImage(enemyR, block.x, block.y, this);
            }
        }
        // To make the ladder that leads up to victory
        for (int i = 0; i < 15; i++) {
            if (count == 4) {
                ladder.add(new Rectangle(1200, 0 + 50 * i, 50, 50));
            }

        }
        // Once the player has fallen.. 
 if(levels == 2){
         g.drawImage(gameOver, 0,0, this);
     }
        // GAME DRAWING ENDS HERE
    }
// List of all levels in order 
 
    public void level1() {

        blocks.clear();;
        ladder.clear();

      
        for (int i = 0; i < 13; i++) {
            blocks.add(new Rectangle(0 + 50 * i, 350, 50, 50));

            blocks.add(new Rectangle(0 + 50 * i, 550, 50, 50));
            blocks.add(new Rectangle(350, 300, 50, 50));
            blocks.add(new Rectangle(500 + (50 * i), 600, 50, 50));

            //separate blocks
            blocks.add(new Rectangle(600, 450, 50, 50));
            blocks.add(new Rectangle(650, 450, 50, 50));
            blocks.add(new Rectangle(700, 450, 50, 50));
            blocks.add(new Rectangle(750, 450, 50, 50));
            blocks.add(new Rectangle(250, 500, 50, 50));
            blocks.add(new Rectangle(250, 450, 50, 50));
            blocks.add(new Rectangle(250, 400, 50, 50));
            blocks.add(new Rectangle(0 + 50 * i, 18 * 50, 50, 50));
            blocks.add(new Rectangle(13 * 50 + (0 + 50) * i, 900, 50, 50));
            blocks.add(new Rectangle(0, 300, 50, 50));
            blocks.add(new Rectangle((0 - 50) * i, -300, 50,50));

        }
      
        // a 15 blocks platform 
        for (int i = 0; i < 15; i++) {
            blocks.add(new Rectangle(0 + 50 * i, 0, 50, 50));
        }
        for (int i = 0; i < 3; i++) {
            blocks.add(new Rectangle(0 + 50 * i, 14 * 50, 50, 50));
            blocks.add(new Rectangle(200, 14 * 50, 50, 50));
        }
        blocks.add(new Rectangle(450, 500, 50, 50));
        // For ladders

        for (int i = 0; i < 15; i++) {

            ladder.add(new Rectangle(400, 0 + 50 * i, 50, 50));

        }
        //Dust to collect

        dust.add(new Rectangle(200, 300, 50, 50));
        dust.add(new Rectangle(300, 300, 50, 50));
        dust.add(new Rectangle(400, 300, 50, 50));
        dust.add(new Rectangle(400, 500, 50, 50));
        enemies.add(new Rectangle(50, 300, 50, 50));
    
        
        
         
       // loop created for the obstacle
        for(int i = 0; i < 60; i++){
            int randomNumber = (int) (Math.random() * 23) + 1;
            System.out.println(randomNumber);
         int random = randomNumber*50;
         
       // randomizer for ladder
       for(int y = 0; y < 60; y= y + 3){
         for(int p = 0; p < 15; p++){
             spawn = 0+random*p;
             int minus = y*50;
            blocks.add(new Rectangle(spawn, 200, 50, 50));
         
            
            //Generator for ladders
            
        }}}
        

        
             
             
    }
   
       
           
    
    public void level2() {
        blocks.clear();;
        ladder.clear();

        for (int i = 0; i < 13; i++) {
            blocks.add(new Rectangle((0 + 50) * i, 350 - (i * 50), 50, 50));
        }
        
   

    }

    public void level3() {
        blocks.clear();;
        ladder.clear();
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;
        // all the animation pictures used
        facing[0] = ImageHelper.loadImage("LoderunnerW.png");
        facing[1] = ImageHelper.loadImage("LoderunnerE.png");
        LodeRunR[0] = ImageHelper.loadImage("Loderunner1.png");
        LodeRunR[1] = ImageHelper.loadImage("Loderunner2.png");
        LodeRunR[2] = ImageHelper.loadImage("Loderunner3.png");
        LodeRunL[0] = ImageHelper.loadImage("Loderunner4.png");
        LodeRunL[1] = ImageHelper.loadImage("Loderunner5.png");
        LodeRunL[2] = ImageHelper.loadImage("Loderunner6.png");
        Climbs[0] = ImageHelper.loadImage("LodeClimb1.png");
        Climbs[1] = ImageHelper.loadImage("LodeClimb2.png");
        Climbs[2] = ImageHelper.loadImage("LodeClimb3.png");
        JumpR[0] = ImageHelper.loadImage("Lodejump1.png");
        JumpR[1] = ImageHelper.loadImage("Lodejump2.png");
        JumpL[0] = ImageHelper.loadImage("Lodejump3.png");
        JumpL[1] = ImageHelper.loadImage("Lodejump4.png");
        Enemy[0] = ImageHelper.loadImage("Enemy1.png");
        Enemy[1] = ImageHelper.loadImage("Enemy2.png");
        Enemy[2] = ImageHelper.loadImage("Enemy3.png");

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
           
         
           
            
            if (levels == 0) {
                if (up) {
                    levels = 1;
                }

                if (levels == 1) {
                    level1();
                }

            }
           
            // once player finishes a level
            if (player.y < 0) {
                levels++;
                player.x = 50;
                player.y = 300;
            }
          
            if (levels == 3) {
                level3();
            }
            

            if (levels >= 1) {
                if (levels == 2) {
                    level2();
                }
                   
            

                // moves right
                if (right) {
                    player.x = player.x + 5;

                }
                // Enemy going different directions
                if (enemyFlip && !playerDetected) {
                    enemy.x = enemy.x - 1;
                }
                if (!enemyFlip && !playerDetected) {
                    enemy.x = enemy.x + 1;
                }
                if (playerDetected) {
                    if (enemy.x < player.x) {
                        enemy.x = enemy.x + 1;
                    } else if (enemy.x > player.x) {
                        enemy.x = enemy.x - 1;
                    }
                }

                // changes levels everytime he reaches the top
                // moves left
                if (left) {
                    player.x = player.x - 5;
                }
                // Can't escape the window >:)
                if (player.x < 0) {
                    player.x = 0;
                }
                if (player.x + 50 > WIDTH) {
                    player.x = WIDTH - 50;
                }

                if (player.y > HEIGHT) {
                    player.x = 50;
                    player.y = 250;
                    
                }
                //respawning enemy
              

                // Jumps sets dy = -15 (meaning he's in air)
                if (!jumping && jump && dy == 0 && climbing == false) {

                    dy = -15;

                    //sets jumping true
                    jumping = true;

                }

                // animation
                if (right && startTime == 0) {
                    startTime = System.currentTimeMillis();
                    frameCount = 0;

                }

                if (!right && !left) {
                    startTime = 0;

                }// To determine which he faces when he's idle
                if (right) {
                    fFrame = 1;
                } else if (left) {
                    fFrame = 0;
                }
                if (System.currentTimeMillis() - startTime < 5) {
                    startTime = System.currentTimeMillis();
                    frameCount++;

                    // delays animation 
                    if (frameCount > 2) {
                        frameCount = 0;

                    }

                }
                if (up && climbing) {

                    CFrame++;

                    if (CFrame > 2) {
                        CFrame = 0;
                    }
                }
                if (down && climbing) {

                    CFrame++;

                    if (CFrame > 2) {
                        CFrame = 0;
                    }
                }
                // when he falls animation facing right
                if (dy > 0 && fFrame == 1) {
                    fallR++;
                    if (fallR > 1) {
                        fallR = 0;
                    }

                }
                // Falling facing left
                if (fFrame == 0 && dy > 0) {
                    fallL++;
                    if (fallL > 1) {
                        fallL = 0;

                    }

                }
                // Enemy interactions and configurations

                // Climbs ladder
                climbing = false;
                for (Rectangle ladders : ladder) {
                    if (player.intersects(ladders)) {
                        dy = 0;
                        climbing = true;
                        if (up) {
                            player.y = player.y - 2;
                            climbing = true;

                        }
                        if (down) {
                            player.y = player.y + 2;
                            climbing = true;

                        }

                    }
                    
                }

                // Gravity component (pulls ya down)
                if (!climbing) {
                    dy = dy + gravity;

                }
                ay = ay + gravity;

                // Sets the location once gravity took part
                player.y = player.y + dy;
                enemy.y = enemy.y + ay;

                // is there a block under me
                for (Rectangle block : blocks) {
                    // hitting a block

                    if (player.intersects(block)) {
                        Rectangle overlap = player.intersection(block);
                        // Once lands on a block
                        if (!left && !right) {
                            if (player.y < block.y) {
                                player.y = player.y - overlap.height;

                                jumping = false;
                                dy = 0;
                                // If is not on a block 
                            } else {
                                player.y = player.y + overlap.height;

                                dy = 0;
                            }
                            //  if he collides with a block as he falls
                        } else {
                            if (overlap.width < overlap.height) {
                                if (player.x < block.x) {

                                    player.x = player.x - overlap.width;
                                    // to avoid moving while in air going right

                                } else {
                                    player.x = player.x + overlap.width;

                                    // to avoid moving while in air going left
                                }
                                // The opposite, if he's under the block
                            } else {
                                if (player.y < block.y) {
                                    player.y = player.y - overlap.height;

                                    jumping = false;
                                    dy = 0;
                                } else {
                                    player.y = player.y + overlap.height;

                                    dy = 0;
                                }
                            }
                        }
                        if(block.y > HEIGHT){
                            count++;
                           
                            score++;
                            
                        }
                        block.y = block.y + 1;
                    }
                   block.y = block.y + 1;
               
                   
                    
                }
                // For the bot arrays 
                for (Rectangle b : blocks) {
                    for (Rectangle R : enemies) {
                        if (b.intersects(R)) {

                            Rectangle overlap = enemy.intersection(R);
                            // Once lands on a block
                            if (R.y - 1 == player.y) {
                                if (R.y < b.y) {

                                    R.y = R.y - overlap.height;

                                    ay = 0;
                                    // If is not on a block 
                                } else {

                                    R.y = R.y + overlap.height;
                                    ay = 0;
                                }
                                //  if he collides with a block as he falls
                            } else {
                                if (overlap.width < overlap.height) {
                                    if (R.x < b.x) {

                                        R.x = R.x - overlap.width;
                                        // If player isn't on same platform level enemy rotates
                                        if (player.y != enemy.y - 1) {
                                            enemyFlip = true;

                                        }

                                    } else {

                                        R.x = R.x + overlap.width;
                                        if (player.y != enemy.y - 1) {
                                            enemyFlip = false;
                                        }
                                        System.out.println("WOO");
                                    }
                                    // The opposite, if he's under the block
                                } else {
                                    if (R.y < b.y) {

                                        R.y = R.y - overlap.height;

                                        ay = 0;
                                    } else {

                                        R.y = R.y + overlap.height;
                                        ay = 0;

                                    }
                                }
                            }

                        }

                    }
                }

                // interacting with dust
                Iterator<Rectangle> it = dust.iterator();
                // While there are more than one dust 
                while (it.hasNext()) {
                    // recognizing the dust
                    Rectangle block = it.next();
                    // If player touches dust, collects it and stores it
                    if (player.intersects(block)) {
                        it.remove();
                        count++;

                    }

                }
                if (player.y == enemy.y - 1) {
                    playerDetected = true;
                } else {
                    playerDetected = false;
                }

                //Enemy's collison 
                for (Rectangle block : blocks) {
                    // hitting a block

                    if (enemy.intersects(block)) {
                        Rectangle overlap = enemy.intersection(block);
                        // Once lands on a block
                        if (enemy.y - 1 == player.y) {
                            if (enemy.y < block.y) {

                                enemy.y = enemy.y - overlap.height;

                                ay = 0;
                                // If is not on a block 
                            } else {

                                enemy.y = enemy.y + overlap.height;
                                ay = 0;
                            }
                            //  if he collides with a block as he falls
                        } else {
                            if (overlap.width < overlap.height) {
                                if (enemy.x < block.x) {

                                    enemy.x = enemy.x - overlap.width;
                                    // If player isn't on same platform level enemy rotates
                                    if (player.y != enemy.y - 1) {
                                        enemyFlip = true;

                                    }

                                } else {

                                    enemy.x = enemy.x + overlap.width;
                                    if (player.y != enemy.y - 1) {
                                        enemyFlip = false;
                                    }

                                }
                                // The opposite, if he's under the block
                            } else {
                                if (enemy.y < block.y) {

                                    enemy.y = enemy.y - overlap.height;

                                    ay = 0;
                                } else {

                                    enemy.y = enemy.y + overlap.height;
                                    ay = 0;

                                }
                            }
                        }

                    }

                }
                if(enemy.y > HEIGHT){
                    enemy.y = 50;
                }

            }

            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(15);
                } else {

                    Thread.sleep(desiredTime - deltaTime);

                }
            } catch (Exception e) {
            };
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        Lode game = new Lode();
        // sets the size of my game

        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            left = true;
        }

        if (code == KeyEvent.VK_SPACE) {
            jump = true;

        }
        if (code == KeyEvent.VK_UP) {
            up = true;
        }
        if (code == KeyEvent.VK_DOWN) {
            down = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            left = false;
        }

        if (code == KeyEvent.VK_SPACE) {
            jump = false;

        }
        if (code == KeyEvent.VK_UP) {
            up = false;

        }
        if (code == KeyEvent.VK_DOWN) {
            down = false;
        }
    }
}
