package tpass.chatme;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import java.io.InputStream;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import java.text.Normalizer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyprofileActivity extends AppCompatActivity {
	
	public final int REQ_CD_FPICK = 101;
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private String user = "";
	private String newDp = "";
	private String email = "";
	private String access = "";
	private String id = "";
	private String limit = "";
	private String hashcode = "";
	private HashMap<String, Object> mps = new HashMap<>();
	private String username = "";
	private String myUuid = "";
	private String path = "";
	private String file_name = "";
	private boolean last_change_email = false;
	private String lastChange = "";
	private String fontName = "";
	private String typeace = "";
	private String userInput = "";
	private boolean CHANGE = false;
	private boolean me = false;
	private boolean editTrue = false;
	private String bio = "";
	private String webs = "";
	private String profs = "";
	private String friendUid = "";
	private String myUid = "";
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private LinearLayout linear_bg;
	private LinearLayout gap;
	private LinearLayout linear_main_user;
	private LinearLayout linear13;
	private LinearLayout linear_noinfo;
	private TextView profile;
	private TextView pairkeys;
	private ImageView share;
	private TextView edit;
	private LinearLayout linear_profile_photo;
	private LinearLayout linear_profile_info;
	private CircleImageView iconImageView;
	private CircleImageView EditIcon;
	private ProgressBar progressbar1;
	private TextView name;
	private TextView profession;
	private TextView at_userid;
	private LinearLayout linear_weblink;
	private TextView about;
	private Button button1;
	private ImageView web;
	private TextView webUrl;
	private LinearLayout linear_username;
	private LinearLayout linear_userid;
	private LinearLayout linear_profession;
	private LinearLayout linear_email;
	private LinearLayout linear_pass;
	private LinearLayout linear_web;
	private LinearLayout linear_bio;
	private LinearLayout linear21;
	private TextView username_text;
	private EditText edittext_username;
	private LinearLayout line_usename;
	private TextView userid;
	private EditText edittext_userid;
	private LinearLayout linear16;
	private TextView profe;
	private EditText prof;
	private LinearLayout linear28;
	private TextView textview5;
	private EditText edittext_email;
	private LinearLayout linear18;
	private TextView textview6;
	private LinearLayout linear22;
	private LinearLayout linear20;
	private EditText edittext_pass;
	private TextView send_link;
	private TextView textview_web;
	private EditText edittext_web;
	private LinearLayout linear26;
	private TextView AboutMe;
	private EditText edittext_bio;
	private LinearLayout linear24;
	private Button button_save;
	private ImageView icon_cloud;
	private TextView noinfo_txt;
	
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
	private Intent fpick = new Intent(Intent.ACTION_GET_CONTENT);
	private StorageReference chatme = _firebase_storage.getReference("chatme");
	private OnCompleteListener<Uri> _chatme_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _chatme_download_success_listener;
	private OnSuccessListener _chatme_delete_success_listener;
	private OnProgressListener _chatme_upload_progress_listener;
	private OnProgressListener _chatme_download_progress_listener;
	private OnFailureListener _chatme_failure_listener;
	private SharedPreferences file;
	private ProgressDialog prog;
	private TimerTask ts;
	private AlertDialog.Builder diago;
	private Intent as = new Intent();
	private Calendar cal = Calendar.getInstance();
	private Calendar time = Calendar.getInstance();
	private Intent a = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.myprofile);
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
		vscroll1 = findViewById(R.id.vscroll1);
		linear1 = findViewById(R.id.linear1);
		linear_bg = findViewById(R.id.linear_bg);
		gap = findViewById(R.id.gap);
		linear_main_user = findViewById(R.id.linear_main_user);
		linear13 = findViewById(R.id.linear13);
		linear_noinfo = findViewById(R.id.linear_noinfo);
		profile = findViewById(R.id.profile);
		pairkeys = findViewById(R.id.pairkeys);
		share = findViewById(R.id.share);
		edit = findViewById(R.id.edit);
		linear_profile_photo = findViewById(R.id.linear_profile_photo);
		linear_profile_info = findViewById(R.id.linear_profile_info);
		iconImageView = findViewById(R.id.iconImageView);
		EditIcon = findViewById(R.id.EditIcon);
		progressbar1 = findViewById(R.id.progressbar1);
		name = findViewById(R.id.name);
		profession = findViewById(R.id.profession);
		at_userid = findViewById(R.id.at_userid);
		linear_weblink = findViewById(R.id.linear_weblink);
		about = findViewById(R.id.about);
		button1 = findViewById(R.id.button1);
		web = findViewById(R.id.web);
		webUrl = findViewById(R.id.webUrl);
		linear_username = findViewById(R.id.linear_username);
		linear_userid = findViewById(R.id.linear_userid);
		linear_profession = findViewById(R.id.linear_profession);
		linear_email = findViewById(R.id.linear_email);
		linear_pass = findViewById(R.id.linear_pass);
		linear_web = findViewById(R.id.linear_web);
		linear_bio = findViewById(R.id.linear_bio);
		linear21 = findViewById(R.id.linear21);
		username_text = findViewById(R.id.username_text);
		edittext_username = findViewById(R.id.edittext_username);
		line_usename = findViewById(R.id.line_usename);
		userid = findViewById(R.id.userid);
		edittext_userid = findViewById(R.id.edittext_userid);
		linear16 = findViewById(R.id.linear16);
		profe = findViewById(R.id.profe);
		prof = findViewById(R.id.prof);
		linear28 = findViewById(R.id.linear28);
		textview5 = findViewById(R.id.textview5);
		edittext_email = findViewById(R.id.edittext_email);
		linear18 = findViewById(R.id.linear18);
		textview6 = findViewById(R.id.textview6);
		linear22 = findViewById(R.id.linear22);
		linear20 = findViewById(R.id.linear20);
		edittext_pass = findViewById(R.id.edittext_pass);
		send_link = findViewById(R.id.send_link);
		textview_web = findViewById(R.id.textview_web);
		edittext_web = findViewById(R.id.edittext_web);
		linear26 = findViewById(R.id.linear26);
		AboutMe = findViewById(R.id.AboutMe);
		edittext_bio = findViewById(R.id.edittext_bio);
		linear24 = findViewById(R.id.linear24);
		button_save = findViewById(R.id.button_save);
		icon_cloud = findViewById(R.id.icon_cloud);
		noinfo_txt = findViewById(R.id.noinfo_txt);
		fauth = FirebaseAuth.getInstance();
		fpick.setType("image/*");
		fpick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		file = getSharedPreferences("f", Activity.MODE_PRIVATE);
		diago = new AlertDialog.Builder(this);
		
		pairkeys.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", pairkeys.getText().toString()));
				SketchwareUtil.showMessage(getApplicationContext(), pairkeys.getText().toString());
			}
		});
		
		share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (me) {
					if (linear13.getVisibility() == View.VISIBLE) {
						linear13.setVisibility(View.GONE);
						linear_profile_info.setVisibility(View.VISIBLE);
						EditIcon.setVisibility(View.GONE);
						editTrue = false;
						edit.setText("Edit");
					} else {
						linear13.setVisibility(View.VISIBLE);
						linear_profile_info.setVisibility(View.GONE);
						EditIcon.setVisibility(View.VISIBLE);
						editTrue = true;
						edit.setText("Close");
					}
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Request Failed!");
				}
			}
		});
		
		linear_profile_photo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (me) {
					if (editTrue) {
						startActivityForResult(fpick, REQ_CD_FPICK);
					}
				}
			}
		});
		
		linear_weblink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				// Get the input URL and trim whitespace
				String urlInput = webUrl.getText().toString().trim();
				
				// Check for empty input
				if (urlInput.isEmpty()) {
					Toast.makeText(MyprofileActivity.this, "Please enter a URL", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Uri uri = Uri.parse(urlInput);
				String scheme = uri.getScheme();
				
				// Validate scheme (HTTP/HTTPS)
				if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
					Toast.makeText(MyprofileActivity.this, "URL must start with http:// or https://", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// Ensure the URI has a valid host
				if (uri.getHost() == null) {
					Toast.makeText(MyprofileActivity.this, "Invalid URL: Host not found", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// Create the Intent and check if it can be handled
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				if (intent.resolveActivity(getPackageManager()) == null) {
					Toast.makeText(MyprofileActivity.this, "No app can handle this URL", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// Launch the URL
				startActivity(intent);
			}
		});
		
		edittext_username.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				username = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		edittext_userid.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				userInput = _charSeq;
				if (!userInput.isEmpty()) {
					FirebaseDatabase.getInstance()
					.getReference("Users")
					.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							boolean matchFound = false;
							
							for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
								String userKey = userSnapshot.child("user").getValue(String.class);
								if (userInput.equals(userKey)) {
									matchFound = true;
									break;
								}
							}
							
							CHANGE = !matchFound;
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							// Handle database error
						}
					});
				} else {
					CHANGE = true; // Default to true if input is empty
				}
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		prof.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				profs = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		edittext_email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				email = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		send_link.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!"".equals(edittext_email.getText().toString().trim())) {
					fauth.sendPasswordResetEmail(edittext_email.getText().toString()).addOnCompleteListener(_fauth_reset_password_listener);
					_ProgresbarShow("Sending..");
				} else {
					((EditText)edittext_email).setError("Enter Email First!");
				}
			}
		});
		
		edittext_web.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				webs = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		edittext_bio.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				bio = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		button_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		_users_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				//New Code:
				if (_childKey.equals(getIntent().getStringExtra("id"))) {
					// Iterate through all keys in _childValue
					for (String key : _childValue.keySet()) {
						Object value = _childValue.get(key);
						if (value == null) {
							continue; // Skip null values
						}
						
						switch (key) {
							case "username":
							username = value.toString();
							edittext_username.setText(username);
							break;
							
							case "user":
							user = value.toString();
							edittext_userid.setText(user);
							break;
							
							case "dp":
							newDp = value.toString();
							Glide.with(getApplicationContext()).load(Uri.parse(newDp)).into(iconImageView); // Use iconImageView to match old code
							break;
							
							case "email":
							email = value.toString();
							edittext_email.setText(email);
							break;
							
							case "id":
							id = value.toString();
							break;
							
							case "limit":
							limit = value.toString();
							break;
							
							case "hashcode":
							hashcode = value.toString();
							break;
							
							case "v":
							access = value.toString();
							break;
							
							case "bio":
							bio = value.toString();
							break;
							
							case "prof":
							profs = value.toString();
							break;
							
							case "web":
							webs = value.toString();
							break;
							
							
							case "timestamp":
							time = Calendar.getInstance();
							lastChange = value.toString();
							try {
								if ((time.getTimeInMillis() - Double.parseDouble(lastChange)) > 86400000) {
									last_change_email = true;
								} else {
									last_change_email = false;
								}
							} catch (NumberFormatException e) {
								// Handle invalid timestamp format
								last_change_email = true;
								lastChange = "false";
							}
							break;
							
							default:
							// Handle any unexpected keys if needed
							break;
						}
					}
					
					// Handle missing timestamp key
					if (!_childValue.containsKey("timestamp")) {
						last_change_email = true;
						lastChange = "false";
					}
					
					// Set Pair key (use pairkeys to match old code)
					if (FirebaseAuth.getInstance().getCurrentUser() != null) {
						pairkeys.setText("Pair key : ".concat(
						String.valueOf((long) (FirebaseAuth.getInstance().getCurrentUser().getUid().hashCode()))
						));
					} else {
						// Handle case where the current user is null
						pairkeys.setText("Pair key : N/A");
					}
				} else {
					// Handle case when _childKey does not match intent id
				}
				name.setText(username);
				at_userid.setText(" @".concat(user));
				if (!profs.equals("")) {
					profession.setText(profs);
				} else {
					profession.setText("Profession Not Added");
				}
				if (!bio.equals("")) {
					about.setText("");
				} else {
					about.setText("About Not Added");
				}
				webUrl.setText(webs);
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
		
		_chatme_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				_ProgresbarDimiss();
				_ProgresbarShow(String.valueOf((long)(_progressValue)).concat("% Uploaded..."));
				linear_profile_photo.setEnabled(false);
				progressbar1.setVisibility(View.VISIBLE);
			}
		};
		
		_chatme_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_chatme_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				linear_profile_photo.setEnabled(true);
				newDp = _downloadUrl;
				Glide.with(getApplicationContext()).load(Uri.parse(_downloadUrl)).into(iconImageView);
				
				// Get current user's UID and mark it as final
				final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
				
				// Reference to the user's data in Firebase
				DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(myUid);
				
				// Update the 'dp' key with the new URL
				userRef.child("dp").setValue(newDp).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							SketchwareUtil.showMessage(getApplicationContext(), "DP updated successfully");
							_ProgresbarDimiss();
							
						} else {
							SketchwareUtil.showMessage(getApplicationContext(), "Failed to update DP");
							_ProgresbarDimiss();
						}
					}
				});
				
				progressbar1.setVisibility(View.GONE);
			}
		};
		
		_chatme_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_chatme_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_chatme_failure_listener = new OnFailureListener() {
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
				if (_success) {
					SketchwareUtil.showMessage(getApplicationContext(), "Password reset link sent check your email inbox.");
					_ProgresbarDimiss();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "failed!");
					_ProgresbarDimiss();
				}
			}
		};
	}
	
	private void initializeLogic() {
		friendUid = getIntent().getStringExtra("id");
		_changeActivityFont("fn1");
		// Get the UIDs (replace with actual logic to retrieve friendUid)
		// Replace with the actual friend's UID
		final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		
		// Firebase references
		final DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference("FriendRequests");
		final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
		final DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference("inbox");
		
		// Retrieve current user's data (e.g., "dp" and "username")
		usersRef.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					// Store dp and username in final variables to avoid scope issues
					final String myDp = dataSnapshot.child("dp").getValue(String.class);
					final String myUsername = dataSnapshot.child("username").getValue(String.class);
					
					// Check if the friend is already added in the inbox
					inboxRef.child(myUid).child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot inboxSnapshot) {
							if (inboxSnapshot.exists()) {
								// Friend is already added, update button state
								button1.setText("Remove Friend");
								button1.setEnabled(true);
								
								// Set click listener to remove the friend
								button1.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										// Disable button and show progress
										button1.setEnabled(false);
										button1.setText("Removing...");
										
										// Remove friend from both users' inboxes
										inboxRef.child(myUid).child(friendUid).removeValue()
										.addOnCompleteListener(new OnCompleteListener<Void>() {
											@Override
											public void onComplete(@NonNull Task<Void> task) {
												if (task.isSuccessful()) {
													inboxRef.child(friendUid).child(myUid).removeValue()
													.addOnCompleteListener(new OnCompleteListener<Void>() {
														@Override
														public void onComplete(@NonNull Task<Void> task) {
															if (task.isSuccessful()) {
																// Remove friend request from both users
																friendRequestRef.child(friendUid).child(myUid).removeValue()
																.addOnCompleteListener(new OnCompleteListener<Void>() {
																	@Override
																	public void onComplete(@NonNull Task<Void> task) {
																		if (task.isSuccessful()) {
																			friendRequestRef.child(myUid).child(friendUid).removeValue()
																			.addOnCompleteListener(new OnCompleteListener<Void>() {
																				@Override
																				public void onComplete(@NonNull Task<Void> task) {
																					if (task.isSuccessful()) {
																						// Friend removed successfully
																						button1.setText("Add Friend");
																						button1.setEnabled(true);
																					} else {
																						// Handle failure
																						button1.setEnabled(true);
																						button1.setText("Remove Friend");
																					}
																				}
																			});
																		} else {
																			// Handle failure
																			button1.setEnabled(true);
																			button1.setText("Remove Friend");
																		}
																	}
																});
															} else {
																// Handle failure
																button1.setEnabled(true);
																button1.setText("Remove Friend");
															}
														}
													});
												} else {
													// Handle failure
													button1.setEnabled(true);
													button1.setText("Remove Friend");
												}
											}
										});
									}
								});
							} else {
								// Check if a friend request was already sent
								friendRequestRef.child(friendUid).child(myUid)
								.addListenerForSingleValueEvent(new ValueEventListener() {
									@Override
									public void onDataChange(@NonNull DataSnapshot requestSnapshot) {
										if (requestSnapshot.exists()) {
											// Request already sent, update button state
											button1.setText("Requested");
											button1.setEnabled(false);
										} else {
											// Allow sending a friend request
											button1.setText("Add Friend");
											button1.setEnabled(true);
											
											// Set click listener to send the friend request
											button1.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													// Disable button and show progress
													button1.setEnabled(false);
													button1.setText("Requesting...");
													
													// Create the friend request object with "dp" and "username"
													Map<String, String> requestData = new HashMap<>();
													requestData.put("dp", myDp);  // Now accessible
													requestData.put("username", myUsername);  // Now accessible
													requestData.put("status", "pending");
													requestData.put("id", myUid);
													requestData.put("senderId", friendUid);
													
													// Send the friend request
													friendRequestRef.child(friendUid).child(myUid)
													.setValue(requestData)
													.addOnCompleteListener(new OnCompleteListener<Void>() {
														@Override
														public void onComplete(@NonNull Task<Void> task) {
															if (task.isSuccessful()) {
																// Request sent successfully
																button1.setText("Requested");
															} else {
																// Handle failure
																button1.setEnabled(true);
																button1.setText("Add Friend");
															}
														}
													});
												}
											});
										}
									}
									
									@Override
									public void onCancelled(@NonNull DatabaseError databaseError) {
										// Handle request check error
									}
								});
							}
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							// Handle inbox check error
						}
					});
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				// Handle error fetching user data
			}
		});
		/*

// Set the OnClickListener for button_save
button_save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(myUid);
         // views
         linear13.setVisibility(View.GONE);
         EditIcon.setVisibility(View.GONE);
         linear_profile_info.setVisibility(View.VISIBLE);
         edit.setVisibility(View.VISIBLE);
         
        // Retrieve current 'username' and 'user' from Firebase
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get current username and user values
                    String currentUsername = snapshot.child("username").getValue(String.class);
                    String currentUser = snapshot.child("user").getValue(String.class);

                    // Get new values from EditTexts
                    String newUsername = edittext_username.getText().toString();
                    String newUser = edittext_userid.getText().toString();

                    // Check if the new values differ from the current values
                    if (!newUsername.equals(currentUsername) || !newUser.equals(currentUser)) {
                        // Update fields if they are different
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("username", newUsername);
                        if (CHANGE) {
                            updates.put("user", newUser);
                            }else{
                        updates.put("user", currentUser);
                        SketchwareUtil.showMessage(getApplicationContext(), "userid Not Changed");
                                                }
                        userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    SketchwareUtil.showMessage(getApplicationContext(), "Profile updated successfully");
                                } else {
                                    SketchwareUtil.showMessage(getApplicationContext(), "Failed to update profile");
                                }
                            }
                        });
                    } else {
                        SketchwareUtil.showMessage(getApplicationContext(), "No changes detected");
                    }
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), "User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
            }
        });
    }
});

*/
		//new code: 
		
		button_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
				final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(myUid);
				
				// UI visibility changes
				linear13.setVisibility(View.GONE);
				EditIcon.setVisibility(View.GONE);
				linear_profile_info.setVisibility(View.VISIBLE);
				edit.setVisibility(View.VISIBLE);
				edit.setText("Edit");
				
				userRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot snapshot) {
						if (snapshot.exists()) {
							final String currentUsername = snapshot.child("username").getValue(String.class);
							final String currentUser = snapshot.child("user").getValue(String.class);
							final String newUsername = edittext_username.getText().toString().trim();
							final String newUser = edittext_userid.getText().toString().trim();
							
							final boolean usernameChanged = !newUsername.equals(currentUsername);
							final boolean userChanged = !newUser.equals(currentUser);
							
							// Always prepare updates for web, bio, and prof
							final  Map<String, Object> updates = new HashMap<>();
							updates.put("web", webs);
							updates.put("bio", bio);
							updates.put("prof", profs);
							
							if (usernameChanged) {
								updates.put("username", newUsername);
							}
							
							if (userChanged) {
								// Check if the new user ID is available
								final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
								usersRef.orderByChild("user").equalTo(newUser).addListenerForSingleValueEvent(new ValueEventListener() {
									@Override
									public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
										boolean userTaken = false;
										for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
											if (!userSnapshot.getKey().equals(myUid)) {
												userTaken = true;
												break;
											}
										}
										
										if (userTaken) {
											SketchwareUtil.showMessage(getApplicationContext(), "User ID is already taken!");
											return; // Exit if user ID is taken
										}
										
										// Add user ID to updates if it's not taken
										updates.put("user", newUser);
										
										// Perform the update
										userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
											@Override
											public void onComplete(@NonNull Task<Void> task) {
												if (task.isSuccessful()) {
													SketchwareUtil.showMessage(getApplicationContext(), "Profile updated successfully");
												} else {
													SketchwareUtil.showMessage(getApplicationContext(), "Update failed: " + task.getException().getMessage());
												}
											}
										});
									}
									
									@Override
									public void onCancelled(@NonNull DatabaseError databaseError) {
										SketchwareUtil.showMessage(getApplicationContext(), "Check failed: " + databaseError.getMessage());
									}
								});
							} else {
								// No user ID change, just update other fields
								userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										if (task.isSuccessful()) {
											SketchwareUtil.showMessage(getApplicationContext(), "Profile updated successfully");
										} else {
											SketchwareUtil.showMessage(getApplicationContext(), "Update failed: " + task.getException().getMessage());
										}
									}
								});
							}
						} else {
							SketchwareUtil.showMessage(getApplicationContext(), "User data not found");
						}
					}
					
					@Override
					public void onCancelled(@NonNull DatabaseError error) {
						SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
					}
				});
			}
		});
		_ui();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_FPICK:
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
				file_name = Uri.parse(_filePath.get((int)(0))).getLastPathSegment();
				try {
					chatme.child(file_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_chatme_failure_listener).addOnProgressListener(_chatme_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
						@Override
						public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
							return chatme.child(file_name).getDownloadUrl();
						}}).addOnCompleteListener(_chatme_upload_success_listener);
				} catch (Exception e) {
					SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
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
	public void onStart() {
		super.onStart();
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			if (SketchwareUtil.isConnected(getApplicationContext())) {
				
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "Network response Error!");
				finish();
			}
		} else {
			as.setClass(getApplicationContext(), LogActivity.class);
			startActivity(as);
			finish();
		}
	}
	
	
	@Override
	public void onBackPressed() {
		if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(getIntent().getStringExtra("id"))) {
			a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			a.setClass(getApplicationContext(), SettingsActivity.class);
			startActivity(a);
			finish();
		} else {
			finish();
		}
	}
	public void _ImageColor(final ImageView _image, final String _color) {
		_image.setColorFilter(Color.parseColor(_color),PorterDuff.Mode.SRC_ATOP);
	}
	
	
	public void _theme(final String _color) {
		if ("dark".equals(file.getString("theme", ""))) {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
				Window w =MyprofileActivity.this.getWindow();
				w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF212121);
			}
			gap.setBackgroundColor(0xFF212121);
			vscroll1.setBackgroundColor(0xFF212121);
			linear_main_user.setBackgroundColor(0xFF303032);
			edittext_username.setTextColor(0xFFFFFFFF);
			edittext_userid.setTextColor(0xFFFFFFFF);
			edittext_email.setTextColor(0xFFFFFFFF);
			edittext_pass.setTextColor(0xFFE0E0E0);
			edittext_username.setTextColor(0xFFF0F2F5);
			edittext_userid.setTextColor(0xFFF0F2F5);
			edittext_web.setTextColor(0xFFF0F2F5);
			edittext_bio.setTextColor(0xFFF0F2F5);
			at_userid.setTextColor(0xFFF0F2F5);
			prof.setTextColor(0xFFF0F2F5);
			name.setTextColor(0xFFFFFFFF);
			about.setTextColor(0xFF7F8C8D);
		} else {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
				Window w =MyprofileActivity.this.getWindow();
				w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF007BA7);
			}
			vscroll1.setBackgroundColor(0xFFFFFFFF);
			gap.setBackgroundColor(0xFF007BA7);
			linear_main_user.setBackgroundColor(0xFFF5F5F5);
			edittext_username.setTextColor(0xFF000000);
			edittext_userid.setTextColor(0xFF000000);
			edittext_email.setTextColor(0xFF000000);
			edittext_pass.setTextColor(0xFF000000);
			edittext_username.setTextColor(0xFF39393B);
			edittext_userid.setTextColor(0xFF39393B);
			prof.setTextColor(0xFF39393B);
			edittext_web.setTextColor(0xFF39393B);
			edittext_bio.setTextColor(0xFF39393B);
			at_userid.setTextColor(0xFF39393B);
			name.setTextColor(0xFF000000);
			about.setTextColor(0xFF7F8C8D);
		}
		webUrl.setTextColor(0xFF007BA7);
		_rippleRoundStroke(button_save, "#007BA7", "#ffffff", 555, 0, "#9e9e9e");
		_ImageColor(web, "#007BA7");
		_ImageColor(share, "#FFFFFF");
		_ImageColor(icon_cloud, "#BDBDBD");
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
	
	
	public void _ProgresbarShow(final String _title) {
		prog = new ProgressDialog(MyprofileActivity.this);
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
	
	
	public void _ui() {
		me = false;
		_theme("");
		if (getIntent().hasExtra("id")) {
			if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(getIntent().getStringExtra("id"))) {
				me = true;
				edit.setVisibility(View.VISIBLE);
				button1.setVisibility(View.GONE);
			} else {
				me = false;
				edit.setVisibility(View.GONE);
				button1.setVisibility(View.VISIBLE);
			}
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "User not found!");
			finish();
		}
		linear13.setVisibility(View.GONE);
		EditIcon.setVisibility(View.GONE);
		progressbar1.setVisibility(View.GONE);
		linear_email.setEnabled(false);
		edittext_pass.setEnabled(false);
		button1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)36, 0xFF007BA7));
		edit.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)25, 0xFFEAF6FF));
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