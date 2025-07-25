package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class webDialogClass extends Dialog  {

    public Activity c;
    Context context;
    Controller controller;
    String url;
    public webDialogClass(Activity c, String url) {
        super(c);
        // TODO Auto-generated constructor stub
        this.c = c;
        this.context= c;
        this.url=url;
    }
     RelativeLayout btnok;
    WebView webView;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_web);
        try {
            ((Dialog) webDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch (Exception e){}
        webView= (WebView) findViewById(R.id.webview1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);

    }

}