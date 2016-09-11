package ir.nazery.asrezohur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

import ir.nazery.asrezohur.libs.Remember;
import ir.nazery.asrezohur.libs.StatusBarUtil;

/***
 * Created by reza on 95/3/2.
 ***/
public class MainActivity extends AppCompatActivity {

    private final String TAG = "rezanazeri";
    private final String HOME_PAGE = "file:///android_asset/text/html/0.htm";
    private final String LAST_URL = "lu";
    private final String SCROLL_X = "x";
    private final String SCROLL_Y = "y";
    private final String SCALE = "s";
    private WebView webView;
    private MenuItem bookmarkMenuItem;
    private ArrayList<String> history;
    private boolean needScrollToBookmark = false;
    private ProgressDialog progressDialog;

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
            settings.setSupportZoom(true);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                settings.setBuiltInZoomControls(true);
            }

            if (savedInstanceState != null) {
                history = savedInstanceState.getStringArrayList(LAST_URL);
            }

            if (history == null || history.isEmpty()) {
                history = new ArrayList<>();
                history.add(HOME_PAGE);
            }

            webView.loadUrl(history.get(history.size() - 1));

            changeStatusBarColor();
            Remember.init(this, getPackageName());

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
        history.remove(size - 1);
        size--;
//        Log.d(TAG, "history size: " + size);
        if (size > 0) {
            webView.loadUrl(history.get(size - 1));
        } else {
            super.onBackPressed();
        }
    }

    private void zoomIn() {
        try {
            int scale = Math.round(webView.getScale() * 100);
            webView.setInitialScale(scale + 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zoomOut() {
        try {
            int scale = Math.round(webView.getScale() * 100);
            webView.setInitialScale(scale - 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBookmark() {
        Remember.putInt(SCROLL_X, webView.getScrollX());
        Remember.putInt(SCROLL_Y, webView.getScrollY());
        String url = webView.getUrl();
        if (url.contains("#")) {
            int i = url.indexOf("#");
            url = url.substring(0, i);
            Log.d(TAG, "saved url is: " + url);
        }
        Remember.putString(LAST_URL, url);

        int scale = Math.round(webView.getScale() * 100);
        Remember.putInt(SCALE, scale);

//        Log.d(TAG, "scale: " + scale);
    }

    private void goToBookmark() {
        if (!Remember.containsKey(LAST_URL)) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("درحال بارگذاری");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();

        needScrollToBookmark = true;
        String url = Remember.getString(LAST_URL, "");
        history.clear();
        history.add(HOME_PAGE);
        history.add(url);

        webView.loadUrl(url);
        webView.setInitialScale(Remember.getInt(SCALE, 100));


//            Log.d(TAG, String.format("x: %d  y: %d  scale: %d", x, y, 85));
        Snackbar.make(webView, "نشانه بارگذاری شد", Snackbar.LENGTH_SHORT).show();

    }

    private void loadHomePage() {
        history = null;
        history = new ArrayList<>();
        history.add(HOME_PAGE);
        webView.loadUrl(history.get(history.size() - 1));
    }

    private void showMyketRate() throws Exception {
        // myket rate
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format("myket://comment/#Intent;scheme=comment;package=%s;end", getPackageName())));
        startActivity(intent);
    }

    private void showBazarRate() throws Exception {
        // bazar rate
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
        intent.setPackage("com.farsitel.bazaar");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        bookmarkMenuItem = menu.findItem(R.id.action_bookmark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark:
                saveBookmark();
                item.setIcon(R.drawable.ic_bookmark_white_36dp);
                Snackbar.make(webView, "نشانه ذخیره شد", Snackbar.LENGTH_SHORT).show();
                return true;

            case R.id.action_zoomIn:
                zoomIn();
                return true;

            case R.id.action_zoomOut:
                zoomOut();
                return true;

            case R.id.action_index:
                loadHomePage();
                return true;

            case R.id.action_goToBookmark:
                goToBookmark();
                return true;

            case R.id.action_rate:
                try {
                    showMyketRate();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "خطا", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_aboutus:
                startActivity(new Intent(this, AboutusActivity.class));
                return true;

            case R.id.action_exit:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "load: " + url);

            if (url.contains(".htm")) {
                if (history != null && !history.contains(url)) {
                    history.add(url);
//                    Log.d(TAG, "added: " + url);
                }

                if (url.contains(Remember.getString(LAST_URL, ""))) {
                    if (bookmarkMenuItem != null) {
                        bookmarkMenuItem.setIcon(R.drawable.ic_bookmark_white_36dp);

                        Log.d(TAG, "1");
                    }

                    if (needScrollToBookmark) {
                        needScrollToBookmark = false;

                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int x = Remember.getInt(SCROLL_X, 0);
                                int y = Remember.getInt(SCROLL_Y, 0);
                                view.scrollBy(x, y);

                                progressDialog.dismiss();
                                Log.d(TAG, "scrolled");
                            }
                        }, 1000);
                    }
                } else {
                    if (bookmarkMenuItem != null) {
                        bookmarkMenuItem.setIcon(R.drawable.ic_bookmark_border_white_36dp);

                        Log.d(TAG, "2");
                    }
                }
            }
        }
    }
}
