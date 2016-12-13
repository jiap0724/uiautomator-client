package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.Utils;
import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Status;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiObjectNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xdf
 *
 */
public class Alert extends CommandBase {
    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            String action = (String) args.get("action");
            Utils.output("-----------------");
            Utils.output(action);
            Utils.output("-----------------");
            if (action.equals("accept")) {
                return acceptAlert();
            } else if (action.equals("dismiss")) {
                return dismissAlert();
            } else if (action.equals("get")) {
                return getAlertText();
            } else if (action.equals("post")) {
                String text = (String) args.get("text");
                return sendAlertKeys(text);
            } else {
                return failed(Status.UnknownCommand);
            }
        } catch (final UiObjectNotFoundException e) {
            return failed(Status.NoAlertOpenError);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }

    public UiObject getAlertView() throws UiObjectNotFoundException {
        UiObject alertView = new UiObject(new UiSelector().className("android.app.AlertDialog"));
        if (!alertView.exists()) {
            throw new UiObjectNotFoundException("Alert Dialog does not exists!");
        }
        return alertView;
    }

    public String acceptAlert() throws Exception {
        UiObject alertView = getAlertView();
        alertView.clickBottomRight();
        return success(null);
    }

    public String dismissAlert() throws Exception {
        UiObject alertView = getAlertView();
        alertView.clickTopLeft();
        return success(null);
    }

    public String getAlertText() throws Exception {
        UiObject alertView = getAlertView();
        String text = alertView.getText();
        return success((Object) text);
    }

    public String sendAlertKeys(String text) throws Exception {
        UiObject alertView = getAlertView();
        alertView.setText(text);
        return success(null);
    }
}
