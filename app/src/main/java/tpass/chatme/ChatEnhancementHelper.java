package tpass.chatme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatEnhancementHelper {
    
    // Message status constants
    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_DELIVERED = 2;
    public static final int STATUS_READ = 3;
    
    // Message types
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_FILE = 4;
    public static final int TYPE_LOCATION = 5;
    
    // Time formatting
    public static String formatMessageTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    public static String formatMessageDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    public static String getRelativeTime(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        
        if (diff < 60000) { // Less than 1 minute
            return "just now";
        } else if (diff < 3600000) { // Less than 1 hour
            return (diff / 60000) + "m ago";
        } else if (diff < 86400000) { // Less than 1 day
            return (diff / 3600000) + "h ago";
        } else if (diff < 604800000) { // Less than 1 week
            return (diff / 86400000) + "d ago";
        } else {
            return formatMessageDate(timestamp);
        }
    }
    
    // Message status color helper
    public static int getStatusColor(int status) {
        switch (status) {
            case STATUS_SENDING:
                return Color.parseColor("#9E9E9E");
            case STATUS_SENT:
                return Color.parseColor("#9E9E9E");
            case STATUS_DELIVERED:
                return Color.parseColor("#2196F3");
            case STATUS_READ:
                return Color.parseColor("#4CAF50");
            default:
                return Color.parseColor("#9E9E9E");
        }
    }
    
    // Enhanced text formatting for messages
    public static SpannableString formatMessageText(String text) {
        SpannableString spannable = new SpannableString(text);
        
        // Bold text pattern **text**
        Pattern boldPattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Matcher boldMatcher = boldPattern.matcher(text);
        while (boldMatcher.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    boldMatcher.start(), boldMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        // Italic text pattern *text*
        Pattern italicPattern = Pattern.compile("\\*(.*?)\\*");
        Matcher italicMatcher = italicPattern.matcher(text);
        while (italicMatcher.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
                    italicMatcher.start(), italicMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        // Code text pattern `code`
        Pattern codePattern = Pattern.compile("`(.*?)`");
        Matcher codeMatcher = codePattern.matcher(text);
        while (codeMatcher.find()) {
            spannable.setSpan(new android.text.style.TypefaceSpan("monospace"),
                    codeMatcher.start(), codeMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new BackgroundColorSpan(Color.parseColor("#F5F5F5")),
                    codeMatcher.start(), codeMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        // URLs
        Pattern urlPattern = Pattern.compile("(https?://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?)");
        Matcher urlMatcher = urlPattern.matcher(text);
        while (urlMatcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#2196F3")),
                    urlMatcher.start(), urlMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new android.text.style.UnderlineSpan(),
                    urlMatcher.start(), urlMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        return spannable;
    }
    
    // Create dynamic chat bubble background
    public static GradientDrawable createChatBubble(boolean isSender, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        
        if (isSender) {
            drawable.setColors(new int[]{color, adjustBrightness(color, 0.9f)});
            drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            drawable.setOrientation(GradientDrawable.Orientation.TL_BR);
            drawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 4, 4});
        } else {
            drawable.setColor(color);
            drawable.setCornerRadii(new float[]{20, 20, 20, 20, 4, 4, 20, 20});
        }
        
        return drawable;
    }
    
    // Adjust color brightness
    public static int adjustBrightness(int color, float factor) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        
        red = Math.round(red * factor);
        green = Math.round(green * factor);
        blue = Math.round(blue * factor);
        
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));
        
        return Color.rgb(red, green, blue);
    }
    
    // Message reactions helper
    public static class ReactionHelper {
        public static final String[] REACTIONS = {"❤️", "😂", "😮", "😢", "😡", "👍"};
        
        public static int getReactionColor(String reaction) {
            switch (reaction) {
                case "❤️": return Color.parseColor("#FF1744");
                case "😂": return Color.parseColor("#FFD54F");
                case "😮": return Color.parseColor("#FFA726");
                case "😢": return Color.parseColor("#64B5F6");
                case "😡": return Color.parseColor("#EF5350");
                case "👍": return Color.parseColor("#4CAF50");
                default: return Color.parseColor("#9E9E9E");
            }
        }
        
        public static void showReactionPopup(Context context, View anchorView, OnReactionSelectedListener listener) {
            // This would implement a popup showing reaction options
            // Implementation would depend on your popup library or custom implementation
        }
        
        public interface OnReactionSelectedListener {
            void onReactionSelected(String reaction);
        }
    }
    
    // Online status helper
    public static class OnlineStatusHelper {
        public static String getStatusText(boolean isOnline, long lastSeen) {
            if (isOnline) {
                return "online";
            } else {
                return "last seen " + getRelativeTime(lastSeen);
            }
        }
        
        public static int getStatusColor(boolean isOnline) {
            return isOnline ? Color.parseColor("#4CAF50") : Color.parseColor("#9E9E9E");
        }
        
        public static void updateStatusIndicator(View statusView, boolean isOnline) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(getStatusColor(isOnline));
            drawable.setSize(24, 24);
            statusView.setBackground(drawable);
        }
    }
    
    // Voice message helper
    public static class VoiceMessageHelper {
        public static String formatDuration(long durationMs) {
            long seconds = durationMs / 1000;
            long minutes = seconds / 60;
            seconds = seconds % 60;
            return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
        }
        
        public static void animateVoiceWave(ViewGroup waveContainer, boolean isPlaying) {
            if (isPlaying) {
                EnhancedAnimationHelper.animateVoiceWave(waveContainer);
            } else {
                // Stop animation
                for (int i = 0; i < waveContainer.getChildCount(); i++) {
                    waveContainer.getChildAt(i).clearAnimation();
                }
            }
        }
    }
    
    // File size helper
    public static String formatFileSize(long sizeInBytes) {
        if (sizeInBytes < 1024) {
            return sizeInBytes + " B";
        } else if (sizeInBytes < 1024 * 1024) {
            return String.format(Locale.getDefault(), "%.1f KB", sizeInBytes / 1024.0);
        } else if (sizeInBytes < 1024 * 1024 * 1024) {
            return String.format(Locale.getDefault(), "%.1f MB", sizeInBytes / (1024.0 * 1024.0));
        } else {
            return String.format(Locale.getDefault(), "%.1f GB", sizeInBytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    // Enhanced RecyclerView animation
    public static void animateRecyclerViewItems(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                // Animate visible items based on scroll direction
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View child = recyclerView.getChildAt(i);
                    float alpha = 1f - Math.abs(dy) / 1000f;
                    alpha = Math.max(0.3f, Math.min(1f, alpha));
                    child.setAlpha(alpha);
                }
            }
        });
    }
}