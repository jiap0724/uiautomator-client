package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;
import com.android.uiautomator.client.Status;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xdf
 *
 */
public class ClearText extends CommandBase {
    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            String elementId = (String) args.get("elementId");
            Element el = Elements.getGlobal().getElement(elementId);
            el.clearText();
            UiDevice.getInstance().clearLastTraversedText();
            String text = el.getText();
            for (int i=0;i<text.length();i++) {
                UiDevice.getInstance().pressDelete();
            }
            return success(true);
        } catch (final UiObjectNotFoundException e) {
            return failed(Status.NoSuchElement);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }
}