package com.example.newcovidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DistrictAdapter extends ArrayAdapter<DistrictModel>
{
    private Context context;
    private List<DistrictModel> DistrictModelList;
    private List<DistrictModel> DistrictModelListFiltered;

    public DistrictAdapter(@NonNull Context context, List<DistrictModel> DistrictModelList)
    {

        super(context, R.layout.india_custom_item,DistrictModelList);
        this.context = context;
        this.DistrictModelList = DistrictModelList;
        this.DistrictModelListFiltered = DistrictModelList;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.india_custom_item,null,true);
        TextView DistrictName = view.findViewById(R.id.stateName);

        DistrictName.setText(DistrictModelListFiltered.get(position).getState());


        return view;
    }

    @Override
    public int getCount() {
        return DistrictModelListFiltered.size();
    }

    @Nullable
    @Override
    public DistrictModel getItem(int position)
    {
        return DistrictModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

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
                    filterResults.count = DistrictModelList.size();
                    filterResults.values = DistrictModelList;
                }
                else
                {
                    List<DistrictModel> DistrictresultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(DistrictModel itemsModel: DistrictModelList)
                    {
                        if(itemsModel.getState().toLowerCase().contains(searchStr))
                        {
                            DistrictresultsModel.add(itemsModel);
                        }
                        filterResults.count = DistrictresultsModel.size();
                        filterResults.values = DistrictresultsModel;
                    }
                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                DistrictModelListFiltered = (List<DistrictModel>) results.values;
                DistrictActivity.DistrictModelList = (List<DistrictModel>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;

    }

}
