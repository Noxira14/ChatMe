package tpass.chatme;

import android.Manifest;
import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.media.MediaPlayer;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import de.hdodenhof.circleimageview.*;
import io.agora.rtc.*;
import java.io.*;
import java.io.File;
import java.io.InputStream;
import java.text.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {
	
	public final int REQ_CD_FPICK = 101;
	public final int REQ_CD_CAMXD = 102;
	public final int REQ_CD_MUSIC = 103;
	public final int REQ_CD_ALLFILES = 104;
	public final int REQ_CD_PRTFILES = 105;
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private String chatroom = "";
	private String chatcopy = "";
	private HashMap<String, Object> status = new HashMap<>();
	private String msg_trim = "";
	private HashMap<String, Object> chatmap = new HashMap<>();
	private String chat_key = "";
	private String sender_name = "";
	private boolean voiceplay = false;
	private double voice_pose = 0;
	private String reco_path = "";
	private double audio_time = 0;
	private String reco_name = "";
	private boolean VoiceMessage = false;
	private boolean Img = false;
	private String record_txt = "";
	private boolean play = false;
	private String url = "";
	private String filename = "";
	private String voice_file_location = "";
	private String voice_save = "";
	private String fontName = "";
	private String typeace = "";
	private String getMSG_key = "";
	private HashMap<String, Object> HEADERS = new HashMap<>();
	private String ServerKey = "";
	private HashMap<String, Object> NOTIFICATION_EXTRA_DATA = new HashMap<>();
	private HashMap<String, Object> Notification_BODY = new HashMap<>();
	private HashMap<String, Object> MAIN_BODY = new HashMap<>();
	private String privete_key = "";
	private String user2_name = "";
	private String path = "";
	private String path_name = "";
	private HashMap<String, Object> staS = new HashMap<>();
	private boolean block = false;
	private boolean puase = false;
	private String replytrue = "";
	MediaRecorder recorder;
	private String audioPath = "";
	private String AudioURL = "";
	private double PosePlay = 0;
	private double prog = 0;
	private boolean prt = false;
	private String encrypted = "";
	private String decrypted = "";
	private String dKey = "";
	private String netURLPreview = "";
	private String patt = "";
	private String object_clicked = "";
	private double menT = 0;
	private boolean audio = false;
	private boolean video = false;
	private boolean allFir = false;
	private String mydp = "";
	private String colorHex = "";
	private String user2DP = "";
	private String admin = "";
	private String adminName = "";
	private HashMap<String, Object> call1 = new HashMap<>();
	private HashMap<String, Object> call3 = new HashMap<>();
	private boolean IsCalling = false;
	private String channel = "";
	
	private ArrayList<HashMap<String, Object>> chats = new ArrayList<>();
	private ArrayList<String> paths = new ArrayList<>();
	private ArrayList<String> userMention = new ArrayList<>();
	private ArrayList<String> userids = new ArrayList<>();
	
	private LinearLayout linear_head_top;
	private LinearLayout main_bg;
	private LinearLayout linear_message;
	private ImageView back;
	private LinearLayout linear_dp_box;
	private LinearLayout linear12;
	private ImageView voiceCall;
	private ImageView videoCall;
	private ImageView settings;
	private CircleImageView dps;
	private ImageView status_dots;
	private LinearLayout linear16;
	private LinearLayout linearF;
	private TextView name;
	private ImageView verified;
	private TextView user_status;
	private LinearLayout upload_image;
	private RecyclerView recyclerview1;
	private TextView typeing;
	private ImageView imageview_up_symbol;
	private TextView textview_up_progress;
	private ProgressBar progressbar1;
	private LinearLayout message_body_in_reply;
	private LinearLayout sound_record_body;
	private LinearLayout message_body_in_message;
	private LinearLayout linearExtra_attach;
	private CardView cardview1;
	private LinearLayout linear_use_for_line;
	private LinearLayout message_body_in_reply_chld;
	private ImageView message_body_in_reply_cancel;
	private TextView message_body_in_reply_title;
	private TextView message_body_in_reply_text;
	private ImageView sound_record_cancel;
	private TextView recording_text;
	private ImageView send_audio;
	private ImageView image___atch;
	private LinearLayout msg_spc;
	private ImageView view1;
	private EditText message;
	private ImageView sound;
	private ImageView send;
	private ImageView image_camera_attach;
	private ImageView image_music_attach;
	private ImageView attach_files;
	private ImageView media;
	
	private Intent a = new Intent();
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
	private DatabaseReference Chat1 = _firebase.getReference("+ chatroom +");
	private ChildEventListener _Chat1_child_listener;
	private DatabaseReference Chat2 = _firebase.getReference("+ chatcopy +");
	private ChildEventListener _Chat2_child_listener;
	private DatabaseReference get_status = _firebase.getReference("get_status");
	private ChildEventListener _get_status_child_listener;
	private TimerTask ts;
	private RequestNetwork net;
	private RequestNetwork.RequestListener _net_request_listener;
	private Calendar timest = Calendar.getInstance();
	private MediaPlayer player;
	private Vibrator f;
	private StorageReference storage = _firebase_storage.getReference("storage");
	private OnCompleteListener<Uri> _storage_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _storage_download_success_listener;
	private OnSuccessListener _storage_delete_success_listener;
	private OnProgressListener _storage_upload_progress_listener;
	private OnProgressListener _storage_download_progress_listener;
	private OnFailureListener _storage_failure_listener;
	private DatabaseReference users = _firebase.getReference("users");
	private ChildEventListener _users_child_listener;
	private Calendar t24 = Calendar.getInstance();
	private RequestNetwork requestNetwork;
	private RequestNetwork.RequestListener _requestNetwork_request_listener;
	private OnCompleteListener FCM_onCompleteListener;
	private Intent fpick = new Intent(Intent.ACTION_GET_CONTENT);
	private DatabaseReference pending = _firebase.getReference("pending");
	private ChildEventListener _pending_child_listener;
	private MediaPlayer mp6;
	private Intent intent = new Intent();
	private Intent camxd = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	private File _file_camxd;
	private Intent music = new Intent(Intent.ACTION_GET_CONTENT);
	private Intent allfiles = new Intent(Intent.ACTION_GET_CONTENT);
	private DatabaseReference group = _firebase.getReference("group");
	private ChildEventListener _group_child_listener;
	private Intent prtFiles = new Intent(Intent.ACTION_GET_CONTENT);
	private DatabaseReference call = _firebase.getReference("call");
	private ChildEventListener _call_child_listener;
	private ObjectAnimator o1 = new ObjectAnimator();
	private ObjectAnimator o2 = new ObjectAnimator();
	private TimerTask callT;
	private Vibrator vib;
	private TimerTask ct;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.chat);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
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
		linear_head_top = findViewById(R.id.linear_head_top);
		main_bg = findViewById(R.id.main_bg);
		linear_message = findViewById(R.id.linear_message);
		back = findViewById(R.id.back);
		linear_dp_box = findViewById(R.id.linear_dp_box);
		linear12 = findViewById(R.id.linear12);
		voiceCall = findViewById(R.id.voiceCall);
		videoCall = findViewById(R.id.videoCall);
		settings = findViewById(R.id.settings);
		dps = findViewById(R.id.dps);
		status_dots = findViewById(R.id.status_dots);
		linear16 = findViewById(R.id.linear16);
		linearF = findViewById(R.id.linearF);
		name = findViewById(R.id.name);
		verified = findViewById(R.id.verified);
		user_status = findViewById(R.id.user_status);
		upload_image = findViewById(R.id.upload_image);
		recyclerview1 = findViewById(R.id.recyclerview1);
		typeing = findViewById(R.id.typeing);
		imageview_up_symbol = findViewById(R.id.imageview_up_symbol);
		textview_up_progress = findViewById(R.id.textview_up_progress);
		progressbar1 = findViewById(R.id.progressbar1);
		message_body_in_reply = findViewById(R.id.message_body_in_reply);
		sound_record_body = findViewById(R.id.sound_record_body);
		message_body_in_message = findViewById(R.id.message_body_in_message);
		linearExtra_attach = findViewById(R.id.linearExtra_attach);
		cardview1 = findViewById(R.id.cardview1);
		linear_use_for_line = findViewById(R.id.linear_use_for_line);
		message_body_in_reply_chld = findViewById(R.id.message_body_in_reply_chld);
		message_body_in_reply_cancel = findViewById(R.id.message_body_in_reply_cancel);
		message_body_in_reply_title = findViewById(R.id.message_body_in_reply_title);
		message_body_in_reply_text = findViewById(R.id.message_body_in_reply_text);
		sound_record_cancel = findViewById(R.id.sound_record_cancel);
		recording_text = findViewById(R.id.recording_text);
		send_audio = findViewById(R.id.send_audio);
		image___atch = findViewById(R.id.image___atch);
		msg_spc = findViewById(R.id.msg_spc);
		view1 = findViewById(R.id.view1);
		message = findViewById(R.id.message);
		sound = findViewById(R.id.sound);
		send = findViewById(R.id.send);
		image_camera_attach = findViewById(R.id.image_camera_attach);
		image_music_attach = findViewById(R.id.image_music_attach);
		attach_files = findViewById(R.id.attach_files);
		media = findViewById(R.id.media);
		file = getSharedPreferences("f", Activity.MODE_PRIVATE);
		fauth = FirebaseAuth.getInstance();
		net = new RequestNetwork(this);
		f = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		requestNetwork = new RequestNetwork(this);
		fpick.setType("*/*");
		fpick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		_file_camxd = FileUtil.createNewPictureFile(getApplicationContext());
		Uri _uri_camxd;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			_uri_camxd = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", _file_camxd);
		} else {
			_uri_camxd = Uri.fromFile(_file_camxd);
		}
		camxd.putExtra(MediaStore.EXTRA_OUTPUT, _uri_camxd);
		camxd.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		music.setType("audio/*");
		music.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		allfiles.setType("*/*");
		allfiles.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		prtFiles.setType("image/*");
		prtFiles.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		linear_head_top.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (getIntent().getStringExtra("group").equals("true")) {
					a.setClass(getApplicationContext(), GroupInfoActivity.class);
					a.putExtra("user2", getIntent().getStringExtra("user2"));
					startActivity(a);
				} else {
					a.setClass(getApplicationContext(), MyprofileActivity.class);
					a.putExtra("id", getIntent().getStringExtra("user2"));
					startActivity(a);
				}
			}
		});
		
		voiceCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "currently not available!");
			}
		});
		
		videoCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (IsCalling) {
					intent.setClass(getApplicationContext(), CallActivity.class);
					intent.putExtra("channel", channel);
					startActivity(intent);
				} else {
					channel = FirebaseAuth.getInstance().getCurrentUser().getUid();
					call1 = new HashMap<>();
					call1.put("channel", channel);
					call1.put("username", user2_name);
					call1.put("sen", FirebaseAuth.getInstance().getCurrentUser().getUid());
					call1.put("user2", getIntent().getStringExtra("user2"));
					call.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(call1);
					call1.clear();
					call3 = new HashMap<>();
					call3.put("channel", channel);
					call3.put("rec", getIntent().getStringExtra("user2"));
					call3.put("username", sender_name);
					call3.put("user2", FirebaseAuth.getInstance().getCurrentUser().getUid());
					call.child(getIntent().getStringExtra("user2")).updateChildren(call3);
					call3.clear();
					intent.setClass(getApplicationContext(), CallActivity.class);
					intent.putExtra("channel", channel);
					startActivity(intent);
				}
			}
		});
		
		settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (getIntent().getStringExtra("group").equals("true")) {
					a.setClass(getApplicationContext(), GroupInfoActivity.class);
					a.putExtra("user2", getIntent().getStringExtra("user2"));
					startActivity(a);
				} else {
					a.setClass(getApplicationContext(), ChatSettingsActivity.class);
					a.putExtra("user2", getIntent().getStringExtra("user2"));
					startActivity(a);
				}
			}
		});
		
		message_body_in_reply_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				message_body_in_reply.setVisibility(View.GONE);
			}
		});
		
		sound_record_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				sound_record_body.setVisibility(View.GONE);
				message_body_in_message.setVisibility(View.VISIBLE);
				if (recorder != null) {
					
					
					ts.cancel();
					audio_time = 0;
					VoiceMessage = false;
					
					recorder.stop();
					recorder.release();
					recorder = null;
					File file = new File(audioPath);
					if (file.exists()) {
						file.delete();
					}
					
				}
				
			}
		});
		
		send_audio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				audio_time = 0;
				message_body_in_message.setVisibility(View.VISIBLE);
				sound_record_body.setVisibility(View.GONE);
				if (recorder != null) {
					try {
						// Stop recording
						recorder.stop();
						recorder.release();
						recorder = null;
						
						
						
						
						final String fileName = "audio_" + System.currentTimeMillis() + ".3gp";
						ts.cancel();
						VoiceMessage = true;
						storage.child(fileName).putFile(Uri.fromFile(new File(audioPath))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
							@Override
							public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
								return storage.child(fileName).getDownloadUrl();
							}}).addOnCompleteListener(_storage_upload_success_listener);
						
						
						
					} catch (Exception e) {
						showMessage("Error: " + e.getMessage());
					}
				} else {
					showMessage("Recorder is not initialized!");
				}
				
			}
		});
		
		image___atch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (linearExtra_attach.getVisibility() == View.VISIBLE) {
					linearExtra_attach.setVisibility(View.GONE);
					image___atch.setImageResource(R.drawable.ux_6_2);
					prt = false;
					audio = false;
					video = false;
					allFir = false;
				} else {
					linearExtra_attach.setVisibility(View.VISIBLE);
					image___atch.setImageResource(R.drawable.ic_close_black);
				}
			}
		});
		
		view1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				prt = true;
				startActivityForResult(prtFiles, REQ_CD_PRTFILES);
			}
		});
		
		message.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				msg_trim = _charSeq.trim();
				if (0 < _charSeq.length()) {
					sound.setVisibility(View.GONE);
					send.setVisibility(View.VISIBLE);
					status = new HashMap<>();
					status.put("status", "typing..");
					get_status.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(status);
					status.clear();
					ts = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									status = new HashMap<>();
									status.put("status", "online");
									get_status.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(status);
									status.clear();
								}
							});
						}
					};
					_timer.schedule(ts, (int)(2600));
				} else {
					status = new HashMap<>();
					status.put("status", "online");
					get_status.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(status);
					status.clear();
					send.setVisibility(View.GONE);
					sound.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		sound.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || 
				ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					
					// Request permissions
					ActivityCompat.requestPermissions(ChatActivity.this, new String[]{
						Manifest.permission.RECORD_AUDIO, 
						Manifest.permission.WRITE_EXTERNAL_STORAGE
					}, 100);
					
				} else {
					// Permissions are granted, start recording
					
					audioPath = getExternalFilesDir(null).getAbsolutePath() + "/audio_record.3gp";
					recorder = new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					recorder.setOutputFile(audioPath);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					
					try {
						recorder.prepare();
						recorder.start();
						
						sound_record_body.setVisibility(View.VISIBLE);
						message_body_in_message.setVisibility(View.GONE);
						ts = new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										audio_time++;
										recording_text.setText(record_txt.concat("   ".concat(new DecimalFormat("00.00").format(audio_time))));
									}
								});
							}
						};
						_timer.scheduleAtFixedRate(ts, (int)(0), (int)(1000));
						
					} catch (Exception e) {
						
						sound_record_body.setVisibility(View.GONE);
						message_body_in_message.setVisibility(View.VISIBLE);
						
					}
				}
			}
		});
		
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (SketchwareUtil.isConnected(getApplicationContext())) {
					if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
						chat_key = Chat1.push().getKey();
						if (!msg_trim.equals("")) {
							if (replytrue.equals("true")) {
								replytrue = "false";
								_MessageReply(message_body_in_reply_text.getText().toString(), msg_trim);
							} else {
								dKey = getIntent().getStringExtra("user2").concat(FirebaseAuth.getInstance().getCurrentUser().getUid());
								_EncryptedStringKey(msg_trim, dKey);
							}
						} else {
							
						}
					} else {
						a.setClass(getApplicationContext(), LogActivity.class);
						startActivity(a);
						finish();
					}
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Failed!");
				}
			}
		});
		
		image_camera_attach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				linearExtra_attach.setVisibility(View.GONE);
				image___atch.setImageResource(R.drawable.ux_6_2);
				startActivityForResult(camxd, REQ_CD_CAMXD);
			}
		});
		
		image_music_attach.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				linearExtra_attach.setVisibility(View.GONE);
				image___atch.setImageResource(R.drawable.ux_6_2);
				startActivityForResult(music, REQ_CD_MUSIC);
			}
		});
		
		attach_files.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				linearExtra_attach.setVisibility(View.GONE);
				image___atch.setImageResource(R.drawable.ux_6_2);
				startActivityForResult(allfiles, REQ_CD_ALLFILES);
			}
		});
		
		media.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				linearExtra_attach.setVisibility(View.GONE);
				image___atch.setImageResource(R.drawable.ux_6_2);
				startActivityForResult(fpick, REQ_CD_FPICK);
			}
		});
		
		_Chat1_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				Chat1.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						chats = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								chats.add(_map);
							}
						} catch (Exception _e) {
							_e.printStackTrace();
						}
						recyclerview1.setAdapter(new Recyclerview1Adapter(chats));
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				if (0 < chats.size()) {
					recyclerview1.smoothScrollToPosition((int)chats.size() - 1);
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				Chat1.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						chats = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								chats.add(_map);
							}
						} catch (Exception _e) {
							_e.printStackTrace();
						}
						recyclerview1.setAdapter(new Recyclerview1Adapter(chats));
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				if (0 < chats.size()) {
					recyclerview1.smoothScrollToPosition((int)chats.size() - 1);
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
				Chat1.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						chats = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								chats.add(_map);
							}
						} catch (Exception _e) {
							_e.printStackTrace();
						}
						recyclerview1.setAdapter(new Recyclerview1Adapter(chats));
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				if (0 < chats.size()) {
					recyclerview1.smoothScrollToPosition((int)chats.size() - 1);
				}
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
			}
		};
		Chat1.addChildEventListener(_Chat1_child_listener);
		
		_Chat2_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (!puase) {
					if (_childValue.containsKey("status")) {
						staS = new HashMap<>();
						staS.put("status", "seen");
						Chat2.child(_childValue.get("key").toString()).updateChildren(staS);
						staS.clear();
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
		Chat2.addChildEventListener(_Chat2_child_listener);
		
		_get_status_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(getIntent().getStringExtra("user2"))) {
					if (_childValue.get("status").toString().equals("online")) {
						status_dots.setVisibility(View.VISIBLE);
						user_status.setVisibility(View.VISIBLE);
						user_status.setTextColor(0xFFFFFFFF);
						user_status.setText("Online");
						_ImageColor(status_dots, "#58D77D");
					} else {
						if (_childValue.get("status").toString().equals("offline")) {
							status_dots.setVisibility(View.VISIBLE);
							user_status.setVisibility(View.VISIBLE);
							_ImageColor(status_dots, "#FFCDD2");
							user_status.setTextColor(0xFFFFCDD2);
							user_status.setText("Offline");
						} else {
							_ImageColor(status_dots, "#EEEEEE");
							user_status.setTextColor(0xFFEEEEEE);
							user_status.setText(_childValue.get("status").toString());
						}
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(getIntent().getStringExtra("user2"))) {
					if (_childValue.get("status").toString().equals("online")) {
						status_dots.setVisibility(View.VISIBLE);
						user_status.setVisibility(View.VISIBLE);
						user_status.setTextColor(0xFFFFFFFF);
						user_status.setText("Online");
						_ImageColor(status_dots, "#58D77D");
					} else {
						if (_childValue.get("status").toString().equals("offline")) {
							status_dots.setVisibility(View.VISIBLE);
							user_status.setVisibility(View.VISIBLE);
							_ImageColor(status_dots, "#FFCDD2");
							user_status.setTextColor(0xFFFFCDD2);
							user_status.setText("Offline");
						} else {
							if (_childValue.get("status").toString().equals("typing")) {
								status_dots.setVisibility(View.VISIBLE);
								user_status.setVisibility(View.VISIBLE);
								_ImageColor(status_dots, "#B9F6CA");
								user_status.setTextColor(0xFFFFFFFF);
								typeing.setVisibility(View.VISIBLE);
								user_status.setText("typing..");
								typeing.setText(user2_name.concat(" is typing.."));
							} else {
								_ImageColor(status_dots, "#EEEEEE");
								user_status.setTextColor(0xFFEEEEEE);
								user_status.setText(_childValue.get("status").toString());
								typeing.setVisibility(View.GONE);
							}
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
		get_status.addChildEventListener(_get_status_child_listener);
		
		_net_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				if (_response.contains("<title>")) {
					patt = "<title>(.+?)</title>";
					java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
					java.util.regex.Matcher matcher = pattern.matcher(_response);
					if (matcher.find()) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							file.edit()
							.putString(_tag, Html.fromHtml(matcher.group(1), Html.FROM_HTML_MODE_COMPACT).toString())
							.commit();
						} else {
							file.edit()
							.putString(_tag, Html.fromHtml(matcher.group(1)).toString())
							.commit();
						}
					}
				} else {
					if (_response.contains("itle\">")) {
						patt = "itle\">(.+?)</title>";
						java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
						java.util.regex.Matcher matcher = pattern.matcher(_response);
						if (matcher.find()) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
								file.edit()
								.putString(_tag, Html.fromHtml(matcher.group(1), Html.FROM_HTML_MODE_COMPACT).toString())
								.commit();
							} else {
								file.edit()
								.putString(_tag, Html.fromHtml(matcher.group(1)).toString())
								.commit();
							}
						}
					} else {
						if (_response.contains("itle=\"")) {
							patt = "itle=\"(.+?)\"";
							java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
							java.util.regex.Matcher matcher = pattern.matcher(_response);
							if (matcher.find()) {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
									file.edit()
									.putString(_tag, Html.fromHtml(matcher.group(1), Html.FROM_HTML_MODE_COMPACT).toString())
									.commit();
								} else {
									file.edit()
									.putString(_tag, Html.fromHtml(matcher.group(1)).toString())
									.commit();
								}
							}
						} else {
							if (_response.contains("itle\" content=\"")) {
								patt = "itle\" content=\"(.+?)";
								java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
								java.util.regex.Matcher matcher = pattern.matcher(_response);
								if (matcher.find()) {
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
										file.edit()
										.putString(_tag, Html.fromHtml(matcher.group(1), Html.FROM_HTML_MODE_COMPACT).toString())
										.commit();
									} else {
										file.edit()
										.putString(_tag, Html.fromHtml(matcher.group(1)).toString())
										.commit();
									}
								}
							} else {
								
							}
						}
					}
				}
				if (_response.contains("description\" content=\"")) {
					patt = "description\" content=\"(.+?)\"";
					java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
					java.util.regex.Matcher matcher = pattern.matcher(_response);
					if (matcher.find()) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							file.edit()
							.putString("dec" + _tag, Html.fromHtml(matcher.group(1), Html.FROM_HTML_MODE_COMPACT).toString())
							.commit();
						} else {
							file.edit()
							.putString("dec" + _tag, Html.fromHtml(matcher.group(1)).toString())
							.commit();
						}
					}
				} else {
					
				}
				if (_response.contains("name\" content=\"")) {
					patt = "name\" content=\"(.+?)\"";
					java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
					java.util.regex.Matcher matcher = pattern.matcher(_response);
					if (matcher.find()) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							file.edit()
							.putString(_tag, Html.fromHtml(matcher.group(1), Html.FROM_HTML_MODE_COMPACT).toString())
							.commit();
						} else {
							file.edit()
							.putString(_tag, Html.fromHtml(matcher.group(1)).toString())
							.commit();
						}
					}
				} else {
					if (_response.contains("title=\"")) {
						patt = "title=\"(.+?)\"";
						java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
						java.util.regex.Matcher matcher = pattern.matcher(_response);
						if (matcher.find()) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
								file.edit()
								.putString(_tag, Html.fromHtml(matcher.group(1), Html.FROM_HTML_MODE_COMPACT).toString())
								.commit();
							} else {
								file.edit()
								.putString(_tag, Html.fromHtml(matcher.group(1)).toString())
								.commit();
							}
						}
					} else {
						
					}
				}
				if (_response.contains("image\" content=\"")) {
					if (_response.contains("og:image\" content=\"")) {
						patt = "og:image\" content=\"(.+?)\"";
						java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
						java.util.regex.Matcher matcher = pattern.matcher(_response);
						if (matcher.find()) {
							// Using Html.fromHtml with toString() to convert Spanned to String
							file.edit()
							.putString("url" + _tag, matcher.group(1))
							.commit();
						}
					} else {
						patt = "image\" content=\"(.+?)\"";
						java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
						java.util.regex.Matcher matcher = pattern.matcher(_response);
						if (matcher.find()) {
							// Using Html.fromHtml with toString() to convert Spanned to String
							file.edit()
							.putString("url" + _tag, matcher.group(1))
							.commit();
						}
					}
				} else {
					if (_response.contains("og:image\" content='")) {
						patt = "og:image\" content='(.+?)'";
						java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
						java.util.regex.Matcher matcher = pattern.matcher(_response);
						if (matcher.find()) {
							// Using Html.fromHtml with toString() to convert Spanned to String
							file.edit()
							.putString("url" + _tag, matcher.group(1))
							.commit();
						}
					} else {
						if (_response.contains("icon\" href=\"")) {
							patt = "icon\" href=\"(.+?)\"";
							java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
							java.util.regex.Matcher matcher = pattern.matcher(_response);
							if (matcher.find()) {
								// Using Html.fromHtml with toString() to convert Spanned to String
								file.edit()
								.putString("url" + _tag, matcher.group(1))
								.commit();
							}
						} else {
							if (_response.contains("Icon\" href=\"")) {
								patt = "Icon\" href=\"(.+?)\"";
								java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
								java.util.regex.Matcher matcher = pattern.matcher(_response);
								if (matcher.find()) {
									// Using Html.fromHtml with toString() to convert Spanned to String
									file.edit()
									.putString("url" + _tag, matcher.group(1))
									.commit();
								}
							} else {
								if (_response.contains("rel=\"Shortcut Icon\"")) {
									patt = "href=\"(.+?)\" rel=\"Shortcut Icon\"";
									java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patt);
									java.util.regex.Matcher matcher = pattern.matcher(_response);
									if (matcher.find()) {
										// Using Html.fromHtml with toString() to convert Spanned to String
										file.edit()
										.putString("url" + _tag, matcher.group(1))
										.commit();
									}
								} else {
									
								}
							}
						}
					}
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
		
		_storage_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				upload_image.setVisibility(View.VISIBLE);
				textview_up_progress.setText(String.valueOf((long)(_progressValue)).concat("% Uploaded.. "));
				progressbar1.setProgress((int)_progressValue);
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
				upload_image.setVisibility(View.GONE);
				if (VoiceMessage) {
					VoiceMessage = false;
					_Voice_msg_sender(_downloadUrl);
				}
				if (Img) {
					_Image_Sender(_downloadUrl);
					Img = false;
				}
				if (prt) {
					chat_key = Chat1.push().getKey();
					_EncryptedStringKey(_downloadUrl, chat_key);
				}
				if (audio) {
					_audio_sender(_downloadUrl);
					audio = false;
				}
				if (video) {
					_video_sender(_downloadUrl);
					video = false;
				}
				if (allFir) {
					_allFiles_Sender(_downloadUrl);
					allFir = false;
				}
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
		
		_users_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					sender_name = _childValue.get("username").toString();
					mydp = _childValue.get("dp").toString();
				}
				if (_childKey.equals(getIntent().getStringExtra("user2"))) {
					user2_name = _childValue.get("username").toString();
					if (!_childValue.get("dp").toString().equals("null")) {
						Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("dp").toString())).into(dps);
						user2DP = _childValue.get("dp").toString();
					}
					name.setText(_childValue.get("username").toString());
					if (_childValue.containsKey("v")) {
						if (_childValue.get("v").toString().equals("true")) {
							verified.setVisibility(View.VISIBLE);
						} else {
							verified.setVisibility(View.GONE);
						}
					}
					if (getIntent().getStringExtra("group").equals("true")) {
						if (_childValue.containsKey("admin")) {
							if (_childValue.get("admin").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
								admin = FirebaseAuth.getInstance().getCurrentUser().getUid();
								adminName = sender_name;
							} else {
								admin = _childValue.get("admin").toString();
								
								final FirebaseDatabase database = FirebaseDatabase.getInstance();
								final DatabaseReference ref = database.getReference("users" + "/" + admin);
								ref.addListenerForSingleValueEvent(new ValueEventListener() {
									@Override
									public void onDataChange(DataSnapshot dataSnapshot) {
										if (dataSnapshot.exists()) {
											// Admin exists, retrieve username
											adminName = dataSnapshot.child("username").getValue(String.class);
											// Use the admin_name variable as needed
											
										} else {
											// Admin does not exist
											adminName = null;
										}
									}
									
									@Override
									public void onCancelled(DatabaseError databaseError) {
										// Handle errors
										
									}
								});
							}
						}
					}
				}
				if (_childValue.containsKey("user")) {
					userMention.add(_childValue.get("user").toString());
					userids.add(_childKey);
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
		
		_requestNetwork_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				SketchwareUtil.showMessage(getApplicationContext(), _response);
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
		
		FCM_onCompleteListener = new OnCompleteListener<InstanceIdResult>() {
			@Override
			public void onComplete(Task<InstanceIdResult> task) {
				final boolean _success = task.isSuccessful();
				final String _token = task.getResult().getToken();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				if (_success) {
					
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
				}
			}
		};
		
		_pending_child_listener = new ChildEventListener() {
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
		pending.addChildEventListener(_pending_child_listener);
		
		_group_child_listener = new ChildEventListener() {
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
		group.addChildEventListener(_group_child_listener);
		
		_call_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
					if (_childValue.containsKey("rec")) {
						if (_childValue.containsKey("channel")) {
							callT = new TimerTask() {
								@Override
								public void run() {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											_Pop(o1, o2, videoCall);
										}
									});
								}
							};
							_timer.scheduleAtFixedRate(callT, (int)(0), (int)(2000));
							IsCalling = true;
							channel = _childValue.get("channel").toString();
						}
					} else {
						if (_childValue.containsKey("sen")) {
							IsCalling = false;
						} else {
							IsCalling = false;
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
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			_theme("Black or White");
			_changeActivityFont("fn1");
			_setStatus("online");
			linearExtra_attach.setVisibility(View.GONE);
			_TransitionManager(linear_message, 1000);
			_edittext_mh(message);
			if (getIntent().getStringExtra("group").equals("true")) {
				_groupChat();
				user_status.setText("Tap here for group info..");
				status_dots.setVisibility(View.INVISIBLE);
				view1.setVisibility(View.INVISIBLE);
			} else {
				_chatrooms();
			}
		}
		recyclerview1.setLayoutManager(new LinearLayoutManager(this));
		_ui();
		reco_path = FileUtil.getPackageDataDir(getApplicationContext()).concat("/temp.mp3");
		
		androidx.recyclerview.widget.LinearLayoutManager layoutManager=
		new androidx.recyclerview.widget.LinearLayoutManager(getApplicationContext(),androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,true);
		layoutManager.setReverseLayout(false);
		layoutManager.setStackFromEnd(true);
		recyclerview1.setLayoutManager(layoutManager);
		SwipeController controller = new SwipeController(getApplicationContext(), new ISwipeControllerActions() {
			@Override
			public void onSwipePerformed(int position) {
				
				//action here when recycler is swiped
				if (chats.get((int)position).containsKey("msg")) {
					message_body_in_reply.setVisibility(View.VISIBLE);
					message_body_in_reply_text.setText(chats.get((int)position).get("msg").toString());
					file.edit().putString("reply", chats.get((int)position).get("msg").toString()).commit();
					replytrue = "true";
				}
				if (chats.get((int)position).containsKey("reply")) {
					message_body_in_reply.setVisibility(View.VISIBLE);
					message_body_in_reply_text.setText(chats.get((int)position).get("reply").toString());
					file.edit().putString("reply", chats.get((int)position).get("reply").toString()).commit();
					replytrue = "true";
				}
				if (chats.get((int)position).containsKey("img")) {
					message_body_in_reply.setVisibility(View.VISIBLE);
					message_body_in_reply_text.setText(chats.get((int)position).get("img").toString());
					file.edit().putString("reply", chats.get((int)position).get("img").toString()).commit();
					replytrue = "true";
				}
			}
		});
		androidx.recyclerview.widget.ItemTouchHelper itemTouchHelper = new androidx.recyclerview.widget.ItemTouchHelper(controller);
		itemTouchHelper.attachToRecyclerView(recyclerview1);
		_TransitionManager(main_bg, 1000);
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
				_extension(_filePath);
			}
			else {
				
			}
			break;
			
			case REQ_CD_CAMXD:
			if (_resultCode == Activity.RESULT_OK) {
				String _filePath = _file_camxd.getAbsolutePath();
				
				path_name = Uri.parse(_filePath).getLastPathSegment();
				path = _filePath;
				if (path_name.endsWith("jpg") || (path_name.endsWith("jpeg") || (path_name.endsWith("png") || (path_name.endsWith("webp") || path_name.endsWith("gif"))))) {
					if (prt) {
						storage.child(path_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
							@Override
							public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
								return storage.child(path_name).getDownloadUrl();
							}}).addOnCompleteListener(_storage_upload_success_listener);
						Img = false;
					} else {
						Img = true;
						storage.child(path_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
							@Override
							public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
								return storage.child(path_name).getDownloadUrl();
							}}).addOnCompleteListener(_storage_upload_success_listener);
					}
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Only images supported in this update!");
					Img = false;
				}
			}
			else {
				
			}
			break;
			
			case REQ_CD_MUSIC:
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
				_extension(_filePath);
			}
			else {
				audio = false;
			}
			break;
			
			case REQ_CD_ALLFILES:
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
				_extension(_filePath);
			}
			else {
				allFir = false;
			}
			break;
			
			case REQ_CD_PRTFILES:
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
				_extension(_filePath);
			}
			else {
				prt = false;
			}
			break;
			default:
			break;
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		puase = false;
		_setStatus("online");
		if (0 < chats.size()) {
			recyclerview1.smoothScrollToPosition((int)chats.size() - 1);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		puase = true;
		_setStatus("offline");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timest = Calendar.getInstance();
		_setStatus("Last Seen : ".concat(new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())));
		a.removeExtra("group");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			imageview_up_symbol.setColorFilter(0xFF5BC0EB, PorterDuff.Mode.MULTIPLY);
		} else {
			a.setClass(getApplicationContext(), LogActivity.class);
			startActivity(a);
			finish();
		}
	}
	public void _chatrooms() {
		Chat1.removeEventListener(_Chat1_child_listener);
		Chat2.removeEventListener(_Chat2_child_listener);
		chatroom = "chat/".concat(FirebaseAuth.getInstance().getCurrentUser().getUid().concat("/".concat(getIntent().getStringExtra("user2"))));
		chatcopy = "chat/".concat(getIntent().getStringExtra("user2").concat("/".concat(FirebaseAuth.getInstance().getCurrentUser().getUid())));
		Chat1 = _firebase.getReference(chatroom);
		
		Chat2 = _firebase.getReference(chatcopy);
		Chat1.addChildEventListener(_Chat1_child_listener);
		Chat2.addChildEventListener(_Chat2_child_listener);
	}
	
	
	public void _ui() {
		_theme("");
		sound_record_body.setVisibility(View.GONE);
		message_body_in_reply.setVisibility(View.GONE);
		typeing.setVisibility(View.GONE);
		record_txt = "●●●   Recording... ";
		audio_time = 0;
		send.setVisibility(View.GONE);
		upload_image.setVisibility(View.GONE);
		block = false;
		VoiceMessage = false;
		Img = false;
		prt = false;
		audio = false;
		video = false;
		allFir = false;
		IsCalling = false;
	}
	
	
	public void _theme(final String _color) {
		if (file.contains("wallxP")) {
			colorHex = file.getString("wallxst", "");
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) { 
				Window w = ChatActivity.this.getWindow();
				
				// Clear the translucent status flag
				w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				
				// Add the flag to draw system bar backgrounds
				w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				
				// Define your hex color
				// String colorHex = "#007BA7"; Replace with your desired hex color
				
				try {
					// Parse and set the status bar color
					w.setStatusBarColor(Color.parseColor(colorHex));
				} catch (IllegalArgumentException e) {
					// Handle invalid color format
					Log.e("ColorError", "Invalid color: " + colorHex, e);
				}
			}
			
			/*String colorHex = "#007BA7"; // Replace with your desired hex color */
			try {
				linear_head_top.setBackgroundColor(Color.parseColor(colorHex)); // Parse and set the color
			} catch (IllegalArgumentException e) {
				// Handle invalid color format
				Log.e("ColorError", "Invalid color: " + colorHex, e);
			}
			
			_Setcolor(name, file.getString("wallxset", ""));
			_Setcolor(user_status, file.getString("wallxt", ""));
			_Setcolor(message, file.getString("wallxt", ""));
			_ImageColor(settings, file.getString("wallxset", ""));
			_ImageColor(sound, file.getString("wallxset", ""));
			_ImageColor(send, file.getString("wallxset", ""));
			_ImageColor(image___atch, file.getString("wallxset", ""));
		} else {
			if ("dark".equals(file.getString("theme", ""))) {
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					Window w =ChatActivity.this.getWindow();
					w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF212121);
				}
				main_bg.setBackgroundColor(0xFF303032);
				linear_head_top.setBackgroundColor(0xFF212121);
				settings.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
				name.setTextColor(0xFFFFFFFF);
				user_status.setTextColor(0xFFE0E0E0);
				status_dots.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFF2C3E50));
				linear_message.setBackgroundColor(0xFF39393B);
				message_body_in_reply_title.setTextColor(0xFFE0E0E0);
				message_body_in_reply_text.setTextColor(0xFFE0E0E0);
				message.setTextColor(0xFFE0E0E0);
			} else {
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					Window w =ChatActivity.this.getWindow();
					w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF007BA7);
				}
				linear_head_top.setBackgroundColor(0xFF007BA7);
				main_bg.setBackgroundColor(0xFFFFFFFF);
				settings.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
				name.setTextColor(0xFFFFFFFF);
				user_status.setTextColor(0xFFE0E0E0);
				status_dots.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFF007BA7));
				linear_message.setBackgroundColor(0xFFFFFFFF);
				message_body_in_reply_title.setTextColor(0xFF000000);
				message_body_in_reply_text.setTextColor(0xFF000000);
				message.setTextColor(0xFF000000);
			}
		}
	}
	
	
	public void _SwipeController() {
		//This is the swipe controller class
	}
	public class SwipeController extends androidx.recyclerview.widget.ItemTouchHelper.Callback {
		
		private Context mContext;
		private ISwipeControllerActions mSwipeControllerActions;
		
		private android.graphics.drawable.Drawable mReplyIcon;
		private android.graphics.drawable.Drawable mReplyIconBackground;
		
		private androidx.recyclerview.widget.RecyclerView.ViewHolder mCurrentViewHolder;
		private View mView;
		
		private float mDx = 0f;
		
		private float mReplyButtonProgress = 0f;
		private long  mLastReplyButtonAnimationTime = 0;
		
		private boolean mSwipeBack = false;
		private boolean mIsVibrating = false;
		private boolean mStartTracking = false;
		
		private int mBackgroundColor = 0x20606060;
		
		private int mReplyBackgroundOffset = 18;
		
		private int mReplyIconXOffset = 12;
		private int mReplyIconYOffset = 11;
		
		public SwipeController(Context context, ISwipeControllerActions swipeControllerActions){
			mContext = context;
			mSwipeControllerActions = swipeControllerActions;
			
			mReplyIcon = ContextCompat.getDrawable(mContext,R.drawable.ic_reply);
			mReplyIconBackground = androidx.core.content.res.ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_round_shape,null);
		}
		
		
		public SwipeController(Context context, ISwipeControllerActions swipeControllerActions, int replyIcon, int replyIconBackground, int backgroundColor){
			mContext = context;
			mSwipeControllerActions = swipeControllerActions;
			
			mReplyIcon = mContext.getResources().getDrawable(replyIcon);
			mReplyIconBackground = mContext.getResources().getDrawable(replyIconBackground);
			mBackgroundColor = backgroundColor;
		}
		
		@Override
		public int getMovementFlags(@androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder) {
			mView = viewHolder.itemView;
			
			return androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags(androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE, androidx.recyclerview.widget.ItemTouchHelper.LEFT);
		}
		
		@Override
		public boolean onMove(@androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder1) {
			return false;
		}
		
		@Override
		public void onSwiped(@androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int i) {
			
		}
		
		@Override
		public int convertToAbsoluteDirection(int flags, int layoutDirection){
			if (mSwipeBack){
				mSwipeBack = false;
				return 0;
			}
			return super.convertToAbsoluteDirection(flags, layoutDirection);
		}
		
		@Override
		public void onChildDraw(@androidx.annotation.NonNull Canvas c, @androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
			if (actionState == androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE){
				setTouchListener(recyclerView, viewHolder);
			}
			if (mView.getTranslationX() < convertToDp(130) || dX > mDx ){
				
				float width =getDisplayWidthPixels();
				float widthCenter = (width / 2) - 170;
				float offset = dX / width;
				
				float newX = widthCenter * offset;
				super.onChildDraw(c, recyclerView, viewHolder, newX, dY, actionState, isCurrentlyActive);
				mDx = newX;
				mStartTracking = true;
			}
			mCurrentViewHolder = viewHolder;
			drawReplyButton(c);
		}
		
		private void setTouchListener(androidx.recyclerview.widget.RecyclerView recyclerView, final androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder){
			recyclerView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
						mSwipeBack = true;
					} else {
						mSwipeBack = false;
					}
					if (mSwipeBack) {
						if (Math.abs(mView.getTranslationX()) >= convertToDp(35)) {
							mSwipeControllerActions.onSwipePerformed(viewHolder.getAdapterPosition());
						}
					}
					return false;
				}
			});
		}
		
		private int convertToDp(int pixels){
			return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
		}
		
		
		private void drawReplyButton(Canvas canvas){
			
			int width = (int)getDisplayWidthPixels();
			if (mCurrentViewHolder == null){
				return;
			}
			
			float translationX = -mView.getTranslationX();
			
			long newTime = System.currentTimeMillis();
			long dt = Math.min(17, newTime - mLastReplyButtonAnimationTime);
			mLastReplyButtonAnimationTime = newTime;
			boolean showing = false;
			if (translationX >= convertToDp(10)){
				showing = true;
			}
			if (showing){
				if (mReplyButtonProgress < 1.0f){
					mReplyButtonProgress += dt / 180.0f;
					if (mReplyButtonProgress > 1.0f){
						mReplyButtonProgress = 1.0f;
					} else {
						mView.invalidate();
					}
				}
			} else if (translationX <= 0.0f){
				mReplyButtonProgress = 0f;
				mStartTracking = false;
				mIsVibrating = false;
			} else {
				if (mReplyButtonProgress > 0.0f){
					mReplyButtonProgress -= dt / 180.0f;
					if (mReplyButtonProgress < 0.1f){
						mReplyButtonProgress = 0f;
					}
				}
				mView.invalidate();
			}
			int alpha;
			float scale;
			if (showing){
				if (mReplyButtonProgress <= 0.8f){
					scale = 1.2f * (mReplyButtonProgress / 0.8f);
				} else{
					scale = 1.2f - 0.2f * ((mReplyButtonProgress - 0.8f) / 0.2f);
				}
				alpha = Math.min(255, 255 * ((int)(mReplyButtonProgress / 0.8f)));
			} else{
				scale = mReplyButtonProgress;
				alpha = Math.min(255, 255 * (int)mReplyButtonProgress);
			}
			mReplyIconBackground.setAlpha(alpha);
			mReplyIcon.setAlpha(alpha);
			if (mStartTracking){
				if (!mIsVibrating && -mView.getTranslationX() >= convertToDp(6)){
					mView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
				}
				mIsVibrating = true;
			}
			
			int x;
			float y;
			//        if (-mView.getTranslationX() > convertToDp(30)){
			//            x = width - ((int)-mView.getTranslationX()) + 40;
			//        }else{
			//            x = (int)mView.getTranslationX() + 40;
			//        }
			x = width - ((int)-mView.getTranslationX()) + 40;
			
			y = mView.getTop() + ((float) mView.getMeasuredHeight() /2);
			mReplyIconBackground.setColorFilter(mBackgroundColor, PorterDuff.Mode.MULTIPLY);
			
			mReplyIconBackground.setBounds(new Rect(
			(int)(x - convertToDp(mReplyBackgroundOffset) * scale),
			(int)(y - convertToDp(mReplyBackgroundOffset) * scale),
			(int)(x + convertToDp(mReplyBackgroundOffset) * scale),
			(int)(y + convertToDp(mReplyBackgroundOffset) * scale)
			));
			mReplyIconBackground.draw(canvas);
			
			mReplyIcon.setBounds(new Rect(
			(int)(x - convertToDp(mReplyIconXOffset) * scale),
			(int)(y - convertToDp(mReplyIconYOffset) * scale),
			(int)(x + convertToDp(mReplyIconXOffset) * scale),
			(int)(y + convertToDp(mReplyIconYOffset) * scale)
			));
			mReplyIcon.draw(canvas);
			
			mReplyIconBackground.setAlpha(255);
			mReplyIcon.setAlpha(255);
		}
	}
	
	
	public void _SwipeInterface() {
		//swipe actions interface
	}
	public interface ISwipeControllerActions {
		
		void onSwipePerformed(int position);
	}
	
	
	public void _TransitionManager(final View _view, final double _duration) {
		LinearLayout viewgroup =(LinearLayout) _view;
		
		android.transition.AutoTransition autoTransition = new android.transition.AutoTransition(); autoTransition.setDuration((long)_duration); android.transition.TransitionManager.beginDelayedTransition(viewgroup, autoTransition);
	}
	
	
	public void _setStatus(final String _status) {
		status = new HashMap<>();
		status.put("status", _status);
		get_status.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(status);
		status.clear();
	}
	
	
	public void _TextMessage(final String _msg) {
		timest = Calendar.getInstance(); 
		chat_key = Chat1.push().getKey(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("msg", _msg); 
		chatmap.put("p", _msg); 
		chatmap.put("dKey", dKey); 
		if (getIntent().hasExtra("group")) {
			chatmap.put("dp", mydp); 
		}
		chatmap.put("status", "uns"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		//here is missing code!
		pending.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("lastMessage", _msg); 
		currentUserUpdateMap.put("sunimg", "null"); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", "null");
				updateMap.put("lastMessage", _msg); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
	}
	
	
	public void _Image_Sender(final String _url) {
		//New data push method
		
		timest = Calendar.getInstance(); 
		chat_key = Chat1.push().getKey(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("img", _url); 
		chatmap.put("p", "Sent a image"); 
		chatmap.put("status", "uns"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		pending.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		final HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("subimg", _url);
		currentUserUpdateMap.put("lastMessage", "You : Sent an image"); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", _url); 
				updateMap.put("lastMessage", "Sent an image"); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
	}
	
	
	public void _setTime(final double _currentTime, final TextView _txt) {
		timest.setTimeInMillis((long)(_currentTime));
		_txt.setText(new SimpleDateFormat("hh:mm").format(timest.getTime()));
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
	
	
	public void _ImageColor(final ImageView _image, final String _color) {
		_image.setColorFilter(Color.parseColor(_color),PorterDuff.Mode.SRC_ATOP);
	}
	
	
	public void _Send_notification(final String _username, final String _body, final String _image) {
		if (1 == 0) {
			//setup headers details
			HEADERS = new HashMap<>();
			HEADERS.put("Authorization", "key=".concat(ServerKey.trim()));
			HEADERS.put("Content-Type", "application/json");
			//set notification extra data setup
			NOTIFICATION_EXTRA_DATA = new HashMap<>();
			NOTIFICATION_EXTRA_DATA.put("extradata", FirebaseAuth.getInstance().getCurrentUser().getUid());
			//set Notification basic setup
			Notification_BODY = new HashMap<>();
			Notification_BODY.put("body", _body);
			Notification_BODY.put("title", _username);
			Notification_BODY.put("image", _image);
			Notification_BODY.put("icon", "YjRmZTlkOWItYjY2My00Nzg2LTllOWUtZTgwMTE2NDlkNmJj");
			//setup main body setup
			MAIN_BODY = new HashMap<>();
			MAIN_BODY.put("to", "/topics/".concat(privete_key));
			MAIN_BODY.put("notification", Notification_BODY);
			MAIN_BODY.put("data", NOTIFICATION_EXTRA_DATA);
			requestNetwork.setHeaders(HEADERS);
			requestNetwork.setParams(MAIN_BODY, RequestNetworkController.REQUEST_BODY);
			requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://fcm.googleapis.com/fcm/send", "A", _requestNetwork_request_listener);
		} else {
			/* if (SketchwareUtil.isConnected(getApplicationContext())) { 
    // Setup headers details
    HEADERS = new HashMap<>();
    HEADERS.put("Authorization", "Basic MGI3MDcwMzYtYTA0Mi00OTM0LTkxOTEtY2Y2ZDc3ZWZmNjJh");
    HEADERS.put("Content-Type", "application/json");

    // Set notification extra data setup
    NOTIFICATION_EXTRA_DATA = new HashMap<>();
    NOTIFICATION_EXTRA_DATA.put("custom_key1", "custom_value1");
    NOTIFICATION_EXTRA_DATA.put("custom_key2", "custom_value2");

    // Set Notification basic setup
    Notification_BODY = new HashMap<>();
    Notification_BODY.put("en", "Notification Message");
    Notification_BODY.put("headings", "Notification Title");

    // Setup main body setup
    MAIN_BODY = new HashMap<>();
    MAIN_BODY.put("app_id", "6a13ba9a-5bac-4825-9f89-e6003448fe97");  // Replace with your OneSignal App ID
    MAIN_BODY.put("included_segments", Arrays.asList("all"));  // Send to all subscribed users
    MAIN_BODY.put("contents", Notification_BODY);  // Attach contents for the notification message
    MAIN_BODY.put("data", NOTIFICATION_EXTRA_DATA);  // Attach additional data to notification

    // Optional: Add a URL and buttons
    MAIN_BODY.put("url", "https://yourwebsite.com"); 
    ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
    HashMap<String, Object> button1 = new HashMap<>();
    button1.put("id", "id1");
    button1.put("text", "Button1");
    button1.put("icon", "ic_menu_share");
    buttons.add(button1);
    HashMap<String, Object> button2 = new HashMap<>();
    button2.put("id", "id2");
    button2.put("text", "Button2");
    button2.put("icon", "ic_menu_send");
    buttons.add(button2);
    MAIN_BODY.put("buttons", buttons);  // Attach buttons to the main body

    // Configure the RequestNetwork to send the OneSignal request
    requestNetwork.setHeaders(HEADERS);
    requestNetwork.setParams(MAIN_BODY, RequestNetworkController.REQUEST_BODY);
    requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://onesignal.com/api/v1/notifications", "A", _requestNetwork_request_listener);
} 
else {
    SketchwareUtil.showMessage(getApplicationContext(), "No Internet");
}

*/ 
			
			
			if (SketchwareUtil.isConnected(getApplicationContext())) { 
				// Setup headers details
				HEADERS = new HashMap<>();
				HEADERS.put("Authorization", "Basic MGI3MDcwMzYtYTA0Mi00OTM0LTkxOTEtY2Y2ZDc3ZWZmNjJh");  // Replace with your OneSignal REST API Key
				HEADERS.put("Content-Type", "application/json");
				
				// Set up the notification message and title
				Notification_BODY = new HashMap<>();
				Notification_BODY.put("en", "Notification Message");  // Notification message
				Notification_BODY.put("headings", "Notification Title");  // Notification title
				
				// Main body setup to send to all users
				MAIN_BODY = new HashMap<>();
				MAIN_BODY.put("app_id", "6a13ba9a-5bac-4825-9f89-e6003448fe97");  // Replace with your OneSignal App ID
				MAIN_BODY.put("included_segments", Arrays.asList("All"));  // Targets all users subscribed to your app
				
				// Attach the contents to the main body
				MAIN_BODY.put("contents", Notification_BODY);
				
				// Send the request with the headers and main body
				requestNetwork.setHeaders(HEADERS);
				requestNetwork.setParams(MAIN_BODY, RequestNetworkController.REQUEST_BODY);
				requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://onesignal.com/api/v1/notifications", "A", _requestNetwork_request_listener);
			} else {
				SketchwareUtil.showMessage(getApplicationContext(), "No Internet");
			}
			
		}
	}
	
	
	public void _subscribeFCMTopic(final String _name) {
		if (_name.matches("[a-zA-Z0-9-_.~%]{1,900}")) {
			String topicName = java.text.Normalizer.normalize(_name, java.text.Normalizer.Form.NFD);
			topicName = topicName.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
			FirebaseMessaging.getInstance().subscribeToTopic(topicName).addOnCompleteListener(new OnCompleteListener<Void>() {
				@Override
				public void onComplete(@NonNull Task<Void> task) {
					if (task.isSuccessful()) { 
						
						
					}
					
				}
			});
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "Badly Formated Topic");
		}
	}
	
	
	public void _extension(final ArrayList<String> _path) {
		path_name = Uri.parse(_path.get((int)(0))).getLastPathSegment();
		path = _path.get((int)(0));
		if (path_name.endsWith("jpg") || (path_name.endsWith("jpeg") || (path_name.endsWith("png") || (path_name.endsWith("webp") || path_name.endsWith("gif"))))) {
			if (prt) {
				storage.child(path_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
					@Override
					public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
						return storage.child(path_name).getDownloadUrl();
					}}).addOnCompleteListener(_storage_upload_success_listener);
				Img = false;
			} else {
				Img = true;
				storage.child(path_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
					@Override
					public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
						return storage.child(path_name).getDownloadUrl();
					}}).addOnCompleteListener(_storage_upload_success_listener);
			}
		} else {
			if (path_name.endsWith("mp3") || (path_name.endsWith("m3a") || (path_name.endsWith("wav") || (path_name.endsWith("5gp") || path_name.endsWith("Ogg"))))) {
				storage.child(path_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
					@Override
					public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
						return storage.child(path_name).getDownloadUrl();
					}}).addOnCompleteListener(_storage_upload_success_listener);
				audio = true;
			} else {
				if (path_name.endsWith("mp4") || (path_name.endsWith("mkv") || (path_name.endsWith("webm") || (path_name.endsWith("mov") || path_name.endsWith("3gp"))))) {
					storage.child(path_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
						@Override
						public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
							return storage.child(path_name).getDownloadUrl();
						}}).addOnCompleteListener(_storage_upload_success_listener);
					video = true;
				} else {
					storage.child(path_name).putFile(Uri.fromFile(new File(path))).addOnFailureListener(_storage_failure_listener).addOnProgressListener(_storage_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
						@Override
						public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
							return storage.child(path_name).getDownloadUrl();
						}}).addOnCompleteListener(_storage_upload_success_listener);
					allFir = true;
				}
			}
		}
	}
	
	
	public void _MessageReply(final String _msg, final String _reply) {
		timest = Calendar.getInstance(); 
		chat_key = Chat1.push().getKey(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("msg2", _msg); 
		chatmap.put("reply", _reply); 
		chatmap.put("p", _reply);
		chatmap.put("status", "null"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		if (getIntent().hasExtra("group")) {
			chatmap.put("dp", mydp); 
		}
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		pending.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("subimg", "null"); 
		currentUserUpdateMap.put("lastMessage", "You Reply : " + _reply); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", "null");
				updateMap.put("lastMessage", _reply); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
		message_body_in_reply.setVisibility(View.GONE);
	}
	
	
	public void _Voice_msg_sender(final String _url) {
		timest = Calendar.getInstance(); 
		chat_key = Chat1.push().getKey(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("voice", _url); 
		chatmap.put("p", "Sent a voice message"); 
		chatmap.put("status", "uns"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		if (getIntent().hasExtra("group")) {
			chatmap.put("dp", mydp); 
		}
		
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		pending.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("subimg", "null"); 
		currentUserUpdateMap.put("lastMessage", "You sent a voice message"); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", "null");
				updateMap.put("lastMessage", "Sent a voice message"); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
	}
	
	
	public void _initializeMediaPlayer(final String _url, final ProgressBar _progressbar1, final MediaPlayer _mp6) {
		mp6 = new MediaPlayer();
		Timer _timer = new Timer(); // Initialize the timer
		
		try {
			mp6.setDataSource(_url); // Set the data source for the audio file
			mp6.prepare(); // Prepare synchronously
			mp6.start(); // Start playback
			
			prog = mp6.getCurrentPosition();
			
			// Set the max value of the progress bar to the duration of the audio
			
			
			// TimerTask to update the progress bar every second
			TimerTask t = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (mp6 != null && mp6.isPlaying()) {
								// Update the progress bar based on the current position
								
							}
						}
					});
				}
			};
			
			// Schedule the TimerTask
			_timer.scheduleAtFixedRate(t, 1000, 1000);
			
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Error initializing player!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	public void _PlayVoiceMessage(final String _url, final TextView _txt) {
		if (mp6 == null) {
			// Initialize MediaPlayer if not already created      
			_txt.setText("Voice Message");
			_initializeMediaPlayer(_url, progressbar1, mp6);
			_txt.setText("Playing..");
		} else {
			// If mp6 already exists
			if (mp6.isPlaying()) {
				// If currently playing, stop and release it
				
				mp6.stop();
				mp6.release();
				mp6 = null; // Clear reference
				
				// Start new playback           
				_txt.setText("Playing..");
				_initializeMediaPlayer(_url, progressbar1, mp6);
			} else {
				// If not playing             
				_txt.setText("Voice Message");
				_initializeMediaPlayer(_url, progressbar1, mp6);
				_txt.setText("Playing..");
			} 
		}
	}
	
	
	public void _PRT_Sender(final String _url) {
		//New data push method
		
		timest = Calendar.getInstance(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("prt", _url);
		chatmap.put("p", "Sent a Photo"); 
		chatmap.put("status", "uns"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		if (getIntent().hasExtra("group")) {
			chatmap.put("dp", mydp); 
		}
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		final HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("subimg", _url);
		currentUserUpdateMap.put("lastMessage", " Sent a Photo"); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", _url); 
				updateMap.put("lastMessage", "Sent a Photo"); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
	}
	
	
	public void _EncryptedStringKey(final String _string, final String _key) {
		try {
			
			javax.crypto.SecretKey key = generateKey(_key);
			
			javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
			
			c.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
			
			byte[] encVal = c.doFinal(_string.getBytes());
			
			encrypted = android.util.Base64.encodeToString(encVal,android.util.Base64.DEFAULT);
			if (prt) {
				_PRT_Sender(encrypted);
				prt = false;
			} else {
				if (file.contains("dKey")) {
					_TextMessage(encrypted);
				} else {
					_TextMessage(_string);
				}
			}
		} catch (Exception ex) {
			showMessage(String.valueOf(ex));
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
	
	
	public void _Decryption(final TextView _set, final String _string, final String _key) {
		try {
			
			javax.crypto.spec.SecretKeySpec key = (javax.crypto.spec.SecretKeySpec) generateKey(_key);
			
			javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
			
			c.init(javax.crypto.Cipher.DECRYPT_MODE,key);
			
			byte[] decode = android.util.Base64.decode(_string,android.util.Base64.DEFAULT);
			
			byte[] decval = c.doFinal(decode);
			
			decrypted = new String(decval);
			_set.setText(decrypted);
		} catch (Exception ex) {
			_set.setText(_string);
		}
	}
	
	
	public void _edittext_mh(final TextView _txt) {
		/* Code Credit :  https://web.sketchub.in/p/1613 */
		final TextView regex1 = new TextView(this);
		
		regex1.setText("(?<![^\\s])(([@]{1}|[#]{1})([A-Za-z0-9_-]\\.?)+)(?![^\\s,])");
		final String mentionColor = "#2196F3";
		_txt.addTextChangedListener(new TextWatcher() {
			ColorScheme keywords1 = new ColorScheme(java.util.regex.Pattern.compile(regex1.getText().toString()), Color.parseColor(mentionColor));
			final ColorScheme[] schemes = {keywords1};
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				removeSpans(s, android.text.style.ForegroundColorSpan.class);
				for(ColorScheme scheme : schemes) {
					for(java.util.regex.Matcher m = scheme.pattern.matcher(s);
					m.find();) {
						s.setSpan(new android.text.style.ForegroundColorSpan(scheme.color), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						s.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						
					}
				}
			}
			void removeSpans(Editable e, Class type) {
				android.text.style.CharacterStyle[] spans = e.getSpans(0, e.length(), type);
				for (android.text.style.CharacterStyle span : spans) {
					e.removeSpan(span);
				}
			}
			class ColorScheme {
				final java.util.regex.Pattern pattern;
				final int color;
				ColorScheme(java.util.regex.Pattern pattern, int color) {
					this.pattern = pattern;
					this.color = color;
				}
			}
		});
	}
	
	
	public void _video_sender(final String _url) {
		timest = Calendar.getInstance(); 
		chat_key = Chat1.push().getKey(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("video", _url); 
		chatmap.put("p", "Sent a Video"); 
		chatmap.put("filename", path_name);
		chatmap.put("status", "uns"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		if (getIntent().hasExtra("group")) {
			chatmap.put("dp", mydp); 
		}
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		pending.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("subimg", "null"); 
		currentUserUpdateMap.put("lastMessage", "You sent a Video"); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", "null");
				updateMap.put("lastMessage", "Sent a Video"); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
	}
	
	
	public void _allFiles_Sender(final String _url) {
		timest = Calendar.getInstance(); 
		chat_key = Chat1.push().getKey(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("file", _url); 
		chatmap.put("p", "Sent a File"); 
		chatmap.put("filename", path_name);
		chatmap.put("status", "uns"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		if (getIntent().hasExtra("group")) {
			chatmap.put("dp", mydp); 
		}
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		pending.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("subimg", "null"); 
		currentUserUpdateMap.put("lastMessage", "You sent a File"); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", "null");
				updateMap.put("lastMessage", "Sent a File"); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
	}
	
	
	public void _audio_sender(final String _url) {
		timest = Calendar.getInstance(); 
		chat_key = Chat1.push().getKey(); 
		
		// Prepare chat message map
		chatmap = new HashMap<>(); 
		chatmap.put("dp", mydp);
		chatmap.put("sender", sender_name); 
		chatmap.put("re", getIntent().getStringExtra("user2")); 
		chatmap.put("key", chat_key); 
		chatmap.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("audio", _url); 
		chatmap.put("p", "Sent a Audio"); 
		chatmap.put("filename", path_name);
		chatmap.put("status", "uns"); 
		chatmap.put("timestamp", String.valueOf((long) (timest.getTimeInMillis()))); 
		chatmap.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid()); 
		chatmap.put("date", new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime())); 
		
		if (getIntent().hasExtra("group")) {
			chatmap.put("dp", mydp); 
		}
		
		// Update message in both Chat1 and Chat2
		Chat1.child(chat_key).updateChildren(chatmap); 
		Chat2.child(chat_key).updateChildren(chatmap); 
		pending.child(chat_key).updateChildren(chatmap); 
		
		// Update inbox for the current user under inbox/{current_user}/{user2}
		DatabaseReference currentUserInboxRef = _firebase.getReference("inbox")
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		.child(getIntent().getStringExtra("user2"));
		
		HashMap<String, Object> currentUserUpdateMap = new HashMap<>();
		currentUserUpdateMap.put("subimg", "null"); 
		currentUserUpdateMap.put("lastMessage", "You sent a Audio file"); // Last message sent
		currentUserUpdateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
		currentUserInboxRef.updateChildren(currentUserUpdateMap);
		
		// Update inbox for the recipient under inbox/{user2}/{current_user}
		final DatabaseReference recipientInboxRef = _firebase.getReference("inbox")
		.child(getIntent().getStringExtra("user2"))
		.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
		
		recipientInboxRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				long unreadCount = 0;
				if (snapshot.hasChild("unreadCount")) {
					unreadCount = snapshot.child("unreadCount").getValue(Long.class);
				}
				HashMap<String, Object> updateMap = new HashMap<>();
				updateMap.put("subimg", "null");
				updateMap.put("lastMessage", "Sent a Audio file"); // Last message sent
				updateMap.put("timestamp", timest.getTimeInMillis()); // Timestamp
				updateMap.put("unreadCount", unreadCount + 1); // Increment unread count
				
				recipientInboxRef.updateChildren(updateMap);
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// Handle error if needed
			}
		});
		
		// Clear chat map and reset input field
		chatmap.clear(); 
		message.setText(""); 
		
		// Scroll the RecyclerView to the latest message
		ts = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (0 < chats.size()) { 
							recyclerview1.smoothScrollToPosition(chats.size() - 1); 
						}
					}
				});
			}
		};
		_timer.schedule(ts, 260);
		
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
	
	
	public void _groupChat() {
		Chat1.removeEventListener(_Chat1_child_listener);
		Chat2.removeEventListener(_Chat2_child_listener);
		chatroom = "chat/".concat("all".concat("/".concat(getIntent().getStringExtra("user2"))));
		chatcopy = "chat/".concat(getIntent().getStringExtra("user2").concat("/".concat("all")));
		Chat1 = _firebase.getReference(chatroom);
		
		Chat2 = _firebase.getReference(chatcopy);
		Chat1.addChildEventListener(_Chat1_child_listener);
		Chat2.addChildEventListener(_Chat2_child_listener);
	}
	
	
	public void _Setcolor(final TextView _view, final String _color) {
		_view.setTextColor(Color.parseColor(_color)); // Set text color to white
		
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
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.chat_lists, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout date = _view.findViewById(R.id.date);
			final LinearLayout main = _view.findViewById(R.id.main);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final de.hdodenhof.circleimageview.CircleImageView disappeared = _view.findViewById(R.id.disappeared);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final LinearLayout linear_group = _view.findViewById(R.id.linear_group);
			final ImageView admin = _view.findViewById(R.id.admin);
			final de.hdodenhof.circleimageview.CircleImageView group_img = _view.findViewById(R.id.group_img);
			final LinearLayout voice_message = _view.findViewById(R.id.voice_message);
			final androidx.cardview.widget.CardView linkPreview = _view.findViewById(R.id.linkPreview);
			final androidx.cardview.widget.CardView image_box = _view.findViewById(R.id.image_box);
			final LinearLayout reply_main = _view.findViewById(R.id.reply_main);
			final LinearLayout chat_main = _view.findViewById(R.id.chat_main);
			final LinearLayout chat_time = _view.findViewById(R.id.chat_time);
			final ImageView image_voice = _view.findViewById(R.id.image_voice);
			final TextView vm_text = _view.findViewById(R.id.vm_text);
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final ImageView imageview_download = _view.findViewById(R.id.imageview_download);
			final ProgressBar progressbar1 = _view.findViewById(R.id.progressbar1);
			final LinearLayout link_pre_child = _view.findViewById(R.id.link_pre_child);
			final androidx.cardview.widget.CardView link_img = _view.findViewById(R.id.link_img);
			final TextView tittle = _view.findViewById(R.id.tittle);
			final TextView link_dec = _view.findViewById(R.id.link_dec);
			final TextView text_link = _view.findViewById(R.id.text_link);
			final LinearLayout linear_link_img_box = _view.findViewById(R.id.linear_link_img_box);
			final ImageView imageview_icon = _view.findViewById(R.id.imageview_icon);
			final ImageView image = _view.findViewById(R.id.image);
			final ImageView play = _view.findViewById(R.id.play);
			final androidx.cardview.widget.CardView reply_cardview = _view.findViewById(R.id.reply_cardview);
			final TextView reply_txt = _view.findViewById(R.id.reply_txt);
			final LinearLayout reply_card_child = _view.findViewById(R.id.reply_card_child);
			final LinearLayout reply_bar = _view.findViewById(R.id.reply_bar);
			final TextView chat_2 = _view.findViewById(R.id.chat_2);
			final de.hdodenhof.circleimageview.CircleImageView view_lock = _view.findViewById(R.id.view_lock);
			final TextView text_chat = _view.findViewById(R.id.text_chat);
			final TextView edit = _view.findViewById(R.id.edit);
			final TextView time_txt = _view.findViewById(R.id.time_txt);
			final ImageView tick_image = _view.findViewById(R.id.tick_image);
			
			// ===== Sender-Receiver-Identification =====
			if (_data.get((int)_position).containsKey("id")) {
				if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(_data.get((int)_position).get("id").toString())) {
					// === SenderStyling ===
					chat_main.setBackgroundResource(R.drawable.sender); 
					reply_main.setBackgroundResource(R.drawable.sender); 
					voice_message.setBackgroundResource(R.drawable.sender); 
					main.setGravity(Gravity.RIGHT);
					linear_group.setGravity(Gravity.RIGHT);
					chat_time.setVisibility(View.VISIBLE);
					reply_cardview.setCardBackgroundColor(0xFFC2F5D9);
					text_chat.setTextColor(0xFFFFFFFF);
					vm_text.setTextColor(0xFFFFFFFF);
					
					group_img.setVisibility(View.GONE);
					admin.setVisibility(View.GONE);
					
				}
				else {
					// === ReceiverStyling ===
					chat_main.setBackgroundResource(R.drawable.reciever); 
					reply_main.setBackgroundResource(R.drawable.reciever);
					voice_message.setBackgroundResource(R.drawable.reciever);
					main.setGravity(Gravity.LEFT);
					linear_group.setGravity(Gravity.LEFT);
					chat_time.setVisibility(View.GONE);
					reply_cardview.setCardBackgroundColor(0xFFECECEC);
					text_chat.setTextColor(0xFF1C1C1C);
					vm_text.setTextColor(0xFF000000);
					
					
					// === GroupChatImageHandling ===
					
					if (getIntent().getStringExtra("group").equals("true")) {
						
						if (_data.get((int)_position).containsKey("dp")) {
							group_img.setVisibility(View.VISIBLE);
							Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("dp").toString())).into(group_img);
						}
						else {
							group_img.setVisibility(View.GONE);
						}
						
					}
					else {
						group_img.setVisibility(View.GONE);
					}
					
					if (_data.get((int)_position).containsKey("admin")) {
						
						if (_data.get((int)_position).get("admin").toString().equals(_data.get((int)_position).get("id").toString())) {
							
							admin.setVisibility(View.VISIBLE); 
							
						}
						else {
							
							admin.setVisibility(View.GONE); 
							
						}
						
					}else {
						
						admin.setVisibility(View.GONE); 
						
					}
					
					
					
					
					
				}
			}
			
			// ===== TextMessageHandling =====
			if (_data.get((int)_position).containsKey("msg")) {
				view_lock.setVisibility(View.GONE);
				voice_message.setVisibility(View.GONE);
				text_chat.setText(_data.get((int)_position).get("msg").toString());
				
				// === LinkPreviewHandling ===
				if (8 < _data.get((int)_position).get("msg").toString().length()) {
					if ("https://".equals(_data.get((int)_position).get("msg").toString().substring((int)(0), (int)(8)))) {
						linkPreview.setVisibility(View.VISIBLE);
						chat_main.setVisibility(View.GONE);
						netURLPreview = _data.get((int)_position).get("msg").toString();
						net.startRequestNetwork(RequestNetworkController.GET, netURLPreview, String.valueOf((long)(netURLPreview.hashCode())), _net_request_listener);
						text_link.setText(_data.get((int)_position).get("msg").toString());
						
						// === LinkPreviewDataHandling ===
						if (file.contains(String.valueOf((long)(netURLPreview.hashCode())))) {
							tittle.setVisibility(View.VISIBLE);
							tittle.setText(file.getString(String.valueOf((long)(netURLPreview.hashCode())), ""));
						}
						else {
							tittle.setVisibility(View.GONE);
						}
						if (file.contains("dec".concat(String.valueOf((long)(netURLPreview.hashCode()))))) {
							link_dec.setVisibility(View.VISIBLE);
							link_dec.setText(file.getString("dec".concat(String.valueOf((long)(netURLPreview.hashCode()))), ""));
						}
						else {
							link_dec.setVisibility(View.GONE);
						}
						if (file.contains("url".concat(String.valueOf((long)(netURLPreview.hashCode()))))) {
							Glide.with(getApplicationContext()).load(Uri.parse(file.getString("url".concat(String.valueOf((long)(netURLPreview.hashCode()))), ""))).into(imageview_icon);
							link_img.setVisibility(View.VISIBLE);
						}
						else {
							link_img.setVisibility(View.GONE);
						}
					}
					else {
						chat_main.setVisibility(View.VISIBLE);
						linkPreview.setVisibility(View.GONE);
					}
				}
				else {
					chat_main.setVisibility(View.VISIBLE);
					linkPreview.setVisibility(View.GONE);
				}
			}
			else {
				// === SpecialMessageHandling ===
				if (_data.get((int)_position).containsKey("prt_open")) {
					chat_main.setVisibility(View.VISIBLE);
					text_chat.setText("Opened");
					_ImageColor(view_lock, "#BDBDBD");
					linkPreview.setVisibility(View.GONE);
					view_lock.setImageResource(R.drawable.view1);
					view_lock.setVisibility(View.VISIBLE);
				}
				else {
					if (_data.get((int)_position).containsKey("prt")) {
						chat_main.setVisibility(View.VISIBLE);
						text_chat.setText("Photo");
						view_lock.setVisibility(View.VISIBLE);
						_ImageColor(view_lock, "#FFFFFF");
						linkPreview.setVisibility(View.GONE);
						view_lock.setImageResource(R.drawable.view1);
					}
					else {
						view_lock.setVisibility(View.GONE);
						chat_main.setVisibility(View.GONE);
						linkPreview.setVisibility(View.GONE);
					}
				}
			}
			
			// ===== ReplyMessageHandling =====
			if (_data.get((int)_position).containsKey("reply")) {
				reply_main.setVisibility(View.VISIBLE);
				chat_2.setText(_data.get((int)_position).get("msg2").toString());
				reply_txt.setText(_data.get((int)_position).get("reply").toString());
				linkPreview.setVisibility(View.GONE);
			}
			else {
				reply_main.setVisibility(View.GONE);
			}
			
			// ===== MediaAttachments =====
			if (_data.get((int)_position).containsKey("img")) {
				Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("img").toString())).into(image);
				image_box.setVisibility(View.VISIBLE);
				voice_message.setVisibility(View.GONE);
				chat_main.setVisibility(View.GONE);
				linkPreview.setVisibility(View.GONE);
				play.setVisibility(View.GONE);
			}
			else {
				if (_data.get((int)_position).containsKey("video")) {
					Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("video").toString())).into(image);
					image_box.setVisibility(View.VISIBLE);
					voice_message.setVisibility(View.GONE);
					chat_main.setVisibility(View.GONE);
					linkPreview.setVisibility(View.GONE);
					play.setVisibility(View.VISIBLE);
				}
				else {
					image_box.setVisibility(View.GONE);
					play.setVisibility(View.GONE);
				}
			}
			
			// ===== FileAttachments =====
			if (_data.get((int)_position).containsKey("voice")) {
				voice_message.setVisibility(View.VISIBLE);
				chat_main.setVisibility(View.GONE);
				vm_text.setText("Voice Message");
				image_voice.setImageResource(R.drawable.recording);
				linear1.setVisibility(View.GONE);
				linkPreview.setVisibility(View.GONE);
			}
			else {
				if (_data.get((int)_position).containsKey("file")) {
					voice_message.setVisibility(View.VISIBLE);
					chat_main.setVisibility(View.GONE);
					vm_text.setText(_data.get((int)_position).get("filename").toString());
					image_voice.setImageResource(R.drawable.files);
					linear1.setVisibility(View.GONE);
					linkPreview.setVisibility(View.GONE);
				}
				else {
					if (_data.get((int)_position).containsKey("audio")) {
						voice_message.setVisibility(View.VISIBLE);
						chat_main.setVisibility(View.GONE);
						vm_text.setText(_data.get((int)_position).get("filename").toString());
						image_voice.setImageResource(R.drawable.music);
						linear1.setVisibility(View.GONE);
						linkPreview.setVisibility(View.GONE);
					}
					else {
						voice_message.setVisibility(View.GONE);
					}
				}
			}
			
			// ===== DisappearingMessages =====
			if (_data.get((int)_position).containsKey("sub_img")) {
				disappeared.setVisibility(View.VISIBLE);
				Glide.with(getApplicationContext()).load(Uri.parse(_data.get((int)_position).get("sub_img").toString())).into(disappeared);
				linkPreview.setVisibility(View.GONE);
			}
			else {
				disappeared.setVisibility(View.GONE);
			}
			
			// ===== TimestampStatusHandling =====
			if (_data.get((int)_position).containsKey("timestamp")) {
				t24 = Calendar.getInstance();
				_setTime(Double.parseDouble(_data.get((int)_position).get("timestamp").toString()), time_txt);
				if ("true".equals(file.getString("dis", ""))) {
					disappeared.setVisibility(View.VISIBLE);
					if ((t24.getTimeInMillis() - Double.parseDouble(_data.get((int)_position).get("timestamp").toString())) > 86400000) {
						getMSG_key = _data.get((int)_position).get("key").toString();
						Chat1.child(getMSG_key).removeValue();
						Chat2.child(getMSG_key).removeValue();
					}
				}
				else {
					disappeared.setVisibility(View.GONE);
				}
			}
			
			if (_data.get((int)_position).containsKey("status")) {
				if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(_data.get((int)_position).get("id").toString())) {
					tick_image.setVisibility(View.VISIBLE);
					if ("seen".equals(_data.get((int)_position).get("status").toString())) {
						tick_image.setImageResource(R.drawable.ic_done_all_white);
						tick_image.setColorFilter(0xFF2196F3, PorterDuff.Mode.MULTIPLY);
					}
					else {
						tick_image.setImageResource(R.drawable.ic_done_white);
						tick_image.setColorFilter(0xFF9E9E9E, PorterDuff.Mode.MULTIPLY);
					}
				}
				else {
					tick_image.setVisibility(View.GONE);
				}
			}
			
			// ===== DateHeaderHandling =====
			edit.setVisibility(View.GONE);
			textview1.setBackground(new GradientDrawable() { 
				public GradientDrawable getIns(int a, int b) { 
					this.setCornerRadius(a); 
					this.setColor(b); 
					return this; 
				} 
			}.getIns((int)20, 0xFFF7F7F7));
			textview1.setTextColor(0xFF5C6267);
			if (_data.get((int)_position).containsKey("date")) {
				timest = Calendar.getInstance();
				if (_data.get((int)_position).get("date").toString().equals(new SimpleDateFormat("E, MMM dd yyyy").format(timest.getTime()))) {
					textview1.setText("Today");
				}
				else {
					textview1.setText(_data.get((int)_position).get("date").toString());
				}
				if (_position == 0) {
					date.setVisibility(View.VISIBLE);
				}
				else {
					if (_data.get((int)_position).get("date").toString().equals(_data.get((int)_position - 1).get("date").toString())) {
						date.setVisibility(View.GONE);
					}
					else {
						date.setVisibility(View.VISIBLE);
					}
				}
			}
			
			// ===== ClickListeners =====
			main.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View _view) {
					a.setClass(getApplicationContext(), AllInOneActivity.class);
					if (_data.get((int)_position).containsKey("msg")) {
						a.putExtra("copy", _data.get((int)_position).get("msg").toString());
					}
					if (_data.get((int)_position).containsKey("id")) {
						a.putExtra("id", _data.get((int)_position).get("id").toString());
					}
					if (_data.get((int)_position).containsKey("key")) {
						a.putExtra("key", _data.get((int)_position).get("key").toString());
					}
					if (_data.get((int)_position).containsKey("prt")) {
						a.putExtra("copy", "1Photo");
					}
					a.putExtra("user1", FirebaseAuth.getInstance().getCurrentUser().getUid());
					a.putExtra("user2", getIntent().getStringExtra("user2"));
					startActivity(a);
					return true;
				}
			});
			
			main.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if (_data.get((int)_position).containsKey("voice")) {
						a.setClass(getApplicationContext(), MplayerActivity.class);
						a.putExtra("url", _data.get((int)_position).get("voice").toString());
						startActivity(a);
					}
					else {
						if (_data.get((int)_position).containsKey("prt_open")) {
							// Empty handler
						}
						else {
							if (_data.get((int)_position).containsKey("prt")) {
								if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(_data.get((int)_position).get("id").toString())) {
									a.setClass(getApplicationContext(), ImageshowActivity.class);
									a.putExtra("url", _data.get((int)_position).get("prt").toString());
									a.putExtra("v1", _data.get((int)_position).get("key").toString());
									a.putExtra("enc", "true");
									a.putExtra("user1", FirebaseAuth.getInstance().getCurrentUser().getUid());
									a.putExtra("user2", getIntent().getStringExtra("user2"));
									startActivity(a);
								}
							}
							else {
								if (_data.get((int)_position).containsKey("img")) {
									a.setClass(getApplicationContext(), ImageshowActivity.class);
									a.putExtra("url", _data.get((int)_position).get("img").toString());
									a.putExtra("user1", FirebaseAuth.getInstance().getCurrentUser().getUid());
									a.putExtra("enc", "false");
									a.putExtra("user2", getIntent().getStringExtra("user2"));
									startActivity(a);
								}
								else {
									if (_data.get((int)_position).containsKey("video")) {
										a.setClass(getApplicationContext(), VideoShowActivity.class);
										a.putExtra("url", _data.get((int)_position).get("video").toString());
										a.putExtra("user1", FirebaseAuth.getInstance().getCurrentUser().getUid());
										a.putExtra("enc", "false");
										a.putExtra("user2", getIntent().getStringExtra("user2"));
										startActivity(a);
									}
									else {
										if (_data.get((int)_position).containsKey("audio")) {
											a.setClass(getApplicationContext(), MplayerActivity.class);
											a.putExtra("url", _data.get((int)_position).get("audio").toString());
											startActivity(a);
										}
										else {
											if (_data.get((int)_position).containsKey("msg")) {
												if (8 < _data.get((int)_position).get("msg").toString().length()) {
													if ("https://".equals(_data.get((int)_position).get("msg").toString().substring((int)(0), (int)(8)))) {
														netURLPreview = _data.get((int)_position).get("msg").toString();
														a.setClass(getApplicationContext(), BrownerActivity.class);
														a.putExtra("url", netURLPreview);
														startActivity(a);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			});
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