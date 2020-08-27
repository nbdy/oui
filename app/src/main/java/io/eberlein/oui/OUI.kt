package io.eberlein.oui

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.mapdb.*

class OUIEntry(var assignment: String, var orgName: String, var orgAddress: String)

class OUIEntrySerializer(): Serializer<OUIEntry> {
    override fun serialize(out: DataOutput2, value: OUIEntry) {
        out.writeUTF(value.assignment)
        out.writeUTF(value.orgName)
        out.writeUTF(value.orgAddress)
    }

    override fun deserialize(input: DataInput2, available: Int): OUIEntry {
        return OUIEntry(input.readUTF(), input.readUTF(), input.readUTF())
    }

}

open class OUI(
    savePath: String,
    downloadIfNeeded: Boolean = true,
    url: String = "http://standards-oui.ieee.org/oui/oui.csv"
) {
    private var db: DB = DBMaker.fileDB(savePath).closeOnJvmShutdown().make()
    private var oui = db.hashMap("oui", Serializer.STRING, OUIEntrySerializer()).createOrOpen()

    init {
        if(downloadIfNeeded && size() == 0) import(download(url))
    }

    fun close(){
        db.close()
    }

    /**
     * returns the number of keys
     * @return Int
     */
    fun size(): Int = oui.size

    /**
     * looks up a mac address and returns an OUIEntry
     * @param mac: String
     * @return OUIEntry?
     */
    fun lookup(mac: String): OUIEntry? {
        return oui[mac2assignment(mac)]
    }

    fun lookupByOrgName(orgName: String): OUIEntry? {
        val r = oui.filterValues { e -> e.orgName.contains(orgName) }
        return r[r.keys.first()]
    }

    fun lookupByOrgAddress(orgAddress: String): OUIEntry? {
        val r = oui.filterValues { e -> e.orgAddress.contains(orgAddress) }
        return r[r.keys.first()]
    }

    /**
     * imports data from a csv into the PaperDB
     * @param data: String
     */
    fun import(data: String) {
        csvReader().readAll(data).forEach {
            if (it[0] == "MA-L") oui.put(it[1], OUIEntry(it[1], it[2], it[3]))
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

        /**
         * get the first x bytes from a mac
         * @param mac: String
         * @param delimiter: String (default: ":")
         * @param bytes: Int (default: 3)
         * @return String
         */
        fun getMacPrefix(mac: String, delimiter: String = ":", bytes: Int = 3): String {
            return mac.split(delimiter).subList(0, bytes).toString()
        }

        /**
         * gets the first 3 bytes of a mac address
         * @param mac: String
         * @return String?
         */
        fun mac2assignment(mac: String): String? {
            for(d in arrayOf(":", "-", "/")) {
                if(mac.contains(d)) return getMacPrefix(mac, d)
            }
            return null
        }
    }
}