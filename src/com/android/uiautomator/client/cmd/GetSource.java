package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xdf
 *
 */
public class GetSource extends CommandBase {
    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            UiDevice.getInstance();
            return success(true);
        } catch (final Exception e) {
            return failed("UnknownError");
        }
    }
}
