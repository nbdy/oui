package io.eberlein.oui

interface IStorage<T> {
    fun open()
    fun save()
    fun get(key: String): T?
    fun set(key: String, value: T)
    fun size(): Int
}

interface IOUIEntryStorage : IStorage<OUIEntry> {
    /**
     * get the first x bytes from a mac
     * @param mac: String
     * @param delimiter: String (default: ":")
     * @param bytes: Int (default: 3)
     * @return String
     */
    fun getMacPrefix(mac: String, delimiter: String = ":", bytes: Int = 3): String {
        var r = ""
        mac.split(delimiter).subList(0, bytes).forEach {c -> r += c}
        println(r)
        return r
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

    fun randomEntry(): OUIEntry?
    fun findByMac(mac: String): OUIEntry?
    fun findByAssignment(assignment: String): OUIEntry?
    fun findByOrgName(orgName: String, contains: Boolean=false, caseInsensitive: Boolean=false): ArrayList<OUIEntry?>
    fun findByOrgAddress(orgAddress: String, contains: Boolean=false, caseInsensitive: Boolean=false): ArrayList<OUIEntry?>
}