package com.github.airk.easynote.util;

import android.content.Context;
import android.util.Log;

import com.github.airk.easynote.bean.Note;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kevin on 2014/10/18.
 */
public class FileOperation {
    private static final String TAG = FileOperation.class.getSimpleName();

    public static List<File> getAllFiles(Context context) {
        return Arrays.asList(context.getFilesDir().listFiles());
    }

    public static void writeOrUpdateNote(Context context, Note note, String filename) {
        File file = new File(context.getFilesDir(), filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(new Gson().toJson(note, Note.class));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Note readFromFile(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        StringBuilder result = new StringBuilder();
        try {
            char[] buffer = new char[1024];
            FileReader reader = new FileReader(file);
            while (reader.read(buffer) > 0) {
                result.append(buffer);
            }
            reader.close();
            return new Gson().fromJson(result.toString().trim(), Note.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteFile(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        return file.exists() && file.delete();
    }
}
