import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.Math.*;

class SnakeAI extends Snake {
    boolean human = false;
    int waitTime = 50;
    int upMoveAt = 10;
    //public void keyPressed(KeyEvent event) {}// Overrides it to do nothing
    public void chooseDirection (){  // Decides which way to go; AI part
        
        if(xCoords.get(0) == upMoveAt) {
            direction = 0;
            upMoveAt+= SIZE; // one before move up
        }
        else {direction = 3;}
        //System.out.println(upMoveAt);
        if(upMoveAt >= X_HIGHT) {
            upMoveAt -= X_HIGHT;
        }

    }
    private int figureXway(int closerPieceX, int closerPieceY) {
        if(xCoords.get(0) == closerPieceX && yCoords.get(0) == closerPieceY) { // If it's on it, just do the same thing
            return direction;
        }
        else if(xCoords.get(0) == closerPieceX && yCoords.get(0) != closerPieceY) { // If x is on, do y
            return figureYway(closerPieceX, closerPieceY);
        }
        else {
            return xCoords.get(0) > closerPieceX ? 3:1; // Go left if too far right, right if too far left
        }

    } 
    private int figureYway(int closerPieceX, int closerPieceY) {
        if(xCoords.get(0) == closerPieceX && yCoords.get(0) == closerPieceY) { // If it's on it, just do the same thing
            return direction;
        }
        else if(xCoords.get(0) != closerPieceX && yCoords.get(0) == closerPieceY) { // If y is on, do x
            return figureXway(closerPieceX, closerPieceY);
        }
        else {
            return yCoords.get(0) < closerPieceY ? 2:0; // Go up if too far down, down if too far up
        }

    }

   
    private double distanceForm(int x1, int y1, int x2, int y2) { // Distance formula
        double ret = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
        return ret;
    }
}
public class Snake extends JPanel implements KeyListener, ActionListener{
    int SIZE = 10; //them size
    boolean human = true;
    static final int X_HIGHT = 500;  // Constants for window sizing
    static final int Y_HIGHT = 500;
    int waitTime = 150;
    java.util.List<Integer> foodX = new ArrayList();
    java.util.List<Integer> foodY = new ArrayList();
    java.util.List<Integer> xCoords = new ArrayList();
    java.util.List<Integer> yCoords = new ArrayList();
    boolean walls = false;
    boolean paused = false;
    JLabel score = new JLabel();
    Thread main = Thread.currentThread();
    /*
        0 --> UP   --> NORTH
        1 --> RIGHT--> EAST
        2 --> DOWN --> SOUTH
        3 --> LEFT --> WEST
    */
    int direction = 3; // Starts facing left/WEST
    int amountOfFood = 2;
    int length = 1;
    int nums = 0;
    int distance = 0;
    int time = distance*waitTime;
    boolean dead = false;

    static SnakeAI game = new SnakeAI();   // Creates the game
    public void go() throws Exception{
        
        //SIZE = Integer.parseInt(arg);
        
        xCoords.add(240);
        yCoords.add(240);

        xCoords.add(-1000);
        yCoords.add(-1000);
        for(int i = 0; i < amountOfFood; i++){
            //foodX.add(game.spawnFood()[0]);
            //foodY.add(game.spawnFood()[1]); 
            foodX.add(spawnFood()[0]);
            foodY.add(spawnFood()[1]);
        }
        
        game.setBorder(BorderFactory.createLineBorder(Color.black)); // Creates black border around the field
        JFrame frame = new JFrame("Snake Game");   // Creates the frame for the game
        frame.setVisible(true);
        frame.setSize(X_HIGHT+80, Y_HIGHT+80);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(game); // Looks for key presses
        //frame.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new GridBagLayout());
        score.setText("Length: " + (length) + "          Eaten: " + (nums) + "          Time: " + (time));
        JLabel deadMessage = new JLabel("You are still alive!");
        JButton playAgain = new JButton("Play Again!");
        GridBagConstraints c = new GridBagConstraints();
        
        //c.gridx = 1;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.weightx = 0.333;
        //c.weighty = 0.0;
        //panel.add(deadMessage, c);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        panel.add(score, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        panel.add(playAgain, c);
        playAgain.addActionListener(new NewGame());

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        game.setPreferredSize(new Dimension(X_HIGHT,Y_HIGHT));
        game.setMinimumSize(new Dimension(X_HIGHT,Y_HIGHT));
        panel.add(game, c);

        try{
        gameRun();
        }catch(Exception e){}
        
        /*
        deadMessage.setText("You are dead!");
        game.updateUI();
        JFrame stats = new JFrame("Stats");
        stats.setVisible(true);
        stats.setSize(350,400);
        stats.setResizable(true);
        JPanel statPanel = new JPanel();
        stats.add(statPanel);
        statPanel.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        */
        //String[] statNames = {"Score: ", "Number Eaten: ", "Distance: ", "Time alive: ", "Avg. Time per pellet: "};
        //            score,#eaten,distance, time alive,          avg time per pellet
        //float[] statis = {length, nums, distance, (float)(distance*(float)(waitTime/1000)), (float)(distance*(float)((float)(waitTime/1000)/nums))};
        
        /*System.out.println("Score: " + length);
        System.out.println("#Eaten: " + nums);
        System.out.println("Distance: " + distance);
        System.out.println("Time alive: " + distance*waitTime/1000.0);
        System.out.println("Avg. : " + distance*waitTime/nums/1000.0);

        con.weightx = 0.5;
        con.weighty = 0.5;
        con.gridx = 0;
        con.gridy = 0;
        statPanel.add(new JLabel("Score: " + length), con);
        con.gridy = 1;
        statPanel.add(new JLabel("Number Eaten: " + nums), con);
        con.gridy = 2;
        statPanel.add(new JLabel("Distance: " + distance), con);
        con.gridy = 3;
        statPanel.add(new JLabel("Time alive: " + distance*waitTime/1000.0), con);
        con.gridy = 4;
        statPanel.add(new JLabel("Avg. Time per block: " + distance*waitTime/1000.0/nums), con);*/

    }
    public void gameRun() throws Exception{
        //System.out.println("Playing again");
        //System.out.println(dead);
        boolean eaten = false;
        boolean repaint = true;
        while(!dead) { // run game in here while not dead
            //System.out.println("in here!");
            if(!paused) {
                //System.out.println("running");
                Thread.sleep(waitTime);
                distance++;
                time = waitTime*distance/1000;
                score.setText("Length: " + (length) + "          Eaten: " + (nums) + "          Distance: " + (distance));
                eaten = game.checkEat();

                if(!human) {
                    game.chooseDirection();
                }
                game.moveSnake(eaten, score); 
                 
                //System.out.println("repainted");

            }
            game.repaint();
        }
        System.out.println("dead");    
    }
   
    public class NewGame implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            length = 1;
            distance = 0;
            time = 0;
            nums = 0;
            dead = false;
            paused = false;
            xCoords.clear();
            yCoords.clear();
           
            xCoords.add(240);
            yCoords.add(240);

            xCoords.add(-1000);
            yCoords.add(-1000);
            for(int i = 0; i < amountOfFood; i++){
                //foodX.add(game.spawnFood()[0]);
                //foodY.add(game.spawnFood()[1]); 
                foodX.add(spawnFood()[0]);
                foodY.add(spawnFood()[1]);
            }

            System.out.println("Yay! cleared");
            game.repaint();
            try{
            gameRun();} catch(Exception except){except.printStackTrace();}
        }
    }    
    

    public static void main(String[] args) {
        try {
            game.go();
        } catch (Exception e) {e.printStackTrace();}

    }

    public boolean moveSnake(boolean eaten, JLabel score) throws Exception{ // Moves snake
        int prevX = -1;
        int prevY = -1;
        if(eaten){
            length+=10;
            //nums++;
            waitTime /= 1.01;
            
        }
        
        for(int i = 0; i < length; i++) {
            if(prevX != -1 && prevY != -1) { // Tries to move to where the previous body part was
                int tempPrevx = -1;
                int tempPrevy = -1;
                try {
                    tempPrevx = xCoords.get(i);
                    tempPrevy = yCoords.get(i);
                    xCoords.remove(i);
                    yCoords.remove(i);
                }catch(IndexOutOfBoundsException e) {/*System.out.println("added one!");*/}
                xCoords.add(i, prevX);
                yCoords.add(i, prevY);
                
                prevX = tempPrevx;
                prevY = tempPrevy;
                
            }
            else { // Head of the snake
                try{
                prevX = xCoords.get(i);
                prevY = yCoords.get(i);
                
                switch(direction) {  // Moves head depending on direction facing (obtained by keyTyped)
                    case 0:
                        yCoords.set(i, yCoords.get(i)-SIZE);  // Moves up; doesn't change x coord
                        break;
                    case 1:
                        xCoords.set(i, xCoords.get(i)+SIZE); // Moves right; doesn't change y coord
                        break;
                    case 2:
                        yCoords.set(i, yCoords.get(i)+SIZE); // Moves down
                        break;
                    case 3:
                        xCoords.set(i, xCoords.get(i)-SIZE); // Moves left
                        break;
                    default:
                        System.out.println("Error happened!");
                }// End switch 
                //Now check if the snake is hitting something
                // ex. Out of bounds, itself;
                int x = xCoords.get(i);
                int y = yCoords.get(i);
                if((x < 0|| x+SIZE > X_HIGHT || y+SIZE > Y_HIGHT || y < 0) && walls) { // Out of bounds is dead
                    yCoords.set(i, prevY);
                    xCoords.set(i, prevX);
                    dead = true;
                    return false;
                }
                else if((x+SIZE > X_HIGHT) && !walls) {  // Unless playing with pass through walls
                    xCoords.set(i, 0);
                    //System.out.println(xCoords.get(i) + " " + yCoords.get(i));
                }
                else if(x < 0 && !walls){
                    xCoords.set(i, X_HIGHT-SIZE);
                    //System.out.println(xCoords.get(i) + " " + yCoords.get(i));
                }
                else if((y+SIZE > Y_HIGHT) && !walls) {
                    yCoords.set(i, 0);
                    //System.out.println(xCoords.get(i) + " " + yCoords.get(i));
                }
                else if(y < 0){
                    yCoords.set(i, Y_HIGHT-SIZE);
                    //System.out.println(xCoords.get(i) + " " + yCoords.get(i));
                }
                //int lToUse = eaten ? length-1:length;
                if(x > X_HIGHT) {
                    x -= X_HIGHT;
                }
                else if(x < 0) {
                    x += X_HIGHT;
                }
                if(y > Y_HIGHT) {
                    y -= Y_HIGHT;
                }
                else if(y < 0) {
                    y += Y_HIGHT;
                }
                int lToUse = length;
                for(int j = 2; j < lToUse; j++) { // Skips first coord, cuz that is the point;
                    if(x == xCoords.get(j) && y == yCoords.get(j)) { // Hit yourself is dead
                        yCoords.set(i, prevY);
                        xCoords.set(i, prevX);
                        /*System.out.println("dead at " + j);
                        System.out.println(x + " " + y);
                        System.out.println(xCoords.get(j) + " " + yCoords.get(j));*/
                        dead = true;
                        return false;
                    }
                    
                    
                }// End death checking
                }catch(IndexOutOfBoundsException e) {/*System.out.println("ok");*/}
            }//End head work
            
        } // End all moving
        return true;
    }
    public int[] spawnFood(){ // Makes food
        double raw1 = Math.random();
        double raw2 = Math.random();

        int newX = (int)((int)((int)(raw1*X_HIGHT*10))/(10*SIZE))*SIZE;
        int newY = (int)((int)((int)(raw2*Y_HIGHT*10))/(10*SIZE))*SIZE;
        int whatToreturn[] = {newX, newY};
        return whatToreturn;
    }
    public boolean checkEat(){
        boolean eaten = false;
        int headX = xCoords.get(0);
        int headY = yCoords.get(0);
        for(int i = 0; i < amountOfFood; i++) {
            int blobX = foodX.get(i);
            int blobY = foodY.get(i);
            if(headX == blobX && headY == blobY) {
                eaten = true;
                break;
            }
        }
        if(eaten) {
            //System.out.println("Eaten"); 
            waitTime /= 1;   // Factor by which it speeds up  
            int index = 0;
            for(int i = 0; i < amountOfFood; i++){
                if(foodX.get(i) == headX && foodY.get(i) == headY) { // Find the food pellet we just ate
                    index = i;

                    break;
                }
            }
            int newCoords[] = spawnFood();
            foodX.set(index, newCoords[0]);
            foodY.set(index, newCoords[1]);
        }
        
        return eaten;
    }
    
    public void keyTyped(KeyEvent event){} // Overriding function that's needed
    public void keyReleased(KeyEvent event) {} // Overriding function that's needed
    @Override
    public void keyPressed(KeyEvent event) { // When the key is pressed, do this:
        int keyCode = event.getKeyCode();
        if(keyCode == KeyEvent.VK_UP && yCoords.get(1)+SIZE != yCoords.get(0)) { // Up arrow key pressed (NORTH)
            direction = 0; 
            System.out.println("Going up");
        }
        else if(keyCode == KeyEvent.VK_RIGHT && xCoords.get(1)-SIZE != xCoords.get(0)) { // Right Key pressed (EAST)
            direction = 1;
            System.out.println("Going right");
        }
        else if(keyCode == KeyEvent.VK_DOWN && yCoords.get(1)-SIZE != yCoords.get(0)) { // Down key pressed (SOUTH)
            direction = 2;
            System.out.println("Going down");
        }
        else if(keyCode == KeyEvent.VK_LEFT && xCoords.get(1)+SIZE != xCoords.get(0)) { // Left key pressed (WEST)
            direction = 3;
            System.out.println("Going left");
        }
        if(keyCode == KeyEvent.VK_SPACE) {
            paused = !paused;
        }
    } 
    @Override
    public void actionPerformed(ActionEvent e) {   // Button pressed --> restarts game

    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        
        for(int i = 0; i < foodX.size(); i++) {//Loops through food items
            int x = foodX.get(i);
            int y = foodY.get(i);
            ;

           

            g.setColor(new Color(255,0,255));
            g.fillRoundRect(x, y, SIZE, SIZE, 2, 2);
        }
        //System.out.println("--------------");
        
        if(dead) {
            g.setColor(new Color(255,0,0));
            g.fillRoundRect(xCoords.get(0), yCoords.get(0), SIZE,SIZE,0,0);
        }
        else if(paused) {
            g.setColor(new Color(255,255,0));
            g.fillRoundRect(xCoords.get(0),yCoords.get(0),SIZE,SIZE,0,0);
        }
        else {  
            g.setColor(new Color(0,0,0));
            g.fillRoundRect(xCoords.get(0),yCoords.get(0),SIZE,SIZE,0,0);//The head
        }
        int r = 0;
        int gr = 0;
        int b = 0;

        for(int i = 1; i < length; i++) { //Loops through all the body parts
            /*int n = i;
            int k = 20;
            if(n>=255) {n-=255;}
            if(n<42){
                gr += k;
                if(gr>255){gr=255;}
            }
            else if(n<42*2){
                r -= k;
                if(r<0){r=0;}
            }
            else if(n<42*3) {
                b += k;
                if(b>255){b=255;}
            }
            else if(n<42*4){
                gr -= k;
                if(gr<0){gr=0;}
            }
            else if(n<42*5) {
                r += k;
                if(r>255){r=255;}
            }
            else if(n<42*6) {
                b -=k;
                if(b<0){b=0;}
            }*/
            //System.out.println(r + "\n" + gr + "\n" +b);

            try{    
                int x = xCoords.get(i);
                int y = yCoords.get(i);
                //System.out.println(x + " "+ y);
                g.setColor(new Color(r,gr,b));
                g.fillRoundRect(x,y,SIZE,SIZE,/*SIZE/2+2*/0,/*SIZE/2+2*/0);
            }catch(IndexOutOfBoundsException e) {/*System.out.println("still kk");*/}
            
        }
    }   
}