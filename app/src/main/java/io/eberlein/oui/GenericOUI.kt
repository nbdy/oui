package io.eberlein.oui

class GenericOUI(
    downloadIfNeeded: Boolean = true,
    url: String = "http://standards-oui.ieee.org/oui/oui.csv"
) : OUI(GsonStorage("/tmp/oui.json"), downloadIfNeeded, url)
