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
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

// make sure you rename this class if you are doing a copy/paste
public class Lode extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 1255;
    static final int HEIGHT = 1024;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    
    int gravity = 1;
    // Player's spawn basically
    Rectangle player = new Rectangle(50, 300, 40, 50);
    ArrayList<Rectangle> blocks = new ArrayList<Rectangle>();
    ArrayList<Rectangle> ladder = new ArrayList<Rectangle>();
    ArrayList<Rectangle> dust = new ArrayList<Rectangle>();
    // a variable only made to check if user climbs block
    
    int dy = 0;
    int ay = -4;
    boolean right = false;
    boolean left = false;
    boolean jump = false;
    boolean jumping = false;
    boolean climbing = false;
    boolean down = false;
    // Images I imported for textures like wall blocks.. etc
    BufferedImage blockImg = ImageHelper.loadImage("block.png");
    BufferedImage blockImg2 = ImageHelper.loadImage("block2.png");
    BufferedImage blockImgWall = ImageHelper.loadImage("Wall.png");
    BufferedImage ladderImg = ImageHelper.loadImage("Ladderz.png");
    
    // animation aspect of the game
    BufferedImage[] LodeRunR = new BufferedImage[2];
    BufferedImage[] LodeRunL = new BufferedImage[2];
    BufferedImage[] facing = new BufferedImage[2];
    boolean up = false;
    int fFrame = 0;
    int frameCount = 0;
    long startTime = 0;
    
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)

    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!

        g.clearRect(0, 0, WIDTH, HEIGHT);
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
            for (int i = 0; i < 12; i++) {
                g.drawImage(ladderImg, blocks.x, blocks.y, this);
            }
        }

        // GAME DRAWING GOES HERE 
        if(!right && !left){
            g.drawImage(facing[fFrame], player.x, player.y,50,50, this);
        }else if (right){
            g.drawImage(LodeRunR[frameCount], player.x, player.y, 50, 50, this);
        }else if (left){
            g.drawImage(LodeRunL[frameCount], player.x, player.y, 50, 50, this);
        }

        for (Rectangle block : blocks) {
            //g.fill3DRect(block.x, block.y, block.width, block.height, jump);
            g.drawImage(blockImg, block.x, block.y, null);

            g.drawImage(blockImg2, 100, 200, this);


        }




        // GAME DRAWING ENDS HERE
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;
        
        facing[0] = ImageHelper.loadImage("LoderunnerW.png");
        facing[1] = ImageHelper.loadImage("LoderunnerE.png");
        LodeRunR[0] = ImageHelper.loadImage("Loderunner1.png");
        LodeRunR[1] = ImageHelper.loadImage("Loderunner2.png");
        LodeRunL[0] = ImageHelper.loadImage("Loderunner4.png");
        LodeRunL[1] = ImageHelper.loadImage("Loderunner5.png");
       
        
        for (int i = 0; i < 12; i++) {
            blocks.add(new Rectangle(0 + 50 * i, 350, 50, 50));

            blocks.add(new Rectangle(0 + 50 * i, 550, 50, 50));
            blocks.add(new Rectangle(350, 300, 50, 50));
            blocks.add(new Rectangle(600,600, 0+50*i, 69 ))

            //separate blocks
            blocks.add(new Rectangle(600, 450, 50, 50));
            blocks.add(new Rectangle(650, 450, 50, 50));
            blocks.add(new Rectangle(700, 450, 50, 50));
            blocks.add(new Rectangle(750, 450, 50, 50));
            blocks.add(new Rectangle(250,500,50,50));
            blocks.add(new Rectangle(250,450,50,50));
            blocks.add(new Rectangle(250,400,50,50));
        }
        for (int i = 0; i < 15; i++) {
            blocks.add(new Rectangle(0 + 50 * i, 0, 50, 50));
        }
        // For ladders

        for (int i = 0; i < 15; i++) {
            ladder.add(new Rectangle(1200, 0 + 50* i, 50,50 ));
            ladder.add(new Rectangle(400, 450, 50,50));
            ladder.add(new Rectangle(400, 500, 50,50));
            ladder.add(new Rectangle(400,400,50,50));
            ladder.add(new Rectangle(200, 200, 50,50));
        }
        //Dust to collect
        for(int i = 0; i < 5; i++){
            
        }

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 

            // moves right
            if (right) {
                player.x = player.x + 5;
            }
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
            if(player.y > HEIGHT){
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
            if(right && startTime == 0){
                startTime = System.currentTimeMillis();
                frameCount = 0;
                
                
            }
           
            if(!right && !left){
                startTime = 0;
                
            }
            if(right){
                    fFrame = 1;
                }else if (left){
                    fFrame = 0;
                }
            if(System.currentTimeMillis() - startTime < 5){
                startTime = System.currentTimeMillis();
                frameCount++;
                
                if(frameCount > 1){
                    frameCount = 0;
                    
                }
               
                
            }
            
            
           

            // Gravity component (pulls ya down)
            if(!climbing){
            dy = dy + gravity;
            }
            // Sets the location once gravity took part
            player.y = player.y + dy;
 // Climbs ladder
            climbing = false;
            for (Rectangle ladders : ladder) {
                if (player.intersects(ladders)) {
                     dy = 0;
                    climbing = false;
                   if(up){
                       player.y = player.y - 1;
                     climbing = true;
                } if (down){
                    player.y = player.y + 1;
                    
                }
                   
            }
            }
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
                                
                            } else {
                                player.x = player.x + overlap.width;
                                jump = false;
                            }
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
                Thread.sleep(10);
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
        if(code == KeyEvent.VK_UP){
             up = true;
        }
        if(code == KeyEvent.VK_DOWN){
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
         if(code == KeyEvent.VK_UP){
             up = false;
        }
         if(code == KeyEvent.VK_DOWN){
             down = false;
        }
    }
}
