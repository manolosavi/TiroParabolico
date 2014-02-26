//
//  Created by manolo on Feb 24, 2014.
//  Copyright (c) 2014 manolo. All rights reserved.
//

package tiro.parabolico;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author manolo
 */

public class Tiro {
	int deg, range, startX, startY, currentX, currentY;
	double vel, time;
	
	Tiro() {
		time = 0;
		deg = (int)((Math.random()*70) + 10);
		range = (int)((Math.random()*600) + 568);
		vel = sqrt(range*9.8/sin(2*deg));
	}
	
	Tiro (int sX, int sY) {
		startX = sX;
		startY = sY;
	}
	
	double getTime() {
		return time;
	}
	
	void addTime() {
		time+=0.030;
	}
	
	int getX() {
		currentX = (int)(vel*cos(deg)*time);
		return currentX;
	}
	
	int getY() {
		currentY = (int)(vel*sin(deg)*4.9*time*time);
		return currentY;
	}
	
//	boolean cont() {
//		if (1) {
//			
//		}
//		return true;
//	}
}