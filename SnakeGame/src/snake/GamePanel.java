package snake;
import java.awt.*;
import java.awt.event.*;
//import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
    
    
    private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600; //Screen dimensions
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; // Size of each game cell
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static int DELAY = 100;
    final int x[] = new int [GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // U = UP; D = DOWN, L = LEFT, R = RIGHT
    boolean running = false;
    public Timer timer;
    public Random random;
	public JButton cerrar = new JButton("Reiniciar");
	public Image bcImage;
	public Image head;
	public Image body;
	public Image egg;
	
    
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
       // this.setBackground(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    public void startGame() {
        try {
			setImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawImage(bcImage, 0, 0, this);
        g.drawImage(bcImage, 0, 0, SCREEN_HEIGHT, SCREEN_WIDTH, null, this);
        draw(g);
    }
    
    public void draw(Graphics g) {
        if(running) {
        	
        	//Place a grid
        	/* for (int i = 0;  i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
        		g.drawLine(i*UNIT_SIZE,  0,  i*UNIT_SIZE,  SCREEN_HEIGHT);
        		g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);      	
        	}*/
   
        	//Draw an apple (EGG)
        	g.drawImage(egg, appleX, appleY, UNIT_SIZE+5, UNIT_SIZE+5, null, this);
    	
        	// Draw the Snake. First color is the head, and the rest is the body    	
        	for (int i = 0; i < bodyParts; i++) {
        		if (i == 0) {
        			g.drawImage(head, x[0], y[0], UNIT_SIZE, UNIT_SIZE, Color.black, this);
        			
        		} 
        		else {
        			g.setColor(Color.BLACK);
        			g.fillRect(x[i],  y[i],  UNIT_SIZE,  UNIT_SIZE);
        		}
        	}
        	//Score in the top of the Screen
        	g.setColor(Color.RED);
        	g.setFont(new Font("Comic Sans MS", Font.ITALIC, 25));
        	FontMetrics metrics = getFontMetrics(g.getFont());
        	g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        	

        } 
        else {
        	gameOver(g);
        }
    	
    }
    
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        
    	for(int i = bodyParts; i > 0; i--) {
        	if((x[i] == appleX)  || (y[i] == appleY)) {
        		 appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        	     appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        	}        	
        }
    }
    
    public void move() {
        for(int i = bodyParts; i > 0; i--) {
        	x[i] = x[i-1];
        	y[i] = y[i-1];
        }
        
        switch(direction) {
        case 'U':
        	y[0] = y[0] - UNIT_SIZE;
        	break;
        case 'D':
        	y[0] = y[0] + UNIT_SIZE;
        	break;
        case 'L':
        	x[0] = x[0] - UNIT_SIZE;
        	break;
        case 'R':
        	x[0] = x[0] + UNIT_SIZE;
        	break;
        }
    }
    
    public void checkApple() {
        if((x[0] == appleX) && y[0] == appleY) {
        	bodyParts++;
        	applesEaten++;
        	newApple();
        	//INCREASE SPEED
            if (applesEaten % 5 == 0) {
            	DELAY = DELAY -10;
            	timer.setDelay(DELAY);
            }
        }
    }
    
    public void checkCollisions() {
    	for(int i = bodyParts; i > 0; i--) {
        	if((x[0] == x[i])  && (y[0] == y[i])) {
        		running = false;
        	}        	
        }
    	/*
    	//Check if head touches borders
    	if (x[0] < 0){
    		running = false;
    	}
    	if (x[0] >= SCREEN_WIDTH){
    		running = false;
    	}
    	if (y[0] < 0){
    		running = false;
    	}
    	if (y[0] >= SCREEN_HEIGHT){
    		running = false;
    	}
    	*/
    	
    	// Try snake pass walls
    	
    	if (x[0] < 0){
    		x[0] = SCREEN_WIDTH-UNIT_SIZE;
    	}
    	if (x[0] >= SCREEN_WIDTH){
    		x[0] = 0;
    	}
    	if (y[0] < 0){
    		y[0] = SCREEN_HEIGHT-UNIT_SIZE;
    	}
    	if (y[0] >= SCREEN_HEIGHT){
    		y[0] = 0;
    	}
    	if(!running) {
    		timer.stop();
    	}
    }
    
    public void gameOver(Graphics g) {
        //Game Over Text
    	g.setColor(Color.BLACK);
    	g.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
    	FontMetrics metrics = getFontMetrics(g.getFont());
    	String text = "Game Over Coleguita";
    	String score = "Your Score is: "+applesEaten;
    	String restart = "Press Space to play again";
    	g.drawString(text,(SCREEN_WIDTH - metrics.stringWidth(text))/2, (SCREEN_HEIGHT/2)-100);
    	g.drawString(score,(SCREEN_WIDTH - metrics.stringWidth(score))/2, (SCREEN_HEIGHT/2)-75);
    	g.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
    	g.drawString(restart,(SCREEN_WIDTH - metrics.stringWidth(restart))/2, (SCREEN_HEIGHT/2)+100);
    }
    
	@Override
    public void actionPerformed(ActionEvent e) {
       
    	if(running) {
    		move();
    		checkApple();
    		checkCollisions();
    		
    	}
    	repaint();
    }
	
	//Import the images for BackGround and snake
	public void setImages() throws IOException {
    	bcImage = ImageIO.read(new File("src//FondoSnake.jpg"));
    	head = ImageIO.read(new File("src//SnakeHead.png"));
    	egg = ImageIO.read(new File("src//Huevo.png"));
    	body = ImageIO.read(new File("src//SnakeBody.png"));
      }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override 
        public void keyPressed(KeyEvent e){
            //Set directions for the snake
        	switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            	if(direction != 'R') {
            		direction = 'L';
            	}
            	break;            
            case KeyEvent.VK_RIGHT:
            	if(direction != 'L') {
            		direction = 'R';
            	}
            break;
            
            case KeyEvent.VK_UP:
            	if(direction != 'D') {
            		direction = 'U';
            	}
            break;
            case KeyEvent.VK_DOWN:
            	if(direction != 'U') {
            		direction = 'D';
            	}
            break;
            case KeyEvent.VK_SPACE:
            	DELAY = 100;
            	Window win = SwingUtilities.getWindowAncestor((Component) e.getSource());
      		  	win.dispose();    		  
      		  	new GameFrame();
            break;
            case KeyEvent.VK_CONTROL:
            	DELAY = 100;
            	timer.setDelay(DELAY);
            	break;
            }
        }
    }
    
    
    
}
