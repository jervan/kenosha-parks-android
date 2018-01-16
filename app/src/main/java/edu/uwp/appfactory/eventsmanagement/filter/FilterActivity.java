package edu.uwp.appfactory.eventsmanagement.filter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.model.EventFilter;
import edu.uwp.appfactory.eventsmanagement.util.DateUtils;
import edu.uwp.appfactory.eventsmanagement.util.SearchUtil;
import info.hoang8f.android.segmented.SegmentedGroup;


import static edu.uwp.appfactory.eventsmanagement.EventListScreen.EventListFragment.RESULT_FILTER;
import static edu.uwp.appfactory.eventsmanagement.EventListScreen.EventListFragment.RESULT_RESET;
/**
 * Created by dakota on 4/16/17.
 */

public class FilterActivity extends AppCompatActivity {

    private SegmentedGroup mParkPicker;
    private SegmentedGroup mAreaPicker;
    private SeekBar mDistanceSlider;
    private SeekBar mPriceSlider;
    private CheckBox mReoccurringCheckbox;
    private TextView mStartDateTextView;
    private TextView mEndDateTextView;
    //private TextView mAgeGroupTextView;
    private TextView mDistanceMaxTextView;
    private TextView mPriceMaxTextView;
    private LinearLayout mStartDateLayout;
    private LinearLayout mEndDateLayout;
    private Spinner mAgeGroupSpinner;
    private Button mResetButton;
    private Button mCancelButton;
    private Button mApplyButton;

    private String mAgeGroup;
    private Date mStartDate;
    private Date mEndDate;

    private EventFilter filter;

    //Result codes for start activity on result

    private final DatePickerDialog.OnDateSetListener mStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mStartDate = c.getTime();
            mStartDateTextView.setText(DateUtils.formatDateForFilter(mStartDate));
        }
    };

    private final DatePickerDialog.OnDateSetListener mEndDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mEndDate = c.getTime();
            mEndDateTextView.setText(DateUtils.formatDateForFilter(mEndDate));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        filter = SearchUtil.getFilterCriteria(this);
        setupDistanceSlider();
        setupSegmentedPicker();
        setupDatePickers();
        //setupAgeGroupSpinner();
        setupReoccurringCheckbox();
        setupButtons();
    }

    private void setupReoccurringCheckbox() {
        mReoccurringCheckbox = (CheckBox) findViewById(R.id.reoccurring_checkbox);
        mReoccurringCheckbox.setChecked(filter.isReoccurring());
    }

    private void setupSegmentedPicker() {
        mParkPicker = (SegmentedGroup) findViewById(R.id.park_picker);
        if (filter.getLocation() != null) {
            switch (filter.getLocation()) {
                case "Lincoln Park":
                    mParkPicker.check(R.id.lincoln_button);
                    break;
                case "Hobbs Park":
                    mParkPicker.check(R.id.hobbs_button);
                    break;
                case "Roosevelt Park":
                    mParkPicker.check(R.id.roosevelt_button);
                    break;
                default:
                    mParkPicker.check(R.id.all_park_button);
            }
        } else {
            mParkPicker.check(R.id.all_park_button);
        }
    }

    private void setupDistanceSlider() {
        //Set to 50 for now
        final int maxValue = 50;
        mDistanceSlider = (SeekBar) findViewById(R.id.distance_slider);
        mDistanceMaxTextView = (TextView) findViewById(R.id.distance_slider_max_label);
        mDistanceSlider.setMax(maxValue);
        if (filter.getDistance() > -1) {
            mDistanceSlider.setProgress(filter.getDistance());
            if (filter.getDistance() == maxValue) {
                mDistanceMaxTextView.setText("< " + Integer.toString(maxValue) + "mi");
            } else {
                mDistanceMaxTextView.setText(Integer.toString(filter.getDistance()) + " mi");
            }
        } else
        {
            mDistanceSlider.setProgress(0);
        }
        mDistanceSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 if (progress == maxValue) {
                     mDistanceMaxTextView.setText("< " + Integer.toString(maxValue) + "mi");
                 } else {
                     mDistanceMaxTextView.setText(Integer.toString(progress) + " mi");
                 }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setupDatePickers() {
        final Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        mStartDateLayout = (LinearLayout) findViewById(R.id.start_date_layout);
        mEndDateLayout = (LinearLayout) findViewById(R.id.end_date_layout);
        mStartDateTextView = (TextView) findViewById(R.id.start_date_label);
        mEndDateTextView = (TextView) findViewById(R.id.end_date_value);
        if (filter.getStartDate() > -1) {
            mStartDate = new Date(filter.getStartDate());
        } else {
            mStartDate = null;
        }
        if (filter.getEndDate() > -1) {
            mEndDate = new Date(filter.getEndDate());
        } else {
            mEndDate = null;
        }
        mStartDateTextView.setText(DateUtils.formatDateForFilter(mStartDate));
        mEndDateTextView.setText(DateUtils.formatDateForFilter(mEndDate));
        mStartDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog startPicker = new DatePickerDialog(FilterActivity.this, mStartDateListener,
                        currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                startPicker.show();
            }
        });

        mEndDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog endPicker = new DatePickerDialog(FilterActivity.this, mEndDateListener,
                        currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                endPicker.show();
            }
        });
    }

    private void setupAgeGroupSpinner() {
        mAgeGroupSpinner = (Spinner) findViewById(R.id.age_group_value);
        ArrayList<String> options = new ArrayList<>();
        options.add("Everyone");
        options.add("Toddler");
        options.add("Preschool");
        options.add("Youth");
        options.add("Family");
        options.add("Teen");
        options.add("Adult");
        options.add("Senior");

        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<>(this, R.layout.filter_spinner, R.id.age_group_spinner_value, options);
        optionsAdapter.setDropDownViewResource(R.layout.filter_spinner);
        mAgeGroupSpinner.setAdapter(optionsAdapter);
        mAgeGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAgeGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAgeGroupSpinner.setSelection(0);
        mAgeGroup = options.get(0);
    }

    private void resetFilter() {
        mReoccurringCheckbox.setChecked(false);
        //mAgeGroupSpinner.setSelection(0);
        //mAgeGroup = mAgeGroupSpinner.getSelectedItem().toString();
        mStartDate = null;
        mEndDate = null;
        mStartDateTextView.setText(DateUtils.formatDateForFilter(mStartDate));
        mEndDateTextView.setText(DateUtils.formatDateForFilter(mEndDate));
        mDistanceSlider.setProgress(0);
        //mPriceSlider.setProgress(0);
        mParkPicker.check(R.id.all_park_button);
        //mAreaPicker.check(R.id.all_area_button);
        setResult(RESULT_RESET);
    }

    private void setupButtons(){
        mResetButton = (Button) findViewById(R.id.filter_reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });

        mCancelButton = (Button) findViewById(R.id.filter_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mApplyButton = (Button) findViewById(R.id.filter_apply_button);
        mApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("LOCATION", ((RadioButton) findViewById(mParkPicker.getCheckedRadioButtonId())).getText());
                if (mDistanceSlider.getProgress() != 0) {
                    data.putExtra("DISTANCE", mDistanceSlider.getProgress());
                }
                //data.putExtra("PRICE", mPriceSlider.getProgress());
                //data.putExtra("AGE_GROUP", mAgeGroup);
                data.putExtra("FREE", mReoccurringCheckbox.isChecked());
                if (mStartDate != null) {
                    data.putExtra("START_DATE", mStartDate.getTime());
                }
                if (mEndDate != null) {
                    data.putExtra("END_DATE", mEndDate.getTime());
                }
                data.putExtra("REOCCURRING", mReoccurringCheckbox.isChecked());
                setResult(RESULT_FILTER, data);
                finish();
            }
        });
    }
}
