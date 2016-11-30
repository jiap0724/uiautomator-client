package com.android.uiautomator.client.cmd;

import android.view.KeyEvent;
import com.android.uiautomator.client.CommandBase;
import com.android.uiautomator.client.Element;
import com.android.uiautomator.client.Elements;
import com.android.uiautomator.client.Status;
import com.android.uiautomator.client.XmlUtils.InteractionController;
import com.android.uiautomator.client.XmlUtils.UiAutomatorBridge;
import com.android.uiautomator.core.UiObjectNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

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
            if (el.getText().isEmpty()) {
                return success(true);
            }
            if (hasHintText(el)) {
                return success(true);
            }
            if (sendDeleteKeys(el)) {
                return success(true);
            }
            if (!el.getText().isEmpty()) {
                if (hasHintText(el)) {
                    System.out.println("The text should be the hint text");
                } else if (!el.getText().isEmpty()) {
                    System.out.println("oh my god. Can't clear the Text");
                }
                return success(true);
            }
            return success(true);
        } catch (final UiObjectNotFoundException e) {
            return failed(Status.NoSuchElement);
        } catch (final Exception e) {
            return failed(Status.UnknownError);
        }
    }

    private boolean sendDeleteKeys(Element el)
        throws UiObjectNotFoundException, IllegalAccessException,
        InvocationTargetException, NoSuchMethodException {
        String tempTextHolder = "";

        while (!el.getText().isEmpty() && !tempTextHolder.equalsIgnoreCase(el.getText())) {
            // focus the textbox
            el.click();

            for (int key : new int[] { KeyEvent.KEYCODE_DEL, KeyEvent.KEYCODE_FORWARD_DEL }) {
                tempTextHolder = el.getText();
                final int length = tempTextHolder.length();
                for (int count = 0; count < length; count++) {
                    try {
                        InteractionController interactionController = UiAutomatorBridge.getInstance().getInteractionController();
                        interactionController.sendKey(key, 0);
                    } catch (Exception e) {
                        System.out.println("UiAutomatorBridge.getInteractionController error happen!");
                    }
                }
            }
        }

        return el.getText().isEmpty();
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
            InteractionController interactionController = UiAutomatorBridge.getInstance().getInteractionController();
            interactionController.sendKey(KeyEvent.KEYCODE_DEL, 0);
            interactionController.sendKey(KeyEvent.KEYCODE_FORWARD_DEL, 0);
        } catch (Exception e) {
            System.out.println("UiAutomatorBridge.getInteractionController error happen!");
        }

        return currText.equals(el.getText());
    }
}