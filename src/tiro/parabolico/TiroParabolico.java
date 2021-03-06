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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class TiroParabolico extends JFrame implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
//	Se declaran las variables.
	private Image dbImage;		// Imagen a proyectar
	private Image background;	// Imagen de fondo
	private Graphics dbg;		// Objeto grafico
	private Base brain;			// Objeto cerebro
	private Base zombie;		// Objeto zombie
	private SoundClip alegria;	// Sonido de alegria
	private SoundClip tristeza;	// Sonido de tristeza
	private char dir;			// dir es la direccion que le vas a dar al objeto 
	private int estado;			// el estado actual del juego (0 = corriendo, 1 = pausa, 2 = informacion,3= creditos)
	private int fallCount;		// numero de veces que ha caido el cerebro al piso
	private int lives;			// las vidas que tiene el jugador
	private int score;			// el puntaje del jugador
	private long tiempoActual;	// el tiempo actual que esta corriendo el jar
	private long tiempoInicial;
	private double velX, velY;	// la velocidad en x y y del cerebro
	private double deg;			// el angulo del cerebro
	private double dificultad;
	private boolean guardar;	// guardar es la propiedad que establece que se va a guardar el juego 
	private boolean cargar;		// cargar el juego previamiente establecida
	private boolean sound;		// sound es para ver si el sonido esta activado
	private boolean tirando;	// tirando es para ver si el misil o el objeto se encuentra moviendo 
	
	
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
		//Inicializacion de variables
		setSize(1200,700);
		
		lives = 5;
		fallCount = -1;
		score = 0;
		dificultad = 1;
		estado = 0;
		
		guardar = false;
		cargar = false;
		sound = false;
		tirando = false;
		
		alegria = new SoundClip("resources/alegria.wav");	// Sonido cuando chocas con un malo
		alegria.setLooping(false);
		tristeza = new SoundClip("resources/tristeza.wav");	// Sonido cuando chocas con la pared
		tristeza.setLooping(false);
		
		background = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/background.jpg"));
		
//		Se cargan las imágenes para la animación
		Image brain1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain1.png"));
		Image brain2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain2.png"));
		Image brain3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain3.png"));
		Image brain4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain4.png"));
		Image brain5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain5.png"));
		Image brain6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain6.png"));
		Image brain7 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain7.png"));
		Image brain8 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/brain8.png"));
		Image zombie1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/zombieHappy1.png"));
		Image zombie2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/zombieHappy2.png"));
		Image zombie3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/zombieHappy3.png"));
		Image zombie4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/zombieSad1.png"));
		Image zombie5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/zombieSad2.png"));
//		Se crea la animación
		Animacion anim1 = new Animacion(), anim2 = new Animacion();
		int brainFrameTime = 80, zombieFrameTime = 150;
		anim1.sumaCuadro(brain1, brainFrameTime);
		anim1.sumaCuadro(brain2, brainFrameTime);
		anim1.sumaCuadro(brain3, brainFrameTime);
		anim1.sumaCuadro(brain4, brainFrameTime);
		anim1.sumaCuadro(brain5, brainFrameTime);
		anim1.sumaCuadro(brain6, brainFrameTime);
		anim1.sumaCuadro(brain7, brainFrameTime);
		anim1.sumaCuadro(brain8, brainFrameTime);
		anim2.sumaCuadro(zombie1, zombieFrameTime);
		anim2.sumaCuadro(zombie2, zombieFrameTime);
		anim2.sumaCuadro(zombie3, zombieFrameTime);
		anim2.sumaCuadro(zombie4, zombieFrameTime);
		anim2.sumaCuadro(zombie5, zombieFrameTime);
//		Se agrega la animacion a los objetos
		brain = new Base(0,0,anim1);
		brain.setY(getHeight()-brain.getHeight());
		
		zombie = new Base(0,552,anim2);
		zombie.setX((int) ((Math.random()*600 + 408)));
		setResizable(false);
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
//			Se actualiza el <code>Applet</code> repintando el contenido.
			repaint();
			try	{
//				El thread se duerme.
				Thread.sleep (20);
			}
			catch (InterruptedException ex)	{
				System.out.println("Error en " + ex.toString());
			}
		}
	}
	
	/**
	 * Metodo usado para actualizar la posicion de objetos zombie y cerebro.
	 * 
	 */
	public void actualiza() {
		if(guardar){
			guardar = false;
			try {
			 grabaArchivo();
			} catch(IOException e) {
			 System.out.println("Error en guardar");
			}
		 }

		 if(cargar){
			cargar = false;
			try {
			leeArchivo();
			} catch(IOException e) {
			 System.out.println("Error en cargar");
			 }
		}
		
		if (estado == 0) {
//			Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
			long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
            
//			Guarda el tiempo actual
			tiempoActual += tiempoTranscurrido;

//			Actualiza la posicion y la animación en base al tiempo transcurrido
			if (tirando) {
				if (brain.getX() == 0) {
					deg = (Math.random()*0.7) + 0.5;
					double vel = sqrt((Math.random()*500 + 550)/sin(2*deg));
					velX = vel*cos(deg);
					velY = -vel*sin(deg);
				}
				brain.actualiza(tiempoTranscurrido);
				brain.setX((int) (brain.getX()+velX*dificultad));
				brain.setY((int) (brain.getY()+velY*dificultad));
				velY += dificultad;
			}
			
			if (dir == 'l') {
				zombie.actualiza(tiempoTranscurrido);
				int newX = zombie.getX()-5;
				if (newX >= getWidth()/2) {
					zombie.setX(newX);
				}
			} else if (dir == 'r') {
				zombie.actualiza(tiempoTranscurrido);
				int newX = zombie.getX()+5;
				if (newX <= getWidth()-zombie.getWidth()) {
					zombie.setX(newX);
				}
			}
		}
		if(lives == 0){
			estado = 3;
		}
	}
	
	public void leeArchivo() throws IOException{
		//Lectura del archivo el cual tiene las variables del juego guardado
		BufferedReader fileIn;
		try {
			fileIn = new BufferedReader(new FileReader("Guardado"));
		} catch (FileNotFoundException e){
			File puntos = new File("Guardado");
			PrintWriter fileOut = new PrintWriter(puntos);
			fileOut.println("100,demo");
			fileOut.close();
			fileIn = new BufferedReader(new FileReader("Guardado"));
		}
		String dato = fileIn.readLine();
		deg = (Double.parseDouble(dato));
		dato = fileIn.readLine();
		score = (Integer.parseInt(dato));
		dato= fileIn.readLine();
		tiempoActual = (Long.parseLong(dato));
		dato = fileIn.readLine();
		brain.setX(Integer.parseInt(dato));
		dato =fileIn.readLine();
		brain.setY(Integer.parseInt(dato));
		dato = fileIn.readLine();
		zombie.setX(Integer.parseInt(dato));
		dato = fileIn.readLine();
		tirando = Boolean.parseBoolean(dato);
		dato = fileIn.readLine();
		estado = Integer.parseInt(dato);
		dato = fileIn.readLine();
		velX = Double.parseDouble(dato);
		dato = fileIn.readLine();
		velY = Double.parseDouble(dato);
		dato = fileIn.readLine();
		fallCount = Integer.parseInt(dato);

		fileIn.close();
	}
	

	public void grabaArchivo() throws IOException{
		//Grabar las variables necesarias para reiniciar el juego de donde se quedo el usuario en un txt llamado Guardado
		PrintWriter fileOut= new PrintWriter(new FileWriter("Guardado"));
		fileOut.println(String.valueOf(deg));
		fileOut.println(String.valueOf(score));
		fileOut.println(String.valueOf(tiempoActual));
		fileOut.println(String.valueOf(brain.getX()));
		fileOut.println(String.valueOf(brain.getY()));
		fileOut.println(String.valueOf(zombie.getX()));
		fileOut.println(String.valueOf(tirando));
		fileOut.println(String.valueOf(estado));
		fileOut.println(String.valueOf(velX));
		fileOut.println(String.valueOf(velY));
		fileOut.println(String.valueOf(fallCount));
		
		fileOut.close();
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
			brain.setY(getHeight()-brain.getHeight());
			fallCount++;
			if (fallCount == 3) {
				fallCount = 0;
				lives--;
				dificultad += 0.5;
			}
			if (sound) {
				tristeza.play();
			}		
		}

//		Colision entre objetos
		if (zombie.intersecta(brain)) {
			tirando = false;
			brain.setX(0);
			brain.setY(getHeight()-brain.getHeight());
			score += 2;
			if (sound) {
				alegria.play();
			}
		}
	}
	
	/**
	 * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
	 * @param e es el <code>evento</code> generado al presionar las teclas.
	 */
        @Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {			//Presiono flecha izquierda/a
			dir = 'l';
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {	//Presiono flecha derecha/d
			dir = 'r';
		} else if (e.getKeyCode() == KeyEvent.VK_S) {		//Presiono tecla s / para quitar sonido
			sound = !sound;
		} else if (e.getKeyCode() == KeyEvent.VK_I) {
//			Mostrar/Quitar las instrucciones del juego
			if(estado ==2){
				estado=0;
			}else{
				estado =2;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_G) {	//Presiono tecla G para guardar el juego
			guardar = true;
		} else if (e.getKeyCode() == KeyEvent.VK_C) {	//Presiono tecla C para cargar el juego
			cargar = true;	
		} else if (e.getKeyCode() == KeyEvent.VK_P) {	//Presiono tecla P para parar el juego en ejecuccion
			if(estado==1){
				estado=0;
			}else{
				estado =1;
			}
		}
    }

    /**
	 * Metodo <I>keyReleased</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que detiene el movimiento del zombie.
	 * @param e es el <code>evento</code> que se genera al dejar de presionar la tecla izquierda o derecha.
	 */
        @Override
	public void keyReleased(KeyEvent e){
		//Si se deja de presionar la tecla izquierda el zombie se dejara de mover, lo mismo sucede con la tecla derecha
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dir = '.';
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dir = '.';
		}
	}
        @Override
	public void keyTyped(KeyEvent e){}
	
	/**
	 * Metodo <I>mousePressed</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera al empezar un click.
	 * @param e es el <code>evento</code> que se genera al empezar un click.
	 */
        @Override
	public void mousePressed(MouseEvent e) {
//		Para a brain si se le da click/vuelve a moverse
		if (brain.didClickInside(e.getX(), e.getY())) {
			if (!tirando) {
				tirando = !tirando;
			}
		}
	}
        @Override
	public void mouseClicked(MouseEvent e) {}
        @Override
	public void mouseReleased(MouseEvent e){}
        @Override
	public void mouseEntered(MouseEvent e) {}
        @Override
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo lo que hace es actualizar el contenedor
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
        @Override
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
			g.drawImage(background, 0, 0, this);
			if(estado == 0){
//			Dibuja el estado corriendo del juego
				if (tirando) {
					g.drawImage(brain.getImage(), brain.getX(),brain.getY(), this);
				} else {
					g.drawImage(brain.getImage0(), brain.getX(),brain.getY(), this);
				}
				if (dir != '.') {
					g.drawImage(zombie.getImage(), zombie.getX(),zombie.getY(), this);
				} else {
					g.drawImage(zombie.getImage0(), zombie.getX(),zombie.getY(), this);
				}
				g.drawString("Score: " + String.valueOf(score), 10, 50);	// draw score at (10,25)
				g.drawString("Vidas: " + String.valueOf(lives), 10, 75);	// draw score at (10,25)
			} else if(estado == 1){
//				Dibuja el estado de pausa en el jframe
				g.drawString("PAUSA", getWidth()/2 - 100, getHeight()/2);
			} else if(estado == 2){
//				Dibuja el estado de informacion para el usuario en el jframe
				g.drawString("INSTRUCCIONES", getWidth()/2 - 210, 200);
				g.drawString("Para jugar debes presionar con el", getWidth()/2 - 210, 250);
				g.drawString("mouse en el cerebro de la izquierda con", getWidth()/2 - 210, 280);
				g.drawString("las teclas ← y → mueves el zombie.", getWidth()/2 - 210,310);
				g.drawString("Se el mejor zombie y come muchos cerebros!", getWidth()/2 - 210, 340);
			} else if(estado == 3){
//				Dibuja el estado de creditos en el jframe
				g.setColor(new Color(78, 88, 93));
				g.fillRect(100, 100, getWidth() - 200, getHeight() - 200);
				g.setColor(Color.white);
				g.drawString("CREDITOS", getWidth()/2 - 210, 200);
				g.drawString("Andres Rodriguez    A00812121", getWidth()/2 - 210, 250);
				g.drawString("Alejandro Sanchez   A01191434", getWidth()/2 - 210, 300);
				g.drawString("Manuel Sañudo       A01192241", getWidth()/2 - 210, 350);
			}

		} else {
//			Da un mensaje mientras se carga el dibujo	
			g.drawString("No se cargo la imagen..", 20, 20);
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