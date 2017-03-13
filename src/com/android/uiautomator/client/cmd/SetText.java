package com.android.uiautomator.client.cmd;

import com.android.uiautomator.client.Status;
import com.android.uiautomator.client.charsetUtils.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiSelector;

import com.android.uiautomator.core.UiObjectNotFoundException;

import java.nio.charset.Charset;

/**
 * @author xdf
 */
public class SetText extends CommandBase {
    @Override
    public String execute(JSONObject args) throws JSONException {
        try {

            String elementId = (String) args.get("elementId");
            String text = (String) args.get("text");

            Element element = null;

            if (elementId == null || "".equals(elementId)) {
                element = Elements.getGlobal().getElement(new UiSelector().focused(true));
            } else {
                element = Elements.getGlobal().getElement(elementId);
            }

            boolean needPressEnter = false;

            if (text.endsWith("\\n")) {
                needPressEnter = true;
                text = text.replace("\\n", "");
            }

            Charset UTF7 = new CharsetProvider().charsetForName("X-MODIFIED-UTF-7");
            Charset ASCII = Charset.forName("US-ASCII");

            byte[] encoded = text.getBytes(UTF7);
            String str = new String(encoded, ASCII);

            boolean result = element.setText(str);

            if (needPressEnter) {
                final UiDevice d = UiDevice.getInstance();
                d.pressEnter();
            }
            return success(result);
        } catch (final UiObjectNotFoundException e) {
            return failed(Status.NoSuchElement);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }
}
