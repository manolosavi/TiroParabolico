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
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class TiroParabolico extends JFrame implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
//	Se declaran las variables.
	private Image dbImage;		// Imagen a proyectar	
	private Graphics dbg;		// Objeto grafico
	private Base brain;
	private Base zombie;
	private SoundClip boom;		// Objeto AudioClip
	private SoundClip bomb;		// Objeto AudioClip
	private char dir;			//dir es la direccion que le vas a dar al objeto 
	private char oldDir;		//old dir es la direccion vieja que tenia el objeto 
	private boolean pause;		//pause es un booleano para checar si el juego esta en pausa
	private boolean sound;		
	private boolean desaparece;	//es para ver si el desplegado de desaparece es igual a true o no
	private boolean tirando;	//tirando es para ver si el misil o el objeto se encuentra moviendo 
	private int range;			
	private int fallCount;
	private int lives;
	private int score;
	private long tiempoActual;
	private long tiempoInicial;
	private double velX, velY;
	private double time;
	private double deg;
	
	
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
		
		fallCount = 0;
		score = 0;
		
		pause = false;
		sound = false;
		desaparece = false;
		tirando = false;
		
		boom = new SoundClip("resources/boom.wav");	// Sonido cuando chocas con un malo
		boom.setLooping(false);
		bomb = new SoundClip("resources/bomb.wav");	// Sonido cuando chocas con la pared
		bomb.setLooping(false);
		
		
//		Se cargan las imágenes para la animación
		Image ball1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball1.png"));
		Image ball2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball2.png"));
		Image ball3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball3.png"));
		Image ball4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball4.png"));
		Image ball5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball5.png"));
		Image ball6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball6.png"));
		Image ball7 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball7.png"));
		Image ball8 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball8.png"));
		Image ball9 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball9.png"));
		Image ball10 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball10.png"));
		Image ball11 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball11.png"));
		Image ball12 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball12.png"));
		Image ball13 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball13.png"));
		Image ball14 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball14.png"));
		Image ball15 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball15.png"));
		Image ball16 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball16.png"));
		Image ball17 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball17.png"));
		Image ball18 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball18.png"));
		Image ball19 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball19.png"));
		Image ball20 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball20.png"));
		Image ball21 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball21.png"));
		Image ball22 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball22.png"));
		Image ball23 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball23.png"));
		Image ball24 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ball24.png"));
		Image basket1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/basket1.png"));
		Image basket2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/basket2.png"));
		Image basket3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/basket3.png"));
		Image basket4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/basket4.png"));
//		Se crea la animación
		Animacion anim1 = new Animacion(), anim2 = new Animacion();
		int velb = 100, velbk = 1000;
		anim1.sumaCuadro(ball1, velb);
		anim1.sumaCuadro(ball2, velb);
		anim1.sumaCuadro(ball3, velb);
		anim1.sumaCuadro(ball4, velb);
		anim1.sumaCuadro(ball5, velb);
		anim1.sumaCuadro(ball6, velb);
		anim1.sumaCuadro(ball7, velb);
		anim1.sumaCuadro(ball8, velb);
		anim1.sumaCuadro(ball9, velb);
		anim1.sumaCuadro(ball10, velb);
		anim1.sumaCuadro(ball11, velb);
		anim1.sumaCuadro(ball12, velb);
		anim1.sumaCuadro(ball13, velb);
		anim1.sumaCuadro(ball14, velb);
		anim1.sumaCuadro(ball15, velb);
		anim1.sumaCuadro(ball16, velb);
		anim1.sumaCuadro(ball17, velb);
		anim1.sumaCuadro(ball18, velb);
		anim1.sumaCuadro(ball19, velb);
		anim1.sumaCuadro(ball20, velb);
		anim1.sumaCuadro(ball21, velb);
		anim1.sumaCuadro(ball22, velb);
		anim1.sumaCuadro(ball23, velb);
		anim1.sumaCuadro(ball24, velb);
		anim2.sumaCuadro(basket1, velbk);
		anim2.sumaCuadro(basket2, velbk);
		anim2.sumaCuadro(basket3, velbk);
		anim2.sumaCuadro(basket4, velbk);
//		Se agrega la animacion a los objetos
		brain = new Base(0,getHeight()*3/4,anim1);
		brain.setY(brain.getY()-brain.getHeight()/2);
		
		zombie = new Base(100,100,anim2);
		zombie.setX(getWidth()/2 - zombie.getWidth()/2);
		zombie.setY(getHeight()/2 - zombie.getHeight()/2);
		
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
	@Override
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
			if (tirando) {
				if (brain.getX() == 0) {
					time = 1;
					deg = (Math.random()*0.8) + 0.5;
					double vel = sqrt((Math.random()*600 + 550)/sin(2*deg));
					velX = vel*cos(deg);
					velY = -vel*sin(deg);
				}
				brain.actualiza(tiempoTranscurrido);
				brain.setX((int) (brain.getX()+velX));
				brain.setY((int) (brain.getY()+(velY++)));
				time+=1;
			}
			
			zombie.actualiza(tiempoTranscurrido);
		}
	}
	
	/**
	 * Metodo usado para checar las colisiones del objeto elefante y asteroid
	 * con las orillas del <code>Applet</code>.
	 */
	public void checaColision() {
//		Colision con el Applet dependiendo a donde se mueve.
		if (brain.getY() + brain.getHeight() > getHeight()) {
			tirando = false;
			brain.setX(0);
			brain.setY(brain.getY()-brain.getHeight()/2);
			fallCount++;
			if (fallCount == 2) {
				fallCount = 0;
				lives--;
			}
			if (sound) {
				bomb.play();
			}		
		}

//		Colision entre objetos
		if (zombie.intersecta(brain)) {
			desaparece = true;
			
			score+=2;
			if (sound) {
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
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {			//Presiono flecha izquierda/a
			dir = 'l';
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {	//Presiono flecha derecha/d
			dir = 'r';
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			sound = !sound;
		} else if (e.getKeyCode() == KeyEvent.VK_I) {
//			Mostrar/Quitar las instrucciones del juego
		} else if (e.getKeyCode() == KeyEvent.VK_G) {
//			Grabar el juego
		} else if (e.getKeyCode() == KeyEvent.VK_C) {
//			Cargar un juego guardado
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			pause = !pause;
		}
    }
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	/**
	 * Metodo <I>mousePressed</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera al empezar un click.
	 * @param e es el <code>evento</code> que se genera al empezar un click.
	 */
	public void mousePressed(MouseEvent e) {
//		Para a brain si se le da click/vuelve a moverse
		if (brain.didClickInside(e.getX(), e.getY())) {
			if (!tirando) {
				tirando = !tirando;
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
		
		if (brain != null && zombie != null) {
//			Dibuja la imagen en la posicion actualizada
			g.drawImage(brain.getImage(), brain.getX(),brain.getY(), this);
			g.drawImage(zombie.getImage(), zombie.getX(),zombie.getY(), this);
			g.drawString("Score: " + String.valueOf(score), 10, 50);	// draw score at (10,25)
		} else {
//			Da un mensaje mientras se carga el dibujo	
			g.drawString("No se cargo la imagen..", 20, 20);
		}
			
		if (pause) {
			
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