package com.quantuminventions.speechtotext.dictionary;

import android.annotation.SuppressLint;

import com.quantuminventions.speechtotext.model.DictionaryModel;

import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
    private ArrayList<DictionaryModel> dictionaryData = null;
    public ArrayList<DictionaryModel> createDictionaryNew(ArrayList<DictionaryModel> array) {
        if (array != null) {
            dictionaryData = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                dictionaryData.add(new DictionaryModel(array.get(i).getDictionaryWord(), 0));
            }
        }
        return dictionaryData;
    }

    public HashMap<Integer, Integer> getDictionaryMatchPosition(String speechTxt) {
        String[] speechWords = speechTxt.split("\\s+");
        @SuppressLint("UseSparseArrays") HashMap<Integer, Integer> updatedPositions = new HashMap<>();

        //Check each individual word
        for (int i = 0; i < dictionaryData.size(); i++) {
            DictionaryModel dictionaryModel = dictionaryData.get(i);

            for (String speechWord : speechWords) {
                if (dictionaryModel.getDictionaryWord().equalsIgnoreCase(speechWord) ||
                        dictionaryModel.getDictionaryWord().equalsIgnoreCase(speechTxt)) {
                    dictionaryModel.setSpeechFrequency(dictionaryModel.getSpeechFrequency() + 1);
                    updatedPositions.put(i, i);
                }
            }
        }


        //Check word sequence
        for (int i = 0; i < dictionaryData.size(); i++) {
            DictionaryModel dictionaryModel = dictionaryData.get(i);

            if (speechTxt.toLowerCase().contains(dictionaryModel.getDictionaryWord().toLowerCase())) {
                if (!updatedPositions.containsKey(i)) {
                    dictionaryModel.setSpeechFrequency(dictionaryModel.getSpeechFrequency() + 1);
                    updatedPositions.put(i, i);
                }
            }
        }

        return updatedPositions;
    }
}
