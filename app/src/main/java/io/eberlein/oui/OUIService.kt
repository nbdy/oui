package io.eberlein.oui

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OUIService<S: OUIStorage>(private val storage: S): Service() {
    private lateinit var oui: OUI

    class EventOUI(val oui: OUI)
    class EventRequestOUI()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventRequestOUI(e: EventRequestOUI){
        EventBus.getDefault().post(EventOUI(oui))
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        oui = OUI(storage)
        EventBus.getDefault().post(EventOUI(oui))
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}