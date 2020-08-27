package io.eberlein.oui

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request

class OUIEntry(var assignment: String, var orgName: String, var orgAddress: String)

open class OUI(
    private val storage: OUIStorage,
    downloadIfNeeded: Boolean = true,
    url: String = "http://standards-oui.ieee.org/oui/oui.csv"
) {
    init {
        storage.open()
        if(downloadIfNeeded && size() == 0) import(download(url))
    }

    /**
     * returns the number of keys
     * @return Int
     */
    fun size(): Int = storage.size()

    /**
     * looks up a mac address and returns an OUIEntry
     * @param mac: String
     * @return OUIEntry?
     */
    fun lookupByMac(mac: String): OUIEntry? {
        return storage.findByMac(mac)
    }

    fun lookupByOrgName(orgName: String, contains: Boolean=false, caseInsensitive: Boolean=false): ArrayList<OUIEntry?> {
        return storage.findByOrgName(orgName, contains, caseInsensitive)
    }

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
            rsp.body.toString()
        }
    }
}