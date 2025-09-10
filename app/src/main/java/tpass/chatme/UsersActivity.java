package tpass.chatme;

import android.Manifest;
import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Context;
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
import android.os.Vibrator;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.hdodenhof.circleimageview.*;
import io.agora.rtc.*;
import java.io.*;
import java.io.File;
import java.io.InputStream;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
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

public class UsersActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private FloatingActionButton _fab;
	private String getuser = "";
	private String fontName = "";
	private String typeace = "";
	private String DpSaveLink = "";
	private String username = "";
	private String id = "";
	private String proshow_link = "";
	private double position = 0;
	private HashMap<String, Object> map_search = new HashMap<>();
	private double getMsgCount = 0;
	private double totalCounts = 0;
	private HashMap<String, Object> status = new HashMap<>();
	private HashMap<String, Object> push = new HashMap<>();
	private HashMap<String, Object> data = new HashMap<>();
	private HashMap<String, Object> map = new HashMap<>();
	private String myf = "";
	private double f_req = 0;
	private boolean offline = false;
	private String package_name = "";
	private String ver = "";
	private double currentTime = 0;
	private double difference = 0;
	private String chatroom = "";
	private String chatcopy = "";
	private String getStory = "";
	private String inboxId = "";
	private String storyId = "";
	private boolean mystory = false;
	private String chat_key = "";
	private HashMap<String, Object> chatmap = new HashMap<>();
	private String mydp = "";
	private String w_user = "";
	private String story_key = "";
	private String storyref = "";
	private String currentUserId = "";
	private String callerkey = "";
	private boolean user2calling = false;
	private boolean imCalling = false;
	private String user2CallID = "";
	private String channel = "";
	
	private ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> search_users = new ArrayList<>();
	private ArrayList<String> msg_for_me = new ArrayList<>();
	private ArrayList<String> msg_key_count = new ArrayList<>();
	private ArrayList<String> msg_rec = new ArrayList<>();
	private ArrayList<String> all_dps = new ArrayList<>();
	private ArrayList<String> all_usernames = new ArrayList<>();
	private ArrayList<String> all_verification = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> storys = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> glob = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> storymap = new ArrayList<>();
	
	private LinearLayout linear_horizon;
	private LinearLayout linear1;
	private LinearLayout linear_tranp;
	private LinearLayout linear_header;
	private LinearLayout linear_story;
	private LinearLayout linear_caller;
	private ListView listview1;
	private TextView textview_header;
	private LinearLayout linear_search_id;
	private ImageView search;
	private LinearLayout request_box;
	private ImageView settings;
	private ImageView search_2;
	private EditText edittext1;
	private ImageView imageview_close_seach;
	private ImageView request;
	private TextView request_count;
	private LinearLayout linear_story_main;
	private RecyclerView recyclerview1;
	private LinearLayout story_bg;
	private TextView textview2;
	private CircleImageView story_img;
	private ImageView imageview_plus;
	private TextView textview_user_call;
	private TextView textview_calling;
	private ImageView img_call;
	
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
	private Intent a = new Intent();
	private SharedPreferences file;
	private StorageReference dp = _firebase_storage.getReference("dp");
	private OnCompleteListener<Uri> _dp_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _dp_download_success_listener;
	private OnSuccessListener _dp_delete_success_listener;
	private OnProgressListener _dp_upload_progress_listener;
	private OnProgressListener _dp_download_progress_listener;
	private OnFailureListener _dp_failure_listener;
	private RequestNetwork net;
	private RequestNetwork.RequestListener _net_request_listener;
	private DatabaseReference inbox = _firebase.getReference("inbox");
	private ChildEventListener _inbox_child_listener;
	private DatabaseReference get_status = _firebase.getReference("get_status");
	private ChildEventListener _get_status_child_listener;
	private Calendar timest = Calendar.getInstance();
	private DatabaseReference users = _firebase.getReference("users");
	private ChildEventListener _users_child_listener;
	private DatabaseReference log = _firebase.getReference("log");
	private ChildEventListener _log_child_listener;
	private DatabaseReference FriendRequests = _firebase.getReference("FriendRequests");
	private ChildEventListener _FriendRequests_child_listener;
	private ObjectAnimator qbim = new ObjectAnimator();
	private OnCompleteListener FCM_onCompleteListener;
	private DatabaseReference update = _firebase.getReference("update");
	private ChildEventListener _update_child_listener;
	private ObjectAnimator a1 = new ObjectAnimator();
	private ObjectAnimator a2 = new ObjectAnimator();
	private TimerTask t;
	private Intent storyV = new Intent();
	private DatabaseReference post = _firebase.getReference("post");
	private ChildEventListener _post_child_listener;
	private DatabaseReference call = _firebase.getReference("call");
	private ChildEventListener _call_child_listener;
	private Vibrator vib;
	private TimerTask tt;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.users);
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
		_fab = findViewById(R.id._fab);
		linear_horizon = findViewById(R.id.linear_horizon);
		linear1 = findViewById(R.id.linear1);
		linear_tranp = findViewById(R.id.linear_tranp);
		linear_header = findViewById(R.id.linear_header);
		linear_story = findViewById(R.id.linear_story);
		linear_caller = findViewById(R.id.linear_caller);
		listview1 = findViewById(R.id.listview1);
		textview_header = findViewById(R.id.textview_header);
		linear_search_id = findViewById(R.id.linear_search_id);
		search = findViewById(R.id.search);
		request_box = findViewById(R.id.request_box);
		settings = findViewById(R.id.settings);
		search_2 = findViewById(R.id.search_2);
		edittext1 = findViewById(R.id.edittext1);
		imageview_close_seach = findViewById(R.id.imageview_close_seach);
		request = findViewById(R.id.request);
		request_count = findViewById(R.id.request_count);
		linear_story_main = findViewById(R.id.linear_story_main);
		recyclerview1 = findViewById(R.id.recyclerview1);
		story_bg = findViewById(R.id.story_bg);
		textview2 = findViewById(R.id.textview2);
		story_img = findViewById(R.id.story_img);
		imageview_plus = findViewById(R.id.imageview_plus);
		textview_user_call = findViewById(R.id.textview_user_call);
		textview_calling = findViewById(R.id.textview_calling);
		img_call = findViewById(R.id.img_call);
		fauth = FirebaseAuth.getInstance();
		file = getSharedPreferences("f", Activity.MODE_PRIVATE);
		net = new RequestNetwork(this);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		textview_header.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				edittext1.requestFocus();
				linear_search_id.setVisibility(View.VISIBLE);
				search.setVisibility(View.GONE);
				textview_header.setVisibility(View.GONE);
				request_box.setVisibility(View.GONE);
			}
		});
		
		settings.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				a.setClass(getApplicationContext(), UpdateActivity.class);
				startActivity(a);
				return true;
			}
		});
		
		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				a.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(a);
				finish();
			}
		});
		
		edittext1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				_SearchListMap(maps, search_users, "username", _charSeq);
				listview1.setAdapter(new Listview1Adapter(search_users));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		imageview_close_seach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				edittext1.setText("");
				SketchwareUtil.hideKeyboard(getApplicationContext());
				linear_search_id.setVisibility(View.INVISIBLE);
				search.setVisibility(View.VISIBLE);
				textview_header.setVisibility(View.VISIBLE);
				request_box.setVisibility(View.VISIBLE);
			}
		});
		
		request.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				a.setClass(getApplicationContext(), RequestlistActivity.class);
				startActivity(a);
			}
		});
		
		linear_story_main.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				a.setClass(getApplicationContext(), AddstoryActivity.class);
				startActivity(a);
			}
		});
		
		story_bg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Pop(a1, a1, story_bg);
				a.setClass(getApplicationContext(), AddstoryActivity.class);
				startActivity(a);
			}
		});
		
		img_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (user2calling) {
					if (!channel.equals("")) {
						a.setClass(getApplicationContext(), CallActivity.class);
						a.putExtra("channel", channel);
						startActivity(a);
					}
				} else {
					if (imCalling) {
						call.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
						linear_caller.setVisibility(View.GONE);
						if (!user2CallID.equals("")) {
							call.child(user2CallID).removeValue();
						}
					}
				}
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				a.setClass(getApplicationContext(), AddFriendsActivity.class);
				startActivity(a);
			}
		});
		
		_dp_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_dp_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_dp_upload_success_listener = new OnCompleteListener<Uri>() {
			@Override
			public void onComplete(Task<Uri> _param1) {
				final String _downloadUrl = _param1.getResult().toString();
				
			}
		};
		
		_dp_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_dp_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_dp_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				
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
				inbox.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						maps = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								maps.add(_map);
							}
						} catch (Exception _e) {
							_e.printStackTrace();
						}
						listview1.setAdapter(new Listview1Adapter(maps));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				inbox.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						maps = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								maps.add(_map);
							}
						} catch (Exception _e) {
							_e.printStackTrace();
						}
						if (_childValue.containsKey("timestamp")) {
							_sortListMap(maps, "timestamp", true, true);
						}
						listview1.setAdapter(new Listview1Adapter(maps));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				inbox.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						maps = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								maps.add(_map);
							}
						} catch (Exception _e) {
							_e.printStackTrace();
						}
						listview1.setAdapter(new Listview1Adapter(maps));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
			}
		};
		inbox.addChildEventListener(_inbox_child_listener);
		
		_get_status_child_listener = new ChildEventListener() {
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
		get_status.addChildEventListener(_get_status_child_listener);
		
		_users_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
					if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
						if (_childValue.containsKey("dp")) {
							if (!"null".equals(_childValue.get("dp").toString())) {
								mydp = _childValue.get("dp").toString();
								file.edit().putString("dp", _childValue.get("dp").toString()).commit();
								Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("dp").toString())).into(story_img);
							} else {
								mydp = "null";
							}
						}
						if (_childValue.containsKey("user")) {
							w_user = _childValue.get("user").toString();
						}
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
					if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
						if (_childValue.containsKey("dp")) {
							if (!"null".equals(_childValue.get("dp").toString())) {
								
							} else {
								
							}
						} else {
							
						}
					} else {
						
					}
				}
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
					if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
						if (_childValue.containsKey("dp")) {
							if (!"null".equals(_childValue.get("dp").toString())) {
								
							} else {
								
							}
						} else {
							
						}
					} else {
						
					}
				}
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
		
		_FriendRequests_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.containsKey("status")) {
					if ("accepted".equals(_childValue.get("status").toString())) {
						
					} else {
						f_req++;
						request_count.setText(String.valueOf((long)(f_req)));
						if (qbim.isRunning()) {
							
						} else {
							qbim.setTarget(request_box);
							qbim.setPropertyName("translationY");
							qbim.setFloatValues((float)(20), (float)(0));
							qbim.setRepeatMode(ValueAnimator.RESTART);
							qbim.setRepeatCount((int)(2));
							qbim.start();
						}
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.containsKey("status")) {
					f_req = 0;
					if ("accepted".equals(_childValue.get("status").toString())) {
						
					} else {
						f_req++;
						request_count.setText(String.valueOf((long)(f_req)));
						if (qbim.isRunning()) {
							
						} else {
							qbim.setTarget(request_box);
							qbim.setPropertyName("translationY");
							qbim.setFloatValues((float)(0), (float)(200));
							qbim.setRepeatCount((int)(1));
							qbim.start();
						}
					}
				}
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
		FriendRequests.addChildEventListener(_FriendRequests_child_listener);
		
		FCM_onCompleteListener = new OnCompleteListener<InstanceIdResult>() {
			@Override
			public void onComplete(Task<InstanceIdResult> task) {
				final boolean _success = task.isSuccessful();
				final String _token = task.getResult().getToken();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
		
		_update_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals("chatme")) {
					if (Double.parseDouble(ver) < Double.parseDouble(_childValue.get("v").toString())) {
						a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						a.setClass(getApplicationContext(), UpdateActivity.class);
						a.putExtra("url", _childValue.get("url").toString());
						startActivity(a);
					} else {
						
					}
				} else {
					
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
		update.addChildEventListener(_update_child_listener);
		
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
		
		_call_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					if (_childValue.containsKey("rec")) {
						if (_childValue.containsKey("channel")) {
							linear_caller.setVisibility(View.VISIBLE);
							user2calling = true;
							imCalling = false;
							if (_childValue.containsKey("user2")) {
								user2CallID = _childValue.get("user2").toString();
							}
							img_call.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)90, 0xFF4CAF50));
							vib.vibrate((long)(300));
							t = new TimerTask() {
								@Override
								public void run() {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											tt.cancel();
											linear_caller.setVisibility(View.GONE);
											call.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
											if (!user2CallID.trim().equals("")) {
												call.child(user2CallID).removeValue();
												user2CallID = "";
											}
										}
									});
								}
							};
							_timer.schedule(t, (int)(15000));
							if (_childValue.containsKey("username")) {
								textview_user_call.setText(_childValue.get("username").toString());
							}
							channel = _childValue.get("channel").toString();
						}
					} else {
						if (_childValue.containsKey("sen")) {
							linear_caller.setVisibility(View.VISIBLE);
							user2calling = false;
							imCalling = true;
							img_call.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)90, 0xFFFD4B63));
							img_call.setRotation((float)(135));
						} else {
							linear_caller.setVisibility(View.GONE);
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
		_check_update();
		_changeActivityFont("fn1");
		_visible_or_not();
		_theme("");
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			_GetStory();
			//Starts Background Service for Notifications!
			
			Intent intent = new Intent(UsersActivity.this, notiservice.class);
			startService(intent);
			
			// Get current user's UID
			final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
			
			// Firebase references
			final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
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
										// Get current data for this inbox entry
										inboxRef.child(userUid).child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
											@Override
											public void onDataChange(@NonNull DataSnapshot snapshot) {
												// Prepare a map to merge data
												HashMap<String, Object> mergedData = new HashMap<>();
												
												// Retain existing keys if they exist
												if (snapshot.hasChild("lastMessage")) {
													mergedData.put("lastMessage", snapshot.child("lastMessage").getValue());
												}
												if (snapshot.hasChild("timestamp")) {
													mergedData.put("timestamp", snapshot.child("timestamp").getValue());
												}
												if (snapshot.hasChild("unreadCount")) {
													mergedData.put("unreadCount", snapshot.child("unreadCount").getValue());
												}
												if (snapshot.hasChild("subimg")) {
													mergedData.put("subimg", snapshot.child("subimg").getValue());
												}
												
												// Add new data
												mergedData.putAll(myData);
												
												// Update the inbox entry with merged data
												inboxRef.child(userUid).child(myUid)
												.updateChildren(mergedData)
												.addOnCompleteListener(new OnCompleteListener<Void>() {
													@Override
													public void onComplete(@NonNull Task<Void> task) {
														if (task.isSuccessful()) {
															// Successfully updated
														} else {
															SketchwareUtil.showMessage(getApplicationContext(), "Failed");
														}
													}
												});
											}
											
											@Override
											public void onCancelled(@NonNull DatabaseError error) {
												SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
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
						a.setClass(getApplicationContext(), UsernameActivity.class);
						a.putExtra("set", "set");
						startActivity(a);
						finish();
						
						SketchwareUtil.showMessage(getApplicationContext(), "Failed to fetch your data.");
					}
				}
				
				@Override
				public void onCancelled(@NonNull DatabaseError error) {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
				}
			});
			
			//here new code--
			
			/*
final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("post");
final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// List to store retrieved stories
final ArrayList<HashMap<String, Object>> storyList = new ArrayList<>();

// Fetch the current user's own stories
postRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull final DataSnapshot snapshot) {
        if (snapshot.exists()) {
            for (final DataSnapshot userSnapshot : snapshot.getChildren()) {
                String userId = userSnapshot.getKey(); // User who shared stories
                for (final DataSnapshot storySnapshot : userSnapshot.getChildren()) {
                    HashMap<String, Object> storyData = (HashMap<String, Object>) storySnapshot.getValue();
                    if (storyData != null) {
                        storyList.add(storyData);
                    }
                }
            }
        }

        // Update UI with retrieved stories
        _updateUIWithStories(new ArrayList<>(storyList));
    }

    @Override
    public void onCancelled(@NonNull final DatabaseError error) {
        
    }
});

*/
			
			final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("post");
			final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
			
			// List to store retrieved stories
			final ArrayList<HashMap<String, Object>> storyList = new ArrayList<>();
			
			// Fetch the current user's own stories
			postRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull final DataSnapshot snapshot) {
					if (snapshot.exists()) {
						for (final DataSnapshot userSnapshot : snapshot.getChildren()) {
							String userId = userSnapshot.getKey(); // User who shared stories
							if (userId != null) {
								for (final DataSnapshot storySnapshot : userSnapshot.getChildren()) {
									// Ensure the data is in the expected format
									if (storySnapshot.exists()) {
										HashMap<String, Object> storyData = (HashMap<String, Object>) storySnapshot.getValue();
										if (storyData != null) {
											storyList.add(storyData);
										}
									}
								}
							}
						}
					}
					
					// Update UI with retrieved stories
					_updateUIWithStories(new ArrayList<>(storyList));
				}
				
				@Override
				public void onCancelled(@NonNull final DatabaseError error) {
					// Handle database read errors
					Log.e("FirebaseError", "Database read failed: " + error.getMessage());
					// Optionally, notify the user of the error
				}
			});
			_friend_requests();
			_Update_status();
			if (SketchwareUtil.isConnected(getApplicationContext())) {
				_setStatus("online");
				inbox.removeEventListener(_inbox_child_listener);
				getuser = "inbox/".concat(FirebaseAuth.getInstance().getCurrentUser().getUid());
				inbox = _firebase.getReference(getuser);
				inbox.addChildEventListener(_inbox_child_listener);
				offline = false;
			} else {
				if (file.contains("userdata_list")) {
					maps = new Gson().fromJson(file.getString("userdata_list", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					listview1.setAdapter(new Listview1Adapter(maps));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					offline = true;
				}
			}
			// Firebase references
			final String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
			final String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
			
			// Check for all UUIDs linked to the current user's email
			usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
					List<String> oldUserUids = new ArrayList<>();
					
					// Collect all UUIDs linked to the current user's email
					for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
						String userEmail = userSnapshot.child("email").getValue(String.class);
						if (userEmail != null && userEmail.equals(currentUserEmail) && !userSnapshot.getKey().equals(currentUserUid)) {
							oldUserUids.add(userSnapshot.getKey());
						}
					}
					
					if (!oldUserUids.isEmpty()) {
						// Step 1: Copy each old UUID's data to users/currentUserUid/
						for (String oldUid : oldUserUids) {
							final String oldUserUid = oldUid;  // Declare oldUserUid as final inside loop
							
							usersRef.child(oldUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot oldUserSnapshot) {
									Map<String, Object> userData = new HashMap<>();
									for (DataSnapshot data : oldUserSnapshot.getChildren()) {
										userData.put(data.getKey(), data.getValue());
									}
									userData.put("id", currentUserUid); // Replace ID with current UUID
									
									// Update current user's UID with data from old UUID
									usersRef.child(currentUserUid).updateChildren(userData)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if (task.isSuccessful()) {
												SketchwareUtil.showMessage(getApplicationContext(), "successfully.");
												
												// Step 2: Copy inbox data from oldUserUid to currentUserUid
												inboxRef.child(oldUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
													@Override
													public void onDataChange(@NonNull DataSnapshot inboxSnapshot) {
														Map<String, Object> inboxData = new HashMap<>();
														for (DataSnapshot data : inboxSnapshot.getChildren()) {
															inboxData.put(data.getKey(), data.getValue());
														}
														
														// Copy inbox data to inbox/currentUserUid
														inboxRef.child(currentUserUid).updateChildren(inboxData)
														.addOnCompleteListener(new OnCompleteListener<Void>() {
															@Override
															public void onComplete(@NonNull Task<Void> task) {
																if (task.isSuccessful()) {
																	SketchwareUtil.showMessage(getApplicationContext(), "Inbox data successfully.");
																	
																	// Step 3: Delete old user data and inbox data
																	usersRef.child(oldUserUid).removeValue();
																	inboxRef.child(oldUserUid).removeValue();
																} else {
																	SketchwareUtil.showMessage(getApplicationContext(), "Failed to get inbox data.");
																}
															}
														});
													}
													
													@Override
													public void onCancelled(@NonNull DatabaseError error) {
														SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
													}
												});
											} else {
												SketchwareUtil.showMessage(getApplicationContext(), "Failed to get user data.");
											}
										}
									});
								}
								
								@Override
								public void onCancelled(@NonNull DatabaseError error) {
									SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
								}
							});
						}
					} else {
						
					}
				}
				
				@Override
				public void onCancelled(@NonNull DatabaseError error) {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: " + error.getMessage());
				}
			});
			
			linear_header.setElevation((float)8);
		}
		linear_story.setVisibility(View.VISIBLE);
		linear_caller.setVisibility(View.GONE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			timest = Calendar.getInstance();
			_setStatus("Last Seen : ".concat(new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())));
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			_theme("");
		} else {
			a.setClass(getApplicationContext(), LogActivity.class);
			a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(a);
			finish();
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if (0 < maps.size()) {
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
		_setStatus("online");
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("call");
			String userUuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
			
			ref.child(userUuid).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					if (snapshot.exists()) {
						linear_caller.setVisibility(View.VISIBLE);
					} else {
						linear_caller.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void onCancelled(DatabaseError error) {
					showMessage("Error: " + error.getMessage());
				}
			});
			
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		_setStatus("offline");
	}
	public void _visible_or_not() {
		linear_search_id.setVisibility(View.INVISIBLE);
		recyclerview1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
	}
	
	
	public void _theme(final String _color) {
		if (file.contains("wallxP")) {
			_Transparent_StatusBar();
			_pickImage_toLinear(linear1, file.getString("wallxP", ""));
			linear_header.setBackgroundColor(Color.TRANSPARENT);
			_Setcolor(textview_header, file.getString("wallxt", ""));
			_ImageColor(imageview_plus, file.getString("wallxt", ""));
			_ImageColor(search, file.getString("wallxst", ""));
			_ImageColor(settings, file.getString("wallxst", ""));
			linear_tranp.setVisibility(View.VISIBLE);
		} else {
			search.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
			search_2.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
			imageview_close_seach.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
			linear_tranp.setVisibility(View.GONE);
			if ("dark".equals(file.getString("theme", ""))) {
				linear_header.setBackgroundColor(0xFF212121);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					Window w =UsersActivity.this.getWindow();
					w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF212121);
				}
				imageview_plus.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFF303032));
				linear1.setBackgroundColor(0xFF303032);
				_ImageColor(imageview_plus, "#ffffff");
			} else {
				_ImageColor(imageview_plus, "#000000");
				linear1.setBackgroundColor(0xFFFFFFFF);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					Window w =UsersActivity.this.getWindow();
					w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF007BA7);
				}
				linear_header.setBackgroundColor(0xFF007BA7);
				imageview_plus.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFFFFFFFF));
			}
		}
		img_call.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFFFD4B63));
		linear_caller.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)25, 0xFFF5F9FC));
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
	
	
	public void _proshow() {
		final AlertDialog c1 = new AlertDialog.Builder(UsersActivity.this).create();
		LayoutInflater c1LI = getLayoutInflater();
		View c1CV = (View) c1LI.inflate(R.layout.proimg, null);
		c1.setView(c1CV);
		final LinearLayout linear1 = (LinearLayout)
		c1CV.findViewById(R.id.linear1);
		final ImageView imageview1 = (ImageView)
		c1CV.findViewById(R.id.imageview1);
		final TextView b2 = (TextView)
		c1CV.findViewById(R.id.b2);
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (FileUtil.isExistFile("/storage/emulated/0/SC/".concat("IMG_profile_".concat(String.valueOf((long)(DpSaveLink.hashCode())).concat(".png"))))) {
					SketchwareUtil.showMessage(getApplicationContext(), "already saved!");
				} else {
					try {
						// Code that may throw an exception
						
						
						_firebase_storage.getReferenceFromUrl(DpSaveLink).getFile(new File("/storage/emulated/0/SC/".concat("IMG_profile_".concat(String.valueOf((long)(DpSaveLink.hashCode())).concat(".png"))))).addOnSuccessListener(_dp_download_success_listener).addOnFailureListener(_dp_failure_listener).addOnProgressListener(_dp_download_progress_listener);
					} catch (Exception e) {
						// Handle the exception
						
					}
					c1.dismiss();
				}
			}
		});
		final ImageView close = (ImageView)
		c1CV.findViewById(R.id.close);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				c1.dismiss();
			}
		});
		final TextView t1 = (TextView)
		c1CV.findViewById(R.id.t1);
		t1.setText(username);
		final TextView t2 = (TextView)
		c1CV.findViewById(R.id.t2);
		t2.setText(" @".concat(id));
		Glide.with(getApplicationContext()).load(Uri.parse(proshow_link)).into(imageview1);
		c1.show();
	}
	
	
	public void _removeListViewSelect(final ListView _listview) {
		_listview.setSelector(android.R.color.transparent);
	}
	
	
	public void _SearchListMap(final ArrayList<HashMap<String, Object>> _list_map, final ArrayList<HashMap<String, Object>> _list_serach, final String _key, final String _text) {
		position = 0;
		_list_serach.clear();
		if (_text.equals("")) {
			for(int _repeat48 = 0; _repeat48 < (int)(_list_map.size()); _repeat48++) {
				map_search = _list_map.get((int)position);
				_list_serach.add(map_search);
				position++;
			}
		} else {
			try{
				for(int _repeat13 = 0; _repeat13 < (int)(_list_map.size()); _repeat13++) {
					if (_list_map.get((int)position).containsKey(_key)) {
						if (_list_map.get((int)position).get(_key).toString().toLowerCase().contains(_text.toLowerCase())) {
							map_search = _list_map.get((int)position);
							_list_serach.add(map_search);
						}
					}
					position++;
				}
			}catch(Exception e){
				SketchwareUtil.showMessage(getApplicationContext(), "Error: "+e.toString());
			}
		}
	}
	
	
	public void _setStatus(final String _status) {
		status = new HashMap<>();
		status.put("status", _status.toLowerCase().trim());
		get_status.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(status);
		status.clear();
	}
	
	
	public void _Send_log(final String _status) {
		data = new HashMap<>();
		data.put("log", _status);
		log.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
		data.clear();
	}
	
	
	public void _friend_requests() {
		FriendRequests.removeEventListener(_FriendRequests_child_listener);
		myf = "FriendRequests/".concat(FirebaseAuth.getInstance().getCurrentUser().getUid());
		FriendRequests = _firebase.getReference(myf);
		FriendRequests.addChildEventListener(_FriendRequests_child_listener);
	}
	
	
	public void _check_update() {
		package_name = "tpass.chatme";
		try {
			android.content.pm.PackageInfo pinfo = getPackageManager().getPackageInfo(package_name, android.content.pm.PackageManager.GET_ACTIVITIES);
			ver = pinfo.versionName; }
		catch (Exception e){ showMessage(e.toString()); }
	}
	
	
	public void _Update_status() {
		// Get current user's UID
		final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		
		// Firebase references
		final DatabaseReference inboxRef = FirebaseDatabase.getInstance().getReference("inbox");
		final DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("get_status");
		
		// Fetch all inbox entries for the current user
		inboxRef.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot inboxSnapshot) {
				if (inboxSnapshot.exists()) {
					for (DataSnapshot userInbox : inboxSnapshot.getChildren()) {
						final String recipientUid = userInbox.getKey(); // UID of the recipient
						
						// Fetch status for the recipient from "get_status"
						statusRef.child(recipientUid).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(@NonNull DataSnapshot statusSnapshot) {
								if (statusSnapshot.exists()) {
									// Get the status of the recipient
									final Object recipientStatus = statusSnapshot.getValue();
									
									// Update only the "status" key for the recipient in the current user's inbox
									inboxRef.child(myUid).child(recipientUid)
									.child("status")
									.setValue(recipientStatus)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if (task.isSuccessful()) {
												// Successfully updated the status
											} else {
												//errehere
											}
										}
									});
								} else {
									// Status not found for the recipient
									
								}
							}
							
							@Override
							public void onCancelled(@NonNull DatabaseError error) {
								//errors here
							}
						});
					}
				} else {
					// New user
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				SketchwareUtil.showMessage(getApplicationContext(), "Error fetching inbox: " + error.getMessage());
			}
		});
		
	}
	
	
	public void _SetTimeAgo(final double _timestamp, final TextView _extra) {
		// Get the current timestamp
		Calendar timest = Calendar.getInstance();
		long currentTime = timest.getTimeInMillis();
		
		// Calculate the difference in milliseconds (cast _timestamp to long)
		long difference = currentTime - (long) _timestamp;
		
		// Determine the time format and set it directly to the TextView
		if (difference < 10000) { // Less than 10 seconds
			_extra.setText("Just now");
		} else if (difference < 60000) { // Less than 1 minute
			_extra.setText((difference / 1000) + " sec ago");
		} else if (difference < 3600000) { // Less than 1 hour
			_extra.setText((difference / 60000) + " min ago");
		} else if (difference < 86400000) { // Less than 1 day
			_extra.setText((difference / 3600000) + " hr ago");
		} else if (difference < 604800000) { // Less than 1 week
			_extra.setText((difference / 86400000) + " days ago");
		} else if (difference < 2592000000L) { // Less than 1 month
			_extra.setText((difference / 604800000) + " weeks ago");
		} else if (difference < 31536000000L) { // Less than 1 year
			_extra.setText((difference / 2592000000L) + " months ago");
		} else { // More than 1 year
			_extra.setText((difference / 31536000000L) + " years ago");
		}
		
	}
	
	
	public void _sortListMap(final ArrayList<HashMap<String, Object>> _maps, final String _key, final boolean _descending, final boolean _numeric) {
		
		Collections.sort(_maps, new Comparator<HashMap<String, Object>>() {
			@Override
			public int compare(HashMap<String, Object> map1, HashMap<String, Object> map2) {
				Object value1 = map1.get(_key);
				Object value2 = map2.get(_key);
				
				if (value1 == null || value2 == null) {
					return 0; // Null-safe comparison
				}
				
				if (_numeric) {
					try {
						// Use Long to handle large numbers
						long num1 = Long.parseLong(value1.toString());
						long num2 = Long.parseLong(value2.toString());
						return  _descending ? Long.compare(num2, num1) : Long.compare(num1, num2);
					} catch (NumberFormatException e) {
						return 0; // Handle invalid formats gracefully
					}
				} else {
					String str1 = value1.toString();
					String str2 = value2.toString();
					return _descending ? str2.compareTo(str1) : str1.compareTo(str2);
				}
			}
		});
		
		
	}
	
	
	public void _Pop(final ObjectAnimator _x, final ObjectAnimator _y, final View _view) {
		_x.setTarget(_view);
		_x.setPropertyName("scaleX");
		_x.setFloatValues((float)(1), (float)(0.8d));
		_x.setDuration((int)(100));
		_x.setRepeatMode(ValueAnimator.REVERSE);
		_x.setRepeatCount((int)(1));
		_x.setInterpolator(new LinearInterpolator());
		_x.start();
		_y.setTarget(_view);
		_y.setPropertyName("scaleY");
		_y.setFloatValues((float)(1), (float)(0.8d));
		_y.setDuration((int)(100));
		_y.setRepeatMode(ValueAnimator.REVERSE);
		_y.setRepeatCount((int)(1));
		_y.setInterpolator(new LinearInterpolator());
		_y.start();
	}
	
	
	public void _ImageColor(final ImageView _image, final String _color) {
		_image.setColorFilter(Color.parseColor(_color),PorterDuff.Mode.SRC_ATOP);
	}
	
	
	public void _GetStory() {
		post.removeEventListener(_post_child_listener);
		storyref = "post".concat("/".concat(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/")));
		post = _firebase.getReference(storyref);
		post.addChildEventListener(_post_child_listener);
	}
	
	
	public void _ProgressBarColour(final ProgressBar _progressbar, final String _color) {
		if (android.os.Build.VERSION.SDK_INT >=21) {
			_progressbar.getIndeterminateDrawable() .setColorFilter(Color.parseColor(_color), PorterDuff.Mode.SRC_IN);
		}
	}
	
	
	public void _pickImage_toLinear(final View _view, final String _path) {
		_view.setBackground(new android.graphics.drawable.BitmapDrawable(getResources(), FileUtil.decodeSampleBitmapFromPath(_path, 1024, 1024)));
	}
	
	
	public void _Setcolor(final TextView _view, final String _color) {
		_view.setTextColor(Color.parseColor(_color)); // Set text color to white
		
	}
	
	
	public void _Transparent_StatusBar() {
		//Insert into oncreate()
		if(Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
			| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
	}
	
	
	public void _updateUIWithStories(final ArrayList<HashMap<String, Object>> _stories) {
		recyclerview1.setAdapter(new Recyclerview1Adapter(_stories));
	}
	
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.users_list, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final LinearLayout system = _view.findViewById(R.id.system);
			final LinearLayout linear_dp = _view.findViewById(R.id.linear_dp);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final de.hdodenhof.circleimageview.CircleImageView circleimageview1 = _view.findViewById(R.id.circleimageview1);
			final de.hdodenhof.circleimageview.CircleImageView disappeare_icon = _view.findViewById(R.id.disappeare_icon);
			final LinearLayout linear3 = _view.findViewById(R.id.linear3);
			final LinearLayout linearSubTitle = _view.findViewById(R.id.linearSubTitle);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final ImageView verified = _view.findViewById(R.id.verified);
			final ImageView sub_image = _view.findViewById(R.id.sub_image);
			final TextView subtitle_text = _view.findViewById(R.id.subtitle_text);
			final LinearLayout linear5 = _view.findViewById(R.id.linear5);
			final LinearLayout linear_message_dot = _view.findViewById(R.id.linear_message_dot);
			final TextView extra = _view.findViewById(R.id.extra);
			final TextView message_count = _view.findViewById(R.id.message_count);
			final ImageView sys_icon = _view.findViewById(R.id.sys_icon);
			final TextView sys_text = _view.findViewById(R.id.sys_text);
			
			if (_data.get((int)_position).containsKey("system")) {
				system.setVisibility(View.VISIBLE);
				linear1.setVisibility(View.GONE);
				sys_text.setText(_data.get((int)_position).get("sys").toString());
				if (_data.get((int)_position).containsKey("sys_color")) {
					_Setcolor(sys_text, _data.get((int)_position).get("sys_color").toString());
				}
				if (_data.get((int)_position).containsKey("sys_icon")) {
					sys_icon.setVisibility(View.VISIBLE);
					Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("sys_icon").toString())).into(sys_icon);
				} else {
					sys_icon.setVisibility(View.GONE);
				}
			} else {
				system.setVisibility(View.GONE);
				linear1.setVisibility(View.VISIBLE);
				// Updated Code
				
				textview1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/fn1.ttf"), 1);
				subtitle_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/fn1.ttf"), 0);
				
				if (_data.get((int) _position).containsKey("username")) {
					if (_data.get((int) _position).containsKey("block_".concat(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
						textview1.setText("ChatUser".concat(String.valueOf((long) (SketchwareUtil.getRandom((int) (12), (int) (12365))))));
					} else {
						textview1.setText(_data.get((int) _position).get("username").toString());
					}
				}
				
				if (file.contains("wallxP")) {
					_Setcolor(textview1, file.getString("wallxt", ""));
					_Setcolor(subtitle_text, file.getString("wallxst", ""));
					_Setcolor(extra, file.getString("wallxst", ""));
				} else {
					if ("dark".equals(file.getString("theme", ""))) {
						linear_message_dot.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b) {
								this.setCornerRadius(a);
								this.setColor(b);
								return this;
							}
						}.getIns((int) 100, 0xFF2C3E50));
						textview1.setTextColor(0xFFEAF6FF);
						subtitle_text.setTextColor(0xFF9E9E9E);
					} else {
						textview1.setTextColor(0xFF2C3E50);
						linear_message_dot.setBackground(new GradientDrawable() {
							public GradientDrawable getIns(int a, int b) {
								this.setCornerRadius(a);
								this.setColor(b);
								return this;
							}
						}.getIns((int) 100, 0xFF007BA7));
						subtitle_text.setTextColor(0xFF9E9E9E);
					}
				}
				
				if (_data.get((int) _position).containsKey("unreadCount")) {
					if (0 == Double.parseDouble(_data.get((int) _position).get("unreadCount").toString())) {
						linear_message_dot.setVisibility(View.GONE);
						subtitle_text.setTextColor(0xFFFAFAFA);
					} else {
						subtitle_text.setTextColor(0xFF007BA7);
						linear_message_dot.setVisibility(View.VISIBLE);
						message_count.setText(String.valueOf((long) (Double.parseDouble(_data.get((int) _position).get("unreadCount").toString()))));
					}
				} else {
					linear_message_dot.setVisibility(View.GONE);
				}
				
				if (_data.get((int) _position).containsKey("timestamp")) {
					extra.setVisibility(View.VISIBLE);
					_SetTimeAgo(Double.parseDouble(_data.get((int) _position).get("timestamp").toString()), extra);
				} else {
					extra.setVisibility(View.GONE);
				}
				
				if (_data.get((int) _position).containsKey("subimg")) {
					if ("null".equals(_data.get((int) _position).get("subimg").toString())) {
						sub_image.setVisibility(View.GONE);
						sub_image.setImageResource(R.drawable.logo);
					} else {
						sub_image.setVisibility(View.VISIBLE);
						sub_image.setImageResource(R.drawable.sub_img);
					}
				} else {
					sub_image.setVisibility(View.GONE);
				}
				
				if (_data.get((int) _position).containsKey("lastMessage")) {
					subtitle_text.setText(_data.get((int) _position).get("lastMessage").toString());
				} else {
					if (_data.get((int) _position).containsKey("user")) {
						subtitle_text.setText(" @".concat(_data.get((int) _position).get("user").toString()));
					}
				}
				
				if (_data.get((int) _position).containsKey("id")) {
					if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(_data.get((int) _position).get("id").toString())) {
						linear1.setVisibility(View.GONE);
					} else {
						if (_data.get((int) _position).containsKey("hide")) {
							linear1.setVisibility(View.GONE);
						} else {
							linear1.setVisibility(View.VISIBLE);
						}
					}
				} else {
					linear1.setVisibility(View.GONE);
				}
				
				if (_data.get((int) _position).containsKey("dp")) {
					if ("null".equals(_data.get((int) _position).get("dp").toString())) {
						circleimageview1.setImageResource(R.drawable.brw);
					} else {
						if (_data.get((int) _position).containsKey("block_".concat(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
							circleimageview1.setImageResource(R.drawable.banned_user);
						} else {
							if (_data.get((int) _position).containsKey("block_".concat(_data.get((int) _position).get("id").toString()))) {
								circleimageview1.setImageResource(R.drawable.ban);
							} else {
								Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int) _position).get("dp").toString())).into(circleimageview1);
							}
						}
					}
				}
				
				if (_data.get((int) _position).containsKey("v")) {
					if (_data.get((int) _position).get("v").toString().equals("true")) {
						verified.setVisibility(View.VISIBLE);
						verified.setImageResource(R.drawable.verify563_3);
					} else {
						verified.setVisibility(View.GONE);
					}
				} else {
					verified.setVisibility(View.GONE);
					extra.setVisibility(View.GONE);
				}
				
				if (_data.get((int) _position).containsKey("dis")) {
					disappeare_icon.setVisibility(View.VISIBLE);
					disappeare_icon.setImageResource(R.drawable.dis);
				} else {
					if (_data.get((int) _position).containsKey("lock")) {
						disappeare_icon.setImageResource(R.drawable.lock);
						disappeare_icon.setVisibility(View.VISIBLE);
					} else {
						disappeare_icon.setVisibility(View.GONE);
					}
				}
				
				linear1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						if (_data.get((int) _position).containsKey("lock")) {
							verified.setImageResource(R.drawable.lock);
							verified.setVisibility(View.VISIBLE);
						} else {
							if (_data.get((int) _position).containsKey("block_".concat(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
								SketchwareUtil.showMessage(getApplicationContext(), "Can't Send messages to this user.");
							} else {
								if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
									if (_data.get((int) _position).containsKey("type")) {
										if ("group".equals(_data.get((int) _position).get("type").toString())) {
											a.setClass(getApplicationContext(), ChatActivity.class);
											a.putExtra("user1", FirebaseAuth.getInstance().getCurrentUser().getUid());
											a.putExtra("group", "true"); // Group chat
											a.putExtra("Tuser", "false"); // User chat
										} else {
											a.setClass(getApplicationContext(), ChatActivity.class);
											a.putExtra("user1", FirebaseAuth.getInstance().getCurrentUser().getUid());
											a.putExtra("Tuser", "true"); // User chat
											a.putExtra("group", "false"); // Group chat
										}
									} else {
										a.setClass(getApplicationContext(), ChatActivity.class);
										a.putExtra("user1", FirebaseAuth.getInstance().getCurrentUser().getUid());
										a.putExtra("Tuser", "true"); // Default to user chat
									}
									
									// Common Intent setup
									if (_data.get((int) _position).containsKey("id")) {
										a.putExtra("user2", _data.get((int) _position).get("id").toString());
									} else {
										a.putExtra("user2", "0000");
									}
									
									if (_data.get((int) _position).containsKey("user")) {
										a.putExtra("user", _data.get((int) _position).get("user").toString());
									} else {
										a.putExtra("user", "Unknown Person");
									}
									
									if (_data.get((int) _position).containsKey("dp")) {
										a.putExtra("dp", _data.get((int) _position).get("dp").toString());
									} else {
										a.putExtra("dp", "null");
									}
									
									startActivity(a);
									
									if (_data.get((int) _position).containsKey("unreadCount")) {
										// Reset unread count logic
										final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
										final String recipientId = _data.get((int) _position).get("id").toString();
										DatabaseReference inboxRef = FirebaseDatabase.getInstance()
										.getReference("inbox")
										.child(myUid)
										.child(recipientId);
										HashMap<String, Object> updateMap = new HashMap<>();
										updateMap.put("unreadCount", 0);
										inboxRef.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
											@Override
											public void onComplete(@NonNull Task<Void> task) {
												if (!task.isSuccessful()) {
													SketchwareUtil.showMessage(getApplicationContext(), "Failed to reset.");
												}
											}
										}).addOnFailureListener(new OnFailureListener() {
											@Override
											public void onFailure(@NonNull Exception e) {
												SketchwareUtil.showMessage(getApplicationContext(), "Error: " + e.getMessage());
											}
										});
									}
								}
							}
						}
					}
				});
				
				circleimageview1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						if (SketchwareUtil.isConnected(getApplicationContext())) {
							try {
								if (_data.get((int) _position).containsKey("dp")) {
									if ("null".equals(_data.get((int) _position).get("dp").toString())) {
										SketchwareUtil.showMessage(getApplicationContext(), "Profile Not Updated!");
										circleimageview1.setImageResource(R.drawable.brw);
									} else {
										proshow_link = _data.get((int) _position).get("dp").toString();
										DpSaveLink = _data.get((int) _position).get("dp").toString();
										username = _data.get((int) _position).get("username").toString();
										id = _data.get((int) _position).get("user").toString();
										_proshow();
									}
								} else {
									SketchwareUtil.showMessage(getApplicationContext(), "Profile Not Updated!");
									circleimageview1.setImageResource(R.drawable.brw);
								}
							} catch (Exception e) {
								SketchwareUtil.showMessage(getApplicationContext(), "Something missing!");
							}
						}
					}
				});
				
				if (SketchwareUtil.isConnected(getApplicationContext())) {
					file.edit().putString("userdata_list", new Gson().toJson(maps)).commit();
				}
			}
			
			return _view;
		}
	}
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.storyview, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout linear_story_main = _view.findViewById(R.id.linear_story_main);
			final LinearLayout story_bg = _view.findViewById(R.id.story_bg);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			final de.hdodenhof.circleimageview.CircleImageView story_img = _view.findViewById(R.id.story_img);
			
			if (_data.get((int)_position).containsKey("story_username")) {
				textview2.setText(_data.get((int)_position).get("story_username").toString());
			}
			if (_data.get((int)_position).containsKey("story_url")) {
				if ("null".equals(_data.get((int)_position).get("story_url").toString())) {
					linear_story_main.setVisibility(View.GONE);
				} else {
					Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("story_url").toString())).into(story_img);
					linear_story_main.setVisibility(View.VISIBLE);
				}
			} else {
				linear_story_main.setVisibility(View.GONE);
			}
			if (_data.get((int)_position).containsKey("story_key")) {
				if (file.contains("story".concat(String.valueOf((long)(_data.get((int)_position).get("story_key").toString().hashCode()))))) {
					if ("seen".equals(file.getString("story".concat(String.valueOf((long)(_data.get((int)_position).get("story_key").toString().hashCode()))), ""))) {
						story_bg.setBackgroundResource(R.drawable.story_rings_2);
					} else {
						story_bg.setBackgroundResource(R.drawable.story_rings_1);
					}
				} else {
					story_bg.setBackgroundResource(R.drawable.story_rings_1);
				}
				linear_story_main.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						story_key = _data.get((int)_position).get("story_key").toString();
						_Pop(a1, a2, story_bg);
						Intent storyV = new Intent(getApplicationContext(), AddstoryActivity.class);
						storyV.putExtra("view", "true");
						if ("image".equals(_data.get((int)_position).get("story_type").toString())) {
							storyV.putExtra("image", "true");
						}
						else {
							storyV.putExtra("video", "true");
						}
						storyV.putExtra("url", _data.get((int)_position).get("story_url").toString());
						storyV.putExtra("username", _data.get((int)_position).get("story_username").toString());
						startActivity(storyV);
						
						
						// Apply smooth fade animation
						overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						
						
						
					}
				});
			}
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
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