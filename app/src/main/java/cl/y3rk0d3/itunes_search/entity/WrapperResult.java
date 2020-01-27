package cl.y3rk0d3.itunes_search.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WrapperResult {

    @SerializedName("resultCount")
    @Expose
    public Integer resultCount;
    @SerializedName("results")
    @Expose
    public List<Result> results = null;

}
