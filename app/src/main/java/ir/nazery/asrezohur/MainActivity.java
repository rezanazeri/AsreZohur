package ir.nazery.asrezohur;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

import ir.nazery.asrezohur.libs.StatusBarUtil;

/***
 * Created by reza on 95/3/2.
 ***/
public class MainActivity extends AppCompatActivity {

    private final String TAG = "rezanazeri";
    private final String LAST_URL = "lastUrl";
    private final String HOME_PAGE = "file:///android_asset/text/html/0.htm";
    private WebView webView;
    private ArrayList<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        try {
            webView = (WebView) findViewById(R.id.webView);
            webView.setWebViewClient(new MyWebClient());
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(false);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);

            if (savedInstanceState != null) {
                history = savedInstanceState.getStringArrayList(LAST_URL);
            }

            if (history == null || history.isEmpty()) {
                history = new ArrayList<>();
                history.add(HOME_PAGE);
            }

            webView.loadUrl(history.get(history.size() - 1));

            changeStatusBarColor();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.mainContainer, TestFragment.newInstance())
//                .commit();
//
//        dataManager = new DataManager(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (history != null) {
            outState.putStringArrayList(LAST_URL, history);
        }
        super.onSaveInstanceState(outState);
    }

    private void changeStatusBarColor() throws Exception {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            StatusBarUtil.setTranslucent(this, 0);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
        }
    }

    @Override
    public void onBackPressed() {
        int size = history.size();
        Log.d(TAG, "history size: " + size);
        if (size > 1) {
            history.remove(--size);
            webView.loadUrl(history.get(size - 1));
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished: " + url);
            if (url.contains(".htm") && history != null && !history.contains(url)) {
                history.add(url);
                Log.d(TAG, "added: " + url);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_aboutus:
                startActivity(new Intent(this, AboutusActivity.class));
                return true;
            case R.id.action_exit:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //    @Override
//    public void onIndexItemClick(int id) {
//        String text = dataManager.getTextForID(id);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.mainContainer, ContentFragment.newInstance(text))
//                .addToBackStack(null)
//                .commit();
//    }
//
//    /***
//     * if fragment stack count equal 0 exit from app
//     * else back to previous fragment
//     ***/
//    @Override
//    public void onBackPressed() {
//
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            super.onBackPressed();
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//    }
}
