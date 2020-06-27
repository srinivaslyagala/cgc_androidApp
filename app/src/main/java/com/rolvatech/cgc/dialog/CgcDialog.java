package com.rolvatech.cgc.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rolvatech.cgc.R;

import java.util.Collection;
import java.util.List;




/**
 * Created by viya on 17-10-2017.
 */

public class CgcDialog extends DialogBase implements View.OnClickListener,
        DefaultRvAdapter.InternalListCallback {


    protected final Builder builder;

    protected RecyclerView recyclerView;

    private ListType listType;

    public CgcDialog(Builder builder) {
        super(builder.getContext(), R.style.AppTheme);
        this.builder = builder;
        final LayoutInflater inflater = LayoutInflater.from(builder.getContext());
        view = (LinearLayout) inflater.inflate(DialogInit.getInflateLayout(builder), null);

        if (TextUtils.isEmpty(builder.getPositiveText())) {
            view.findViewById(R.id.btnPositive).setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(builder.getNegativeText())) {
            view.findViewById(R.id.btnNegative).setVisibility(View.GONE);
        }

        DialogInit.init(this);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public ListType getListType() {
        return listType;
    }

    public void setListType(ListType listType) {
        this.listType = listType;
    }

    @Override
    public boolean onItemSelected(CgcDialog dialog, View itemView, int position, CharSequence text, boolean longPress) {
        final RadioButton radio = (RadioButton) view.findViewById(R.id.md_control);
        if (!radio.isEnabled()) {
            return false;
        }
        boolean allowSelection = true;
        final int oldSelected = builder.selectedIndex;

        allowSelection = false;
        // Update selected index and send callback
        builder.setSelectedIndex(position);
        builder.selectedView = view;
        builder.adapter.notifyDataSetChanged();
        return false;
    }

    private void sendSingleChoiceCallback(View v) {
        if (builder.listCallback == null) {
            return;
        }
        CharSequence text = null;
        if (builder.getSelectedIndex() >= 0 && builder.getSelectedIndex() < builder.items.size()) {
            text = builder.items.get(builder.getSelectedIndex()).toString();
        }
        builder.listCallback.onSelection(this, v, builder.getSelectedIndex(), text);
    }

    @Override
    public void onClick(View view) {
        DialogAction tag = (DialogAction) view.getTag();
        switch (tag) {
            case POSITIVE:

                if (builder.onPositiveCallback != null) {
                    builder.onPositiveCallback.onClick(this, tag);
                } else {
                    if (builder.selectedView == null && builder.getItems() != null
                            && builder.getItems().size() > 0) {
                        Toast.makeText(builder.getContext(), "Please select item", Toast.LENGTH_LONG).show();
                    } else {
                        sendSingleChoiceCallback(builder.selectedView);
                        dismiss();
                    }
                }

                break;
            case NEGATIVE:
                if (builder.onNegativeCallback != null)
                    builder.onNegativeCallback.onClick(this, tag);
                else
                    dismiss();
                break;
        }
    }

    private static class DialogException extends WindowManager.BadTokenException {

        DialogException(@SuppressWarnings("SameParameterValue") String message) {
            super(message);
        }
    }

    @Override
    @UiThread
    public void show() {
        try {
            super.show();
        } catch (WindowManager.BadTokenException e) {
            throw new DialogException(
                    "Bad window token, you cannot show a dialog "
                            + "before an Activity is created or after it's hidden.");
        }
    }

    /**
     * Sets the dialog RecyclerView's adapter/layout manager, and it's item click listener.
     */
    final void invalidateList() {
        if (recyclerView == null) {
            return;
        } else if ((builder.items == null || builder.items.size() == 0)) {
            return;
        }
        if (builder.layoutManager == null) {
            builder.layoutManager = new LinearLayoutManager(getContext());
        }
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(builder.layoutManager);
        }
        recyclerView.setAdapter(builder.adapter);
        if (listType != null) {
            ((DefaultRvAdapter) builder.adapter).setCallback(this);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    enum ListType {
        REGULAR,
        SINGLE,
        MULTI;

        public static int getLayoutForType(ListType type) {
            switch (type) {
                case SINGLE:
                    return R.layout.md_listitem_singlechoice;
                default:
                    throw new IllegalArgumentException("Not a valid list type");
            }
        }
    }


    /**
     * A callback used for regular list dialogs.
     */
    public interface ListCallback {

        void onSelection(CgcDialog dialog, View itemView, int position, CharSequence text);
    }


    /**
     * An alternate way to define a single callback.
     */
    public interface SingleButtonCallback {

        void onClick(@NonNull CgcDialog dialog, @NonNull DialogAction which);
    }

    public static class Builder {


        private Context mContext;

        private String mTitle;

        private String mContent;

        private String mPositiveText;

        private String mNegativeText;

        private View customView;

        private List<Object> items;

        private LinearLayoutManager layoutManager;

        private RecyclerView.Adapter<?> adapter;

        private ListCallback listCallback;

        private int selectedIndex;

        public View selectedView;

        protected SingleButtonCallback onPositiveCallback;

        protected SingleButtonCallback onNegativeCallback;

        public Builder(Context mContext) {
            selectedIndex = -1;
            this.mContext = mContext;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public Builder setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
            return this;
        }

        public RecyclerView.Adapter<?> getAdapter() {
            return adapter;
        }

        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            this.adapter = adapter;
        }

        public Context getContext() {
            return mContext;
        }

        public void setContext(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setItems(Collection items) {
            this.items = (List<Object>) items;
            return this;
        }

        public List<Object> getItems() {
            return items;
        }

        public String getTitle() {
            return mTitle;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public String getContent() {
            return mContent;
        }

        public Builder setContent(String mContent) {
            this.mContent = mContent;
            return this;
        }

        public String getPositiveText() {
            return mPositiveText;
        }

        public Builder setPositiveText(String mPositiveText) {
            this.mPositiveText = mPositiveText;
            return this;
        }

        public String getNegativeText() {
            return mNegativeText;
        }

        public Builder setNegativeText(String mNegativeText) {
            this.mNegativeText = mNegativeText;
            return this;
        }

        public Builder itemsCallback(@NonNull ListCallback callback) {
            this.listCallback = callback;
            return this;
        }

        public Builder onPositive(@NonNull SingleButtonCallback callback) {
            this.onPositiveCallback = callback;
            return this;
        }

        public Builder onNegative(@NonNull SingleButtonCallback callback) {
            this.onNegativeCallback = callback;
            return this;
        }

        @UiThread
        public CgcDialog build() {
            return new CgcDialog(this);
        }

        @UiThread
        public void show() {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CgcDialog dialog = build();
                    dialog.show();
                }
            });

        }
    }

}
