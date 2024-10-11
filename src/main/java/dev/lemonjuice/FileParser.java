package dev.lemonjuice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

    public static ModProperties parseModProperties(String fileContent) {
        String modId = extractProperty(fileContent, "mod_id");
        String modName = extractProperty(fileContent, "mod_name");
        String modVersion = extractProperty(fileContent, "mod_version");

        return new ModProperties(modId, modName, modVersion);
    }

    private static String extractProperty(String content, String propertyName) {
        Pattern pattern = Pattern.compile(propertyName + "=([^\n\r]*)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    public static class ModProperties {
        private final String modId;
        private final String modName;
        private final String modVersion;

        public ModProperties(String modId, String modName, String modVersion) {
            this.modId = modId;
            this.modName = modName;
            this.modVersion = modVersion;
        }

        public String getModId() {
            return modId;
        }

        public String getModName() {
            return modName;
        }

        public String getModVersion() {
            return modVersion;
        }

        @Override
        public String toString() {
            return "ModProperties{" +
                    "modId='" + modId + '\'' +
                    ", modName='" + modName + '\'' +
                    ", modVersion='" + modVersion + '\'' +
                    '}';
        }
    }
}