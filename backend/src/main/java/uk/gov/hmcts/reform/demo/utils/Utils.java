package uk.gov.hmcts.reform.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.MultipartBodyBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Enumeration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class Utils {
    public <T> String JsonStringify(T object) {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            return objMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle this better in a real-world scenario
        }
    }


        public static String uploadFile( byte[] fileContent, String fileName, String keyPrefix) throws IOException {
            String boundary = Long.toHexString(System.currentTimeMillis());
            String CRLF = "\r\n";

            URL url = new URL(EnvThingies.UplaodServiceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream output = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true)) {

                // Send key prefix parameter
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"keyPrefix\"").append(CRLF);
                writer.append(CRLF);
                writer.append(keyPrefix).append(CRLF);

                // Send file
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"").append(CRLF);
                writer.append("Content-Type: application/octet-stream").append(CRLF);
                writer.append(CRLF);
                writer.flush();

                output.write(fileContent);
                output.flush();

                writer.append(CRLF);
                writer.append("--" + boundary + "--").append(CRLF);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String response = reader.lines().reduce("", String::concat);
                    return response;
                }
            } else {
                throw new IOException("Server returned non-OK status: " + responseCode);
            }
        }

//        public static void main(String[] args) {
//            try {
//                // Create an example file in memory
//                String content = "This is an example file content.";
//                byte[] fileContent = content.getBytes();
//
//                String serverUrl = "http://localhost:3005/upload";
//                String fileName = "example.txt";
//                String keyPrefix = "test-uploads";
//
//                String response = uploadFile(serverUrl, fileContent, fileName, keyPrefix);
//                System.out.println("Server response: " + response);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
