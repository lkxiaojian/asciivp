package com.example.lk.asciivp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.lk.asciivp.utils.CommonUtil.TIME_STYLE

import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import java.io.ByteArrayOutputStream

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Created by Solang on 2018/8/21.
 */

object CommonUtil {

    private val SD_PATH = Environment.getExternalStorageDirectory().path + "/meiniepan/"
    private val IN_PATH = Environment.getExternalStorageDirectory().path + "/meiniepan/"

    /**
     * 存放拍摄图片的文件夹
     */
    private val FILES_NAME = "/MyPhoto"
    /**
     * 获取的时间格式
     */
    val TIME_STYLE = "yyyyMMddHHmmss"
    /**
     * 图片种类
     */
    val IMAGE_TYPE = ".png"

    /**
     * @param context
     * @param requestCode
     * @param type        全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
     */
    fun choosePhoto(context: Activity, requestCode: Int, type: Int) {
        PictureSelector.create(context)
                .openGallery(type)
                //                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                //                .minSelectNum()// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                //                .previewImage()// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                //                .enablePreviewAudio() // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                //                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(false)// 是否压缩 true or false
                //                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //                .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                //                .isGif()// 是否显示gif图片 true or false
                .compressSavePath(context.filesDir.absolutePath)//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                //                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                //                .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(50)// 小于100kb的图片不压缩
                //                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                //                .rotateEnabled() // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                //                .videoQuality()// 视频录制质量 0 or 1 int
                //                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //                .recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(requestCode)//结果回调onActivityResult code
    }

    private fun generateFileName(): String {
        return UUID.randomUUID().toString()
    }

    fun saveBitmap2file(bmp: Bitmap, desFileName: String, context: Context): String? {
        val savePath: String
        val filePic: File
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            savePath = SD_PATH
        } else {
            savePath = IN_PATH
        }
        try {
            filePic = File(savePath + generateFileName() + ".JPEG")
            if (!filePic.exists()) {
                filePic.parentFile.mkdirs()
                filePic.createNewFile()
            }
            val fos = FileOutputStream(filePic)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            Toast.makeText(context, "保存成功,位置:" + filePic.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return null
        }

        return filePic.absolutePath
    }

    fun getFileDir(context: Context, desFileName: String): File {
        try {
            val dir = File(Environment.getExternalStorageDirectory().toString() + "/carefree/")
            if (!dir.exists()) {
                dir.createNewFile()
            }
            return dir
        } catch (e: Exception) {
            e.printStackTrace()
            return File(context.filesDir.toString() + desFileName)
        }

    }

    fun createAsciiPic(path: String, context: Context): Bitmap? {
        val base = "#8XOHLTI)i=+;:,."// 字符串由复杂到简单
        //        final String base = "#,.0123456789:;@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";// 字符串由复杂到简单
        val text = StringBuilder()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        var image = BitmapFactory.decodeFile(path)  //读取图片
        val width0 = image.width
        val height0 = image.height
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
        image = scale(path, width1, height1)  //读取图片
        //输出到指定文件中
        var y = 0
        while (y < image.height) {
            for (x in 0 until image.width) {
                val pixel = image.getPixel(x, y)
                val r = pixel and 0xff0000 shr 16
                val g = pixel and 0xff00 shr 8
                val b = pixel and 0xff
                val gray = 0.299f * r + 0.578f * g + 0.114f * b
                val index = Math.round(gray * (base.length + 1) / 255)
                val s = if (index >= base.length) " " else base[index].toString()
                text.append(s)
            }
            text.append("\n")
            y += 2
        }
        return textAsBitmap(text, context)
        //        return image;
    }


    fun createAsciiBitmap(image: Bitmap, context: Context): Bitmap? {
        val base = "#8XOHLTI)i=+;:,."// 字符串由复杂到简单
        //        final String base = "#,.0123456789:;@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";// 字符串由复杂到简单
        val text = StringBuilder()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        //        Bitmap image = BitmapFactory.decodeFile(path);  //读取图片
        val width0 = image.width
        val height0 = image.height
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
        //        image = scale(path, width1, height1);  //读取图片
        //输出到指定文件中
        var y = 0
        while (y < image.height) {
            for (x in 0 until image.width) {
                val pixel = image.getPixel(x, y)
                val r = pixel and 0xff0000 shr 16
                val g = pixel and 0xff00 shr 8
                val b = pixel and 0xff
                val gray = 0.299f * r + 0.578f * g + 0.114f * b
                val index = Math.round(gray * (base.length + 1) / 255)
                val s = if (index >= base.length) " " else base[index].toString()
                text.append(s)
            }
            text.append("\n")
            y += 2
        }
        return textAsBitmap(text, context)
        //        return image;
    }


    fun creatCodeBitmap(contents: StringBuilder, context: Context): Bitmap {
        val scale = context.resources.displayMetrics.scaledDensity

        val tv = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        tv.layoutParams = layoutParams
        tv.text = contents
        tv.textSize = scale * 2
        tv.typeface = Typeface.MONOSPACE
        tv.gravity = Gravity.CENTER_HORIZONTAL
        tv.isDrawingCacheEnabled = true
        tv.setTextColor(Color.GRAY)
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        tv.layout(0, 0, tv.measuredWidth, tv.measuredHeight)


        tv.setBackgroundColor(Color.WHITE)

        tv.buildDrawingCache()
        return tv.drawingCache
    }

    fun textAsBitmap(text: StringBuilder, context: Context): Bitmap? {
        val textPaint = TextPaint()
        textPaint.color = Color.BLACK

        textPaint.isAntiAlias = true
        textPaint.typeface = Typeface.MONOSPACE

        textPaint.textSize = 12f
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels         //

        val layout = StaticLayout(text, textPaint, width,

                Layout.Alignment.ALIGN_CENTER, 1f, 0.0f, true)

        val bitmap = Bitmap.createBitmap(layout.width + 20,

                layout.height + 20, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        canvas.translate(10f, 10f)

        canvas.drawColor(Color.WHITE)
        //        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        layout.draw(canvas)
        return bitmap

    }

    fun scale(src: String, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(src), newWidth, newHeight, true)
    }


    /**
     * 获取手机可存储路径
     *
     * @param context 上下文
     * @return 手机可存储路径
     */
    private fun getPhoneRootPath(context: Context): String {
        // 是否有SD卡
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment.isExternalStorageRemovable()) {
            // 获取SD卡根目录
            context.externalCacheDir!!.path
        } else {
            // 获取apk包下的缓存路径
            context.cacheDir.path
        }
    }

    /**
     * 使用当前系统时间作为上传图片的名称
     *
     * @return 存储的根路径+图片名称
     */
    fun getPhotoFileName(context: Context): String {
        val file = File(getPhoneRootPath(context) + FILES_NAME)
        // 判断文件是否已经存在，不存在则创建
        if (!file.exists()) {
            file.mkdirs()
        }
        // 设置图片文件名称
        val format = SimpleDateFormat(TIME_STYLE, Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        val time = format.format(date)
        val photoName = "/$time$IMAGE_TYPE"
        return file.toString() + photoName
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    fun savePhotoToSD(mbitmap: Bitmap?, context: Context): String? {
        var outStream: FileOutputStream? = null
        val fileName = getPhotoFileName(context)
        try {
            outStream = FileOutputStream(fileName)
            // 把数据写入文件，100表示不压缩
            mbitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            return fileName
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close()
                }
                mbitmap?.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 把原图按1/10的比例压缩
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    fun getCompressPhoto(path: String): Bitmap {
        var options: BitmapFactory.Options? = BitmapFactory.Options()
        options!!.inJustDecodeBounds = false
        options.inSampleSize = 10 // 图片的大小设置为原来的十分之一
        val bmp = BitmapFactory.decodeFile(path, options)
        options = null
        return bmp
    }

    /**
     * 处理旋转后的图片
     *
     * @param originpath 原图路径
     * @param context    上下文
     * @return 返回修复完毕后的图片路径
     */
    fun amendRotatePhoto(originpath: String, context: Context): String? {

        // 取得图片旋转角度
        val angle = readPictureDegree(originpath)

        // 把原图压缩后得到Bitmap对象
        if (angle != 0) {
            val bmp = getCompressPhoto(originpath)
            val bitmap = rotaingImageView(angle, bmp)
            return savePhotoToSD(bitmap, context)
        } else {
            return originpath
        }

    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap {
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: OutOfMemoryError) {
        }

        if (returnBm == null) {
            returnBm = bitmap
        }
        if (bitmap != returnBm) {
            bitmap.recycle()
        }
        return returnBm
    }

    fun sizeCompres(path: String, rqsW: Int, rqsH: Int): Bitmap {
        // 用option设置返回的bitmap对象的一些属性参数
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true// 设置仅读取Bitmap的宽高而不读取内容
        BitmapFactory.decodeFile(path, options)// 获取到图片的宽高，放在option里边
        val height = options.outHeight//图片的高度放在option里的outHeight属性中
        val width = options.outWidth
        var inSampleSize = 1
        if (rqsW == 0 || rqsH == 0) {
            options.inSampleSize = 1
        } else if (height > rqsH || width > rqsW) {
            val heightRatio = Math.round(height.toFloat() / rqsH.toFloat())
            val widthRatio = Math.round(width.toFloat() / rqsW.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            options.inSampleSize = inSampleSize
            options.inJustDecodeBounds = false
        }
        return BitmapFactory.decodeFile(path, options)// 主要通过option里的inSampleSize对原图片进行按比例压缩
    }


    fun bitmapCompres(bitmap: Bitmap): Bitmap {
        var count1 = bitmap.byteCount
        val dm = DisplayMetrics()
        val width = dm.widthPixels
        val height = dm.heightPixels

        val width0 = bitmap.width
        val height0 = bitmap.height
        val width1: Int
        val height1: Int
        val scale = 2
        if (width0 <= width / scale) {
            width1 = width0
            height1 = height0
        } else {
            width1 = width / scale
            height1 = width1 * height0 / width0
        }
        var bm=   Bitmap.createScaledBitmap(bitmap,height1, width1, true)
        var count2 = bm.byteCount
        return bm
    }

}
