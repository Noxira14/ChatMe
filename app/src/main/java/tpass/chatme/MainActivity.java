package tpass.chatme;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private final FirebaseAuth fauth = FirebaseAuth.getInstance();

    private TextView dot1, dot2, dot3;
    private ImageView tpass;
    private TextView loadingStatus;
    private LottieAnimationView lottieLoading;
    private LottieAnimationView lottieLogo;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        initialize(_savedInstanceState);
        FirebaseApp.initializeApp(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {
            initializeLogic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            initializeLogic();
        }
    }

    private void initialize(Bundle _savedInstanceState) {
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        tpass = findViewById(R.id.tpass);
        
        // Enhanced elements - check if they exist first
        loadingStatus = findViewById(R.id.loading_status);
        lottieLoading = findViewById(R.id.lottie_loading);
        lottieLogo = findViewById(R.id.lottie_logo);
    }

    private void initializeLogic() {
        _enhancedLoading();
        _enhancedTpassAnimation();
        _updateLoadingStatus("Initializing ChatMe...");
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            _updateLoadingStatus("Connecting to servers...");
        }, 800);
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            _updateLoadingStatus("Almost ready...");
        }, 1600);
        
        new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus, 2200);
    }

    private void checkLoginStatus() {
        FirebaseUser currentUser = fauth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = _firebase.getReference("users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), LogActivity.class));
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LogActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(getApplicationContext(), LogActivity.class));
            finish();
        }
    }

    private void _enhancedLoading() {
        // Start Lottie animation if available
        if (lottieLoading != null) {
            lottieLoading.setVisibility(android.view.View.VISIBLE);
            lottieLoading.playAnimation();
        }
        
        // Enhanced dot animation with better timing
        EnhancedAnimationHelper.animateTypingIndicator(dot1, dot2, dot3);
    }
    
    private void _updateLoadingStatus(String status) {
        if (loadingStatus != null) {
            EnhancedAnimationHelper.animateTextReveal(loadingStatus, status);
        }
    }

    private void _enhancedTpassAnimation() {
        // Enhanced logo animation with spring effect
        if (lottieLogo != null) {
            lottieLogo.setVisibility(android.view.View.VISIBLE);
            lottieLogo.playAnimation();
        }
        
        // Enhanced tpass animation with better easing
        EnhancedAnimationHelper.fadeInWithSpring(tpass, 2000, null);
    }
}


