package troid.dythm.ui.util;

import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ResourceHelper {

    private static final int BUFFER_SIZE = 4096;

    private static String home = null;

    private static String theme = null;

    private static void createDirectory(String path) {
        File file = new File(getHome(), path);
        if (file.exists())
            return;

        if (!file.mkdir())
            System.err.println("Failed to create directory " + path);
        else
            System.err.println("Created directory: " + path);
    }

    private static void copyAssetFile(AssetManager assetManager, String path) {
        File file = new File(getHome(), path);
        if (file.exists())
            return;

        try (InputStream input = assetManager.open(path);
             OutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = input.read(buffer)) != -1)
                output.write(buffer, 0, read);
        } catch (IOException e) {
            System.err.println("Failed to copy file " + path);
            return;
        }

        System.err.println("Copied file: " + path);
    }

    private static void copyAsset(AssetManager assetManager, String path) {
        String[] files;
        try {
            files = assetManager.list(path);
        } catch (IOException e) {
            System.err.println("Failed to list directory " + path);
            return;
        }

        if (files.length == 0) {
            // regular file
            copyAssetFile(assetManager, path);
        } else {
            // directory
            createDirectory(path);
            for (String file : files) {
                copyAsset(assetManager, path + "/" + file);
            }
        }
    }

    public static void initializeAssets(AssetManager assetManager) {
        createDirectory("");
//        createDirectory("map/");
        copyAsset(assetManager, "map");
        createDirectory("save/");

        copyAsset(assetManager, "theme");
    }

    public static String getHome() {
        if (home == null) {
            home = Environment.getExternalStorageDirectory().getPath() + "/Dythm/";
        }
        return home;
    }

    public static String getProfilePath() {
        return "save/profile.ini";
    }

    public static String getImagePath(String path) {
        if (theme == null) {
            System.err.println("WARNING: theme unset");
            theme = "Default";
        }

        return getHome() + "theme/" + theme + "/" + path + ".png";
    }

    public static void setTheme(String theme) {
        ResourceHelper.theme = theme;
    }

    public static void writeFile(String fileName, List<String> fileContent) {
        try (FileWriter writer = new FileWriter(getHome() + fileName)) {
            for (String s : fileContent) {
                writer.write(s);
                writer.write('\n');
            }
        } catch (IOException e) {
            // ignore
        }
    }

    public static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getHome() + fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            // ignore - just return empty list
        }
        return lines;
    }

    public static List<String> listDirectories(String parent) {
        List<String> dirs = new ArrayList<>();
        File base = new File(getHome() + parent);
        File[] list = base.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isDirectory())
                    dirs.add(parent + file.getName() + "/");
            }
        }
        return dirs;
    }

    public static List<String> listFiles(String directory, String extension) {
        List<String> files = new ArrayList<>();
        File base = new File(getHome() + directory);
        File[] list = base.listFiles();
        if (list != null) {
            for (File file : base.listFiles()) {
                if (!file.isFile())
                    continue;

                String name = file.getName();
                if (name.substring(name.lastIndexOf('.') + 1).equals(extension))
                    files.add(name);
            }
        }
        return files;
    }

}
