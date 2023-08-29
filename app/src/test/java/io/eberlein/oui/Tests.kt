package io.eberlein.oui

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

class Tests : FunSpec({
    val oui = GenericOUI()

    test("Download") {
        OUI.download().shouldNotBeNull()
    }

    test("LookupByMAC") {
        oui.lookupByMac("A4:45:19:DE:AD:ME").shouldNotBeNull()
        oui.lookupByMac("66:66:66:66:66:66").shouldBeNull()
    }

    test("LookupByOrgName") {
        oui.lookupByOrgName("Nokia").shouldNotBeNull()
        oui.lookupByOrgName("amazon").shouldNotBeNull()
        oui.lookupByOrgName("disnotarealcompanybutthisstringneedstobelongersoijustkeeptyping").size.shouldBeEqualComparingTo(0)
    }

    test("LookupByOrgAddress") {
        oui.lookupByOrgAddress("3333 Scott Blvd").shouldNotBeNull()
        oui.lookupByOrgAddress("9800 SAVAGE ROAD").shouldNotBeNull()
        oui.lookupByOrgAddress("42000 Wonderland").size.shouldBeEqualComparingTo(0)
    }

    test("RandomMAC") {
        val macLength = "A4:45:19:DE:AD:ME".length
        oui.randomMAC().length.shouldBeEqualComparingTo(macLength)
        oui.randomMAC(oui.lookupByMac("A4:45:19:DE:AD:ME")!!).length.shouldBeEqualComparingTo(macLength)
    }
})
