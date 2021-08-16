package com.hxs.ktutil.core.misc

import fr.arnaudguyon.xmltojsonlib.XmlToJson.Builder

object StringUtil {

    fun xmlToJson(xmlStr: String): String {
        val obj = Builder(xmlStr).build().toJson()
        return obj?.toString() ?: ""
    }
}