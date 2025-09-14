package tpass.chatme;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EnhancedChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;
    private static final int VIEW_TYPE_DATE_SEPARATOR = 3;
    
    private Context context;
    private ArrayList<HashMap<String, Object>> messages;
    private String currentUserId;
    private OnMessageClickListener messageClickListener;
    private OnMessageLongClickListener messageLongClickListener;
    
    public interface OnMessageClickListener {
        void onMessageClick(HashMap<String, Object> message, int position);
        void onImageClick(String imageUrl);
        void onReactionClick(HashMap<String, Object> message, String reaction);
    }
    
    public interface OnMessageLongClickListener {
        void onMessageLongClick(HashMap<String, Object> message, int position, View view);
    }
    
    public EnhancedChatAdapter(Context context, ArrayList<HashMap<String, Object>> messages, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }
    
    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.messageClickListener = listener;
    }
    
    public void setOnMessageLongClickListener(OnMessageLongClickListener listener) {
        this.messageLongClickListener = listener;
    }
    
    @Override
    public int getItemViewType(int position) {
        HashMap<String, Object> message = messages.get(position);
        String messageType = (String) message.get("type");
        
        if ("date_separator".equals(messageType)) {
            return VIEW_TYPE_DATE_SEPARATOR;
        }
        
        String senderId = (String) message.get("userId");
        return currentUserId.equals(senderId) ? VIEW_TYPE_SENDER : VIEW_TYPE_RECEIVER;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        
        switch (viewType) {
            case VIEW_TYPE_SENDER:
                return new SenderViewHolder(inflater.inflate(R.layout.message_sender_enhanced, parent, false));
            case VIEW_TYPE_RECEIVER:
                return new ReceiverViewHolder(inflater.inflate(R.layout.message_receiver_enhanced, parent, false));
            case VIEW_TYPE_DATE_SEPARATOR:
                return new DateSeparatorViewHolder(inflater.inflate(R.layout.date_separator, parent, false));
            default:
                return new SenderViewHolder(inflater.inflate(R.layout.message_sender_enhanced, parent, false));
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap<String, Object> message = messages.get(position);
        
        if (holder instanceof SenderViewHolder) {
            bindSenderMessage((SenderViewHolder) holder, message, position);
        } else if (holder instanceof ReceiverViewHolder) {
            bindReceiverMessage((ReceiverViewHolder) holder, message, position);
        } else if (holder instanceof DateSeparatorViewHolder) {
            bindDateSeparator((DateSeparatorViewHolder) holder, message);
        }
        
        // Animate item appearance
        EnhancedAnimationHelper.animateMessageSendEnhanced(holder.itemView);
    }
    
    private void bindSenderMessage(SenderViewHolder holder, HashMap<String, Object> message, int position) {
        String messageText = (String) message.get("message");
        String timestamp = (String) message.get("timestamp");
        String messageType = (String) message.get("messageType");
        int status = getMessageStatus(message);
        
        // Set message text with formatting
        if (messageText != null) {
            holder.messageText.setText(ChatEnhancementHelper.formatMessageText(messageText));
        }
        
        // Set timestamp
        if (timestamp != null) {
            holder.timestamp.setText(ChatEnhancementHelper.formatMessageTime(Long.parseLong(timestamp)));
        }
        
        // Set message status
        updateMessageStatus(holder.statusIcon, status);
        
        // Handle different message types
        handleMessageType(holder, message, messageType);
        
        // Setup interactions
        setupMessageInteractions(holder.itemView, message, position);
        
        // Setup swipe for reply
        setupSwipeGesture(holder.itemView, message, position);
    }
    
    private void bindReceiverMessage(ReceiverViewHolder holder, HashMap<String, Object> message, int position) {
        String messageText = (String) message.get("message");
        String timestamp = (String) message.get("timestamp");
        String messageType = (String) message.get("messageType");
        String senderName = (String) message.get("senderName");
        String senderImage = (String) message.get("senderImage");
        
        // Set message text with formatting
        if (messageText != null) {
            holder.messageText.setText(ChatEnhancementHelper.formatMessageText(messageText));
        }
        
        // Set timestamp
        if (timestamp != null) {
            holder.timestamp.setText(ChatEnhancementHelper.formatMessageTime(Long.parseLong(timestamp)));
        }
        
        // Set sender info
        if (senderName != null) {
            holder.senderName.setText(senderName);
        }
        
        // Load sender image
        if (senderImage != null && !senderImage.isEmpty()) {
            Glide.with(context)
                .load(senderImage)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.drawable.banned_user)
                .into(holder.senderImage);
        }
        
        // Handle different message types
        handleMessageType(holder, message, messageType);
        
        // Setup interactions
        setupMessageInteractions(holder.itemView, message, position);
        
        // Setup swipe for reply
        setupSwipeGesture(holder.itemView, message, position);
    }
    
    private void bindDateSeparator(DateSeparatorViewHolder holder, HashMap<String, Object> message) {
        String dateString = (String) message.get("date");
        if (dateString != null) {
            holder.dateText.setText(dateString);
        }
    }
    
    private void handleMessageType(RecyclerView.ViewHolder holder, HashMap<String, Object> message, String messageType) {
        if ("image".equals(messageType)) {
            // Handle image messages
            handleImageMessage(holder, message);
        } else if ("voice".equals(messageType)) {
            // Handle voice messages
            handleVoiceMessage(holder, message);
        } else if ("file".equals(messageType)) {
            // Handle file messages
            handleFileMessage(holder, message);
        }
    }
    
    private void handleImageMessage(RecyclerView.ViewHolder holder, HashMap<String, Object> message) {
        String imageUrl = (String) message.get("imageUrl");
        if (imageUrl != null && holder instanceof SenderViewHolder) {
            SenderViewHolder senderHolder = (SenderViewHolder) holder;
            senderHolder.messageImage.setVisibility(View.VISIBLE);
            
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(senderHolder.messageImage);
                
            senderHolder.messageImage.setOnClickListener(v -> {
                if (messageClickListener != null) {
                    messageClickListener.onImageClick(imageUrl);
                }
            });
        }
    }
    
    private void handleVoiceMessage(RecyclerView.ViewHolder holder, HashMap<String, Object> message) {
        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderHolder = (SenderViewHolder) holder;
            senderHolder.voiceContainer.setVisibility(View.VISIBLE);
            
            String duration = (String) message.get("voiceDuration");
            if (duration != null) {
                senderHolder.voiceDuration.setText(ChatEnhancementHelper.VoiceMessageHelper.formatDuration(Long.parseLong(duration)));
            }
            
            // Setup voice wave animation
            ChatEnhancementHelper.VoiceMessageHelper.animateVoiceWave(senderHolder.voiceWaveContainer, false);
        }
    }
    
    private void handleFileMessage(RecyclerView.ViewHolder holder, HashMap<String, Object> message) {
        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderHolder = (SenderViewHolder) holder;
            senderHolder.fileContainer.setVisibility(View.VISIBLE);
            
            String fileName = (String) message.get("fileName");
            String fileSize = (String) message.get("fileSize");
            
            if (fileName != null) {
                senderHolder.fileName.setText(fileName);
            }
            
            if (fileSize != null) {
                senderHolder.fileSize.setText(ChatEnhancementHelper.formatFileSize(Long.parseLong(fileSize)));
            }
        }
    }
    
    private void setupMessageInteractions(View itemView, HashMap<String, Object> message, int position) {
        // Setup enhanced touch listener
        InteractivityHelper.EnhancedTouchListener touchListener = 
            new InteractivityHelper.EnhancedTouchListener(context, itemView, position);
        
        touchListener.setLongPressListener((view, pos) -> {
            if (messageLongClickListener != null) {
                messageLongClickListener.onMessageLongClick(message, pos, view);
            }
        });
        
        touchListener.setDoubleTapListener((view, pos) -> {
            // Handle double tap for quick reaction
            if (messageClickListener != null) {
                messageClickListener.onReactionClick(message, "❤️");
            }
        });
        
        itemView.setOnTouchListener(touchListener);
    }
    
    private void setupSwipeGesture(View itemView, HashMap<String, Object> message, int position) {
        InteractivityHelper.SwipeGestureDetector swipeDetector = 
            new InteractivityHelper.SwipeGestureDetector(
                new InteractivityHelper.SwipeListener() {
                    @Override
                    public void onSwipeLeft(View view, int pos) {
                        // Handle swipe left (maybe delete)
                    }
                    
                    @Override
                    public void onSwipeRight(View view, int pos) {
                        // Handle swipe right (reply)
                        if (messageClickListener != null) {
                            messageClickListener.onMessageClick(message, pos);
                        }
                    }
                }, 
                itemView, 
                position
            );
    }
    
    private int getMessageStatus(HashMap<String, Object> message) {
        String status = (String) message.get("status");
        if (status != null) {
            switch (status) {
                case "sending": return ChatEnhancementHelper.STATUS_SENDING;
                case "sent": return ChatEnhancementHelper.STATUS_SENT;
                case "delivered": return ChatEnhancementHelper.STATUS_DELIVERED;
                case "read": return ChatEnhancementHelper.STATUS_READ;
            }
        }
        return ChatEnhancementHelper.STATUS_SENT;
    }
    
    private void updateMessageStatus(ImageView statusIcon, int status) {
        statusIcon.setColorFilter(ChatEnhancementHelper.getStatusColor(status));
        
        // Animate status change
        EnhancedAnimationHelper.pulseView(statusIcon);
    }
    
    @Override
    public int getItemCount() {
        return messages.size();
    }
    
    // ViewHolder classes
    static class SenderViewHolder extends RecyclerView.ViewHolder {
        CardView messageCard;
        TextView messageText;
        TextView timestamp;
        ImageView statusIcon;
        ImageView messageImage;
        LinearLayout voiceContainer;
        TextView voiceDuration;
        ViewGroup voiceWaveContainer;
        LinearLayout fileContainer;
        TextView fileName;
        TextView fileSize;
        LinearLayout reactionsContainer;
        
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            messageCard = itemView.findViewById(R.id.message_card);
            messageText = itemView.findViewById(R.id.message_text);
            timestamp = itemView.findViewById(R.id.timestamp);
            statusIcon = itemView.findViewById(R.id.status_icon);
            messageImage = itemView.findViewById(R.id.message_image);
            voiceContainer = itemView.findViewById(R.id.voice_container);
            voiceDuration = itemView.findViewById(R.id.voice_duration);
            voiceWaveContainer = itemView.findViewById(R.id.voice_wave_container);
            fileContainer = itemView.findViewById(R.id.file_container);
            fileName = itemView.findViewById(R.id.file_name);
            fileSize = itemView.findViewById(R.id.file_size);
            reactionsContainer = itemView.findViewById(R.id.reactions_container);
        }
    }
    
    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CardView messageCard;
        CircleImageView senderImage;
        TextView senderName;
        TextView messageText;
        TextView timestamp;
        ImageView messageImage;
        LinearLayout voiceContainer;
        TextView voiceDuration;
        ViewGroup voiceWaveContainer;
        LinearLayout fileContainer;
        TextView fileName;
        TextView fileSize;
        LinearLayout reactionsContainer;
        
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            messageCard = itemView.findViewById(R.id.message_card);
            senderImage = itemView.findViewById(R.id.sender_image);
            senderName = itemView.findViewById(R.id.sender_name);
            messageText = itemView.findViewById(R.id.message_text);
            timestamp = itemView.findViewById(R.id.timestamp);
            messageImage = itemView.findViewById(R.id.message_image);
            voiceContainer = itemView.findViewById(R.id.voice_container);
            voiceDuration = itemView.findViewById(R.id.voice_duration);
            voiceWaveContainer = itemView.findViewById(R.id.voice_wave_container);
            fileContainer = itemView.findViewById(R.id.file_container);
            fileName = itemView.findViewById(R.id.file_name);
            fileSize = itemView.findViewById(R.id.file_size);
            reactionsContainer = itemView.findViewById(R.id.reactions_container);
        }
    }
    
    static class DateSeparatorViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        
        public DateSeparatorViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
        }
    }
}