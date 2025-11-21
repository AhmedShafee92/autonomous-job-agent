package model;

import conncet.server.analyse.file.ConnectGoogleAPIServer;
import controller.AnalyseController;
import store.user.data.StoreUserDataLocal;
import javax.swing.*;
import java.io.IOException;

public class MainModel 
{

    public void attachCV() 
    {
        StoreUserDataLocal.storeCVUserLocal();
    }

    public boolean isCVValid() 
    {
        return StoreUserDataLocal.CheckCVFileExist() && !StoreUserDataLocal.isWordFileCVEmpty();
    }

    public void analyseData(JFrame frame) throws Exception 
    {
        try {
	            String result = ConnectGoogleAPIServer.analyseUserCVData();
	            int decision = JOptionPane.showOptionDialog(frame, result,
                "AI CV Analysis",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Yes, Save", "No, Retry"},
                "Yes, Save");
            
	            // Here if the user press yes which mean the analysing that the user got is suitable 
	            // else if pressed no -> nothing will happen and the data will not be save in the server .
	            if (decision == JOptionPane.OK_OPTION) 
	            {
	            	int userId = Session.getUserId();
	            	String filePath =  StoreUserDataLocal.getPersonaldatapahtcv();
	            	// Here we sending request to the server to save the user analysing data in the DB . 
	            	AnalyseController.sendAnalyseRequest(filePath,userId);
	            		    
	            }
	            
    		} catch (IOException e) {
            e.printStackTrace();
        }
    }
}
