package tpass.chatme;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic unit test for AnimationHelper class
 */
public class AnimationHelperTest {
    
    @Test
    public void testAnimationConstants() {
        // Test that animation duration constants are properly defined
        assertEquals(200, AnimationHelper.DURATION_SHORT);
        assertEquals(300, AnimationHelper.DURATION_MEDIUM);
        assertEquals(500, AnimationHelper.DURATION_LONG);
    }
    
    @Test
    public void testAnimationHelperExists() {
        // Test that AnimationHelper class can be instantiated
        AnimationHelper helper = new AnimationHelper();
        assertNotNull(helper);
    }
}
