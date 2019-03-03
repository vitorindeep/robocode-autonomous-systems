package TP;

import java.util.*;
import robocode.*;
import robocode.util.Utils;

public class MeleeBot extends TeamRobot implements Droid {

	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	public void run() {}
	
	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof Enemy) {
			Enemy p = (Enemy) e.getMessage();
			//Ciclo para update
			for(Enemy en2 : this.enemies){
				//Caso encontre atualiza
				if(en2.getName().equals(p.getName())){
					en2.setX(p.getX());
					en2.setY(p.getY());
					return;
				}
			}
			//Não encontrou, adiciona
			this.enemies.add(p);
		} else if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();
			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		}
		kill();
	}
	
	public void OnHitRobot(HitRobotEvent e){
		if(!isTeammate(e.getName()))
			fire(3);
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		for(Enemy en : this.enemies)
			if(en.getName().equals(e.getName()))
				this.enemies.remove(en);
	}
	
	private void go(double x, double y) {
    	x = x - getX(); y = y - getY();
	    double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
	    setTurnRightRadians(Math.atan(Math.tan(goAngle)));
	    ahead(Math.cos(goAngle) * Math.hypot(x, y));
	}
	
	private void kill(){
		if(!this.enemies.isEmpty()){
			double aux=0.0, res=Math.sqrt(Math.pow((getX()-this.enemies.get(0).getX()),2) + Math.pow((getY()-getX()-this.enemies.get(0).getY()), 2));
			Enemy prox=null;
			for(Enemy en : this.enemies){
				aux = Math.sqrt(Math.pow((getX()-en.getX()),2) + Math.pow((getY()-en.getY()), 2));
				if(aux<res){
					res=aux;
					prox=en;
				}
			}
			double dx = prox.getX() - this.getX();
			double dy = prox.getY() - this.getY();
			double theta = Math.toDegrees(Math.atan2(dx, dy));
			turnRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading()));
			fire(3);
			//go(this.enemies.get(0).getX(), this.enemies.get(0).getY());
		}
	}
}