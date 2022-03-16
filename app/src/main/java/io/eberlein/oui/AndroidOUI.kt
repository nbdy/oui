package io.eberlein.oui

import android.content.Context

class AndroidOUI (
    context: Context, downloadIfNeeded: Boolean = true,
    url: String = "http://standards-oui.ieee.org/oui/oui.csv"
): OUI(GsonStorage(context.filesDir.absolutePath + "/oui.json"), downloadIfNeeded, url)
