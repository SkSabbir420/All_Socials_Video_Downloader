/*
 * *
 *  * Created by Syed Usama Ahmad on 2/24/23, 5:29 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/24/23, 3:05 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;

import static com.erickruo.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.erickruo.allinonevideodownloader.webservices.DownloadVideosMain.pd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.databinding.ActivityGetLinkThroughWebviewBinding;
import com.erickruo.allinonevideodownloader.models.dlapismodels.VideoModel;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;

public class GetTiktokLinkThroughWebview extends AppCompatActivity {
    String url = "";
    ProgressDialog progressDialog;
    boolean isOnetime = false;
    private ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();
    private ActivityGetLinkThroughWebviewBinding binding;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetLinkThroughWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(GetTiktokLinkThroughWebview.this);
        progressDialog.setMessage("Loading.... Note if it takes longer then 1 min close the app and retry");
        progressDialog.show();

        url = "https://audiomack.com/lightskinkeisha/song/fdh";


        // url = "https://audiomack.com/embed/song/lightskinkeisha/fdh?background=1";

        if (getIntent().hasExtra("myurlis")) {
            url = getIntent().getStringExtra("myurlis");

        } else {
            url = "https://audiomack.com/embed/song/lightskinkeisha/fdh?background=1";
        }
        url = "http://tikdd.infusiblecoder.com/";


        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.browser, true);

        binding.browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        binding.browser.getSettings().setJavaScriptEnabled(true);
        binding.browser.getSettings().getAllowFileAccess();
        int randomnumber = iUtils.getRandomNumber(iUtils.UserAgentsList0.length);

        try {
            binding.browser.getSettings().setUserAgentString(iUtils.UserAgentsList0[randomnumber]);

        } catch (Exception e) {
            binding.browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");
        }

        // binding.browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");
        //  binding.browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 6.0.1; Nexus 5X Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.96 Mobile Safari/537.36 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

//        binding.browser.getSettings().setUserAgentString("com.zhiliaoapp.musically/2018052132 (Linux; U; Android 8.0.0; en_US; Pixel 2; Build/ABCXYZ; Cronet/58.0.2991.0)");
        // binding.browser.getSettings().setUserAgentString("com.zhiliaoapp.musically/2018052132 (Linux; Android 10;TXY567; en_US; SM-J700F; Build/MMB29K; Cronet/58.0.2991.0)");

        binding.browser.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {


                String response = "var _0xc59e=[\"\",\"split\",\"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/\",\"slice\",\"indexOf\",\"\",\"\",\".\",\"pow\",\"reduce\",\"reverse\",\"0\"];function _0xe65c(d,e,f){var g=_0xc59e[2][_0xc59e[1]](_0xc59e[0]);var h=g[_0xc59e[3]](0,e);var i=g[_0xc59e[3]](0,f);var j=d[_0xc59e[1]](_0xc59e[0])[_0xc59e[10]]()[_0xc59e[9]](function(a,b,c){if(h[_0xc59e[4]](b)!==-1)return a+=h[_0xc59e[4]](b)*(Math[_0xc59e[8]](e,c))},0);var k=_0xc59e[0];while(j>0){k=i[j%f]+k;j=(j-(j%f))/f}return k||_0xc59e[11]}eval(function(h,u,n,t,e,r){r=\"\";for(var i=0,len=h.length;i<len;i++){var s=\"\";while(h[i]!==n[e]){s+=h[i];i++}for(var j=0;j<n.length;j++)s=s.replace(new RegExp(n[j],\"g\"),j);r+=String.fromCharCode(_0xe65c(s,e,10)-t)}return decodeURIComponent(escape(r))}(\"cdahc2ehgehbafhbafhbgahcbghce4hc2ghb1ehce2hcdghcefhcdfhc22hbafhb11hcdfhc2dhcfahgehbefhcbghce4hc2dhbafhbaghgehb1fhgehbcahb1ghb1ghb1ghbaghbaghgehbdehgehbcahbcfhbcghbcghbcahbd1hbcdhbcdhbcfhb1ghbaghcfehd3hcdahc2ehgehbafhcfahcdahcdfhc22hcdghcfahb1ehcd3hcdghc2bhcbghce4hcdahcdghcdfhb1ehc2ghcdghcedhce4hcdfhcbghcdehc2dhgehbdfhbdfhbdfhgehbaehcfbhbc1hc22hcdghcfahcdfhcd3hcdghcbghc22hb1ehc2bhcdghcdehbaehgehcf5hcf5hgehcfahcdahcdfhc22hcdghcfahb1ehcd3hcdghc2bhcbghce4hcdahcdghcdfhb1ehc2ghcdghcedhce4hcdfhcbghcdehc2dhgehbdfhbdfhbdfhgehbaehc22hc2dhceghb1ehcfbhbc1hc22hcdghcfahcdfhcd3hcdghcbghc22hb1ehc2bhcdghcdehbaehbaghcfehd3hc22hcdghc2bhcefhcdehc2dhcdfhce4hb1ehc2fhc2dhce4hbeghcd3hc2dhcdehc2dhcdfhce4hbedhcf2hbfdhc22hbafhg6hcedhc2dhcbghce2hc2bhc2ghb1dhce2hc2dhcedhcefhcd3hce4hg6hbaghb1ehcdahcdfhcdfhc2dhce2hbfchca0hbgahbfghgehbdfhgehg6hbdehc22hcdahceghgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hc22hc2dhce4hcbghcdahcd3hcb1hg6hbdghbdehc22hcdahceghgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hce4hc2ghcefhcdehc2ahcdfhcbghcdahcd3hcb1hg6hbdghbdehcdahcdehc2fhgehcedhce2hc2bhbdfhcb1hg6hc2ghce4hce4hceahcedhbdchb1fhb1fhcedhc2bhcdghcdfhce4hc2dhcdfhce4hb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhb1ehc2ehc2ahc2bhc22hcdfhb1ehcdfhc2dhce4hb1fhceghb1fhce4hbcahbcehb1ehbcehbc1hbcehbcfhb1dhbcahb1ghb1fhbc2hbc1hbcehbcdhbcahbdahbcghbc1hb1ghcbehbcahbcahb1ghbcghbcdhbdahbcehbcehbd1hbc2hbc1hbcfhbcfhbc1hbd1hbd1hcbehbcahbd1hbcdhbdahbdahb1ghbcdhbcghbcghbcghbd1hbcahbc1hbc1hbcdhbcehbc1hbcfhb1ghcbehcdfhb1ehcdbhceahc2fhbeahcedhce4hceahbdfhc22hcedhce4hb1dhcdbhceahc2fhcbehcedhbd1hbcfhb1ghcfbhbd1hbcfhb1ghbadhcbghcdehceahbd3hcbehcdfhc2bhcbehc2bhcbghce4hbdfhbcahbcahb1ghbadhcbghcdehceahbd3hc2bhc2bhc2ahbdfhbcahb1dhbcghbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcedhcdahc22hbdfhc22hc2ehbcdhbcahbd1hc2dhbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcdghc2ghc2bhbdfhbgchbefhbfghcd2hbgehcdbhbgahbc1hc2bhcabhcfahbechcaehbdahcd2hbgahbe4hbgchcaghbadhcbghcdehceahbd3hcbehcdfhc2bhcbehc2ghce4hbdfhcedhc2bhcdghcdfhce4hc2dhcdfhce4hb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhbadhcbghcdehceahbd3hcdghc2ghbdfhb1ghb1ghcbehbechc2ehbe4hca0hca2hc2ahcdghc2ehbg1hb1dhbfehbcdhbfehcbghcaghc22hcdbhbcahbeghbf1hbgchbcdhbechbf5hcebhcfbhc2dhbgchbfchbf1hcfbhbcfhc2ehbeghbfehcdahcd2hbg1hcaghbg1hbfghb1ghbf5hcefhc2bhcfahbadhcbghcdehceahbd3hcdghc2dhbdfhbcfhbc2hbfahbe4hb1ghbe4hbfahbechcb1hg6hgehcbghcd3hce4hbdfhcb1hg6hcfbhbc1hc22hcdghcfahcdfhcd3hcdghcbghc22hcb1hg6hbdghbdehc22hcdahceghgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hc2bhcdghcdfhce4hc2dhcdfhce4hcb1hg6hbdghbdehc22hcdahceghgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hc2bhcd3hc2dhcbghce2hc2ehcdahcfbhcb1hg6hbdghbdehc2ghbc2hbdghbfahcbghc2bhc2dhc2ahcdghcdghcd2hgehca2hcdahc22hc2dhcdghbdehb1fhc2ghbc2hbdghbdehceahgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hcdehcbghc2fhb1ghcb1hg6hbdghbcahbdchb1ghbdahbdehb1fhceahbdghbdehc22hcdahceghgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hcdehcbghc2fhca0hbcahb1ghcb1hg6hbdghbdehc22hcdahceghgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hc2ehcd3hc2dhcfbhcb1hg6hbdghbdehcedhc2dhcd3hc2dhc2bhce4hgehcdfhcbghcdehc2dhbdfhcb1hg6hc2ehcdghce2hcdehcbghce4hbg6hc2dhcd3hc2dhc2bhce4hcb1hg6hgehcdghcdfhc2bhc2ghcbghcdfhc2fhc2dhbdfhcb1hg6hc2ehc2ahcbehbg6hc2dhcd3hc2dhc2bhce4hbfahcdahcd3hc2dhbgehcefhcbghcd3hcdahce4hcf2hbafhce4hc2ghcdahcedhbaghcb1hg6hgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hc2ehcdghce2hcdehb1dhc2bhcdghcdfhce4hce2hcdghcd3hgehc2ehcdghce2hcdehb1dhc2bhcdghcdfhce4hce2hcdghcd3hb1dhcedhcdehcbghcd3hcd3hcb1hg6hgehcdahc22hbdfhcb1hg6hc2ehcdghce2hcdehcbghce4hbg6hc2dhcd3hc2dhc2bhce4hcb1hg6hbdghbdehcdghceahce4hcdahcdghcdfhgehceghcbghcd3hcefhc2dhbdfhcb1hg6hc2ghce4hce4hceahcedhbdchb1fhb1fhceghcdahc22hc2dhcdghb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhb1ehc2ehc2ahc2bhc22hcdfhb1ehcdfhc2dhce4hb1fhceghb1fhce4hbc2hbd1hb1ehbc1hbcehbcdhbcdhbcghb1dhbc1hb1fhbc2hbc1hbcehbcghbcahbdahb1ghbd1hbc2hcbehbcahbcahbcehbdahbcehb1ghbcdhbc1hbcehbdahbc2hbcfhbc1hb1ghbcdhbc2hcbehbdahbcfhbd1hbcghbc1hbc2hbc2hbcehbc1hbc2hbc1hbdahbcghbd1hbcdhbcehbcehbc2hbcghcbehcdfhb1ehcdehceahbcdhbeahcbehcdfhc2bhcbehc2bhcbghce4hbdfhbcahb1ghbcfhbadhcbghcdehceahbd3hceghcedhbdfhbcfhbdahbcghc2ahbcdhc2dhc2ehb1ghbd1hb1ghbc2hc22hc2ahbdahc2ahbcahbadhcbghcdehceahbd3hcbehcdfhc2bhcbehceghcedhbdfhbfchbedhcd2hcedhbfahbgehbechcafhbfehbeghc22hbfahbgahbfahbfahc2ghcbghbeghbdahbc2hca2hbc2hbg1hcfahbg6hbc2hbedhbcdhbgahbeghca2hbedhbgfhcabhcaghbcdhca0hcadhceahbcfhcabhbfchc2fhbc2hbg6hcdbhbgfhcdahc2ahcadhbgfhcebhbgehcabhbfahbedhbgfhc2ghcabhbechbechcedhc2fhbedhbechbedhcabhbechbf1hbe4hbgfhbfchca0hbeghbcahbe4hcafhcdehc2ghbgehcaghbeghbfehce4hca0hbeghbdahcfbhc22hcd3hca2hbefhbgehcabhca2hbe4hc2bhbc2hcd3hbefhca0hcfdhc2ghceghc2bhbcahbeghbcehcafhcdfhbfehbf1hc2bhcabhbfahbedhbgehcabhcafhca2hbechc2fhbfghbfdhbechbgehbedhbfghbedhbcdhc2fhbg6hc2bhbfchbfehceghcaghbc2hbfehcd3hc2bhbc2hbg1hceahc22hcdehca2hc2ehc2bhcdehca2hcdbhcbghcaehbedhcd3hbechca0hbeghbg1hc2bhbc2hca2hcdahc2bhbc1hbfahce4hc2bhbf1hcfbhcd3hcaehbc1hcaghcfahc2bhcfahbechbgehc22hcdehbcahc2ghcaghcd3hbd1hcd3hc2ahcdehbfahcdahc2ahbf1hca2hc2ehc2ahcdfhbg1hbcahcafhc2fhbechc2fhc2ahcadhca2hc2ghc2bhbc2hca2hcf2hcaghca2hbd1hceghc2bhcdehcd3hcdfhcbghcadhbcehc2ghc2ahbfahbd1hcf2hcaghcaehbg1hceghc2ahbfchca2hb1ghcbghcadhbd1hcefhcaehbc2hbg1hcfdhcbghcadhb1ghbechbf5hbf1hbg1hceghc2ahcaehbedhbcahc22hbf1hca2hc2ehc2bhbc2hbg1hceahc2ahca2hbd1hceghc2ahcdehcfbhbcehcaehbc1hbfahb1ghcaehbc1hbd1hcf2hcbghcadhc22hceahc2ahcdehbfahcedhcaehbc2hbfehcd3hc2bhbc1hbd1hcedhc22hcaehbgfhceahc2ahbc1hbcdhbechbfchcaehca2hcfdhcaghca2hbd1hcedhcafhcadhbcehcdbhc2dhcdehbd1hcfdhcaehbc1hcaghceghc2bhcd3hbd1hbc1hc2bhcadhbcahc2ehc22hcaehbedhcfdhcafhbc1hbfahcedhcbghcadhbcehcdfhbechbedhbfahcd2hcbghcaehbg1hc2ghcafhcdehcfbhcd3hcaehbc2hbedhceghc2bhbc2hbgfhc2ehc2bhbfchcaghcfbhc2bhcfahbechca2hbechbe4hcabhbechbfchbechbechbechbfehcdghcbghcefhbcdhbachbc1hbedhca0hcd2hcf2hc2bhcd2hbe4hbfahbgehbfdhcdghbechcd2hbgahcfdhbf1hbechce4hbc1hc22hbfchbg1hc2ehc2bhbfchbfehcd3hc22hcdehcd3hcd3hc22hcfbhcfahcaehbgehbfahbeghc2fhbechbechbechbechbechbechbechcafhbfehbc1hbgfhc2ghc2bhbc1hc2ghc2ehc2bhcdbhbfehc2ehcafhcaehcaghcdbhcaehbc1hc22hcd3hc2ahcdbhbfahc2ghc22hcdehbg1hc2ehc2ahbf1hbg1hc2ehc2bhca0hc2fhcfahcaehbc1hcaghcf2hcafhcadhc22hc2ehbgahcd3hbd1hbc1hcbghcadhbgfhcd3hc2ahcfbhbfdhbechbf1hbedhc2ghbc1hcbghcadhbgfhcd3hc2ahbc2hbgahcefhc22hcdfhbgfhcfdhbfghcdehbg1hc2ghc2ahbf1hcfbhcdahcafhcadhbg1hce2hbfghcdfhbedhcf2hc2ahbc1hbgehbcdhbeghcd3hcaghbfehbgfhbeghca2hbgdhcaehbcahcaghbfehbgfhca2hc22hc2ehcabhcd2hca2hbgfhca2hcabhca2hca0hca2hbedhcedhbf5hcdahbedhca2hceghcaghcadhbcahc2ehc22hbf1hbfahcf2hcaghbc1hca2hb1ghcaehbc1hca2hcefhcafhbc1hbd1hcd2hcaghca2hbd1hb1ghcafhcadhc2bhbf1hc2ahbc1hca2hcfahcaehbc1hc2ghcd2hbeghbc1hbd1hcd3hc2ahca2hbd1hcf2hcaghcaehbfahbcahcaghcaehbg1hb1ghcaehbc2hbgfhceahc2ahcadhca2hc2ehc2ahcaehbgahbedhbgahbechcfbhceghcaghcadhbcahc2ehcafhbc1hcaghcdfhcaehbc2hbfehbcahc2ahbf1hcabhbfchc22hcadhbcehce4hc22hcaehbgfhcd3hcaghbedhbg1hceghcaghcadhbcahc2ehc2bhcdehbd1hceahcaehbc2hbfehcd3hcafhcadhbg1hcdghcaehbc1hbg1hceghc22hcadhbcehb1ghbedhca0hbeghcf2hbg1hcfdhbgahbc2hbeghcadhbd1hcd3hc2ahca2hbd1hceahc2bhbcahbd1hcd3hc2dhbfchbedhcd3hc2bhcdehcd3hce4hcaghcadhbcehb1ghbechbechcfbhceghcaghcadhbcahc2ehc22hcdehcd3hcd2hcaghcadhbd1hc2ehcbghcadhbgehbgdhbg1hca0hc2bhbc1hbgchca0hcd2hb1ghbg1hcdbhcabhbc2hbgahca0hcd2hcfdhbgahca0hbgehbcdhbeghcdehbd1hcd3hc2ahca2hbd1hbc1hcbghcadhbgfhcd3hc2ahbcahbd1hc2ghc2bhbc2hbg1hcd3hc22hbfahbd1hceahcaghbechbdahbc1hbgchca0hcd2hbcahbg1hbefhcabhcfahbgahbefhbeghbcahbg1hcfdhbfdhcfahbg1hcfdhbechca2hc2ahbc1hca2hce4hcaehbc2hcaghceahcaghbf1hca2hceghcaehbc2hbfehcd3hc2bhbc1hbd1hbcahc2bhcdehbg1hcd3hcaehbc1hcd3hcd2hbefhcfdhc2bhcf2hbg1hbefhc2bhb1ghbg1hcdbhbgahcfahbgchbefhcd2hbc2hbgahca0hbgahbcehbg1hbgfhcfbhceghcaghcadhbcahc2ehc2bhbc1hbd1hbcahc2bhcdehbg1hcd3hcaehbc2hcaghceahcaghbf1hca2hceghcaehbc1hca2hcefhcafhbc1hbd1hcd2hcbghcadhbcehcdfhcaehbc1hcd3hcd2hbeghbefhbeghcfahbg1hcfdhcafhbcdhbg1hca0hbeghcfdhbg1hbefhc2bhcfahbg1hbefhbgahbcdhbgchca0hbgehbgchc22hcdfhbgfhcfdhcaehbc2hbfehcd3hc2bhcaehca2hcd3hc2bhbc2hbgfhc2ehcbghcadhbgehbechbfehbgehbfdhc2bhbechbe4hcaehbeghbechbgfhcedhbfchcdahbechbfahcfdhbedhbefhc2fhbcdhbg1hca0hc2bhbe4hcafhbc1hbgehbf5hbgahcdbhbechcf2hbgahcf2hb1ghcfahbgahbg6hb1ghcfbhbg1hc2fhbg1hcf2hcafhbc1hbfdhbfahbgahca0hbfdhbc2hbgahbefhbechbefhcafhcaehbedhcfahbfahbeghcaghc2ghcafhbc1hca2hcdahc2ahbc1hbd1hce2hbfdhbf1hcaghceghc2bhcdahbedhbedhc2ahcdehbgfhcf2hc2ahbc1hcd3hcd2hbechcdehbg1hb1ghbefhcd2hcaghbe4hcaehbcahbg1hbfdhca0hbcahbfehcabhcabhbcahbd1hbgehca0hbcahbg1hcabhbeghbc1hbd1hcf2hcbghcadhc22hceahc2ahcdehbfahcedhcaehbc1hbgfhbcahc2bhcdehbfahb1ghcbghcadhbd1hcefhcaehbc2hbgahbf1hbg1hcdbhc2fhcefhbg1hca0hcafhbcehbechcdfhbgfhcfdhbfahcaehbedhcf2hc2ahbc1hc22hcf2hcaghcaehbg1hcfdhcbghcaehcaghcd3hcaehbc1hca2hcefhcafhbc1hbd1hcd2hcbghcadhbcehcdfhc2bhcfahbechbachbc2hbefhbadhcbghcdehceahbd3hc2bhc2bhc2ahbdfhbcahb1dhbcghbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcedhcdahc22hbdfhbcfhc2bhbcehbcghc2dhbdahbadhcbghcdehceahbd3hc2dhc2ehc2fhbdfhc2dhcf2hbfehbc1hcaghcadhbcehcdbhc2ahbc1hbgfhcd3hcaehbc2hbgfhc2ghcaghcf2hbfdhbcfhbfdhcdehbd1hcd3hc2bhbfahbd1hcdghcaghbe4hbfehbd1hbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcdghc2ghc2bhbdfhbfehcaehcdghbd1hbechcdbhcaehc2dhb1dhbgchb1ghbechcaehbd1hcabhbcfhcfdhcadhc2bhbadhcbghcdehceahbd3hcbehcdfhc2bhcbehc2ghce4hbdfhceghcdahc22hc2dhcdghb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhbadhcbghcdehceahbd3hcdghc2ghbdfhb1ghb1ghcbehbechc2ehbechcebhbefhc2fhcbghbc2hcedhc2fhbfehcfbhbfdhcdehcbehbfdhbfehbeghc22hcf2hcedhc2ghbefhbfdhbgehbgdhbgfhcdbhcdfhbfchbeghcbghce2hbefhceahbf5hbd1hbefhceghbgehbechbdahbgdhbechbdahbgehbadhcbghcdehceahbd3hcdghc2dhbdfhbcfhbc2hbfahbedhbechbedhbfahbfahbadhcbghcdehceahbd3hcbehcdfhc2bhcbehce2hcdahc22hbdfhbc2hbcghbcdhbcahbd1hbcdhbcehbcehbdahbcghbcghbcghbc1hbc1hbcehbadhcbghcdehceahbd3hc22hcd3hbdfhbcahcb1hg6hgehc22hcbghce4hcbghb1dhce4hcdahce4hcd3hc2dhbdfhcb1hg6hbcehbcghbcfhbd1hbd1hbcdhbcfhbcehbcghbcahbd1hbc2hbcahbcdhbdahcbehc2ghc22hb1ehcdehceahbcdhcb1hg6hbdghbgahbgdhbcdhgehbfchbefhbdehb1fhcdghceahce4hcdahcdghcdfhbdghbdehcdghceahce4hcdahcdghcdfhgehceghcbghcd3hcefhc2dhbdfhcb1hg6hc2ghce4hce4hceahcedhbdchb1fhb1fhceghcdahc22hc2dhcdghb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhb1ehc2ehc2ahc2bhc22hcdfhb1ehcdfhc2dhce4hb1fhceghb1fhce4hbcdhbc1hb1ehbcahbcghbd1hb1ghb1dhbc1hb1fhbc2hbc1hbcehbd1hbd1hbcdhbcghb1ghb1ghcbehbcdhbdahbdahbd1hbdahbc2hbc1hbcehb1ghb1ghbcehbcfhb1ghbcdhbd1hcbehbcghbc1hb1ghbc2hb1ghbc1hbdahbcehb1ghbcfhbcdhbcghbc1hbcdhb1ghbc2hbdahb1ghb1ghcbehcdfhb1ehcdehceahbcdhbeahcbehcdfhc2bhcbehc2bhcbghce4hbdfhbcahbcahbcahbadhcbghcdehceahbd3hc2bhc2bhc2ahbdfhbcahb1dhbcghbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcedhcdahc22hbdfhbd1hbdahbcehc2bhbcfhbc2hbadhcbghcdehceahbd3hc2dhc2ehc2fhbdfhc2dhcf2hbfehcf2hc2ahbfchbfdhcdahbgchcdbhc2fhcfahbgahbg6hcfahcdahc2bhcdehcfbhc2ghbfdhcdbhcdghbcahbg1hcfdhbfdhcedhbfdhcdfhcaghcd3hc2ahcdehbg1hceghcaghbf1hca2hc2ehc22hbf1hbfahcdfhbfdhcdbhcdghcdahc2bhbc2hcaghcd3hcaehbc2hbg1hcd2hbfdhcdfhb1ghbachbc2hbefhbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcdghc2ghc2bhbdfhcaehc2dhc2bhbgdhbedhcfdhcd2hcfdhcfahcd2hbcdhbechcaehcbehce2hbcdhcdahbg6hbc2hbadhcbghcdehceahbd3hce2hcd3hbdfhbdahb1ghbcahbadhcbghcdehceahbd3hceghcbghc2ahce2hbdfhbcdhbcdhbcehbadhcbghcdehceahbd3hcbehcdfhc2bhcbehc2ghce4hbdfhceghcdahc22hc2dhcdghb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhbadhcbghcdehceahbd3hcdghc2ghbdfhb1ghb1ghcbehbechc2ehbefhca2hcdbhbcdhbdahbcghbe4hc2ahcedhbcehbcfhbgchca2hcd2hcaghceahcaehceghc2bhcedhcf2hc22hc2ehcefhc2dhca0hbechbc2hbg6hb1ghb1dhbeghbf5hbcfhbcehcfdhcdfhbg6hcd3hbdahbcghbfahcbghc2fhbadhcbghcdehceahbd3hcdghc2dhbdfhbcfhbc2hbfahbcghbe4hbd1hbc2hbcdhbadhcbghcdehceahbd3hc22hcd3hbdfhbcahcb1hg6hgehc22hcbghce4hcbghb1dhce4hcdahce4hcd3hc2dhbdfhcb1hg6hbcehbcghbcfhbd1hbd1hbcdhbcfhbcehbcghbcahbd1hbc2hbcahbcdhbdahcbehcedhc22hb1ehcdehceahbcdhcb1hg6hbdghbgahbgdhbcdhgehbg6hbefhbdehb1fhcdghceahce4hcdahcdghcdfhbdghbdehb1fhcedhc2dhcd3hc2dhc2bhce4hbdghbdehcbghgehcdahc22hbdfhcb1hg6hcbghcedhcefhc2bhc2bhc2dhcedhcedhcb1hg6hgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hc2ehcdghce2hcdehb1dhc2bhcdghcdfhce4hce2hcdghcd3hgehcdehc2dhcedhc2fhb1dhc2bhcdghcdfhceghc2dhce2hce4hgehcedhcefhc2bhc2bhc2dhcedhcedhgehc2ahce4hcdfhb1dhc2ehc2ahgehcdehc2fhb1dhce2hb1dhbcahb1ghcb1hg6hgehce4hcbghce2hc2fhc2dhce4hbdfhcb1hg6hcbehc2ahcd3hcbghcdfhcd2hcb1hg6hgehce2hc2dhcd3hbdfhcb1hg6hcdfhcdghc2ehcdghcd3hcd3hcdghcfahcb1hg6hgehc2ghce2hc2dhc2ehbdfhcb1hg6hc2ghce4hce4hceahcedhbdchb1fhb1fhceghcdahc22hc2dhcdghb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhb1ehc2ehc2ahc2bhc22hcdfhb1ehcdfhc2dhce4hb1fhceghb1fhce4hbc2hbd1hb1ehbc1hbcehbcdhbcdhbcghb1dhbc1hb1fhbc2hbc1hbcehbcghbcahbdahb1ghbd1hbc2hcbehbcahbcahbcehbdahbcehb1ghbcdhbc1hbcehbdahbc2hbcfhbc1hb1ghbcdhbc2hcbehbdahbcfhbd1hbcghbc1hbc2hbc2hbcehbc1hbc2hbc1hbdahbcghbd1hbcdhbcehbcehbc2hbcghcbehcdfhb1ehcdehceahbcdhbeahcbehcdfhc2bhcbehc2bhcbghce4hbdfhbcahb1ghbcfhbadhcbghcdehceahbd3hceghcedhbdfhbcfhbdahbcghc2ahbcdhc2dhc2ehb1ghbd1hb1ghbc2hc22hc2ahbdahc2ahbcahbadhcbghcdehceahbd3hcbehcdfhc2bhcbehceghcedhbdfhbfchbedhcd2hcedhbfahbgehbechcafhbfehbeghc22hbfahbgahbfahbfahc2ghcbghbeghbdahbc2hca2hbc2hbg1hcfahbg6hbc2hbedhbcdhbgahbeghca2hbedhbgfhcabhcaghbcdhca0hcadhceahbcfhcabhbfchc2fhbc2hbg6hcdbhbgfhcdahc2ahcadhbgfhcebhbgehcabhbfahbedhbgfhc2ghcabhbechbechcedhc2fhbedhbechbedhcabhbechbf1hbe4hbgfhbfchca0hbeghbcahbe4hcafhcdehc2ghbgehcaghbeghbfehce4hca0hbeghbdahcfbhc22hcd3hca2hbefhbgehcabhca2hbe4hc2bhbc2hcd3hbefhca0hcfdhc2ghceghc2bhbcahbeghbcehcafhcdfhbfehbf1hc2bhcabhbfahbedhbgehcabhcafhca2hbechc2fhbfghbfdhbechbgehbedhbfghbedhbcdhc2fhbg6hc2bhbfchbfehceghcaghbc2hbfehcd3hc2bhbc2hbg1hceahc22hcdehca2hc2ehc2bhcdehca2hcdbhcbghcaehbedhcd3hbechca0hbeghbg1hc2bhbc2hca2hcdahc2bhbc1hbfahce4hc2bhbf1hcfbhcd3hcaehbc1hcaghcfahc2bhcfahbechbgehc22hcdehbcahc2ghcaghcd3hbd1hcd3hc2ahcdehbfahcdahc2ahbf1hca2hc2ehc2ahcdfhbg1hbcahcafhc2fhbechc2fhc2ahcadhca2hc2ghc2bhbc2hca2hcf2hcaghca2hbd1hceghc2bhcdehcd3hcdfhcbghcadhbcehc2ghc2ahbfahbd1hcf2hcaghcaehbg1hceghc2ahbfchca2hb1ghcbghcadhbd1hcefhcaehbc2hbg1hcfdhcbghcadhb1ghbechbf5hbf1hbg1hceghc2ahcaehbedhbcahc22hbf1hca2hc2ehc2bhbc2hbg1hceahc2ahca2hbd1hceghc2ahcdehcfbhbcehcaehbc1hbfahb1ghcaehbc1hbd1hcf2hcbghcadhc22hceahc2ahcdehbfahcedhcaehbc2hbfehcd3hc2bhbc1hbd1hcedhc22hcaehbgfhceahc2ahbc1hbcdhbechbfchcaehca2hcfdhcaghca2hbd1hcedhcafhcadhbcehcdbhc2dhcdehbd1hcfdhcaehbc1hcaghceghc2bhcd3hbd1hbc1hc2bhcadhbcahc2ehc22hcaehbedhcfdhcafhbc1hbfahcedhcbghcadhbcehcdfhbechbedhbfahcd2hcbghcaehbg1hc2ghcafhcdehcfbhcd3hcaehbc2hbedhceghc2bhbc2hbgfhc2ehc2bhbfchcaghcfbhc2bhcfahbechca2hbechbe4hcabhbechbfchbechbechbechbfehcdghcbghcefhbcdhbachbc1hbedhca0hcd2hcf2hc2bhcd2hbe4hbfahbgehbfdhcdghbechcd2hbgahcfdhbf1hbechce4hbc1hc22hbfchbg1hc2ehc2bhbfchbfehcd3hc22hcdehcd3hcd3hc22hcfbhcfahcaehbgehbfahbeghc2fhbechbechbechbechbechbechbechcafhbfehbc1hbgfhc2ghc2bhbc1hc2ghc2ehc2bhcdbhbfehc2ehcafhcaehcaghcdbhcaehbc1hc22hcd3hc2ahcdbhbfahc2ghc22hcdehbg1hc2ehc2ahbf1hbg1hc2ehc2bhca0hc2fhcfahcaehbc1hcaghcf2hcafhcadhc22hc2ehbgahcd3hbd1hbc1hcbghcadhbgfhcd3hc2ahcfbhbfdhbechbf1hbedhc2ghbc1hcbghcadhbgfhcd3hc2ahbc2hbgahcefhc22hcdfhbgfhcfdhbfghcdehbg1hc2ghc2ahbf1hcfbhcdahcafhcadhbg1hce2hbfghcdfhbedhcf2hc2ahbc1hbgehbcdhbeghcd3hcaghbfehbgfhbeghca2hbgdhcaehbcahcaghbfehbgfhca2hc22hc2ehcabhcd2hca2hbgfhca2hcabhca2hca0hca2hbedhcedhbf5hcdahbedhca2hceghcaghcadhbcahc2ehc22hbf1hbfahcf2hcaghbc1hca2hb1ghcaehbc1hca2hcefhcafhbc1hbd1hcd2hcaghca2hbd1hb1ghcafhcadhc2bhbf1hc2ahbc1hca2hcfahcaehbc1hc2ghcd2hbeghbc1hbd1hcd3hc2ahca2hbd1hcf2hcaghcaehbfahbcahcaghcaehbg1hb1ghcaehbc2hbgfhceahc2ahcadhca2hc2ehc2ahcaehbgahbedhbgahbechcfbhceghcaghcadhbcahc2ehcafhbc1hcaghcdfhcaehbc2hbfehbcahc2ahbf1hcabhbfchc22hcadhbcehce4hc22hcaehbgfhcd3hcaghbedhbg1hceghcaghcadhbcahc2ehc2bhcdehbd1hceahcaehbc2hbfehcd3hcafhcadhbg1hcdghcaehbc1hbg1hceghc22hcadhbcehb1ghbedhca0hbeghcf2hbg1hcfdhbgahbc2hbeghcadhbd1hcd3hc2ahca2hbd1hceahc2bhbcahbd1hcd3hc2dhbfchbedhcd3hc2bhcdehcd3hce4hcaghcadhbcehb1ghbechbechcfbhceghcaghcadhbcahc2ehc22hcdehcd3hcd2hcaghcadhbd1hc2ehcbghcadhbgehbgdhbg1hca0hc2bhbc1hbgchca0hcd2hb1ghbg1hcdbhcabhbc2hbgahca0hcd2hcfdhbgahca0hbgehbcdhbeghcdehbd1hcd3hc2ahca2hbd1hbc1hcbghcadhbgfhcd3hc2ahbcahbd1hc2ghc2bhbc2hbg1hcd3hc22hbfahbd1hceahcaghbechbdahbc1hbgchca0hcd2hbcahbg1hbefhcabhcfahbgahbefhbeghbcahbg1hcfdhbfdhcfahbg1hcfdhbechca2hc2ahbc1hca2hce4hcaehbc2hcaghceahcaghbf1hca2hceghcaehbc2hbfehcd3hc2bhbc1hbd1hbcahc2bhcdehbg1hcd3hcaehbc1hcd3hcd2hbefhcfdhc2bhcf2hbg1hbefhc2bhb1ghbg1hcdbhbgahcfahbgchbefhcd2hbc2hbgahca0hbgahbcehbg1hbgfhcfbhceghcaghcadhbcahc2ehc2bhbc1hbd1hbcahc2bhcdehbg1hcd3hcaehbc2hcaghceahcaghbf1hca2hceghcaehbc1hca2hcefhcafhbc1hbd1hcd2hcbghcadhbcehcdfhcaehbc1hcd3hcd2hbeghbefhbeghcfahbg1hcfdhcafhbcdhbg1hca0hbeghcfdhbg1hbefhc2bhcfahbg1hbefhbgahbcdhbgchca0hbgehbgchc22hcdfhbgfhcfdhcaehbc2hbfehcd3hc2bhcaehca2hcd3hc2bhbc2hbgfhc2ehcbghcadhbgehbechbfehbgehbfdhc2bhbechbe4hcaehbeghbechbgfhcedhbfchcdahbechbfahcfdhbedhbefhc2fhbcdhbg1hca0hc2bhbe4hcafhbc1hbgehbf5hbgahcdbhbechcf2hbgahcf2hb1ghcfahbgahbg6hb1ghcfbhbg1hc2fhbg1hcf2hcafhbc1hbfdhbfahbgahca0hbfdhbc2hbgahbefhbechbefhcafhcaehbedhcfahbfahbeghcaghc2ghcafhbc1hca2hcdahc2ahbc1hbd1hce2hbfdhbf1hcaghceghc2bhcdahbedhbedhc2ahcdehbgfhcf2hc2ahbc1hcd3hcd2hbechcdehbg1hb1ghbefhcd2hcaghbe4hcaehbcahbg1hbfdhca0hbcahbfehcabhcabhbcahbd1hbgehca0hbcahbg1hcabhbeghbc1hbd1hcf2hcbghcadhc22hceahc2ahcdehbfahcedhcaehbc1hbgfhbcahc2bhcdehbfahb1ghcbghcadhbd1hcefhcaehbc2hbgahbf1hbg1hcdbhc2fhcefhbg1hca0hcafhbcehbechcdfhbgfhcfdhbfahcaehbedhcf2hc2ahbc1hc22hcf2hcaghcaehbg1hcfdhcbghcaehcaghcd3hcaehbc1hca2hcefhcafhbc1hbd1hcd2hcbghcadhbcehcdfhc2bhcfahbechbachbc2hbefhbadhcbghcdehceahbd3hc2bhc2bhc2ahbdfhbcahb1dhbcghbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcedhcdahc22hbdfhbcfhc2bhbcehbcghc2dhbdahbadhcbghcdehceahbd3hc2dhc2ehc2fhbdfhc2dhcf2hbfehbc1hcaghcadhbcehcdbhc2ahbc1hbgfhcd3hcaehbc2hbgfhc2ghcaghcf2hbfdhbcfhbfdhcdehbd1hcd3hc2bhbfahbd1hcdghcaghbe4hbfehbd1hbadhcbghcdehceahbd3hcbehcdfhc2bhcbehcdghc2ghc2bhbdfhbfehcaehcdghbd1hbechcdbhcaehc2dhb1dhbgchb1ghbechcaehbd1hcabhbcfhcfdhcadhc2bhbadhcbghcdehceahbd3hcbehcdfhc2bhcbehc2ghce4hbdfhceghcdahc22hc2dhcdghb1dhcf2hcf2hcfdhbcahb1dhbcahb1ehcfbhcfbhbadhcbghcdehceahbd3hcdghc2ghbdfhb1ghb1ghcbehbechc2ehbechcebhbefhc2fhcbghbc2hcedhc2fhbfehcfbhbfdhcdehcbehbfdhbfehbeghc22hcf2hcedhc2ghbefhbfdhbgehbgdhbgfhcdbhcdfhbfchbeghcbghce2hbefhceahbf5hbd1hbefhceghbgehbechbdahbgdhbechbdahbgehbadhcbghcdehceahbd3hcdghc2dhbdfhbcfhbc2hbfahbedhbechbedhbfahbfahbadhcbghcdehceahbd3hcbehcdfhc2bhcbehce2hcdahc22hbdfhbc2hbcghbcdhbcahbd1hbcdhbcehbcehbdahbcghbcghbcghbc1hbc1hbcehbadhcbghcdehceahbd3hc22hcd3hbdfhbcahcb1hg6hgehc22hcdghcfahcdfhcd3hcdghcbghc22hbdfhcb1hg6hbcehbcghbcfhbd1hbd1hbcdhbcfhbcehbcghbcahbd1hbc2hbcahbcdhbdahcbehc2ghc22hb1ehcdehceahbcdhcb1hg6hgehc22hcbghce4hcbghb1dhce4hcf2hceahc2dhbdfhcb1hg6hcdehceahbcdhcb1hg6hbdghbefhcdghcfahcdfhcd3hcdghcbghc22hbdehb1fhcbghbdghbdehcbghgehcdahc22hbdfhcb1hg6hc2bhcdfhc2dhcfbhce4hcb1hg6hgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hc2ehcdghce2hcdehb1dhc2bhcdghcdfhce4hce2hcdghcd3hgehcdehc2dhcedhc2fhb1dhc2bhcdghcdfhceghc2dhce2hce4hgehc2ahce4hcdfhb1dhc2ehc2ahcb1hg6hgehce4hcbghce2hc2fhc2dhce4hbdfhcb1hg6hcbehc2ahcd3hcbghcdfhcd2hcb1hg6hgehc2ghce2hc2dhc2ehbdfhcb1hg6hc2ghce4hce4hceahcedhbdchb1fhb1fhc2ehc22hcdghcfahcdfhcd3hcdghcbghc22hc2dhce2hb1ehcdfhc2dhce4hb1fhbeahcebhbdfhc2ghce4hce4hceahcedhbachbc2hcbghbachbc1hc2ehbachbc1hc2ehcfahcfahcfahb1ehc2ehcbghc2bhc2dhc2ahcdghcdghcd2hb1ehc2bhcdghcdehbachbc1hc2ehce2hc2dhc2dhcd3hbachbc1hc2ehbcehbcghbcfhbd1hbd1hbcdhbcfhbcehbcghbcahbd1hbc2hbcahbcdhbdahcb1hg6hbdghbefhcdghcfahcdfhcd3hcdghcbghc22hgehbfchbefhb11hbdehb1fhcbghbdghbdehb1fhc22hcdahceghbdghbdehc2ahce2hbdghbdehc2ahce2hbdghbdehc2ahce2hbdghbdehc22hcdahceghgehc2bhcd3hcbghcedhcedhbdfhcb1hg6hcbghc22hc22hce4hc2ghcdahcedhcbehcdahcdfhcd3hcdahcdfhc2dhcbehcedhc2ghcbghce2hc2dhcbehce4hcdghcdghcd3hc2ahcdghcfbhcb1hg6hbdghbdehb1fhc22hcdahceghbdghbdehb1fhc22hcdahceghbdghbdehb1fhc22hcdahceghbdghbdehb1fhc22hcdahceghbdghbdehb1fhc22hcdahceghbdghbdehb1fhc22hcdahceghbdghg6hbd3hd3hcfghd3hcfghd3h\",60,\"abcdefghi\",14,7,10))";


                String inputString = response;
                String targetString = "decodeURIComponent(escape(r))";
                String prefix = "console.log('Hello'+";
                String suffix = ")";

                String outputString = inputString.replace(targetString, prefix + targetString + suffix);
                System.out.println(outputString);

                WebView web = new WebView(GetTiktokLinkThroughWebview.this);
                web.getSettings().setJavaScriptEnabled(true);
                web.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                        Log.d("chromium-A-WebView", consoleMessage.message());
                        String decodedHtml = StringEscapeUtils.unescapeHtml4(consoleMessage.message());
                        Log.d("chromium-A-WebView222 ", iUtils.extractUrls(decodedHtml).get(0));
                        return true;

                    }
                });
                web.evaluateJavascript("javascript:" + outputString, value -> {
                    System.out.println("myvall=" + value);
                });

                binding.browser.getSettings().setJavaScriptEnabled(true);
//                binding.browser.evaluateJavascript("javascript:" + outputString, new ValueCallback<String>() {
//
//                    @Override
//                    public void onReceiveValue(String value) {
//
//                        System.out.println("myvall=" + value);
//                        // Do something with the result if necessary
//                    }
//                });

//                new Handler().postDelayed(() -> {
//
//
//                    if (url.contains("tiktok")) {
//
//                        // binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementById('RENDER_DATA').textContent);");
//                        binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");
//                        //binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");
//                        // binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.querySelectorAll(\"script[id='__NEXT_DATA__']\")[0].innerHTML));");
//
//                    } else {
//                        progressDialog.dismiss();
//
//                        if (!fromService) {
//                            pd.dismiss();
//                            iUtils.ShowToast(GetTiktokLinkThroughWebview.this, getResources().getString(R.string.somthing));
//                        }
//
//                        Intent intent = new Intent();
//                        setResult(2, intent);
//                        finish();
//                    }
//
//                }, 1000);

            }
        });


        binding.browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("chromium-WebView", consoleMessage.message());
                String decodedHtml = StringEscapeUtils.unescapeHtml4(consoleMessage.message());
                Log.d("chromium-WebView222 ", iUtils.extractUrls(decodedHtml).get(0));
                return true;
            }
        });

        binding.browser.loadUrl(url);


    }


    class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void showHTML(final String myurl, final String html) {
            progressDialog.dismiss();

            System.out.println("myhtml res uu =" + myurl);
            System.out.println("myhtml res =" + html);


            if (!isOnetime) {
                isOnetime = true;


                if (myurl.contains("tiktok")) {

                    try {
//                        String afterDecode = URLDecoder.decode(html, "UTF-8");
//
//
//                        JSONObject jsonObject = new JSONObject(afterDecode);
//
//                        String video_id = jsonObject
//                                .getJSONObject("rpcRes")
//                                .getJSONObject("res")
//                                .getJSONObject("ItemInfo")
//                                .getJSONObject("ItemStruct")
//                                .getJSONObject("Video")
//                                .getJSONObject("PlayAddr")
//                                .getString("Uri");
//
//
//                        String ddurl = "https://api2-16-h2.musical.ly/aweme/v1/play/?video_id=" + video_id + "&vr_type=0&is_play_url=1&source=PackSourceEnum_PUBLISH&media_type=4&ratio=default&improve_bitrate=1";
//
//                        System.out.println("wojfdjhfdjh 111 " + ddurl);


                        //    new downloadFile().Downloading(GetTiktokLinkThroughWebview.this, html, "Tiktok_" + System.currentTimeMillis(), ".mp4");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    progressDialog.dismiss();

                    if (!fromService) {
                        pd.dismiss();
                        iUtils.ShowToast(GetTiktokLinkThroughWebview.this, getResources().getString(R.string.somthing));
                    }


                }

                System.out.println("htmlissss vid_url=" + html + " url=" + myurl);


            }

        }
    }


}