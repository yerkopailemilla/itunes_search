package cl.y3rk0d3.itunes_search.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import cl.y3rk0d3.itunes_search.entity.Result;

@Database(entities = {Result.class}, exportSchema = false, version = 1)
public abstract class ItunesSearchDB extends RoomDatabase {
    abstract public ResultDAO resultDAO();
}
