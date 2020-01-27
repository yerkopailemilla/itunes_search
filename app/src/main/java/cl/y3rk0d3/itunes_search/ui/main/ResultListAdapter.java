package cl.y3rk0d3.itunes_search.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import java.util.Objects;

import cl.y3rk0d3.itunes_search.R;
import cl.y3rk0d3.itunes_search.databinding.ItemListResultBinding;
import cl.y3rk0d3.itunes_search.entity.Result;
import cl.y3rk0d3.itunes_search.ui.resources.DataBoundListAdapter;

public class ResultListAdapter extends DataBoundListAdapter<Result, ItemListResultBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ResultClickCallback resultClickCallback;

    public ResultListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, ResultClickCallback resultClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.resultClickCallback = resultClickCallback;
    }

    @Override
    protected ItemListResultBinding createBinding(ViewGroup parent) {
        ItemListResultBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_result,
                        parent, false, dataBindingComponent);

        return binding;
    }

    @Override
    protected void bind(ItemListResultBinding binding, Result item) {
        binding.setResult(item);
        binding.getRoot().setOnClickListener(v -> {
            Result result = binding.getResult();
            if (result != null && resultClickCallback != null) {
                resultClickCallback.display(result);
            }
        });
        binding.itemListResultFavorite.setOnClickListener(v -> resultClickCallback.addFavorite());
    }

    @Override
    protected boolean areItemsTheSame(Result oldItem, Result newItem) {
        return Objects.equals(oldItem.trackId, newItem.trackId) &&
                Objects.equals(oldItem.trackName, newItem.trackName);
    }

    @Override
    protected boolean areContentsTheSame(Result oldItem, Result newItem) {
        return Objects.equals(oldItem.artistName, newItem.artistName) &&
                oldItem.primaryGenreName.equals(newItem.primaryGenreName);
    }

    public interface ResultClickCallback {
        void display(Result result);

        void addFavorite();
    }

}
