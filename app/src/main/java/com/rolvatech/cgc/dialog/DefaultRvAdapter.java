package com.rolvatech.cgc.dialog;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.rolvatech.cgc.R;



class DefaultRvAdapter extends RecyclerView.Adapter<DefaultRvAdapter.DefaultVH> {

    private final CgcDialog dialog;
    @LayoutRes
    private final int layout;
    private InternalListCallback callback;

    DefaultRvAdapter(CgcDialog dialog, @LayoutRes int layout) {
        this.dialog = dialog;
        this.layout = layout;
    }

    void setCallback(InternalListCallback callback) {
        this.callback = callback;
    }

    @Override
    public DefaultVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new DefaultVH(view, this);
    }

    @Override
    public void onBindViewHolder(DefaultVH holder, int index) {
        final View view = holder.itemView;
        switch (dialog.getListType()) {
            case SINGLE: {
                @SuppressLint("CutPasteId")
                RadioButton radio = (RadioButton) holder.control;
                boolean selected = dialog.builder.getSelectedIndex() == index;
                if (selected) {
                    dialog.builder.selectedView = view;
                }
                radio.setChecked(selected);
                break;
            }
        }

        holder.title.setText(dialog.builder.getItems().get(index).toString());
    }

    @Override
    public int getItemCount() {
        return dialog.builder.getItems() != null ? dialog.builder.getItems().size() : 0;
    }

    interface InternalListCallback {

        boolean onItemSelected(
                CgcDialog dialog, View itemView, int position, CharSequence text, boolean longPress);
    }

    static class DefaultVH extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        final CompoundButton control;
        final TextView title;
        final DefaultRvAdapter adapter;

        DefaultVH(View itemView, DefaultRvAdapter adapter) {
            super(itemView);
            control = (CompoundButton) itemView.findViewById(R.id.md_control);
            title = (TextView) itemView.findViewById(R.id.md_title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (adapter.callback != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                CharSequence text = null;
                if (adapter.dialog.builder.getItems() != null
                        && getAdapterPosition() < adapter.dialog.builder.getItems().size()) {
                    text = adapter.dialog.builder.getItems().get(getAdapterPosition()).toString();
                }
                adapter.callback.onItemSelected(adapter.dialog, view, getAdapterPosition(), text, false);
            }
        }

        @Override
        public boolean onLongClick(View view) {

            return false;
        }
    }
}
