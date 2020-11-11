package com.example.newcovidtracker.Tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newcovidtracker.R;

import java.util.ArrayList;
import java.util.List;

public class IndiaAdapter extends ArrayAdapter<IndiaModel>
{
    private Context context;
    private List<IndiaModel> IndiaModelList;
    private List<IndiaModel> IndiaModelListFiltered;

    public IndiaAdapter(@NonNull Context context, List<IndiaModel> IndiaModelList)
    {

        super(context, R.layout.india_custom_item,IndiaModelList);
        this.context = context;
        this.IndiaModelList = IndiaModelList;
        this.IndiaModelListFiltered = IndiaModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.india_custom_item,null,true);
        TextView stateName = view.findViewById(R.id.stateName);

        stateName.setText(IndiaModelListFiltered.get(position).getState());


        return view;
    }

    @Override
    public int getCount() {
        return IndiaModelListFiltered.size();
    }

    @Nullable
    @Override
    public IndiaModel getItem(int position)
    {
        return IndiaModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter()
    {
        Filter filter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults filterResults = new FilterResults();
                if(constraint ==null || constraint.length()==0)
                {
                    filterResults.count = IndiaModelList.size();
                    filterResults.values = IndiaModelList;
                }
                else
                {
                    List<IndiaModel> IndiaresultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(IndiaModel itemsModel: IndiaModelList)
                    {
                        if(itemsModel.getState().toLowerCase().contains(searchStr))
                        {
                            IndiaresultsModel.add(itemsModel);
                        }
                        filterResults.count = IndiaresultsModel.size();
                        filterResults.values = IndiaresultsModel;
                    }
                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                IndiaModelListFiltered = (List<IndiaModel>) results.values;
                IndiaActivity.IndiaModelList = (List<IndiaModel>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;

    }
}
