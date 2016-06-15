package particlesystem.example.com.particlesystem.lib.initializers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

class ParticleField extends View {

	private ArrayList<Particle> mParticles;

	private boolean isNeedMerge = false;

	private int orinalParticleHeight = 0;

	public ParticleField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ParticleField(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ParticleField(Context context) {
		super(context);
	}

	public void initMergeParticle(boolean isNeedMerge,int orinalParticleHeight){
		this.isNeedMerge = isNeedMerge;
		this.orinalParticleHeight = orinalParticleHeight;
	}
	public void setParticles(ArrayList<Particle> particles) {
		mParticles = particles;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Draw all the particles
        try{
            if (isNeedMerge){
                for (int i=0; i<mParticles.size(); i++) {
                    //merge the particle
                    judgeOverlapParticle(mParticles.get(i), i);
                }
            }
        }
        catch (Exception e){

        }

        for (int i=0; i<mParticles.size(); i++) {

			try{
				if (mParticles.get(i) != null){
					if (mParticles.get(i).mImage.isRecycled()){
						break;
					}
					mParticles.get(i).draw(canvas);
				}
			}
			catch (Exception e){
			}
        }
	}


	private void judgeOverlapParticle(Particle currentParticle,int index){
		for (int j = 0; j< mParticles.size(); j++){
			if (j != index && currentParticle.getIsNeedDraw()){
				Particle particleItem = mParticles.get(j);
				if ((currentParticle.mCurrentY - particleItem.mCurrentY <= orinalParticleHeight) ||
						(particleItem.mCurrentY - currentParticle.mCurrentY <= currentParticle.getParticleHeight())){
					//need to compare if is overlap
					if(isOverlap((int)currentParticle.mCurrentX,(int)particleItem.mCurrentX,(int)currentParticle.mCurrentY,
							(int)particleItem.mCurrentY,currentParticle.getParticleWidth(),particleItem.getParticleWidth(),
							currentParticle.getParticleHeight(),particleItem.getParticleHeight())){
						if (currentParticle.mCurrentY - particleItem.mCurrentY <= orinalParticleHeight){
							particleItem.setIsNeedDraw(false);
						}
//					else{
//						currentParticle.setIsNeedDraw(false);
//					}
					}
				}
			}

		}
	}

	private  boolean isOverlap(int x1, int x2, int y1, int y2, int w1,
							   int w2, int h1, int h2){
		if(x1 < x2 && x1 + w1 <= x2)
		{
			return false;
		}
		else if(x1 > x2 && x1 >= x2 + w2)
		{
			return false;
		}
		else if (y1 < y2 && y1 + h1 <= y2)
		{
			return false;
		}
		else if(y1 > y2 && y1 >= y2 + h2)
		{
			return false;
		}
		return true;
	}

}
