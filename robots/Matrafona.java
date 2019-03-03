package TP;

import java.util.*;
import java.util.concurrent.*;
import robocode.*;
import robocode.util.Utils;

public class Matrafona extends TeamRobot implements Droid {
	
	public void run() {}

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
}