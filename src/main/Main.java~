package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;




import javax.swing.JFrame;
import javax.swing.JPanel;

import files.ImageReader;
import objects.Neuron;
import objects.Point;
import objects.Status;



public class Main extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static Main panel;
	private static Status status;
	
	private static int netz_g = 200;
	public static int getNetzG () {return netz_g;}
	private static int ill_counter = 0;

	public static ArrayList<Neuron> neuron = null;
	private static BufferedImage img_neuron;
	
	private static Color c_normal = new Color(0xADBBFF);
	private static Color c_krank = new Color(0xFF07A0);
	
	private static Color c_dop_max = new Color(0x545454);
	private static Color c_dop_min = new Color(0xC6C6C6);
	private static double f_dop = 1.0;
	
    private static File logfile = new File ("log");
    
	public static PrintWriter writer = null;
	
	private static int[] a = new int[netz_g / 100]; //stores neurons to use as input
	private static int[] a_log = new int[2 * netz_g / 100]; //stores neurons to use as log
	
	//private java.nio.file.Path log = Paths.get("activity_log");
	
	
	private static double p_conn = 0.0004; //0,0015
	
	public static double getConnProb () {
		return p_conn;
	}
	
	public static long get_timestep () {
		return step_millis;
	}
	
	private static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static long timestamp;
	private static long timestamp_log;
	private static long step_millis = 30;
	int c = 0;
	private Neuron input_neuron = new Neuron(new Point(0, 0.5));
	
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
		status = new Status();
		status.setLayout(new GridLayout(5,1));
		
		frame.addKeyListener(new KeyListener () {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				System.out.println(arg0.getKeyCode());
				if (arg0.getKeyCode() == KeyEvent.VK_PLUS) {
					//setUpNeuronen();
					Neuron.set_selbsthemmung(Neuron.get_selbsthemmung() + 10);
					System.out.println("Aktuelle Selbsthemmung aller Neuronen ist bei " + Neuron.get_selbsthemmung());
					writer.println("Aktuelle Selbsthemmung aller Neuronen ist bei " + Neuron.get_selbsthemmung());
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_MINUS) {
					//setUpNeuronen();
					Neuron.set_selbsthemmung(Neuron.get_selbsthemmung() - 10);
					System.out.println("Aktuelle Selbsthemmung aller Neuronen ist bei " + Neuron.get_selbsthemmung());
					writer.println("Aktuelle Selbsthemmung aller Neuronen ist bei " + Neuron.get_selbsthemmung());
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
					setUpNeuronen();
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		});
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getContentPane().add(status, BorderLayout.EAST);

		frame.setSize((int)(d.width*0.8), (int)(d.height*0.8));
		frame.setLocation((d.width-frame.getWidth())/2, (d.height-frame.getHeight())/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void paint (Graphics g) {
		status.refresh();

		super.paintComponent(g);
		g.setColor(mixColor(c_dop_max, c_dop_min, f_dop));
		g.fillRect(0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
		
		if (neuron!=null){// && img_neuron!=null) {
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
				//g.drawImage(img_neuron, (int)(spacer_x+w*neuron.get(i).pos.x), (int)(spacer_y+h*neuron.get(i).pos.y), b, b, null);
			}
		}
		
	
		for(int i = 0; i < netz_g / 100; i++) //add noise to input neurons
		{
			neuron.get(Main.a[i]).addPing(new Double[]{10.0, 0.0}, neuron.get(Main.a[i]));
			
		}
		
		c = 0;
		
		Neuron.store_gleichzeitigsFeuern(); //speichere gleichzeitg feuender Neuronen
		 
		/* Logging */
		if(System.currentTimeMillis() > (timestamp_log + (long)Neuron.get_activityTime()*(long)1000 - (long)500) && System.currentTimeMillis() < (timestamp_log + (long)Neuron.get_activityTime()*(long)1000 + (long)500))
		{
			int n1 = 0;
			for(int i = 0; i < Neuron.get_gleichzeitgesFeuern().size(); i++)
			{
				n1 += Neuron.get_gleichzeitgesFeuern().get(i);
						
			}
			
			int n = 0;
			for(int i = 0; i < netz_g; i++)
			{
				if(neuron.get(i).getGes() < 0.2)
				{
					n++;
				}	
			}
			ill_counter = n;
			n = 0;
			
			
			String tmp = "";
			for (int i = 0; i < netz_g/100; i++)
			{
				tmp = tmp + "Neuron#" + Integer.toString(a_log[i]) + " " + Double.toString(neuron.get(a_log[i]).calc_activity()) +  "   ";
			}
			// calc_netActivity - Anzahl der Male, die jedes Neuron durschnittlich in den letzten 10s gefeuert hat
			// n1/get_gleichzeitgesFeuern - durschnittliche Anzahl an Neuronen, die zu einem Zeitpunkt gleichzeitg feuern
			writer.println(tmp + Double.toString(calc_netActivity()) + " " + (double)n1/(double)Neuron.get_gleichzeitgesFeuern().size() + "   " + ill_counter);
			writer.flush();
			Neuron.reset_gleichzeitgesFeuern();
			timestamp_log = System.currentTimeMillis();
		}
		
		
		
		
		
		while (timestamp>System.currentTimeMillis());
		timestamp += step_millis;
		repaint();
	}
	
	public static void setUpNeuronen () {
		neuron = new ArrayList<>();
		
		for (int i=0; i<netz_g; i++) {
			neuron.add(new Neuron(new Point(0.85 * Math.random(), Math.random())));
		}
		Neuron.set_up(neuron);
	
	}
	
	public static int get_illNeurons()
	{
		return ill_counter;
	}
	
	private static void invoke_clearing_of_activity () {
		for (int i=0; i<neuron.size(); i++) {
			
			if(neuron.get(i).getGes() == 0 && neuron.get(i).get_n() < Neuron.get_critical_fire_rate()) //falls Neuron krank war, aber dieses Mal unter get_critical_fire_rate lag, soll es gesund werden
			{
				neuron.get(i).setGes(1.0);
			}
			
			neuron.get(i).set_n(0);
			
		}
		
	}
	
	private static double calc_netActivity()
	{
		double activity_mean = 0.0;
		
		for(int i = 0; i < netz_g; i++)
		{
			activity_mean += neuron.get(i).calc_activity();
		}
		
		activity_mean = activity_mean / netz_g;
		Main.invoke_clearing_of_activity();
		return activity_mean;
	}
	
	public static void main (String[] args) {
		
		//img_neuron = ImageReader.read();
		try {
         		   writer = new PrintWriter(logfile);
       		 }
		catch (FileNotFoundException fe){
         	   	fe.printStackTrace();
       		 }
		
		for(int i = 0; i < Main.netz_g / 100; i++)
		{
			a[i] = (int)(Math.random() * Main.netz_g);
			a_log[i] = (int)(Math.random() * Main.netz_g);
		}
		
		setUpNeuronen();
		timestamp = System.currentTimeMillis();
		timestamp_log = timestamp;
		initFrame();
		
	}
}

