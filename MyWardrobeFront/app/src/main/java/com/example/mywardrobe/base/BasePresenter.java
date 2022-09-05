package com.example.mywardrobe.base;

public class BasePresenter <V extends BaseActivity, M extends BaseModel> {
    protected V mView;
    protected M mModel;

    /**
     * 绑定View，一般在初始化中调用该方法
     */
    public void attachView(V view) {
        this.mView = view;
    }

    /***
     * 解绑View，一般在onDestroy中调用
     */
    public void detachView() {
        this.mView = null;
    }

    /**
     * View是否绑定
     */
    public boolean isAttachView() {
        return this.mView != null;
    }
}

