package io.eberlein.oui

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertEquals
import org.junit.Test

class Tests {
    companion object {
        @Test
        fun downloadWorks(){
            assertNotNull(OUI.download())
        }

        @Test
        fun lookupWorks(oui: OUI){
            assertNotNull(oui.lookupByMac("A4:45:19:DE:AD:ME")) // should return xiaomi
            assertNull(oui.lookupByMac("66:66:66:66:66:66")) // should not exist
        }

        @Test
        fun lookupByOrgNameWorks(oui: OUI){
            assertNotNull(oui.lookupByOrgName("Nokia"))
            assertNotNull(oui.lookupByOrgName("amazon"))
            assertEquals(oui.lookupByOrgName("disnotarealcompanybutthisstringneedstobelongersoijustkeeptyping").size, 0)
        }

        @Test
        fun lookupByOrgAddressWorks(oui: OUI){
            assertNotNull(oui.lookupByOrgAddress("3333 Scott Blvd")) // should yield Aruba / 883A30
            assertNotNull(oui.lookupByOrgAddress("9800 SAVAGE ROAD")) // should yield National Security Agency / 002091
            assertEquals(oui.lookupByOrgAddress("42000 Wonderland").size, 0) // does not exist
        }

        @Test
        fun randomMACAddressCorrect(oui: OUI) {
            assertEquals(oui.randomMAC().length, "A4:45:19:DE:AD:ME".length)
            assertEquals(oui.randomMAC(oui.lookupByMac("A4:45:19:DE:AD:ME")!!).length, "A4:45:19:DE:AD:ME".length)
        }

        fun runAllTests(oui: OUI){
            println("oui entry's: ${oui.size()}")
            downloadWorks()
            lookupWorks(oui)
            lookupByOrgNameWorks(oui)
            lookupByOrgAddressWorks(oui)
            randomMACAddressCorrect(oui)
        }
    }
}
