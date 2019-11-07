package edu.wisc.entity.normalizer.model;

public class CdriveDownload {
    String download_url;

    public String getDownload_url() {
        return download_url;
    }

    @Override
    public String toString() {
        return "CdriveDownload{" +
                "download_url='" + download_url + '\'' +
                '}';
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
