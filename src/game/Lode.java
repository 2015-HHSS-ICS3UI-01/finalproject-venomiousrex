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
import java.awt.Font;
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
    //gravity component
    int gravity = 1;
    //score to store (HAH)
    int score = 0;
    // Creates the entity player
    Rectangle player = new Rectangle(50, 300, 40, 50);
    // creates the entity enemy
    Rectangle enemy = new Rectangle(100, 500, 50, 50);
    // An array list used to create MYRIAD of blocks/ladders/dust
    ArrayList<Rectangle> blocks = new ArrayList<Rectangle>();
    ArrayList<Rectangle> ladder = new ArrayList<Rectangle>();
    ArrayList<Rectangle> dust = new ArrayList<Rectangle>();
    ArrayList<Rectangle> rocks = new ArrayList<Rectangle>();
    //A variable that's created for when player jumps or falls (in air)
    int dy = 0;
    // Same as dy except it's used for bot
    int ay = 0;
    // right,left,up,jump,down buttons 
    boolean right = false;
    boolean left = false;
    boolean up = false;
    boolean jump = false;
    boolean down = false;
    // when player is jumping
    boolean jumping = false;
    //when player is climbing
    boolean climbing = false;
    int spawn;
    int spawn2;
    int spawn3;
    Font myFont = new Font("Arial", Font.BOLD, 150);
    //Which way he faces frames
    int fFrame = 0;
    // falling frames
    int fallR = 0;
    int fallL = 0;
    // Climbing frames
    int CFrame = 0;
    //Walking frame count
    int frameCount = 0;
    // used for changing screens (menu, game)
    int levels = 0;
    boolean increase = false;
    // A boolean help to track player
    boolean playerDetected = false;
    //Score
    int count = 0;
    int frames = 0;
    //Enemy animation frames (where he faces)
    int enemyFrames = 0;
    //Enemy flipping to different sides
    boolean enemyFlip = false;
    boolean dead = false;
    boolean start = false;

    // Images I imported for textures like wall blocks.. etc
    BufferedImage blockImg = ImageHelper.loadImage("block.png");
    BufferedImage blockImgWall = ImageHelper.loadImage("Wall.png");
    BufferedImage ladderImg = ImageHelper.loadImage("Ladderz.png");
    BufferedImage gold = ImageHelper.loadImage("Dust_edited-2.png");
    BufferedImage title = ImageHelper.loadImage("title.png");
    BufferedImage instructions = ImageHelper.loadImage("Instructions.png");
    BufferedImage hammer = ImageHelper.loadImage("Lodehammer.png");
    BufferedImage rock = ImageHelper.loadImage("rock.png");

// animation aspect of the game ARRAYS!
    BufferedImage[] LodeRunR = new BufferedImage[3];
    BufferedImage[] LodeRunL = new BufferedImage[3];
    BufferedImage[] facing = new BufferedImage[2];
    BufferedImage[] Climbs = new BufferedImage[3];
    BufferedImage[] JumpR = new BufferedImage[2];
    BufferedImage[] JumpL = new BufferedImage[2];
    BufferedImage[] EnemyAnimation = new BufferedImage[2];
    BufferedImage[] Dino = new BufferedImage[2];
    BufferedImage[] GameoverScreen = new BufferedImage[2];

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        // GAME DRAWING GOES HERE 

        g.clearRect(0, 0, WIDTH, HEIGHT);
        // Mainscreen
        if (levels == 0) {
            g.drawImage(title, 0, 0, this);
        }

        //Menuscreen 
        if (levels == 5) {
            g.drawImage(instructions, 0, 0, this);
            //proceeds to game
            if (up) {
                levels = 0;
            }
        }

        // A condition stating to draw everything on second level
        if (levels >= 1 && levels != 5 && dead == false) {
            //Making wall
            for (int o = 0; o < 20; o++) {
                for (int i = 0; i < 30; i++) {
                    g.drawImage(blockImgWall, i * 50, o * 50, this);
                }
            }

            // Making lines
            for (int i = 0; i <= 30; i++) {
                g.drawLine(i * 50, 0, i * 50, HEIGHT);
                g.drawLine(0, i * 50, WIDTH, i * 50);
            }
            //Spawns the ultra powered Dinosaur
            if (count >= 1) {
                g.drawImage(Dino[fFrame], 750, 300, null);
            }

            // Building ladders 
            for (Rectangle blocks : ladder) {
                g.drawImage(ladderImg, blocks.x, blocks.y, this);
            }

            ///ANIMATION CONTROLS///
            //If he's standing still
            if (!right && !left && !climbing && !(dy > 0) && !jump) {
                g.drawImage(facing[fFrame], player.x, player.y, 50, 50, this);
            }

            // He moves right animation
            if (right && !left) {
                g.drawImage(LodeRunR[frameCount], player.x, player.y, 50, 50, this);
            }

            // he moves left animation
            if (left && !right) {
                g.drawImage(LodeRunL[frameCount], player.x, player.y, 50, 50, this);
            }

            // if he's climbing 
            if (climbing && !right && !left) {
                g.drawImage(Climbs[CFrame], player.x, player.y, 50, 50, this);
            }

            // If he jumps
            if (jump && !climbing) {
                g.drawImage(JumpR[fallR], player.x, player.y, 50, 50, this);
            }

            // if he's falling
            if (dy > 0 && !jump && fFrame == 0) {
                g.drawImage(JumpL[fallL], player.x, player.y, 50, 50, this);
            }

            // if he's in air
            if (dy > 0 && !jump && fFrame == 1) {
                g.drawImage(JumpR[fallR], player.x, player.y, 50, 50, this);
            }

            // if right and left are pressed
            if (right && left) {
                g.drawImage(facing[fFrame], player.x, player.y, 50, 50, this);
            }
            if (right && left && jump) {
                g.drawImage(hammer, player.x, player.y, 50, 50, this);
            }

            //Drawing the blocks that are added
            for (Rectangle block : blocks) {
                g.drawImage(blockImg, block.x, block.y, null);
            }

            // visual aspect of dust (draws dust)
            for (Rectangle block : dust) {
                g.drawImage(gold, block.x, block.y, null);
            }
            //Animation for enemy movement
            g.drawImage(EnemyAnimation[enemyFrames], enemy.x, enemy.y, 50, 50, this);

            for (Rectangle block : rocks) {

                g.drawImage(rock, block.x, block.y, null);

            }

        }

        // Once the player has fallen.. GAME OVER
        if (dead) {
            blocks.clear();
            dust.clear();
            frames++;
            if (frames > 1) {
                frames = 0;
            }

            g.drawImage(GameoverScreen[frames], 0, 0, this);
            g.setFont(myFont);
            g.setColor(Color.RED);
            //informs player their score
            g.drawString("" + count, 660, 175);

            if (up) {
                levels = 0;

            }
        }

        // GAME DRAWING ENDS HERE
    }

    //Different methods 
    // Main level method (game)
    public void MainLevel() {
        //clears previous blocks when player dies
        blocks.clear();;
        ladder.clear();
        dust.clear();
        rocks.clear();
        // resets score
        count = 0;
        //no longer dead
        dead = false;

        //Sets player coords
        player.x = 100;
        player.y = 300;

        // Sets enemy coords
        enemy.x = 100;
        enemy.y = 500;

        // used to create blocks that are later drawn
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
            blocks.add(new Rectangle((0 - 50) * i, -300, 50, 50));

        }

        // a 15 blocks platform 
        for (int i = 0; i < 3; i++) {
            blocks.add(new Rectangle(0 + 50 * i, 14 * 50, 50, 50));
            blocks.add(new Rectangle(200, 14 * 50, 50, 50));
        }

        // loop created for the obstacle, random generator
        for (int i = 0; i < 600; i++) {
            //Bunch of random number generators
            int randomNumber = (int) (Math.random() * 24) + 1;
            int randomNumber2 = (int) (Math.random() * 24) + 1;
            int randomNumber3 = (int) (Math.random() * 24) + 1;

            // takes random number and multiplies by 50 since blocks are 50x50
            int random = randomNumber * 50;
            int random2 = randomNumber2 * 50;
            int random3 = randomNumber3 * 50;

            // Adds the random blocks
            for (int p = 0; p < 15; p++) {
                spawn = 0 + random * p;
                spawn2 = 0 + random2 * p;
                spawn3 = 0 + random3 * p;
                int minus = i * 100;
                if (random != random2 && random != random3 && random != random2) {
                    blocks.add(new Rectangle(spawn, 200 - minus, 50, 50));

                    blocks.add(new Rectangle(spawn - 50, 200 - minus, 50, 50));
                    blocks.add(new Rectangle(spawn2, 200 - minus, 50, 50));

                    blocks.add(new Rectangle(spawn3, 200 - minus, 50, 50));
                    blocks.add(new Rectangle(spawn3 + 50, 200 - minus, 50, 50));

                }

                //adds rocks that are no visible to screen  just yet
                rocks.add(new Rectangle(random, -50 - minus, 50, 50));

                //randomly generated dust on top of designated blocks
                if (random != random2 && random != random3 && random != random2 && random2 != random3) {
                    dust.add(new Rectangle(spawn2, (200 - minus) - 50, 50, 50));
                }
            }
        }

        // To make the ladder that leads up to victory
        for (int i = 0; i < 15; i++) {
            ladder.add(new Rectangle(400, 0 + 50 * i, 50, 50));
            ladder.add(new Rectangle(950, 0 + 50 * i, 50, 50));
        }

        //sets start o true
        start = true;
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
        EnemyAnimation[0] = ImageHelper.loadImage("Enemy3.png");
        EnemyAnimation[1] = ImageHelper.loadImage("Enemy4.png");
        Dino[0] = ImageHelper.loadImage("Dino1.png");
        Dino[1] = ImageHelper.loadImage("Dino2.png");
        GameoverScreen[0] = ImageHelper.loadImage("GAMEOVER.jpg");
        GameoverScreen[1] = ImageHelper.loadImage("GAMEOVER2.jpg");

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            // Main menu screen
            if (levels == 0) {
                //switch to game
                if (up) {
                    levels = 1;
                    start = true;
                }
                if (down) {
                    levels = 5;
                }

                // the main level for the game
                if (levels == 1 && start) {
                    MainLevel();
                }
            }

            // for game not menu
            if (levels >= 1 && levels != 5 && dead == false) {

                // moves right
                if (right) {
                    player.x = player.x + 5;

                }
                // moves left
                if (left) {
                    player.x = player.x - 5;
                }
                //Can't escape window left side
                if (player.x < 0) {
                    player.x = 0;
                }
                // Can't escape right side
                if (player.x + 50 > WIDTH) {
                    player.x = WIDTH - 50;
                }

                ////////////////////////////TEMP
                if (player.y > HEIGHT) {
                    player.x = 50;
                    player.y = 250;

                }

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
                // when he's not moving 
                if (!right && !left) {
                    startTime = 0;

                }// To determine which he faces when he's idle
                if (right) {
                    fFrame = 1;
                } else if (left) {
                    fFrame = 0;
                }
                // frames component
                if (System.currentTimeMillis() - startTime < 5) {
                    startTime = System.currentTimeMillis();
                    //moves on to next frame (like flipbook)
                    frameCount++;

                    // resets animation after all pictures are displayed
                    if (frameCount > 2) {
                        frameCount = 0;
                    }
                }

                //Animation for climbing ladder up
                if (up && climbing) {
                    //adds frames for climbing up
                    CFrame++;
                    //resets frames
                    if (CFrame > 2) {
                        CFrame = 0;
                    }
                }
                //Animation for climbing ladder down
                if (down && climbing) {
                    //add frames for climbing down
                    CFrame++;
                    //resets frames
                    if (CFrame > 2) {
                        CFrame = 0;
                    }
                }
                // when he falls animation facing right
                if (dy > 0 && fFrame == 1) {
                    fallR++;
                    //resets frames
                    if (fallR > 1) {
                        fallR = 0;
                    }

                }
                // Falling facing left
                if (fFrame == 0 && dy > 0) {
                    fallL++;
                    //resets frames
                    if (fallL > 1) {
                        fallL = 0;
                    }
                }

                // Enemy interactions and configurations
                // determines where enemy faces, in this case he faces right
                if (!enemyFlip) {
                    enemyFrames = 0;

                    // faces left 
                } else {
                    enemyFrames = 1;
                }
                //enemy can't escape window plus, when he hits edges enemy animation flips
                if (enemy.x < 0) {
                    enemy.x = 0;
                    enemyFlip = false;
                }

                // Can't escape right side
                if (enemy.x + 50 > WIDTH) {
                    enemy.x = WIDTH - 50;
                    enemyFlip = true;
                }

                // Enemy going different directions
                if (enemyFlip && !playerDetected) {
                    enemy.x = enemy.x - 1;
                }

                //indicates if player and enemy are in same Y level
                if (player.y == enemy.y - 1) {
                    playerDetected = true;
                } else {
                    playerDetected = false;
                }

                // used for walking when enemy not in sight
                if (!enemyFlip && !playerDetected) {
                    enemy.x = enemy.x + 1;
                }

                // If player on sight, (same y coord) track him down
                if (playerDetected) {
                    if (enemy.x < player.x) {
                        enemy.x = enemy.x + 1;
                        enemyFrames = 0;
                    } else if (enemy.x > player.x) {
                        enemy.x = enemy.x - 1;
                        enemyFrames = 1;
                    }
                }

                // Climbs ladder
                climbing = false;
                for (Rectangle ladders : ladder) {
                    //once on a ladder
                    if (player.intersects(ladders)) {
                        dy = 0;

                        //set climbing true
                        climbing = true;
                        //if up is pressed, player climbs up
                        if (up) {
                            player.y = player.y - 2;
                            climbing = true;

                            // player climbs down
                        } else if (down) {
                            player.y = player.y + 2;
                            climbing = true;
                        }
                    }
                }

                // Gravity component pulls player down
                if (!climbing) {
                    dy = dy + gravity;
                }
                // Gravity component pulls enemy down
                ay = ay + gravity;

                // Sets the location once gravity took part of BOTH player and enemy
                player.y = player.y + dy;
                enemy.y = enemy.y + ay;

                // Collisions of player with block
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
                            //if he interacts from the side of blocks
                            if (overlap.width < overlap.height) {
                                //sets player left or right depending on which side
                                if (player.x < block.x) {
                                    player.x = player.x - overlap.width;

                                } else {

                                    player.x = player.x + overlap.width;

                                }
                                // The opposite, if he's under the block
                            } else {
                                if (player.y < block.y) {
                                    player.y = player.y - overlap.height;

                                    jumping = false;
                                    dy = 0;
                                } else {
                                    player.y = player.y + overlap.height;
                                    // Pushes blocks up if down is pressed 
                                    if (down) {
                                        block.y = block.y - 25;
                                    }
                                    dy = 0;
                                }
                            }
                        }
                        if(count >= 40){
                            block.y = block.y + 1;
                        }
                    }
                    //Once one dust is collected, the game is activated 
                    if (count >= 1 && !dead) {
                        //blocks descend down
                        block.y = block.y + 1;
                    }
                }

                //Enemy's collison 
                for (Rectangle block : blocks) {
                    // hitting a block
                    if (enemy.intersects(block)) {
                        Rectangle overlap = enemy.intersection(block);
                        // Once lands on a block
                        if (dead) {
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
                            //if enemy hits the block from either side, prevents him from going through
                            if (overlap.width < overlap.height) {
                                if (enemy.x < block.x) {

                                    enemy.x = enemy.x - overlap.width;
                                    // If player isn't on same platform level, enemy rotates left
                                    if (player.y != enemy.y - 1 && !playerDetected) {
                                        enemyFlip = true;
                                    }

                                } else {
                                    enemy.x = enemy.x + overlap.width;
                                    //same thing, this time making enemy change direction to right
                                    if (player.y != enemy.y - 1 && !playerDetected) {
                                        enemyFlip = false;
                                    }
                                }

                                // The opposite, if he's under/above the block
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
            }

            // Creates Irerator type of rectangle for dust
            Iterator<Rectangle> it = dust.iterator();
            // While there are more than one dust 
            while (it.hasNext()) {
                // recognizing the dust
                Rectangle block = it.next();
                // If player touches dust, collects it and stores it
                if (player.intersects(block)) {
                    it.remove();
                    //adds to score
                    count++;
                }
            }
            //if enemy touches player
            if (player.intersects(enemy)) {
                //player dies
                dead = true;
            }

            //rocks
            for (Rectangle block : rocks) {
                //overlap 
                Rectangle overlap = player.intersection(block);

                //Once player collects 40 or over dust, rocks fall
                if (count >= 40) {
                    block.y = block.y + 2;

                    //if player touches rock
                    if (player.intersects(block)) {
                        // if player lands on rock
                        if (player.y < block.y) {
                            player.y = player.y - overlap.height;
                            dy = 0;
                        } else {
                            //if player is hit from above, player dies
                            dead = true;
                        }
                        // if player touching from sides of rock, prevents player from going through
                        if (overlap.width < overlap.height) {
                            if (player.x < block.x) {
                                player.x = player.x - overlap.width;
                            } else {
                                player.x = player.x + overlap.width;

                            }

                        }
                    }
                }
            }

            //Keeps respawning enemy sets coords to closest block
            if (enemy.y > HEIGHT) {
                enemy.y = 50;
                enemy.x = 50;
            }

            //Making dust go down when game activates
            for (Rectangle block : dust) {
                if (count >= 1) {
                    //makes blocks descend 
                    block.y = block.y + 1;
                }
            }

            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                // delay things in motion 
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
        JFrame frame = new JFrame("Lode Runner");

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

    //Controls aspect of the game
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //If pressed right
        if (code == KeyEvent.VK_RIGHT) {
            right = true;
        }
        //if pressed left
        if (code == KeyEvent.VK_LEFT) {
            left = true;
        }
        //if pressed space 
        if (code == KeyEvent.VK_SPACE) {
            jump = true;

        }
        //if pressed up
        if (code == KeyEvent.VK_UP) {
            up = true;
        }
        // if pressed down
        if (code == KeyEvent.VK_DOWN) {
            down = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        //if right is released
        if (code == KeyEvent.VK_RIGHT) {
            right = false;
        }
        //if left is released
        if (code == KeyEvent.VK_LEFT) {
            left = false;
        }
        //if space is released
        if (code == KeyEvent.VK_SPACE) {
            jump = false;
        }
        //if up is released
        if (code == KeyEvent.VK_UP) {
            up = false;
        }
        // if down is released
        if (code == KeyEvent.VK_DOWN) {
            down = false;
        }
    }
}
