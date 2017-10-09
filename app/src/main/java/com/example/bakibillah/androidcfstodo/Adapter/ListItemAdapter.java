package com.example.bakibillah.androidcfstodo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakibillah.androidcfstodo.MainActivity;
import com.example.bakibillah.androidcfstodo.Model.ToDo;
import com.example.bakibillah.androidcfstodo.R;

import java.util.List;

/**
 * Created by BakiBillah on 10/10/2017.
 */

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener{

    ItemClickListener itemClickListener;
    TextView item_title;
    TextView item_description;


    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);


        item_title = (TextView)itemView.findViewById(R.id.item_title);
        item_description = (TextView)itemView.findViewById(R.id.item_description);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.OnClick(view,getAdapterPosition(),false);


    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo
            contextMenuInfo) {
        contextMenu.setHeaderTitle("Select The Action");
        contextMenu.add(0,0,getAdapterPosition(),"DELETE");


    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    MainActivity mainActivity;
    List<ToDo> todoList;

    public ListItemAdapter(MainActivity mainActivity, List<ToDo> todoList) {
        this.mainActivity = mainActivity;
        this.todoList = todoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mainActivity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);

        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        //cond late


        //set item data

        holder.item_title.setText(todoList.get(position).getTile());
        holder.item_description.setText(todoList.get(position).getDescription());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                mainActivity.title.setText(todoList.get(position).getTile());
                mainActivity.description.setText(todoList.get(position).getDescription());


                mainActivity.isupdate =true; // flag is update
                mainActivity.idUpdate = todoList.get(position).getId();
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
