package com.fifty.fiftynews.db

import androidx.room.TypeConverter
import com.fifty.fiftynews.models.Source

class Converters {
    // Convert article source to room readable type.
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name!!
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}