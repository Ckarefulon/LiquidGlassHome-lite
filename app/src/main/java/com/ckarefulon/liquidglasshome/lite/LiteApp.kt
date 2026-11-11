package com.ckarefulon.liquidglasshome.lite

import android.app.Application
import com.ckarefulon.liquidglasshome.platform.initAndroidPlatform

/**
 * 进程入口。
 *
 * `:shared:model` 的 Android 版 `keyValueStore()` 要一份进程级 `Context`，拿不到会直接
 * `error("initAndroidPlatform(context) 还没被调用")`。上游在 `XjtuApp` 里做这件事，lite 版
 * 少了这一步，于是首页一读 `SettingsStore()`（`LiteRoot`）就崩——这是本仓库之前「装上打不开」的唯一原因。
 */
class LiteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initAndroidPlatform(this)
    }
}
