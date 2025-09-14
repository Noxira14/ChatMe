package tpass.chatme;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.RecyclerView;

public class InteractivityHelper {
    
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    
    public interface SwipeListener {
        void onSwipeLeft(View view, int position);
        void onSwipeRight(View view, int position);
    }
    
    public interface LongPressListener {
        void onLongPress(View view, int position);
    }
    
    public interface DoubleTapListener {
        void onDoubleTap(View view, int position);
    }
    
    // Swipe gesture detector for RecyclerView items
    public static class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private SwipeListener swipeListener;
        private View view;
        private int position;
        
        public SwipeGestureDetector(SwipeListener listener, View view, int position) {
            this.swipeListener = listener;
            this.view = view;
            this.position = position;
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null) return false;
            
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Right swipe
                        if (swipeListener != null) {
                            swipeListener.onSwipeRight(view, position);
                        }
                        return true;
                    } else {
                        // Left swipe
                        if (swipeListener != null) {
                            swipeListener.onSwipeLeft(view, position);
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    // Enhanced touch listener with multiple gestures
    public static class EnhancedTouchListener implements View.OnTouchListener {
        private GestureDetector gestureDetector;
        private SwipeListener swipeListener;
        private LongPressListener longPressListener;
        private DoubleTapListener doubleTapListener;
        private View view;
        private int position;
        
        public EnhancedTouchListener(Context context, View view, int position) {
            this.view = view;
            this.position = position;
            this.gestureDetector = new GestureDetector(context, new MultiGestureDetector());
        }
        
        public EnhancedTouchListener setSwipeListener(SwipeListener listener) {
            this.swipeListener = listener;
            return this;
        }
        
        public EnhancedTouchListener setLongPressListener(LongPressListener listener) {
            this.longPressListener = listener;
            return this;
        }
        
        public EnhancedTouchListener setDoubleTapListener(DoubleTapListener listener) {
            this.doubleTapListener = listener;
            return this;
        }
        
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
        
        private class MultiGestureDetector extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        // Haptic feedback
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        
                        if (diffX > 0) {
                            if (swipeListener != null) {
                                swipeListener.onSwipeRight(view, position);
                            }
                        } else {
                            if (swipeListener != null) {
                                swipeListener.onSwipeLeft(view, position);
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public void onLongPress(MotionEvent e) {
                // Haptic feedback for long press
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                
                if (longPressListener != null) {
                    longPressListener.onLongPress(view, position);
                }
            }
            
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Light haptic feedback for double tap
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                
                if (doubleTapListener != null) {
                    doubleTapListener.onDoubleTap(view, position);
                }
                return true;
            }
            
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                view.performClick();
                return true;
            }
        }
    }
    
    // Pull to refresh helper
    public static class PullToRefreshHelper {
        private RecyclerView recyclerView;
        private OnRefreshListener refreshListener;
        private boolean isRefreshing = false;
        private float initialY;
        private static final float REFRESH_THRESHOLD = 200f;
        
        public interface OnRefreshListener {
            void onRefresh();
        }
        
        public PullToRefreshHelper(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            setupPullToRefresh();
        }
        
        public void setOnRefreshListener(OnRefreshListener listener) {
            this.refreshListener = listener;
        }
        
        private void setupPullToRefresh() {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (isRefreshing) return false;
                    
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialY = event.getY();
                            break;
                            
                        case MotionEvent.ACTION_MOVE:
                            float currentY = event.getY();
                            float deltaY = currentY - initialY;
                            
                            // Only trigger if we're at the top of the list
                            if (deltaY > 0 && !recyclerView.canScrollVertically(-1)) {
                                if (deltaY > REFRESH_THRESHOLD) {
                                    triggerRefresh();
                                }
                            }
                            break;
                    }
                    return false;
                }
            });
        }
        
        private void triggerRefresh() {
            if (isRefreshing) return;
            
            isRefreshing = true;
            
            // Haptic feedback
            recyclerView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            
            if (refreshListener != null) {
                refreshListener.onRefresh();
            }
        }
        
        public void setRefreshing(boolean refreshing) {
            this.isRefreshing = refreshing;
        }
    }
    
    // Haptic feedback helper
    public static class HapticHelper {
        public static void lightImpact(View view) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        }
        
        public static void mediumImpact(View view) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            }
        }
        
        public static void heavyImpact(View view) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
        
        public static void customVibration(Context context, long duration) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(duration);
                }
            }
        }
        
        public static void successVibration(Context context) {
            customVibration(context, 50);
        }
        
        public static void errorVibration(Context context) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    long[] pattern = {0, 100, 50, 100};
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                } else {
                    long[] pattern = {0, 100, 50, 100};
                    vibrator.vibrate(pattern, -1);
                }
            }
        }
    }
    
    // Enhanced RecyclerView scroll listener
    public static class EnhancedScrollListener extends RecyclerView.OnScrollListener {
        private OnScrollStateListener scrollStateListener;
        private int visibleThreshold = 5;
        private boolean loading = false;
        
        public interface OnScrollStateListener {
            void onScrollUp();
            void onScrollDown();
            void onLoadMore();
            void onScrollToTop();
            void onScrollToBottom();
        }
        
        public EnhancedScrollListener setOnScrollStateListener(OnScrollStateListener listener) {
            this.scrollStateListener = listener;
            return this;
        }
        
        public EnhancedScrollListener setVisibleThreshold(int threshold) {
            this.visibleThreshold = threshold;
            return this;
        }
        
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            
            if (scrollStateListener == null) return;
            
            // Detect scroll direction
            if (dy > 0) {
                scrollStateListener.onScrollDown();
            } else if (dy < 0) {
                scrollStateListener.onScrollUp();
            }
            
            // Check if we're at top or bottom
            if (!recyclerView.canScrollVertically(-1)) {
                scrollStateListener.onScrollToTop();
            } else if (!recyclerView.canScrollVertically(1)) {
                scrollStateListener.onScrollToBottom();
                
                // Load more functionality
                if (!loading) {
                    loading = true;
                    scrollStateListener.onLoadMore();
                }
            }
        }
        
        public void setLoading(boolean loading) {
            this.loading = loading;
        }
    }
}