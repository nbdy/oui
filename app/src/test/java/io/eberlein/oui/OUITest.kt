package io.eberlein.oui

import org.junit.Test


class OUITest {
    @Test
    fun runAllTests(){
        Tests.runAllTests(OUI(createTempDir().absolutePath + "/data.db"))
    }
}