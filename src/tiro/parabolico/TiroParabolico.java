//
//  Created by manolo on Feb 24, 2014.
//  Copyright (c) 2014 manolo. All rights reserved.
//

package tiro.parabolico;

/**
 *
 * @author manolo
 */

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class TiroParabolico extends JFrame implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
//	Se declaran las variables.
	private Image dbImage;		// Imagen a proyectar	
	private Graphics dbg;		// Objeto grafico
	private Bueno link;
	private SoundClip boom;		// Objeto AudioClip
	private SoundClip bomb;		// Objeto AudioClip
	private LinkedList<Malo> malos = new LinkedList();
	private char dir;
	private char oldDir;
	private boolean pause=false;
	private boolean desaparece=false;
	private int dCount;
	private int range;
	private long tiempoActual;
	private long tiempoInicial;
	private int rM = (int)(Math.random()*3+1);
	private int numMalos=(rM==1)?12:(rM==2)?14:16;
	
	
	public TiroParabolico() {
		init();
		start();
	}
	
	/** 
     * Metodo <I>init</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     */
	public void init() {
		setSize(1200,700);
		
		boom = new SoundClip("resources/boom.wav");	// Sonido cuando chocas con un malo
		boom.setLooping(false);
		bomb = new SoundClip("resources/bomb.wav");	// Sonido cuando chocas con la pared
		bomb.setLooping(false);
		
		
//		Se cargan las imágenes para la animación
		Image link1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link1.png"));
		Image link2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link2.png"));
		Image link3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link3.png"));
		Image link4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link4.png"));
		Image link5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link5.png"));
		Image link6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link6.png"));
		Image link7 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link7.png"));
		Image link8 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("link8.png"));
		Image thwomp1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("thwomp1.png"));
		Image thwomp2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("thwomp2.png"));
		Image thwomp3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("thwomp3.png"));
		Image thwomp4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("thwomp4.png"));
//		Se crea la animación
		Animacion animL = new Animacion(), animT = new Animacion();
		animL.sumaCuadro(link1, 130);
		animL.sumaCuadro(link2, 130);
		animL.sumaCuadro(link3, 130);
		animL.sumaCuadro(link4, 130);
		animL.sumaCuadro(link5, 130);
		animL.sumaCuadro(link6, 130);
		animL.sumaCuadro(link7, 130);
		animL.sumaCuadro(link8, 130);
		animT.sumaCuadro(thwomp1, 1000);
		animT.sumaCuadro(thwomp2, 1000);
		animT.sumaCuadro(thwomp3, 1000);
		animT.sumaCuadro(thwomp4, 1000);
//		Se agrega la animacion a los objetos
		link = new Bueno(100,100,animL);
		link.setX(getWidth()/2 - link.getWidth()/2);
		link.setY(getHeight()/2 - link.getHeight()/2);
		
//		Crea malos
		for (int i=0; i<numMalos; i++) {
			Malo thwomp = new Malo(0,0,animT,((i%2==0)?true:false));
			range = (getWidth()-thwomp.getWidth())-(0);
			thwomp.setX((int)(Math.random()*range + 1));	// posision x es random
			if (i%2 == 0) {
				thwomp.setY(-3*thwomp.getHeight());
			} else {
				thwomp.setY(getHeight()+3*thwomp.getHeight());
			}
			malos.add(thwomp);
		}
		
		setBackground(new Color(43, 48, 51));
		addKeyListener(this);
		addMouseListener(this);
	}
	
	/** 
	 * Metodo <I>start</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
	public void start () {
//		Declaras un hilo
		Thread th = new Thread(this);
//		Empieza el hilo
		th.start();
	}
		
	/** 
	 * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se incrementa
     * la posicion en x o y dependiendo de la direccion, finalmente 
     * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
     * 
     */
	public void run () {
		while (true) {
			actualiza();
			checaColision();
			repaint();	// Se actualiza el <code>Applet</code> repintando el contenido.
			try	{
//				El thread se duerme.
				Thread.sleep (30);
			}
			catch (InterruptedException ex)	{
				System.out.println("Error en " + ex.toString());
			}
		}
	}
	
	/**
	 * Metodo usado para actualizar la posicion de objetos earth y asteroid.
	 * 
	 */
	public void actualiza() {
		if (!pause) {
//			Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
			long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
            
//			Guarda el tiempo actual
			tiempoActual += tiempoTranscurrido;

//			Actualiza la posicion y la animación en base al tiempo transcurrido
			link.actualiza(tiempoTranscurrido);
			switch(dir) {
				case 'u': {
				    link.setY(link.getY() - 1);
					break;    //se mueve hacia arriba
				}
				case 'd': {
				    link.setY(link.getY() + 1);
					break;    //se mueve hacia abajo
				}
				case 'l': {
				 	link.setX(link.getX() - 1);
					break;    //se mueve hacia izquierda
				}
				case 'r': {
					link.setX(link.getX() + 1);
					break;    //se mueve hacia derecha	
				}
			}
			
			for (int i=0; i<numMalos; i++) {
				Malo thwomp = malos.get(i);
				thwomp.actualiza(tiempoTranscurrido);
				if (thwomp.arriba) {
					thwomp.setY(thwomp.getY()+thwomp.getVel());
				} else {
					thwomp.setY(thwomp.getY()-thwomp.getVel());
				}
			}
		}
	}
	
	/**
	 * Metodo usado para checar las colisiones del objeto elefante y asteroid
	 * con las orillas del <code>Applet</code>.
	 */
	public void checaColision() {
//		Colision de la tierra con el Applet dependiendo a donde se mueve.
		switch(dir){
			case 1: { //se mueve hacia arriba con la flecha arriba.
				if (link.getY() < 0) {
					dir = 'd';
					bomb.play();
				}
				break;    	
			}     
			case 2: { //se mueve hacia abajo con la flecha abajo.
				if (link.getY() + link.getHeight() > getHeight()) {
					dir = 'u';
					bomb.play();
				}
				break;    	
			} 
			case 3: { //se mueve hacia izquierda con la flecha izquierda.
				if (link.getX() < 0) {
					dir = 'r';
					bomb.play();
				}
				break;    	
			}    
			case 4: { //se mueve hacia derecha con la flecha derecha.
				if (link.getX() + link.getWidth() > getWidth()) {
					dir = 'l';
					bomb.play();
				}
				break;
			}			
		}

//		checa colision con el applet
		for (int i=0; i<numMalos; i++) {
			Malo thwomp = malos.get(i);
			
//			Thwomp se regresa afuera del applet
			if (thwomp.arriba) {
				if (thwomp.getY() + thwomp.getHeight() > getHeight()) {
					range = (getWidth()-thwomp.getWidth())-(0) + 1;
					thwomp.setX((int)((Math.random()*range) + thwomp.getWidth()/2));
					thwomp.setY(-3*thwomp.getHeight());
					bomb.play();
				}
			} else {
				if (thwomp.getY() < 0) {
					range = (getWidth()-thwomp.getWidth())-(0) + 1;
					thwomp.setX((int)((Math.random()*range) + thwomp.getWidth()/2));
					thwomp.setY(getHeight()+3*thwomp.getHeight());
					bomb.play();
				}
			}
		}

//		Colision entre objetos
		for (int i=0; i<numMalos; i++) {
			Malo thwomp = malos.get(i);
			if (thwomp.intersecta(link)) {
				desaparece = true;
				dCount = 75;
//				Thwomp se regresa afuera del applet
				thwomp.setX((int)((Math.random()*range) + thwomp.getWidth()/2));
				if (thwomp.arriba) {
					thwomp.setY(getHeight()-3*thwomp.getHeight());
				} else {
					thwomp.setY(getHeight()+3*thwomp.getHeight());
				}
				thwomp.score+=1;
				boom.play();
			}
		}
	}
	
	/**
	 * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
	 * @param e es el <code>evento</code> generado al presionar las teclas.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {    //Presiono flecha arriba/w
			dir = 'u';
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {    //Presiono flecha abajo/s
			dir = 'd';
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {    //Presiono flecha izquierda/a
			dir = 'l';
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {    //Presiono flecha derecha/d
			dir = 'r';
		}
    }
	
	public void keyReleased(KeyEvent e){
		if (e.getKeyChar() == 'p') {
			pause = !pause;
		}
	}
	public void keyTyped(KeyEvent e){}
	
	/**
	 * Metodo <I>mousePressed</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera al empezar un click.
	 * @param e es el <code>evento</code> que se genera al empezar un click.
	 */
	public void mousePressed(MouseEvent e) {
//		Para a link si se le da click/vuelve a moverse
		if (link.didClickInside(e.getX(), e.getY())) {
			if (dir == '.') {
				dir = oldDir;
			} else {
				oldDir = dir;
				dir = '.';
			}
		}
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo lo que hace es actualizar el contenedor
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint(Graphics g) {
//		Inicializan el DoubleBuffer
		dbImage = createImage (this.getSize().width, this.getSize().height);
		dbg = dbImage.getGraphics ();

//		Actualiza la imagen de fondo.
		dbg.setColor(getBackground ());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

//		Actualiza el Foreground.
		dbg.setColor(getForeground());
		paint1(dbg);

//		Dibuja la imagen actualizada
		g.drawImage (dbImage, 0, 0, this);
	}
	
	/**
	 * Metodo <I>paint1</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo se dibuja la imagen con la posicion actualizada,
	 * ademas que cuando la imagen es cargada te despliega una advertencia.
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint1(Graphics g) {
		g.setFont(new Font("Helvetica", Font.PLAIN, 20));	// plain font size 20
		g.setColor(Color.white);							// black font
		
		for (int i=0; i<numMalos; i++) {
			Malo thwomp = malos.get(i);
			if (link != null && thwomp != null) {
//				Dibuja la imagen en la posicion actualizada
				g.drawImage(link.getImage(), link.getX(),link.getY(), this);
				g.drawImage(thwomp.getImage(), thwomp.getX(),thwomp.getY(), this);
				g.drawString("Score: " + String.valueOf(thwomp.score), 10, 50);	// draw score at (10,25)
				if (desaparece) {
					dCount--;
					if (dCount == 0) {
						desaparece = !desaparece;
					}
					g.drawString(link.getDesaparece(),link.getX(),link.getY());
				}
			} else {
//				Da un mensaje mientras se carga el dibujo	
				g.drawString("No se cargo la imagen..", 20, 20);
			}
		}
			
		if (pause) {
			g.drawString(link.getPausado(),link.getX(),link.getY());
		}
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		TiroParabolico examen1 = new TiroParabolico();
		examen1.setVisible(true);
		examen1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}