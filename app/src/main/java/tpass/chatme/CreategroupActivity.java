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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class CreategroupActivity extends AppCompatActivity {
	
	public final int REQ_CD_FPICKER = 101;
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private boolean valid = false;
	private String dp = "";
	private HashMap<String, Object> map = new HashMap<>();
	private String groupname = "";
	private String groupid = "";
	private String path = "";
	private String fname = "";
	private double b = 0;
	private String group = "";
	
	private ArrayList<String> usernames = new ArrayList<>();
	
	private LinearLayout linear7;
	private LinearLayout linear1;
	private TextView textview4;
	private CircleImageView iconImageView;
	private LinearLayout linear3;
	private LinearLayout linear5;
	private LinearLayout buttonValidate;
	private TextView textview2;
	private LinearLayout linear6;
	private LinearLayout linear10;
	private LinearLayout linear4;
	private EditText Groupnames;
	private EditText editTextUsername;
	private TextView textview3;
	
	private DatabaseReference users = _firebase.getReference("users");
	private ChildEventListener _users_child_listener;
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
	private Intent fpicker = new Intent(Intent.ACTION_GET_CONTENT);
	private StorageReference storage = _firebase_storage.getReference("storage");
	private OnCompleteListener<Uri> _storage_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _storage_download_success_listener;
	private OnSuccessListener _storage_delete_success_listener;
	private OnProgressListener _storage_upload_progress_listener;
	private OnProgressListener _storage_download_progress_listener;
	private OnFailureListener _storage_failure_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.creategroup);
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
		linear7 = findViewById(R.id.linear7);
		linear1 = findViewById(R.id.linear1);
		textview4 = findViewById(R.id.textview4);
		iconImageView = findViewById(R.id.iconImageView);
		linear3 = findViewById(R.id.linear3);
		linear5 = findViewById(R.id.linear5);
		buttonValidate = findViewById(R.id.buttonValidate);
		textview2 = findViewById(R.id.textview2);
		linear6 = findViewById(R.id.linear6);
		linear10 = findViewById(R.id.linear10);
		linear4 = findViewById(R.id.linear4);
		Groupnames = findViewById(R.id.Groupnames);
		editTextUsername = findViewById(R.id.editTextUsername);
		textview3 = findViewById(R.id.textview3);
		fauth = FirebaseAuth.getInstance();
		fpicker.setType("image/*");
		fpicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		iconImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(fpicker, REQ_CD_FPICKER);
			}
		});
		
		buttonValidate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				/*
if (valid) {
    // If profile picture (dp) is not set, use default image
    if (dp.equals("")) {
        dp = "https://cdn-icons-png.flaticon.com/128/12882/12882578.png";  // Default image
    }

    // Generate a unique group ID
    groupid = users.push().getKey();

    // Prepare group data
    HashMap<String, Object> map = new HashMap<>();
    map.put("username", groupname);
    map.put("user", groupname);
    map.put("admin", FirebaseAuth.getInstance().getCurrentUser().getUid());
    map.put("dp", dp);
    map.put("email", "null");
    map.put("limit", "false");
    map.put("v", "false");
    map.put("type", "group");
    map.put("id", groupid);

    // Save group data to Firebase
    users.child(groupid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            // Group created successfully, finish the activity
            Toast.makeText(getApplicationContext(), "Group created successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            // Handle failure in group creation
            Toast.makeText(getApplicationContext(), "Group creation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });

} else {
    // If validation fails
    SketchwareUtil.showMessage(getApplicationContext(), "Group not validated!");
}

*/
				if (valid) {
					// Use default image if 'dp' is empty
					if (dp.equals("")) {
						dp = "https://cdn-icons-png.flaticon.com/128/166/166258.png";
					}
					
					// Generate unique group ID
					groupid = users.push().getKey();
					
					// Prepare group data
					final  HashMap<String, Object> map = new HashMap<>();
					map.put("username", group);
					map.put("user", groupname);
					map.put("admin", FirebaseAuth.getInstance().getCurrentUser().getUid());
					map.put("dp", dp);
					map.put("email", "null");
					map.put("limit", "false");
					map.put("v", "false");
					map.put("type", "group");
					map.put("id", groupid);
					
					// Save group data to Firebase
					users.child(groupid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
							// Add group data to inbox
							String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
							DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference("inbox");
							
							inboxRef.child(myUid).child(groupid).updateChildren(map)
							.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									if (task.isSuccessful()) {
										Toast.makeText(getApplicationContext(), "Group created and added to inbox!", Toast.LENGTH_SHORT).show();
										finish();
									} else {
										Toast.makeText(getApplicationContext(), "Group added but failed to add to inbox.", Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
					}).addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Toast.makeText(getApplicationContext(), "Group creation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					// Validation failed
					SketchwareUtil.showMessage(getApplicationContext(), "Group not validated!");
				}
				
			}
		});
		
		Groupnames.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				group = _charSeq;
				textview2.setText(_charSeq);
				editTextUsername.setText(_charSeq.trim());
				final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
				final EditText editTextUsername = findViewById(R.id.editTextUsername); // EditText reference
				
				// Get the value from EditText
				final String textValue = editTextUsername.getText().toString().trim();
				
				// Regular expression to check for special characters
				String specialCharacterPattern = "^[a-zA-Z0-9_]*$"; // Only allows letters, numbers, and underscores
				
				if (!textValue.matches(specialCharacterPattern)) {
					// Set an error message on EditText if special characters are found
					editTextUsername.setError("No special characters allowed!");
					valid = false;
				} else {
					// Perform Firebase query to check if the value exists in "user"
					usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							boolean matchFound = false;
							
							// Iterate through all users
							for (DataSnapshot userSnapshot : snapshot.getChildren()) {
								String userValue = userSnapshot.child("user").getValue(String.class);
								if (userValue != null && userValue.equals(textValue)) {
									matchFound = true;
									break;
								}
							}
							
							if (matchFound) {
								// If a match is found, set an error message on EditText
								editTextUsername.setError("Username already taken!");
								valid = false;
							} else {
								// Clear any previous error if no match is found
								editTextUsername.setError(null);
								valid = true;
							}
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError error) {
							// Handle database error
							Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		editTextUsername.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
				final EditText editTextUsername = findViewById(R.id.editTextUsername); // EditText reference
				
				// Get the value from EditText
				final String textValue = Groupnames.getText().toString().trim();
				
				// Regular expression to check for special characters
				String specialCharacterPattern = "^[a-zA-Z0-9_]*$"; // Only allows letters, numbers, and underscores
				
				if (!textValue.matches(specialCharacterPattern)) {
					// Set an error message on EditText if special characters are found
					editTextUsername.setError("No special characters allowed!");
					valid = false;
				} else {
					// Perform Firebase query to check if the value exists in "user"
					usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot snapshot) {
							boolean matchFound = false;
							
							// Iterate through all users
							for (DataSnapshot userSnapshot : snapshot.getChildren()) {
								String userValue = userSnapshot.child("user").getValue(String.class);
								if (userValue != null && userValue.equals(textValue)) {
									matchFound = true;
									break;
								}
							}
							
							if (matchFound) {
								// If a match is found, set an error message on EditText
								editTextUsername.setError("Username already taken!");
								valid = false;
							} else {
								// Clear any previous error if no match is found
								editTextUsername.setError(null);
								valid = true;
								groupname = _charSeq;
								
							}
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError error) {
							// Handle database error
							Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_users_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.containsKey("user")) {
					usernames.add(_childValue.get("user").toString());
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
		
		_storage_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				textview4.setText(String.valueOf((long)(_progressValue)).concat("% uploaded.."));
			}
		};
		
		_storage_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_storage_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				dp = _downloadUrl;
				textview4.setText("Create New Group");
				Glide.with(getApplicationContext()).load(Uri.parse(_downloadUrl)).into(iconImageView);
			}
		};
		
		_storage_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_storage_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_storage_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				
			}
		};
		
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
		valid = false;
		editTextUsername.setEnabled(false);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_FPICKER:
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
				fname = Uri.parse(_filePath.get((int)(0))).getLastPathSegment();
				storage.child(fname).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
					@Override
					public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
						return storage.child(fname).getDownloadUrl();
					}}).addOnCompleteListener(_storage_upload_success_listener);
			}
			else {
				
			}
			break;
			default:
			break;
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