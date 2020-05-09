package com.example.pkuscheduler.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.pkuscheduler.R;

public class ItemTouchHelperClass extends ItemTouchHelper.Callback {
    private ItemTouchHelperAdapter adapter;
    private VectorDrawableCompat mDeleteIcon;
    public Context mContext;

    public interface ItemTouchHelperAdapter {
        void onItemMoved(int fromPosition, int toPosition);
        void onItemRemoved(int position);
    }

    public ItemTouchHelperClass(ItemTouchHelperAdapter ad, Context context) {
        adapter = ad;
        mContext=context;
        mDeleteIcon = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_arrow_forward_24px, null);
        mDeleteIcon.setBounds(0, 0, mDeleteIcon.getIntrinsicWidth(), mDeleteIcon.getIntrinsicHeight());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int upFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(upFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemRemoved(viewHolder.getAdapterPosition());

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View itemView = viewHolder.itemView;
            Paint p = new Paint();


            ToDoItemRecyclerViewAdapter.ViewHolder vh= (ToDoItemRecyclerViewAdapter.ViewHolder)viewHolder;

            if(dX > 0){
                float opacity = 0.1f+(2*dX/(itemView.getRight()-itemView.getLeft()));
                opacity=opacity>1?1:opacity;
                //TODO: 提到外面去
                int nonOpacityColor = recyclerView.getResources().getColor(R.color.colorBrandingBlue);
                p.setColor((nonOpacityColor&0x00ffffff) |
                        ((int)(opacity *0xff))<<24);
            }
            else{
                float opacity = 0.1f+(2*dX/(itemView.getLeft()-itemView.getRight()));
                opacity=opacity>1?1:opacity;
                //TODO: 提到外面去
                int nonOpacityColor = recyclerView.getResources().getColor(R.color.colorBrandingRed);
                p.setColor((nonOpacityColor&0x00ffffff) |
                        ((int)(opacity *0xff))<<24);
            }
            c.drawRoundRect((float)itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(),
                    DpToPx(8), DpToPx(8), p);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    public int DpToPx(float dp){
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                mContext.getResources().getDisplayMetrics()));
    }

}
