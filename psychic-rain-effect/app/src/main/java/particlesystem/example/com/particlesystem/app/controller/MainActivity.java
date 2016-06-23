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

package particlesystem.example.com.particlesystem.app.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import particlesystem.example.com.particlesystem.R;
import particlesystem.example.com.particlesystem.app.view.PushPullDoorView;
import particlesystem.example.com.particlesystem.lib.initializers.ParticleSystem;

public class MainActivity extends Activity {

    private boolean isFirstTime = true;

    private ParticleSystem mRainSystemTop;
    private ParticleSystem mRainSystemLeft;
    private ParticleSystem mRainDropSystem;

    private int mCurrentHomeIndex = 0;

    private RelativeLayout mParentView;

    private int mScreenHeight;

    private PushPullDoorView mPullDoorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        mParentView = (RelativeLayout) findViewById(R.id.mainview_id);
        mScreenHeight = getScreenHeight();

        mPullDoorView = (PushPullDoorView)findViewById(R.id.pull_down_view);
        mPullDoorView.setMainActivity(this);
    }


    /** Called when the activity is first created. */
    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTime) {
            mParentView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mPullDoorView.dragDownViewAuto();
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
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    private void startRunEffect(boolean isPullUp) {
        //rain
        if (mRainSystemLeft == null) {
            mRainSystemLeft = new ParticleSystem(MainActivity.this, 200, R.drawable.rainline, 10000);
            mRainSystemLeft = mRainSystemLeft.setExactlyScale(0.4f)
                    .setAcceleration(0.000013f, 90)
                    .setInitialRotationRange(true, 150)
                    .setSpeedByComponentsRange(0.8f, 0.8f, 0.8f, 0.8f)
                    .setParticleAlpha(0.8f);
            //set gravity = 10 make sure the emit covery all the screen.
            mRainSystemLeft.emitWithGravity(mParentView, Gravity.LEFT, 20);

        }
        if (mRainSystemTop == null) {
            mRainSystemTop = new ParticleSystem(MainActivity.this, 200, R.drawable.rainline, 10000);
            mRainSystemTop = mRainSystemTop.setExactlyScale(0.4f)
                    .setAcceleration(0.000013f, 90)
                    .setInitialRotationRange(true, 150)
                    .setSpeedByComponentsRange(0.8f, 0.8f, 0.8f, 0.8f)
                    .setParticleAlpha(0.8f);
            //set gravity = 10 make sure the emit covery all the screen.
            mRainSystemTop.emitWithGravity(mParentView, Gravity.TOP, 20);

        }

        //rain drop

        if (mRainDropSystem == null && isPullUp) {
            mRainDropSystem = new ParticleSystem(MainActivity.this, 200, R.drawable.raindrop2, 30000);
            mRainDropSystem = mRainDropSystem.setSpeedRelatedSize(0.05f, 0.1f, 0.5f, 0.000013f)
                    .setFadeOut(200).setIsNeedMerge(true);
            //set gravity = 10 make sure the emit covery all the screen.
            mRainDropSystem.emitWithGravity(mParentView, 10, 20);
        }

    }

    public void stopParticle() {
        if (mRainSystemLeft != null) {
            mRainSystemLeft.stopEmitting();
            mRainSystemTop.stopEmitting();

            mRainSystemLeft.recycleUsedParticle();
            mRainSystemTop.recycleUsedParticle();
            mRainSystemLeft = null;
            mRainSystemTop = null;
        }
        if (mRainDropSystem != null) {
            mRainDropSystem.stopEmitting();
            mRainDropSystem.recycleUsedParticle();
            mRainDropSystem = null;
        }
    }

    public void stopRainDrop(){
        if (mRainDropSystem != null){
            mRainDropSystem.stopEmitting();
            mRainDropSystem.recycleUsedParticle();
            mRainDropSystem = null;
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


    public void resumeParticle2(){
        if (mPullDoorView != null && mPullDoorView.isStateBottom()){
            if (mRainDropSystem == null){
                mRainDropSystem = new ParticleSystem(this, 200, R.drawable.raindrop2, 30000);
                mRainDropSystem = mRainDropSystem.setSpeedRelatedSize(0.05f,0.1f, 0.5f,0.000013f)
                        .setFadeOut(200).setIsNeedMerge(true);
                //set gravity = 10 make sure the emit covery all the screen.
                mRainDropSystem.emitWithGravity(mPullDoorView, 10, 20);
            }
        }
    }
}
