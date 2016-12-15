package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.Utils;
import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Status;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiWatcher;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiObjectNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author zenzhu
 *
 */
public class Alert extends CommandBase {

    UiDevice device = UiDevice.getInstance();

    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            String action = (String) args.get("action");
            if (action.equals("accept")) {
                acceptAlert();
            } else if (action.equals("dismiss")) {
                dismissAlert();
            } else if (action.equals("registerAutoAcceptAlerts")) {
                registerAlertWatcher("accept");
            } else if (action.equals("registerAutoDismissAlerts")) {
                registerAlertWatcher("dismiss");
            } else {
                throw new Exception("Not support action: " + action);
            }
            return success(true);
        } catch (final UiObjectNotFoundException e) {
            return failed(Status.NoAlertOpenError);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }

    private Void registerAlertWatcher(final String alertType) {
        UiWatcher closeAlertWatcher = new UiWatcher() {
            @Override
            public boolean checkForCondition() {
                try {
                    UiObject alertButton = getAlertButton(alertType);
                    alertButton.click();
                    return true;
                } catch (final Exception e) {
                    return false;
                }
            }
        };
        device.registerWatcher("CLOSE_ALERT_WATCHER", closeAlertWatcher);
        device.runWatchers();
        return null;
    }

    private UiObject getAlertButton(String alertType) throws UiObjectNotFoundException, Exception {
        int buttonIndex;
        if (alertType.equals("accept")) {
            buttonIndex = 1;
        } else if (alertType.equals("dismiss")) {
            buttonIndex = 0;
        } else {
            throw new Exception("alertType can only be 'accept' or 'dismiss'");
        }
        UiObject alertButton = new UiObject(new UiSelector()
                .className("android.widget.Button")
                .clickable(true)
                .checkable(false)
                .index(buttonIndex));

        if (!alertButton.exists()) {
            throw new UiObjectNotFoundException("Alert Dialog does not exist.");
        }
        return alertButton;
    }

    private boolean acceptAlert() throws UiObjectNotFoundException, Exception {
        return getAlertButton("accept").click();
    }

    private boolean dismissAlert() throws UiObjectNotFoundException, Exception {
        return getAlertButton("dismiss").click();
    }
}
