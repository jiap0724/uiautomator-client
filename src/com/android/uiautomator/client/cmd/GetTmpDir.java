package com.android.uiautomator.client.cmd;

import android.os.Environment;
import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Status;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author CodeToSurvive1
 */
public class GetTmpDir extends CommandBase {

    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            return success(Environment.getDataDirectory().getPath()+"/local/tmp");
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }
}
