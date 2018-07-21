package com.zxz.hencoderplus

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * <pre>
 *     @author : Zhan Xuzhao
 *     time   : 2018/7/21 8:38
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}