package chemistry;

public interface GRN {
	world.Decision getDecisionAtTime(double time, world.Perception p);
}
