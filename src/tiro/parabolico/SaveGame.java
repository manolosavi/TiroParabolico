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
	private int lives, score, ballX, ballY, basketX;

	/**
	 * Constructor vacio con darle valores iniciales al momento de
	 * crear el objeto Puntaje
	 */
	public SaveGame() {
		lives = 0;
		score = 0;
		ballX = 0;
		ballY = 0;
		basketX = 0;
	}

	/**
	 * Metodo constructor usado para crear el objeto
	 * @param l es las <code>vidas</code>.
	 * @param s es el <code>score</code>.
	 * @param bx es la <code>posicion</code> x de la pelota.
	 * @param by es la <code>posicion</code> y de la pelota.
	 * @param bkx es la <code>posicion x</code> de la canasta.
	 */
	public SaveGame(int l, int s, int bx, int by, int bkx) {
		lives = l;
		score = s;
		ballX = bx;
		ballY = by;
		basketX = bkx;
	}

	/**
	 * Metodo que regresa el objeto en formato String 
	 * @return un objeto de la clase <code>String</code>.
	 */
	@Override
	public String toString(){
		return "" + lives + ", " + score + ", " + ballX + ", " + ballY + ", " + basketX;
	}
}