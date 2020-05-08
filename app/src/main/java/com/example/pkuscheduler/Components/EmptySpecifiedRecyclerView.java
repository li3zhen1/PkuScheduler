package com.example.pkuscheduler.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EmptySpecifiedRecyclerView extends RecyclerView {
    private View emptyView;

    private RecyclerView.AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    };


    public EmptySpecifiedRecyclerView(Context context) {
        super(context);
    }

    public void showEmptyView() {

        Adapter<?> adapter = getAdapter();
        if (adapter != null && emptyView != null) {
            if (adapter.getItemCount() == 0) {
                emptyView.setVisibility(VISIBLE);
                EmptySpecifiedRecyclerView.this.setVisibility(GONE);
            } else {
                emptyView.setVisibility(GONE);
                EmptySpecifiedRecyclerView.this.setVisibility(VISIBLE);
            }
        }

    }

    public EmptySpecifiedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySpecifiedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
            observer.onChanged();
        }
    }

    public void setEmptyView(View v) {
        emptyView = v;
    }
}
