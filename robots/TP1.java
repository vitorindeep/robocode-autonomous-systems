package TP;
import robocode.*;
import standardOdometer.Odometer;
import robocode.util.Utils;
import java.awt.Color;
import java.math.*;

/**
 * TP1 - a robot by (your name here)
 */
public class TP1 extends AdvancedRobot
{
	// Private Instance Variable
	private double distanceRoundDone = 0.0;
	private static double distanceBattleDone = 0.0;
	private double oldX;
	private double oldY;
	private double posX;
	private double posY;
	private boolean isRacing = false;
	private int turns = 0;
	private Odometer odometer = new Odometer("IsRacing", this);

	/**
	 * run: TP1's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		setColors(Color.red,Color.red,Color.red);
		this.oldX=getX();
		this.oldY=getY();
		this.posX=getX();
		this.posY=getY();
		addCustomEvent(odometer);
		addCustomEvent(new Condition("timer") {
			public boolean test() {
				return (getTime()!=0);
			}
		});
		//Face robot to origin
		turnRight(225-getHeading());
		// Go to begining position
		while(!isRacing)
			go(18,18);
		// Face robot to North
		turnRight(360-getHeading());
		// Robot main loop
		while(true){
			// Searching for others
			turnRight(1);
			// Returning to begining
			if(this.turns>=3)
				go(18,18);
			// Final prints
			if(this.posX==18.0 && this.posY==18.0 && isRacing && this.turns>=3){
				System.out.println("Distance Battle Done TP: " + this.distanceBattleDone);
				System.out.println("Distance Battle Done OD: " + this.odometer.getRaceDistance());
				isRacing=false;
			}
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		if(isRacing){
			turnLeft(e.getBearing()*2);
			ahead(e.getDistance()+18);
			setTurnRight(60);
			ahead(30);
			this.turns++;
		}
	}
	
	/**
	 * onHitRobot: What to do when you see another robot
	 */
	
	public void onHitRobot(HitRobotEvent e){
		setTurnLeft(10);
		back(20);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		if(isRacing){
			turnRight(e.getBearing());
			turnRight(90);
			ahead(60);
		}
	}
	
	/**
	 * onCustomEvent handler
	 */
	public void onCustomEvent(CustomEvent e) {
		// If our custom event "timer" went off,
		if(e.getCondition().getName().equals("timer")) {
			this.posX = getX();
			this.posY = getY();
			if(this.posX==18.0 && this.posY==18.0 && this.turns<3)
				isRacing=true;
			if(isRacing){
				double aux = Math.sqrt(Math.pow((this.posX -this.oldX),2) + Math.pow((this.posY - this.oldY), 2));
				this.distanceRoundDone += aux;
				this.distanceBattleDone += aux;
			}
			this.oldX = getX();
			this.oldY = getY();
		}else if(e.getCondition().getName().equals("IsRacing")){
			this.odometer.getRaceDistance();
		}
	}
	
	private void go(double x, double y) {
    	x = x - getX(); y = y - getY();
	    double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
	    setTurnRightRadians(Math.atan(Math.tan(goAngle)));
	    ahead(Math.cos(goAngle) * Math.hypot(x, y));
	}
}