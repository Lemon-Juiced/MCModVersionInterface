package dev.lemonjuice;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;

public class FileFetcher {

    private static final String GITHUB_API_URL = "https://api.github.com/repos";

    /**
     * Fetches the contents of a file from a GitHub repository
     * In this case it fetches the contents of the gradle.properties file.
     *
     * @param owner The owner of the repository
     * @param repo The repository name
     * @return The contents of the file
     */
    public static String fetchFileContents(String owner, String repo) {
        String fileContents = "";
        try {
            fileContents = getFileContent(owner, repo, "gradle.properties");
            return fileContents;
        } catch (IOException e) {
            System.err.println("Error fetching file contents: " + e.getMessage());
        }
        return "Error fetching file contents";
    }

    /**
     * Fetches the contents of a file from a GitHub repository
     *
     * @param owner The owner of the repository
     * @param repo The repository name
     * @param filePath The path to the file
     * @return The contents of the file
     * @throws IOException If an error occurs during the request
     */
    public static String getFileContent(String owner, String repo, String filePath) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Build the URL for fetching the file content
        String fileUrl = GITHUB_API_URL + "/" + owner + "/" + repo + "/contents/" + filePath;

        // Create the request
        Request request = new Request.Builder()
                .url(fileUrl)
                .header("Accept", "application/vnd.github.v3+json")  // Request JSON response
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse the JSON response to extract the content field
            String responseBody = response.body().string();
            JsonReader jsonReader = new JsonReader(new StringReader(responseBody));
            jsonReader.setLenient(true);
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
            String base64Content = jsonObject.get("content").getAsString();

            // Remove newline characters from the base64 content
            base64Content = base64Content.replaceAll("\\n", "");

            // Decode the base64 content
            return decodeBase64Content(base64Content);
        }
    }

    /**
     * Decodes the base64 encoded content of a file
     *
     * @param base64Content The base64 encoded content
     * @return The decoded content
     */
    private static String decodeBase64Content(String base64Content) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
        return new String(decodedBytes);
    }
}
