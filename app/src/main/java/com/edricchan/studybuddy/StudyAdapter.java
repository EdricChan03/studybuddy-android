package com.edricchan.studybuddy;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.TooltipCompat;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;


/***
 * Part of code came from the following sources:
 * <a href="https://guides.codepath.com/android/using-the-recyclerview">Using the recyclerview</a>
 *
 */
public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.Holder> {
    private List<TaskItem> mFeedItemList;
    private Context mContext;

    public StudyAdapter(Context context, List<TaskItem> feedItemList) {
        mFeedItemList = feedItemList;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public StudyAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recyclerview_item_row, parent, false);
        Holder viewHolder = new Holder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StudyAdapter.Holder holder, final int position) {
        TaskItem item = mFeedItemList.get(position);
        TextView textView = holder.textView;
        textView.setText(item.getTaskName());
        ImageButton imgBtn = holder.markDoneBtn;
        TooltipCompat.setTooltipText(imgBtn, "Mark as done");
//        imgBtn.setTooltipText() = TooltipCompat.setTooltipText();
//        imgBtn.setTooltipText(test);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemRemoved(position);
                mFeedItemList.remove(position);
                Snackbar.make(view, "Task marked as done.", 6000).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFeedItemList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton markDoneBtn;

        public Holder(View view) {
            super(view);
            markDoneBtn = (ImageButton) view.findViewById(R.id.item_markdone);
            textView = (TextView) view.findViewById(R.id.item_description);
        }
    }
}