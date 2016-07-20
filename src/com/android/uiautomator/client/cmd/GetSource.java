package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.core.UiDevice;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xdf
 */
public class GetSource extends CommandBase {

    private static final String dumpFileName = "macaca-dump.xml";

    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            UiDevice.getInstance().dumpWindowHierarchy(dumpFileName);
            return success(true);
        } catch (final Exception e) {
            return failed("UnknownError");
        }
    }
}
