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
        startAnimationSet()
    }

    private fun startAnimationSet() {
        rotate_view.degreeZ = 0f
        rotate_view.rightDegreeY = 0f
        rotate_view.leftDegreeY = 0f

        val yAnimatorDuration = 600L
        val zAnimatorDuration = 1500L
        val maxYDegree = 60f
        val animatorRightY = ObjectAnimator.ofFloat(rotate_view, "rightDegreeY", 0f, maxYDegree)
        animatorRightY.duration = yAnimatorDuration
        val animatorZ = ObjectAnimator.ofFloat(rotate_view, "degreeZ", 0f, -270f)
        animatorZ.duration = zAnimatorDuration
        val animatorLeftY = ObjectAnimator.ofFloat(rotate_view, "leftDegreeY", 0f, -maxYDegree)
        animatorLeftY.duration = yAnimatorDuration

        val animatorRightY2 = ObjectAnimator.ofFloat(rotate_view, "rightDegreeY", maxYDegree, 0f)
        animatorRightY2.duration = yAnimatorDuration
        val animatorLeftY2 = ObjectAnimator.ofFloat(rotate_view, "leftDegreeY", -maxYDegree, 0f)
        animatorLeftY2.duration = yAnimatorDuration

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animatorRightY, animatorZ, animatorLeftY, animatorLeftY2, animatorRightY2)
        animatorSet.start()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                startAnimationSet()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }
}