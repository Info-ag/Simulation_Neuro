package objects;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.*;

import main.Main;

public class Status extends JPanel implements ActionListener{
	private static JTextField text;
	private static JSpinner selbsthemmung;
	private static JButton reset;
	
	public Status(){		
	text = new JTextField();
	selbsthemmung = new JSpinner(new SpinnerNumberModel(100, 0, 150, 5));
	reset = new JButton();
	
	reset.setText("Reset Net");
	reset.setActionCommand("reset_net");
	reset.addActionListener(this);
	
	
	selbsthemmung.addChangeListener(new ChangeListener(){
	public void stateChanged(ChangeEvent e)
	{
		Neuron.set_selbsthemmung((int)selbsthemmung.getValue());
		Main.writer.println("Selbsthemmung: " + (int)selbsthemmung.getValue());
	};
	}
			
	);
	
	
	this.add(text);
	this.add(selbsthemmung);
	this.add(reset);
	text.setText("Es sind " + Main.get_illNeurons() + " Neurone " + "(" + (double)Main.get_illNeurons()/(double)Main.getNetzG() + "%)" + " krank.");
	}	
	
	
	public void refresh()
	{
		text.setText("Es sind " + Main.get_illNeurons() + " Neurone " + "(" + Math.round((double)100*Main.get_illNeurons()/(double)Main.getNetzG()) + "%)" + " krank.");
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if("reset_net".equals(e.getActionCommand())) 
	    {
	            Main.setUpNeuronen();            
	    } 
		
	}
	
};
