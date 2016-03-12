package com.logmedown.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arces.logmedown.R;

public class DrawerListItemAdapter extends BaseAdapter {

    private Activity mainActivity;
    private String[] optionName;
    private int[] optionImage;
    private ViewPager viewPager;
    private TextView fragmentName;
    private DrawerLayout drawer;
    private ListView drawerList;
    private TabLayout tabLayout;
    private int tabSize;
    private static LayoutInflater inflater = null;
    private static final String USERPREFERENCES = "UserPreferences";
    public SharedPreferences sharedPreferences;

    public DrawerListItemAdapter(Activity mainActivity, String[] optionName, int[] optionImage, ViewPager viewPager,
                                 TextView fragmentName, DrawerLayout drawer, ListView drawerList, TabLayout tabLayout,
                                 int tabSize) {
        this.mainActivity = mainActivity;
        this.optionName = optionName;
        this.optionImage = optionImage;
        this.viewPager = viewPager;
        this.fragmentName = fragmentName;
        this.drawer = drawer;
        this.drawerList = drawerList;
        this.tabLayout = tabLayout;
        this.tabSize = tabSize;
        inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return optionName.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.drawer_list_item, null);
        }
        TextView textViewOptionName = (TextView) view.findViewById(R.id.optionName);
        ImageView imageViewOptionImage = (ImageView) view.findViewById(R.id.optionImage);

        textViewOptionName.setText(optionName[position]);
        imageViewOptionImage.setImageResource(optionImage[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = -1;
                for (int i = 0; i < tabSize; i++) {
                    if (optionName[position].equalsIgnoreCase(tabLayout.getTabAt(i).getText().toString())) {
                        index = i;
                    }
                }
                if (index < tabSize && index != -1) {
                    viewPager.setCurrentItem(index);
                    fragmentName.setText(tabLayout.getTabAt(index).getText().toString());
                    drawer.closeDrawer(drawerList);
                } else if (optionName[position].equalsIgnoreCase("Log Out")) {
                    new AlertDialog.Builder(mainActivity)
                            .setTitle("Log Out")
                            .setMessage(R.string.textLogOutPrompt)
                            .setPositiveButton(R.string.textLogOutYes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences = mainActivity.getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    mainActivity.finish();
                                }

                            })
                            .setNegativeButton(R.string.textLogOutNo, null)
                            .show();
                }
            }
        });

        return view;
    }
}
