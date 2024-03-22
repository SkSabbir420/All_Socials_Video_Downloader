package com.erickruo.allinonevideodownloader.interfaces;

public interface VideoDownloader {

    String getVideoId(String link);

    void DownloadVideo();
}
