package com.potikot.doings.di

import androidx.room.TypeConverter
import com.potikot.doings.data.data_source.util.TagType
import com.potikot.doings.domain.util.PriorityLevel

class Converters {
    @TypeConverter
    fun fromPriorityLevel(value: PriorityLevel): String {
        return value.name
    }

    @TypeConverter
    fun toPriorityLevel(value: String): PriorityLevel {
        return PriorityLevel.valueOf(value)
    }

    @TypeConverter
    fun fromTagType(value: TagType): String {
        return value.name
    }

    @TypeConverter
    fun toTagType(value: String): TagType {
        return TagType.valueOf(value)
    }
}