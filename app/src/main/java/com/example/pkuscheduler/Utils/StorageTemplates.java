package com.example.pkuscheduler.Utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.example.pkuscheduler.ViewModels.ScheduleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class StorageTemplates {
    private Context mContext;
    private String mFileName;

    public StorageTemplates(Context context, String filename) {
        mContext = context;
        mFileName = filename;
    }

    public void saveToFile(ArrayList<ScheduleItem> items) throws JSONException, IOException {
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        fileOutputStream = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(JSON.toJSONString(items));
        outputStreamWriter.close();
        fileOutputStream.close();
    }

    public ArrayList<ScheduleItem> loadFromFile(ScheduleItem t) throws IOException, JSONException {
        ArrayList<ScheduleItem> items = new ArrayList<>();
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = mContext.openFileInput(mFileName);
            StringBuilder builder = new StringBuilder();
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(builder.toString()).nextValue();
            for (int i = 0; i < jsonArray.length(); i++) {
                ScheduleItem item = JSON.parseObject(builder.toString(), ScheduleItem.class);
                items.add(item);
            }


        } catch (FileNotFoundException fnfe) {
            //do nothing about it
            //file won't exist first time app is run
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }

        }
        return items;
    }

}

