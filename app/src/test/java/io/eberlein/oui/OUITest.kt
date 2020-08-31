package io.eberlein.oui

import org.junit.Test
import java.io.File

class OUITest {
    @Test
    fun runAllTests(){
        Tests.runAllTests(OUI(GsonStorage(File("/tmp/oui.json").absolutePath), debug = true))
    }
}