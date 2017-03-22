package com.android.uiautomator.client.cmd;

import android.view.KeyEvent;
import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;
import com.android.uiautomator.client.Status;
import com.android.uiautomator.client.charsetUtils.CharsetProvider;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
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

            boolean needPressEnter = false;

            if (text.endsWith("\\n")) {
                needPressEnter = true;
                text = text.replace("\\n", "");
            }

            Charset UTF7 = new CharsetProvider().charsetForName("X-MODIFIED-UTF-7");
            Charset ASCII = Charset.forName("US-ASCII");


            Element element = null;

            String resultText = text;

            if (elementId == null || "".equals(elementId)) {
                try {
                    //if id not exist and has no focus do nothing
                    element = Elements.getGlobal().getElement(new UiSelector().focused(true));
                    args.put("elementId", element.getId());
                } catch (Exception e) {
                    return success(true);
                }

                String currentText = element.getText();
                if (hasHintText(element)) {
                    // if default value has hints ,empty value
                    currentText = "";
                }
                new ClearText().execute(args);
                resultText = currentText + text;
            } else {
                element = Elements.getGlobal().getElement(elementId);
            }

            byte[] encoded = (resultText).getBytes(UTF7);
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

    private boolean hasHintText(Element el)
            throws UiObjectNotFoundException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        // to test if the remaining text is hint text, try sending a single delete key and testing if there is any change.
        String currText = el.getText();

        try {
            if (!el.getUiObject().isFocused()) {
                System.out.println("Could not check for hint text because the element is not focused!");
                return false;
            }
        } catch (final Exception e) {
            System.out.println("Could not check for hint text: " + e.getMessage());
            return false;
        }

        try {
            com.android.uiautomator.client.xmlUtils.InteractionController interactionController = com.android.uiautomator.client.xmlUtils.UiAutomatorBridge.getInstance().getInteractionController();
            interactionController.sendKey(KeyEvent.KEYCODE_DEL, 0);
            interactionController.sendKey(KeyEvent.KEYCODE_FORWARD_DEL, 0);
        } catch (Exception e) {
            System.out.println("UiAutomatorBridge.getInteractionController error happen!");
        }

        return currText.equals(el.getText());
    }
}
