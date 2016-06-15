/**
 * File:		MainActivity.java
 * * Author:		Dongli Zhang
 * * Contact:	dongli.zhang0129@gmail.com
 * *
 * * Copyright (C) Dongli Zhang 2013
 * *
 * * This program is free software;  you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation; either version 2 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY;  without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * * the GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program;  if not, write to the Free Software
 * * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

package particlesystem.example.com.particlesystem.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import particlesystem.example.com.particlesystem.R;
import particlesystem.example.com.particlesystem.lib.initializers.ParticleSystem;

public class MainActivity extends Activity {

    private boolean isFirstTime = true;

    private ParticleSystem rainSystemTop;
    private ParticleSystem rainSystemLeft;
    private ParticleSystem rainDropSystem;

    private int preScrollHomeIndex = 0;
    private int mCurrentHomeIndex = 0;

    private int screenHeight;


    private RelativeLayout parentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);


        parentView = (RelativeLayout) findViewById(R.id.mainview_id);

        screenHeight = getScreenHeight();

    }


    /** Called when the activity is first created. */
    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTime) {
            parentView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    startRunEffect(true);
                }
            }, 600);
            isFirstTime = false;
        }
    }


    protected void onPause() {
        super.onPause();
    }


    private int getScreenHeight() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int height = wm.getDefaultDisplay().getHeight();// 屏幕高度
        return height;
    }

    private void startRunEffect(boolean isPullUp) {
        //rain
        if (rainSystemLeft == null) {
            rainSystemLeft = new ParticleSystem(MainActivity.this, 200, R.drawable.rainline, 10000);
            rainSystemLeft = rainSystemLeft.setExactlyScale(0.4f)
                    .setAcceleration(0.000013f, 90)
                    .setInitialRotationRange(true, 150)
                    .setSpeedByComponentsRange(0.8f, 0.8f, 0.8f, 0.8f)
                    .setParticleAlpha(0.8f);
            //set gravity = 10 make sure the emit covery all the screen.
            rainSystemLeft.emitWithGravity(parentView, Gravity.LEFT, 20);

        }
        if (rainSystemTop == null) {
            rainSystemTop = new ParticleSystem(MainActivity.this, 200, R.drawable.rainline, 10000);
            rainSystemTop = rainSystemTop.setExactlyScale(0.4f)
                    .setAcceleration(0.000013f, 90)
                    .setInitialRotationRange(true, 150)
                    .setSpeedByComponentsRange(0.8f, 0.8f, 0.8f, 0.8f)
                    .setParticleAlpha(0.8f);
            //set gravity = 10 make sure the emit covery all the screen.
            rainSystemTop.emitWithGravity(parentView, Gravity.TOP, 20);

        }

        //rain drop

        if (rainDropSystem == null && isPullUp) {
            rainDropSystem = new ParticleSystem(MainActivity.this, 200, R.drawable.raindrop2, 30000);
            rainDropSystem = rainDropSystem.setSpeedRelatedSize(0.05f, 0.1f, 0.5f, 0.000013f)
                    .setFadeOut(200).setIsNeedMerge(true);
            //set gravity = 10 make sure the emit covery all the screen.
            rainDropSystem.emitWithGravity(parentView, 10, 20);
        }

    }

    public void stopParticle() {
        if (rainSystemLeft != null) {
            rainSystemLeft.stopEmitting();
            rainSystemTop.stopEmitting();

            rainSystemLeft.recycleUsedParticle();
            rainSystemTop.recycleUsedParticle();
            rainSystemLeft = null;
            rainSystemTop = null;
        }
        if (rainDropSystem != null) {
            rainDropSystem.stopEmitting();
            rainDropSystem.recycleUsedParticle();
            rainDropSystem = null;
        }
    }


    public void stopWeatherEffect() {
        stopParticle();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopWeatherEffect();
    }
}
