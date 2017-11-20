package app.paypro.payproapp.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by rogerbaiget on 20/11/17.
 */

public class DateTypeConverter {

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }
}