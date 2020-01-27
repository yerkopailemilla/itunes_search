package cl.y3rk0d3.itunes_search.binding.fragment;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

public class FragmentBindingAdapter {

    private final Fragment fragment;

    @Inject
    public FragmentBindingAdapter(Fragment fragment){
        this.fragment = fragment;
    }

    @BindingAdapter("fmAlbumUrl")
    public void bindImageAlbum(ImageView imageView, String urlAlbum) {
        Glide.with(fragment).load(urlAlbum).into(imageView);
    }

}
