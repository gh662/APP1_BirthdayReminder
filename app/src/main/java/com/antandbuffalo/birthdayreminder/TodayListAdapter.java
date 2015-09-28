package com.antandbuffalo.birthdayreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i677567 on 23/9/15.
 */
public class TodayListAdapter extends BaseAdapter {

    List<DateOfBirth> dobs = getDataForListView();
    @Override
    public int getCount() {
        return dobs.size();
    }

    @Override
    public DateOfBirth getItem(int position) {
        return dobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.today_listitem, parent, false);
        }

        TextView name = (TextView)convertView.findViewById(R.id.textView1);
        TextView desc = (TextView)convertView.findViewById(R.id.textView2);

        DateOfBirth dob = dobs.get(position);
        name.setText(dob.name);
        desc.setText(dob.description);

        return convertView;
    }

    public List<DateOfBirth> getDataForListView()
    {
        List<DateOfBirth> codeLearnChaptersList = new ArrayList<DateOfBirth>();

        for(int i=0;i<10;i++)
        {

            DateOfBirth chapter = new DateOfBirth();
            chapter.name = "Chapter "+i;
            chapter.description = "This is description for chapter "+i;
            codeLearnChaptersList.add(chapter);
        }

        return codeLearnChaptersList;

    }
}