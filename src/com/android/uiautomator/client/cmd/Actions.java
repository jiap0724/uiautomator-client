package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;
import com.android.uiautomator.client.Status;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * @author zenzhu
 *
 */
public class Actions extends CommandBase {

    UiDevice device = UiDevice.getInstance();

    @Override
    public String execute(JSONObject args) throws JSONException {
        try {
            JSONArray actions = args.getJSONArray("actions");
            List<JSONArray> queue = new ArrayList<JSONArray>();
            String lastGesture = "";
            for (int i = 0; i < actions.length(); i++) {
                JSONObject action = actions.getJSONObject(i);
                String type = action.optString("type");

                if (type.isEmpty()) {
                    continue;
                }
                if (type.equals(lastGesture)) {
                    if (queue.size() > 0) {
                        JSONArray arr = queue.get(queue.size() - 1);
                        arr.put(action);
                    }
                } else {
                    JSONArray arr = new JSONArray();
                    arr.put(action);
                    queue.add(arr);
                }

                lastGesture = type;
            }

            for (JSONArray action: queue) {
                JSONObject first = action.getJSONObject(0);
                String type = first.optString("type");
                boolean result = false;
                if (type.equals("tap")) {
                    result = tap(action);
                } else if (type.equals("doubleTap")) {
                    result = doubleTap(action);
                } else if (type.equals("press")) {
                    result = press(action);
                } else if (type.equals("pinch")) {
                    result = pinch(action);
                } else if (type.equals("drag")) {
                    result = drag(action);
                }
                if(!result) {
                    throw new Exception("Fail to execute action: " + type);
                }
            }
            return success(true);
        } catch (final Exception e) {
            e.printStackTrace();
            return failed(Status.UnknownError);
        }
    }

    private Element getElement(String elementId) throws UiObjectNotFoundException {
        Element el = Elements.getGlobal().getElement(elementId);
        return el;
    }

    private boolean tap(JSONObject args) throws Exception {
        String elementId = args.optString("element");
        if (elementId != "") {
            Element el = getElement(elementId);
            return el.tap();
        } else {
            int x = args.optInt("x");
            int y = args.optInt("y");
            return device.click(x, y);
        }
    }

    private boolean tap(JSONArray actions) throws Exception {
        for (int i = 0; i < actions.length(); i++) {
            JSONObject action = actions.getJSONObject(i);
            if(!tap(action)) {
                return false;
            }
        }
        return true;
    }

    private boolean doubleTap(JSONObject args) throws Exception {
        String elementId = args.optString("element");
        if (elementId != "") {
            Element el = getElement(elementId);
            return el.doubleTap();
        } else {
            int x = args.optInt("x");
            int y = args.optInt("y");
            device.click(x, y);
            Thread.sleep(100);
            device.click(x, y);
            return true;
        }
    }

    private boolean doubleTap(JSONArray actions) throws Exception {
        for (int i = 0; i < actions.length(); i++) {
            JSONObject action = actions.getJSONObject(i);
            if(!doubleTap(action)) {
                return false;
            }
        }
        return true;
    }

    private boolean press(JSONObject args) throws Exception {
        String elementId = args.optString("element");
        double duration = args.optDouble("duration", 1);
        int steps = args.optInt("steps");
        if (steps == 0) {
            duration = args.optDouble("duration", 1);
            steps = (int) Math.round(duration * 40);
        }
        if (elementId != "") {
            Element el = getElement(elementId);
            Rect elRect = el.getUiObject().getBounds();
            return device.swipe(elRect.centerX(), elRect.centerY(),  elRect.centerX(), elRect.centerY(), steps);
        } else {
            int x = args.optInt("x");
            int y = args.optInt("y");
            return device.swipe(x, y, x, y, steps);
        }
    }

    private boolean press(JSONArray actions) throws Exception {
        for (int i = 0; i < actions.length(); i++) {
            JSONObject action = actions.getJSONObject(i);
            if(!press(action)) {
                return false;
            }
        }
        return true;
    }

    private boolean pinch(JSONObject args) throws Exception {
        String elementId = args.optString("element");
        if (elementId == "") {
            throw new Exception("Missing 'element' in parameters.");
        }
        Element el = getElement(elementId);
        String direction = args.optString("direction");
        int percent = args.optInt("percent", 100);
        int steps = args.optInt("steps", 100);
        return el.pinch(direction, percent, steps);
    }

    private boolean pinch(JSONArray actions) throws Exception {
        for (int i = 0; i < actions.length(); i++) {
            JSONObject action = actions.getJSONObject(i);
            if(!pinch(action)) {
                return false;
            }
        }
        return true;
    }

    private boolean drag(JSONObject args) throws Exception {
        String elementId = args.optString("element");
        int fromX = args.optInt("fromX");
        int fromY = args.optInt("fromY");
        int toX = args.optInt("toX");
        int toY = args.optInt("toY");
        int steps = args.optInt("steps");
        if (steps == 0) {
            double duration = args.optDouble("duration", 1);
            steps = (int) Math.round(duration * 40);
        }
        if (elementId != "") {
            Element el = getElement(elementId);
            return el.drag(toX, toY, steps);
        } else {
            return device.drag(fromX, fromY, toX, toY, steps);
        }
    }

    private boolean drag(JSONArray actions) throws Exception {
        if (actions.length() == 1) {
            JSONObject action = actions.getJSONObject(0);
            return drag(action);
        }

        Point[] allPoint = new Point[actions.length() + 1];
        int steps = 0;
        for (int i = 0; i < actions.length(); i++) {
            JSONObject action = actions.getJSONObject(i);
            if (i == 0) {
                String elementId = action.optString("element");
                steps = action.optInt("steps");
                if (steps == 0) {
                    double duration = action.optDouble("duration", 1);
                    steps = (int) Math.round(duration * 40);
                }
                if (elementId != "") {
                    Element el = getElement(elementId);
                    Rect elRect = el.getUiObject().getBounds();
                    Point p = new Point(elRect.centerX(), elRect.centerY());
                    allPoint[0] = p;
                } else {
                    int fromX = action.optInt("fromX");
                    int fromY = action.optInt("fromY");
                    Point p = new Point(fromX, fromY);
                    allPoint[0] = p;
                }
            }
            int toX = action.optInt("toX");
            int toY = action.optInt("toY");
            Point p = new Point(toX, toY);
            allPoint[i + 1] = p;
        }
        return device.swipe(allPoint, steps);
    }
}
