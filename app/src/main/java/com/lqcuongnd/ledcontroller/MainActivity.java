package com.lqcuongnd.ledcontroller;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    ImageView btnTurn;
    ImageView btnAuto;
    ImageView btnSetting;
    ImageView btnAbout;
    TextView lblValue;
    TextView lblMax;
    LottieAnimationView gif;
    LottieAnimationView lotOn;

    String mode = "0";
    int max = 0;
    int sensor = 0;
    int light = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPalettes();

        setOnClick();

        setListenner();
    }

    private void setPalettes() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnTurn = (ImageView) findViewById(R.id.btnTurn);
        btnAuto = (ImageView) findViewById(R.id.btnAuto);
        btnSetting = (ImageView) findViewById(R.id.btnSetting);
        btnAbout = (ImageView) findViewById(R.id.btnAbout);
        lblValue = (TextView) findViewById(R.id.lblValue);
        lblMax = (TextView) findViewById(R.id.lblMax);
        gif = (LottieAnimationView) findViewById(R.id.gif);
        lotOn = (LottieAnimationView) findViewById(R.id.lotOn);
    }

    private void setOnClick() {
        btnTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (light == 1) {
                    //btnTurn.setImageResource(R.drawable.off);
                    mDatabase.child("Mode").setValue(0);
                    setTurn(0);
                } else {
                    //btnTurn.setImageResource(R.drawable.on);
                    mDatabase.child("Mode").setValue(1);
                    setTurn(1);
                }
            }
        });
        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.compareTo("2") == 0) {
                    if(light == 0){
                        mDatabase.child("Mode").setValue(0);
                    }
                    else {
                        mDatabase.child("Mode").setValue(1);
                    }
                    //btnAuto.setImageResource(R.drawable.auto_off);
                } else {
                    //btnAuto.setImageResource(R.drawable.auto_on);
                    mDatabase.child("Mode").setValue(2);
                }
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setListenner() {

        mDatabase.child("Mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mode = dataSnapshot.getValue().toString();

                if (mode.compareTo("2") == 0) {
                    btnAuto.setImageResource(R.drawable.auto_on);
                } else {
                    btnAuto.setImageResource(R.drawable.auto_off);
                    if(mode.compareTo("1") == 0){
                        setTurn(1);
                    }
                    else{
                        setTurn(0);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("fail", "Failed to read value.", error.toException());
            }
        });

        mDatabase.child("LightSensor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lblValue.setText(dataSnapshot.getValue().toString());
                sensor = Integer.parseInt(dataSnapshot.getValue().toString());

                if(mode.compareTo("2") == 0){
                    if(sensor >= max){
                        setTurn(1);
                    }
                    else{
                        setTurn(0);
                    }
                }
                else{
                    if(mode.compareTo("1") == 0){
                        setTurn(1);
                    }
                    else{
                        setTurn(0);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("fail", "Failed to read value.", error.toException());
            }
        });

        mDatabase.child("MaxValueToTurnOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lblMax.setText(dataSnapshot.getValue().toString());
                max = Integer.parseInt(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("fail", "Failed to read value.", error.toException());
            }
        });

    }

    private void setTurn(int p){
        if(p == 1){
            btnTurn.setImageResource(R.drawable.on);
            gif.setVisibility(View.VISIBLE);
            lotOn.setVisibility(View.VISIBLE);
            light = 1;
        }
        else{
            btnTurn.setImageResource(R.drawable.off);
            gif.setVisibility(View.GONE);
            lotOn.setVisibility(View.GONE);
            light = 0;
        }
    }

}
