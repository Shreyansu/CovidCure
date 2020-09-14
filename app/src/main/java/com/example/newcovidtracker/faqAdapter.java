package com.example.newcovidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class faqAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<FAQ> mDataset;

    public faqAdapter(List<FAQ> mDataset) {
        this.mDataset = mDataset;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView questionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextView=(TextView) itemView.findViewById(R.id.question_faq);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context=parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);

        View faqView=inflator.inflate(R.layout.item_faq,parent,false);
        ViewHolder viewHolder = new ViewHolder(faqView);
        return viewHolder;

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {

        FAQ faq = mDataset.get(position);
        TextView questionFaq = holder.itemView.findViewById(R.id.question_faq);
        TextView answerFaq = holder.itemView.findViewById(R.id.answer_faq);

        questionFaq.setText(faq.getQuestion());
        answerFaq.setText(faq.getAnswer());


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
