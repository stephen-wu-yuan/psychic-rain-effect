package particlesystem.example.com.particlesystem.lib.initializers.initializers;

import java.util.Random;

import particlesystem.example.com.particlesystem.lib.initializers.Particle;

public interface ParticleInitializer {

	void initParticle(Particle p, Random r);

}
