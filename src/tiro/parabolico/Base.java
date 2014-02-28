//
//  Created by manolo on Feb 17, 2014.
//  Copyright (c) 2014 manolo. All rights reserved.
//

package tiro.parabolico;

/**
 *
 * @author manolo
 */

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Base {
//	private ImageIcon icono;	//icono.
	private int posX;
	private int posY;
	private Animacion anim;
	
	/**
	 * Metodo constructor usado para crear el objeto
	 * @param x es la <code>posicion en x</code> del objeto.
	 * @param y es la <code>posicion en y</code> del objeto.
	 * @param a es la <code>animacion</code> del objeto.
	 */
	public Base(int x, int y ,Animacion a) {
		posX = x;
		posY = y;
		anim = a;
	}
	
	/**
	 *	Actualiza la imagen (cuadro) actual de la animación,
	 *	si es necesario.
	 * @param t tiempo transcurrido
	*/
	public synchronized void actualiza(long t) {
		anim.actualiza(t);
	}
	
	/**
	 * Metodo de acceso que regresa la imagen del objeto 
	 * @return anim.getImagen() es la <code>imagen</code> del objeto.
	 */
	public Image getImage() {
		return anim.getImagen();
	}
	
	/**
	 * Metodo modificador usado para cambiar la posicion en x del objeto 
	 * @param x es la <code>posicion en x</code> del objeto.
	 */
	public void setX(int x) {
		posX = x;
	}
	
	/**
	 * Metodo modificador usado para cambiar la posicion en y del objeto 
	 * @param y es la <code>posicion en y</code> del objeto.
	 */
	public void setY(int y) {
		posY = y;
	}
	
	/**
	 * Metodo modificador usado para cambiar la animacion del objeto 
	 * @param a es la <code>animacion</code> del objeto.
	 */
	public void setAnimacion(Animacion a) {
		anim = a;
	}
	
	/**
	 * Metodo de acceso que regresa la posicion en x del objeto 
	 * @return posX es la <code>posicion en x</code> del objeto.
	 */
	public int getX() {
		return posX;
	}
	
	/**
	 * Metodo de acceso que regresa la posicion en y del objeto 
	 * @return posY es la <code>posicion en y</code> del objeto.
	 */
	public int getY() {
		return posY;
	}
	
	/**
	 * Metodo de acceso que regresa el ancho del icono 
	 * @return un objeto de la clase <code>ImageIcon</code> que es el ancho del icono.
	 */
	public int getWidth() {
		return anim.getWidth();
	}
	
	/**
	 * Metodo de acceso que regresa el alto del icono 
	 * @return un objeto de la clase <code>ImageIcon</code> que es el alto del icono.
	 */
	public int getHeight() {
		return anim.getHeight();
	}
	
	/**
	 * Metodo de acceso que regresa un nuevo rectangulo
	 * @return un objeto de la clase <code>Rectangle</code> que es el perimetro 
	 * del rectangulo
	 */
	public Rectangle getPerimetro(){
		return new Rectangle(getX(),getY(),getWidth(),getHeight());
	}
	
	/**
	 * Checa si el objeto <code>Base</code> intersecta a otro <code>Base</code>
	 * @param obj objecto con el que se checa si se intersecta
	 * @return un valor boleano <code>true</code> si lo intersecta <code>false</code>
	 * en caso contrario
	 */
	public boolean intersecta(Base obj) {
		return getPerimetro().intersects(obj.getPerimetro());
	}
	
	/**
	 * Checa si el click es dentro del rectangulo del <code>Animal</code>
	 * @param x posicion x del click
	 * @param y posicion y del click
	 * @return un valor boleano <code>true</code> si esta dentro, <code>false</code>
	 * en caso contrario
	 */
	public boolean didClickInside(int x, int y) {
		return getPerimetro().contains(new Point(x,y));
	}
}