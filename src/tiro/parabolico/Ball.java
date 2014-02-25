//
//  Created by manolo on Feb 17, 2014.
//  Copyright (c) 2014 manolo. All rights reserved.
//

package tiro.parabolico;

/**
 *
 * @author manolo
 */

public class Ball extends Base {
	private static final String PAUSADO = "pausado";
	private static final String DESAPARECE = "desaparece";
	
	/**
	 * Metodo constructor que hereda los atributos de la clase <code>Base</code>.
	 * @param x es la <code>posiscion en x</code> del objeto.
	 * @param y es el <code>posiscion en y</code> del objeto.
	 * @param a es la <code>animacion</code> del objeto.
	 */
	public Ball(int x, int y, Animacion a) {
		super(x,y,a);
	}
	
	/**
	 * Metodo de acceso que regresa la posicion en x del objeto 
	 * @return PAUSADO es el string PAUSADO.
	 */
	public static String getPausado() {
		return PAUSADO;
	}
	
	/**
	 * Metodo de acceso que regresa la posicion en x del objeto 
	 * @return DESAPARECE es el string DESAPARECE.
	 */
	public static String getDesaparece() {
		return DESAPARECE;
	}
}