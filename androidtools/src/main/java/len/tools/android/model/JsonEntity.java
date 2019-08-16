package len.tools.android.model;

import len.tools.android.JsonUtils;

public abstract class JsonEntity implements JsonInterface {

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
