package com.android.uiautomator.client.cmd;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.client.CommandBase;

public class GetDisplayWidth extends CommandBase
{
	@Override
	public String execute(JSONObject args) throws JSONException {
		try {
			//获取宽度
			Integer width = UiDevice.getInstance().getDisplayWidth();
			
			return success(width);
			
		} catch(JSONException e) {
			
			return failed("UnknownError");
		}
	}
}

