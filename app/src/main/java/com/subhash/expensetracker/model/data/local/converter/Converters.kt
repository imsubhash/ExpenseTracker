package com.subhash.expensetracker.model.data.local.converter

import androidx.room.TypeConverter
import com.subhash.expensetracker.model.data.local.entity.ExpenseCategory
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromCategory(category: ExpenseCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(categoryName: String): ExpenseCategory {
        return ExpenseCategory.valueOf(categoryName)
    }
}