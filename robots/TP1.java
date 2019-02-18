package sample;
import robocode.*;
import java.awt.Color;
import java.lang.Object;
import robocode.util.Utils;
import standardOdometer.Odometer;

/**
 * TP1 - a robot by (your name here)
 */
public class TP1 extends AdvancedRobot
{
	private double distanceRoundDone = 0.0;
	private static double distanceBattleDone = 0.0;
	private double oldX;
	private double oldY;
	private double posX;
	private double posY;
	private Odometer odometer = new Odometer("IsRacing", this);
	// For controling racing beginning and end
	private int start;

	/**
	 * run: TP1's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		this.oldX=getX();
		this.oldY=getY();
		this.posX=getX();
		this.posY=getY();
		// Timer for getting Tick by Tick
		addCustomEvent(new Condition("timer") {
			public boolean test() {
				return (getTime()!=0);
			}
		});
		// PROF odometer
		addCustomEvent(odometer);
		// For beggining and end control
		start = 0;
		
		while(start < 1) {
			go(18,18);
			execute();
		}
		// dar a volta para o campo de batalha e em frente p/ sair do canto
		turnRight(180);
		ahead(20);
		
		
		// Robot main loop
		while(true) {
			
			// comportamento para fazer a volta
			setTurnRight(60);
			ahead(300);
			setTurnLeft(180);
			ahead(300);
			/*ahead(100);
			turnRight(90);
			ahead(100);
			turnRight(135);
			ahead(141);
			turnRight(135);*/
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		System.out.println("$ BULLET");
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		System.out.println("$ WALL");
		back(20);
		turnRight(45);
	}
	
	private void go(double x, double y) {
    	/* Calculate the difference bettwen the current position and the target position. */
	    x = x - getX();
	    y = y - getY();
	 
	    /* Calculate the angle relative to the current heading. */
	    double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
	 
	    /*
	     * Apply a tangent to the turn this is a cheap way of achieving back to front turn angle as tangents period is PI.
	     * The output is very close to doing it correctly under most inputs. Applying the arctan will reverse the function
	     * back into a normal value, correcting the value. The arctan is not needed if code size is required, the error from
	     * tangent evening out over multiple turns.
	     */
	    setTurnRightRadians(Math.atan(Math.tan(goAngle)));
	 
	    /* 
	     * The cosine call reduces the amount moved more the more perpendicular it is to the desired angle of travel. The
	     * hypot is a quick way of calculating the distance to move as it calculates the length of the given coordinates
	     * from 0.
	     */
	    setAhead(Math.cos(goAngle) * Math.hypot(x, y));
	}
	
	/**
	 * onCustomEvent handler
	 */
	public void onCustomEvent(CustomEvent e) {
		
		Condition cd = e.getCondition();
		
		// If our custom event "timer" went off,
		if(cd.getName().equals("timer")) {
				
			this.posX = getX();
			this.posY = getY();

			// Checking passing by (18,18)
			if(posX == 18 && posY == 18 && oldX != 18 && oldY != 18) {
				System.out.println("(18,18)");
				start++;
			}

			double aux = Math.sqrt(Math.pow((this.posX -this.oldX),2) + Math.pow((this.posY - this.oldY), 2));
			this.distanceRoundDone += aux;
			this.distanceBattleDone += aux;
			this.oldX = getX();
			this.oldY = getY();
			// System.out.println("X: " + this.posX + "\nY: " + this.posY);
			System.out.println("Distance Round Done: " + this.distanceRoundDone);
			// System.out.println("Distance Battle Done: " + this.distanceBattleDone);
		}
		
		// PROF ODOMETER: Method for handling the condition of race finished
		if (cd.getName().equals("IsRacing"))
			System.out.println("$ PROF: " + this.odometer.getRaceDistance());
	}
	
}