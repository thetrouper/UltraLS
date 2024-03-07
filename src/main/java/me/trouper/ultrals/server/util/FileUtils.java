package me.trouper.ultrals.server.util;

import me.trouper.ultrals.UltraLS;

import java.io.File;
public class FileUtils {
    public static boolean folderExists(String folderName) {
        File folder = new File(UltraLS.getInstance().getDataFolder(), folderName);
        return folder.exists() && folder.isDirectory();
    }
    public static void createFolder(String folderName) {
        File folder = new File(UltraLS.getInstance().getDataFolder(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
