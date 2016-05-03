package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import files.ImageReader;
import objects.Neuron;
import objects.Point;



public class Main extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static Main panel;
	
	private static int netz_g = 500;
	private static int n_input_neurons;
	public static int getNetzG () {return netz_g;}
	public static ArrayList<Neuron> neuron = null;
	private static BufferedImage img_neuron;
	
	private static Color c_normal = new Color(0xADBBFF);
	private static Color c_krank = new Color(0xFF07A0);
	
	private static Color c_dop_max = new Color(0x545454);
	private static Color c_dop_min = new Color(0xC6C6C6);
	private static double f_dop = 1;
	
	private static double p_conn = 0.0001;
	
	public static double getConnProb () {
		return p_conn;
	}
	
	
	private static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static long timestamp;
	private long step_millis = 10;
	int c = 0;
	
	private static double sq (double x) {
		return x*x;
	}
	
	private static int range (int z, int min, int max) {
		if (z>max) return max;
		if (z<min) return min;
		return z;
	}
	
	private static Color mixColor (Color c1, Color c2, double f) {
		int r = (int) sq(Math.sqrt(c1.getRed())*f + Math.sqrt(c2.getRed()*(1-f)));
		int g = (int) sq(Math.sqrt(c1.getGreen())*f + Math.sqrt(c2.getGreen()*(1-f)));
		int b = (int) sq(Math.sqrt(c1.getBlue())*f + Math.sqrt(c2.getBlue()*(1-f)));
		r = range(r,0,255);
		g = range(g,0,255);
		b = range(b,0,255);
		return new Color (r, g, b);
	}
	
	private static void initFrame () {
		frame = new JFrame();
		panel = new Main();
		
		frame.addKeyListener(new KeyListener () {
			@Override
			public void keyPressed(KeyEvent arg0) {
				setUpNeuronen();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				//System.out.println(KeyEvent.);
				//if (KeyEvent == KeyEvent.VK_SPACE) {
//			setUpNeuronen();
				//}
			}		

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		});
		
		frame.getContentPane().add(panel);
		frame.setSize((int)(d.width*0.8), (int)(d.height*0.8));
		frame.setLocation((d.width-frame.getWidth())/2, (d.height-frame.getHeight())/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void paint (Graphics g) {
		super.paintComponent(g);
		g.setColor(mixColor(c_dop_max, c_dop_min, f_dop));
		g.fillRect(0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
		
		if (neuron!=null && img_neuron!=null) {
			int h = (int) (0.9*frame.getContentPane().getHeight());
			int w = (int) (0.9*frame.getContentPane().getWidth());
			int spacer_x = (frame.getContentPane().getWidth()-w)/2;
			int spacer_y = (frame.getContentPane().getHeight()-h)/2;
			int b = (int) (frame.getContentPane().getWidth()*0.9/100);
			
			int x1,y1,x2,y2;
			g.setColor(new Color(0x00AA00));
			for (int i=0; i<neuron.size(); i++) {
				for (int j=0; j<neuron.get(i).getZielknoten().size(); j++) {
					x1 = (int) (spacer_x+w*neuron.get(i).pos.x + b/2);
					y1 = (int) (spacer_y+h*neuron.get(i).pos.y + b/2);
					x2 = (int) (spacer_x+w*neuron.get(i).getZielknoten().get(j).pos.x + b/2);
					y2 = (int) (spacer_y+h*neuron.get(i).getZielknoten().get(j).pos.y + b/2);
					g.drawLine(x1, y1, x2, y2);
				}
			}
			g.setColor(new Color(0x00FF00));//aktive Verbindungen
			for (int i=0; i<neuron.size(); i++) {
				for (int j=0; j<neuron.get(i).getQuelle().size(); j++) {
					x1 = (int) (spacer_x+w*neuron.get(i).pos.x + b/2);
					y1 = (int) (spacer_y+h*neuron.get(i).pos.y + b/2);
					x2 = (int) (spacer_x+w*neuron.get(i).getQuelle().get(j).pos.x + b/2);
					y2 = (int) (spacer_y+h*neuron.get(i).getQuelle().get(j).pos.y + b/2);
					g.drawLine(x1, y1, x2, y2);
				}
			}
			
			for (int i=0; i<neuron.size(); i++) neuron.get(i).calc();
			for (int i=0; i<neuron.size(); i++) {
				g.setColor(mixColor(c_normal, c_krank, neuron.get(i).getGes()));
				neuron.get(i).act();
				if (neuron.get(i).getActive()) {g.setColor(Color.WHITE);c++;}
				g.fillRect((int)(spacer_x+w*neuron.get(i).pos.x), (int)(spacer_y+h*neuron.get(i).pos.y), b, b);
				//g.drawImage(img_neuron, (int)(spacer_x+w*neuron[i].pos.x), (int)(spacer_y+h*neuron[i].pos.y), b, b, null);
			}
		}
		
		for (int i=0; i<n_input_neurons; i++) {
			neuron.get(i).addPing(new Double[]{10.0, 0.0}, neuron.get(i));
		}
		c = 0;
		
		while (timestamp>System.currentTimeMillis());
		timestamp += step_millis;
		repaint();
	}
	
	private static void setUpNeuronen () {
		neuron = new ArrayList<>();
		for (int i=0; i<netz_g; i++) {
			neuron.add(new Neuron(new Point(Math.random(), Math.random())));
		}
		Neuron.set_up(neuron);
		for (int i=0; i<n_input_neurons; i++) {
			neuron.get(i).setGes(0);
		}
	}
	
	public static void main (String[] args) {
		n_input_neurons = netz_g/100;
		setUpNeuronen();
		img_neuron = ImageReader.read();
		timestamp = System.currentTimeMillis();
		initFrame();
	}
}
