package com.tyshi00.astrolight.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "preferences")
data class PreferenceEntity(
    @PrimaryKey val key: String,
    val value: String,
)

@Dao
interface PreferenceDao {
    @Query("SELECT * FROM preferences WHERE `key` = :key LIMIT 1")
    suspend fun get(key: String): PreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun set(entity: PreferenceEntity)

    @Query("DELETE FROM preferences WHERE `key` = :key")
    suspend fun delete(key: String)
}

@Database(entities = [PreferenceEntity::class], version = 1, exportSchema = false)
abstract class AstroLightDatabase : RoomDatabase() {
    abstract fun preferenceDao(): PreferenceDao
}

/**
 * Stored DOB entry with label, date, and index.
 */
data class DOBEntry(
    val index: Int,
    val label: String,
    val year: Int,
    val month: Int,
    val day: Int,
) {
    val dateDisplay: String get() = "$month/$day/$year"
}

class AstroLightRepository(private val db: AstroLightDatabase) {

    companion object {
        private const val DOB_COUNT = "dob_count"
        private const val ACTIVE_DOB = "active_dob_index"
        private const val INVERT_COLORS = "invert_colors"
        private const val SHOW_DAILY = "show_daily"
        private const val SHOW_WEEKLY = "show_weekly"
        private const val SHOW_MONTHLY = "show_monthly"
        private const val SHOW_WESTERN = "show_western"
        private const val SHOW_CHINESE = "show_chinese"
        const val MAX_DOBS = 5

        @Volatile
        private var INSTANCE: AstroLightRepository? = null

        fun getInstance(factory: () -> AstroLightDatabase): AstroLightRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AstroLightRepository(factory()).also { INSTANCE = it }
            }
    }

    // ---- Multi-DOB storage ----

    private fun dobKey(index: Int, field: String) = "dob_${index}_$field"

    suspend fun getDOBCount(): Int =
        db.preferenceDao().get(DOB_COUNT)?.value?.toIntOrNull() ?: 0

    suspend fun getActiveDOBIndex(): Int =
        db.preferenceDao().get(ACTIVE_DOB)?.value?.toIntOrNull() ?: 0

    suspend fun setActiveDOBIndex(index: Int) {
        db.preferenceDao().set(PreferenceEntity(ACTIVE_DOB, index.toString()))
    }

    suspend fun getDOBEntry(index: Int): DOBEntry? {
        val year = db.preferenceDao().get(dobKey(index, "year"))?.value?.toIntOrNull() ?: return null
        val month = db.preferenceDao().get(dobKey(index, "month"))?.value?.toIntOrNull() ?: return null
        val day = db.preferenceDao().get(dobKey(index, "day"))?.value?.toIntOrNull() ?: return null
        val label = db.preferenceDao().get(dobKey(index, "label"))?.value ?: "Entry ${index + 1}"
        return DOBEntry(index, label, year, month, day)
    }

    suspend fun getAllDOBEntries(): List<DOBEntry> {
        val count = getDOBCount()
        return (0 until count).mapNotNull { getDOBEntry(it) }
    }

    suspend fun addDOB(year: Int, month: Int, day: Int, label: String): Int {
        val count = getDOBCount()
        if (count >= MAX_DOBS) return -1
        val index = count
        db.preferenceDao().set(PreferenceEntity(dobKey(index, "year"), year.toString()))
        db.preferenceDao().set(PreferenceEntity(dobKey(index, "month"), month.toString()))
        db.preferenceDao().set(PreferenceEntity(dobKey(index, "day"), day.toString()))
        db.preferenceDao().set(PreferenceEntity(dobKey(index, "label"), label))
        db.preferenceDao().set(PreferenceEntity(DOB_COUNT, (count + 1).toString()))
        // If this is the first entry, make it active
        if (count == 0) setActiveDOBIndex(0)
        return index
    }

    suspend fun deleteDOB(index: Int) {
        val count = getDOBCount()
        if (index < 0 || index >= count) return

        // Remove the entry by shifting everything after it down
        for (i in index until count - 1) {
            val next = getDOBEntry(i + 1) ?: continue
            db.preferenceDao().set(PreferenceEntity(dobKey(i, "year"), next.year.toString()))
            db.preferenceDao().set(PreferenceEntity(dobKey(i, "month"), next.month.toString()))
            db.preferenceDao().set(PreferenceEntity(dobKey(i, "day"), next.day.toString()))
            db.preferenceDao().set(PreferenceEntity(dobKey(i, "label"), next.label))
        }

        // Clear the last slot
        val last = count - 1
        db.preferenceDao().delete(dobKey(last, "year"))
        db.preferenceDao().delete(dobKey(last, "month"))
        db.preferenceDao().delete(dobKey(last, "day"))
        db.preferenceDao().delete(dobKey(last, "label"))
        db.preferenceDao().set(PreferenceEntity(DOB_COUNT, (count - 1).toString()))

        // Adjust active index
        val active = getActiveDOBIndex()
        if (count - 1 == 0) {
            setActiveDOBIndex(0)
        } else if (active >= count - 1) {
            setActiveDOBIndex(count - 2)
        } else if (active > index) {
            setActiveDOBIndex(active - 1)
        }
    }

    // ---- Active profile ----

    suspend fun hasDOB(): Boolean = getDOBCount() > 0

    suspend fun getProfile(): ZodiacCalculator.ZodiacProfile? {
        val active = getActiveDOBIndex()
        val entry = getDOBEntry(active) ?: return null
        return ZodiacCalculator.profile(entry.year, entry.month, entry.day)
    }

    suspend fun getActiveLabel(): String {
        val active = getActiveDOBIndex()
        return getDOBEntry(active)?.label ?: ""
    }

    // ---- Toggles (all default true) ----

    private suspend fun getBool(key: String, default: Boolean = true): Boolean {
        val raw = db.preferenceDao().get(key)?.value ?: return default
        return raw == "true"
    }
    private suspend fun setBool(key: String, value: Boolean) {
        db.preferenceDao().set(PreferenceEntity(key, value.toString()))
    }

    suspend fun getInvertColors(): Boolean = getBool(INVERT_COLORS, default = false)
    suspend fun setInvertColors(v: Boolean) = setBool(INVERT_COLORS, v)

    suspend fun getShowDaily(): Boolean = getBool(SHOW_DAILY)
    suspend fun setShowDaily(v: Boolean) = setBool(SHOW_DAILY, v)

    suspend fun getShowWeekly(): Boolean = getBool(SHOW_WEEKLY)
    suspend fun setShowWeekly(v: Boolean) = setBool(SHOW_WEEKLY, v)

    suspend fun getShowMonthly(): Boolean = getBool(SHOW_MONTHLY)
    suspend fun setShowMonthly(v: Boolean) = setBool(SHOW_MONTHLY, v)

    suspend fun getShowWestern(): Boolean = getBool(SHOW_WESTERN)
    suspend fun setShowWestern(v: Boolean) = setBool(SHOW_WESTERN, v)

    suspend fun getShowChinese(): Boolean = getBool(SHOW_CHINESE)
    suspend fun setShowChinese(v: Boolean) = setBool(SHOW_CHINESE, v)
}
