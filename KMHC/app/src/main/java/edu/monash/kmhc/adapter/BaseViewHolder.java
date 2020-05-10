package edu.monash.kmhc.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public abstract void onClick(View v);

}
