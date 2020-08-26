package io.eberlein.oui

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction


object OUIEntries: IntIdTable() {
    val assignment: Column<String> = varchar("assignment", 6).uniqueIndex()
    val orgName: Column<String> = varchar("orgName", 500)
    val orgAddress: Column<String> = varchar("orgAddress", 500)
    override val primaryKey = PrimaryKey(assignment)
}

class OUIEntry(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<OUIEntry>(OUIEntries)
    var assignment by OUIEntries.assignment
    var orgName by OUIEntries.orgName
    var orgAddress by OUIEntries.orgAddress
}

open class OUI(
    savePath: String,
    downloadIfNeeded: Boolean = true,
    url: String = "http://standards-oui.ieee.org/oui/oui.csv"
) {
    init {
        Database.connect("jdbc:sqlite:${savePath}", "org.sqlite.JDBC")
        transaction { SchemaUtils.create(OUIEntries) }
        if(downloadIfNeeded && size() == 0) import(download(url))
    }

    /**
     * returns the number of keys
     * @return Int
     */
    fun size(): Int = OUIEntry.all().count().toInt()

    /**
     * queries the OUIEntries Table
     * @param op: Operation to execute
     * @return OUIEntry?
     */
    fun query(op: Op<Boolean>): OUIEntry? {
        var r: OUIEntry? = null
        transaction { r = OUIEntry.find { op }.first() }
        return r
    }

    /**
     * looks up a mac address and returns an OUIEntry
     * @param mac: String
     * @return OUIEntry?
     */
    fun lookup(mac: String): OUIEntry? {
        return query(OUIEntries.assignment eq mac2assignment(mac)!!)
    }

    fun lookupByOrgName(orgName: String): OUIEntry? {
        return query(OUIEntries.orgName like orgName)
    }

    fun lookupByOrgAddress(orgAddress: String): OUIEntry? {
        return query(OUIEntries.orgAddress like orgAddress)
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
         * imports data from a csv into the PaperDB
         * @param data: String
         */
        fun import(data: String) {
            csvReader().readAll(data).forEach {
                if (it[0] == "MA-L") transaction {
                    OUIEntry.new {
                        assignment = it[1]
                        orgName = it[2]
                        orgAddress = it[3]
                    }
                }
            }
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