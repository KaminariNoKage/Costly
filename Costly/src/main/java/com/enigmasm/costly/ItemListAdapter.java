package com.enigmasm.costly;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaustin on 10/16/13.
 */
public class ItemListAdapter extends ArrayAdapter<Item> {

    private final List<Item> data;
    private Activity activity;

    public ItemListAdapter(Activity a,  List<Item> data){
        super(a, R.layout.manage_item, data);
        this.activity = a;
        this.data = data;
    }


    private class ItemHolder{
        TextView itemName;
        TextView itemPrice;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        //Getting the database in the MainActivity
        final SQLiteDatabase db = MainActivity.mDBHelper.getWritableDatabase();

        View v = convertView;
        ItemHolder holder;

        ImageButton del;
        final TextView itemAmount;
        final TextView nameOfItem;

        if (v==null){
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.manage_item, null);

            del = (ImageButton) v.findViewById(R.id.deleteButton);
            itemAmount = (TextView) v.findViewById(R.id.valueAmount);
            nameOfItem = (TextView) v.findViewById(R.id.nameOfItem);

            holder = new ItemHolder();
            holder.itemName = nameOfItem;
            holder.itemPrice = itemAmount;
            v.setTag(holder);

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String itemName = nameOfItem.getText().toString();
                    activity.deleteFile(itemName);
                    data.remove(position);

                    //Delete from the database
                    // Define 'where' part of query.
                    String selection = SpenDBHelper.FeedEntry.COLUMN_NAME + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = { String.valueOf(itemName) };
                    // Issue SQL statement.
                    db.delete("items", selection, selectionArgs);

                    System.out.println("DELETING " + itemName);

                    ItemListAdapter.this.notifyDataSetChanged();
                }
            });
        }
        else  {
            holder = (ItemHolder) v.getTag();
        }

        Item item = data.get(position);

        holder.itemName.setText(item.itemName);
        holder.itemPrice.setText(item.itemPrice);

        return v;
    }

}