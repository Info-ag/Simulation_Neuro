package objects;

import java.util.ArrayList;

import main.Main;

public class Neuron {
	private ArrayList<Neuron> zielknoten = new ArrayList<>();
	private ArrayList<Double> wichtung = new ArrayList<>();
	private ArrayList<Double> dist = new ArrayList<>();
	private final double perc_per_step = 0.05;
	private final double ruhepot = -70;//mV
	private double ladung = this.ruhepot;
	private final double schwelle_normal = -30;
	private double schwellenwert = this.schwelle_normal;
	private final double feuer_wert = 20;
	private ArrayList<Double[]> signale = new ArrayList<>();//[0] - Ladung  |  [1] - Counter
	private ArrayList<Neuron> quellen = new ArrayList<>();
	private boolean active = false;
	private final double selbsthemmung = 50;
	
	private boolean apoptose = false;
	public boolean getApoptose () {return this.apoptose;}
	
	public Point pos;
	
	private double f_ges = 1;//1 - normal  |  0 - krank
	
	public Neuron (Point pos) {
		this.pos = pos;
	}
	
	public ArrayList<Double> getDists () {
		return this.dist;
	}
	
	public ArrayList<Neuron> getQuelle () {
		return this.quellen;
	}
	
	public ArrayList<Neuron> getZielknoten () {
		return this.zielknoten;
	}
	public ArrayList<Double[]> getSignals () {
		return this.signale;
	}
	
	public void setZielknoten (ArrayList<Neuron> zielknoten, ArrayList<Double> dist) {
		this.zielknoten = zielknoten;
		this.dist = dist;
		this.wichtung = new ArrayList<>();
		for (int i=0; i<this.zielknoten.size(); i++) {
			this.wichtung.add(Math.random()*2-1);
		}
	}
	
	public void addZielknoten (Neuron zielknoten, double dist) {
		this.zielknoten.add(zielknoten);
		this.dist.add(dist);
		this.wichtung.add(Math.random()*2-1);//TODO: Gewichte der Verbindungen anpassen
	}
	
	public static void set_up (ArrayList<Neuron> neuron) {
		double sigma = Main.getConnProb()*Main.getNetzG();
		double d = 0;
		
		for(int i=0; i<Main.getNetzG(); i++) {
			for(int j=0; j<Main.getNetzG(); j++) {
				d = Math.sqrt(sq(neuron.get(j).pos.x - neuron.get(i).pos.x) + sq(neuron.get(j).pos.y - neuron.get(i).pos.y));
				if (d!=0) {//verhindert, dass neuron mit sich selbst verbunden ist
					double prob_connect = (Math.pow(Math.E, -sq(d)/(sq(sigma)*2)) / Math.sqrt(Math.PI*sq(sigma)*2)) + Main.getConnProb();
					double prop_vgl = Math.random();
					if(prop_vgl < prob_connect) //soll verbinden
					{
						neuron.get(i).addZielknoten(neuron.get(j), d);
					}
				}
			}
		}
	}
	
	private static double sq (double x) {
		return x*x;
	}
	
	
	private void addLadung (double d) {
		this.ladung += d;
	}
	
	public double getGes () {
		return this.f_ges;
	}

	public void setGes(double ges) {
		if (ges<0) this.f_ges = 0;
		else if (ges>1) this.f_ges = 1;
		else this.f_ges = ges;
	}
	
	public boolean addPing (Double[] d, Neuron neuron) {
		if (d.length==2) {
			this.signale.add(d);
			this.quellen.add(neuron);
			return true;
		}
		return false;
	}
	
	private void fire () {
		this.active = true;
		this.ladung = this.ruhepot;
		for (int i=0; i<zielknoten.size(); i++) {
			this.zielknoten.get(i).addPing(new Double[]{feuer_wert*this.wichtung.get(i), this.dist.get(i)/perc_per_step}, this);
		}
		this.schwellenwert += this.selbsthemmung;//Selbsthemmung
	}
	
	public boolean getActive () {
		return this.active;
	}
	
	public void calc () {
		Double[] tmp;
		for (int i=0; i<this.signale.size(); i++) {
			tmp = this.signale.get(i);
			tmp[1]--;
			if (tmp[1]<=0) {
				this.addLadung(tmp[0]);
				this.signale.remove(i);
				this.quellen.remove(i);
				i--;
			}
		}
	}
	
	public void act () {
		this.active = false;
		
		if (this.ladung < 3*this.ruhepot) {this.ladung = 3*this.ruhepot;}
		if (this.ladung > this.schwellenwert) {
			this.fire();
		}
		this.schwellenwert -= this.schwelle_normal;
		this.schwellenwert *= 0.93;
		this.schwellenwert += this.schwelle_normal;
	}
}
