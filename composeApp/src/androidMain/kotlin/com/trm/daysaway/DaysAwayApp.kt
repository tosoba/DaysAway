package com.trm.daysaway

import android.app.Application
import com.trm.daysaway.widget.DeviceWidgetManager

class DaysAwayApp : Application() {
  val widgetManager by lazy { DeviceWidgetManager(this) }
}
