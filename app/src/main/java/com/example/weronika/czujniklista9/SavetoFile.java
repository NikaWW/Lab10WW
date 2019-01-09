package com.example.weronika.czujniklista9;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class SavetoFile {
    private String TAG;

    public SavetoFile(String TAG) {
        this.TAG = TAG;
    }

    public void saveFile(String filename, String textToSave){

        String albumName="StepsCounter";
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName);
        if (!file.mkdirs()) {
            Log.d(TAG, "Directory not created");
        }

        File file2=new File(file,filename);

        try {
            PrintWriter printWriter=new PrintWriter(file2);
            printWriter.println(textToSave);
            printWriter.close();
        }
        catch (IOException e){
            Log.d(TAG,"write error");

        }
    }
}

