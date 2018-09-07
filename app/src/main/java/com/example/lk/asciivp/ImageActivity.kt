package com.example.lk.asciivp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lk.asciivp.utils.CommonUtil
import com.example.lk.asciivp.utils.showToast
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {
    var bitmap: Bitmap? = null
    var filepath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        setonclick()
    }

    private fun setonclick() {
        iv_back.setOnClickListener {
            finish()
        }

    }

    fun doPick(view: View) {
        CommonUtil.choosePhoto(this, PictureConfig.CHOOSE_REQUEST, PictureMimeType.ofImage())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                var path = ""
                if (selectList != null && selectList.size > 0) {
                    val localMedia = selectList[0]
                    if (localMedia.isCompressed) {
                        path = localMedia.compressPath
                    } else if (localMedia.isCut) {
                        path = localMedia.cutPath
                    } else {
                        path = localMedia.path
                    }
                }
                filepath = CommonUtil.amendRotatePhoto(path, this@ImageActivity)
                //                imageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
                bitmap = CommonUtil.createAsciiPic(filepath, this@ImageActivity)
                iv_image.setImageBitmap(bitmap)
            }
        }
    }

    fun doSave(view: View) {
        if (bitmap == null) {
            showToast("Please select photo!")
            return
        }
        CommonUtil.saveBitmap2file(bitmap, System.currentTimeMillis().toString() + "", this@ImageActivity)
    }
}
