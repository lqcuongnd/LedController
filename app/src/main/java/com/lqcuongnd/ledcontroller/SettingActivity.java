package com.lqcuongnd.ledcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText txtMaxValue;
    SeekBar  seekMaxValue;
    Button   btnApply;

    int max = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setPalettes();

        setListenner();
    }

    private void setPalettes() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txtMaxValue = (EditText) findViewById(R.id.txtMaxValue);
        seekMaxValue = (SeekBar) findViewById(R.id.seekMaxValue);
        btnApply = (Button) findViewById(R.id.btnApply);

        seekMaxValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                max = seekMaxValue.getProgress();
                txtMaxValue.setText(Integer.toString(max));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("MaxValueToTurnOn").setValue(max);
                finish();
            }
        });

    }

    private void setListenner() {
        mDatabase.child("MaxValueToTurnOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                max = Integer.parseInt(dataSnapshot.getValue().toString());

                txtMaxValue.setText(dataSnapshot.getValue().toString());
                seekMaxValue.setProgress(max);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("fail", "Failed to read value.", error.toException());
            }
        });
    }
}
