package tpass.chatme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class AnimationHelper {
    
    public static final int DURATION_SHORT = 200;
    public static final int DURATION_MEDIUM = 300;
    public static final int DURATION_LONG = 500;
    
    // Fade animations
    public static void fadeIn(View view) {
        fadeIn(view, DURATION_MEDIUM, null);
    }
    
    public static void fadeIn(View view, int duration, Animator.AnimatorListener listener) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
    }
    
    public static void fadeOut(View view) {
        fadeOut(view, DURATION_MEDIUM, null);
    }
    
    public static void fadeOut(View view, int duration, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }
        });
        animator.start();
    }
    
    // Scale animations
    public static void scaleIn(View view) {
        scaleIn(view, DURATION_SHORT, null);
    }
    
    public static void scaleIn(View view, int duration, Animator.AnimatorListener listener) {
        view.setScaleX(0.95f);
        view.setScaleY(0.95f);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        alpha.setDuration(duration);
        
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleY.setInterpolator(new DecelerateInterpolator());
        alpha.setInterpolator(new DecelerateInterpolator());
        
        if (listener != null) {
            scaleX.addListener(listener);
        }
        
        scaleX.start();
        scaleY.start();
        alpha.start();
    }
    
    public static void scaleOut(View view) {
        scaleOut(view, DURATION_SHORT, null);
    }
    
    public static void scaleOut(View view, int duration, Animator.AnimatorListener listener) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        alpha.setDuration(duration);
        
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        alpha.setInterpolator(new AccelerateDecelerateInterpolator());
        
        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }
        });
        
        scaleX.start();
        scaleY.start();
        alpha.start();
    }
    
    // Slide animations
    public static void slideInFromBottom(View view) {
        slideInFromBottom(view, DURATION_MEDIUM, null);
    }
    
    public static void slideInFromBottom(View view, int duration, Animator.AnimatorListener listener) {
        view.setTranslationY(view.getHeight());
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0f);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.start();
    }
    
    public static void slideOutToBottom(View view) {
        slideOutToBottom(view, DURATION_MEDIUM, null);
    }
    
    public static void slideOutToBottom(View view, int duration, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0f, view.getHeight());
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }
        });
        animator.start();
    }
    
    // Message send animation
    public static void animateMessageSend(View view) {
        animateMessageSend(view, null);
    }
    
    public static void animateMessageSend(View view, Animator.AnimatorListener listener) {
        view.setTranslationY(20f);
        view.setScaleX(0.95f);
        view.setScaleY(0.95f);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", 20f, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        
        translateY.setDuration(DURATION_SHORT);
        scaleX.setDuration(DURATION_SHORT);
        scaleY.setDuration(DURATION_SHORT);
        alpha.setDuration(150);
        
        translateY.setInterpolator(new DecelerateInterpolator());
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleY.setInterpolator(new DecelerateInterpolator());
        alpha.setInterpolator(new DecelerateInterpolator());
        
        if (listener != null) {
            translateY.addListener(listener);
        }
        
        translateY.start();
        scaleX.start();
        scaleY.start();
        alpha.start();
    }
    
    // Ripple effect simulation
    public static void createRippleEffect(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f);
        
        scaleX.setDuration(150);
        scaleY.setDuration(150);
        
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        
        scaleX.start();
        scaleY.start();
    }
}
