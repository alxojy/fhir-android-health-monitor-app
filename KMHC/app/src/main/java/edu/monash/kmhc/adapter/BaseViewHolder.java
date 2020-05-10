package edu.monash.kmhc.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

/**
 * BaseViewHolder is an abstract class.
 * This class serves as a skeleton class for all ViewHolder classes to inherit from.
 * Subclasses must implement all the abstract methods found in this class.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * Constructor.
     * @param itemView
     */
    BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * On click method from View.OnClickListener interface
     * Subclass that inherits from this class must implement this method.
     */
    @Override
    public abstract void onClick(View v);

}
