package len.tools.android.extend;

/**
 * 网络请求时，ListView类型UI处理回调接口
 */

public interface ListViewUiHandler extends RequestUiHandler {
    void onListRspSuccess(ListRspInterface<?> listRsp, int pageNum, int pageSize);
}
