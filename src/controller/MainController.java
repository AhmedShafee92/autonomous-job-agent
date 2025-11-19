package controller;

import model.MainModel;
//import model.AutomationModel;
import view.MainView;
import view.AutomationView;
//import controller.AutomationController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController 
{
    private final MainModel model;
    private final MainView view;
    private final JFrame frame;

     
    public MainController(MainView view) 
    {
        this.model = new MainModel();
		this.view = view;		
		this.frame = new JFrame();
        initListeners(); 
    }

    public MainController(MainModel model, MainView view) 
    {
        this.model = model;
        this.view = view;

        frame = new JFrame("Insert Your Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        initListeners();
    }

    private void initListeners() 
    {
        view.attachCVButton.addActionListener(e -> model.attachCV());
        view.startSearchingButton.addActionListener(e -> {	
	        // Here we show the new frame (automation search || manual search ) .
	        showAutomationView();
 
        });
        
        view.analysePersonalDataButton.addActionListener(e -> {
            if (!model.isCVValid()) {
                JOptionPane.showMessageDialog(view, "CV file missing or empty.");
                return;
            }

            try {
				model.analyseData(frame);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });

        view.viewPersonalDataButton.addActionListener(e ->{
            JOptionPane.showMessageDialog(frame, "Soon Available!");
        
        });
    }
    
    private void showAutomationView() 
    {		
		  AutomationView automationView = new AutomationView(); 
		  frame.setContentPane(automationView); 
		  frame.revalidate(); 
		  frame.repaint();
		 
    }
      
}
