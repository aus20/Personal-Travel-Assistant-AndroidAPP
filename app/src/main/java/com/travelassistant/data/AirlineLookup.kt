package com.travelassistant.data

import android.content.Context
import com.travelassistant.data.model.Airline
import com.travelassistant.R
import java.io.BufferedReader
import java.io.InputStreamReader

object AirlineLookup {
    private var map: Map<String, String>? = null

    // Call this before any getName() to lazy-load the CSV once
    private fun ensureLoaded(context: Context) {
        if (map != null) return

        val temp = mutableMapOf<String, String>()
        context.resources.openRawResource(R.raw.airlines).use { stream ->
            BufferedReader(InputStreamReader(stream)).useLines { lines ->
                lines.forEach { line ->
                    // split on commas not inside quotes
                    val cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                    if (cols.size > 3) {
                        val rawCode = cols[3].trim().trim('"')
                        val rawName = cols[1].trim().trim('"')
                        if (rawCode.isNotEmpty() && rawCode != "\\N") {
                            temp[rawCode] = rawName
                        }
                    }
                }
            }
        }
        map = temp
    }

    /** Returns the full airline name for `iataCode`, or falls back to the code itself. */
    fun getName(context: Context, iataCode: String): String {
        ensureLoaded(context)
        return map?.get(iataCode) ?: iataCode
    }
}
