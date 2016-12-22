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
		element.click();
		return true;
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean doubleTap() throws UiObjectNotFoundException, Exception {
		element.click();
		Thread.sleep(100);
		element.click();
		return true;
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean pinch(String direction, int percent, int steps) throws UiObjectNotFoundException {
		if (direction.equals("in")) {
			element.pinchIn(percent, steps);
		} else if (direction.equals("out")) {
			element.pinchOut(percent, steps);
		}
		return true;
	}

	/**
	 * @return res
	 * @throws UiObjectNotFoundException
	 */
	public boolean drag(int x, int y, int steps) throws UiObjectNotFoundException {
		element.dragTo(x, y, steps);
		return true;
	}

	public UiObject getUiObject() {
		return this.element;
	}
}
