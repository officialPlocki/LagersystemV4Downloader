package de.kabuecher.downloader.v4;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class Main {

    public static void main(String[] args) {
        String jarUrl = "https://github.com/officialPlocki/lagersystemV4/raw/refs/heads/main/latest/latest.jar";
        String downloaderJarUrl = "https://github.com/officialPlocki/LagersystemV4Downloader/releases/download/release/lagersystemV4downloader-1.0.jar";
        String jarFileName = "lagersystemV4.jar";

        try {
            downloadJar(jarUrl, jarFileName);
            runJar(jarFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //delete
        try {
            Path path = Path.of(jarFileName);
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String currentJarFileName = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            downloadJar(downloaderJarUrl, currentJarFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadJar(String jarUrl, String jarFileName) throws IOException {
        System.out.println("Downloading JAR from: " + jarUrl);

        HttpURLConnection connection = (HttpURLConnection) new URL(jarUrl).openConnection();
        connection.setRequestProperty("Accept", "application/octet-stream");

        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = new FileOutputStream(jarFileName)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Downloaded JAR to: " + jarFileName);
        }
    }

    private static void runJar(String jarFileName) throws IOException, InterruptedException {
        System.out.println("Running JAR: " + jarFileName);
        Process process = new ProcessBuilder("java", "-jar", jarFileName)
                .inheritIO()
                .start();
        process.waitFor();
    }
}