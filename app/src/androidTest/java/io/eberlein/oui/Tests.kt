package io.eberlein.oui

import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Test

class Tests {
    companion object {
        @Test
        fun downloadWorks(){
            assertNotNull(OUI.download())
        }

        @Test
        fun lookupWorks(oui: OUI){
            assertNotNull(oui.lookup("A4:45:19:DE:AD:ME")) // should return xiaomi
            Assert.assertNull(oui.lookup("66:66:66:66:66:66")) // should not exist
        }

        @Test
        fun lookupByOrgNameWorks(oui: OUI){
            assertNotNull(oui.lookupByOrgName("Nokia"))
            assertNotNull(oui.lookupByOrgName("amazon"))
            Assert.assertNull(oui.lookupByOrgName("disnotarealcompanybutthisstringneedstobelongersoijustkeeptyping"))
        }

        @Test
        fun lookupByOrgAddressWorks(oui: OUI){

        }

        fun runAllTests(oui: OUI){
            downloadWorks()
            lookupWorks(oui)
            lookupByOrgNameWorks(oui)
            lookupByOrgAddressWorks(oui)
        }
    }
}