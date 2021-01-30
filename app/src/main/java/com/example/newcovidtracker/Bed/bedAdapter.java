package com.example.newcovidtracker.Bed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newcovidtracker.R;

import java.util.ArrayList;
import java.util.List;

public class bedAdapter extends RecyclerView.Adapter<bedAdapter.ViewHolder> implements Filterable
{

    Context context;
    ArrayList<bedModel> details;
    ArrayList<bedModel> detailsArrayList;

    public bedAdapter(Context context, ArrayList<bedModel> details)
    {


        this.context = context;
        this.details = details;
        this.detailsArrayList = new ArrayList<>(details);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.details_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.HosName.setText(details.get(position).getHosName());
        holder.totBed.setText(details.get(position).getHosName());
        holder.availbed.setText(details.get(position).getAvailbed());

        boolean isExpanded=details.get(position).isExpnaded();
        holder.layout2.setVisibility((isExpanded ? View.VISIBLE:View.INVISIBLE));

    }

    @Override
    public int getItemCount()
    {

        return details.size();
    }

    @Override
    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                List<bedModel> filteredList=new ArrayList<>();
                if(charSequence.toString().isEmpty())
                {
                    filteredList.addAll(detailsArrayList);
                }
                else
                {
                    for(bedModel bed:detailsArrayList)
                    {
                        if(bed.toString().toLowerCase().contains(charSequence.toString().toLowerCase()))
                            filteredList.add(bed);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView HosName,totBed,availbed;
        CardView layout;
        CardView layout2;
        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);
            HosName=itemView.findViewById(R.id.hospitalName);
            totBed=itemView.findViewById(R.id.totalBeds);
            availbed=itemView.findViewById(R.id.avail_bed);
            layout=itemView.findViewById(R.id.cardView);
            layout2=itemView.findViewById(R.id.cardView2);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bedModel b= details.get(getAdapterPosition());
                    b.setExpnaded(!b.isExpnaded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
