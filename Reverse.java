// Don Tran and Kaibo Huang
// June 17, 2024
// This class draws the reverse slider.
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Reverse extends Rectangle {
	public int radius = 100; // initial size of the circles
	public int moveRadius = 200; // initial size of the larger circle
	public static final int MAX_RADIUS = 200; // Maximum radius size
	public static final int MIN_RADIUS = 100; // Minimum radius size
	int initialX, initialY;
	int finalX, finalY; // Position of the right circle
	int moveX, moveY;
	double moveTime;
	boolean mousePressed;
	boolean zPressed;
	boolean xPressed;

	int id;
	int length;
	double angle; // Rotation angle

	boolean isClicked;
	boolean movingAlongPath; // Indicates if the circle is moving along the path
	boolean goodSlide;
	boolean goodClick;
	boolean reversePath;
	int scoreState = 0;

	public static Color c;
	public static Color moveC;
	int scoreX, scoreY;
	int num = 0;

	// New constructor with rotation angle
	public Reverse(int centerX, int centerY, int l, int i, double angle) {
		super(centerX - 100 / 2, centerY - 100 / 2, 100, 100);

		isClicked = false;
		movingAlongPath = false; // Indicates if the circle is moving along the path
		goodSlide = true;
		goodClick = false;
		reversePath = false;
		scoreState = 0;
		mousePressed = false;
		zPressed = false;
		xPressed = false;

		initialX = centerX;
		initialY = centerY;
		moveX = centerX;
		moveY = centerY;
		id = i;
		length = l;
		this.angle = angle; // Set the angle
	}

	// Draws the current location of the circle to the screen
	public void draw(Graphics g) {
		this.finalX = initialX + length; // Calculate final position of the right circle
		this.finalY = initialY; // Same y-position as the initial

		Graphics2D g2d = (Graphics2D) g;

		Stroke oldStroke = g2d.getStroke(); // Save the old stroke

		g2d.setStroke(new BasicStroke(10)); // Change the thickness as needed

		// Save the original transform
		AffineTransform originalTransform = g2d.getTransform();

		// Apply rotation
		g2d.rotate(Math.toRadians(angle), initialX, initialY);

		g2d.setColor(Color.white);
		// Draw the connecting rectangle with rounded edges
		int rectX = initialX - radius / 2;
		int rectY = initialY - radius / 2;
		g2d.drawRoundRect(rectX, rectY, length + radius, radius, radius, radius);

		g2d.setColor(c);
		g2d.fillRoundRect(rectX + 2, rectY + 2, length + radius - 4, radius - 4, radius, radius);
		g2d.setColor(Color.white);

		int[] xPoints = { initialX + length + 25 / 2, initialX + length - 25 / 2, initialX + length + 25 / 2 };
		int[] yPoints = { initialY - 25 / 2, initialY, initialY + 25 / 2 };
		g2d.fillPolygon(xPoints, yPoints, 3);
		g2d.setColor(Color.white);
		// Draw the left circle
		g2d.drawOval(initialX - radius / 2, initialY - radius / 2, radius, radius);

		// Draw the right circle

		g2d.drawOval(initialX + length - radius / 2, initialY - radius / 2, radius, radius);

		g2d.setStroke(new BasicStroke(7)); // Change the thickness as needed

		// Draw the larger circle around the left circle
		g2d.setColor(moveC);
		g2d.drawOval(moveX - moveRadius / 2, moveY - moveRadius / 2, moveRadius, moveRadius);

		// Restore the original transform
		g2d.setTransform(originalTransform);
		// Draw the number inside the left circle
		g2d.setColor(Color.white);
		Font originalFont = g2d.getFont();
		Font largeFont = originalFont.deriveFont(48f); // Set a larger font size
		g2d.setFont(largeFont);
		FontMetrics fm = g2d.getFontMetrics();

		g2d.drawString(Integer.toString(num), initialX - fm.stringWidth(Integer.toString(num)) / 2,
				initialY + fm.getAscent() / 2 - fm.getDescent());
		g2d.setFont(originalFont); // Restore the original font

		g2d.setStroke(oldStroke); // Restore the old stroke

	}

	// Method to set the radius and update the rectangle bounds
	public void setRadius(int r) {
		moveRadius = r;
	}

	// Method to get the radius
	public int getRadius() {
		return moveRadius;
	}

	// Method to update the position based on the center
	public void setPosition(int centerX, int centerY) {
		this.x = centerX;
		this.y = centerY;
	}

	// Method to check if the mouse click is within the blue moving circle
	public boolean isMouseClickedInside(int mouseX, int mouseY) {

		return (double) Math.sqrt(Math.pow(mouseX - moveX, 2) + Math.pow(mouseY - moveY, 2)) <= moveRadius / 2;
	}
}
