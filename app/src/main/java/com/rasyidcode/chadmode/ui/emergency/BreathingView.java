package com.rasyidcode.chadmode.ui.emergency;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.rasyidcode.chadmode.R;

import java.util.Locale;

/**
 * Custom animated view providing the 4-4-4-4 Box Breathing visualizer.
 * Uses ValueAnimator with zero battery drain when detached or paused.
 */
public class BreathingView extends View {

    public interface BreathingListener {
        void onStageChanged(BreathingStage newStage, int cycle);
    }

    private Paint outerTrackPaint;
    private Paint pulsatingCirclePaint;
    private Paint glowingStrokePaint;
    private Paint textStagePaint;
    private Paint textCountdownPaint;
    private Paint textCyclePaint;

    private int colorInhale;
    private int colorHold;
    private int colorExhale;

    private BreathingStage currentStage = BreathingStage.INHALE;
    private int currentCycle = 1;
    private boolean isRunning = false;

    private float progress = 0.0f; // 0.0f to 1.0f within current 4s stage
    private ValueAnimator stageAnimator;
    private BreathingListener listener;

    private final Rect textBounds = new Rect();

    public BreathingView(Context context) {
        super(context);
        init(context);
    }

    public BreathingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BreathingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        colorInhale = ContextCompat.getColor(context, R.color.cyan_inhale);
        colorHold = ContextCompat.getColor(context, R.color.amber_hold);
        colorExhale = ContextCompat.getColor(context, R.color.violet_exhale);
        int trackColor = ContextCompat.getColor(context, R.color.breathing_circle_stroke);
        int circleBg = ContextCompat.getColor(context, R.color.breathing_circle_bg);

        float density = getResources().getDisplayMetrics().density;

        outerTrackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerTrackPaint.setStyle(Paint.Style.STROKE);
        outerTrackPaint.setColor(trackColor);
        outerTrackPaint.setStrokeWidth(2f * density);

        pulsatingCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pulsatingCirclePaint.setStyle(Paint.Style.FILL);
        pulsatingCirclePaint.setColor(circleBg);

        glowingStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glowingStrokePaint.setStyle(Paint.Style.STROKE);
        glowingStrokePaint.setColor(colorInhale);
        glowingStrokePaint.setStrokeWidth(4f * density);

        textStagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textStagePaint.setColor(Color.WHITE);
        textStagePaint.setTextAlign(Paint.Align.CENTER);
        textStagePaint.setTextSize(18f * density);
        textStagePaint.setFakeBoldText(true);
        textStagePaint.setLetterSpacing(0.1f);

        textCountdownPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textCountdownPaint.setColor(colorInhale);
        textCountdownPaint.setTextAlign(Paint.Align.CENTER);
        textCountdownPaint.setTextSize(44f * density);
        textCountdownPaint.setFakeBoldText(true);

        textCyclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textCyclePaint.setColor(ContextCompat.getColor(context, R.color.text_muted));
        textCyclePaint.setTextAlign(Paint.Align.CENTER);
        textCyclePaint.setTextSize(12f * density);
    }

    public void setListener(BreathingListener listener) {
        this.listener = listener;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public BreathingStage getCurrentStage() {
        return currentStage;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;
        animateCurrentStage();
    }

    public void pause() {
        isRunning = false;
        if (stageAnimator != null) {
            stageAnimator.cancel();
            stageAnimator = null;
        }
        invalidate();
    }

    public void reset() {
        pause();
        currentStage = BreathingStage.INHALE;
        currentCycle = 1;
        progress = 0.0f;
        updateColorsForStage();
        invalidate();
    }

    private void animateCurrentStage() {
        if (!isRunning) return;

        if (stageAnimator != null) {
            stageAnimator.cancel();
        }

        updateColorsForStage();
        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

        if (listener != null) {
            listener.onStageChanged(currentStage, currentCycle);
        }

        stageAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        stageAnimator.setDuration(currentStage.getDurationMillis());
        stageAnimator.setInterpolator(
                (currentStage == BreathingStage.INHALE || currentStage == BreathingStage.EXHALE)
                        ? new AccelerateDecelerateInterpolator()
                        : new LinearInterpolator()
        );

        stageAnimator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            invalidate();
        });

        stageAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isRunning) {
                    if (currentStage == BreathingStage.HOLD_OUT) {
                        currentCycle++;
                    }
                    currentStage = currentStage.getNext();
                    progress = 0.0f;
                    animateCurrentStage();
                }
            }
        });

        stageAnimator.start();
    }

    private void updateColorsForStage() {
        int targetColor;
        switch (currentStage) {
            case INHALE:
                targetColor = colorInhale;
                break;
            case HOLD_IN:
            case HOLD_OUT:
                targetColor = colorHold;
                break;
            case EXHALE:
            default:
                targetColor = colorExhale;
                break;
        }
        glowingStrokePaint.setColor(targetColor);
        textCountdownPaint.setColor(targetColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2.0f;
        float centerY = height / 2.0f;

        float maxRadius = Math.min(centerX, centerY) * 0.85f;
        float minRadius = maxRadius * 0.45f;

        // Draw outer circular track
        canvas.drawCircle(centerX, centerY, maxRadius, outerTrackPaint);

        // Calculate dynamic circle radius
        float animatedRadius;
        switch (currentStage) {
            case INHALE:
                animatedRadius = minRadius + (maxRadius - minRadius) * progress;
                break;
            case HOLD_IN:
                animatedRadius = maxRadius;
                break;
            case EXHALE:
                animatedRadius = maxRadius - (maxRadius - minRadius) * progress;
                break;
            case HOLD_OUT:
            default:
                animatedRadius = minRadius;
                break;
        }

        // Draw animated central breathing circle & glowing ring
        canvas.drawCircle(centerX, centerY, animatedRadius, pulsatingCirclePaint);
        canvas.drawCircle(centerX, centerY, animatedRadius, glowingStrokePaint);

        // Calculate seconds countdown (4, 3, 2, 1)
        int secondsRemaining = (int) Math.ceil((1.0f - progress) * 4);
        if (secondsRemaining <= 0) secondsRemaining = 1;

        // Draw Texts in Center
        String countdownText = String.valueOf(secondsRemaining);
        String stageText = currentStage.getLabel();
        String cycleText = String.format(Locale.US, "Cycle %d", currentCycle);

        // Center Countdown number
        textCountdownPaint.getTextBounds(countdownText, 0, countdownText.length(), textBounds);
        float countdownY = centerY + (textBounds.height() / 2.0f);
        canvas.drawText(countdownText, centerX, countdownY, textCountdownPaint);

        // Stage label above countdown
        float stageY = centerY - 32f * getResources().getDisplayMetrics().density;
        canvas.drawText(stageText, centerX, stageY, textStagePaint);

        // Cycle text below countdown
        float cycleY = centerY + 42f * getResources().getDisplayMetrics().density;
        canvas.drawText(cycleText, centerX, cycleY, textCyclePaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pause();
    }
}
