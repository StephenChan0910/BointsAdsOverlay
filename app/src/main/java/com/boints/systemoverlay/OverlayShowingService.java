package com.boints.systemoverlay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.view.LayoutInflater;

public class OverlayShowingService extends Service implements OnTouchListener, OnClickListener {

    public static final int BTN_MENU_RESID = 100;
    public static final int BTN_SIZE = 130;

    private View bottomLeftView;
    private View bottomRightView;

    private ImageButton btn_move;
    private ImageButton btn_menu;
    
    private ImageButton btn_setting;
    private ImageButton btn_close;
    private ImageButton btn_ads;
    private ImageButton btn_ava_ads;
    private ImageButton btn_exit;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;

    private boolean moving;
    private WindowManager wm;
    private WindowManager.LayoutParams bottomLeftParams;

    private boolean bShowMenu;
    private boolean bShowSetting;
    private boolean bShowAvailableAds;
    private View settingsview;
    private View availableadsView;
    private View overlayMenuView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        bShowSetting = false;
        bShowAvailableAds = false;
        bShowMenu = false;

        settingsview = null;
        availableadsView = null;
        overlayMenuView = null;

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // add control overlay buttons
        addMenuButton();

        bottomLeftView = new View(this);
        bottomLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        bottomLeftParams.gravity = Gravity.RIGHT | Gravity.CENTER;
        bottomLeftParams.x = 0;
        bottomLeftParams.y = 0;
        bottomLeftParams.width = 0;
        bottomLeftParams.height = 0;
        wm.addView(bottomLeftView, bottomLeftParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (btn_menu != null) {
            wm.removeView(btn_menu);
            wm.removeView(bottomLeftView);
            btn_menu = null;
            bottomLeftView = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if( v.getId() == R.id.ib_move) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getRawX();
                float y = event.getRawY();

                moving = false;

                int[] location = new int[2];
                btn_move.getLocationOnScreen(location);

                originalXPos = location[0];
                originalYPos = location[1];

                System.out.println("moving = actioin_down"+y);

                offsetX = originalXPos - x;
                offsetY = originalYPos - y;

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                int[] topLeftLocationOnScreen = new int[2];
                bottomLeftView.getLocationOnScreen(topLeftLocationOnScreen);

                //System.out.println("pos:topLeftY=" + topLeftLocationOnScreen[0]);

                float x = event.getRawX();
                float y = event.getRawY();
                System.out.println("pos:originalY =" + topLeftLocationOnScreen[1] + ", curY : "+y);

                WindowManager.LayoutParams params = (LayoutParams) overlayMenuView.getLayoutParams();

                int newX = (int) (offsetX + x);
                int newY = (int) (offsetY + y);

                if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                    return false;
                }

                params.y = newY - (topLeftLocationOnScreen[1]) + BTN_SIZE;
                wm.updateViewLayout(overlayMenuView, params);

                WindowManager.LayoutParams params1 = (LayoutParams) btn_menu.getLayoutParams();
                params1.y = newY - (topLeftLocationOnScreen[1]);
                wm.updateViewLayout(btn_menu, params1);

                moving = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (moving) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateButtonPosition(ImageButton btn_pos, int x, int y)
    {
        if( btn_pos == null ) return;
        WindowManager.LayoutParams params = (LayoutParams) btn_pos.getLayoutParams();
        if( x != -1) params.x = x;
        if( y != -1) params.y = y;
        wm.updateViewLayout(btn_pos, params);
    }

    @Override
    public void onClick(View v) {


        ////////////////////////////////////////////////////////////////////////////////////////////
        switch( v.getId() ) {
            case R.id.ib_exit:
                showOverlayMenu();
                break;
            case R.id.ib_ava_ads:

                break;
            case R.id.ib_move:
                Toast.makeText(this, "Use this to move the ad to the top or bottom of the screen.", Toast.LENGTH_LONG).show();
                break;
            case R.id.ib_ads:
                showAdsettingMenu();
                break;
            case R.id.ib_settings:
                showSettingMenu();
                break;
            case R.id.ib_close:
                showOverlayMenu();
                break;
            case BTN_MENU_RESID:
                showOverlayMenu();
                break;

            default:
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Menu Button
    public void addMenuButton()
    {
        btn_menu = new ImageButton(this);
        btn_menu.setId(BTN_MENU_RESID);
        btn_menu.setImageResource(R.drawable.menu);

        btn_menu.setOnTouchListener(this);
        btn_menu.setOnClickListener(this);

        // set transparent bg and alpha, size
        btn_menu.setBackgroundResource(0);
        btn_menu.setAlpha(.9f);
        btn_menu.setScaleType(ImageView.ScaleType.CENTER_CROP);

        WindowManager.LayoutParams params1 = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params1.gravity = Gravity.RIGHT | Gravity.CENTER;
        params1.x = 0;
        params1.y = 0;
        params1.width = BTN_SIZE;
        params1.height = BTN_SIZE;
        wm.addView(btn_menu, params1);
        btn_menu.setTranslationX(BTN_SIZE/4);

    }

    public void showSettingMenu()
    {
        bShowSetting = !bShowSetting;
        if( settingsview == null ) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.TYPE_APPLICATION_OVERLAY,
                    LayoutParams.FLAG_NOT_FOCUSABLE
                            | LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.RGBA_8888);

            settingsview = MainActivity.mview;
            wm.addView(settingsview, params);
        }

        if( bShowSetting == false )
            settingsview.setVisibility(View.INVISIBLE);
        else
        {
            if( availableadsView != null )
                availableadsView.setVisibility(View.INVISIBLE);
            settingsview.setVisibility(View.VISIBLE);
        }


        ImageButton btn_close = (ImageButton) settingsview.findViewById(R.id.ic_btn_close_setting);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "HIDE SETTING", Toast.LENGTH_SHORT).show();
                bShowSetting = false;
                settingsview.setVisibility(View.INVISIBLE);
            }
        });

    }


    public void showAdsettingMenu()
    {
        bShowAvailableAds = !bShowAvailableAds;
        if( availableadsView == null ) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.TYPE_APPLICATION_OVERLAY,
                    LayoutParams.FLAG_NOT_FOCUSABLE
                            | LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.RGBA_8888);

            availableadsView = MainActivity.mavailableadsView;
            wm.addView(availableadsView, params);
        }

        if( bShowAvailableAds == false )
            availableadsView.setVisibility(View.INVISIBLE);
        else
        {
            if( settingsview != null )
                settingsview.setVisibility(View.INVISIBLE);
            availableadsView.setVisibility(View.VISIBLE);
        }


        ImageButton btn_close = (ImageButton) availableadsView.findViewById(R.id.btn_closead);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "HIDE SETTING", Toast.LENGTH_SHORT).show();
                bShowAvailableAds = false;
                availableadsView.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void showOverlayMenu()
    {
        bShowMenu = !bShowMenu;
        if( overlayMenuView == null ) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.TYPE_APPLICATION_OVERLAY,
                    LayoutParams.FLAG_NOT_FOCUSABLE
                            | LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.RGBA_8888);

            params.gravity = Gravity.RIGHT | Gravity.CENTER;
            params.x = 0;
            params.y = 0;

            overlayMenuView = MainActivity.mOverlayMenuView;
            wm.addView(overlayMenuView, params);
        }

        if( bShowMenu == false )
        {
            overlayMenuView.setVisibility(View.INVISIBLE);
            btn_menu.setVisibility(View.VISIBLE);
        }
        else
        {
            overlayMenuView.setVisibility(View.VISIBLE);
            btn_menu.setVisibility(View.INVISIBLE);
        }

        // close button
        btn_close = (ImageButton) overlayMenuView.findViewById(R.id.ib_close);
        btn_close.setOnClickListener(this);

        // exit button
        btn_exit = (ImageButton) overlayMenuView.findViewById(R.id.ib_exit);
        btn_exit.setOnClickListener(this);

        // move button
        btn_move = (ImageButton) overlayMenuView.findViewById(R.id.ib_move);
        btn_move.setOnClickListener(this);
        btn_move.setOnTouchListener(this);

        // ads button
        btn_ads = (ImageButton) overlayMenuView.findViewById(R.id.ib_ads);
        btn_ads.setOnClickListener(this);

        // setting button
        btn_setting = (ImageButton) overlayMenuView.findViewById(R.id.ib_settings);
        btn_setting.setOnClickListener(this);

        // ava_ads button
        btn_ava_ads = (ImageButton) overlayMenuView.findViewById(R.id.ib_ava_ads);
        btn_ava_ads.setOnClickListener(this);

    }
}
