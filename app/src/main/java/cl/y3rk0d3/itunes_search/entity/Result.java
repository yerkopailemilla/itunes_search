package cl.y3rk0d3.itunes_search.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "result_table")
public class Result {

    @SerializedName("wrapperType")
    public String wrapperType;

    @SerializedName("kind")
    public String kind;

    @PrimaryKey()
    @SerializedName("trackId")
    public int trackId;

    @SerializedName("artistName")
    public String artistName;

    @SerializedName("collectionName")
    public String collectionName;

    @SerializedName("trackName")
    public String trackName;

    @SerializedName("collectionCensoredName")
    public String collectionCensoredName;

    @SerializedName("trackCensoredName")
    public String trackCensoredName;

    @SerializedName("previewUrl")
    public String previewUrl;

    @SerializedName("artworkUrl30")
    public String artworkUrl30;

    @SerializedName("artworkUrl60")
    public String artworkUrl60;

    @SerializedName("artworkUrl100")
    public String artworkUrl100;

    @SerializedName("trackPrice")
    public double trackPrice;

    @SerializedName("primaryGenreName")
    public String primaryGenreName;

}
