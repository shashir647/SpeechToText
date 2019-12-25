package com.quantuminventions.speechtotext.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.quantuminventions.speechtotext.R;
import com.quantuminventions.speechtotext.model.DictionaryModel;
import com.quantuminventions.speechtotext.ui.viewholder.DictionaryItemHolder;

import java.util.ArrayList;

public class DictionaryListAdapter extends RecyclerView.Adapter<DictionaryItemHolder> {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<DictionaryModel> dictionaryArrayNew;

    public DictionaryListAdapter(Context mContext, ArrayList<DictionaryModel> dictionaryArrayData) {
        this.mContext = mContext;
        this.dictionaryArrayNew = dictionaryArrayData;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public DictionaryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.inflate_dictionary_item, parent, false);
        return new DictionaryItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DictionaryItemHolder holder, int position) {
        holder.bindItem(dictionaryArrayNew.get(position));
        holder.updateItem(dictionaryArrayNew.get(position).isUpdated());
    }

    @Override
    public int getItemCount() {
        return (dictionaryArrayNew != null && dictionaryArrayNew.size() > 0) ? dictionaryArrayNew.size() : 0;
    }
}
