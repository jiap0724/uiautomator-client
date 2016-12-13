package com.android.uiautomator.client;

import com.android.uiautomator.core.Configurator;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;

/**
 * @author xdf
 *
 */
public class Element {
	/**
	 *
	 */
	public UiObject element;
	/**
	 *
	 */
	public String id;

	/**
	 * @param id
	 * @param element
	 */
	Element(String id, UiObject element) {
		this.element = element;
		this.id = id;
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean click() throws UiObjectNotFoundException {
		return element.click();
	}

	/**
	 * @return res
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public String getText() throws UiObjectNotFoundException {
		return element.getText();
	}

	/**
	 * @param text
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean setText(String text) throws UiObjectNotFoundException {
		Configurator config = Configurator.getInstance();
		config.setKeyInjectionDelay(20);
		Boolean success = element.setText(text);
		config.setKeyInjectionDelay(0);
		return success;
	}

	/**
	 * @throws UiObjectNotFoundException
	 */
	public void clearText() throws UiObjectNotFoundException {
		element.clearTextField();
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean isDisplayed() throws UiObjectNotFoundException {
		return element.waitForExists(500);
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean tap() throws UiObjectNotFoundException {
		return element.click();
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean doubleTap() throws UiObjectNotFoundException {
		element.click();
		return element.click();
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean press() throws UiObjectNotFoundException {
		return element.longClick();
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean pinch(float scale) throws UiObjectNotFoundException {
		if (scale > 1) {
			int percent = Math.round(100 * (scale - 1) / scale);
			return element.pinchOut(percent, 100);
		} else if (scale < 1) {
			int percent = Math.round(100 * (1 - scale) / scale);
			return element.pinchIn(percent, 100);
		} else {
			return true;
		}
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean drag(int x, int y, int duration) throws UiObjectNotFoundException {
		// Steps are injected about 5ms apart.
		int steps = duration * 200;
		return element.dragTo(x, y, steps);
	}

	public UiObject getUiObject() {
		return this.element;
	}
}
