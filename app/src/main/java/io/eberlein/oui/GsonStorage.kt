package io.eberlein.oui

import com.google.gson.Gson
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OUIEntryMap : HashMap<String, OUIEntry>()

class GsonStorage(savePath: String) : OUIStorage(savePath) {
    private lateinit var fp: File
    private lateinit var data: HashMap<String, OUIEntry>
    private val gson = Gson()

    override fun open() {
        if(isOpen) return
        fp = File(savePath)
        if(!fp.isFile) {
            fp.createNewFile()
            fp.writeText("{}")
        }
        data = gson.fromJson(fp.reader(), OUIEntryMap::class.java)
        isOpen = true
    }

    override fun save() {
        fp.writeText(gson.toJson(data))
    }

    override fun get(key: String): OUIEntry? {
        return data[key]
    }

    override fun set(key: String, value: OUIEntry) {
        data[key] = value
    }

    override fun size(): Int {
        return data.size
    }

    override fun randomEntry(): OUIEntry? {
        return data[data.keys.random()]
    }

    override fun findByMac(mac: String): OUIEntry? {
        return data[mac2assignment(mac)]
    }

    override fun findByAssignment(assignment: String): OUIEntry? {
        return data[assignment]
    }

    override fun findByOrgName(orgName: String, contains: Boolean, caseInsensitive: Boolean): ArrayList<OUIEntry?> {
        val c = if(caseInsensitive) orgName.lowercase(Locale.getDefault()) else orgName
        val r = ArrayList<OUIEntry?>()
        for(e in data.values) {
            val d = if(caseInsensitive) e.orgName.lowercase(Locale.getDefault()) else e.orgName
            if((contains && d.contains(c)) || (!contains && d == c)) r.add(e)
        }
        return r
    }

    override fun findByOrgAddress(orgAddress: String, contains: Boolean, caseInsensitive: Boolean): ArrayList<OUIEntry?> {
        val c = if(caseInsensitive) orgAddress.lowercase(Locale.getDefault()) else orgAddress
        val r = ArrayList<OUIEntry?>()
        for(e in data.values) {
            val d = if(caseInsensitive) e.orgAddress.lowercase(Locale.getDefault()) else e.orgAddress
            if((contains && d.contains(c)) || (!contains && d == c)) r.add(e)
        }
        return r
    }
}
