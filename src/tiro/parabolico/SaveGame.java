//
//  Created by manolo on Feb 24, 2014.
//  Copyright (c) 2014 manolo. All rights reserved.
//

package tiro.parabolico;

/**
 *
 * @author manolo
 */

public class SaveGame {
	private int lives, score, brainX, brainY, zombieX;

	/**
	 * Constructor vacio con darle valores iniciales al momento de
	 * crear el objeto Puntaje
	 */
	public SaveGame() {
		lives = 0;
		score = 0;
		brainX = 0;
		brainY = 0;
		zombieX = 0;
	}

	/**
	 * Metodo constructor usado para crear el objeto
	 * @param l es las <code>vidas</code>.
	 * @param s es el <code>score</code>.
	 * @param bx es la <code>posicion</code> x del cerebro.
	 * @param by es la <code>posicion</code> y del cerebro.
	 * @param zx es la <code>posicion x</code> del zombie.
	 */
	public SaveGame(int l, int s, int bx, int by, int zx) {
		lives = l;
		score = s;
		brainX = bx;
		brainY = by;
		zombieX = zx;
	}

	/**
	 * Metodo que regresa el objeto en formato String 
	 * @return un objeto de la clase <code>String</code>.
	 */
	@Override
	public String toString(){
		return "" + lives + ", " + score + ", " + brainX + ", " + brainY + ", " + zombieX;
	}
}