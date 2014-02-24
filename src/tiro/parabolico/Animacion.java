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
import java.util.ArrayList;

/**
	La clase Animacion maneja una serie de imágenes (cuadros)
	y la cantidad de tiempo que se muestra cada cuadro.
*/

public class Animacion{
	
	private ArrayList cuadros;
	private int indiceCuadroActual;
	private long tiempoDeAnimacion;
	private long duracionTotal;
	
	/**
		Crea una nueva Animacion vacía
	*/
	public Animacion(){
		cuadros = new ArrayList();
		duracionTotal = 0;
		iniciar();
	}
	
	/**
		Añade una cuadro a la animación con la duración
		indicada (tiempo que se muestra la imagen).
	*/	
	public synchronized void sumaCuadro(Image imagen, long duracion) {
		duracionTotal += duracion;
		cuadros.add(new cuadroDeAnimacion(imagen, duracionTotal));
	}
	
//	Inicializa la animación desde el principio. 
	public synchronized void iniciar(){
		tiempoDeAnimacion = 0;
		indiceCuadroActual = 0;
	}
	
	/**
		Actualiza la imagen (cuadro) actual de la animación,
		si es necesario.
	*/
	public synchronized void actualiza(long tiempoTranscurrido) {
		if (cuadros.size() > 1){
			tiempoDeAnimacion += tiempoTranscurrido;
			
			if (tiempoDeAnimacion >= duracionTotal){
				tiempoDeAnimacion = tiempoDeAnimacion % duracionTotal;
				indiceCuadroActual = 0; 
			}
			
			while (tiempoDeAnimacion > getCuadro(indiceCuadroActual).tiempoFinal){
				indiceCuadroActual++;
			}
		}
	}
	
	/**
		Captura la imagen actual de la animación. Regeresa null
		si la animación no tiene imágenes.
	*/
	public synchronized Image getImagen(){
		if (cuadros.size() == 0){
			return null;
		}
		else {
			return getCuadro(indiceCuadroActual).imagen;
		}
	}
	
	private cuadroDeAnimacion getCuadro(int i){
		return (cuadroDeAnimacion)cuadros.get(i);
	}
	
	public class cuadroDeAnimacion{
		
		Image imagen;
		long tiempoFinal;
		
		public cuadroDeAnimacion(){
			this.imagen = null;
			this.tiempoFinal = 0;
		}
		
		public cuadroDeAnimacion(Image imagen, long tiempoFinal){
			this.imagen = imagen;
			this.tiempoFinal = tiempoFinal;
		}
		
		public Image getImagen(){
			return imagen;
		}
		
		public long getTiempoFinal(){
			return tiempoFinal;
		}
		
		public void setImagen (Image imagen){
			this.imagen = imagen;
		}
		
		public void setTiempoFinal(long tiempoFinal){
			this.tiempoFinal = tiempoFinal;
		}
	}
	
	/**
	 *	Metodo de acceso que regresa el ancho de la imagen
	 * @return width
	*/
	public int getWidth(){
		if (cuadros.isEmpty()){
			return -1;
		}
		else {
			return getCuadro(indiceCuadroActual).imagen.getWidth(null);
		}
	}
	
	/**
	 *	Metodo de acceso que regresa el alto de la imagen
	 * @return height
	*/
	public int getHeight(){
		if (cuadros.isEmpty()){
			return -1;
		}
		else {
			return getCuadro(indiceCuadroActual).imagen.getHeight(null);
		}
	}
}