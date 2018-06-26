import java.util.Scanner;

import javax.swing.JFrame;

/**
 * Test Petita demostració de representacions possibles. s'avança donant enter a
 * la consola
 *
 */

public class Test {

	public static void main(String[] args) {
		Board t = new Board();
		Window f = new Window(t);

		Scanner sc = new Scanner(System.in);

		sc.nextLine();

		// cas 1: Hundir la flota

		System.out.println("cas 1: Colors - Enfonsar la flota");

		t.setActcolors(true);
		t.setActsprites(false);
		t.setActtext(false);
		int[] colors = { 0x0000FF, 0x00FF00, 0xFFFF00, 0xFF0000, 0xFF00FF, 0x00FFFF, 0x000000, 0xFFFFFF, 0xFF8000,
				0x7F00FF };
		t.setColors(colors);
		f.setActLabels(true); // les etiquetes i titol com que no tenen a
									// veure amb la matriu es tracten per la
									// finestra!
		String[] etiquetes = { "Dispars:10", "Portavions:1", "Cuirassats:0", "Creuers:0", "Submarins:1" };
		f.setLabels(etiquetes);
		f.setTitle("Enfonsar la flota");
		int[][] matriu = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 5, 3, 3, 0, 0, 5, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 5, 0, 6, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 6, 0, 5, 0, 0, 0 },
				{ 0, 0, 0, 0, 6, 0, 0, 0, 6, 0 }, { 0, 0, 5, 0, 6, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 2, 4, 1, 7, 8, 9 }, };

		t.draw(matriu);
		sc.nextLine();

		// cas 2: Buscaminas

		System.out.println("cas 2: Text - Buscamines");

		t.setActcolors(false);
		t.setColorbackground(0xb1adad);
		t.setActsprites(false);
		t.setActtext(true);
		String[] lletres = { "", "1", "2", "3", "4", "5", "6", "7", "8", "*" }; // què
																				// s'ha
																				// d'escriure
																				// en
																				// cada
																				// casella
																				// en
																				// base
																				// al
																				// nombre
		t.setText(lletres);
		int[] colorlletres = { 0x0000FF, 0x00FF00, 0xFFFF00, 0xFF0000, 0xFF00FF, 0x00FFFF, 0x521b98, 0xFFFFFF, 0xFF8000,
				0x7F00FF };
		t.setColortext(colorlletres);
		String[] etiquetes2 = { "Mines: 10", "Temps: 600" };
		f.setLabels(etiquetes2);
		f.setActLabels(true);
		f.setTitle("Cercamines");

		int[][] matriu2 = { { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 }, { 9, 9, 1, 1, 2, 9, 9, 5, 9, 9 },
				{ 9, 9, 9, 9, 3, 9, 9, 9, 9, 9 }, { 9, 9, 5, 9, 6, 9, 9, 9, 9, 9 }, { 9, 9, 9, 9, 6, 9, 5, 9, 9, 9 },
				{ 9, 9, 8, 9, 6, 9, 9, 9, 6, 9 }, { 9, 9, 5, 9, 6, 9, 9, 9, 9, 9 }, { 1, 1, 1, 9, 9, 9, 9, 9, 9, 9 },
				{ 0, 0, 1, 2, 9, 9, 9, 9, 9, 9 }, { 0, 0, 0, 1, 9, 4, 1, 7, 8, 9 }, };

		t.draw(matriu2);
		sc.nextLine();

		// cas 3: The Legend of Zelda

		System.out.println("cas 3: Imatges - The Legend of Zelda");
		t.setActcolors(false);
		t.setColorbackground(0xfed8a7);
		t.setActsprites(true);
		t.setActtext(false);
		t.setActborder(false);
		String[] imatges = { "", "Link1.gif", "rock2.png", "rock1.png", "octorok.gif", "octorok.gif", "octorok.gif",
				"octorok.gif", "octorok.gif", "octorok.gif" };
		t.setSprites(imatges);
		f.setActLabels(false);
		f.setTitle("The Legend of Zelda");

		int[][] matriu3 = { { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, { 0, 0, 0, 0, 0, 0, 0, 4, 0, 0 },
				{ 0, 0, 0, 3, 3, 0, 0, 0, 0, 0 }, { 0, 1, 0, 3, 3, 0, 4, 0, 0, 0 }, { 0, 0, 0, 0, 6, 0, 0, 0, 0, 0 },
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, };

		t.draw(matriu3);

		sc.nextLine();

		// cas 4: Exemple de background

		System.out.println("cas 4: Exemple de imgbackground");
		t.setActimgbackground(true);
		t.setImgbackground("b84.png");
		t.draw(matriu3);

		sc.nextLine();

		// cas 5: Exemple d'overdraw

		System.out.println("cas 5: Exemple d'overdraw");
		t.setActimgbackground(false);
		int[][][] matriuover = {{ { 0, 1 } },{ { 6, 0 }, { 0, 0 }, }};
		
		t.overdraw(matriuover);

		sc.nextLine();
		
		t.borraOverdraw();
		
		sc.nextLine();

		/// cas 6: Comprovacio mouse i teclat
		
		f.setActLabels(false);
		f.setDebugLabel(true);

		System.out.println("cas 6: Comprovacio mouse i teclat");

		System.out.println("pots provar de clicar la casella que vulguis o de apretar les tecles que vulguis i es mostraran (per mostrar la casella clicada també has d'apretar una tecla qualsevol."
				+ "Prem enter quan acabis.");
		
		sc.nextLine();
		System.out.println("l'ultima casella clickada es:  fila " + t.getMouseRow() + "   columna: " + t.getMouseCol());
		System.out.println("l'ultima tecla premuda es:   " +  f.getKeyPressed());
		System.out.println("Aquesta es la llista de totes les tecles que tenies apretades al premer l'enter:   "+f.getPressedKeys());

		/// cas 7: Freedraw

		System.out.println("cas 7: Freedraw");
		f.setActLabels(false);
		f.setDebugLabel(false);

		
		t.setActcolors(false);
		t.setColorbackground(0xfed8a7);
		t.setActoverdraw(false);
		t.setActsprites(true);
		t.setActtext(false);
		t.setActborder(false);
		t.setActfreedraw(true);
		String[] imatges2 = { "", "Link1.gif", "rock2.png", "rock1.png", "octorok.gif", "octorok.gif", "octorok.gif",
				"octorok.gif", "octorok.gif", "octorok.gif" };
		double[] freedrawx = { 1, 1, 1, 1, 1.5, 1, 1, 1, 1, 1 };
		double[] freedrawy = { 1, 1, 1, 1, 2, 1, 1, 1, 1, 1 };
		int[][][] matriuover2 = {{ { 0, 1 } },{ { 6, 0 }, { 0, 0 }, }};
		t.setFreedrawx(freedrawx);
		t.setFreedrawy(freedrawy);
		t.setSprites(imatges2);
		f.setActLabels(false);
		f.setTitle("The Legend of Zelda");

		
		t.overdraw(matriuover2);

		sc.nextLine();
		
		
		/// cas 7.1: Freeoverdraw

				System.out.println("cas 7: Freeoverdraw");
				f.setActLabels(false);
				f.setDebugLabel(false);

				
				t.setActcolors(false);
				t.setColorbackground(0xfed8a7);
				t.setActsprites(true);
				t.setActtext(false);
				t.setActborder(false);
				t.setActfreedraw(true);
				String[] imatges3 = { "", "Link1.gif", "rock2.png", "rock1.png", "octorok.gif", "octorok.gif", "octorok.gif",
						"octorok.gif", "octorok.gif", "octorok.gif" };
				double[] freedrawx2 = { 1, 1, 1, 1, 1.5, 1, 1, 1, 1, 1 };
				double[] freedrawy2 = { 1, 1, 1, 1, 2, 1, 1, 1, 1, 1 };
				t.setFreedrawx(freedrawx2);
				t.setFreedrawy(freedrawy2);
				t.setSprites(imatges3);
				f.setActLabels(false);
				f.setTitle("The Legend of Zelda");

				int[][] matriu6 = { { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, { 0, 0, 0, 0, 0, 0, 0, 4, 0, 0 },
						{ 0, 0, 0, 3, 3, 0, 0, 0, 0, 0 }, { 0, 1, 0, 3, 3, 0, 4, 0, 0, 0 }, { 0, 0, 0, 0, 6, 0, 0, 0, 0, 0 },
						{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, };

				t.draw(matriu6);

				sc.nextLine();

		/// cas 8: Doble finestra

		System.out.println("cas 8: Doble finestra");
		Board t2 = new Board();
		Window f2 = new Window(t, t2);
		t.setActcolors(true);
		t.setActsprites(false);
		t.setActtext(false);
		t.setActoverdraw(false);
		int[] colors2 = { 0x0000FF, 0x00FF00, 0xFFFF00, 0xFF0000, 0xFF00FF, 0x00FFFF, 0x000000, 0xFFFFFF, 0xFF8000,
				0x7F00FF };
		t.setColors(colors2);
		f2.setActLabels(true); // les etiquetes i titol com que no tenen a
									// veure amb la matriu es tracten per la
									// finestra!
		String[] etiquetes3 = { "Dispars:10", "Portavions:1", "Cuirassats:0", "Creuers:0", "Submarins:1" };
		f2.setLabels(etiquetes3);
		f2.setTitle("Enfonsar la flota");
		t2.setActcolors(true);
		t2.setColors(colors2);

		int[][] matriu41 = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 5, 3, 3, 0, 0, 5, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 5, 0, 6, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 6, 0, 5, 0, 0, 0 },
				{ 0, 0, 0, 0, 6, 0, 0, 0, 6, 0 }, { 0, 0, 5, 0, 6, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 2, 4, 1, 7, 8, 9 }, };

		int[][] matriu42 = { { 0, 0, 0, 5, 0, 5, 0, 0, 0, 0 }, { 0, 0, 5, 3, 3, 0, 0, 5, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 5, 0, 6, 0, 0, 0, 0, 0 }, { 0, 0, 5, 0, 6, 0, 5, 0, 0, 0 },
				{ 0, 5, 0, 0, 6, 0, 0, 5, 6, 0 }, { 0, 0, 5, 0, 6, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 2, 4, 1, 7, 8, 9 }, };
		t.draw(matriu41);

		t2.draw(matriu42);

	}

}