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
public class GetProperties extends CommandBase {
    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            String elementId = (String) args.get("elementId");
            Element el = Elements.getGlobal().getElement(elementId);
            final Rect rect = el.element.getBounds();
            JSONObject size = new JSONObject();
            size.put("width", rect.width());
            size.put("height", rect.height());
            size.put("centerX", rect.centerX());
            size.put("centerY", rect.centerY());
            JSONObject origin = new JSONObject();
            origin.put("x", rect.left);
            origin.put("y", rect.top);
            JSONObject text = new JSONObject();
            text.put("text", el.element.getText());
            JSONObject description = new JSONObject();
            description.put("description", el.element.getContentDescription());
            JSONObject enabled = new JSONObject();
            enabled.put("enabled", el.element.isEnabled());
            JSONObject checkable = new JSONObject();
            checkable.put("checkable", el.element.isCheckable());
            JSONObject checked = new JSONObject();
            checked.put("checked", el.element.isChecked());
            JSONObject clickable = new JSONObject();
            clickable.put("clickable", el.element.isClickable());
            JSONObject focusable = new JSONObject();
            focusable.put("focusable", el.element.isFocusable());
            JSONObject focused = new JSONObject();
            focused.put("focused", el.element.isFocused());
            JSONObject longClickable = new JSONObject();
            longClickable.put("longClickable", el.element.isLongClickable());
            JSONObject scrollable = new JSONObject();
            scrollable.put("scrollable", el.element.isScrollable());
            JSONObject selected = new JSONObject();
            selected.put("selected", el.element.isSelected());


            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("size", size);
            props.put("text", text);
            props.put("description", description);
            props.put("enabled", enabled);
            props.put("checkable", checkable);
            props.put("checked", checked);
            props.put("clickable", clickable);
            props.put("focusable", focusable);
            props.put("focused", focused);
            props.put("longClickable", longClickable);
            props.put("scrollable", scrollable);
            props.put("selected", selected);

            return success(props.toString());
        } catch (final UiObjectNotFoundException e) {
            return failed(Status.NoSuchElement);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }
}
