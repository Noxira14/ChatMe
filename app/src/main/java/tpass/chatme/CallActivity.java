package tpass.chatme;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.MaterialColors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import io.agora.rtc.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.Constants;
import io.agora.rtc.video.VideoCanvas;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CallActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private String channelName = "";
	private RtcEngine mRtcEngine;
	private LinearLayout localVideoContainer;
	private LinearLayout remoteVideoContainer;
	private String id = "";
	private boolean newCall = false;
	private String appId = "";
	
	private LinearLayout linear1;
	private LinearLayout linear3;
	private LinearLayout remote_video_container;
	private LinearLayout local_video_container;
	private TextView textview1;
	private Button join_button;
	private Button leave_button;
	
	private Intent a = new Intent();
	private DatabaseReference call = _firebase.getReference("call");
	private ChildEventListener _call_child_listener;
	private FirebaseAuth fauth;
	private OnCompleteListener<AuthResult> _fauth_create_user_listener;
	private OnCompleteListener<AuthResult> _fauth_sign_in_listener;
	private OnCompleteListener<Void> _fauth_reset_password_listener;
	private OnCompleteListener<Void> fauth_updateEmailListener;
	private OnCompleteListener<Void> fauth_updatePasswordListener;
	private OnCompleteListener<Void> fauth_emailVerificationSentListener;
	private OnCompleteListener<Void> fauth_deleteUserListener;
	private OnCompleteListener<Void> fauth_updateProfileListener;
	private OnCompleteListener<AuthResult> fauth_phoneAuthListener;
	private OnCompleteListener<AuthResult> fauth_googleSignInListener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.call);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear3 = findViewById(R.id.linear3);
		remote_video_container = findViewById(R.id.remote_video_container);
		local_video_container = findViewById(R.id.local_video_container);
		textview1 = findViewById(R.id.textview1);
		join_button = findViewById(R.id.join_button);
		leave_button = findViewById(R.id.leave_button);
		fauth = FirebaseAuth.getInstance();
		
		_call_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					_joinChannel();
					if (_childValue.containsKey("rec")) {
						
					} else {
						if (_childValue.containsKey("sen")) {
							if (_childValue.containsKey("username")) {
								textview1.setText("Waiting for ".concat(_childValue.get("username").toString().concat(" to Join..")));
							}
						} else {
							
						}
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		call.addChildEventListener(_call_child_listener);
		
		fauth_updateEmailListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_updatePasswordListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_emailVerificationSentListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_deleteUserListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_phoneAuthListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		fauth_updateProfileListener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		fauth_googleSignInListener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> task) {
				final boolean _success = task.isSuccessful();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_fauth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_fauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	
	private void initializeLogic() {
		id = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
		if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || 
		ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			
			// Request permissions
			ActivityCompat.requestPermissions(CallActivity.this, new String[]{
				Manifest.permission.RECORD_AUDIO, 
				Manifest.permission.WRITE_EXTERNAL_STORAGE
			}, 100);
			
		}
		
		
		
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("call");
			String userUuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
			
			ref.child(userUuid).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					if (snapshot.exists()) {
						
					} else {
						_leaveChannel();
						finish();
					}
				}
				
				@Override
				public void onCancelled(DatabaseError error) {
					showMessage("Error: " + error.getMessage());
				}
			});
			
		}
		join_button.setVisibility(View.GONE);
		leave_button.setVisibility(View.VISIBLE);
		appId = "029f6a3f325842d4b881b394845d65e3";
		channelName = getIntent().getStringExtra("channel");
		localVideoContainer = findViewById(R.id.local_video_container);
		remoteVideoContainer = findViewById(R.id.remote_video_container);
		
		try {
			mRtcEngine = RtcEngine.create(getApplicationContext(), appId, new IRtcEngineEventHandler() {
				@Override
				public void onUserJoined( final int uid, int elapsed) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							SurfaceView remoteView = RtcEngine.CreateRendererView(getApplicationContext());
							remoteVideoContainer.addView(remoteView);
							textview1.setVisibility(View.GONE);
							mRtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
						}
					});
					leave_button.setVisibility(View.VISIBLE);
					join_button.setVisibility(View.GONE);
					
				}
				
				//This App created by sunnymavale 
				
				@Override
				public void onUserOffline(int uid, int reason) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							remoteVideoContainer.removeAllViews();
							textview1.setVisibility(View.VISIBLE);
						}
					});
					
					finish();
					
				}
			});
			
			mRtcEngine.enableVideo();
			mRtcEngine.enableAudio();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		findViewById(R.id.join_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newCall = true;
				_joinChannel();
			}
		});
		
		findViewById(R.id.leave_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_leaveChannel();
			}
		});
		
		
	}
	
	public void _joinChannel() {
		String token = null;
		mRtcEngine.joinChannel(token, channelName, "", 0);
		
		final SurfaceView localView = RtcEngine.CreateRendererView(getApplicationContext());
		localVideoContainer.addView(localView);
		
		mRtcEngine.setupLocalVideo(new VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
		
		
		
	}
	
	
	public void _leaveChannel() {
		mRtcEngine.leaveChannel();
		
		
		localVideoContainer.removeAllViews();
		remoteVideoContainer.removeAllViews();
		
		
		
		
		call.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
		leave_button.setVisibility(View.GONE);
		join_button.setVisibility(View.GONE);
		finish();
	}
	
	
	public void _setupLocalVideo() {
		// Create a TextureView for local video
		TextureView localVideoView = new TextureView(this);
		localVideoContainer.addView(localVideoView);
		// Set up the local video stream
		mRtcEngine.setupLocalVideo(new VideoCanvas(localVideoView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
	}
	
	
	public void _lib() {
	}
	public void _setupRemoteVideo(final int _uid) {
		
		
		// Create a SurfaceView for remote video
		final SurfaceView remoteVideoView = RtcEngine.CreateRendererView(getApplicationContext());
		remoteVideoContainer.addView(remoteVideoView);
		//This App created by sunnymavale 
		// Set up the remote video stream
		VideoCanvas remoteCanvas = new VideoCanvas(remoteVideoView, VideoCanvas.RENDER_MODE_HIDDEN, _uid);
		mRtcEngine.setupRemoteVideo(remoteCanvas);
	}
	
	{
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}