package com.santox.app.weightwatcher;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;


public class MainActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    static TableDbHelper mDbHelper;
    static SimpleCursorAdapter mAdapter;
    static ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mDbHelper = new TableDbHelper(getApplicationContext());
    }

    public static void updateTableView() {

        Cursor cursor = mDbHelper.getAll(TableDbHelper.SORT_DESC);
        mAdapter.changeCursor(cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_wipe) {
            /*wipe db*/
            mDbHelper.wipe();
            updateTableView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a AddFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return AddFragment.newInstance(position + 1);
                case 1:
                    return TableFragment.newInstance(position + 1);
                case 2:
                    return GraphFragment.newInstance(position + 1);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AddFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AddFragment newInstance(int sectionNumber) {
            AddFragment fragment = new AddFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public AddFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_add, container, false);
            Button add = (Button)rootView.findViewById(R.id.buttonAdd);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker dp = (DatePicker)rootView.findViewById(R.id.datePicker);
                    EditText weight = (EditText)rootView.findViewById(R.id.listViewTable);
                    long date = dp.getCalendarView().getDate();
                    if(weight.getText().toString().isEmpty()){
                        Toast.makeText(v.getContext(), "please insert a valid Weight", Toast.LENGTH_SHORT).show();
                    } else {
                        String s = weight.getText().toString();
                        Float f = Float.parseFloat(s);
                        long gr = (long)(f*1000);
                        mDbHelper.add(date, gr);

                        Date dt = new Date(date);
                        Toast.makeText(v.getContext(), "[" + dt.toString() + " , " + weight.getText().toString() +" Kg"
                                + "] Added", Toast.LENGTH_SHORT).show();

                        mAdapter.notifyDataSetChanged();
                        mListView.invalidateViews();
                        updateTableView();

                    }
                }
            });


            return rootView;
        }
    }


    public static class TableFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TableFragment newInstance(int sectionNumber) {
            TableFragment fragment = new TableFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public TableFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_table, container, false);

            Cursor cursor = mDbHelper.getAll(TableDbHelper.SORT_DESC);

            String[] fromColumns = {TableContract.WeightEntry.DATE,
                    TableContract.WeightEntry.G};
            int[] toViews = {R.id.date, R.id.weight};

            mAdapter = new SimpleCursorAdapter(rootView.getContext(),
                    R.layout.date_and_weight_entry, cursor, fromColumns, toViews, 0);

            mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if(view.getId()==R.id.date){
                        Date dt = new Date(cursor.getLong(columnIndex));
                        TextView tv = (TextView)view;
                        tv.setText(dt.toString());
                    }

                    if(view.getId()==R.id.weight){
                        float f = cursor.getLong(columnIndex) /1000f;
                        TextView tv = (TextView)view;
                        tv.setText(""+f);
                    }

                    return true;
                }
            });
            mListView = (ListView) rootView.findViewById(R.id.listViewTable);
            mListView.setAdapter(mAdapter);
            return rootView;
        }
    }

    public static class GraphFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GraphFragment newInstance(int sectionNumber) {
            GraphFragment fragment = new GraphFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GraphFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

            Cursor cursor = mDbHelper.getAll(TableDbHelper.SORT_ASC);

            GraphView.GraphViewData[] data = new GraphView.GraphViewData[cursor.getCount()];

            int i=0;
            while (cursor.moveToNext()) {
                double date = cursor.getLong(1);
                double weight = cursor.getLong(2) / 1000d;
                data[i++] = new GraphView.GraphViewData(date, weight);
            }

            GraphViewSeries exampleSeries = new GraphViewSeries(data);
            GraphView graphView = new LineGraphView(
                    rootView.getContext() // context
                    , "Weight" // heading
            );
            graphView.addSeries(exampleSeries); // data

            graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        Date dt = new Date((long)value);
                        String date = dt.getDate() + "." + (dt.getMonth()+1);
                        return date;
                    }
                    return null; // let graphview generate Y-axis label for us
                }
            });

            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graphLayout);
            layout.addView(graphView);

            return rootView;
        }
    }

}
