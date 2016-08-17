package com.antandbuffalo.birthdayreminder.upcoming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.antandbuffalo.birthdayreminder.Constants;
import com.antandbuffalo.birthdayreminder.DateOfBirth;
import com.antandbuffalo.birthdayreminder.R;
import com.antandbuffalo.birthdayreminder.addnew.AddNew;
import com.antandbuffalo.birthdayreminder.database.DateOfBirthDBHelper;
import com.antandbuffalo.birthdayreminder.fragments.MyFragment;
import com.antandbuffalo.birthdayreminder.today.TodayListAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by i677567 on 28/8/15.
 */
public class Upcoming extends MyFragment {
    UpcomingListAdapter upcomingListAdapter;
    public static Upcoming newInstance() {
        Upcoming fragment = new Upcoming();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upcoming, container, false);

        //ArrayAdapter<String> codeLearnArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, codeLearnChapters);
        upcomingListAdapter = new UpcomingListAdapter();
        //http://stackoverflow.com/questions/6495898/findviewbyid-in-fragment
        ListView upcomingListView = (ListView)rootView.findViewById(R.id.upcomingListView);
        upcomingListView.setAdapter(upcomingListAdapter);

        upcomingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateOfBirth dateOfBirth = upcomingListAdapter.getItem(position);
                System.out.println(dateOfBirth.getName());

                Intent intent = new Intent(view.getContext(), AddNew.class);
                startActivityForResult(intent, Constants.ADD_NEW_MEMBER);
            }
        });

        return rootView;
    }
    @Override
    public void refreshData() {
        upcomingListAdapter.refreshData();
    }
}
