package com.quantuminventions.speechtotext.ui.viewholder;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quantuminventions.speechtotext.R;
import com.quantuminventions.speechtotext.model.DictionaryModel;

import java.text.MessageFormat;

public class DictionaryItemHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView tvWord, tvFrequency;
    private CardView cvWordBg, cvFreqBg;

    public DictionaryItemHolder(View itemView) {
        super(itemView);
        tvWord =  itemView.findViewById(R.id.tv_word);
        tvFrequency =  itemView.findViewById(R.id.tv_frequency);
        cvWordBg =  itemView.findViewById(R.id.cv_word_bg);
        cvFreqBg =  itemView.findViewById(R.id.cv_freq_bg);
    }

    public void bindItem(DictionaryModel dictionaryModel) {
        tvWord.setText(dictionaryModel.getDictionaryWord());
        tvFrequency.setText(MessageFormat.format("{0}", dictionaryModel.getSpeechFrequency()));
    }

    public void updateItem(boolean isUpdated) {
        if (isUpdated) {
            cvWordBg.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
            cvFreqBg.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
            tvWord.setTextColor(itemView.getContext().getResources().getColor(R.color.colorWhite));
            tvFrequency.setTextColor(itemView.getContext().getResources().getColor(R.color.colorWhite));
        } else {
            cvWordBg.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorWhite));
            cvFreqBg.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorWhite));
            tvWord.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
            tvFrequency.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
        }
    }
}
