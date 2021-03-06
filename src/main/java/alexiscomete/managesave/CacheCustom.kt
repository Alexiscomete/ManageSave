package alexiscomete.managesave

import java.sql.SQLException

class CacheCustom<U>(private val table: Table, private val function: (Long) -> U) {
    val hashMap = HashMap<Long, U>()
    operator fun get(l: Long): U? {
        var u = hashMap[l]
        if (u == null) {
            try {
                val resultSet = saveManager.executeQuery("SELECT * FROM " + table.name + " WHERE id = " + l, true)
                if (resultSet != null) {
                    if (resultSet.next()) {
                        u = function(l)
                        hashMap[l] = u
                    }
                }
                resultSet?.close()
            } catch (throwables: SQLException) {
                throwables.printStackTrace()
            }
        }
        return u
    }

    fun add(id: Long) {
        val hashMap = HashMap<String, String>()
        hashMap["id"] = id.toString()
        saveManager.insert(table.name, hashMap)
    }
}