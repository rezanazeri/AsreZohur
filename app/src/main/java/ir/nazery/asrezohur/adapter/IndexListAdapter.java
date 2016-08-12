package ir.nazery.asrezohur.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.nazery.asrezohur.R;

/***
 * Created by reza on 95/3/4.
 ***/
public class IndexListAdapter extends RecyclerView.Adapter<IndexListAdapter.ViewHolder> {
    private View.OnClickListener clickListener;
    private List<String> list;
    private int layoutRes;

    public IndexListAdapter(List<String> list, View.OnClickListener clickListener, int layoutRes) {
        this.list = list;
        this.clickListener = clickListener;
        this.layoutRes = layoutRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        view.setOnClickListener(clickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title_textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_textView;

        public ViewHolder(View view) {
            super(view);
            title_textView = (TextView) view.findViewById(R.id.indexList_textView_name);
            Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/b_yekan.ttf");
            title_textView.setTypeface(font);
        }
    }
}