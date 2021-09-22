package org.ebookdroid.ui.viewer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public abstract class BaseAdapter<T extends Object, VB extends ViewBinding>
        extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>> {

    public AsyncListDiffer<T> differ;

    public BaseAdapter() {
        differ = new AsyncListDiffer<T>(this, initializeDiffItemCallback());
    }

    @NonNull
    @Override
    public BaseViewHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BaseViewHolder<VB>(initializeViewBinding(layoutInflater, parent, viewType));
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    protected abstract VB initializeViewBinding(LayoutInflater inflater, ViewGroup parent, int viewType);

    protected abstract DiffUtil.ItemCallback<T> initializeDiffItemCallback();

    public static class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
        public VB binding;
        public BaseViewHolder(VB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
