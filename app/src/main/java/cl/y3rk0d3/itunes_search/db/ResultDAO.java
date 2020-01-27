package cl.y3rk0d3.itunes_search.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cl.y3rk0d3.itunes_search.entity.Result;

@Dao
public interface ResultDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Result result);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Result> resultList);

    @Query("SELECT * FROM result_table WHERE trackId = :idResult")
    LiveData<Result> findById(int idResult);

    @Query("SELECT * FROM result_table ORDER BY trackId DESC")
    LiveData<List<Result>> findAll();

    @Query("SELECT * FROM result_table WHERE artistName = :artist ORDER BY trackPrice DESC")
    LiveData<List<Result>> loadResults(String artist);

}
