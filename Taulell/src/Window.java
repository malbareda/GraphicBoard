import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 * Window representation of the GUI interface. Manages every part of the GUI which is not the matrix proper.
 * @author Marc Albareda
 *
 */
public class Window extends JFrame {

	/*
	 * TODO Posar opció de musica i opció de carregarli un jPanel de pantalla d'intro?
	 */

	/*
	 * TODO No se si fer que Board sigui privat i l'usuari només interactui amb
	 * Finestra o tenir-ho separat. Hm...
	 */

	private Board board;
	private Board board2;
	private boolean secondBoard=false;
	private boolean actLabels = false;
	private boolean debugLabel = false;  //debug Label, with useful information for debugging your game
	private String[] labels = { "" };
	// Several labels allowed
	private JPanel labelpanel = new JPanel(new GridLayout(0, 1, 5, 5));
	private JPanel tpanel = new JPanel(new GridLayout(0, 2, 5, 5));
	private char keyPressed;  //Last keyboard char pressed. 
	private char currentKeyPressed; //Last keyboard char pressed. Will reseat every time is checked. Deprecated?
	private Set<Character> pressedKeys = new HashSet<Character>(); //set with every key CURRENTLY pressed.

	public Window(Board t) {
		board = t;
		init();
	}
	
	public Window(Board t, Board t2) {
		board = t;
		board2 = t2;
		secondBoard = true;
		init();
	}

	/**
	 * Initializes the window
	 */
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (secondBoard) {
			tpanel.add(board);
			tpanel.add(board2);
			add(tpanel);
		}else add(board);

		if (actLabels) {
			for (String s : labels) {
				JLabel etiq = new JLabel(s);
				labelpanel.add(etiq);
			}
		}
		add(labelpanel, BorderLayout.LINE_END);
		labelpanel.setBorder(new EmptyBorder(10,10,10,10));

		setSize(700, 400);  //change this if you want your window to be bigger.
		setLocation(100, 100);
		setVisible(true);
		board.addComponentListener(board.cl);
		

	    addKeyListener(ka);

	}

	/**
	 * renews the window in case the label changes.
	 */
	private void renew() {
		labelpanel.removeAll();
		if (actLabels) {
			for (String s : labels) {
				JLabel etiq = new JLabel(s);
				labelpanel.add(etiq);
			}

		}
		if (debugLabel){
			JLabel debug = new JLabel("Ultima tecla premuda "+keyPressed+" Tecla actual"+pressedKeys);
			labelpanel.add(debug);
			JLabel debugm = new JLabel("Ultima casella premuda amb el ratolí "+board.getMouseRow()+", "+board.getMouseCol());
			labelpanel.add(debugm);
				
		}
		labelpanel.repaint();
		labelpanel.revalidate();
		// every cell in board is reset as a side effect. 

	}

	public boolean isActLabels() {
		return actLabels;
	}

	public void setActLabels(boolean actetiquetes) {
		this.actLabels = actetiquetes;
		if (!actetiquetes)
			renew();
	}

	public boolean isDebugLabel() {
		return debugLabel;
	}

	public void setDebugLabel(boolean etiquetadebug) {
		this.debugLabel = etiquetadebug;
		renew();
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] etiquetes) {
		this.labels = etiquetes;
		renew();
	}
	
	public char getKeyPressed() {
		return keyPressed;
	}

	public char getCurrentKeyPressed() {
		char tmp=currentKeyPressed;
		currentKeyPressed='0';
		return tmp;
	}
	
	public Set<Character> getPressedKeys() {
		return pressedKeys;
	}

	
	//Integracio del teclat. Ara pot detectar pulsacions de tecles sense necessitat de l'intro i l'escanner.
	/**
	 * Keyboard integration. Detects pressed keys and returns them as a set.
	 */
	private KeyAdapter ka = new KeyAdapter() {
	      @Override
		  public void keyPressed(KeyEvent e)
		  {
	        pressedKeys.add(e.getKeyChar());
		    char char1 = e.getKeyChar();
		    keyPressed = char1;
		    currentKeyPressed = char1;
		    if(debugLabel) renew();
		  }
		  @Override
		  public synchronized void keyReleased(KeyEvent e) {
		        pressedKeys.remove(e.getKeyChar());
		        currentKeyPressed = '0';
		        if(debugLabel) renew();
		  }

	};

}

