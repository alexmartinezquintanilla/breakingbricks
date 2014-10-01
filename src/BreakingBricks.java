
/**
 *
 * @author José Elí Santiago Rodríguez A07025007, Alex Martinez Quintanilla
 * A00810480
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JFrame;
import java.net.URL;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BreakingBricks  extends JFrame implements Runnable, KeyListener {

    // Imagen a proyectar en el JFrame
    private Image imaImagenJFrame;  
    // Objeto grafico de la Imagen
    private Graphics graGraficaJFrame;
    //Contador de vidas
    private int iVidas;
    //Dirección en la que se mueve el crowbar
    private int iDireccionCrowbar;
    //Dirección de la mosca
    private int iDireccionMosca;
    //X del proyectil
    private int iMovX;
    //Y del proyectil
    private int iMovY;
    //Contador de puntos
    private int iScore;
/////Se necesitan?
    private int posX;
    private int posY;
    private int iAzar;
////-----------------
    private URL urlImagenBackG = this.getClass().getResource("fondo.png");
    //Objeto de la clase personaje. El Bate (slider de brick breaker)
    private Personaje perCrowbar;
    //vida y score como personajes por si se implementa colision
    private Personaje perScoreVidas;
    //Objeto personaje mosca aliada
    private Personaje perMosca;
     //Linked List para las charolas
    private LinkedList lnkCharolas;
    //Linked list para la pelota (temporal?)
    private LinkedList lnkProyectiles;
      // Objeto SoundClip cuando la pelota colisiona con el crowbar o la pared
    private SoundClip scSonidoColisionPelota;  
    //Objeto SoundClip para cuando la charola es golpeada la primera vez
    private SoundClip scSonidoColisionCharolaGolpeada; 
    //Objeto SoundClip para cuando la charola es destruida 
    private SoundClip scSonidoColisionCharolaRota; 
    //Objeto SoundClip para la música de fondo
    private SoundClip scSonidoBGM; 
    //Objeto Soundclip para cuando la mosca está en la derecha
    private SoundClip scSonidoMoscaD;
    //Objeto SoundClip para cuando la mosca está en la izquierda
    private SoundClip scSonidoMoscaI;
    //Boleano para pausar el juego.
    private boolean bPausado;    
    //Booleana para saber si el juego comenzó o no
    private boolean bGameStarted;
    //Ubicación de la mosca para sonido estereo: True = Der. False = Izq
    private boolean bUbicacionMosca;
    //URL para cargar imagen de la mosca
    private URL urlImagenMosca = this.getClass().getResource("Mosca/mosco.gif");
    //URL para cargar imagen de la charola
    private URL urlImagenCharola = 
            this.getClass().getResource("Charola/charola.png");
    //URL para cargar imagen de la charola golpeada
    private URL urlImagenCharolaGolpeada = 
            this.getClass().getResource("Charola/charolagolpeada.png");
    //URL para cargar imagen de la charola rota
    private URL urlImagenCharolaRota = 
            this.getClass().getResource("Charola/charolarota.gif");
    //URL para cargar la imagen de la pelota
    private URL urlImagenPelota = 
            this.getClass().getResource("proyectil.gif");
    //URL para cargar la imagen de pausa
    private URL urlImagenPausa = this.getClass().getResource("pause.png");
    //Imagen al pausar el juego.
    private URL urlImagenScoreVidas = 
            this.getClass().getResource("scorevidas.png");
    //imagen para el score y las vidas
    private Image imaImagenPausa = 
            Toolkit.getDefaultToolkit().getImage(urlImagenPausa);
    private URL urlImagenInicio = 
            this.getClass().getResource("pantallaInicio.png");
    //Imagen al pausar el juego.
    private Image imaImagenInicio = 
            Toolkit.getDefaultToolkit().getImage(urlImagenInicio);
    
    //Constructor de BreakingBricks
    public BreakingBricks() {
        init();
        start();
    }
     /**
     * init
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>JFrame</code> y se definen funcionalidades.
     */
    public void init() {
        
//////////RELEVANTE AL JUEGO
        // hago el JFRAME de un tamaño 800,700
        setSize(800, 700);
        // introducir instrucciones para iniciar juego
        bPausado = false; //Booleana para pausar
        //Inicializamos las vidas al azar entre 3 y 5
        iVidas = (int) (Math.random() * (6 - 3) + 3);
        //Inicializamos el score en 0
        iScore = 0;
        //inicializamos la variable que checa si ya empezó el juego en falso.
        perScoreVidas = new Personaje (40, 450 ,
                Toolkit.getDefaultToolkit().getImage(urlImagenScoreVidas));
        //Creo el sonido para la música de fondo; lo loopeo y lo inicio.
        scSonidoBGM = new SoundClip("BGM.wav");
        scSonidoBGM.setLooping(true);
        scSonidoBGM.play();
        //bandera para saber si el juego ha iniciado o no
        bGameStarted = false;
        
      
        
 //////////RELEVANTE A LAS CHAROLAS
         //Se crea una lista para los objetos charola
        lnkCharolas = new LinkedList();
        //Se crean las charolas y se meten a la lista
        int iAzar = (int) (Math.random() * (11 - 8) + 8);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = 0;
            posY = (int) (Math.random() * getHeight());
            // se crea el personaje caminador
            Personaje perCharola;
            perCharola = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenCharola));
            perCharola.setX(0 - perCharola.getAncho());
            perCharola.setY((int) (Math.random() * (getHeight()
                    - perCharola.getAlto())));
            perCharola.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            lnkCharolas.add(perCharola);
            //creo el sonido  de la charola golpeada la primera vez
            scSonidoColisionCharolaGolpeada = new SoundClip
                        ("Charola/charolagolpeada.wav");
            //creo el sonido de la charola rompiéndose
            scSonidoColisionCharolaRota = new SoundClip
                        ("Charola/charolarota.wav");
        }
        
//////////RELEVANTE AL CROWBAR
        // se crea imagen del crowbar
        URL urlImagenCrowbar = this.getClass().getResource("crowbar.png");
        // se crea el crowbar
        perCrowbar = new Personaje(getWidth() / 2, getHeight() / 2,
                Toolkit.getDefaultToolkit().getImage(urlImagenCrowbar));
        //Se inicializa con velocidad 7
        perCrowbar.setVelocidad(7);
        //posiciona al crowbar en el centro  inferior
        perCrowbar.setX((getWidth() / 2) - (perCrowbar.getAncho() / 2));
        //perCrowbar.setY((getHeight() / 2) - (perCrowbar.getAlto() / 2));
        perCrowbar.setY(getHeight() - perCrowbar.getAlto() -20);
        
///////////RELEVANTE AL PROYECTIL
        //inicializamos el movimiento del proyectil en X 
        iMovX = 4;
         //inicializamos el movimiento del proyectil en Y
        iMovY = 4;
        lnkProyectiles = new LinkedList();
        //se crean de 8 a 10 caminadores y se guardan en la lista de caminadores
        iAzar = (int) (Math.random() * (16 - 10) + 10);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = (int) (Math.random() * getHeight());
            posY = 0;
            // se crea el personaje caminador
            Personaje perProyectil;
            perProyectil = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
            perProyectil.setX((int) (Math.random() * (getWidth() 
                    - perProyectil.getAncho())));
            perProyectil.setY(-perProyectil.getAlto() 
                    - ((int) (Math.random() * getWidth())));
            lnkProyectiles.add(perProyectil);
        //creo el sonido del proyectil
        scSonidoColisionPelota = new SoundClip("Proyectil/tapa.wav");
        }
        
//////////RELEVANTE A LA MOSCA
        //se crea la imagen de la mosca
        URL urlImagenMosca = this.getClass().getResource("Mosca/mosco.gif");
        //Se crea el soundclip para cuando la mosca está a la derecha y loopea
        scSonidoMoscaD = new SoundClip("Mosca/moscaderecha.wav");
        scSonidoMoscaD.setLooping(true);
        //Se crea el soundclip para cuando la mosca está a la izquierda y loopea
        scSonidoMoscaI = new SoundClip ("Mosca/moscaizquierda.wav");
        scSonidoMoscaI.setLooping(!bUbicacionMosca);
        //Se crea a la mosca
        perMosca = new Personaje((int) (Math.random() * 
                (this.getWidth() - 1) + 1), (int) (Math.random() * 
                (this.getHeight() - 1) + 1),
                Toolkit.getDefaultToolkit().getImage(urlImagenMosca));
        perMosca.setVelocidad(7);
        //Inicializo la dirección de la mosca como 0
        iDireccionMosca = 0;
        //Si la mosca está en la derecha, suena en la derecha
        if ( perMosca.getX() > this.getWidth() /2) {
            bUbicacionMosca = true;
             scSonidoMoscaD.play();
        }
        //Si la mosca está en la izquierda, suena en la izquierda
        else {
            bUbicacionMosca = false;
            scSonidoMoscaI.play();
        } 

        //Se crea una lista para los objetos charola
        lnkCharolas = new LinkedList();
        //Se crean las charolas y se meten a la lista
        iAzar = (int) (Math.random() * (11 - 8) + 8);
        for (int iK = 1; iK <= iAzar; iK++) {
            posX = 0;
            posY = (int) (Math.random() * getHeight());
            // se crea el personaje caminador
            Personaje perCharola;
            perCharola = new Personaje(posX, posY,
                    Toolkit.getDefaultToolkit().getImage(urlImagenCharola));
            perCharola.setX(0 - perCharola.getAncho());
            perCharola.setY((int) (Math.random() * (getHeight() 
                    - perCharola.getAlto())));
            perCharola.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            lnkCharolas.add(perCharola);
        }
        //agrego keylistner

        addKeyListener(this);
    }

    /**
     * start
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>.<P>
     * En este metodo se crea e inicializa el hilo para la animacion este metodo
     * es llamado despues del init o cuando el usuario visita otra pagina y
     * luego regresa a la pagina en donde esta este <code>JFrame</code>
     *
     */
    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    @Override
    public void run() {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        }
    
}
