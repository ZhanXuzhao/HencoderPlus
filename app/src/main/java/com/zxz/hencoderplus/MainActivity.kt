package com.zxz.hencoderplus

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/21 16:00
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rotate_view.startAnimation()
    }
}