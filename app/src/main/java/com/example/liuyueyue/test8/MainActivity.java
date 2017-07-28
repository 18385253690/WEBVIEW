package com.example.liuyueyue.test8;

import android.app.ProgressDialog;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
   private String url = "http://2014.qq.com/";
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Uri uri = Uri.parse(url); //url为你要链接的地址
        // Intent intent =new Intent(Intent.ACTION_VIEW, uri);
        // startActivity(intent);
        init();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webView);
        //webview加载本地资源
        //webView.loadUrl("file:///android_asset/example.html");
        //webview加载web资源
        webView.loadUrl(url);
        //覆盖webView默认通过第三方或则是系统浏览器打开网页的行为
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回值是true的时候控制网页在webview中打开，为false的时候调用系统浏览器打开
                return true;
            }
            //webviewClient帮助webview去处理页面请求，和请求通知
        });
        //启用javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //webview加载页面优先使用缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //加载网页进度条显示
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               //newProgress 1-100之间的整数
                if(newProgress == 100){
                    //网页加载完毕,关闭progressDialog
                    closeDialog();
                }else{
                    //网页正在加载,打开progressDialog
                    openDialog(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
            private void openDialog(int newProgress ) {
                if(dialog == null){
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();
                }else{
                    dialog.setProgress(newProgress);
                }
            }
            private void closeDialog() {
            if(dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
                dialog=null;
            }
            }
        });

    }

    //改写物理按键—返回值的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //点击返回按钮时弹出当前地址
            Toast.makeText(this,webView.getUrl(),Toast.LENGTH_SHORT).show();
            if(webView.canGoBack()){
                webView.goBack();//返回上一层
                return true;
            }else{
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

