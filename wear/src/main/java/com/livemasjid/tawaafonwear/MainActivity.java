package com.livemasjid.tawaafonwear;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
import android.content.Intent;
import androidx.wear.activity.ConfirmationActivity;

public class MainActivity extends WearableActivity {

    private BoxInsetLayout mContainerView;
    private TextView mHeadingView;
    private TextView mCounterView;
    private TextView mFooterView;
    private ImageButton mResetButton;
    private ImageButton mMinusButton;

    private int counterValue = -1;
    private int tawaafValue = 0;
    private Long tsLong;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        tsLong = System.currentTimeMillis();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mHeadingView = (TextView) findViewById(R.id.heading);
        mCounterView = (TextView) findViewById(R.id.counter);
        mFooterView = (TextView) findViewById(R.id.footer);
        mResetButton = (ImageButton) findViewById(R.id.resetbutton);
        mMinusButton = (ImageButton) findViewById(R.id.minusbutton);

        mContainerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterValue<0||System.currentTimeMillis()-tsLong>30000) {
                    tsLong = System.currentTimeMillis();
                    if (counterValue > 5) {
                        tawaafComplete();
                    } else counterValue += 1;
                    Log.d("MainActivity", Integer.toString(counterValue));
                    updateDisplay();
                } else {
                    tooQuick();
                }
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterValue = 0;
                updateDisplay();
            }
        });

        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterValue > 0){
                    counterValue -= 1;
                    updateDisplay();
                }
            }
        });
    }

    private void tawaafComplete() {
        counterValue = -1;
        tawaafValue+=1;
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                getString(R.string.complete));
        startActivity(intent);
    }

    private void tooQuick() {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.FAILURE_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                getString(R.string.too_quick));
        startActivity(intent);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (counterValue>0){
            mCounterView.setText(Integer.toString(counterValue));
        } else {
            mCounterView.setText(R.string.initial_text);
        }
        if (counterValue>0) {
            mFooterView.setText(Integer.toString(tawaafValue));
        } else {
            mCounterView.setText(R.string.tawaaf_total);
        }
        /*if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mHeadingView.setTextColor(getResources().getColor(android.R.color.white));
            //mCounterView.setVisibility(View.VISIBLE);
        } else {
            mContainerView.setBackground(null);
            mHeadingView.setTextColor(getResources().getColor(android.R.color.black));
            //mCounterView.setVisibility(View.GONE);
        }*/
    }
}
