package len.tools.android.extend;

/**
 * 网络请求时，UI处理回调接口
 */

public interface RequestUiHandler {

    void onStart(String hint);

    void onError(int errcode, String errMsg);

    void onSuccess();

}
