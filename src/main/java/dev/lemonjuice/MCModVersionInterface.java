package dev.lemonjuice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static dev.lemonjuice.FileFetcher.*;
import static dev.lemonjuice.FileParser.parseModProperties;

/**
 * Gets information about mods from their GitHub repositories using the GitHub API
 * The mod URLs are read from a config file
 *
 * @author LemonJuice
 */
public class MCModVersionInterface {

    public static void main(String[] args) {
        ArrayList<String> modURLs = loadConfig("src/main/resources/config.json");

        // Extract the owner and repo name from the URLs
        for (String modURL : modURLs) {
            String owner = modURL.split("/")[3];
            String repo = modURL.split("/")[4];
            System.out.println("Owner: " + owner + ", Repo: " + repo);
            String fileContents = fetchFileContents(owner, repo);

            FileParser.ModProperties modProperties = parseModProperties(fileContents);
            System.out.println("Mod ID: " + modProperties.getModId());
            System.out.println("Mod Name: " + modProperties.getModName());
            System.out.println("Mod Version: " + modProperties.getModVersion());
            System.out.println("Mod URL: " + modURL);
            System.out.println();
        }
    }

    private static ArrayList<String> loadConfig(String filePath) {
        ArrayList<String> modURLs = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("modURLs");
            for (int i = 0; i < jsonArray.size(); i++) {
                modURLs.add(jsonArray.get(i).getAsString());
            }
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
        return modURLs;
    }
}