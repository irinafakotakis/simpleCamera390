package com.simplemobiletools.camera.activities

import android.app.Activity
import android.content.Intent
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.*
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.divyanshu.colorseekbar.ColorSeekBar
import com.simplemobiletools.camera.BuildConfig
import com.simplemobiletools.camera.R
import com.simplemobiletools.camera.extensions.config
import com.simplemobiletools.camera.extensions.navBarHeight
import com.simplemobiletools.camera.helpers.*
import com.simplemobiletools.camera.implementations.MyCameraImpl
import com.simplemobiletools.camera.interfaces.MyPreview
import com.simplemobiletools.camera.views.CameraPreview
import com.simplemobiletools.camera.views.FocusCircleView
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.*
import com.simplemobiletools.commons.models.Release
import kotlinx.android.synthetic.main.activity_main.*
import android.app.*
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import android.util.Log
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Canvas
import java.io.File
import java.io.FileOutputStream
import java.util.Random
import android.os.Environment
import android.util.DisplayMetrics

class MainActivity : SimpleActivity(), PhotoProcessor.MediaSavedListener {
    private val FADE_DELAY = 5000L

    lateinit var mTimerHandler: Handler
    private lateinit var mOrientationEventListener: OrientationEventListener
    private lateinit var mFocusCircleView: FocusCircleView
    private lateinit var mFadeHandler: Handler
    private lateinit var mCameraImpl: MyCameraImpl

    private var mPreview: MyPreview? = null
    private var mPreviewUri: Uri? = null
    private var mIsInPhotoMode = false
    private var mIsCameraAvailable = false
    private var mIsVideoCaptureIntent = false
    private var mIsHardwareShutterHandled = false
    private var mCurrVideoRecTimer = 0
    var mLastHandledOrientation = 0
    private var gridline_state = true
    private var docker_color_state = true
    private var filterOn = false
    private var currentFilter = false
    private var filterIn = false
    private var selfieFlashOn = false


    private val TAG = "MyActivity"
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.simplemobiletools.camera.activities"
    private val description = "Test notification"
    private var cameraEffect = ""
    private var stickerIn = false
    private var smileyFaceToggle = false
    private var dayStampToggle = false
    private var hidingIconToggle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        useDynamicTheme = false
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        appLaunched(BuildConfig.APPLICATION_ID)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        initVariables()
        tryInitCamera()
        supportActionBar?.hide()
        checkWhatsNewDialog()
        setupOrientationEventListener()
        val myPreference = MyPreference(this)
        val dockerColor = myPreference.getDockerColor()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        btn_holder?.setBackgroundColor(dockerColor)
        color_seek_bar?.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (hasStorageAndCameraPermissions()) {
            mPreview?.onResumed()
            resumeCameraItems()
            setupPreviewImage(mIsInPhotoMode)
            scheduleFadeOut()
            mFocusCircleView.setStrokeColor(getAdjustedPrimaryColor())

            if (mIsVideoCaptureIntent && mIsInPhotoMode) {
                handleTogglePhotoVideo()
                checkButtons()
            }
            toggleBottomButtons(false)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (hasStorageAndCameraPermissions()) {
            mOrientationEventListener.enable()
        }
        val myPreference = MyPreference(this)
        val dockerColor = myPreference.getDockerColor()
        btn_holder?.setBackgroundColor(dockerColor)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (!hasStorageAndCameraPermissions() || isAskingPermissions) {
            return
        }

        mFadeHandler.removeCallbacksAndMessages(null)

        hideTimer()
        mOrientationEventListener.disable()

        if (mPreview?.getCameraState() == STATE_PICTURE_TAKEN) {
            toast(R.string.photo_not_saved)
        }
        mPreview?.onPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPreview = null
    }

    private fun initVariables() {
        mIsInPhotoMode = config.initPhotoMode
        mIsCameraAvailable = false
        mIsVideoCaptureIntent = false
        mIsHardwareShutterHandled = false
        mCurrVideoRecTimer = 0
        mLastHandledOrientation = 0
        mCameraImpl = MyCameraImpl(applicationContext)

        if (config.alwaysOpenBackCamera) {
            config.lastUsedCamera = mCameraImpl.getBackCameraId().toString()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_CAMERA && !mIsHardwareShutterHandled) {
            mIsHardwareShutterHandled = true
            shutterPressed()
            true
        } else if (!mIsHardwareShutterHandled && config.volumeButtonsAsShutter && (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            mIsHardwareShutterHandled = true
            shutterPressed()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mIsHardwareShutterHandled = false
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun hideIntentButtons() {
        toggle_photo_video.beGone()
        settings.beGone()
        last_photo_video_preview.beGone()
    }

    private fun tryInitCamera() {
        handlePermission(PERMISSION_CAMERA) {
            if (it) {
                handlePermission(PERMISSION_WRITE_STORAGE) {
                    if (it) {
                        initializeCamera()
                    } else {
                        toast(R.string.no_storage_permissions)
                        finish()
                    }
                }
            } else {
                toast(R.string.no_camera_permissions)
                finish()
            }
        }
    }

    private fun isImageCaptureIntent() = intent?.action == MediaStore.ACTION_IMAGE_CAPTURE || intent?.action == MediaStore.ACTION_IMAGE_CAPTURE_SECURE

    private fun checkImageCaptureIntent() {
        if (isImageCaptureIntent()) {
            hideIntentButtons()
            val output = intent.extras?.get(MediaStore.EXTRA_OUTPUT)
            if (output != null && output is Uri) {
                mPreview?.setTargetUri(output)
            }
        }
    }

    private fun checkVideoCaptureIntent() {
        if (intent?.action == MediaStore.ACTION_VIDEO_CAPTURE) {
            mIsVideoCaptureIntent = true
            mIsInPhotoMode = false
            hideIntentButtons()
            shutter.setImageResource(R.drawable.ic_video_rec)
        }
    }

    private fun initializeCamera() {
        setContentView(R.layout.activity_main)
        initButtons()

        (btn_holder.layoutParams as RelativeLayout.LayoutParams).setMargins(0, 0, 0, (navBarHeight + resources.getDimension(R.dimen.activity_margin)).toInt())

        checkVideoCaptureIntent()
        mPreview = CameraPreview(this, camera_texture_view, mIsInPhotoMode)
        view_holder.addView(mPreview as ViewGroup)
        checkImageCaptureIntent()
        mPreview?.setIsImageCaptureIntent(isImageCaptureIntent())

        val imageDrawable = if (config.lastUsedCamera == mCameraImpl.getBackCameraId().toString()) R.drawable.ic_camera_front else R.drawable.ic_camera_rear
        toggle_camera.setImageResource(imageDrawable)

        mFocusCircleView = FocusCircleView(applicationContext)
        view_holder.addView(mFocusCircleView)

        mTimerHandler = Handler()
        mFadeHandler = Handler()
        setupPreviewImage(true)

        val initialFlashlightState = if (config.turnFlashOffAtStartup) FLASH_OFF else config.flashlightState
        mPreview!!.setFlashlightState(initialFlashlightState)
        updateFlashlightState(initialFlashlightState)
    }

    private fun initButtons() {
        toggle_camera.setOnClickListener { toggleCamera() }
        last_photo_video_preview.setOnClickListener { showLastMediaPreview() }
        toggle_flash.setOnClickListener { toggleFlash() }
        shutter.setOnClickListener { shutterPressed() }
        settings.setOnClickListener { launchSettings() }
        toggle_photo_video.setOnClickListener { handleTogglePhotoVideo() }
        change_resolution.setOnClickListener { mPreview?.showChangeResolutionDialog() }
        gridlines_icon.setOnClickListener { toggleGridlines() }
        filter.setOnClickListener{ fadeInFilters() }
        gridlines_icon.tag = R.drawable.gridlines_white
        filter_icon.setOnClickListener{ enableFilter() }
        bw.setOnClickListener{ enable_BW_Filter() }
        solar.setOnClickListener{ enable_solarize_Filter() }
        no_filter.setOnClickListener{ disableFilter() }
        invert.setOnClickListener{ enable_invert_filter() }
        sticker.setOnClickListener{ fadeInStickers() }
        smiley.setOnClickListener{ enableSmiley() }
        clockStamp.setOnClickListener{ enableDayStamp() }
        no_sticker.setOnClickListener{ removeSticker() }
        seekbar_switch.setOnClickListener{ enableColorSeekBar() }
        sunday.setOnClickListener{ hideAll() }
        monday.setOnClickListener{ hideAll() }
        tuesday.setOnClickListener{ hideAll() }
        wednesday.setOnClickListener{ hideAll() }
        thursday.setOnClickListener{ hideAll() }
        friday.setOnClickListener{ hideAll() }
        saturday.setOnClickListener{ hideAll() }
        smileyFace.setOnClickListener{ hideAll() }

        color_seek_bar.setOnColorChangeListener(object: ColorSeekBar.OnColorChangeListener{
            override fun onColorChangeListener(color: Int) {
                btn_holder.setBackgroundColor(color)
                savePreference(color)
            }
        })
    }

    private fun savePreference(color: Int) {
        val myPreference = MyPreference(this)
        myPreference.setDockerColor(color)
    }

    private fun hideAll(){
        if(!hidingIconToggle){
            hidingIconToggle = true
            makeDisappearAllIcons()
        }
        else{
            hidingIconToggle = false
            makeAppearAllIcons()
        }
    }
    private fun makeDisappearAllIcons(){
        Log.i(TAG, "******************************************Disable ALL ICONS")
        gridlines_icon.setVisibility(View.GONE)
        seekbar_switch.setVisibility(View.GONE)
        sticker.setVisibility(View.GONE)
        settings.setVisibility(View.GONE)
        toggle_photo_video.setVisibility(View.GONE)
        change_resolution.setVisibility(View.GONE)
        filter.setVisibility(View.GONE)
        toggle_camera.setVisibility(View.GONE)
        shutter.setVisibility(View.GONE)
        toggle_flash.setVisibility(View.GONE)
        smiley.setVisibility(View.GONE)
        clockStamp.setVisibility(View.GONE)
        no_sticker.setVisibility(View.GONE)
        solar.setVisibility(View.GONE)
        bw.setVisibility(View.GONE)
        invert.setVisibility(View.GONE)
        no_filter.setVisibility(View.GONE)
    }

    private fun makeAppearAllIcons(){
        Log.i(TAG, "******************************************Enable ALL ICONS")
        gridlines_icon.setVisibility(View.VISIBLE)
        seekbar_switch.setVisibility(View.VISIBLE)
        sticker.setVisibility(View.VISIBLE)
        settings.setVisibility(View.VISIBLE)
        toggle_photo_video.setVisibility(View.VISIBLE)
        change_resolution.setVisibility(View.VISIBLE)
        filter.setVisibility(View.VISIBLE)
        toggle_camera.setVisibility(View.VISIBLE)
        shutter.setVisibility(View.VISIBLE)
        toggle_flash.setVisibility(View.VISIBLE)
    }

    private fun enableFilter() {
        // tap icon to disable filter
        if(filterOn) {
            cameraEffect = ""
            filter_icon.setImageResource(R.drawable.ic_star_off)
            filterOn = false
        }
        // tap icon to enable filter
        else {
            cameraEffect = "black_and_white"
            filter_icon.setImageResource(R.drawable.ic_star_on)
            filterOn = true
        }
        mPreview?.setCameraEffect(cameraEffect)
    }

    private fun disableFilter() {
        // tap icon to disable filter
            cameraEffect = ""
            filterOn = false
        mPreview?.setCameraEffect(cameraEffect)
    }

    private fun enable_solarize_Filter() {
        // tap icon to enable filter
        if(!cameraEffect.equals("solarize")) {
            cameraEffect = "solarize"
            currentFilter = true
        }
        mPreview?.setCameraEffect(cameraEffect)
    }

    private fun enable_BW_Filter() {
        // tap icon to disable filter
        if(!cameraEffect.equals("black_and_white")) {
            cameraEffect = "black_and_white"
            currentFilter = true
        }
        mPreview?.setCameraEffect(cameraEffect)
    }

    open fun enable_invert_filter(){
        // tap icon to enable filter
        if(!cameraEffect.equals("invert")){
            cameraEffect = "invert"
            currentFilter = true
        }
        mPreview?.setCameraEffect(cameraEffect)
    }

    private fun enableColorSeekBar(){
        // on toggle, color-seekbar becomes visible
        if(docker_color_state){
            color_seek_bar?.visibility = View.VISIBLE
            docker_color_state = false
        }
        // on toggle, color-seekbar becomes invisible
        else{
            color_seek_bar?.visibility = View.INVISIBLE
            docker_color_state = true
        }
    }

    private fun fadeOutStickers() {
        fadeAnim(sticker, .5f)
        fadeAnim(smiley, .0f)
        fadeAnim(clockStamp, .0f)
        fadeAnim(no_sticker, .0f)
    }
    private fun fadeInStickers() {
        smiley.setVisibility(View.VISIBLE);
        clockStamp.setVisibility(View.VISIBLE);
        no_sticker.setVisibility(View.VISIBLE);
        if(!stickerIn){
            fadeAnim(sticker, 1f)
            fadeAnim(smiley, 1f)
            fadeAnim(clockStamp, 1f)
            fadeAnim(no_sticker, 1f)
            stickerIn = true

        }else{
            stickerIn = false
            fadeOutStickers()
        }

    }
    private fun enableDayStamp() {
        Log.i(TAG, "******************************************Enable DayStamp")
        disableSmiley()
        val c = Calendar.getInstance()
        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        Log.i(TAG, "******************************************DAY value : "+ dayOfWeek)
        when(dayOfWeek) {
            1 -> Log.i(TAG, "******************************************DAY STAMP SUNDAY")
            2 -> Log.i(TAG, "******************************************DAY STAMP MONDAY ")
            3 -> Log.i(TAG, "******************************************DAY STAMP TUESDAY")
            4 -> Log.i(TAG, "******************************************DAY STAMP WEDNESDAY")
            5 -> Log.i(TAG, "******************************************DAY STAMP THURSDAY")
            6 -> Log.i(TAG, "******************************************DAY STAMP FRIDAY")
            7 -> Log.i(TAG, "******************************************DAY STAMP SATURDAY")
        }
        if(!dayStampToggle){
            dayStampToggle = true

            when(dayOfWeek) {
                1 -> sunday.setVisibility(View.VISIBLE)
                2 -> monday.setVisibility(View.VISIBLE)
                3 -> tuesday.setVisibility(View.VISIBLE)
                4 -> wednesday.setVisibility(View.VISIBLE)
                5 -> thursday.setVisibility(View.VISIBLE)
                6 -> friday.setVisibility(View.VISIBLE)
                7 -> saturday.setVisibility(View.VISIBLE)
            }

            Log.i(TAG, "******************************************Enable DayStamp")
        }
        else{
            disableDayStamp()
            Log.i(TAG, "******************************************Disable DayStamp")
        }

    }

    private fun disableDayStamp() {
        dayStampToggle = false
        saturday.setVisibility(View.GONE)
        monday.setVisibility(View.GONE)
        tuesday.setVisibility(View.GONE)
        wednesday.setVisibility(View.GONE)
        thursday.setVisibility(View.GONE)
        friday.setVisibility(View.GONE)
        sunday.setVisibility(View.GONE)
    }

    private fun disableSmiley(){
        smileyFaceToggle = false
        smileyFace.setVisibility(View.GONE)
    }

    private fun enableSmiley() {
        Log.i(TAG, "******************************************Smiley LISTENER")
        disableDayStamp()
        if(!smileyFaceToggle){
            smileyFaceToggle = true
            smileyFace.setVisibility(View.VISIBLE)
            fadeAnim(smileyFace, 1f)
            Log.i(TAG, "******************************************Enable Smiley")
        }
        else{
            smileyFaceToggle = false
            smileyFace.setVisibility(View.GONE)
            fadeAnim(smileyFace, 1f)
            Log.i(TAG, "******************************************Disable Smiley")
        }
    }


    private fun removeSticker() {
        Log.i(TAG, "******************************************Remove Stickers")
        disableDayStamp()
        disableSmiley()
    }
    private fun toggleGridlines(){
        // on toggle, gridlines are inserted to foreground and toggle icon color becomes black
        if(gridline_state) {
            gridlines.foreground = getDrawable(R.drawable.gridlines43)
            gridlines.tag = R.drawable.gridlines43
            gridlines_icon.setImageResource(R.drawable.gridlines_black)
            gridlines_icon.tag = R.drawable.gridlines_black
            gridline_state = false
        }
        // on toggle, foreground becomes empty, and toggle icon color reverts back to white
        else{
            gridlines.foreground = null
            gridlines.tag = null
            gridlines_icon.setImageResource(R.drawable.gridlines_white)
            gridlines_icon.tag = R.drawable.gridlines_white
            gridline_state = true
        }
    }

    private fun toggleCamera() {
        if (checkCameraAvailable()) {
            mPreview!!.toggleFrontBackCamera()
        }
    }

    private fun showLastMediaPreview() {
        if (mPreviewUri != null) {
            val path = applicationContext.getRealPathFromURI(mPreviewUri!!) ?: mPreviewUri!!.toString()
            openPathIntent(path, false, BuildConfig.APPLICATION_ID)
        }
    }

    private fun toggleFlash() {
        if (checkCameraAvailable() && mPreview?.isUsingFrontCamera()== false ) {
            mPreview?.toggleFlashlight()
        } else if(mPreview?.isUsingFrontCamera()==true && selfieFlashOn == true){
            toggle_flash.setImageResource(R.drawable.ic_flash_off)
            selfieFlashOn = false
        } else if(mPreview?.isUsingFrontCamera()==true && selfieFlashOn == false){
            toggle_flash.setImageResource(R.drawable.ic_flash_on)
            selfieFlashOn = true

        }
    }

    fun updateFlashlightState(state: Int) {
        config.flashlightState = state
        val flashDrawable = when (state) {
            FLASH_OFF -> R.drawable.ic_flash_off
            FLASH_ON -> R.drawable.ic_flash_on
            else -> R.drawable.ic_flash_auto
        }
        toggle_flash.setImageResource(flashDrawable)
    }

    fun updateCameraIcon(isUsingFrontCamera: Boolean) {
        toggle_camera.setImageResource(if (isUsingFrontCamera) R.drawable.ic_camera_rear else R.drawable.ic_camera_front)
    }

    private fun shutterPressed() {
        if (checkCameraAvailable()) {
            if(smileyFaceToggle || dayStampToggle){
                Log.i(TAG, "****************************************** CAPTURING WITH STICKER")
                // makeDisappearAllIcons()

                //get metrics of each system
                val displayMetrics = DisplayMetrics()
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels

                Log.i(TAG, "****************************************** CAPTURING id = "+height +" " + width)
                val bitmap = loadBitmapFromView(findViewById(R.id.view_holder), width, height)
                Log.i(TAG, "****************************************** CAPTURING id = "+R.id.view_holder + " "+findViewById(R.id.view_holder) )
                saveImage(bitmap)

                ///makeAppearAllIcons()
            }
            else{

                Log.i(TAG, "****************************************** HANDLE SHUTTER")
                handleShutter()
            }
        }
    }

    private fun handleShutter() {
        if (mIsInPhotoMode) {
            toggleBottomButtons(true)
            mPreview?.tryTakePicture()
            if( mPreview?.isUsingFrontCamera() == true && selfieFlashOn == true){
                selfieFlash(selfie_flash, mFadeHandler)
            }
            shutterNotification()
        } else {
            mPreview?.toggleRecording()
            shutterNotification()
        }
    }


    fun toggleBottomButtons(hide: Boolean) {
        runOnUiThread {
            val alpha = if (hide) 0f else 1f
            shutter.animate().alpha(alpha).start()
            toggle_camera.animate().alpha(alpha).start()
            toggle_flash.animate().alpha(alpha).start()

            shutter.isClickable = !hide
            toggle_camera.isClickable = !hide
            toggle_flash.isClickable = !hide
        }
    }

    private fun launchSettings() {
        if (settings.alpha == 1f) {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
        } else {
            fadeInButtons()
        }
    }

    private fun handleTogglePhotoVideo() {
        handlePermission(PERMISSION_RECORD_AUDIO) {
            if (it) {
                togglePhotoVideo()
            } else {
                toast(R.string.no_audio_permissions)
                if (mIsVideoCaptureIntent) {
                    finish()
                }
            }
        }
    }

    private fun togglePhotoVideo() {
        if (!checkCameraAvailable()) {
            return
        }

        if (mIsVideoCaptureIntent) {
            mPreview?.tryInitVideoMode()
        }

        mPreview?.setFlashlightState(FLASH_OFF)
        hideTimer()
        mIsInPhotoMode = !mIsInPhotoMode
        config.initPhotoMode = mIsInPhotoMode
        showToggleCameraIfNeeded()
        checkButtons()
        toggleBottomButtons(false)
    }

    private fun checkButtons() {
        if (mIsInPhotoMode) {
            initPhotoMode()
        } else {
            tryInitVideoMode()
        }
    }

    private fun initPhotoMode() {
        toggle_photo_video.setImageResource(R.drawable.ic_video)
        shutter.setImageResource(R.drawable.ic_shutter)
        mPreview?.initPhotoMode()
        setupPreviewImage(true)
    }

    private fun tryInitVideoMode() {
        if (mPreview?.initVideoMode() == true) {
            initVideoButtons()
        } else {
            if (!mIsVideoCaptureIntent) {
                toast(R.string.video_mode_error)
            }
        }
    }

    private fun initVideoButtons() {
        toggle_photo_video.setImageResource(R.drawable.ic_camera)
        showToggleCameraIfNeeded()
        shutter.setImageResource(R.drawable.ic_video_rec)
        setupPreviewImage(false)
        mPreview?.checkFlashlight()
    }

    private fun setupPreviewImage(isPhoto: Boolean) {
        val uri = if (isPhoto) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val lastMediaId = getLatestMediaId(uri)
        if (lastMediaId == 0L) {
            return
        }

        mPreviewUri = Uri.withAppendedPath(uri, lastMediaId.toString())

        runOnUiThread {
            if (!isDestroyed) {
                val options = RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)

                Glide.with(this)
                        .load(mPreviewUri)
                        .apply(options)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(last_photo_video_preview)
            }
        }
    }

    private fun scheduleFadeOut() {
        if (!config.keepSettingsVisible) {
            mFadeHandler.postDelayed({
                fadeOutButtons()
            }, FADE_DELAY)
        }
    }

    private fun fadeOutButtons() {
        fadeAnim(settings, .5f)
        fadeAnim(toggle_photo_video, .0f)
        fadeAnim(change_resolution, .0f)
        fadeAnim(last_photo_video_preview, .0f)
    }

    private fun fadeInButtons() {
        fadeAnim(settings, 1f)
        fadeAnim(toggle_photo_video, 1f)
        fadeAnim(change_resolution, 1f)
        fadeAnim(last_photo_video_preview, 1f)
        scheduleFadeOut()
    }

    private fun fadeOutFilters() {
        fadeAnim(filter, .5f)
        fadeAnim(solar, .0f)
        fadeAnim(bw, .0f)
        fadeAnim(no_filter, .0f)
        fadeAnim(invert, .0f)
    }

    private fun fadeInFilters() {
        bw.setVisibility(View.VISIBLE)
        solar.setVisibility(View.VISIBLE)
        no_filter.setVisibility(View.VISIBLE)
        invert.setVisibility(View.VISIBLE)
        if(!filterIn){
            fadeAnim(filter, 1f)
            fadeAnim(solar, 1f)
            fadeAnim(bw, 1f)
            fadeAnim(no_filter, 1f)
            fadeAnim(invert, 1f)
            filterIn = true

        }else{
            filterIn = false
            fadeOutFilters()
        }

    }

    private fun FiltersScheduleFadeOut() {
        if (!config.keepSettingsVisible) {
            mFadeHandler.postDelayed({
                fadeOutFilters()
            }, FADE_DELAY)
        }
    }
    private fun fadeAnim(view: View, value: Float) {
        view.animate().alpha(value).start()
        view.isClickable = value != .0f
    }

    private fun hideNavigationBarIcons() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
    }

    private fun showTimer() {
        video_rec_curr_timer.beVisible()
        setupTimer()
    }

    private fun hideTimer() {
        video_rec_curr_timer.text = 0.getFormattedDuration()
        video_rec_curr_timer.beGone()
        mCurrVideoRecTimer = 0
        mTimerHandler.removeCallbacksAndMessages(null)
    }

    private fun setupTimer() {
        runOnUiThread(object : Runnable {
            override fun run() {
                video_rec_curr_timer.text = mCurrVideoRecTimer++.getFormattedDuration()
                mTimerHandler.postDelayed(this, 1000L)
            }
        })
    }

    private fun resumeCameraItems() {
        showToggleCameraIfNeeded()
        hideNavigationBarIcons()

        if (!mIsInPhotoMode) {
            initVideoButtons()
        }
    }

    private fun showToggleCameraIfNeeded() {
        toggle_camera?.beInvisibleIf(mCameraImpl.getCountOfCameras() <= 1)
    }

    private fun hasStorageAndCameraPermissions() = hasPermission(PERMISSION_WRITE_STORAGE) && hasPermission(PERMISSION_CAMERA)

    private fun setupOrientationEventListener() {
        mOrientationEventListener = object : OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            override fun onOrientationChanged(orientation: Int) {
                if (isDestroyed) {
                    mOrientationEventListener.disable()
                    return
                }

                val currOrient = when (orientation) {
                    in 75..134 -> ORIENT_LANDSCAPE_RIGHT
                    in 225..289 -> ORIENT_LANDSCAPE_LEFT
                    else -> ORIENT_PORTRAIT
                }

                if (currOrient != mLastHandledOrientation) {
                    val degrees = when (currOrient) {
                        ORIENT_LANDSCAPE_LEFT -> 90
                        ORIENT_LANDSCAPE_RIGHT -> -90
                        else -> 0
                    }

                    animateViews(degrees)
                    mLastHandledOrientation = currOrient
                }
            }
        }
    }

    private fun animateViews(degrees: Int) {
        val views = arrayOf<View>(toggle_camera, toggle_flash, toggle_photo_video, change_resolution, shutter, settings, last_photo_video_preview)
        for (view in views) {
            rotate(view, degrees)
        }
    }

    private fun rotate(view: View, degrees: Int) = view.animate().rotation(degrees.toFloat()).start()

    private fun checkCameraAvailable(): Boolean {
        if (!mIsCameraAvailable) {
            toast(R.string.camera_unavailable)
        }
        return mIsCameraAvailable
    }

    fun displaySelfieFlash(preview: MyPreview, toggle: ImageView){

        if( preview.isUsingFrontCamera() == true ) {

            toggle.beVisible()

            if(selfieFlashOn == false)
                toggle.setImageResource(R.drawable.ic_flash_off)
            else
                toggle.setImageResource(R.drawable.ic_flash_on)
        }
    }


    fun setFlashAvailable(available: Boolean) {
        if (available) {
            toggle_flash.beVisible()
        } else {
            toggle_flash.beInvisible()
            toggle_flash.setImageResource(R.drawable.ic_flash_off)
            mPreview?.setFlashlightState(FLASH_OFF)
        }
        if (mPreview?.isUsingFrontCamera() == true){
            displaySelfieFlash(mPreview!!, toggle_flash)
        }
    }

    fun setIsCameraAvailable(available: Boolean) {
        mIsCameraAvailable = available
    }

    fun setRecordingState(isRecording: Boolean) {
        runOnUiThread {
            if (isRecording) {
                shutter.setImageResource(R.drawable.ic_video_stop)
                toggle_camera.beInvisible()
                showTimer()
            } else {
                shutter.setImageResource(R.drawable.ic_video_rec)
                showToggleCameraIfNeeded()
                hideTimer()
            }
        }
    }

    fun videoSaved(uri: Uri) {
        setupPreviewImage(false)
        if (mIsVideoCaptureIntent) {
            Intent().apply {
                data = uri
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                setResult(Activity.RESULT_OK, this)
            }
            finish()
        }
    }

    fun drawFocusCircle(x: Float, y: Float) = mFocusCircleView.drawFocusCircle(x, y)

    override fun mediaSaved(path: String) {
        rescanPaths(arrayListOf(path)) {
            setupPreviewImage(true)
            Intent(BROADCAST_REFRESH_MEDIA).apply {
                putExtra(REFRESH_PATH, path)
                `package` = "com.simplemobiletools.gallery"
                sendBroadcast(this)
            }
        }

        if (isImageCaptureIntent()) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun checkWhatsNewDialog() {
        arrayListOf<Release>().apply {
            add(Release(33, R.string.release_33))
            add(Release(35, R.string.release_35))
            add(Release(39, R.string.release_39))
            add(Release(44, R.string.release_44))
            add(Release(46, R.string.release_46))
            add(Release(52, R.string.release_52))
            checkWhatsNew(this, BuildConfig.VERSION_CODE)
        }
    }

    fun helloWorld(name: String = "World"): String {
        return "Hello, ${name}!"
    }

    fun setDockerColour(myPreference: MyPreference) {
        val dockerColor = myPreference.getDockerColor()
        btn_holder.setBackgroundColor(dockerColor)
    }

    open fun selfieFlash(selfie: ImageView, fade_handler: Handler) {

        selfie.setVisibility(View.VISIBLE)

        fade_handler.postDelayed(
                { selfie.setVisibility(View.GONE) }
                , 500)

    }
    fun shutterNotification(){

        shutter.setOnClickListener {

            val intent = Intent(this, LauncherActivity::class.java)
            val pendingIntent = PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_UPDATE_CURRENT)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                        .setContentTitle("Picture Taken")
                        .setContentText("Saving...")
                        .setSmallIcon(R.drawable.ic_launcher_round)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher))
                        .setContentIntent(pendingIntent)

                shutterPressed()

            }else{

                builder = Notification.Builder(this)
                        .setContentTitle("Picture Taken")
                        .setContentText("Saving...")
                        .setSmallIcon(R.drawable.ic_launcher_round)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher))
                        .setContentIntent(pendingIntent)

                shutterPressed()

            }

            //generating unique notification numbers, now every photo taken will generate a notification, no need to clear
            //notifications before another one comes in
            var Unique_Integer_Number = ((Date().getTime() / 1000L) % Integer.MAX_VALUE).toInt()

            notificationManager.notify(Unique_Integer_Number,builder.build())

        }
    }

    fun getMPreview(): MyPreview? {
        return mPreview
    }

    fun getCameraEffect(): String {
        return cameraEffect
    }

    fun getCurrentFilter(): Boolean{
        return currentFilter
    }

    fun getSelfieFlashOn(): Boolean{
        return selfieFlashOn
    }

    companion object {

        fun saveImage(bitmap: Bitmap) {
            val root = Environment.getExternalStorageDirectory().toString()

            Log.i(TAG, "******************************************SAVING root = "+ root)
            val myDir = File(root + "/req_images")
            Log.i(TAG, "******************************************SAVING mydir= "+ myDir)
            myDir.mkdirs()
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)
            Log.i(TAG, "******************************************SAVING random number= "+ n)
            val fname = "Image-"+n+".jpg"
            Log.i(TAG, "******************************************SAVING file name= "+ fname)
            val file = File(myDir, fname)
            Log.i(TAG, "******************************************SAVING file = "+ file.toString())
            //  Log.i(TAG, "" + file);
            if (file.exists())
                file.delete()
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                Log.i(TAG, "******************************************SAVING CLOSE STREAM")
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            Log.i(TAG, "******************************************LOADING ")
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.layoutParams.width, v.layoutParams.height)
            v.draw(c)
            return b
        }
    }
}
