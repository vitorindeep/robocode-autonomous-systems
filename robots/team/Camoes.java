package TP;

import java.util.*;
import java.util.concurrent.*;
import robocode.*;
import robocode.util.Utils;

public class Camoes extends TeamRobot implements Droid {
	
	private int border = 150;
	
	public void run() {
		while(true) {
			fire(3);
			if(getX()>this.border && getY()>this.border && getX()<getBattleFieldWidth()-this.border && getY()<getBattleFieldHeight()-this.border){
				Random r = new Random();
				int strategy = r.nextInt(1);
				if(strategy==1){
					setTurnRight(270);
					ahead(500);
					fire(3);
				}else{
					setTurnLeft(180);
					ahead(400);
					fire(3);
					setTurnRight(180);
					ahead(400);
					fire(3);
				}
			}else
				go(getBattleFieldWidth()/2,getBattleFieldHeight()/2);
		}
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof Enemy) {
			Enemy en = (Enemy) e.getMessage();
			double dx = en.getX() - this.getX();
			double dy = en.getY() - this.getY();
			double theta = Math.toDegrees(Math.atan2(dx, dy));
			turnRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading()));
			fire(3);
			ahead(100);
		} else if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();
			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		}
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		Random r = new Random();
		int strategy = r.nextInt(1);
		if(strategy==1){
			setTurnRight(60);
			ahead(100);
		}else{
			setTurnLeft(60);
			ahead(100);
		}
	}
	
	public void onHitWall(HitWallEvent e) {
		Random r = new Random();
		int strategy = r.nextInt(1);
		if(e.getBearing() > -90 && e.getBearing() <= 90){
			if(strategy==1){
				setTurnRight(45);
				back(100);
			}else{
				setTurnLeft(45);
				back(100);
			}
		}else{
			if(strategy==1){
				setTurnRight(45);
				ahead(100);
			}else{
				setTurnLeft(45);
				ahead(100);
			}
		}
	}
	
	public void onHitRobot(HitRobotEvent e) {
		if(!isTeammate(e.getName())){
			fire(3);
			ahead(5);
		}
	}
	
	private void go(double x, double y) {
    	x = x - getX(); y = y - getY();
	    double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
	    setTurnRightRadians(Math.atan(Math.tan(goAngle)));
	    ahead(Math.cos(goAngle) * Math.hypot(x, y));
	}
}