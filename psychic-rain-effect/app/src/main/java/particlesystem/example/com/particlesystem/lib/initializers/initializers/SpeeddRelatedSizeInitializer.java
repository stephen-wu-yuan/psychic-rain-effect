package particlesystem.example.com.particlesystem.lib.initializers.initializers;

import java.util.Random;

import particlesystem.example.com.particlesystem.lib.initializers.Particle;

/**
 * the speed is related the particle size(scale).
 * the scale range is 0.1 to 0.5;
 * when the scale is low or equal 0.3,we will set the
 * the method is special to the effect like rain drop.so the speedY will set 0;
 */
public class SpeeddRelatedSizeInitializer implements ParticleInitializer {

	private static float speedThreshold = 0.4f;
//	private float mMinSpeedX;
	private float mSpeedY;
//	private float mMinSpeedY = 0;
//	private float mMaxSpeedY = 0;

	private float mMaxScale;
	private float mMinScale;

	private float mAcceleration;

	public SpeeddRelatedSizeInitializer(float speedY,float minScale,float maxScale,float acceleration) {
		mSpeedY = speedY;
		mMinScale = minScale;
		mMaxScale = maxScale;
		mAcceleration = acceleration;
	}

	@Override
	public void initParticle(Particle p, Random r) {

		//init random scale of particle
		float scale = r.nextFloat()*(mMaxScale-mMinScale) + mMinScale;
		p.mScale = scale;

		//according to particle's scale,set the particle's speed

		p.mSpeedX = 0;
		if (scale <= speedThreshold){
			//set the speed is 0,and acceleration is 0
			p.mSpeedY = 0;
			p.mAccelerationX = 0;
			p.mAccelerationY = 0;
		}
		else{
			p.mSpeedY = mSpeedY;

			//acceleration will different according to the scale.
           // accelaration = sin((scale - 0.3)*5*PI/2)
			p.mAccelerationX = 0;
			p.mAccelerationY = mAcceleration * (float)Math.sin(((p.mScale - speedThreshold) * 5 * Math.PI)/2);

		}


	}

}
