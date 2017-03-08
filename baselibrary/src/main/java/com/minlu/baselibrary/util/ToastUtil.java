package com.minlu.baselibrary.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast toast;

	public static void showToast(Context context, String string) {
		if(toast == null){
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		toast.setText(string);
		toast.show();
	}

}
