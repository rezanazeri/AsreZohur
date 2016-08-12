package ir.nazery.asrezohur.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.nazery.asrezohur.R;
import ir.nazery.asrezohur.ShowMessage;
import ir.nazery.asrezohur.adapter.IndexListAdapter;
import ir.nazery.asrezohur.database.DataManager;

/***
 * Created by reza on 95/3/3.
 ***/
public class IndexFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "asrezohourApp";
    private OnIndexItemClickListener indexItemClickListener;
    private List<String> indexList;
    private RecyclerView recyclerView;
    private DataManager dataManager;

    public IndexFragment() {
        // Required empty public constructor
    }

    public static IndexFragment newInstance() {
        return new IndexFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Context context = getActivity();
            dataManager = new DataManager(context);
            indexList = dataManager.getIndexList();
            ShowMessage.setView(view);

            recyclerView = (RecyclerView) view.findViewById(R.id.main_recyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setSmoothScrollbarEnabled(true);
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new IndexListAdapter(indexList, this, R.layout.row_indexlist);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.longMessage(R.string.errorInLoadData);
        }
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        int id = dataManager.getIdForIndexPosition(position);
        onListItemClick(id);
    }

    public void onListItemClick(int id) {
        if (indexItemClickListener != null) {
            indexItemClickListener.onIndexItemClick(id);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIndexItemClickListener) {
            indexItemClickListener = (OnIndexItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnIndexItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        indexItemClickListener = null;
    }

    public interface OnIndexItemClickListener {
        void onIndexItemClick(int id);
    }
}
