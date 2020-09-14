package com.example.newcovidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class myAdapter extends ArrayAdapter<model>
{
    private Context context;
    private List<model> countryModelList;
    private List<model> countryModelListFiltered;

    public myAdapter(Context context, List<model> countryModelList)
    {
        super(context, R.layout.list_custom_item, countryModelList);
        this.context = context;
        this.countryModelList = countryModelList;
        this.countryModelListFiltered = countryModelList;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_item,null,true);
        TextView countryName = view.findViewById(R.id.countryName);
        ImageView countryImage = view.findViewById(R.id.flag);

        countryName.setText(countryModelListFiltered.get(position).getCountry());
        Glide.with(context).load(countryModelListFiltered.get(position).getFlag()).into(countryImage);

        return view;
    }

    @Override
    public int getCount() {
        return countryModelListFiltered.size();
    }

    @Nullable
    @Override
    public model getItem(int position) {
        return countryModelListFiltered.get(position);
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
                    filterResults.count = countryModelList.size();
                    filterResults.values = countryModelList;
                }
                else
                {
                    List<model> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(model itemsModel: countryModelList)
                    {
                        if(itemsModel.getCountry().toLowerCase().contains(searchStr))
                        {
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                countryModelListFiltered = (List<model>) results.values;
                CountryActivity.countryModelList = (List<model>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;

    }
}
