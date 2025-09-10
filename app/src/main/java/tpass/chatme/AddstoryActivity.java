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
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
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
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class AddstoryActivity extends AppCompatActivity {
	
	public final int REQ_CD_FPICKS = 101;
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private String path = "";
	private String name = "";
	private boolean video = false;
	private boolean image = false;
	private String myUid = "";
	private HashMap<String, Object> ps = new HashMap<>();
	private String key = "";
	private String username = "";
	private String chatroom = "";
	private String chatcopy = "";
	private String ref = "";
	private double Tlimit = 0;
	private String currentUserId = "";
	
	private LinearLayout linear_header;
	private LinearLayout linear1;
	private TextView textview1;
	private LinearLayout linear_user;
	private ImageView imageview1;
	private VideoView videoview1;
	private ProgressBar progressbar1;
	private LinearLayout linear5;
	private CircleImageView circleimageview1;
	private LinearLayout linear4;
	private TextView textview_username;
	private TextView textview_time;
	
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
	private StorageReference fstories = _firebase_storage.getReference("fstories");
	private OnCompleteListener<Uri> _fstories_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _fstories_download_success_listener;
	private OnSuccessListener _fstories_delete_success_listener;
	private OnProgressListener _fstories_upload_progress_listener;
	private OnProgressListener _fstories_download_progress_listener;
	private OnFailureListener _fstories_failure_listener;
	private Intent fpicks = new Intent(Intent.ACTION_GET_CONTENT);
	private RequestNetwork net;
	private RequestNetwork.RequestListener _net_request_listener;
	private ProgressDialog prog;
	private DatabaseReference inbox = _firebase.getReference("inbox");
	private ChildEventListener _inbox_child_listener;
	private Calendar taskD = Calendar.getInstance();
	private DatabaseReference users = _firebase.getReference("users");
	private ChildEventListener _users_child_listener;
	private TimerTask timestS;
	private DatabaseReference post = _firebase.getReference("post");
	private ChildEventListener _post_child_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.addstory);
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
		linear_header = findViewById(R.id.linear_header);
		linear1 = findViewById(R.id.linear1);
		textview1 = findViewById(R.id.textview1);
		linear_user = findViewById(R.id.linear_user);
		imageview1 = findViewById(R.id.imageview1);
		videoview1 = findViewById(R.id.videoview1);
		MediaController videoview1_controller = new MediaController(this);
		videoview1.setMediaController(videoview1_controller);
		progressbar1 = findViewById(R.id.progressbar1);
		linear5 = findViewById(R.id.linear5);
		circleimageview1 = findViewById(R.id.circleimageview1);
		linear4 = findViewById(R.id.linear4);
		textview_username = findViewById(R.id.textview_username);
		textview_time = findViewById(R.id.textview_time);
		fauth = FirebaseAuth.getInstance();
		fpicks.setType("*/*");
		fpicks.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		net = new RequestNetwork(this);
		
		linear1.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				
				return true;
			}
		});
		
		textview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (SketchwareUtil.isConnected(getApplicationContext())) {
					fstories.child(name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_fstories_failure_listener).addOnProgressListener(_fstories_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
						@Override
						public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
							return fstories.child(name).getDownloadUrl();
						}}).addOnCompleteListener(_fstories_upload_success_listener);
					_ProgresbarShow("Loading.. ");
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "No internet connection..!");
				}
			}
		});
		
		videoview1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer _mediaPlayer) {
				finish();
			}
		});
		
		_fstories_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				_ProgresbarDimiss();
				_ProgresbarShow("Uploading.. ".concat(String.valueOf((long)(_progressValue)).concat("% ")));
			}
		};
		
		_fstories_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_fstories_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				if (video) {
					_pushStory(_downloadUrl, true, false, username);
				} else {
					if (image) {
						_pushStory(_downloadUrl, false, true, username);
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), "Invalid File Format!");
						finish();
					}
				}
			}
		};
		
		_fstories_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_fstories_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_fstories_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				SketchwareUtil.showMessage(getApplicationContext(), _message);
				_ProgresbarDimiss();
			}
		};
		
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
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					if (_childValue.containsKey("username")) {
						username = _childValue.get("username").toString();
					} else {
						username = " @chatuser7";
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
		users.addChildEventListener(_users_child_listener);
		
		_post_child_listener = new ChildEventListener() {
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
		post.addChildEventListener(_post_child_listener);
		
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
			Window w =AddstoryActivity.this.getWindow();
			w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF1C1C1C);
		}
		if (getIntent().hasExtra("view")) {
			_ProgressBarColour(progressbar1, "#ffffff");
			_timelimit();
			linear_header.setVisibility(View.GONE);
			textview_username.setText(getIntent().getStringExtra("username"));
			if (getIntent().hasExtra("image")) {
				imageview1.setVisibility(View.VISIBLE);
				videoview1.setVisibility(View.GONE);
				Glide.with(getApplicationContext()).load(Uri.parse(getIntent().getStringExtra("url"))).into(imageview1);
			} else {
				videoview1.setVideoURI(Uri.parse(getIntent().getStringExtra("url")));
				videoview1.start();
				videoview1.setVisibility(View.VISIBLE);
				imageview1.setVisibility(View.GONE);
			}
		} else {
			startActivityForResult(fpicks, REQ_CD_FPICKS);
			textview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)26, 0xFF4267B2));
			imageview1.setVisibility(View.GONE);
			videoview1.setVisibility(View.GONE);
			video = false;
			image = false;
			linear_user.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_FPICKS:
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
				name = Uri.parse(path).getLastPathSegment();
				_extension(name);
			}
			else {
				finish();
			}
			break;
			default:
			break;
		}
	}
	
	public void _ProgresbarShow(final String _title) {
		prog = new ProgressDialog(AddstoryActivity.this);
		prog.setMax(100);
		prog.setMessage(_title);
		prog.setIndeterminate(true);
		prog.setCancelable(false);
		prog.show();
	}
	
	
	public void _ProgresbarDimiss() {
		if(prog != null)
		{
			prog.dismiss();
		}
	}
	
	
	public void _extension(final String _path) {
		if (_path.endsWith("jpg") || (_path.endsWith("jpeg") || (_path.endsWith("png") || (_path.endsWith("webp") || _path.endsWith("gif"))))) {
			image = true;
			imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(path, 1024, 1024));
			imageview1.setVisibility(View.VISIBLE);
			videoview1.setVisibility(View.GONE);
		} else {
			if (_path.endsWith("mp4") || (_path.endsWith("mkv") || (_path.endsWith("webm") || (_path.endsWith("mov") || _path.endsWith("3gp"))))) {
				video = true;
				videoview1.setVideoURI(Uri.parse("file:///".concat(path)));
				videoview1.start();
				imageview1.setVisibility(View.GONE);
				videoview1.setVisibility(View.VISIBLE);
			} else {
				video = false;
				video = false;
				SketchwareUtil.showMessage(getApplicationContext(), "invalid format..");
				finish();
			}
		}
	}
	
	
	public void _pushStory(final String _downloadUrl, final boolean _isVideo, final boolean _isImage, final String _username) {
		/*
currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


// Firebase references
final DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference("inbox").child(currentUserId);
final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("post").child(currentUserId);

// Generate a unique key for the post
final String postKey = postRef.push().getKey();
final Calendar taskD = Calendar.getInstance();

// Create the story data
final HashMap<String, Object> storyData = new HashMap<>();
storyData.put("story_url", _downloadUrl);
storyData.put("story_timestamp", String.valueOf(taskD.getTimeInMillis()));
storyData.put("story_username", username);
storyData.put("story_key", postKey);
storyData.put("userid", currentUserId);
storyData.put("story_status", "unseen");

if (_isVideo) {
    storyData.put("story_type", "video");
} else if (_isImage) {
    storyData.put("story_type", "image");
}

// Add the story to the current user's "post" node
postRef.child(postKey).updateChildren(storyData).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void unused) {
        // Successfully added the story, now update the inbox users
        inboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        final String userId = userSnapshot.getKey(); // Get the user's UUID
                        final DatabaseReference userPostRef = FirebaseDatabase.getInstance()
                            .getReference("post")
                            .child(userId)
                            .child(currentUserId)
                            .child(postKey);

                        // Add the story to each user's "post" node
                        userPostRef.setValue(storyData);
                    }
                }
                Toast.makeText(AddstoryActivity.this, "Story posted and shared successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddstoryActivity.this, "Failed to update inbox: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(AddstoryActivity.this, "Failed to post story: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
});
*/
		currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
		
		// Firebase references
		final DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference("inbox").child(currentUserId);
		
		// Generate a unique key for the post
		final String postKey = FirebaseDatabase.getInstance().getReference("post").push().getKey();
		final Calendar taskD = Calendar.getInstance();
		
		// Create the story data
		final HashMap<String, Object> storyData = new HashMap<>();
		storyData.put("story_url", _downloadUrl);
		storyData.put("story_timestamp", String.valueOf(taskD.getTimeInMillis()));
		storyData.put("story_username", username);
		storyData.put("story_key", postKey);
		storyData.put("userid", currentUserId);
		storyData.put("story_status", "unseen");
		
		if (_isVideo) {
			storyData.put("story_type", "video");
		} else if (_isImage) {
			storyData.put("story_type", "image");
		}
		
		// Skip adding the story to the current user's "post" node
		// Only update the inbox users
		inboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					for (DataSnapshot userSnapshot : snapshot.getChildren()) {
						final String userId = userSnapshot.getKey(); // Get the user's UUID
						final DatabaseReference userPostRef = FirebaseDatabase.getInstance()
						.getReference("post")
						.child(userId)
						.child(currentUserId)
						.child(postKey);
						
						// Add the story to each user's "post" node
						userPostRef.setValue(storyData).addOnSuccessListener(new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void unused) {
								Toast.makeText(AddstoryActivity.this, "Story shared successfully!", Toast.LENGTH_SHORT).show();
							}
						}).addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								Toast.makeText(AddstoryActivity.this, "Failed to share story: " + e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
				finish(); // Close the activity after sharing the story
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Toast.makeText(AddstoryActivity.this, "Failed to update inbox: " + error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	
	public void _timelimit() {
		Tlimit = 0;
		if (getIntent().hasExtra("image")) {
			progressbar1.setMax((int)15);
		} else {
			progressbar1.setMax((int)30);
		}
		timestS = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (getIntent().hasExtra("image")) {
							if (15 < Tlimit) {
								timestS.cancel();
								finish();
							} else {
								Tlimit++;
								progressbar1.setProgress((int)Tlimit);
							}
						} else {
							if (30 < Tlimit) {
								timestS.cancel();
								finish();
							} else {
								Tlimit++;
								progressbar1.setProgress((int)Tlimit);
							}
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(timestS, (int)(0), (int)(1000));
		timestS = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressbar1.setProgress((int)Tlimit);
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(timestS, (int)(0), (int)(50));
	}
	
	
	public void _ProgressBarColour(final ProgressBar _progressbar, final String _color) {
		if (android.os.Build.VERSION.SDK_INT >=21) {
			_progressbar.getIndeterminateDrawable() .setColorFilter(Color.parseColor(_color), PorterDuff.Mode.SRC_IN);
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