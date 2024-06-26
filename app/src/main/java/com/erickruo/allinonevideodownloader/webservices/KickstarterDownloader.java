package com.erickruo.allinonevideodownloader.webservices;

import static com.erickruo.allinonevideodownloader.webservices.DownloadVideosMain.Mcontext;
import static com.erickruo.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.erickruo.allinonevideodownloader.webservices.DownloadVideosMain.pd;

import android.content.Context;
import android.os.AsyncTask;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.utils.DownloadFileMain;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class KickstarterDownloader {

    private final Context context;
    private final String VideoURL;

    public KickstarterDownloader(Context context, String vid) {
        this.context = context;
        VideoURL = vid;
    }

    public void DownloadVideo() {
        new CallKickstarterData().execute(VideoURL);
    }


    public static class CallKickstarterData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }
                System.out.println("myresponseis111 exp166 " + document);

                String data = "";
                ArrayList<String> arrayList = new ArrayList<>();

                Elements elements = document.select("div");
                for (Element element : elements) {
                    if (element.attr("class").equals("bg-grey-100")) {
                        //Save As you want to

                        String replaceString = element.getElementsByAttribute("data-initial").attr("data-initial").replace("&quot;", "\"");

                        JSONObject obj = new JSONObject(replaceString);
                        String jsonArray = "";
                        if (obj.getJSONObject("project").getJSONObject("video").has("high")) {
                            jsonArray = obj.getJSONObject("project").getJSONObject("video").getJSONObject("videoSources").getJSONObject("high").getString("src");
                        } else {
                            jsonArray = obj.getJSONObject("project").getJSONObject("video").getJSONObject("videoSources").getJSONObject("base").getString("src");

                        }

                        DownloadFileMain.startDownloading(Mcontext, jsonArray, "Kickstarter_" + System.currentTimeMillis(), ".mp4");


                    }
                }


            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


}
