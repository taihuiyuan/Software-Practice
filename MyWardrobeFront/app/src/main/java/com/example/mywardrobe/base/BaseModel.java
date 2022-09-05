package com.example.mywardrobe.base;

import com.example.mywardrobe.utils.RequestManager;

import org.json.JSONObject;

public class BaseModel <P extends BasePresenter> {
    /*两种类型的request*/
    public enum RequestType {
        GET,
        POST;
    }

    public enum RequestDataType {
        ARRAY,
        OBJECT;
    }

    /**
     * 发起request
     */
    public void request(String url, RequestType type, JSONObject params, RequestManager.NetworkListener callback) {
        if (type == RequestType.GET) {
            RequestManager.getInstance().get(url, callback);
        }else {
            RequestManager.getInstance().post(url, params, callback);
        }
    }
}
