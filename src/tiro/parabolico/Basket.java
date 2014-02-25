//
//  Created by manolo on Feb 17, 2014.
//  Copyright (c) 2014 manolo. All rights reserved.
//

package tiro.parabolico;

/**
 *
 * @author manolo
 */

public class Basket extends Base {
	private int vel;
	public static int score;
	
	/**
	 * Metodo constructor que hereda los atributos de la clase <code>Base</code>.
	 * @param x es la <code>posiscion en x</code> del objeto.
	 * @param y es el <code>posiscion en y</code> del objeto.
	 * @param a es la <code>animacion</code> del objeto.
	 * @param arr es la <code>posicion</code> del objeto (arriba/abajo del applet).
	 */
	public Basket(int x, int y, Animacion a) {
		super(x,y,a);
		vel = (int)(Math.random()*4+3);
	}
	
	/**
	 * Metodo de acceso que regresa la posicion en x del objeto 
	 * @return PAUSADO es el string PAUSADO.
	 */
	public int getVel() {
		return vel;
	}
}