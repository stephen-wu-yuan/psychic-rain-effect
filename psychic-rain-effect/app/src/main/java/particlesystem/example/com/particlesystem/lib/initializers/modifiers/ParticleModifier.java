package particlesystem.example.com.particlesystem.lib.initializers.modifiers;

import particlesystem.example.com.particlesystem.lib.initializers.Particle;
public interface ParticleModifier {

	/**
	 * modifies the specific value of a particle given the current miliseconds
	 * @param particle
	 * @param miliseconds
	 */
	void apply(Particle particle, long miliseconds);

}
