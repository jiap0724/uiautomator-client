package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.Utils;
import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Status;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiObjectNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;


import java.util.List;

/**
 * @author zenzhu
 *
 */
public class Actions extends CommandBase {
    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            List<JSONObject> actions = (List) args.get("actions");
            for (JSONObject action: actions) {
                String type = (String) action.get("type");
                if (type.equals("tap")) {
                    tap(action);
                } else if (type.equals("doubleTap")) {
                    doubleTap(action);
                } else if (type.equals("press")) {
                    press(action);
                } else if (type.equals("pinch")) {
                    pinch(action);
                } else if (type.equals("drag")) {
                    drag(action);
                }
            }
            return success(null);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }

    private Element getElement(String elementId) throws UiObjectNotFoundException {
        Element el = Elements.getGlobal().getElement(elementId);
        return el;
    }

    private boolean tap(JSONObject args) throws Exception {
        String elementId = (String) args.get("element");
        if (elementId != null) {
            Element el = getElement(elementId);
            return el.tap();
        } else {
            Integer x = (Integer) args.get("x");
            Integer y = (Integer) args.get("y");
            return UiDevice.getInstance().click(x, y);
        }
    }

    private boolean doubleTap(JSONObject args) throws Exception {
        String elementId = (String) args.get("element");
        if (elementId != null) {
            Element el = getElement(elementId);
            return el.doubleTap();
        } else {
            Integer x = (Integer) args.get("x");
            Integer y = (Integer) args.get("y");
            UiDevice device = UiDevice.getInstance();
            device.click(x, y);
            return device.click(x, y);
        }
    }

    private boolean press(JSONObject args) throws Exception {
        String elementId = (String) args.get("element");
        if (elementId != null) {
            Element el = getElement(elementId);
            return el.press();
        } else {
            // TODO
            Integer x = (Integer) args.get("x");
            Integer y = (Integer) args.get("y");
            return UiDevice.getInstance().click(x, y);
        }
    }

    private boolean pinch(JSONObject args) throws Exception {
        String elementId = (String) args.get("element");
        if (elementId == null) {
            throw new Error("Missing 'element' in parameters.");
        }
        Element el = getElement(elementId);
        float scale = (Integer) args.get("scale");
        return el.pinch(scale);
    }

    private boolean drag(JSONObject args) throws Exception {
        String elementId = (String) args.get("element");
        Integer fromX = (Integer) args.get("fromX");
        Integer fromY = (Integer) args.get("fromY");
        Integer toX = (Integer) args.get("toX");
        Integer toY = (Integer) args.get("toY");
        Integer duration = (Integer) args.get("duration");
        if (elementId != null) {
            Element el = getElement(elementId);
            return el.drag(toX, toY, duration);
        } else {
            int steps = duration * 200;
            return UiDevice.getInstance().drag(fromX, fromY, toX, toY, steps);
        }
    }
}
