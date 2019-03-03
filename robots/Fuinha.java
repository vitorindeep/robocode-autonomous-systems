package TP;

import robocode.*;
import java.awt.Color;
import java.io.IOException;
import java.util.*;
import robocode.util.Utils;

public class Fuinha extends TeamRobot{

	private int border=150;

	public void run() {
		RobotColors c = new RobotColors();
		c.bodyColor = Color.black;
		c.gunColor = Color.black;
		c.radarColor = Color.black;
		c.scanColor = Color.yellow;
		c.bulletColor = Color.yellow;
		setBodyColor(c.bodyColor);
		setGunColor(c.gunColor);
		setRadarColor(c.radarColor);
		setScanColor(c.scanColor);
		setBulletColor(c.bulletColor);
		try {
			broadcastMessage(c);
		} catch (IOException ignored) {}
		
		while(true) {
			setTurnRadarRight(Double.POSITIVE_INFINITY);
			if(getX()>this.border && getY()>this.border && getX()<getBattleFieldWidth()-this.border && getY()<getBattleFieldHeight()-this.border){
				Random r = new Random();
				int strategy = r.nextInt(1);
				if(strategy==1){
					setTurnRight(270);
					ahead(500);
				}else{
					setTurnLeft(180);
					ahead(400);
					setTurnRight(180);
					ahead(400);
				}
			}
			else
				go(getBattleFieldWidth()/2,getBattleFieldHeight()/2);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		if(!isTeammate(e.getName())){
			double enemyBearing = this.getHeading() + e.getBearing();
			double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
			double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
			try {
				broadcastMessage(new Enemy(e.getName(), enemyX, enemyY));
			} catch (IOException ex) {
				ex.printStackTrace(out);
			}
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
		go(getBattleFieldWidth()/2,getBattleFieldHeight()/2);
	}
	
	public void onHitRobot(HitRobotEvent e) {
		Random r = new Random();
		int strategy = r.nextInt(1);
		if(e.getBearing() > -90 && e.getBearing() <= 90){
			if(strategy==1){
				setTurnRight(45);
				back(200);
			}else{
				setTurnLeft(45);
				back(200);
			}
		}else{
			if(strategy==1){
				setTurnRight(45);
				ahead(200);
			}else{
				setTurnLeft(45);
				ahead(200);
			}
		}
	}

	private void go(double x, double y) {
    	x = x - getX(); y = y - getY();
	    double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
	    setTurnRightRadians(Math.atan(Math.tan(goAngle)));
	    ahead(Math.cos(goAngle) * Math.hypot(x, y));
	}
}
