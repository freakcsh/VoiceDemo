package com.example.jpushdemo;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.freak.voicedemo.R;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.example.jpushdemo.control.InitConfig;
import com.example.jpushdemo.control.MainHandlerConstant;
import com.example.jpushdemo.control.MySyntherizer;
import com.example.jpushdemo.control.NonBlockSyntherizer;
import com.example.jpushdemo.lisenter.UiMessageListener;
import com.example.jpushdemo.uitl.AutoCheck;
import com.example.jpushdemo.uitl.OfflineResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;


public class MainActivity extends InstrumentedActivity implements OnClickListener,MainHandlerConstant {

	private Button mInit;
	private Button mSetting;
	private Button mStopPush;
	private Button mResumePush;
	private Button mGetRid;
	private TextView mRegId;
	private EditText msgText;

	/******************************************************************/
	protected String appId = "14366914";

	protected String appKey = "WGGTazPEG3A5h0YvZMIxBEu5";

	protected String secretKey = "i96aqshr5PyeWTHRmS12bU1ffyCuGeVY";

	protected Handler mainHandler;


	// TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
	protected TtsMode ttsMode = TtsMode.MIX;

	// 离线发音选择，VOICE_FEMALE即为离线女声发音。
	// assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
	// assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
	protected String offlineVoice = OfflineResource.VOICE_MALE;

	// ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================



	// 主控制类，所有合成控制方法从这个类开始
	public static MySyntherizer synthesizer;
	/************************************************************************/
	
	public static boolean isForeground = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mainHandler = new Handler() {
			/*
             * @param msg
             */
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				handle(msg);
			}

		};
		initPermission();
		initView();
		initialTts();
		registerMessageReceiver();  // used for receive msg
	}
	/**************************************************************************************************/
	protected void handle(Message msg) {
		int what = msg.what;
		switch (what) {
			case PRINT:
				Log.e("TAG",msg+"");
				break;
			case UI_CHANGE_INPUT_TEXT_SELECTION:
//                if (msg.arg1 <= mInput.getText().length()) {
//                    mInput.setSelection(0, msg.arg1);
//                }
				break;
			case UI_CHANGE_SYNTHES_TEXT_SELECTION:
//                SpannableString colorfulText = new SpannableString(mInput.getText().toString());
//                if (msg.arg1 <= colorfulText.toString().length()) {
//                    colorfulText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, msg.arg1, Spannable
//                            .SPAN_EXCLUSIVE_EXCLUSIVE);
//                    mInput.setText(colorfulText);
//                }
				break;
			default:
				break;
		}
	}
	/**
	 * android 6.0 以上需要动态申请权限
	 */
	private void initPermission() {
		String[] permissions = {
				Manifest.permission.INTERNET,
				Manifest.permission.ACCESS_NETWORK_STATE,
				Manifest.permission.MODIFY_AUDIO_SETTINGS,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_SETTINGS,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.ACCESS_WIFI_STATE,
				Manifest.permission.CHANGE_WIFI_STATE
		};

		ArrayList<String> toApplyList = new ArrayList<String>();

		for (String perm : permissions) {
			if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
				toApplyList.add(perm);
				// 进入到这里代表没有权限.
			}
		}
		String[] tmpList = new String[toApplyList.size()];
		if (!toApplyList.isEmpty()) {
			ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
		}

	}




	/**
	 * 初始化引擎，需要的参数均在InitConfig类里
	 * <p>
	 * DEMO中提供了3个SpeechSynthesizerListener的实现
	 * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
	 * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
	 * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
	 */
	protected void initialTts() {
		LoggerProxy.printable(true); // 日志打印在logcat中
		// 设置初始化参数
		// 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
		SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

		Map<String, String> params = getParams();


		// appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
		InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

		// 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
		// 上线时请删除AutoCheck的调用
		AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 100) {
					AutoCheck autoCheck = (AutoCheck) msg.obj;
					synchronized (autoCheck) {
						String message = autoCheck.obtainDebugMessage();
//                        toPrint(message); // 可以用下面一行替代，在logcat中查看代码
						Log.w("AutoCheckMessage", message);
					}
				}
			}

		});
		synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
	}

	/**
	 * 合成的参数，可以初始化时填写，也可以在合成前设置。
	 *
	 * @return
	 */
	protected Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		// 以下参数均为选填
		// 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
		params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
		// 设置合成的音量，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
		// 设置合成的语速，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_SPEED, "5");
		// 设置合成的语调，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_PITCH, "5");

		params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
		// 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
		// MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
		// MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
		// MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
		// MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

		// 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
		OfflineResource offlineResource = createOfflineResource(offlineVoice);
		// 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
		params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
		params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
				offlineResource.getModelFilename());
		return params;
	}

	protected OfflineResource createOfflineResource(String voiceType) {
		OfflineResource offlineResource = null;
		try {
			offlineResource = new OfflineResource(this, voiceType);
		} catch (IOException e) {
			// IO 错误自行处理
			e.printStackTrace();
			Log.e("TAG", "【error】:copy files from assets failed." + e.getMessage());
		}
		return offlineResource;
	}



	/*******************************************************************************************************/
	private void initView(){
		TextView mImei = (TextView) findViewById(R.id.tv_imei);
		String udid =  ExampleUtil.getImei(getApplicationContext(), "");
        if (null != udid) mImei.setText("IMEI: " + udid);
        
		TextView mAppKey = (TextView) findViewById(R.id.tv_appkey);
		String appKey = ExampleUtil.getAppKey(getApplicationContext());
		if (null == appKey) appKey = "AppKey异常";
		mAppKey.setText("AppKey: " + appKey);

		mRegId = (TextView) findViewById(R.id.tv_regId);
		mRegId.setText("RegId:");

		String packageName =  getPackageName();
		TextView mPackage = (TextView) findViewById(R.id.tv_package);
		mPackage.setText("PackageName: " + packageName);

		String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
		TextView mDeviceId = (TextView) findViewById(R.id.tv_device_id);
		mDeviceId.setText("deviceId:" + deviceId);
		
		String versionName =  ExampleUtil.GetVersion(getApplicationContext());
		TextView mVersion = (TextView) findViewById(R.id.tv_version);
		mVersion.setText("Version: " + versionName);
		
	    mInit = (Button)findViewById(R.id.init);
		mInit.setOnClickListener(this);
		
		mStopPush = (Button)findViewById(R.id.stopPush);
		mStopPush.setOnClickListener(this);
		
		mResumePush = (Button)findViewById(R.id.resumePush);
		mResumePush.setOnClickListener(this);

		mGetRid = (Button) findViewById(R.id.getRegistrationId);
		mGetRid.setOnClickListener(this);

		mSetting = (Button)findViewById(R.id.setting);
		mSetting.setOnClickListener(this);
		
		msgText = (EditText)findViewById(R.id.msg_rec);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.init:
			init();
			break;
		case R.id.setting:
			Intent intent = new Intent(MainActivity.this, PushSetActivity.class);
			startActivity(intent);
			break;
		case R.id.stopPush:
			JPushInterface.stopPush(getApplicationContext());
			break;
		case R.id.resumePush:
			JPushInterface.resumePush(getApplicationContext());
			break;
		case R.id.getRegistrationId:
			String rid = JPushInterface.getRegistrationID(getApplicationContext());
			if (!rid.isEmpty()) {
				mRegId.setText("RegId:" + rid);
			} else {
				Toast.makeText(this, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init(){
		 JPushInterface.init(getApplicationContext());
	}


	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}


	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
	

	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
					String messge = intent.getStringExtra(KEY_MESSAGE);
					String extras = intent.getStringExtra(KEY_EXTRAS);
					StringBuilder showMsg = new StringBuilder();
					showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
					if (!ExampleUtil.isEmpty(extras)) {
						showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
					}
					setCostomMsg(showMsg.toString());
				}
			} catch (Exception e){
			}
		}
	}
	
	private void setCostomMsg(String msg){
		 if (null != msgText) {
			 msgText.setText(msg);
			 msgText.setVisibility(android.view.View.VISIBLE);
         }
	}

}