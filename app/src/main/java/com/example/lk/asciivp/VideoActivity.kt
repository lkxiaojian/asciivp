package com.example.lk.asciivp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lk.asciivp.utils.CommonUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType

import com.lk.playvideolibrary.NiceVideoPlayerManager
import com.example.lk.asciivp.utils.showToast
import com.lk.playvideolibrary.TxVideoPlayerController
import kotlinx.android.synthetic.main.activity_video.*
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.example.lk.asciivp.R.id.nice_video_player
import com.example.lk.asciivp.utils.SequenceEncoderMp4
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.concurrent.thread


class VideoActivity : AppCompatActivity(), TxVideoPlayerController.OnShareClickListener {
    override fun onShareClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var bitmap: Bitmap? = null
    var filepath: String? = null
    var mar = MediaMetadataRetriever()
    var duration: String? = null
    var bitmapList: MutableList<Bitmap>? = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        setonclick()

    }

    private fun setonclick() {
        iv_back.setOnClickListener { finish() }
        btn_save.setOnClickListener {

            var timeLength = duration?.toInt()?.div(1000)

            val out = File(Environment.getExternalStorageDirectory()
                    .absolutePath + File.separator + "/haha.mp4")
            if (!out.exists()) {
                out.createNewFile()
            }
            var se = SequenceEncoderMp4(out)
//            1000000
            for (time in 1..timeLength!!) {
                var bimap1 = mar.getFrameAtTime((time).toLong())
                var wm = this?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                var dm = DisplayMetrics()
                wm.defaultDisplay.getMetrics(dm)
                var width = dm.widthPixels;
                var height = dm.heightPixels;
                val width0 = bimap1.width
                val height0 = bimap1.height
                val width1: Int
                val height1: Int
                val scale = 7
                if (width0 <= width / scale) {
                    width1 = width0
                    height1 = height0
                } else {
                    width1 = width / scale
                    height1 = width1 * height0 / width0
                }
                var mSrcBitmap = Bitmap.createScaledBitmap(bimap1, width1, height1, true)


                if (mSrcBitmap != null) {
                    bitmap = CommonUtil.createAsciiBitmap(mSrcBitmap, this@VideoActivity)
                    iv_image.setImageBitmap(bitmap)
                    se.encodeImage(Bitmap.createScaledBitmap(bimap1, width1, height1, true))

                }
            }
            se.finish()
            Log.e("performJcodec: ", "执行完成")
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(out)))
            mar.release()


        }
    }

    private fun setVideo() {
        if (filepath == null || "".equals(filepath)) {
            showToast("Please select video")
            return
        }

        mar.setDataSource(filepath)
        duration = mar.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)//时长(毫秒)
        val split = filepath!!.split("/")
        nice_video_player.setUp(filepath, null)
        val controller = TxVideoPlayerController(this)
        controller.setShareClickListener(this)
        controller.setVisible(View.VISIBLE)
        controller.setTitle(split[split.lastIndex])
        controller.setLenght(duration!!.toLong())
        controller.setVisibleLength(false)
        nice_video_player.setController(controller)

    }

    fun doPick(view: View) {
        CommonUtil.choosePhoto(this, PictureConfig.CHOOSE_REQUEST, PictureMimeType.ofVideo())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                if (selectList != null && selectList.size > 0) {
                    val localMedia = selectList[0]
                    filepath = localMedia.path
                    setVideo()
                }


            }
        }
    }

    override fun onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
    }
}
