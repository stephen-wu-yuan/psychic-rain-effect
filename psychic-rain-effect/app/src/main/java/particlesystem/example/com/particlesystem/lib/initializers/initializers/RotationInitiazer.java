package particlesystem.example.com.particlesystem.lib.initializers.initializers;

import java.util.Random;

import particlesystem.example.com.particlesystem.lib.initializers.Particle;

public class RotationInitiazer implements ParticleInitializer {

	private int mMinAngle;
	private int mMaxAngle;

	private boolean isExactlyAngle = false;
	private int exactlyAngle = 0;

	public RotationInitiazer(int minAngle, int maxAngle) {
		mMinAngle = minAngle;
		mMaxAngle = maxAngle;
	}

	public RotationInitiazer(boolean isExactlyAngle, int exactlyAngle) {
		this.isExactlyAngle = isExactlyAngle;
		this.exactlyAngle = exactlyAngle;
	}

	@Override
	public void initParticle(Particle p, Random r) {
		if (isExactlyAngle){
			p.mInitialRotation = exactlyAngle;
		}
		else {
			int value = r.nextInt(mMaxAngle-mMinAngle)+mMinAngle;
			p.mInitialRotation = value;
		}

	}

}
