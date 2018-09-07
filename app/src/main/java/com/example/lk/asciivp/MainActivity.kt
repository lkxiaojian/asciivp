package com.example.lk.asciivp
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.lk.asciivp.utils.newIntent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setonclick()
    }

    private fun setonclick() {
        //图片
        bt_image.setOnClickListener {

            newIntent<ImageActivity>()


        }
        //视频
        bt_video.setOnClickListener {
            newIntent<VideoActivity>()
        }
    }
}
