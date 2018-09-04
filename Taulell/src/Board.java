import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Taulellitzador Converteix una matriu de digits positius donada en un taulell
 * a una representació gràfica. Creat per Marc Albareda
 *
 */

/**
 * Graphical User Interface that converts a given matrix into a graphical board.
 * This class also provides methods to manipulate that board and set how the
 * graphics are displayed. This interface can work with either colors, text, or
 * defined image sprites. Other options as displaying backgrounds, overdraws, or
 * changing the size of individual cells are provided.
 * 
 * @author Marc Albareda
 *
 */
public class Board extends JPanel {

	Square[][] squares;
	Square[][] oversquares;
	private int padding = 20; // padding is the margin that the board will have relative to the window
	private int rows = -1;
	private int cols = -1;
	private int[][] matrix;
	private int[][] overdraw;
	private int[][][] succesiveOverdraw; // Consider it as an array of matrixes that will be overdrawn over the main
											// matrix, used if there are different layers to be printed.
	private boolean init = false;
	private boolean change = false;
	private boolean actcolors = false; // acts are booleans that activate each utility provided. If it's true it will
										// activate it, if false it won't.
	private boolean actborder = false;
	private int borderColor = 0x8cc8a0;
	private int colorbackground = 0x0000ff;
	private boolean actfreedraw = false;
	private double[] freedrawx;
	private double[] freedrawy;
	private boolean actoverdraw = false;
	private boolean actsuccessiveOverdraw = false;
	private boolean actimgbackground = false;
	private String imgbackground;
	private int[] colors = { 0x0000FF, 0x00FF00, 0xFFFF00, 0xFF0000, 0xFF00FF, 0x00FFFF, 0x000000, 0xFFFFFF, 0xFF8000,
			0x7F00FF };// RGB color pallete for each integer. In the array each position relates to a
						// number in the matrix (first position will be the color of number 0 in the
						// matrix, etc.)
	private boolean acttext = false;
	private String[] text = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "*" }; // text written in each position
																					// relative to the number in the
																					// matrix
	private int[] colortext = { 0x0000FF, 0x00FF00, 0xFFFF00, 0xFF0000, 0xFF00FF, 0x00FFFF, 0x000000, 0xFFFFFF,
			0xFF8000, 0x7F00FF }; // RBG color pallete for the color of the text. Only if text is enabled.
	private boolean actsprites = false;
	private String[] sprites = { "Link1.gif", "Iron_Axe.png", "Iron_Lance.png", "Iron_Sword.png", "Lightning.png",
			"Vulnerary.png", "Vulnerary.png", "Vulnerary.png", "Vulnerary.png", "Vulnerary.png" }; // path of the image
																									// displayed in each
																									// cell relative to
																									// the number in the
																									// matrix
	private Font font = new Font("SansSerif", Font.PLAIN, 22);

	private int mouseRow = -1; // row of last mouse click
	private int mouseCol = -1; // col of last mouse click
	private int currentMouseRow = -1; // row of last mouse click. Will reset after each check.
	private int currentMouseCol = -1; // col of last mouse click. Will reset after each check.

	public Board() {
		addMouseListener(ml);

	}

	// Drawing methods.

	/**
	 * Inherited method from AWT, paints the matrix on the board.
	 */
	protected void paintComponent(Graphics g) {

		if (init) { // nothing will be shown until the first draw call

			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // SERA
																										// UN
																										// JOC
																										// DE
																										// MERDA,
																										// PERO
																										// SERA
																										// MERDA
																										// HD.
			if (change || squares == null) {
				initSquares();
			}
			if (actimgbackground) {
				BufferedImage img = null;
				try {
					img = ImageIO.read(new File(imgbackground));
				} catch (IOException e) {
				}
				g2.drawImage(img, (int) 0, (int) 0, (int) (getWidth()), (int) (getHeight()), 0, 0, img.getWidth(),
						img.getHeight(), null);
				/// This starts drawing over the corner. Change the zero values for centered
				/// image.

			}
			// Draw every cell individually
			g2.setPaint(Color.blue);
			g2.setFont(font);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					squares[i][j].draw(g2, matrix[i][j], actcolors, colors, actborder, borderColor, colorbackground,
							acttext, text, colortext, actsprites, sprites, actimgbackground, actfreedraw, freedrawx,
							freedrawy);
				}
			}
			if (actoverdraw) {
				initoverSquares(overdraw.length, overdraw[0].length);
				for (int i = 0; i < overdraw.length; i++) {
					for (int j = 0; j < overdraw[0].length; j++) {
						oversquares[i][j].overdraw(g2, overdraw[i][j], sprites, actfreedraw, freedrawx, freedrawy);
					}
				}
			}
			if (actsuccessiveOverdraw) {
				for (int k = 0; k < succesiveOverdraw.length; k++) {
					initoverSquares(succesiveOverdraw[k].length, succesiveOverdraw[k][0].length);
					for (int i = 0; i < succesiveOverdraw[k].length; i++) {
						for (int j = 0; j < succesiveOverdraw[k][0].length; j++) {
							oversquares[i][j].overdraw(g2, succesiveOverdraw[k][i][j], sprites, actfreedraw, freedrawx,
									freedrawy);
						}
					}
				}

			}

		}

	}

	/**
	 * Deletes anything overdrawn on the board.
	 */
	public void delOverdraw() {
		int[][][] delete = { { { 0 } } };
		this.overdraw(delete);
	}

	/**
	 * Initializes the board the first time used
	 */
	private void initSquares() {

		squares = new Square[rows][cols];
		int w = getWidth();
		int h = getHeight();
		double xInc = (double) (w - 2 * padding) / cols; // Each cell will have the same size. Total width/number of
															// squares
		double yInc = (double) (h - 2 * padding) / rows;
		for (int i = 0; i < rows; i++) {
			double y = padding + i * yInc;
			for (int j = 0; j < cols; j++) {
				double x = padding + j * xInc;
				Rectangle2D.Double r = new Rectangle2D.Double(x, y, xInc, yInc);
				squares[i][j] = new Square(i, j, r, x, y, xInc, yInc, this);
			}
		}
	}

	/**
	 * initializes an overdraw matrix if required
	 * 
	 * @param fil
	 *            number of rows
	 * @param col
	 *            number of cols
	 */
	private void initoverSquares(int fil, int col) {

		oversquares = new Square[fil][col];
		int w = getWidth();
		int h = getHeight();
		double xInc = (double) (w - 2 * padding) / col;
		double yInc = (double) (h - 2 * padding) / fil;
		for (int i = 0; i < fil; i++) {
			double y = padding + i * yInc;
			for (int j = 0; j < col; j++) {
				double x = padding + j * xInc;
				Rectangle2D.Double r = new Rectangle2D.Double(x, y, xInc, yInc);
				oversquares[i][j] = new Square(i, j, r, x, y, xInc, yInc, this);
			}
		}
	}

	/**
	 * Redraws board if window size is changed.
	 */
	ComponentListener cl = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			squares = null;
			repaint();
		}
	};

	/**
	 * Public draw method. This is the method called by the user and draws the
	 * matrix received
	 * 
	 * @param int
	 *            matrix to be drawn. It will draw as a board using the options
	 *            received
	 */
	public void draw(int[][] a) {
		this.matrix = a;
		if (rows != a.length || cols != a[0].length) { // if board size is changed on runtime...
			rows = a.length;
			cols = a[0].length;
			change = true;
		} else
			change = false;
		init = true;

		repaint();
	};

	public void draw(Integer[][] a) {
		int[][] b = new int[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				b[i][j] = a[i][j];
			}
		}

		draw(b);
	};

	public void overdraw(int[][] a) {
		this.actoverdraw = true;
		this.overdraw = a;
		repaint();

	};

	public void overdraw(int[][][] a) {
		this.actsuccessiveOverdraw = true;
		this.succesiveOverdraw = a;
		repaint();

	};

	/**
	 * Event handler every time mouse is clicked.
	 */
	private MouseListener ml = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();
			if (!isInGrid(p))
				return;
			double xInc = (double) (getWidth() - 2 * padding) / cols;
			double yInc = (double) (getHeight() - 2 * padding) / rows;
			int f = (int) ((p.y - padding) / yInc);
			int c = (int) ((p.x - padding) / xInc);
			mouseRow = f;
			mouseCol = c;
			currentMouseRow = f;
			currentMouseCol = c;
			/*
			 * Old method. squares[f][c].mouseClick(); boolean isSelected =
			 * squares[f][c].isSelected(); squares[f][c].setSelected(!isSelected);
			 * repaint();
			 */
		}
	};

	/**
	 * Check whether the clicked point is in the matrix grid.
	 */
	private boolean isInGrid(Point p) {
		Rectangle r = getBounds();
		r.grow(-padding, -padding);
		return r.contains(p);
	}

	/**
	 * Getters & Setters. AutoGenerated
	 */

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public int[][] getOverdraw() {
		return overdraw;
	}

	public void setOverdraw(int[][] overdraw) {
		this.overdraw = overdraw;
	}

	public int[][][] getSuccesiveOverdraw() {
		return succesiveOverdraw;
	}

	public void setSuccesiveOverdraw(int[][][] succesiveOverdraw) {
		this.succesiveOverdraw = succesiveOverdraw;
	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public boolean isActcolors() {
		return actcolors;
	}

	public void setActcolors(boolean actcolors) {
		this.actcolors = actcolors;
	}

	public boolean isActborder() {
		return actborder;
	}

	public void setActborder(boolean actborder) {
		this.actborder = actborder;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorder(int border) {
		this.borderColor = border;
	}

	public int getColorbackground() {
		return colorbackground;
	}

	public void setColorbackground(int colorbackground) {
		this.colorbackground = colorbackground;
	}

	public boolean isActfreedraw() {
		return actfreedraw;
	}

	public void setActfreedraw(boolean actfreedraw) {
		this.actfreedraw = actfreedraw;
	}

	public double[] getFreedrawx() {
		return freedrawx;
	}

	public void setFreedrawx(double[] freedrawx) {
		this.freedrawx = freedrawx;
	}

	public double[] getFreedrawy() {
		return freedrawy;
	}

	public void setFreedrawy(double[] freedrawy) {
		this.freedrawy = freedrawy;
	}

	public boolean isActoverdraw() {
		return actoverdraw;
	}

	public void setActoverdraw(boolean actoverdraw) {
		this.actoverdraw = actoverdraw;
	}

	public boolean isActsuccessiveOverdraw() {
		return actsuccessiveOverdraw;
	}

	public void setActsuccessiveOverdraw(boolean actsuccessiveOverdraw) {
		this.actsuccessiveOverdraw = actsuccessiveOverdraw;
	}

	public boolean isActimgbackground() {
		return actimgbackground;
	}

	public void setActimgbackground(boolean actimgbackground) {
		this.actimgbackground = actimgbackground;
	}

	public String getImgbackground() {
		return imgbackground;
	}

	public void setImgbackground(String imgbackground) {
		this.imgbackground = imgbackground;
	}

	public int[] getColors() {
		return colors;
	}

	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public boolean isActtext() {
		return acttext;
	}

	public void setActtext(boolean acttext) {
		this.acttext = acttext;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public int[] getColortext() {
		return colortext;
	}

	public void setColortext(int[] colortext) {
		this.colortext = colortext;
	}

	public boolean isActsprites() {
		return actsprites;
	}

	public void setActsprites(boolean actsprites) {
		this.actsprites = actsprites;
	}

	public String[] getSprites() {
		return sprites;
	}

	public void setSprites(String[] sprites) {
		this.sprites = sprites;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getMouseRow() {
		return mouseRow;
	}

	public int getMouseCol() {
		return mouseCol;
	}

	/**
	 * get the row of the cell last clicked. Returns -1 if no cell was clicked since
	 * last check.
	 */
	public int getCurrentMouseRow() {
		int tmp = currentMouseRow;
		currentMouseRow = -1;
		return tmp;

	}

	/**
	 * get the column of the cell last clicked. Returns -1 if no cell was clicked
	 * since last check.
	 */
	public int getCurrentMouseCol() {
		int tmp = currentMouseCol;
		currentMouseCol = -1;
		return tmp;
	}

	public MouseListener getMl() {
		return ml;
	}

	public void setMl(MouseListener ml) {
		this.ml = ml;
	}

}

/**
 * Class representing every cell on the Board. Not meant to be directly
 * interacted by the user.
 */
class Square {
	private final int row;
	private final int col;
	private int value;
	private final double x;
	private final double y;
	private final double xInc;
	private final double yInc;
	private Color border;

	Rectangle2D.Double rect;

	public Square(int f, int c, Rectangle2D.Double rect, double e, double d, double a, double b, Board taulell) {
		row = f;
		col = c;
		x = e;
		y = d;
		xInc = a;
		yInc = b;
		this.rect = rect;

	}

	/**
	 * Draw every cell on an overdraw matrix. Overdraw matrixes are those drawn over
	 * the main one. They are for sprites only.
	 */
	public void overdraw(Graphics2D g2, int value, String[] overimatges, boolean actfreedraw, double[] freedrawx,
			double[] freedrawy) {
		if (!(overimatges[value].equals(""))) { // An empty string represents no image (or transparency)
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File(overimatges[value]));
				if (actfreedraw) {
					g2.drawImage(img, (int) (x - (xInc * (freedrawx[value] - 1))),
							(int) (y - (yInc * (freedrawy[value] - 1))), (int) (x + xInc), (int) (y + yInc), 0, 0,
							img.getWidth(), img.getHeight(), null);
				} else {

					g2.drawImage(img, (int) x, (int) y, (int) (x + xInc), (int) (y + yInc), 0, 0, img.getWidth(),
							img.getHeight(), null);
				}
			} catch (IOException e) { // almost every error is due to the image not being included on the project
										// folder
				System.out.println("Error on image " + overimatges[value] + " value: " + value);
			}
		}
	}

	/**
	 * Draw every cell individually.
	 */
	public void draw(Graphics2D g2, int a, boolean actcolors, int[] colors, boolean actborde, int borde, int fons,
			boolean actlletres, String[] lletres, int[] colorlletres, boolean actimatges, String[] imatges,
			boolean actimgbackground, boolean actfreedraw, double[] freedrawx, double[] freedrawy) {
		value = a;
		if (actcolors) {
			Color inside = new Color(colors[value]);
			g2.setPaint(inside);

		} else {

			g2.setPaint(new Color(fons));

		}

		border = new Color(borde);

		if (!actimgbackground) {
			g2.fill(rect);
		}
		g2.setPaint(border);
		if (actborde) {
			g2.draw(rect);
		}

		if (actimatges) {
			if (!(imatges[value].equals(""))) { // An empty string represents no image (or transparency)
				BufferedImage img = null;
				try {
					img = ImageIO.read(new File(imatges[value]));

					if (actfreedraw) {
						g2.drawImage(img, (int) (x - (xInc * (freedrawx[value] - 1))),
								(int) (y - (yInc * (freedrawy[value] - 1))), (int) (x + xInc), (int) (y + yInc), 0, 0,
								img.getWidth(), img.getHeight(), null);
					} else {

						g2.drawImage(img, (int) x, (int) y, (int) (x + xInc), (int) (y + yInc), 0, 0, img.getWidth(),
								img.getHeight(), null);
						// /This starts drawing over the corner. Change the first two parameters for
						// centered image.
					}

				} catch (Exception e) {
					System.out.println("Error on image " + imatges[value] + " value: " + value);
					e.printStackTrace();
				}

			}
		}
		if (actlletres) {
			Color inside = new Color(colorlletres[value]);
			g2.setPaint(inside);
			int padding = 5;
			g2.drawString(lletres[value], (int) (x + xInc / 2), (int) (y + yInc - padding));
			// On drawString the starting point is not the upper left, but the bottom left
			// On bigger fonts that means that if centered will go over the top
			// modify the padding value until it is centered according to your square size }
		}

		// Old Mouse processing, before adding listeners
		/*
		 * public void mouseClick() { // System.out.println("SQUARE[row:" + fil +
		 * ", col:" + col + ", value:" // + value + "]"); t.setMousefil(fil);
		 * t.setMousecol(col); t.setActualMousefil(fil); t.setActualMousecol(col);
		 * 
		 * // si vols que les teves opcions vagin per ratolí, a partir d'aquesta //
		 * funció hauries de cridar una funcio estatica de la TEVA classe. També // pots
		 * consultar les variables mitjançant els getters de mosuefil y // mousecol }
		 */
	}
}