package com.android.uiautomator.client.cmd;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.client.CommandBase;

public class GetDisplayHeight extends CommandBase
{
	@Override
	public String execute(JSONObject args) throws JSONException {
		try {
			//获取高度
			Integer height = UiDevice.getInstance().getDisplayHeight();
			
			return success(height);
			
		} catch(JSONException e) {
			
			return failed("UnknownError");
		}
	}
}
