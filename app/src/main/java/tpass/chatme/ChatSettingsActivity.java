package tpass.chatme;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import java.text.*;
import java.util.*;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import android.app.Activity;
import android.app.ActivityOptions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ChatSettingsActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private String uuid = "";
	private String data = "";
	private String fontName = "";
	private String typeace = "";
	private String chatroom = "";
	private String chatcopy = "";
	private String userid = "";
	private String myid = "";
	private HashMap<String, Object> mo = new HashMap<>();
	
	private LinearLayout linear1;
	private LinearLayout line;
	private TextView textview2;
	private LinearLayout linear2;
	private ImageView block;
	private Switch switch1;
	
	private SharedPreferences file;
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
	private DatabaseReference inbox = _firebase.getReference("inbox");
	private ChildEventListener _inbox_child_listener;
	private DatabaseReference users = _firebase.getReference("users");
	private ChildEventListener _users_child_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.chat_settings);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		line = findViewById(R.id.line);
		textview2 = findViewById(R.id.textview2);
		linear2 = findViewById(R.id.linear2);
		block = findViewById(R.id.block);
		switch1 = findViewById(R.id.switch1);
		file = getSharedPreferences("f", Activity.MODE_PRIVATE);
		fauth = FirebaseAuth.getInstance();
		
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					block.setColorFilter(0xFFF44336, PorterDuff.Mode.MULTIPLY);
					_blockUser(uuid, FirebaseAuth.getInstance().getCurrentUser().getUid());
				} else {
					block.setColorFilter(0xFF292C31, PorterDuff.Mode.MULTIPLY);
					_Unblock(uuid, FirebaseAuth.getInstance().getCurrentUser().getUid());
				}
			}
		});
		
		_inbox_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
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
		inbox.addChildEventListener(_inbox_child_listener);
		
		_users_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
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
		users.addChildEventListener(_users_child_listener);
		
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
		_setCustomSwitchs(switch1);
		userid = getIntent().getStringExtra("user2");
		myid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		// Get Firebase reference to the specific user's data in "users" with dynamic block key
		DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(myid).child("block_" + userid);
		
		// Check if the dynamic "block_userUid" key exists
		userRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				// Set switch1 based on the existence of the "block_userUid" key in "users"
				switch1.setChecked(snapshot.exists());
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				// Handle any potential errors
				SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
			}
		});
		
		_changeActivityFont("fn1");
		uuid = getIntent().getStringExtra("user2");
		linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)36, 0xFFF5F5FF));
		line.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFF9E9E9E));
	}
	
	public void _changeActivityFont(final String _fontname) {
		fontName = "fonts/".concat(_fontname.concat(".ttf"));
		overrideFonts(this,getWindow().getDecorView()); 
	} 
	private void overrideFonts(final android.content.Context context, final View v) {
		
		try {
			Typeface 
			typeace = Typeface.createFromAsset(getAssets(), fontName);;
			if ((v instanceof ViewGroup)) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0;
				i < vg.getChildCount();
				i++) {
					View child = vg.getChildAt(i);
					overrideFonts(context, child);
				}
			} else {
				if ((v instanceof TextView)) {
					((TextView) v).setTypeface(typeace);
				} else {
					if ((v instanceof EditText )) {
						((EditText) v).setTypeface(typeace);
					} else {
						if ((v instanceof Button)) {
							((Button) v).setTypeface(typeace);
						}
					}
				}
			}
		}
		catch(Exception e)
		
		{
			SketchwareUtil.showMessage(getApplicationContext(), "Error Loading Font");
		};
	}
	
	
	public void _blockUser(final String _userUid, final String _myUid) {
		DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
		
		
		DatabaseReference blockRef = usersRef.child(_myUid);
		
		Map<String, Object> blockData = new HashMap<>();
		blockData.put("block_" + _userUid, "true");  // Set to true to indicate the user is blocked
		
		blockRef.updateChildren(blockData).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					SketchwareUtil.showMessage(getApplicationContext(), "User Blocked");
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Failed to add block key");
				}
			}
		});
		
		
		// Get current user's UID
		final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		
		// Firebase references
		
		final DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference("inbox");
		
		// Fetch the current user's data
		usersRef.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot myDataSnapshot) {
				if (myDataSnapshot.exists()) {
					// Store user's data to be copied
					final Map<String, Object> myData = (Map<String, Object>) myDataSnapshot.getValue();
					
					// Iterate over each user's inbox to find occurrences of myUid
					inboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot inboxSnapshot) {
							for (DataSnapshot userInbox : inboxSnapshot.getChildren()) {
								final String userUid = userInbox.getKey();  // Marked as final
								if (userInbox.hasChild(myUid)) {
									// Copy myUid's data to this user's inbox
									inboxRef.child(userUid).child(myUid)
									.setValue(myData)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if (task.isSuccessful()) {
												
											} else {
												SketchwareUtil.showMessage(getApplicationContext(), "Failed to copy data to " + userUid + "'s inbox");
											}
										}
									});
								}
							}
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							SketchwareUtil.showMessage(getApplicationContext(), "Error: " + databaseError.getMessage());
						}
					});
				} else {
					
					
					SketchwareUtil.showMessage(getApplicationContext(), "Failed to fetch your data.");
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
			}
		});
		
	}
	
	
	public void _Unblock(final String _userUid, final String _myUid) {
		DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
		
		
		DatabaseReference blockRef = usersRef.child(_myUid).child("block_" + _userUid);
		
		blockRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					SketchwareUtil.showMessage(getApplicationContext(), "Unblocked");
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Failed to remove block key");
				}
			}
		});
		
		// Get current user's UID
		final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		
		// Firebase references
		
		final DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference("inbox");
		
		// Fetch the current user's data
		usersRef.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot myDataSnapshot) {
				if (myDataSnapshot.exists()) {
					// Store user's data to be copied
					final Map<String, Object> myData = (Map<String, Object>) myDataSnapshot.getValue();
					
					// Iterate over each user's inbox to find occurrences of myUid
					inboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot inboxSnapshot) {
							for (DataSnapshot userInbox : inboxSnapshot.getChildren()) {
								final String userUid = userInbox.getKey();  // Marked as final
								if (userInbox.hasChild(myUid)) {
									// Copy myUid's data to this user's inbox
									inboxRef.child(userUid).child(myUid)
									.setValue(myData)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if (task.isSuccessful()) {
												
											} else {
												SketchwareUtil.showMessage(getApplicationContext(), "Failed to copy data to " + userUid + "'s inbox");
											}
										}
									});
								}
							}
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							SketchwareUtil.showMessage(getApplicationContext(), "Error: " + databaseError.getMessage());
						}
					});
				} else {
					
					
					SketchwareUtil.showMessage(getApplicationContext(), "Failed to fetch your data.");
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
			}
		});
		
	}
	
	
	public void _setCustomSwitchs(final Switch _switch1) {
		_switch1.setThumbDrawable(getResources().getDrawable(R.drawable.thumb_switch));
		_switch1.setTrackDrawable(getResources().getDrawable(R.drawable.track_switch));
		
		_switch1.setPadding(3, 3, 3, 3);
	}
	
	
	public void _convertToBottomSheet() {
	}
	private androidx.coordinatorlayout.widget.CoordinatorLayout mCoordinatorLayout;
	@Override
	public void finish(){
		com.google.android.material.bottomsheet.BottomSheetBehavior.from(mCoordinatorLayout.getChildAt(0)).setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED);
	}
	private void superFinish(){
		super.finish();
	}
	@Override
	public void setContentView(int layId){
		if(mCoordinatorLayout == null){
			overridePendingTransition(0,0);
			mCoordinatorLayout = new androidx.coordinatorlayout.widget.CoordinatorLayout(this);
			makeActivityTransparent();
			mCoordinatorLayout.setBackgroundColor(0x80000000);
			mCoordinatorLayout.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick (View v){
					finish();
				}
			});
		}
		mCoordinatorLayout.removeAllViews();
		androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams params = new androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		final com.google.android.material.bottomsheet.BottomSheetBehavior behavior = new com.google.android.material.bottomsheet.BottomSheetBehavior();
		params.setBehavior(behavior);
		behavior.setBottomSheetCallback(new com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback(){
			@Override
			public void onSlide(View bottomSheet, float slideOffset){
				
			}
			@Override
			public void onStateChanged(View bottomSheet, int newState){
				if(newState == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED){
					superFinish();
					overridePendingTransition(0,0);
				}
			}
		});
		View inflated = getLayoutInflater().inflate(layId,null);	
		mCoordinatorLayout.addView(inflated,params);
		
		if(mCoordinatorLayout.getParent()!= null)((ViewGroup)mCoordinatorLayout.getParent()).removeView(mCoordinatorLayout);
		setContentView(mCoordinatorLayout);
		inflated.post(new Runnable(){
			@Override
			public void run() {
				behavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED);
			}});
		
	}
	
	private void makeActivityTransparent(){
		getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
		try {
			java.lang.reflect.Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions"); 
			getActivityOptions.setAccessible(true);
			Object options = getActivityOptions.invoke(this);
			Class<?>[] classes = Activity.class.getDeclaredClasses();
			Class<?> translucentConversionListenerClazz = null;
			for (Class clazz : classes) { 
				if (clazz.getSimpleName().contains("TranslucentConversionListener")) { 
					translucentConversionListenerClazz = clazz;
				} 
			} 
			java.lang.reflect.Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class); 
			convertToTranslucent.setAccessible(true); 
			convertToTranslucent.invoke(this, null, options); 
		} catch (Throwable t) {
		}
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