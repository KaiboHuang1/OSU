import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions of window
	public static final int GAME_WIDTH = 1280;
	public static final int GAME_HEIGHT = 780;

	private Thread gameThread;
	private Image image;
	private Graphics graphics;
	private static Clip menu;

	private Score score;

	static boolean[] appearC = new boolean[10];
	static boolean[] appearS = new boolean[10];

	private Circle c1;
	private Circle c2;
	private Circle c3;
	private Circle c4;
	private Circle c5;
	private Circle c6;
	private Circle c7;
	private Circle c8;
	private Circle c9;

	private Slider s1;
	private Slider s2;
	private Slider s3;
	private Slider s4;
	private Slider s5;
	private Slider s6;
	private Slider s7;
	private Slider s8;
	private Slider s9;

	private JButton maruButton, playButton, exitButton; // menu buttons
	private JButton tutorial, easy, medium, hard, backButton; // level buttons

	private boolean isTitleScreen = true;

	private int[][] easyMap = {
			{ 177, 596, 934, 1154, 1175, 779, 777, 531, 288, 718, 919, 804, 596, 779, 962, 779, 944, 741, 551, 724, 313,
					478, 253, 113, 293, 524, 644, 867, 1172, 867, 723, 804 },
			{ 319, 405, 251, 148, 513, 359, 537, 233, 395, 339, 419, 687, 616, 509, 615, 509, 387, 158, 457, 337, 201,
					48, 371, 397, 537, 738, 469, 622, 612, 431, 251, 391, 559 },
			{ 433, 1393, 2353, 3313, 4273, 5233, 6193, 8113, 9073, 10033, 10513, 11473, 11953, 13393, 13873, 15313,
					15793, 16753, 17713, 19153, 20593, 21553, 22513, 23953, 24433, 25393, 26353, 27793, 28273, 29233,
					30193, 31633, 32133 } };

	// constructor to initialize variables and add initial JButtons
	public GamePanel() {
		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		score = new Score();

		c1 = new Circle(2000, 2000, 1);
		c2 = new Circle(2000, 2000, 2);
		c3 = new Circle(2000, 2000, 3);
		c4 = new Circle(2000, 2000, 4);
		c5 = new Circle(2000, 2000, 5);
		c6 = new Circle(2000, 2000, 6);
		c7 = new Circle(2000, 2000, 7);
		c8 = new Circle(2000, 2000, 8);
		c9 = new Circle(2000, 2000, 9);

		s1 = new Slider(2000, 2000, 300, 1);
		s2 = new Slider(2000, 2000, 300, 2);
		s3 = new Slider(2000, 2000, 300, 3);
		s4 = new Slider(2000, 2000, 300, 4);
		s5 = new Slider(2000, 2000, 300, 5);
		s6 = new Slider(2000, 2000, 300, 6);
		s7 = new Slider(2000, 2000, 300, 7);
		s8 = new Slider(2000, 2000, 300, 8);
		s9 = new Slider(2000, 2000, 300, 9);

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		playMenu();

		// add MARU! button
		maruButton = new JButton("MARU!");
		maruButton.setFont(new Font("Arial", Font.PLAIN, 24));
		maruButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("Music/MARUClick.wav");
				showGameOptions(); // calls showGameOptions when pressed
			}
		});

		// add PLAY button
		playButton = new JButton("PLAY");
		playButton.setFont(new Font("Arial", Font.PLAIN, 24));
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("Music/PlayClick.wav");
				showLevels(); // calls startGame when pressed
			}
		});

		// add EXIT button
		exitButton = new JButton("EXIT");
		exitButton.setFont(new Font("Arial", Font.PLAIN, 24));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // exits when pressed
			}
		});

		// add Tutorial button
		tutorial = new JButton("Tutorial");
		tutorial.setFont(new Font("Arial", Font.PLAIN, 24));
		tutorial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("Music/PlayClick.wav");
				isTitleScreen = false;
				stopMenu();
				tutorial(); // calls startGame when pressed
			}
		});

		// add Easy button
		easy = new JButton("Easy");
		easy.setFont(new Font("Arial", Font.PLAIN, 24));
		easy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("Music/PlayClick.wav");
				isTitleScreen = false;
				stopMenu();
				easy(); // calls startGame when pressed

			}
		});

		// add Medium button
		medium = new JButton("Medium");
		medium.setFont(new Font("Arial", Font.PLAIN, 24));
		medium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("Music/PlayClick.wav");
				isTitleScreen = false;
				stopMenu();
				medium(); // calls startGame when pressed
			}
		});

		// add Hard button
		hard = new JButton("Hard");
		hard.setFont(new Font("Arial", Font.PLAIN, 24));
		hard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("Music/PlayClick.wav");
				isTitleScreen = false;
				stopMenu();
				hard(); // calls startGame when pressed
			}
		});

		// add Back button
		backButton = new JButton("BACK");
		backButton.setFont(new Font("Arial", Font.PLAIN, 24));
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playSound("Music/PlayClick.wav");
				showGameOptions(); // calls startGame when pressed
			}
		});

		// Add MARU! button initially in middle of the screen
		this.setLayout(null);
		maruButton.setBounds(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 - 25, 200, 50);
		this.add(maruButton);

		// Start game thread
		gameThread = new Thread(this);
		gameThread.start();
	}

	// move around JButtons to show PLAY and EXIT buttons
	private void showGameOptions() {
		this.removeAll();

		// shifts the MARU! button and adds PLAY and EXIT buttons
		maruButton.setBounds(GAME_WIDTH / 2 - 200, GAME_HEIGHT / 2 - 50, 200, 50);
		playButton.setBounds(GAME_WIDTH / 2 + 100, GAME_HEIGHT / 2 - 100, 200, 50);
		exitButton.setBounds(GAME_WIDTH / 2 + 100, GAME_HEIGHT / 2, 200, 50);

		this.add(maruButton);
		this.add(playButton);
		this.add(exitButton);

		this.repaint();
	}

	// adds the buttons to show the level
	public void showLevels() {
		this.removeAll();

		tutorial.setBounds(GAME_WIDTH / 2 + 200, GAME_HEIGHT / 4 - 50, 200, 50);
		easy.setBounds(GAME_WIDTH / 2 + 200, GAME_HEIGHT / 4 + 50, 200, 50);
		medium.setBounds(GAME_WIDTH / 2 + 200, GAME_HEIGHT / 2 - 50, 200, 50);
		hard.setBounds(GAME_WIDTH / 2 + 200, GAME_HEIGHT / 2 + 50, 200, 50);
		backButton.setBounds(50, GAME_HEIGHT - 100, 200, 50);

		this.add(tutorial);
		this.add(easy);
		this.add(medium);
		this.add(hard);
		this.add(backButton);

		this.repaint();
	}

	// paints images off the screen and moves them onto the screen to prevent lag
	public void paint(Graphics g) {
		if (isTitleScreen) {
			super.paint(g);
		} else {
			// We are using "double buffering here"
			image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
			graphics = image.getGraphics();
			draw(graphics); // update the positions of everything on the screen
			g.drawImage(image, 0, 0, this); // move the image on the screen
		}
	}

	// draws each of the individual circles, sliders and spinners as well as the
	// score
	public void draw(Graphics g) {
		c1.draw(g);
		c2.draw(g);
		c3.draw(g);
		c4.draw(g);
		c5.draw(g);
		c6.draw(g);
		c7.draw(g);
		c8.draw(g);
		c9.draw(g);

		s1.draw(g);
		s2.draw(g);
		s3.draw(g);
		s4.draw(g);
		s5.draw(g);
		s6.draw(g);
		s7.draw(g);
		s8.draw(g);
		s9.draw(g);

		Score.draw(g);
	}

	public void moveCircle(Circle c) {
		// System.out.println(c.getRadius());
		if (c.getRadius() > Circle.MIN_RADIUS) {
			c.setRadius(c.getRadius() - 1);

			c.setPosition(c.x0 - c.moveRadius / 2, c.y0 - c.moveRadius / 2);

		} else {
			appearC[c.id] = false;
			c.setRadius(80);

			c.setPosition(2000, 2000);
			c.x0 = 2000;
			c.y0 = 2000;
			c.initialX = 2000;
			c.initialY = 2000;
		}
	}

	// Method to move the slider
	public void moveSlider(Slider s) {
		if (!s.movingAlongPath) {

			if (s.getRadius() > Slider.MIN_RADIUS) {
				s.setRadius(s.getRadius() - 1);
				s.setPosition(s.moveX - s.moveRadius / 2, s.moveY - s.moveRadius / 2);

			} else {
				s.movingAlongPath = true; // Start moving along the path

			}

		} else {
			// Move along the path of the rounded rectangle
			int steps = 100; // Number of steps to reach the right circle
			double dx = (s.finalX - s.initialX) / (double) steps;

			// Update the position of the circle along the path

			s.moveX += dx;

			// Check if the circle has reached the right circle
			// System.out.println(s.moveX + " " + s.moveY);
			if (s.moveX >= s.initialX + s.length) {
				s.moveX = s.initialX + s.length;

				s.moveY = s.initialY;
				s.movingAlongPath = false; // Reset the state for next move

				if (s.goodSlide && s.goodClick) {
					Score.score += 2;
				} else if (s.goodSlide || s.goodClick) {
					Score.score++;
				} else {
					Score.score--;
				}
				appearS[s.id] = false;
				s.length = 200;
				s.initialX = 2000;
				s.initialY = 2000;
				s.angle = 0;
				s.moveX = 2000;
				s.moveY = 2000;
			}

		}

	}

	public void checkCollision(Slider s) {
		// Remove existing listeners to avoid duplicates
		for (MouseListener listener : getMouseListeners()) {
			removeMouseListener(listener);
		}
		for (MouseMotionListener listener : getMouseMotionListeners()) {
			removeMouseMotionListener(listener);
		}

		if (!s.movingAlongPath) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// Get the coordinates of the click
					int mouseX = e.getX();
					int mouseY = e.getY();

					// Check if the click falls inside the circle
					if (s.isMouseClickedInside(mouseX, mouseY) && !s.isClicked) {
						s.isClicked = true;
						if (s.moveRadius <= 120) {
							s.goodClick = true;

						} else {
							s.goodClick = false;
						}
					}
				}
			});
		} else {
			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					// Get the coordinates of the mouse
					int mouseX = e.getX();
					int mouseY = e.getY();

					// Transform the mouse coordinates to the rotated coordinate system
					AffineTransform at = new AffineTransform();
					at.rotate(-Math.toRadians(s.angle), s.initialX, s.initialY);
					Point transformedPoint = new Point(mouseX, mouseY);
					at.transform(transformedPoint, transformedPoint);

					// Check if the transformed mouse coordinates are within the circle
					double distance = Math.sqrt(
							Math.pow(transformedPoint.x - s.moveX, 2) + Math.pow(transformedPoint.y - s.moveY, 2));
					if (distance <= s.moveRadius / 2) {

					} else {
						s.goodSlide = false;
					}
				}
			});
		}
	}

	public void checkCollision(Circle c) {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Get the coordinates of the click
				int mouseX = e.getX();
				int mouseY = e.getY();

				// Check if the click falls inside the circle
				if (c.isMouseClickedInside(mouseX, mouseY) && c.isClicked == false) {
					c.isClicked = true;
					if (c.moveRadius <= 100) {
						Score.score++;
					} else {
						c.moveRadius = 80;
						Score.score--;
					}
				}
			}
		});
	}

	// adds the Circles to the screen and the coordinates when needed
	public void add(Circle c, int x, int y, long t) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				c.x0 = x;
				c.y0 = y;

				c.initialX = c.x0 - 100 / 2;
				c.initialY = c.y0 - 100 / 2;

				appearC[c.id] = true;
			}
		};
		timer.schedule(task, t);
	}

	public void add(Slider s, int x, int y, int l, int a) {

		s.length = l;
		s.initialX = x;
		s.initialY = y;
		s.angle = a;
		s.moveX = x;
		s.moveY = y;
		s.moveRadius = Slider.MAX_RADIUS;

		appearS[s.id] = true;
	}

	// ensures smooth movement of the approaching circle
	public void move() {
		if (appearC[1]) {
			moveCircle(c1);
			checkCollision(c1);
		}
		if (appearC[2]) {
			moveCircle(c2);
			checkCollision(c2);
		}
		if (appearC[3]) {
			moveCircle(c3);
			checkCollision(c3);
		}
		if (appearC[4]) {
			moveCircle(c4);
			checkCollision(c4);
		}
		if (appearC[5]) {
			moveCircle(c5);
			checkCollision(c5);
		}
		if (appearC[6]) {
			moveCircle(c6);
			checkCollision(c6);
		}
		if (appearC[7]) {
			moveCircle(c7);
			checkCollision(c7);
		}
		if (appearC[8]) {
			moveCircle(c8);
			checkCollision(c8);
		}
		if (appearC[9]) {
			moveCircle(c9);
			checkCollision(c9);
		}

		if (appearS[1]) {
			moveSlider(s1);
			checkCollision(s1);
		}
		if (appearS[2]) {
			moveSlider(s2);
			checkCollision(s2);
		}
		if (appearS[3]) {
			moveSlider(s3);
			checkCollision(s3);
		}
		if (appearS[4]) {
			moveSlider(s4);
			checkCollision(s4);
		}
		if (appearS[5]) {
			moveSlider(s5);
			checkCollision(s5);
		}
		if (appearS[6]) {
			moveSlider(s6);
			checkCollision(s6);
		}
		if (appearS[7]) {
			moveSlider(s7);
			checkCollision(s7);
		}
		if (appearS[8]) {
			moveSlider(s8);
			checkCollision(s8);
		}
		if (appearS[9]) {
			moveSlider(s9);
			checkCollision(s9);
		}
	}

	// infinite game loop
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) {
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				move();
				repaint();
				delta--;
			}
		}
	}

	private void tutorial() {
		// Implementation for tutorial
		this.remove(maruButton);
		this.remove(playButton);
		this.remove(exitButton);
		this.remove(tutorial);
		this.remove(easy);
		this.remove(medium);
		this.remove(hard);
		this.remove(backButton);
	}

	// code to run the Easy game mode
	private void easy() {
		this.remove(maruButton);
		this.remove(playButton);
		this.remove(exitButton);
		this.remove(tutorial);
		this.remove(easy);
		this.remove(medium);
		this.remove(hard);
		this.remove(backButton);

		playSound("Music/easy.wav");

		add(s1, easyMap[0][0], easyMap[1][0], 150, 0);
		add(c2, easyMap[0][1], easyMap[1][1], easyMap[2][1]);
		add(c3, easyMap[0][2], easyMap[1][2], easyMap[2][2]);
		add(c4, easyMap[0][3], easyMap[1][3], easyMap[2][3]);
		add(c5, easyMap[0][4], easyMap[1][4], easyMap[2][4]);
		add(c6, easyMap[0][5], easyMap[1][5], easyMap[2][5]);
		add(c7, easyMap[0][6], easyMap[1][6], easyMap[2][6]);
		add(c8, easyMap[0][7], easyMap[1][7], easyMap[2][7]);
		add(c9, easyMap[0][8], easyMap[1][8], easyMap[2][8]);

		add(c1, easyMap[0][9], easyMap[1][9], easyMap[2][9]);
		add(c2, easyMap[0][10], easyMap[1][10], easyMap[2][10]);
		add(c3, easyMap[0][11], easyMap[1][11], easyMap[2][11]);
		add(c4, easyMap[0][12], easyMap[1][12], easyMap[2][12]);

	}

	// code to run the Medium game mode
	private void medium() {
		// Implementation for medium
		this.remove(maruButton);
		this.remove(playButton);
		this.remove(exitButton);
		this.remove(tutorial);
		this.remove(easy);
		this.remove(medium);
		this.remove(hard);
		this.remove(backButton);
	}

	// code to run the Hard game mode
	private void hard() {
		// Implementation for hard
		this.remove(maruButton);
		this.remove(playButton);
		this.remove(exitButton);
		this.remove(tutorial);
		this.remove(easy);
		this.remove(medium);
		this.remove(hard);
		this.remove(backButton);
	}

	// plays sound given a file name
	private void playSound(String soundFile) {
		File file;
		AudioInputStream audioStream;
		Clip clip;

		try {
			// open theme song file
			file = new File(soundFile);

			// Get audio input stream
			audioStream = AudioSystem.getAudioInputStream(file);
			// Open and play the theme song
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start(); // Start playing the sound
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// Method to stop playing the game music
	public static void stopGame() {
		menu.stop();
		menu.close();
	}

	// Method to play menu music
	public static void playMenu() {
		try {
			File file = new File("Music/Menu.wav"); // Open menu music file
			AudioInputStream audio = AudioSystem.getAudioInputStream(file); // Get audio input stream
			menu = AudioSystem.getClip(); // Get a clip for playing audio

			menu.open(audio); // Open the audio clip
			menu.addLineListener(event -> {
				// Restart the music if it stops
				if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
					menu.setMicrosecondPosition(0);
					menu.start();
				}
			});
			menu.start(); // Start playing menu music
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method to stop playing the menu music
	public static void stopMenu() {
		if (menu != null && menu.isOpen()) {
			menu.stop(); // Stop the menu music
			menu.setMicrosecondPosition(0); // Reset its position to the beginning
			menu.close(); // Close the clip
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Implementation for keyTyped
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Implementation for keyPressed
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Implementation for keyReleased
	}
}
