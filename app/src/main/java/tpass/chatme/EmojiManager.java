package tpass.chatme;

import android.content.Context;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.bundled.BundledEmojiCompatConfig;

public class EmojiManager {
    private static EmojiManager instance;
    private boolean isInitialized = false;

    private EmojiManager() {}

    public static EmojiManager getInstance() {
        if (instance == null) {
            instance = new EmojiManager();
        }
        return instance;
    }

    public void initialize(Context context) {
        if (!isInitialized) {
            EmojiCompat.Config config = new BundledEmojiCompatConfig(context);
            EmojiCompat.init(config);
            isInitialized = true;
        }
    }

    public boolean isInitialized() {
        return isInitialized && EmojiCompat.get().getLoadState() == EmojiCompat.LOAD_STATE_SUCCEEDED;
    }

    public CharSequence processEmoji(CharSequence text) {
        if (isInitialized()) {
            return EmojiCompat.get().process(text);
        }
        return text;
    }

    // Common emoji categories for the picker
    public static final String[] RECENT_EMOJIS = {
        "😀", "😂", "❤️", "👍", "👎", "😊", "😢", "😡"
    };

    public static final String[] SMILEYS_EMOJIS = {
        "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣",
        "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰",
        "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜",
        "🤪", "🤨", "🧐", "🤓", "😎", "🤩", "🥳", "😏"
    };

    public static final String[] PEOPLE_EMOJIS = {
        "👋", "🤚", "🖐️", "✋", "🖖", "👌", "🤏", "✌️",
        "🤞", "🤟", "🤘", "🤙", "👈", "👉", "👆", "🖕",
        "👇", "☝️", "👍", "👎", "👊", "✊", "🤛", "🤜",
        "👏", "🙌", "👐", "🤲", "🤝", "🙏", "✍️", "💅"
    };

    public static final String[] NATURE_EMOJIS = {
        "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼",
        "🐨", "🐯", "🦁", "🐮", "🐷", "🐽", "🐸", "🐵",
        "🙈", "🙉", "🙊", "🐒", "🐔", "🐧", "🐦", "🐤",
        "🐣", "🐥", "🦆", "🦅", "🦉", "🦇", "🐺", "🐗"
    };

    public static final String[] FOOD_EMOJIS = {
        "🍎", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓",
        "🫐", "🍈", "🍒", "🍑", "🥭", "🍍", "🥥", "🥝",
        "🍅", "🍆", "🥑", "🥦", "🥬", "🥒", "🌶️", "🫑",
        "🌽", "🥕", "🫒", "🧄", "🧅", "🥔", "🍠", "🥐"
    };

    public static final String[] ACTIVITIES_EMOJIS = {
        "⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🏉",
        "🥏", "🎱", "🪀", "🏓", "🏸", "🏒", "🏑", "🥍",
        "🏏", "🪃", "🥅", "⛳", "🪁", "🏹", "🎣", "🤿",
        "🥊", "🥋", "🎽", "🛹", "🛷", "⛸️", "🥌", "🎿"
    };

    public static final String[] TRAVEL_EMOJIS = {
        "🚗", "🚕", "🚙", "🚌", "🚎", "🏎️", "🚓", "🚑",
        "🚒", "🚐", "🛻", "🚚", "🚛", "🚜", "🏍️", "🛵",
        "🚲", "🛴", "🛺", "🚨", "🚔", "🚍", "🚘", "🚖",
        "🚡", "🚠", "🚟", "🚃", "🚋", "🚞", "🚝", "🚄"
    };

    public static final String[] OBJECTS_EMOJIS = {
        "⌚", "📱", "📲", "💻", "⌨️", "🖥️", "🖨️", "🖱️",
        "🖲️", "🕹️", "🗜️", "💽", "💾", "💿", "📀", "📼",
        "📷", "📸", "📹", "🎥", "📽️", "🎞️", "📞", "☎️",
        "📟", "📠", "📺", "📻", "🎙️", "🎚️", "🎛️", "🧭"
    };

    public static final String[] SYMBOLS_EMOJIS = {
        "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍",
        "🤎", "💔", "❣️", "💕", "💞", "💓", "💗", "💖",
        "💘", "💝", "💟", "☮️", "✝️", "☪️", "🕉️", "☸️",
        "✡️", "🔯", "🕎", "☯️", "☦️", "🛐", "⛎", "♈"
    };

    public static final String[] FLAGS_EMOJIS = {
        "🏁", "🚩", "🎌", "🏴", "🏳️", "🏳️‍🌈", "🏳️‍⚧️", "🏴‍☠️",
        "🇦🇫", "🇦🇽", "🇦🇱", "🇩🇿", "🇦🇸", "🇦🇩", "🇦🇴", "🇦🇮",
        "🇦🇶", "🇦🇬", "🇦🇷", "🇦🇲", "🇦🇼", "🇦🇺", "🇦🇹", "🇦🇿",
        "🇧🇸", "🇧🇭", "🇧🇩", "🇧🇧", "🇧🇾", "🇧🇪", "🇧🇿", "🇧🇯"
    };
}
