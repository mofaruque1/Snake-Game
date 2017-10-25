package eecs2030_SnakeProject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore.TrustedCertificateEntry;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.xml.ws.AsyncHandler;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

	private int[] snakeXlength = new int[750];
	private int[] snakeYlength = new int[750];
	private int[] enemyxpos = { 25, 75, 125, 175, 225, 275, 325, 375, 425, 475, 525, 575, 625, 675, 725, 775, 825 };
	private int[] enemyypos = { 75, 125, 175, 225, 275, 325, 375, 425, 475, 525, 575, 625 };
	private int lengthofsnake = 3;
	private int foodCounter = 9;
	private int poisonCounter = 3;
	private int score = 0;
	private int highscore = 0;
	private int level = 1;
	private Random random = new Random();
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private boolean collided = false;
	private boolean youWin = false;

	private ImageIcon rightmouth;
	private ImageIcon upmouth;
	private ImageIcon downmouth;
	private ImageIcon leftmouth;
	private ImageIcon snakeimage;
	private ImageIcon foodimage;
	private ImageIcon poisonimage;
	private ImageIcon winimage;
	private ImageIcon gameoverimage;
	private ImageIcon pressToStartImage;
	private ImageIcon nextLevelImage;
	private ImageIcon restartImage;
	private ImageIcon titleImage;

	private Timer timer;
	private int delay = 100;
	private int moves = 0;

	private FileReader in = null;
	private FileWriter out = null;
	private BufferedReader reader = null;

	public Gameplay() {
		Point2D.INSTANCES.clear();
		collided = false;
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(this.delay, this);
	}

	/*
	 * We extend the JPanel class to be able to overwrite the paint method which
	 * is the built in method called by the AWT Engine to paint what appears in
	 * the screen.
	 * 
	 */
	@Override
	public void paint(Graphics g) {

		if (youWin && delay > 30) {
			poisonCounter += 2;
			foodCounter++;
			delay -= 20;
			timer.setDelay(delay);
		}
		if (collided) {
			delay = 100;
			poisonCounter = 3;
			foodCounter = 7;
			level = 1;
			score = 0;
			timer.setDelay(delay);
		}

		if (moves == 0) {
			timer.start();
			Point2D.INSTANCES.clear();
			Point2D.POISON_INSTANCES.clear();
			snakeXlength[0] = 100;
			snakeXlength[1] = 75;
			snakeXlength[2] = 50;
			snakeYlength[0] = 100;
			snakeYlength[1] = 100;
			snakeYlength[2] = 100;
			youWin = false;
			collided = false;
			getPositionsXY(foodCounter, "food");
			getPositionsXY(poisonCounter, "poison");

		}

		// draw title image border
		g.setColor(Color.BLACK);
		g.drawRect(24, 10, 851, 55);

		// draw the title image
		titleImage = new ImageIcon("src/assets/snaketitle3.jpg");
		titleImage.paintIcon(this, g, 25, 11);

		// draw border for game play
		g.setColor(Color.black);
		g.drawRect(24, 74, 851, 577);

		// draw background for the game play
		g.setColor(Color.black);
		g.fillRect(25, 75, 850, 575);

		// draw border for instruction
		g.setColor(Color.black);
		g.drawRect(24, 665, 851, 75);

		// draw background for the instruction
		g.setColor(Color.black);
		g.fillRect(25, 666, 850, 74);

		g.setColor(Color.red);
		g.setFont(new Font("Times New Roman", Font.BOLD, 30));
		g.drawString("Instructions: ",40, 690);
		g.setColor(Color.yellow);
		g.setFont(new Font("Times New Roman", Font.BOLD, 23));
		g.drawString("Press Arrow key to start a game, eat apple and avoid poison", 210, 690);
		g.drawString("Press Space Bar to restart a game after a win or loose", 210, 720);

		rightmouth = new ImageIcon("src/assets/rightmouth.jpg");
		rightmouth.paintIcon(this, g, snakeXlength[0], snakeYlength[0]);

		for (int i = 0; i < lengthofsnake; i++) {

			if (i == 0 && right) {
				rightmouth = new ImageIcon("src/assets/rightmouth.jpg");
				rightmouth.paintIcon(this, g, snakeXlength[i], snakeYlength[i]);
			}

			if (i == 0 && left) {
				leftmouth = new ImageIcon("src/assets/leftmouth.jpg");
				leftmouth.paintIcon(this, g, snakeXlength[i], snakeYlength[i]);
			}
			if (i == 0 && up) {
				upmouth = new ImageIcon("src/assets/upmouth.jpg");
				upmouth.paintIcon(this, g, snakeXlength[i], snakeYlength[i]);
			}
			if (i == 0 && down) {
				downmouth = new ImageIcon("src/assets/downmouth.jpg");
				downmouth.paintIcon(this, g, snakeXlength[i], snakeYlength[i]);
			}
			if (i != 0) {
				snakeimage = new ImageIcon("src/assets/snakeimage2.jpg");
				snakeimage.paintIcon(this, g, snakeXlength[i], snakeYlength[i]);
			}
		}

		String key = "" + snakeXlength[0] + "" + snakeYlength[0];
		Point2D point2d = Point2D.INSTANCES.get(key);
		if (point2d != null) {
			lengthofsnake += 2;
			score += 2;
			Point2D.INSTANCES.remove(key);
		}

		g.setColor(Color.yellow);
		g.setFont(new Font("Times New Roman", Font.BOLD, 23));
		g.drawString("Score: " + score, 670, 32);
		try {
			highscore = readHighScore(g, score);
		} catch (IOException e) {

			e.printStackTrace();
		}
		g.drawString("High Score: " + highscore, 670, 57);
		// g.drawString("Level: ", 670, 32);

		foodimage = new ImageIcon("src/assets/apple.png");
		poisonimage = new ImageIcon("src/assets/poison.png");
		drawFoodPoison(Point2D.INSTANCES, foodimage, g);
		drawFoodPoison(Point2D.POISON_INSTANCES, poisonimage, g);

		if (moves == 0) {
			g.setColor(Color.WHITE);
			g.drawRect(139, 299, 640, 60);
			g.setColor(Color.BLACK);
			g.fillRect(140, 300, 639, 59);

			g.setColor(Color.WHITE);
			g.drawRect(345, 370, 170, 60);
			g.setColor(Color.BLACK);
			g.fillRect(346, 371, 169, 59);

			pressToStartImage = new ImageIcon("src/assets/pressStart.png");
			pressToStartImage.paintIcon(this, g, 140, 300);
			g.setColor(Color.ORANGE);
			g.setFont(new Font("Times New Roman", Font.BOLD, 43));
			g.drawString("Level: " + level, 360, 415);
		}

		for (int b = 1; b < lengthofsnake; b++) {

			if (snakeXlength[b] == snakeXlength[0] && snakeYlength[b] == snakeYlength[0]) {
				gameOver(g);
			}
		}

		for (Map.Entry<String, Point2D> entry : Point2D.POISON_INSTANCES.entrySet()) {
			if (snakeXlength[0] == entry.getValue().getX() && snakeYlength[0] == entry.getValue().getY()) {
				gameOver(g);
			}
		}

		if (Point2D.INSTANCES.size() == 0) {
			winimage = new ImageIcon("src/assets/win.png");
			winimage.paintIcon(this, g, 300, 300);
			nextLevelImage = new ImageIcon("src/assets/nextLevel.png");
			nextLevelImage.paintIcon(this, g, 300, 370);
			level++;
			youWin = true;
			timer.stop();
		}

		g.dispose();

	}

	public int readHighScore(Graphics g, int score) throws IOException {

		int x = 0;

		try {
			in = new FileReader("src/assets/highscore.txt");
			reader = new BufferedReader(in);
			String tmp = reader.readLine();
			x = Integer.parseInt(tmp);
			if (x < score) {
				x = score;
				out = new FileWriter("src/assets/highscore.txt");
				out.write("" + x);
			}

		} finally {
			if (in != null) {
				in.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (out != null) {
				out.close();
			}
		}
		return x;
	}

	public void drawFoodPoison(Map<String, Point2D> instace, ImageIcon image, Graphics g) {
		for (Map.Entry<String, Point2D> entry : instace.entrySet()) {
			image.paintIcon(this, g, entry.getValue().getX(), entry.getValue().getY());
		}
	}

	public void getPositionsXY(int counter, String foodOrPoison) {
		for (int i = 0; i < counter; i++) {
			int c = random.nextInt(17);
			int d = random.nextInt(12);
			if (foodOrPoison.equals("food")) {
				Point2D.getInstance(enemyxpos[c], enemyypos[d]);
			} else if (foodOrPoison.equals("poison")) {
				Point2D.getPoisonInstance(enemyxpos[c], enemyypos[d]);
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		timer.start();
		if (right) {
			for (int i = lengthofsnake; i > 0; i--) {
				snakeYlength[i] = snakeYlength[i - 1];

			}
			for (int i = lengthofsnake; i > 0; i--) {
				snakeXlength[i] = snakeXlength[i - 1];
			}
			snakeXlength[0] = snakeXlength[0] + 25;
			if (snakeXlength[0] > 850) {
				snakeXlength[0] = 850;
			}
			repaint();

		}

		if (left) {

			for (int i = lengthofsnake; i > 0; i--) {
				snakeYlength[i] = snakeYlength[i - 1];

			}
			for (int i = lengthofsnake; i > 0; i--) {
				snakeXlength[i] = snakeXlength[i - 1];
			}
			snakeXlength[0] = snakeXlength[0] - 25;
			if (snakeXlength[0] < 25) {
				snakeXlength[0] = 25;
			}
			repaint();

		}

		if (down) {

			for (int i = lengthofsnake; i > 0; i--) {
				snakeXlength[i] = snakeXlength[i - 1];

			}
			for (int i = lengthofsnake; i > 0; i--) {
				snakeYlength[i] = snakeYlength[i - 1];
			}
			snakeYlength[0] = snakeYlength[0] + 25;
			if (snakeYlength[0] > 625) {
				snakeYlength[0] = 625;
			}
			repaint();
		}

		if (up) {

			for (int i = lengthofsnake; i > 0; i--) {
				snakeXlength[i] = snakeXlength[i - 1];

			}
			for (int i = lengthofsnake; i > 0; i--) {
				snakeYlength[i] = snakeYlength[i - 1];
			}
			snakeYlength[0] = snakeYlength[0] - 25;
			if (snakeYlength[0] < 75) {
				snakeYlength[0] = 75;
			}
			repaint();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_SPACE && (youWin || collided)) {
			moves = 0;
			lengthofsnake = 3;
			right = false;
			left = false;
			up = false;
			down = false;
			repaint();

		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moves++;
			right = true;
			if (left) {
				right = false;
			}
			up = false;
			down = false;

		}
		// if (e.getKeyCode() == KeyEvent.VK_LEFT && moves > 0)
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			moves++;
			left = true;
			if (right) {
				left = false;
			}
			up = false;
			down = false;

		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			moves++;
			up = true;
			if (down) {
				up = false;
			}
			right = false;
			left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			moves++;
			down = true;
			if (up) {
				down = false;
			}
			right = false;
			left = false;
		}

	}

	public void gameOver(Graphics g) {
		gameoverimage = new ImageIcon("src/assets/gameover.png");
		restartImage = new ImageIcon("src/assets/restart.png");
		if (gameoverimage.getIconWidth() == -1 || restartImage.getIconWidth() == -1) {
			g.setColor(Color.red);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString("Game Over", 300, 300);
			g.setFont(new Font("arial", Font.BOLD, 20));
			g.drawString("Space to restart", 350, 340);
		} else {
			gameoverimage.paintIcon(this, g, 230, 320);
			restartImage.paintIcon(this, g, 300, 390);
		}

		collided = true;
		timer.stop();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
