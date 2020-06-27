package com.rolvatech.cgc.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import com.rolvatech.cgc.R;


class DialogInit {

    @LayoutRes
    static int getInflateLayout(CgcDialog.Builder builder) {
        if (builder.getItems() != null) {
            return R.layout.md_dialog_list;
        } else {
            return R.layout.md_dialog_basic;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @UiThread
    public static void init(final CgcDialog dialog) {
        final CgcDialog.Builder builder = dialog.builder;

        // Set cancelable flag and dialog background color
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView title = (TextView) dialog.view.findViewById(R.id.md_title);
        TextView content = (TextView) dialog.view.findViewById(R.id.md_content);
        ScrollView scrollView = (ScrollView) dialog.view.findViewById(R.id.md_contentScrollView);
        RecyclerView recyclerView = (RecyclerView) dialog.view.findViewById(R.id.md_contentRecyclerView);

        Button btnPositive = (Button) dialog.view.findViewById(R.id.btnPositive);
        Button btnNegative = (Button) dialog.view.findViewById(R.id.btnNegative);

        btnPositive.setTag(DialogAction.POSITIVE);
        btnNegative.setTag(DialogAction.NEGATIVE);


        btnPositive.setText(builder.getPositiveText());
        btnNegative.setText(builder.getNegativeText());

        title.setText(builder.getTitle());

        if (TextUtils.isEmpty(builder.getContent())) {
            scrollView.setVisibility(View.GONE);
        } else {
            content.setText(builder.getContent());
        }

        if (recyclerView != null) {
            dialog.setRecyclerView(recyclerView);
            dialog.setListType(CgcDialog.ListType.SINGLE);
            builder.setAdapter(new DefaultRvAdapter(dialog, CgcDialog.ListType.getLayoutForType(dialog.getListType())));
            dialog.invalidateList();
        }

        btnPositive.setOnClickListener(dialog);
        btnNegative.setOnClickListener(dialog);

        dialog.setViewInternal(dialog.view);
    }
}


