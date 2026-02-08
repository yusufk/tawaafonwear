package com.livemasjid.tawaafonwear;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.activity.ConfirmationActivity;
import androidx.wear.ambient.AmbientLifecycleObserver;
import androidx.wear.ambient.AmbientLifecycleObserverKt;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private View mContainerView;
    private TextView mHeadingView;
    private TextView mCounterView;
    private TextView mFooterView;
    private ImageButton mResetButton;
    private ImageButton mMinusButton;
    private View mGreenLine;

    private int counterValue = 0;
    private int tawaafValue = 0;
    private long lastClickTime = 0;
    private static final long DEBOUNCE_TIME = 30000; // 30 seconds

    private AmbientLifecycleObserver ambientObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ambientObserver = AmbientLifecycleObserverKt.AmbientLifecycleObserver(this, new AmbientLifecycleObserver.AmbientLifecycleCallback() {
            @Override
            public void onEnterAmbient(@NonNull AmbientLifecycleObserver.AmbientDetails ambientDetails) {
                updateDisplay();
            }

            @Override
            public void onExitAmbient() {
                updateDisplay();
            }

            @Override
            public void onUpdateAmbient() {
                updateDisplay();
            }
        });
        getLifecycle().addObserver(ambientObserver);

        mContainerView = findViewById(R.id.container);
        mHeadingView = findViewById(R.id.heading);
        mCounterView = findViewById(R.id.counter);
        mFooterView = findViewById(R.id.footer);
        mResetButton = findViewById(R.id.resetbutton);
        mMinusButton = findViewById(R.id.minusbutton);
        mGreenLine = findViewById(R.id.green_line);

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                handleTap(false);
                return true;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent e) {
                handleTap(true);
            }
        });

        mContainerView.setOnTouchListener((v, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return true;
        });

        mResetButton.setOnClickListener(v -> {
            counterValue = 0;
            updateDisplay();
        });

        mMinusButton.setOnClickListener(v -> {
            if (counterValue > 0){
                counterValue -= 1;
                updateDisplay();
            }
        });

        updateDisplay();
    }

    private void handleTap(boolean isLongPress) {
        long currentTime = System.currentTimeMillis();
        if (isLongPress || (currentTime - lastClickTime > DEBOUNCE_TIME)) {
            incrementCounter();
            lastClickTime = currentTime;
        } else {
            showTooQuick();
        }
    }

    private void incrementCounter() {
        counterValue++;
        if (counterValue > 7) {
            tawaafComplete();
        } else {
            updateDisplay();
        }
    }

    private void tawaafComplete() {
        counterValue = 1;
        tawaafValue += 1;
        updateDisplay();
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                getString(R.string.complete));
        startActivity(intent);
    }

    private void showTooQuick() {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.FAILURE_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                getString(R.string.too_quick) + "\nLong press to override");
        startActivity(intent);
    }

    private void updateDisplay() {
        boolean isAmbient = ambientObserver != null && ambientObserver.isAmbient();
        
        if (counterValue > 0) {
            mCounterView.setText(String.valueOf(counterValue));
            mCounterView.setTextSize(80);
        } else {
            mCounterView.setText(R.string.initial_text);
            mCounterView.setTextSize(40);
        }
        mFooterView.setText(String.valueOf(tawaafValue));
        
        if (isAmbient) {
            mContainerView.setBackground(null);
            mGreenLine.setVisibility(View.GONE);
            mHeadingView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            mCounterView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            mFooterView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            mResetButton.setVisibility(View.GONE);
            mMinusButton.setVisibility(View.GONE);
        } else {
            mContainerView.setBackgroundResource(R.drawable.tawaaf);
            mGreenLine.setVisibility(View.VISIBLE);
            mResetButton.setVisibility(View.VISIBLE);
            mMinusButton.setVisibility(View.VISIBLE);
        }
    }
}
