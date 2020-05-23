package com.example.pkuscheduler.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.pkuscheduler.R;

public class ItemTouchHelperClass extends ItemTouchHelper.Callback {
    private ItemTouchHelperAdapter adapter;
    private VectorDrawableCompat mDeleteIcon;
    private int DP24;
    public Context mContext;

    public Drawable d_close;
    public Drawable d_finish;
    public interface ItemTouchHelperAdapter {
        void onItemMoved(int fromPosition, int toPosition);
        void onItemRemoved(int position);
        void onItemCompleted(int position);
    }

    public ItemTouchHelperClass(ItemTouchHelperAdapter ad, Context context) {
        adapter = ad;
        mContext = context;

        DP24 = DpToPx(24);/*
        mDeleteIcon = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_arrow_forward_24px, null);
        mDeleteIcon.setBounds(0, 0, mDeleteIcon.getIntrinsicWidth(), mDeleteIcon.getIntrinsicHeight());*/
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
        if(direction==(1<<4))//LEFT
            adapter.onItemRemoved(viewHolder.getAdapterPosition());
        else
            adapter.onItemCompleted(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        d_close = mContext.getResources().getDrawable(R.drawable.ic_delete_24);
        d_finish = mContext.getResources().getDrawable(R.drawable.ic_importer_steps_24);
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View itemView = viewHolder.itemView;
            Paint p = new Paint();
            Paint bg = new Paint();
            ToDoItemRecyclerViewAdapter.ViewHolder vh= (ToDoItemRecyclerViewAdapter.ViewHolder)viewHolder;
            int MiddleY = (itemView.getTop()+itemView.getBottom())/2;
            if(dX > 0){
                float opacity = 0.1f+(dX/(itemView.getRight()-itemView.getLeft()));
                opacity=opacity>1?1:opacity;
                int nonOpacityColor = recyclerView.getResources().getColor(R.color.colorBrandingBlue);
                bg.setColor(nonOpacityColor);
                p.setColor((nonOpacityColor&0x00ffffff) |
                        ((int)(opacity *0xff))<<24);
            }
            else{
                float opacity = 0.1f+(dX/(itemView.getLeft()-itemView.getRight()));
                opacity=opacity>1?1:opacity;
                int nonOpacityColor = recyclerView.getResources().getColor(R.color.colorBrandingRed);
                bg.setColor(nonOpacityColor);
                p.setColor((nonOpacityColor&0x00ffffff) |
                        ((int)(opacity *0xff))<<24);
            }

            c.drawRoundRect((float)itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(),
                    0, 0, p);
            if(dX>0){
                c.drawCircle(
                        (float) (itemView.getLeft()+DP24*1.5), (float)MiddleY, dX/2, bg
                );
                d_finish.setBounds(
                        (int)itemView.getLeft()+DP24, MiddleY-DP24/2, (int) itemView.getLeft()+DP24+DP24, MiddleY+DP24/2
                );
                d_finish.draw(c);
            }
            else{
                c.drawCircle(
                        (float)(itemView.getRight()-DP24*1.5), (float)MiddleY, -dX/2, bg
                );
                d_close.setBounds(
                        (int)itemView.getRight()-DP24-DP24, MiddleY-DP24/2, (int) itemView.getRight()-DP24, MiddleY+DP24/2
                );
                d_close.draw(c);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    public int DpToPx(float dp){
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                mContext.getResources().getDisplayMetrics()));
    }

}
