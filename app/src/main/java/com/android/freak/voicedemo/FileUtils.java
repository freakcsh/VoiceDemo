package com.android.freak.voicedemo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

/**
 * Created by jiangkang on 2017/9/20.
 */

public class FileUtils {

    public static String getAssetsPath(String filename) {
        return "file:///android_asset/" + filename;
    }

    public static InputStream getInputStreamFromAssets(String filename) throws IOException {
        AssetManager manager = King.getApplicationContext().getAssets();
        return manager.open(filename);
    }

    public static String[] listFilesFromPath(String path) throws IOException {
        AssetManager manager = King.getApplicationContext().getAssets();
        return manager.list(path);
    }


    public static AssetFileDescriptor getAssetFileDescription(String filename) throws IOException {
        AssetManager manager = King.getApplicationContext().getAssets();
        return manager.openFd(filename);
    }


    public static void writeStringToFile(String string, File file, boolean isAppending) {
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {
            writer = new FileWriter(file);
            bufferedWriter = new BufferedWriter(writer);
            if (isAppending) {
                bufferedWriter.append(string);
            } else {
                bufferedWriter.write(string);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void writeStringToFile(String string, String filePath, boolean isAppending) {
        writeStringToFile(string, new File(filePath), isAppending);
    }


    public static Bitmap getBitmapFromAssets(String filename) throws IOException {
        AssetManager manager = King.getApplicationContext().getAssets();
        InputStream inputStream = manager.open(filename);
        return BitmapFactory.decodeStream(inputStream);
    }


    public static void copyAssetsToFile(final String assetFilename, final String dstName) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    File dstFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ktools", dstName);
                    fos = new FileOutputStream(dstFile);
                    InputStream fileInputStream = getInputStreamFromAssets(assetFilename);
                    byte[] buffer = new byte[1024 * 2];
                    int byteCount;
                    while ((byteCount = fileInputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                } catch (IOException e) {
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }


    /**
     * @param filename  filename you will create
     * @param directory directory where the file exists
     * @return true if the file created successfully, or return false
     */
    public static boolean createFile(String filename, String directory) {
        boolean isSuccess = false;
        File file = new File(directory, filename);
        if (!file.exists()) {
            try {
                isSuccess = file.createNewFile();
            } catch (IOException e) {
            }
        } else {
            file.delete();
            try {
                isSuccess = file.createNewFile();
            } catch (IOException e) {
            }
        }
        return isSuccess;
    }


    public static boolean hideFile(String directory, String filename) {
        boolean isSuccess;
        File file = new File(directory, filename);
        isSuccess = file.renameTo(new File(directory, ".".concat(filename)));
        if (isSuccess) {
            file.delete();
        }
        return isSuccess;
    }


    public static long getFolderSize(final String folderPath) {
        long size = 0;
        File directory = new File(folderPath);
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    size += getFolderSize(file.getAbsolutePath());
                } else {
                    size += file.length();
                }
            }
        }
        return size;
    }


    public static String readFromFile(String filename) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(getAssetFileDescription(filename).getFileDescriptor()));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
