package com.erickruo.allinonevideodownloader.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WorldTimeTask extends AsyncTask<Void, Void, Date> {
    private final String API_URL = "https://worldtimeapi.org/api/timezone/";

    private Context mContext;

    public WorldTimeTask(Context context) {
        mContext = context;
    }

    @Override
    protected Date doInBackground(Void... voids) {
        String timezone = TimeZone.getDefault().getID();
        Date date = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(API_URL + timezone);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            String time = json.toString().split("\"datetime\":\"")[1].split("\",\"")[0];
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(timezone));
            calendar.set(
                    Integer.parseInt(time.substring(0, 4)),
                    Integer.parseInt(time.substring(5, 7)) - 1,
                    Integer.parseInt(time.substring(8, 10)),
                    Integer.parseInt(time.substring(11, 13)),
                    Integer.parseInt(time.substring(14, 16)),
                    Integer.parseInt(time.substring(17, 19))
            );
            date = calendar.getTime();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return date;
    }
}

