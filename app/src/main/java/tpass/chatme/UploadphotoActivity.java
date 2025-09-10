package tpass.chatme;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.*;
import io.agora.rtc.*;
import java.io.*;
import java.io.File;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class UploadphotoActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICK = 101;
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private String dp = "";
	private String username = "";
	private String file_path = "";
	private String file_name = "";
	private HashMap<String, Object> map = new HashMap<>();
	private String fontName = "";
	private String typeace = "";
	private HashMap<String, Object> data = new HashMap<>();
	private String nldp = "";
	
	private ArrayList<HashMap<String, Object>> json = new ArrayList<>();
	
	private LinearLayout linear1;
	private CircleImageView iconImageView;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private TextView txt_progressbar;
	private TextView textview5;
	private LinearLayout imagePickerButton;
	private LinearLayout linear4;
	private TextView textview1;
	private TextView textview2;
	private ProgressBar progressbar1;
	private TextView textview3;
	private TextView textview4;
	
	private Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
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
	private StorageReference userdp = _firebase_storage.getReference("userdp");
	private OnCompleteListener<Uri> _userdp_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _userdp_download_success_listener;
	private OnSuccessListener _userdp_delete_success_listener;
	private OnProgressListener _userdp_upload_progress_listener;
	private OnProgressListener _userdp_download_progress_listener;
	private OnFailureListener _userdp_failure_listener;
	private SharedPreferences file;
	private AlertDialog.Builder diago;
	private DatabaseReference users = _firebase.getReference("users");
	private ChildEventListener _users_child_listener;
	private TimerTask t;
	private Intent as = new Intent();
	private DatabaseReference log = _firebase.getReference("log");
	private ChildEventListener _log_child_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.uploadphoto);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
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
		linear1 = findViewById(R.id.linear1);
		iconImageView = findViewById(R.id.iconImageView);
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		txt_progressbar = findViewById(R.id.txt_progressbar);
		textview5 = findViewById(R.id.textview5);
		imagePickerButton = findViewById(R.id.imagePickerButton);
		linear4 = findViewById(R.id.linear4);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		progressbar1 = findViewById(R.id.progressbar1);
		textview3 = findViewById(R.id.textview3);
		textview4 = findViewById(R.id.textview4);
		pick.setType("image/*");
		pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		fauth = FirebaseAuth.getInstance();
		file = getSharedPreferences("f", Activity.MODE_PRIVATE);
		diago = new AlertDialog.Builder(this);
		
		imagePickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				textview5.setVisibility(View.GONE);
				startActivityForResult(pick, REQ_CD_PICK);
			}
		});
		
		textview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				diago.setTitle("Discard Profile Picture!");
				diago.setMessage("I will upload it later.");
				diago.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						map = new HashMap<>();
						map.put("username", username);
						map.put("user", username);
						map.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
						map.put("dp", file.getString("nulldp", ""));
						map.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
						map.put("limit", "false");
						map.put("v", "false");
						map.put("type", "user");
						users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
						map.clear();
						file.edit().remove("dpup").commit();
						as.setClass(getApplicationContext(), UsersActivity.class);
						as.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(as);
						finish();
					}
				});
				diago.setNegativeButton("Upload again", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				diago.create().show();
			}
		});
		
		_userdp_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				txt_progressbar.setVisibility(View.VISIBLE);
				textview4.setVisibility(View.GONE);
				txt_progressbar.setText(String.valueOf((long)(_progressValue)).concat("% Uploaded.."));
			}
		};
		
		_userdp_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_userdp_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				progressbar1.setVisibility(View.GONE);
				textview3.setVisibility(View.VISIBLE);
				txt_progressbar.setVisibility(View.GONE);
				map = new HashMap<>();
				map.put("username", username);
				map.put("user", username);
				map.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
				map.put("dp", _downloadUrl);
				map.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
				map.put("limit", "false");
				map.put("v", "false");
				map.put("type", "user");
				users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
				map.clear();
				t = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								file.edit().remove("dpup").commit();
								as.setClass(getApplicationContext(), UsersActivity.class);
								as.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(as);
								finish();
							}
						});
					}
				};
				_timer.schedule(t, (int)(500));
			}
		};
		
		_userdp_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_userdp_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_userdp_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				textview5.setVisibility(View.VISIBLE);
			}
		};
		
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
		
		_log_child_listener = new ChildEventListener() {
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
		log.addChildEventListener(_log_child_listener);
		
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
		_changeActivityFont("fn1");
		progressbar1.setVisibility(View.GONE);
		textview5.setVisibility(View.GONE);
		_ProgressBarColour(progressbar1, "#ffffff");
		_theme("");
		_updated_nullDP();
		username = file.getString("username", "");
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			file.edit().putString("dpup", "dpup").commit();
			_Send_log("dp");
		} else {
			as.setClass(getApplicationContext(), LogActivity.class);
			startActivity(as);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICK:
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
				file_path = _filePath.get((int)(0));
				file_name = Uri.parse(file_path).getLastPathSegment();
				iconImageView.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_filePath.get((int)(0)), 1024, 1024));
				progressbar1.setVisibility(View.VISIBLE);
				textview3.setVisibility(View.GONE);
				imagePickerButton.setEnabled(false);
				userdp.child(file_name).putFile(Uri.fromFile(new File(file_path))).addOnFailureListener(_userdp_failure_listener).addOnProgressListener(_userdp_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
					@Override
					public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
						return userdp.child(file_name).getDownloadUrl();
					}}).addOnCompleteListener(_userdp_upload_success_listener);
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(Color.parseColor(_focus));
		GG.setCornerRadius((float)_round);
		GG.setStroke((int) _stroke,
		Color.parseColor("#" + _strokeclr.replace("#", "")));
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
		_view.setBackground(RE);
	}
	
	
	public void _theme(final String _color) {
		_rippleRoundStroke(imagePickerButton, "#007BA7", "#2C3E50", 100, 0, "#ffffff");
		if ("dark".equals(file.getString("theme", ""))) {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
				Window w =UploadphotoActivity.this.getWindow();
				w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF212121);
			}
			linear1.setBackgroundColor(0xFF212121);
			textview1.setTextColor(0xFFEAF6FF);
		} else {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			getWindow().setStatusBarColor(0xFFFFFFFF);
			linear1.setBackgroundColor(0xFFFFFFFF);
			textview1.setTextColor(0xFF2C3E50);
		}
	}
	
	
	public void _ProgressBarColour(final ProgressBar _progressbar, final String _color) {
		if (android.os.Build.VERSION.SDK_INT >=21) {
			_progressbar.getIndeterminateDrawable() .setColorFilter(Color.parseColor(_color), PorterDuff.Mode.SRC_IN);
		}
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
	
	
	public void _Send_log(final String _status) {
		data = new HashMap<>();
		data.put("log", _status);
		log.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
		data.clear();
	}
	
	
	public void _updated_nullDP() {
		nldp = String.valueOf((long)(SketchwareUtil.getRandom((int)(1), (int)(20))));
		if ("1".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868698.png?alt=media&token=8043c6c6-dbec-4634-9ce7-9021f17420e8").commit();
		}
		if ("2".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868700.png?alt=media&token=1e6fff6c-ae6d-4e2b-b885-5b6d3f4e3a25").commit();
		}
		if ("3".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868704.png?alt=media&token=d1707b42-ae70-46f2-a6e5-d863b0498e9c").commit();
		}
		if ("4".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868708.png?alt=media&token=80710c1b-7852-49de-b5de-a7b4a061ccf9").commit();
		}
		if ("5".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868711.png?alt=media&token=fbad192d-c541-4e11-b442-1c49bbacb8c6").commit();
		}
		if ("6".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868712.png?alt=media&token=9302987f-1762-4291-b7f2-b63265971e98").commit();
		}
		if ("7".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868713.png?alt=media&token=7dc60c31-364c-411f-bc0e-79e7b552f043").commit();
		}
		if ("8".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868714.png?alt=media&token=59192bec-864c-4c62-ab42-e7f9c7be4979").commit();
		}
		if ("9".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868715.png?alt=media&token=70d17325-2b4e-41ba-89a6-58ee88297761").commit();
		}
		if ("10".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868718.png?alt=media&token=356fb064-52ad-4099-b30c-1bdb91f288d3").commit();
		}
		if ("11".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868719.png?alt=media&token=f08ed490-9c8c-4ee3-97e3-3993520d6142").commit();
		}
		if ("12".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868725.png?alt=media&token=67b37819-3c6e-4f5b-84a3-560e7e90a14b").commit();
		}
		if ("13".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868726.png?alt=media&token=b6af1f43-f230-43dd-9710-369060c9f42c").commit();
		}
		if ("14".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868728.png?alt=media&token=b729c111-8ef0-405f-b47f-5b160335981b").commit();
		}
		if ("15".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868729.png?alt=media&token=51e1858e-787d-4395-a300-2fed645e2a21").commit();
		}
		if ("16".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868732.png?alt=media&token=e60b5c99-cbc2-4b30-a821-3c176a66af4f").commit();
		}
		if ("17".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868739.png?alt=media&token=35f5c79d-0fba-4483-b0fc-62aaa0de23a5").commit();
		}
		if ("18".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868742.png?alt=media&token=48910e86-5e38-4340-9ebf-1e84bb8a3362").commit();
		}
		if ("19".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868745.png?alt=media&token=ef00538d-f8f9-4348-8489-537cb3061faa").commit();
		}
		if ("20".equals(nldp)) {
			file.edit().putString("nulldp", "https://firebasestorage.googleapis.com/v0/b/chat-2024-ff149.appspot.com/o/Fake%2F5868747.png?alt=media&token=9b7011f0-9a0c-4950-a453-edf7d4a5003c").commit();
		}
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