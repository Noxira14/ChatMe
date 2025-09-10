package tpass.chatme;

import android.content.Context;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieListener;

public class LottieHelper {
    
    // Animation file names
    public static final String LOGIN_ANIMATION = "lottie/Login.json";
    public static final String SIGNUP_ANIMATION = "lottie/SignUp.json";
    public static final String EMAIL_WAITING_ANIMATION = "lottie/Email.json";
    public static final String EMAIL_SUCCESS_ANIMATION = "lottie/email-success.json";
    public static final String LOADING_ANIMATION = "lottie/load.json";
    public static final String GRADIENT_LOADER_ANIMATION = "lottie/gradient loader.json";
    public static final String CALL_CENTER_ANIMATION = "lottie/Call Center Support.json";
    public static final String BUSINESS_STATS_ANIMATION = "lottie/Woman discovering business statistics.json";
    
    /**
     * Load and play a Lottie animation from assets
     */
    public static void loadAndPlayAnimation(Context context, LottieAnimationView animationView, 
                                          String fileName, boolean loop, boolean autoPlay) {
        LottieCompositionFactory.fromAsset(context, fileName)
            .addListener(new LottieListener<LottieComposition>() {
                @Override
                public void onResult(LottieComposition composition) {
                    animationView.setComposition(composition);
                    animationView.setRepeatCount(loop ? -1 : 0);
                    if (autoPlay) {
                        animationView.playAnimation();
                    }
                }
            })
            .addFailureListener(new LottieListener<Throwable>() {
                @Override
                public void onResult(Throwable throwable) {
                    // Handle error - maybe show a fallback or hide the view
                    animationView.setVisibility(android.view.View.GONE);
                }
            });
    }
    
    /**
     * Setup login screen animation
     */
    public static void setupLoginAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, LOGIN_ANIMATION, true, true);
    }
    
    /**
     * Setup signup screen animation
     */
    public static void setupSignupAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, SIGNUP_ANIMATION, true, true);
    }
    
    /**
     * Setup email waiting animation
     */
    public static void setupEmailWaitingAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, EMAIL_WAITING_ANIMATION, true, true);
    }
    
    /**
     * Setup email success animation
     */
    public static void setupEmailSuccessAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, EMAIL_SUCCESS_ANIMATION, false, true);
    }
    
    /**
     * Setup loading animation
     */
    public static void setupLoadingAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, LOADING_ANIMATION, true, true);
    }
    
    /**
     * Setup gradient loader animation
     */
    public static void setupGradientLoaderAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, GRADIENT_LOADER_ANIMATION, true, true);
    }
    
    /**
     * Setup empty state animation (call center support)
     */
    public static void setupEmptyStateAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, CALL_CENTER_ANIMATION, true, true);
    }
    
    /**
     * Setup business statistics animation
     */
    public static void setupBusinessStatsAnimation(Context context, LottieAnimationView animationView) {
        loadAndPlayAnimation(context, animationView, BUSINESS_STATS_ANIMATION, true, true);
    }
    
    /**
     * Preload animations for better performance
     */
    public static void preloadAnimations(Context context) {
        // Preload commonly used animations
        LottieCompositionFactory.fromAsset(context, LOGIN_ANIMATION);
        LottieCompositionFactory.fromAsset(context, LOADING_ANIMATION);
        LottieCompositionFactory.fromAsset(context, EMAIL_WAITING_ANIMATION);
        LottieCompositionFactory.fromAsset(context, CALL_CENTER_ANIMATION);
    }
    
    /**
     * Start animation with fade in effect
     */
    public static void startAnimationWithFadeIn(LottieAnimationView animationView) {
        animationView.setAlpha(0f);
        animationView.setVisibility(android.view.View.VISIBLE);
        animationView.animate()
            .alpha(1f)
            .setDuration(300)
            .withEndAction(() -> animationView.playAnimation())
            .start();
    }
    
    /**
     * Stop animation with fade out effect
     */
    public static void stopAnimationWithFadeOut(LottieAnimationView animationView) {
        animationView.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction(() -> {
                animationView.pauseAnimation();
                animationView.setVisibility(android.view.View.GONE);
            })
            .start();
    }
}
