import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections

// USAGE: class ThingListTypeConverter : GsonListTypeConverter<Thing>()
//
// And Over field:
// @TypeConverters(ThingListTypeConverter::class)
open class GsonListTypeConverter<T> {
    internal var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<T> {
        val listType = object : TypeToken<List<*>>() {
        }.type

        if (data == null) {
            return Collections.emptyList<T>()
        }

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<T>): String {
        return gson.toJson(someObjects)
    }
}
