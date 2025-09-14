package tpass.chatme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

public class EnhancedAnimationHelper {
    
    public static final int DURATION_FAST = 150;
    public static final int DURATION_SHORT = 200;
    public static final int DURATION_MEDIUM = 300;
    public static final int DURATION_LONG = 500;
    
    // Enhanced fade animations with spring effect
    public static void fadeInWithSpring(View view) {
        fadeInWithSpring(view, DURATION_MEDIUM, null);
    }
    
    public static void fadeInWithSpring(View view, int duration, Animator.AnimatorListener listener) {
        view.setAlpha(0f);
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        view.setVisibility(View.VISIBLE);
        
        AnimatorSet animatorSet = new AnimatorSet();
        
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        
        alpha.setDuration(duration - 50);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        
        scaleX.setInterpolator(new OvershootInterpolator(1.2f));
        scaleY.setInterpolator(new OvershootInterpolator(1.2f));
        alpha.setInterpolator(new DecelerateInterpolator());
        
        animatorSet.playTogether(alpha, scaleX, scaleY);
        
        if (listener != null) {
            animatorSet.addListener(listener);
        }
        
        animatorSet.start();
    }
    
    // Message send animation with bounce effect
    public static void animateMessageSendEnhanced(View view) {
        animateMessageSendEnhanced(view, null);
    }
    
    public static void animateMessageSendEnhanced(View view, Animator.AnimatorListener listener) {
        view.setTranslationY(30f);
        view.setScaleX(0.9f);
        view.setScaleY(0.9f);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        AnimatorSet animatorSet = new AnimatorSet();
        
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", 30f, -5f, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.05f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.05f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        
        translateY.setDuration(DURATION_MEDIUM);
        scaleX.setDuration(DURATION_MEDIUM);
        scaleY.setDuration(DURATION_MEDIUM);
        alpha.setDuration(DURATION_SHORT);
        
        translateY.setInterpolator(new DecelerateInterpolator());
        scaleX.setInterpolator(new OvershootInterpolator(1.1f));
        scaleY.setInterpolator(new OvershootInterpolator(1.1f));
        alpha.setInterpolator(new DecelerateInterpolator());
        
        animatorSet.playTogether(translateY, scaleX, scaleY, alpha);
        
        if (listener != null) {
            animatorSet.addListener(listener);
        }
        
        animatorSet.start();
    }
    
    // Typing indicator animation
    public static void animateTypingIndicator(View dot1, View dot2, View dot3) {
        AnimatorSet animatorSet = new AnimatorSet();
        
        ObjectAnimator anim1 = createDotAnimation(dot1, 0);
        ObjectAnimator anim2 = createDotAnimation(dot2, 200);
        ObjectAnimator anim3 = createDotAnimation(dot3, 400);
        
        animatorSet.playTogether(anim1, anim2, anim3);
        animatorSet.start();
    }
    
    private static ObjectAnimator createDotAnimation(View dot, int delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(dot, "alpha", 0.3f, 1f, 0.3f);
        animator.setDuration(1200);
        animator.setStartDelay(delay);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }
    
    // Voice wave animation
    public static void animateVoiceWave(ViewGroup waveContainer) {
        if (waveContainer.getChildCount() == 0) return;
        
        AnimatorSet animatorSet = new AnimatorSet();
        
        for (int i = 0; i < waveContainer.getChildCount(); i++) {
            View wave = waveContainer.getChildAt(i);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(wave, "scaleY", 0.3f, 1.5f, 0.3f);
            scaleY.setDuration(800 + (i * 100)); // Stagger the animations
            scaleY.setRepeatCount(ValueAnimator.INFINITE);
            scaleY.setStartDelay(i * 100);
            scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
            
            animatorSet.play(scaleY);
        }
        
        animatorSet.start();
    }
    
    // Slide with overshoot
    public static void slideInFromBottomWithOvershoot(View view) {
        slideInFromBottomWithOvershoot(view, DURATION_MEDIUM, null);
    }
    
    public static void slideInFromBottomWithOvershoot(View view, int duration, Animator.AnimatorListener listener) {
        view.setTranslationY(view.getHeight());
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0f);
        animator.setDuration(duration);
        animator.setInterpolator(new OvershootInterpolator(1.2f));
        
        if (listener != null) {
            animator.addListener(listener);
        }
        
        animator.start();
    }
    
    // Ripple effect with custom colors
    public static void createCustomRippleEffect(View view, int color) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.15f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.15f, 1f);
        
        scaleX.setDuration(200);
        scaleY.setDuration(200);
        
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }
    
    // Text reveal animation
    public static void animateTextReveal(TextView textView, String text) {
        textView.setText("");
        textView.setVisibility(View.VISIBLE);
        
        ValueAnimator animator = ValueAnimator.ofInt(0, text.length());
        animator.setDuration(text.length() * 50); // 50ms per character
        animator.setInterpolator(new DecelerateInterpolator());
        
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            String partialText = text.substring(0, Math.min(animatedValue, text.length()));
            textView.setText(partialText);
        });
        
        animator.start();
    }
    
    // Progress animation
    public static void animateProgress(View progressView, int fromProgress, int toProgress) {
        ValueAnimator animator = ValueAnimator.ofInt(fromProgress, toProgress);
        animator.setDuration(DURATION_MEDIUM);
        animator.setInterpolator(new DecelerateInterpolator());
        
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            // Update progress - this will be implemented based on progress view type
            if (progressView instanceof android.widget.ProgressBar) {
                ((android.widget.ProgressBar) progressView).setProgress(animatedValue);
            }
        });
        
        animator.start();
    }
    
    // Shake animation for errors
    public static void shakeView(View view) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        shake.setDuration(600);
        shake.setInterpolator(new AccelerateDecelerateInterpolator());
        shake.start();
    }
    
    // Pulse animation
    public static void pulseView(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.8f, 1f);
        
        scaleX.setDuration(400);
        scaleY.setDuration(400);
        alpha.setDuration(400);
        
        scaleX.setRepeatCount(2);
        scaleY.setRepeatCount(2);
        alpha.setRepeatCount(2);
        
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.start();
    }
    
    // Chain animations
    public static void chainAnimations(Animator... animators) {
        if (animators.length == 0) return;
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animators);
        animatorSet.start();
    }
}