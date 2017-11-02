package com.jaiky.test.faceview;

import android.util.ArrayMap;

import java.util.LinkedHashMap;

/**
 * Author by Jaiky, Email jaikydota@163.com, Date on 10/12/2014.
 * PS: Not easy to write code, please indicate.
 */
public class FaceManager {
    private static FaceManager ourInstance;

    public static FaceManager getInstance() {
        if (ourInstance == null)
            ourInstance = new FaceManager();
        return ourInstance;
    }

    private LinkedHashMap<String, Integer> faceMap = null;


    public LinkedHashMap<String, Integer> getFaceMap() {
        return faceMap;
    }

    public int getFace(String faceNum) {
        return faceMap.get(faceNum);
    }

    private FaceManager() {
        faceMap = new LinkedHashMap<>();
        faceMap.put("[em_1]", R.drawable.em_1);
        faceMap.put("[em_2]", R.drawable.em_2);
        faceMap.put("[em_3]", R.drawable.em_3);
        faceMap.put("[em_4]", R.drawable.em_4);
        faceMap.put("[em_5]", R.drawable.em_5);
        faceMap.put("[em_6]", R.drawable.em_6);
        faceMap.put("[em_7]", R.drawable.em_7);
        faceMap.put("[em_8]", R.drawable.em_8);
        faceMap.put("[em_9]", R.drawable.em_9);
        faceMap.put("[em_10]", R.drawable.em_10);
        faceMap.put("[em_11]", R.drawable.em_11);
        faceMap.put("[em_12]", R.drawable.em_12);
        faceMap.put("[em_13]", R.drawable.em_13);
        faceMap.put("[em_14]", R.drawable.em_14);
        faceMap.put("[em_15]", R.drawable.em_15);
        faceMap.put("[em_16]", R.drawable.em_16);
        faceMap.put("[em_17]", R.drawable.em_17);
        faceMap.put("[em_18]", R.drawable.em_18);
        faceMap.put("[em_19]", R.drawable.em_19);
        faceMap.put("[em_20]", R.drawable.em_20);
        faceMap.put("[em_21]", R.drawable.em_21);
        faceMap.put("[em_22]", R.drawable.em_22);
        faceMap.put("[em_23]", R.drawable.em_23);
    }

}
