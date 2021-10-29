package com.boints.systemoverlay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingWnd extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settingwnd);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //int width = dm.widthPixels;
        //int height = dm.heightPixels;
        //getWindow().setLayout((int)width, (int)height);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        System.out.println("pos:setting window close clicked ################################");
        System.out.println("pos:setting window close clicked ################################");
        System.out.println("pos:setting window close clicked ################################");

        ImageButton btn_close = (ImageButton) findViewById(R.id.ic_btn_close_setting);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "HIDE SETTING", Toast.LENGTH_SHORT).show();
                //WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                //wm.removeView(MainActivity.mview);
            }
        });
    }
}
