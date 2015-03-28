package com.example.ohhye.packagemovie.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ohhye.packagemovie.R;
import com.example.ohhye.packagemovie.log.Logger;
import com.example.ohhye.packagemovie.ui.ThemeRadioButton;
import com.example.ohhye.packagemovie.util.ConvertToUtils;
import com.example.ohhye.packagemovie.util.manager.FileManager;
import com.yixia.camera.FFMpegUtils;
import com.yixia.camera.MediaRecorder;
import com.yixia.camera.MediaRecorder.OnErrorListener;
import com.yixia.camera.MediaRecorder.OnPreparedListener;
import com.yixia.camera.MediaRecorderFilter;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.StringUtils;
import com.yixia.camera.view.CameraNdkView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class CameraActivity2 extends BaseActivity implements OnErrorListener, OnClickListener, OnPreparedListener, Animation.AnimationListener, SensorEventListener {


    private final static int[] FILTER_ICONS = new int[] { R.drawable.filter_original, R.drawable.filter_black_white, R.drawable.filter_sharpen, R.drawable.filter_old_film,  R.drawable.filter_anti_color, R.drawable.filter_radial, R.drawable.filter_8bit, R.drawable.filter_lomo };

    private final static String[] FILTER_VALUES = new String[] { MediaRecorderFilter.CAMERA_FILTER_NO, MediaRecorderFilter.CAMERA_FILTER_BLACKWHITE, MediaRecorderFilter.CAMERA_FILTER_SHARPEN, MediaRecorderFilter.CAMERA_FILTER_OLD_PHOTOS, MediaRecorderFilter.CAMERA_FILTER_ANTICOLOR, MediaRecorderFilter.CAMERA_FILTER_PASS_THROUGH, MediaRecorderFilter.CAMERA_FILTER_MOSAICS, MediaRecorderFilter.CAMERA_FILTER_REMINISCENCE };

    public final static int REQUEST_CODE_IMPORT_IMAGE = 999;

    public final static int REQUEST_CODE_IMPORT_VIDEO = 998;

    public final static int REQUEST_CODE_IMPORT_VIDEO_EDIT = 997;

    public final static int RECORD_TIME_MAX = 100 * 1000;

    private boolean isRecording = false;
    private boolean isFrontCamera = false;
    private boolean setting_menu_visible = false;
    private boolean s_isClose = false;
    private boolean s_isDark = false;
    private boolean isGrid  = false;
    private boolean isFlash = false;
    private boolean isMotion = false;
    private boolean isFilterMenu = false;

    private Animation aniShow, aniHide;

    private Button btn_camera_settings_filter;

    private CameraNdkView mSurfaceView;
    /** 滤镜容器 */
    private RadioGroup mRecordFilterContainer;
    private View filter_menu;
    private View settings_menu;

    private ImageView camera_grid;
    private ImageView btn_camera_record;
    private ImageView btn_camera_change;
    private ImageView btn_camera_settings;

    private Button btn_camera_settings_grid;
    private Button btn_camera_settings_flash;
    private Button btn_camera_settings_motion;
    private Button btn_camera_settings_timer;

    private MediaRecorderFilter mMediaRecorder;
    private MediaObject mMediaObject;
    private int mWindowWidth;

    //Sensor
    private SensorManager sensorManager;
    private Sensor proximitySensor;

    //Timer
    private int timer_time = 0;
    private TimerTask mTask;
    private Timer mTimer;


    private String fileName = "";
    private Camera mCamera = null;
    private Camera.Parameters mCameraParameter;
    private SurfaceHolder mHorder;

    private volatile boolean mPressedStatus, mReleased, mStartEncoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mWindowWidth = DeviceUtils.getScreenWidth(this);
        setContentView(R.layout.activity_camera_activity2);

        mSurfaceView = (CameraNdkView) findViewById(R.id.camera_preview);
        init();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSurfaceView.getLayoutParams().height = dm.heightPixels;
        //mSurfaceView.getLayoutParams().height;

        //Sensor
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Timer
        mTimer = new Timer();
    }

    private void init(){

        settings_menu = findViewById(R.id.menu_camera_settings);

        btn_camera_settings_filter = (Button) findViewById(R.id.btn_camera_settings_filter);
        mRecordFilterContainer = (RadioGroup) findViewById(R.id.record_filter_container);
        filter_menu = findViewById(R.id.menu_camera_settings_filter_menu);

        camera_grid = (ImageView)findViewById(R.id.camera_grid);

        btn_camera_settings_grid = (Button)findViewById(R.id.btn_camera_settings_grid);
        btn_camera_settings_flash = (Button)findViewById(R.id.btn_camera_settings_flash);
        btn_camera_settings_motion = (Button)findViewById(R.id.btn_camera_settings_motion);
        btn_camera_settings_timer = (Button)findViewById(R.id.btn_camera_settings_timer);

        btn_camera_settings = (ImageView)findViewById(R.id.btn_camera_settings);
        btn_camera_change = (ImageView)findViewById(R.id.btn_camera_change);
        btn_camera_record = (ImageView)findViewById(R.id.btn_camera_record);

        //Listener Register
        btn_camera_settings_grid.setOnClickListener(this);
        btn_camera_settings_flash.setOnClickListener(this);
        btn_camera_settings_motion.setOnClickListener(this);
        btn_camera_settings_timer.setOnClickListener(this);
        btn_camera_settings_filter.setOnClickListener(this);

        btn_camera_settings.setOnClickListener(this);
        btn_camera_change.setOnClickListener(this);
        btn_camera_record.setOnClickListener(this);
        mSurfaceView.setOnClickListener(this);
        camera_grid.setOnClickListener(this);


        camera_grid.setOnClickListener(this);
        camera_grid.setVisibility(View.GONE);
        settings_menu.setVisibility(View.GONE);
        filter_menu.setVisibility(View.GONE);

        //Animation
        aniShow = AnimationUtils.loadAnimation(this, R.anim.fade);
        aniShow.setAnimationListener(this);
        aniHide = AnimationUtils.loadAnimation(this, R.anim.finish_fade);
        aniHide.setAnimationListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mMediaRecorder == null)
            initMediaRecorder();
        else {
            mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
            mMediaRecorder.prepare();
        }

        if (proximitySensor != null)
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaRecorder != null && !mReleased) {
            mMediaRecorder.release();
        }

        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_PROXIMITY:
                if (event.values[0] == 0) {
                    s_isClose = true;
                }
                else
                    s_isClose = false;

                Log.d("Motion-Proximity", "Proximity : " + event.values[0]);

                if (isMotion && s_isClose) {
                    if (!isRecording) {
                        //촬영시작
                        filterMenuOff();
                        settingMenuOff();
                        startRecord();
                        Log.d("Motion", "촬영시작!");
                    } else if (isRecording) {
                        //촬영종료
                        stopRecord();
                        checkSave();
                        Log.d("Motion", "촬영종료!");
                    }
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        Log.d("myTag","------------PAUSE------------");
        super.onPause();

        //mTimer.cancel();
        if(isRecording==true) {
            stopRecord();
        }
    }
    @Override
    protected  void onResume(){
        Log.d("myTag","------------RESUME------------");
        super.onResume();
        if(isRecording==true) {
            // checkReRecord();
            startRecord();
        }

    }

    @Override
    public void onBackPressed() {

        if (mMediaObject != null && mMediaObject.getDuration() > 1) {

            new AlertDialog.Builder(this).setTitle(R.string.hint).setMessage(R.string.record_camera_exit_dialog_message).setNegativeButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mMediaObject.delete();
                    finish();
                }

            }).setPositiveButton(R.string.dialog_no, null).setCancelable(false).show();
            return;
        }

        if (mMediaObject != null)
            mMediaObject.delete();
        super.onBackPressed();
    }

    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderFilter();
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnPreparedListener(this);

        mMediaRecorder.setVideoBitRate(MediaRecorder.VIDEO_BITRATE_HIGH);
        //      mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
        mMediaRecorder.setSurfaceView(mSurfaceView);
        String key = String.valueOf(System.currentTimeMillis());

        mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath() + key);
        //Log.d("mMediaObject", mMediaObject.getObjectFilePath().toString());
        if (mMediaObject != null) {
            mMediaRecorder.prepare();
            mMediaRecorder.setCameraFilter(MediaRecorderFilter.CAMERA_FILTER_NO);
        } else {
            Toast.makeText(this, R.string.record_camera_init_faild, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void startRecord() {

        btn_camera_record.setImageResource(R.drawable.av_stop);
        isRecording = true;
        mPressedStatus = true;

        if (mMediaRecorder != null) {
            mMediaRecorder.startRecord();
        }

        if (mHandler != null) {
            mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
            mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD, RECORD_TIME_MAX - mMediaObject.getDuration());
        }

        mHandler.removeMessages(HANDLE_SHOW_TIPS);
        mHandler.sendEmptyMessage(HANDLE_SHOW_TIPS);
    }

    private void stopRecord() {
        btn_camera_record.setImageResource(R.drawable.device_access_camera);
        isRecording = false;
        mPressedStatus = false;

        if (mMediaRecorder != null)
            mMediaRecorder.stopRecord();

        //取消倒计时
        mHandler.removeMessages(HANDLE_STOP_RECORD);

    }

    /** 是否可回删 */
    private boolean cancelDelete() {
        if (mMediaObject != null) {
            MediaObject.MediaPart part = mMediaObject.getCurrentPart();
            if (part != null && part.remove) {
                part.remove = false;
                return true;
            }
        }
        return false;
    }

    /** 刷新进度条 */
    private static final int HANDLE_INVALIDATE_PROGRESS = 0;
    /** 延迟拍摄停止 */
    private static final int HANDLE_STOP_RECORD = 1;
    /** 显示下一步 */
    private static final int HANDLE_SHOW_TIPS = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_INVALIDATE_PROGRESS:
                    if (mMediaObject != null && !isFinishing()) {
                        if (mPressedStatus)
                            sendEmptyMessageDelayed(0, 30);
                    }
                    break;
                case HANDLE_SHOW_TIPS:
                    if (mMediaRecorder != null && !isFinishing()) {
                        int duration = checkStatus();

                        if (mPressedStatus) {
                            if (duration < RECORD_TIME_MAX) {
                                sendEmptyMessageDelayed(HANDLE_SHOW_TIPS, 200);
                            } else {
                                sendEmptyMessageDelayed(HANDLE_SHOW_TIPS, 500);
                            }
                        }
                    }
                    break;
                case HANDLE_STOP_RECORD:
                    stopRecord();
                    startEncoding();
                    break;
            }
        }
    };



    private int checkStatus() {
        int duration = 0;
        if (!isFinishing() && mMediaObject != null) {
            duration = mMediaObject.getDuration();
        }
        return duration;
    }

    protected InputFilter spaceFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                char currentChar = source.charAt(i);
                if (Character.isSpaceChar(currentChar) || currentChar == '\\') {
                    return "";
                }
            }
            return null;
        }
    };
    //
//    private void reR
    private void checkSave() {
        //동영상제목 설정
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("동영상 제목");
        alert.setMessage("촬영한 영상의 제목을 입력해주세요");

        final EditText input = new EditText(this);
        input.setSingleLine();

        input.setHint("ex)발리에서 생긴일");
        input.setFilters(new InputFilter[] { spaceFilter });

        alert.setView(input);
        alert.setNegativeButton("저장 취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mMediaObject != null) {
                            MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                            if (part != null) {
                                mMediaObject.removePart(part, true);
                            }
                            checkStatus();
                        }
                    }
                });

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (value.equals("")) {
                    Toast.makeText(getApplication(), "반드시 한글 자 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
                    checkSave();
                } else {
                    fileName = value;
                    Log.d("fileName", fileName);
                    // 파일 이름 변경
                    startEncoding();
                }
            }
        });
        alert.show();
    }



    private void startEncoding() {

        if (FileUtils.showFileAvailable() < 200) {
            Toast.makeText(this, R.string.record_camera_check_available_faild, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("startEncoding", "start!!");
        if (!isFinishing() && mMediaRecorder != null && mMediaObject != null && !mStartEncoding) {
            mStartEncoding = true;

            EncodeAsyncTask encodeAsyncTask = new EncodeAsyncTask();
            encodeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public class EncodeAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = FFMpegUtils.videoTranscoding(mMediaObject, mMediaObject.getOutputVideoPath(), mWindowWidth, false);
            Log.d("doinBackground", ""+result);
            if (result && mMediaRecorder != null) {
                mMediaRecorder.release();
                mReleased = true;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("startProgress", "start!!");
            showProgress("", getString(R.string.record_camera_progress_message));
            Log.d("startProgress", "end!!");
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            mStartEncoding = false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            hideProgress();
            if (result) {

                if (saveMediaObject(mMediaObject)) {
                    //Intent intent = new Intent(MediaRecorderActivity.this, MediaPreviewActivity.class);
                    //intent.putExtra("obj", mMediaObject.getObjectFilePath());
                    //startActivity(intent);
                    Toast.makeText(CameraActivity2.this, "저장성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CameraActivity2.this, R.string.record_camera_save_faild, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CameraActivity2.this, R.string.record_video_transcoding_faild, Toast.LENGTH_SHORT).show();
            }
            mStartEncoding = false;
            Log.d("video path", mMediaObject.getOutputVideoPath());

            if(changeFileName(fileName)) {
                Log.d("file renameing", "succress");
                finish();
            }
        }
    }

    @Override
    public void onVideoError(int what, int extra) {
        Logger.e("[MediaRecorderActvity]onVideoError: what" + what + " extra:" + extra);
    }

    @Override
    public void onAudioError(int what, String message) {
        Logger.e("[MediaRecorderActvity]onAudioError: what" + what + " message:" + message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CameraActivity2.this, R.string.record_camera_open_audio_faild, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterMenuOff(){
        if (isFilterMenu == true) {
            filter_menu.startAnimation(aniHide);
            filter_menu.setVisibility(View.GONE);
            isFilterMenu = false;
        }
    }

    private void filterMenuOn() {
        filter_menu.setVisibility(View.VISIBLE);
        filter_menu.startAnimation(aniShow);
        isFilterMenu = true;
    }

    private void settingMenuOff(){
        if(setting_menu_visible == true) {
            settings_menu.startAnimation(aniHide);
            settings_menu.setVisibility(View.GONE);
            setting_menu_visible = false;
        }
    }

    private void settingMenuOn(){
        settings_menu.setVisibility(View.VISIBLE);
        settings_menu.startAnimation(aniShow);
        setting_menu_visible = true;
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.camera_grid:
                filterMenuOff();
                settingMenuOff();
                break;

            case R.id.camera_preview:
                filterMenuOff();
                settingMenuOff();
                break;

            case R.id.btn_camera_record:
                if(isRecording == true) {
                    stopRecord();
                    checkSave();
                }
                else {
                    filterMenuOff();
                    settingMenuOff();
                    if(timer_time != 0)
                        startTimer();
                    else
                        startRecord();
                }
                break;

            case R.id.btn_camera_settings:
                //촬영중에는 버튼 비활성화
                if(isRecording ==true) break;

                //닫혀있을때
                if(setting_menu_visible==false)
                    settingMenuOn();

                    //열려있을때
                else if(setting_menu_visible==true)
                {
                    filterMenuOff();
                    settingMenuOff();
                }
                break;

            case R.id.btn_camera_settings_grid:
                filterMenuOff();
                if(isGrid==false)
                {
                    camera_grid.setVisibility(View.VISIBLE);
                    btn_camera_settings_grid.setBackgroundResource(R.drawable.grid_on);
                    isGrid=true;
                }
                else if(isGrid==true)
                {
                    camera_grid.setVisibility(View.GONE);
                    btn_camera_settings_grid.setBackgroundResource(R.drawable.grid_off);
                    isGrid=false;
                }
                break;

            case R.id.btn_camera_settings_flash:
                if(isFrontCamera) break;
                filterMenuOff();
                if(isFlash==false)
                {
                    Log.d("Flash","FlashON");
                    mMediaRecorder.toggleFlashMode();
                    isFlash = true;
                }
                else if(isFlash==true)
                {
                    Log.d("Flash","FlashOFF");
                    mMediaRecorder.toggleFlashMode();
                    isFlash=false;
                }
                break;

            case R.id.btn_camera_settings_motion:
                filterMenuOff();
                if(isMotion==false)
                {
                    btn_camera_settings_motion.setBackgroundResource(R.drawable.motion_on);
                    isMotion=true;
                }
                else if(isMotion==true)
                {
                    btn_camera_settings_motion.setBackgroundResource(R.drawable.motion_off);
                    isMotion=false;
                }
                break;

            case R.id.btn_camera_settings_timer:
                filterMenuOff();
                timer_setting(timer_time);
                break;

            case R.id.btn_camera_settings_filter:
                if (isFilterMenu == true) {
                    filterMenuOff();
                } else {
                    loadFilter();
                    filterMenuOn();
                }
                break;

            case R.id.btn_camera_change:
                isFrontCamera = !isFrontCamera;
                mMediaRecorder.switchCamera();
                //startEncoding();
                break;
        }
    }

    private void timer_setting(int _timer_time) {
        switch (_timer_time){
            case 0:
                timer_time = 3;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_3s);
                break;
            case 3:
                timer_time = 5;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_5s);
                break;
            case 5:
                timer_time = 10;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_10s);
                break;
            case 10:
                timer_time = 0;
                btn_camera_settings_timer.setBackgroundResource(R.drawable.timer_off);
                break;
        }
    }

    public void startTimer()
    {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    public void run(){
                        //촬영시작
                        Log.d("Timer","Complete!!");
                        stopTimer();
                        startRecord();
                    }
                });
            }
        },timer_time*1000);
        Log.d("Timer","Start!!!");
    }

    public void stopTimer()
    {
        if(mTimer != null) {
            // 해당 타이머가 수행할 모든 행위들을 정지시킵니다.
            mTimer.cancel();
            // 대기중이던 취소된 행위가 있는 경우 모두 제거한다.
            mTimer.purge();
            mTimer = null;
        }
        Log.d("Timer", "Stop!!!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_IMPORT_VIDEO_EDIT) {
                mMediaObject = restoneMediaObject(mMediaObject.getObjectFilePath());
            } else {
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        String columnName;
                        switch (requestCode) {
                            case REQUEST_CODE_IMPORT_IMAGE:
                                columnName = MediaStore.Images.Media.DATA;
                                break;
                            case REQUEST_CODE_IMPORT_VIDEO:
                                columnName = MediaStore.Video.Media.DATA;
                                break;
                            default:
                                return;
                        }
                        if (StringUtils.isNotEmpty(columnName)) {
                            Cursor cursor = getContentResolver().query(uri, new String[] { columnName }, null, null, null);
                            if (cursor != null) {
                                String path = "";
                                if (cursor.moveToNext()) {
                                    path = cursor.getString(0);
                                }
                                cursor.close();
                            }
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void loadFilter() {
        if (!isFinishing() && mRecordFilterContainer.getChildCount() == 0) {
            final String[] filterNames = getResources().getStringArray(R.array.record_filter);
            int leftMargin = ConvertToUtils.dipToPX(this, 10);
            LayoutInflater mInflater = LayoutInflater.from(this);
            for (int i = 0; i < FILTER_ICONS.length; i++) {
                ThemeRadioButton filterView = (ThemeRadioButton) mInflater.inflate(R.layout.view_radio_item, null);
                filterView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int index = ConvertToUtils.toInt(v.getTag().toString());
                        if (mMediaRecorder != null)
                            mMediaRecorder.setCameraFilter(FILTER_VALUES[index]);
                    }
                });
                filterView.setCompoundDrawablesWithIntrinsicBounds(0, FILTER_ICONS[i], 0, 0);
                filterView.setText(filterNames[i]);
                filterView.setTag(i);
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = leftMargin;
                mRecordFilterContainer.addView(filterView, lp);
            }

            mRecordFilterContainer.getChildAt(0).performClick();
        }
    }

    @Override
    public void onPrepared() {
        if (mMediaRecorder != null) {
//            mMediaRecorder.autoFocus(new Camera.AutoFocusCallback() {
//
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    if (success) {
//
//                    }
//                }
//            });
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public boolean changeFileName(String filename) {
        String path = mMediaObject.getOutputVideoPath();
        Log.d("output directory", path);

        String currentFileDirectory =  VCamera.getVideoCachePath();
        Log.d("currentFileName2", currentFileDirectory.toString());

        File file = new File(path);
        File renameFile = new File(currentFileDirectory+filename+".mp4");
        Log.d("newFilename", renameFile.getPath().toString());

        if(file.exists())
        {
            file.renameTo(renameFile);
            FileManager.upload_record_file(renameFile.getPath(), filename); //녹화파일 업로드
            Log.d("Path",""+renameFile.getPath());
            return true;
        }
        return false;
    }
}