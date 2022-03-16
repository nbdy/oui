package io.eberlein.oui

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.random.Random

// todo, use this in mac generation
enum class MACType {
    INDIVIDUAL, UNIVERSAL
}

class OUIEntry(var assignment: String, var orgName: String, var orgAddress: String)

open class OUI(
    private val storage: OUIStorage,
    downloadIfNeeded: Boolean = true,
    url: String = "http://standards-oui.ieee.org/oui/oui.csv",
    private val debug: Boolean = false
) {
    init {
        if(debug) println("opening storage")
        storage.open()
        if(downloadIfNeeded && size() == 0) {
            if(debug) println("downloadIsNeeded && size: ${size()}")
            import(download(url))
        }
    }

    /**
     * returns the number of keys
     * @return Int
     */
    fun size(): Int = storage.size()

    /**
     * returns a valid mac based on a oui entry assignment
     * @return String
     */
    fun randomMAC(ouiEntry: OUIEntry, type: MACType=MACType.UNIVERSAL): String {
        var r = ""
        for(c in ouiEntry.assignment.chunked(2)) r += "${c}:"
        Random.Default.nextBytes(3).forEach { b -> r += "%02x:".format(b) }
        r = r.substring(0, r.length -1) // removing the trailing ':'
        return r
    }

    fun randomMAC(): String {
        return randomMAC(storage.randomEntry()!!)
    }

    /**
     * looks up a mac address and returns an OUIEntry
     * @param mac: String
     * @return OUIEntry?
     */
    fun lookupByMac(mac: String): OUIEntry? {
        val r = storage.findByMac(mac)
        if(debug && r != null) println("lookupByMac: $mac ; found: ${r.assignment}")
        return r
    }

    /**
     * looks up by the organization name and returns an OUIEntry
     * @param orgName: String
     * @param contains: Boolean (default: false)
     * @param caseInsensitive: Boolean (default: false)
     */
    fun lookupByOrgName(orgName: String, contains: Boolean=false, caseInsensitive: Boolean=false): ArrayList<OUIEntry?> {
        return storage.findByOrgName(orgName, contains, caseInsensitive)
    }

    /**
     * looks up by the organization address and returns an OUIEntry
     * @param orgAddress: String
     * @param contains: Boolean (default: false)
     * @param caseInsensitive: Boolean (default: false)
     */
    fun lookupByOrgAddress(orgAddress: String, contains: Boolean=false, caseInsensitive: Boolean=false): ArrayList<OUIEntry?> {
        return storage.findByOrgAddress(orgAddress, contains, caseInsensitive)
    }

    /**
     * imports data from a csv into the PaperDB
     * @param data: String
     */
    private fun import(data: String) {
        csvReader().readAll(data).forEach {
            if (it[0] == "MA-L") storage.set(it[1], OUIEntry(it[1], it[2], it[3]))
        }
        storage.save()
    }

    companion object {
        /**
         * downloads a file from the given url
         * @param url: String
         * @return String
         */
        fun download(url: String = "http://standards-oui.ieee.org/oui/oui.csv"): String = runBlocking {
            val c = OkHttpClient()
            val r = Request.Builder().url(url).build()
            val rsp = c.newCall(r).execute()
            rsp.body!!.string()
        }
    }
}
