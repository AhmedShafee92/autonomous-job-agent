package controller;

import model.AutomationModel;
import view.AutomationView;

import javax.swing.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AutomationController {
    private final AutomationModel model;
    private final AutomationView view;

    // TODO : In the future (Need three status (default start search, stop searching, restart the searching) )
    // For now the only one that will work in start running the schedule system. 
    public AutomationController(AutomationModel model, AutomationView view) 
    {
        this.model = model;
        this.view = view;

        initController();
    }

    private void initController() 
    {
        view.automatedSearchButton.addActionListener(e -> triggerAutomatedSearch());
     // view.manualSearchButton.addActionListener(e -> setManualSearch());
    }

    private void triggerAutomatedSearch() 
    {
        model.setSearchMode("automated");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() 
            {
                try {
	                	// TODO : should change the URL to new that suitable with the server side . 
	                    URL url = new URL("http://localhost:3000/api/scheduler/start");
	                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                    conn.setRequestMethod("POST");
	                    conn.setRequestProperty("Content-Type", "application/json");
	                    conn.setDoOutput(true);
	
	                    // Example JSON body
	                    String jsonInput = "{\"action\":\"start_schedule\"}";
	                    try (OutputStream os = conn.getOutputStream()) {
	                        os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
	                    }
	
	                    int responseCode = conn.getResponseCode();
	                    if (responseCode == 200) {
	                        JOptionPane.showMessageDialog(view,
	                            "✅ Automated search started successfully!",
	                            "Success",
	                            JOptionPane.INFORMATION_MESSAGE);
	                    } else {
	                        JOptionPane.showMessageDialog(view,
	                            "⚠️ Server responded with code: " + responseCode,
	                            "Error",
	                            JOptionPane.ERROR_MESSAGE);
	                    }

                    conn.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view,
                        "❌ Failed to connect to server:\n" + ex.getMessage(),
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }
        };
        worker.execute();
    }
    
    // This function unused in the future should change to one that will doing manual search . 
    @SuppressWarnings("unused")
	private void setManualSearch() 
    {
        model.setSearchMode("manual");
        JOptionPane.showMessageDialog(view, 
            "Manual Search Selected ✅", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
}
