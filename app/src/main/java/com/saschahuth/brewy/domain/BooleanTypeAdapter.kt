package com.saschahuth.brewy.domain

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * Created by sascha on 20.02.16.
 */
class BooleanTypeAdapter : TypeAdapter<Boolean>() {

    override fun read(jsonReader: JsonReader?): Boolean? {
        return jsonReader?.peek() == JsonToken.STRING && jsonReader?.nextString() == "Y"
    }

    override fun write(jsonWriter: JsonWriter?, value: Boolean?) {
        if (value == null) {
            jsonWriter?.nullValue()
        } else {
            jsonWriter?.value(if (value) "Y" else "N")
        }
    }
}