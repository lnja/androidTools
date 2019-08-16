package len.tools.android.extend;

import len.tools.android.model.JsonInterface;

import java.util.List;

/**
 * 网络请求返回数据为List类型时，数据回调接口
 */

public interface ListRspInterface<D> extends JsonInterface {
    List<D> getList();
}
