import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUploadClient {
    public static void main(String[] args) 
    {
        String targetURL = "http://localhost:3000/upload"; 
        String filePath = "test.txt";
        try {
	            File file = new File(filePath);
	            String boundary = Long.toHexString(System.currentTimeMillis());
	            String CRLF = "\r\n"; // Line separator
	
	            HttpURLConnection connection = (HttpURLConnection) new URL(targetURL).openConnection();
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	
	            try (
	                OutputStream output = connection.getOutputStream();
	                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true)
	            ) {
	                // Send binary file
	                writer.append("--" + boundary).append(CRLF);
	                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append(CRLF);
	                writer.append("Content-Type: application/octet-stream").append(CRLF);
	                writer.append(CRLF).flush();
	
	                try (InputStream input = new FileInputStream(file)) 
	                {
	                    byte[] buffer = new byte[1024];
	                    for (int length; (length = input.read(buffer)) > 0;) 
	                    {
	                        output.write(buffer, 0, length);
	                    }
	                }
	                output.flush();
	                writer.append(CRLF).flush();
	
	                // End boundary
	                writer.append("--" + boundary + "--").append(CRLF).flush();
	            }
	
	            // Get server response
	            int responseCode = connection.getResponseCode();
	            System.out.println("Response Code: " + responseCode);
	
	            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) 
	            {
	                String line;
	                while ((line = in.readLine()) != null) 
	                {
	                    System.out.println("Server Response: " + line);
	                }
	            }

        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
