package com.android.uiautomator.client.cmd;

import android.graphics.Rect;

import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;
import com.android.uiautomator.client.Status;
import com.android.uiautomator.core.UiObjectNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xdf
 */
public class GetRect extends CommandBase {
    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            String elementId = (String) args.get("elementId");
            Element el = Elements.getGlobal().getElement(elementId);
            final Rect rect = el.element.getBounds();
            JSONObject res = new JSONObject();
            res.put("x", rect.left);
            res.put("y", rect.top);
            res.put("height", rect.height());
            res.put("width", rect.width());
            return success((Object) res);
        } catch (final UiObjectNotFoundException e) {
            return failed(Status.NoSuchElement);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }
}
