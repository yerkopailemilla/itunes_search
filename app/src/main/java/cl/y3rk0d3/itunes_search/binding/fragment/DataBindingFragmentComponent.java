package cl.y3rk0d3.itunes_search.binding.fragment;

import androidx.fragment.app.Fragment;

public class DataBindingFragmentComponent implements androidx.databinding.DataBindingComponent {

    private final FragmentBindingAdapter adapters;

    public DataBindingFragmentComponent(Fragment fragment){
        this.adapters = new FragmentBindingAdapter(fragment);
    }

    public FragmentBindingAdapter getFragmentBindingAdapter(){
        return adapters;
    }

}
