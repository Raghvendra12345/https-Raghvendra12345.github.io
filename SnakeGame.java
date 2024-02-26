import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardwidth;
    int boardheight;
    int tileSize=25;
    Tile SnakeHead;
   ArrayList<Tile>Snakebody;

    Tile food;
    Random random;

    //game logic
    Timer gameloop;
    int velocityX;
    int velocityY;
    boolean Gameover=false;
    

    SnakeGame(int boardwidth,int boardheight){
        this.boardwidth=boardwidth;
        this.boardheight=boardheight;
        setPreferredSize(new Dimension(this.boardwidth,this.boardheight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        SnakeHead=new Tile(5,5);
        food=new Tile(10,10);
        random = new Random();
        placefood();
        velocityX=0;
        velocityY=0;
        gameloop=new Timer(100, this);
        gameloop.start();

        Snakebody=new ArrayList<Tile>();


    }
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        //Grid Lines
      
        // for(int i = 0; i < boardwidth/tileSize; i++) {
        //     //(x1, y1, x2, y2)
        //     g.drawLine(i*tileSize, 0, i*tileSize, boardheight);
        //     g.drawLine(0, i*tileSize, boardwidth, i*tileSize); 
        // }
        //food
        g.setColor(Color.red);
        // g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);
        g.fill3DRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize,true);

        //snakehead
        g.setColor(Color.green);
        // g.fillRect(SnakeHead.x*tileSize,SnakeHead.y*tileSize,tileSize,tileSize);
        g.fill3DRect(SnakeHead.x*tileSize,SnakeHead.y*tileSize,tileSize,tileSize,true);
        
        //snakebody
        for(int i=0;i<Snakebody.size();i++){
            Tile snakepart=Snakebody.get(i);
            // g.fillRect(snakepart.x*tileSize,snakepart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakepart.x*tileSize,snakepart.y*tileSize, tileSize, tileSize,true);
        }

        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(Gameover){
            g.setColor(Color.red);
            g.drawString("GameOver "+String.valueOf(Snakebody.size()),tileSize-16,tileSize);
        }
        else{
            g.drawString("Score "+String.valueOf(Snakebody.size()),tileSize-16,tileSize);
        }
    }
    public void placefood(){
        food.x = random.nextInt(boardwidth/tileSize);
		food.y = random.nextInt(boardheight/tileSize);
	}
    public boolean collision(Tile tile1,Tile tile2){
           return tile1.x==tile2.x && tile1.y==tile2.y;
    }
    public void move(){
        if(collision(SnakeHead,food)){
            Snakebody.add(new Tile(food.x,food.y));
            placefood();
        }
        for(int i=Snakebody.size()-1;i>=0;i--){
            Tile snakepart=Snakebody.get(i);
            if(i==0){
                snakepart.x=SnakeHead.x;
                snakepart.y=SnakeHead.y;
            }
            else{
                Tile prevsnakepart=Snakebody.get(i-1);
                snakepart.x=prevsnakepart.x;
                snakepart.y=prevsnakepart.y;

            }
        }
        SnakeHead.x +=velocityX;
        SnakeHead.y +=velocityY;

        ///game overcondition
        for(int i=0;i<Snakebody.size();i++){
            Tile snakepart=Snakebody.get(i);

            if(collision(SnakeHead, snakepart)){
                Gameover=true;
            }
        }
        if(SnakeHead.x*tileSize<0 || SnakeHead.x*tileSize>boardwidth || SnakeHead.y*tileSize<0 || SnakeHead.y*tileSize>boardheight){
            Gameover=true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(Gameover){
            gameloop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1){
            velocityX=0;
            velocityY=-1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) { }
    
    @Override
    public void keyReleased(KeyEvent e) {}
}