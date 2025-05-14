package com.wowcodes.supreme;

import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.widget.TextView;

public class PulseCountdown {

    private final TextView countdownTextView;
    private ObjectAnimator scaleAnimatorX;
    private ObjectAnimator scaleAnimatorY;
    private CountDownTimer countDownTimer;
    private OnCountdownCompletedListener countdownCompletedListener;

    // Constructor to initialize the TextView
    public PulseCountdown(TextView textView) {
        this.countdownTextView = textView;
    }

    // Start the countdown with pulsing animation
    public void startPulseCountdown(long totalTimeInMillis) {
        // Create a countdown timer
        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Update the text with the remaining time
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
                startPulseAnimation(); // Start the pulse animation every second
            }

            @Override
            public void onFinish() {
                countdownTextView.setText("0");
                if (countdownCompletedListener != null) {
                    countdownCompletedListener.onCompleted();
                }
            }
        }.start();
    }

    // Stop the countdown
    public void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // Set a listener for when the countdown finishes
    public void setOnCountdownCompletedListener(OnCountdownCompletedListener listener) {
        this.countdownCompletedListener = listener;
    }

    // Method to trigger the pulsing animation
    private void startPulseAnimation() {
        // Scale animation on X axis
        scaleAnimatorX = ObjectAnimator.ofFloat(countdownTextView, "scaleX", 1f, 1.3f, 1f);
        scaleAnimatorX.setDuration(300); // Duration for the pulse

        // Scale animation on Y axis
        scaleAnimatorY = ObjectAnimator.ofFloat(countdownTextView, "scaleY", 1f, 1.3f, 1f);
        scaleAnimatorY.setDuration(300);

        // Start the animations
        scaleAnimatorX.start();
        scaleAnimatorY.start();
    }

    // Interface to handle countdown completion
    public interface OnCountdownCompletedListener {
        void onCompleted();
    }
}
