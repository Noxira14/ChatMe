package tpass.chatme;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class ImageshowActivity extends AppCompatActivity {
	
	public final int REQ_CD_FILE = 101;
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private String path = "";
	private String filename = "";
	private String enc_restore = "";
	private String decrypted = "";
	private String chatroom = "";
	private String chatcopy = "";
	private String key = "";
	private HashMap<String, Object> chatmap = new HashMap<>();
	private String chat_key = "";
	
	private LinearLayout linear2;
	private LinearLayout linear1;
	private ImageView back;
	private LinearLayout linear3;
	private ImageView download;
	private ProgressBar progressbar1;
	private ImageView imageview1;
	
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
	private RequestNetwork net;
	private RequestNetwork.RequestListener _net_request_listener;
	private Intent file = new Intent(Intent.ACTION_GET_CONTENT);
	private DatabaseReference Chat1 = _firebase.getReference("+ chatroom +");
	private ChildEventListener _Chat1_child_listener;
	private DatabaseReference Chat2 = _firebase.getReference("+ chatcopy +");
	private ChildEventListener _Chat2_child_listener;
	private Calendar timest = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.imageshow);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear2 = findViewById(R.id.linear2);
		linear1 = findViewById(R.id.linear1);
		back = findViewById(R.id.back);
		linear3 = findViewById(R.id.linear3);
		download = findViewById(R.id.download);
		progressbar1 = findViewById(R.id.progressbar1);
		imageview1 = findViewById(R.id.imageview1);
		fauth = FirebaseAuth.getInstance();
		net = new RequestNetwork(this);
		file.setType("*/*");
		file.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if ("true".equals(getIntent().getStringExtra("enc"))) {
					
					// Prepare chat message map
					chatmap = new HashMap<>(); 
					chatmap.put("sender", "user"); 
					chatmap.put("re", getIntent().getStringExtra("user2")); 
					chatmap.put("key", chat_key); 
					chatmap.put("prt_open", "true");
					chatmap.put("p", "Sent a image [PRT]"); 
					chatmap.put("status", "uns"); 
					chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
					chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
					chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
					// Update message in both Chat1 and Chat2
					Chat1.child(chat_key).updateChildren(chatmap); 
					Chat2.child(chat_key).updateChildren(chatmap); 
					// Clear chat map and reset input field
					chatmap.clear(); 
					
				} else {
					finish();
				}
			}
		});
		
		download.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		_net_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
		
		_Chat1_child_listener = new ChildEventListener() {
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
				finish();
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
		Chat1.addChildEventListener(_Chat1_child_listener);
		
		_Chat2_child_listener = new ChildEventListener() {
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
		Chat2.addChildEventListener(_Chat2_child_listener);
		
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
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			Window w =ImageshowActivity.this.getWindow();
			w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF212121);
		}
		if (SketchwareUtil.isConnected(getApplicationContext())) {
			chat_key = getIntent().getStringExtra("v1");
			key = getIntent().getStringExtra("v1");
			_chatrooms();
			if ("true".equals(getIntent().getStringExtra("enc"))) {
				_DecryptedStringKey(getIntent().getStringExtra("url"), key);
				_blockScreenshot();
				progressbar1.setVisibility(View.GONE);
				download.setVisibility(View.GONE);
			} else {
				Glide.with(getApplicationContext()).load(Uri.parse(getIntent().getStringExtra("url"))).into(imageview1);
				progressbar1.setVisibility(View.GONE);
			}
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "error: No Internet Connection!");
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_FILE:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				path = _filePath.get((int)(0));
				filename = Uri.parse(_filePath.get((int)(0))).getLastPathSegment();
				if (filename.endsWith(".enc")) {
					_unlock_with_file(path, filename);
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Unsupported file type!");
				}
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if ("true".equals(getIntent().getStringExtra("enc"))) {
			
			
			// Prepare chat message map
			chatmap = new HashMap<>(); 
			chatmap.put("sender", "user"); 
			chatmap.put("re", getIntent().getStringExtra("user2")); 
			chatmap.put("key", chat_key); 
			chatmap.put("prt_open", "true");
			chatmap.put("p", "Sent a image [PRT]"); 
			chatmap.put("status", "uns"); 
			chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
			chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
			chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
			// Update message in both Chat1 and Chat2
			Chat1.child(chat_key).updateChildren(chatmap); 
			Chat2.child(chat_key).updateChildren(chatmap); 
			// Clear chat map and reset input field
			chatmap.clear(); 
			
		} else {
			finish();
		}
	}
	public void _unlock_with_file(final String _path, final String _name) {
		enc_restore = FileUtil.readFile(_path).replace("//code:", "");
		if (enc_restore.equals(getIntent().getStringExtra("key"))) {
			linear1.setVisibility(View.VISIBLE);
		} else {
			linear1.setVisibility(View.GONE);
			SketchwareUtil.showMessage(getApplicationContext(), "invalid Content!");
		}
	}
	
	
	public void _DecryptedStringKey(final String _string, final String _key) {
		try {
			
			javax.crypto.spec.SecretKeySpec key = (javax.crypto.spec.SecretKeySpec) generateKey(_key);
			
			javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
			
			c.init(javax.crypto.Cipher.DECRYPT_MODE,key);
			
			byte[] decode = android.util.Base64.decode(_string,android.util.Base64.DEFAULT);
			
			byte[] decval = c.doFinal(decode);
			
			decrypted = new String(decval);
			Glide.with(getApplicationContext()).load(Uri.parse(decrypted)).into(imageview1);
		} catch (Exception ex) {
			SketchwareUtil.showMessage(getApplicationContext(), "End-to-End (Protected)");
			finish();
		}
	}
	
	
	public void _LibEncryptedDecrypted() {
	}
	private javax.crypto.SecretKey generateKey(String pwd) throws Exception {
		
		final java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
		
		byte[] b = pwd.getBytes("UTF-8");
		
		digest.update(b,0,b.length);
		
		byte[] key = digest.digest();
		
		javax.crypto.spec.SecretKeySpec sec = new javax.crypto.spec.SecretKeySpec(key, "AES");
		
		return sec;
	}
	
	
	public void _chatrooms() {
		Chat1.removeEventListener(_Chat1_child_listener);
		Chat2.removeEventListener(_Chat2_child_listener);
		chatroom = "chat/".concat(getIntent().getStringExtra("user1").concat("/".concat(getIntent().getStringExtra("user2"))));
		chatcopy = "chat/".concat(getIntent().getStringExtra("user2").concat("/".concat(getIntent().getStringExtra("user1"))));
		Chat1 = _firebase.getReference(chatroom);
		
		Chat2 = _firebase.getReference(chatcopy);
		Chat1.addChildEventListener(_Chat1_child_listener);
		Chat2.addChildEventListener(_Chat2_child_listener);
	}
	
	
	public void _blockScreenshot() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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