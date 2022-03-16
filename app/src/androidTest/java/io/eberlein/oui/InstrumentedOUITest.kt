package io.eberlein.oui

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedOUITest {
    @Test
    fun runAllTests() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("io.eberlein.oui", appContext.packageName)
        Tests.runAllTests(AndroidOUI(appContext))
    }
}
