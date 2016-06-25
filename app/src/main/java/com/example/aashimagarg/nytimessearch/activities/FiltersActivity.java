package com.example.aashimagarg.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.aashimagarg.nytimessearch.models.DatePickerFragment;
import com.example.aashimagarg.nytimessearch.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;

public class FiltersActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    final Calendar c = Calendar.getInstance();
    String date;
    String order;
    String newsd;
    CheckBox cbArts;
    CheckBox cbCulture;
    CheckBox cbDining;
    CheckBox cbPolitics;
    CheckBox cbSports;
    CheckBox cbStyle;
    CheckBox cbTechnology;
    CheckBox cbTravel;
    HashSet<String> checks = new HashSet<>();
        /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        //instantiate
        setupCheckboxes();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //custom font on toolbar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

        // Get access to our TextView
        TextView txt = (TextView) findViewById(R.id.myTitle);
        // Create the TypeFace from the TTF asset
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/NYFont.ttf");
        // Assign the typeface to the view
        txt.setTypeface(font);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        date = format.format(c.getTime());
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_new:
                if (checked) {
                    order = ((RadioButton) view).getText().toString();
                    break;
                }
            case R.id.radio_old:
                if (checked) {
                    order = ((RadioButton) view).getText().toString();
                    break;
                }
        }
    }

    public void setupCheckboxes() {
        cbArts = (CheckBox) findViewById(R.id.cbArts);
        cbCulture = (CheckBox) findViewById(R.id.cbCulture);
        cbDining = (CheckBox) findViewById(R.id.cbDining);
        cbPolitics = (CheckBox) findViewById(R.id.cbPolitics);
        cbSports = (CheckBox) findViewById(R.id.cbSports);
        cbStyle = (CheckBox) findViewById(R.id.cbStyle);
        cbTechnology = (CheckBox) findViewById(R.id.cbTechnology);
        cbTravel = (CheckBox) findViewById(R.id.cbTravel);

        cbArts.setOnCheckedChangeListener(checkListener);
        cbCulture.setOnCheckedChangeListener(checkListener);
        cbDining.setOnCheckedChangeListener(checkListener);
        cbPolitics.setOnCheckedChangeListener(checkListener);
        cbSports.setOnCheckedChangeListener(checkListener);
        cbStyle.setOnCheckedChangeListener(checkListener);
        cbTechnology.setOnCheckedChangeListener(checkListener);
        cbTravel.setOnCheckedChangeListener(checkListener);
    }

    // Fires every time a checkbox is checked or unchecked
    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            // compoundButton is the checkbox
            // boolean is whether or not checkbox is checked
            // Check which checkbox was clicked
            switch(compoundButton.getId()) {
                case R.id.cbArts:
                    if (checked) {
                        checks.add("\"Arts\"");
                    }
                    else
                        checks.remove("\"Arts\"");
                    break;
                case R.id.cbCulture:
                    if (checked)
                        checks.add("\"Culture\"");
                    else
                        checks.remove("\"Culture\"");
                    break;
                case R.id.cbDining:
                    if (checked)
                        checks.add("\"Dining\"");
                    else
                        checks.remove("\"Dining\"");
                    break;
                case R.id.cbPolitics:
                    if (checked)
                        checks.add("\"Politics\"");
                    else
                        checks.remove("\"Politics\"");
                    break;
                case R.id.cbSports:
                    if (checked)
                        checks.add("\"Sports\"");
                    else
                        checks.remove("\"Sports\"");
                    break;
                case R.id.cbStyle:
                    if (checked)
                        checks.add("\"Style\"");
                    else
                        checks.remove("\"Style\"");
                    break;
                case R.id.cbTechnology:
                    if (checked)
                        checks.add("\"Technology\"");
                    else
                        checks.remove("\"Technology\"");
                    break;
                case R.id.cbTravel:
                    if (checked)
                        checks.add("\"Travel\"");
                    else
                        checks.remove("\"Travel\"");
                    break;
            }
        }
    };

    public void onDoneButton(View view) {
        Intent data = new Intent();
        //send radio value
        data.putExtra("order", order);

        //send newsd value
        String newsDesk =
                android.text.TextUtils.join(" ", checks);
        newsd = String.format("news_desk:(%s)", newsDesk);
        data.putExtra("topics", newsd);

        //send calendar
        data.putExtra("date", date);
        setResult(RESULT_OK, data); // set result code and bundle data for response

        finish();
    }

}
