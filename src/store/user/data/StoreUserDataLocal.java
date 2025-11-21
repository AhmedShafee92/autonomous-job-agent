package store.user.data;

//Libraries 
import java.awt.FileDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import javax.swing.JFrame;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;

import java.util.HashMap;
import java.util.Map;

public class StoreUserDataLocal 
{

	public static String getPersonaldatapahtcv() 
	{
		return personalDataPahtCV;
	}


	// Base directory name for the storage within the project directory
	// Data paths for the storage of the user .
	private static final String BASE_STORAGE_DIR = "AppStorage";
	static Path projectDir = Paths.get(System.getProperty("user.dir"), BASE_STORAGE_DIR);
	static Path personalDataPath = projectDir.resolve("personal_data");
	static Path analyseDataPath = projectDir.resolve("analyse_data");
	static final String personalDataPahtCV = "AppStorage/personal_data/user_cv.docx";
	static final String analyseDataPathJson = "AppStorage/analyse_data/user_analyse_data.json";
	
	public static void main(String[] args) {
		createSensitiveUserFiles();
	}
	
	// 1- create copy CV File and personal_data folder .
	// 2 - add CV File to personal_data folder.
	public static void storeCVUserLocal() 
	{
		// create word file and folder and put the word file inside the folder .
		initializeStorage();
		// createFolder();
		// This function show the user option to upload file and stored this file inside
		// local machine .
		createWordFile(personalDataPath);
		createJsonFile(analyseDataPath);		
		storeWordFileData();
	}

	/**
	 * Initializes the base storage directory in the project directory.
	 */
	public static void initializeStorage() 
	{
		try {
				// Create the storage directory if it doesn't exist
				if (!Files.exists(projectDir)) 
				{
					Files.createDirectories(projectDir);
				} else 
				{
					// nothing to do (folder exist)
				}
			} catch (Exception e) 
			{
				System.err.println("Error creating storage directory: " + e.getMessage());
				return;
			}

		try {

				if (!Files.exists(personalDataPath)) 
				{
					Files.createDirectories(personalDataPath);
				}
				if (!Files.exists(analyseDataPath)) 
				{
					Files.createDirectories(analyseDataPath);
				}
			} catch (IOException e) 	
			{
				System.err.println("Error creating directories: " + e.getMessage());
				return;
			}

	}

	// Data Area
	private StoreUserDataLocal() 
	{
		// TODO Auto-generated constructor stub
		storeCVUserLocal();
	}

	/*
	 * private static void createFolder() {
	 * 
	 * // Define the folder path and Word file name String folderPath =
	 * "personal_user_data"; String wordFileName = "user_cv.docx";
	 * 
	 * // Create the folder if it doesn't exist File folder = new File(folderPath);
	 * if (!folder.exists()) { if (folder.mkdir()) { } else {
	 * System.out.println("Failed to create folder: " + folderPath); return; } } //
	 * Define the full path for the Word file String wordFilePath = folderPath +
	 * File.separator + wordFileName; // Create the Word file
	 * createWordFile(wordFilePath); }
	 */

	/*
	 * private static void createWordFile(String filePath) { // Check if the word
	 * file is exist File file = new File(filePath); if (file.exists() &&
	 * file.isFile()) { return; } // Create an empty Word document try (XWPFDocument
	 * document = new XWPFDocument(); FileOutputStream fileOut = new
	 * FileOutputStream(filePath)) { // Write the empty document to the file
	 * document.write(fileOut); } catch (IOException e) {
	 * System.out.println("Error creating Word file: " + filePath);
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	// Here we save the CV file of the user.
	private static void storeWordFileData() 
	{
		// Create a JFrame for the file dialog
		JFrame frame = new JFrame();
		// Use the FileDialog class to prompt the user to select a file
		FileDialog fileDialog = new FileDialog(frame, "Select a file", FileDialog.LOAD);
		fileDialog.setVisible(true);
		// Get the selected file path
		String filePath = fileDialog.getDirectory() + fileDialog.getFile();
			
		// Here we save the word file that the user insert inside the
		// personal_data/cv_user.docs
		copyFile(filePath,personalDataPahtCV);
		
	}

	// Here we save the data in encrypted way
	public static void storeEncrptyData(String email, String password) {

		// Encrypt the data using base64
		String encryptedEmail = Base64.getEncoder().encodeToString(email.getBytes());
		String encryptedPassword = Base64.getEncoder().encodeToString(password.getBytes());

		// Store the encrypted data ץ
		String filePath = "AppStorage/privacy_data/user_credentials.json";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(encryptedEmail);
			writer.newLine();
			writer.write(encryptedPassword);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	
	  private static void copyFile(String source,String desc) 
	  {
	  
		  File sourceFile = new File(source); 
		  File destinationFile = new File(desc);
		  try 
		  { 
			  
			  InputStream in = new FileInputStream(sourceFile); 
			  OutputStream out =new FileOutputStream(destinationFile);
			  byte[] buffer = new byte[1024]; 
			  int length; 
			  while ((length = in.read(buffer)) > 0) 
			  { 
				  out.write(buffer, 0,length); 
			  } 
			  
			  in.close(); out.close(); 
		  	  System.out.println("File copied from " +sourceFile + " to " + destinationFile); 
			  
		  } catch (IOException e) 
		  {
				  System.out.println("Error copying file: " + e.getMessage()); 
		  }
	  
	  }
	 

	/*
	 * private static void copyFile(Path sourcePath) { File sourceFile =
	 * sourcePath.toFile(); Path test = presonal_user_cv; System.out.println(test);
	 * File destinationFile = presonal_user_cv.toFile();
	 * 
	 * try (InputStream in = new FileInputStream(sourceFile); OutputStream out = new
	 * FileOutputStream(destinationFile)) {
	 * 
	 * byte[] buffer = new byte[1024]; int length;
	 * 
	 * while ((length = in.read(buffer)) > 0) { out.write(buffer, 0, length); }
	 * 
	 * 
	 * } catch (IOException e) { System.out.println("Error copying file: " +
	 * e.getMessage()); }
	 * 
	 * }
	 */
	 

	// Building file analyse user data .
	private static int createAnalyseUserFolder() {

		// Inside the folder : 1- excel file -list places 2- excel file -list positions.
		String folderName = "analyse_user_data";
		File folder = new File(folderName);
		// Check if the folder exists, if not, create it
		if (!folder.exists()) {
			if (folder.mkdir()) {
			} else {
				System.out.println("Failed to create the folder: " + folderName);
				return 1;
			}
		}

		return 0;
	}

	// Building file analyse user data .
	public static int createSensitiveUserFiles() 
	{
		
	    // Define directory structure
	     final String BASE_DIR = "AppStorage";
	     final String PRIVACY_DIR = "privacy_data";
	     final String CREDENTIALS_FILE = "user_credentials.json";
	     
	     // Full path to credentials file
	     final String FULL_PATH = Paths.get(BASE_DIR, PRIVACY_DIR, CREDENTIALS_FILE).toString();
	     {
	         // Create directories if they don't exist
	         try {
	             Path path = Paths.get(BASE_DIR, PRIVACY_DIR);
	             Files.createDirectories(path);
	         } catch (IOException e) {
	             System.err.println("Failed to create directories: " + e.getMessage());
	             return -1;
	         }
	     }
	      
	    
		return 0;
	}

	private static Path createJsonFile(Path directoryPath) 
	{
		Path jsonFilePath = directoryPath.resolve("user_analyse_data.json");
		try {
				// Ensure the directory exists
				if (!Files.exists(directoryPath)) 
				{
					Files.createDirectories(directoryPath); // Create the directory if it doesn't exist
				}
	
				// Check if the JSON file already exists
				if (Files.exists(jsonFilePath)) 
				{
					return null;
				}
	
				// Create an empty JSON file
				Files.createFile(jsonFilePath);

			} catch (IOException e) {
			System.err.println("Error creating JSON file: " + e.getMessage());
		}

		return jsonFilePath;
	}

	private static Path createWordFile(Path directoryPath) 
	{
		// Define the Word file name and path
		Path wordFilePath = directoryPath.resolve("user_cv.docx");
		try {
			// Ensure the directory exists
			if (!Files.exists(directoryPath)) {
				Files.createDirectories(directoryPath); // Create the directory if it doesn't exist
			}

			// Check if the file already exists
			if (Files.exists(wordFilePath)) {
				return null;
			}

			// Create an empty Word document
			try (XWPFDocument document = new XWPFDocument();
					FileOutputStream out = new FileOutputStream(wordFilePath.toFile())) {
				document.write(out); // Write the empty document to the file
			}

		} catch (IOException e) {
			System.err.println("Error creating Word file: " + e.getMessage());
		}

		return wordFilePath;
	}

	public static Path getPersonalDataPath() {
		return personalDataPath;
	}

	public static void setPersonalDataPath(Path personalDataPath) {
		StoreUserDataLocal.personalDataPath = personalDataPath;
	}

	public static boolean CheckCVFileExist() 
	{
		return isPathExist(personalDataPath);
	}

	// Function to check if a path exists
	public static boolean isPathExist(Path path) {
		return Files.exists(path);
	}
	
	
    public static boolean isWordFileCVEmpty() 
    {
        try (FileInputStream fis = new FileInputStream(personalDataPahtCV)) 
        {
            // Load the Word document (docx)
            XWPFDocument document = new XWPFDocument(fis);
            
            // Check if the document has any paragraphs (this is a basic check)
            if (document.getParagraphs().isEmpty()) {
                return true;  // The document is empty
            }
            
            // Optionally, check for tables, headers, etc. if needed
            return false;  // The document is not empty
        } catch (IOException e) {
            e.printStackTrace();
            return false;  // In case of error, assume file is not empty
        }
    }
	
    
	
}
