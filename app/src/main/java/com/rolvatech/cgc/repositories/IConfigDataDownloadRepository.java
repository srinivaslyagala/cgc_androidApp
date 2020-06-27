package com.rolvatech.cgc.repositories;

public interface IConfigDataDownloadRepository {

    void downloadConfigData(OnLoadDataCallBack onLoadDataCallBack);

    void deleteConfigData(OnLoadDataCallBack onLoadDataCallBack);

    interface OnLoadDataCallBack {

        void onDataDownloaded();

        void onDataDownloadFailure();
    }
}
