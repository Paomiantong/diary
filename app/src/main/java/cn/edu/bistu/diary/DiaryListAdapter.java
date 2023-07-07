package cn.edu.bistu.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cn.edu.bistu.diary.Data.Model.Diary;

public class DiaryListAdapter extends ArrayAdapter<Diary> {
    private final int resourceId;

    public DiaryListAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Diary> objects) {
        super(context, resource, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        Diary item = getItem(position);
        if (convertView == null) { // 复用view
            holder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder.itemTitle = view.findViewById(R.id.item_title);
            holder.itemInfoAuthor = view.findViewById(R.id.item_info_a);
            holder.itemInfoTime = view.findViewById(R.id.item_info_t);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.itemTitle.setText(item.getTitle());
        holder.itemInfoAuthor.setText(item.getAuthor());
        holder.itemInfoTime.setText(item.getTime());
        return view;
    }
}

class ViewHolder {
    TextView itemTitle;
    TextView itemInfoAuthor;
    TextView itemInfoTime;
}
