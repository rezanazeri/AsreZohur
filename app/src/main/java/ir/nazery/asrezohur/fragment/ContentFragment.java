package ir.nazery.asrezohur.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.nazery.asrezohur.R;
import ir.nazery.asrezohur.ShowMessage;

/***
 * Created by reza on 95/3/6.
 ***/
public class ContentFragment extends Fragment {
    private static final String TEXT = "t";

    private String text = "";

    public ContentFragment() {
        // Required empty public constructor
    }

    public static ContentFragment newInstance(String text) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ShowMessage.setView(view);
            TextView text_textView = (TextView) view.findViewById(R.id.content_textView_text);
            text_textView.setText(text);
            Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/b_yekan.ttf");
            text_textView.setTypeface(font);
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.longMessage(R.string.errorInLoadData);
        }
    }
}
