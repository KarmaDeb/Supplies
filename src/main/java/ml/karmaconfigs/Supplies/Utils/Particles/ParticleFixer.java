package ml.karmaconfigs.Supplies.Utils.Particles;

import ml.karmaconfigs.Supplies.Suministry;
import org.bukkit.Effect;
import org.bukkit.Location;

import java.util.Objects;

/*
GNU LESSER GENERAL PUBLIC LICENSE
                       Version 2.1, February 1999
 Copyright (C) 1991, 1999 Free Software Foundation, Inc.
 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
[This is the first released version of the Lesser GPL.  It also counts
 as the successor of the GNU Library Public License, version 2, hence
 the version number 2.1.]
 */

public class ParticleFixer implements Suministry {

    private final ParticleType particle;

    public ParticleFixer(ParticleType type) {
        this.particle = type;
    }

    public void sendParticle(Location location) {
        switch (particle) {
            case CLOUD:
                Effect eCloud = effectFixer("CLOUD");
                if (eCloud != null) {
                    Objects.requireNonNull(location.getWorld()).playEffect(location, eCloud, 1, 10);
                }/* else {
                    Particle pCloud = particleFixer("CLOUD");
                    if (pCloud != null) {
                        Objects.requireNonNull(location.getWorld()).spawnParticle(pCloud, location, 10);
                    } else {
                        out.send("It was not possible to play cloud particle", WarningLevel.ERROR);
                    }
                }*/
                break;
            case SMOKE:
                Effect eSmoke = effectFixer("SMOKE");
                if (eSmoke != null) {
                    Objects.requireNonNull(location.getWorld()).playEffect(location, eSmoke, 1, 10);
                }/* else {
                    Particle pSmoke = particleFixer("SMOKE");
                    if (pSmoke != null) {
                        Objects.requireNonNull(location.getWorld()).spawnParticle(pSmoke, location, 10);
                    } else {
                        out.send("It was not possible to play smoke particle", WarningLevel.ERROR);
                    }
                }*/
                break;
            case EXPLOSION:
                Effect eExplosion = effectFixer("EXPLOSION_HUGE");
                if (eExplosion != null) {
                    Objects.requireNonNull(location.getWorld()).playEffect(location, eExplosion, 1, 10);
                }/* else {
                    Particle pExplosion = particleFixer("EXPLOSION_HUGE");
                    if (pExplosion != null) {
                        Objects.requireNonNull(location.getWorld()).spawnParticle(pExplosion, location, 10);
                    } else {
                        out.send("It was not possible to play explosion particle", WarningLevel.ERROR);
                    }
                }*/
                break;
        }
    }

    private Effect effectFixer(String effectName) {
        Effect effect = null;
        for (Effect effects : Effect.values()) {
            if (effects.name().contains(effectName)) {
                effect = effects;
                break;
            }
        }
        return effect;
    }

    /*
    private Particle particleFixer(String particleName) {
        Particle particle = null;
        for (Particle particles : Particle.values()) {
            if (particles.name().contains(particleName)) {
                particle = particles;
            }
        }
        return particle;
    }*/
}