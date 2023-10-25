package syslab.cloudcomputing.pso;

import java.util.ArrayList;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Workload;
import syslab.cloudcomputing.utils.Matrix;

public class Swarm {
  final static double w1 = 0.9;
  final static double w2 = 0.4;

  private double p_s = 0.0;

  private double w;

  private Matrix globalBestPosition;
  private double globalBestObjectiveValue;

  private ArrayList<Particle> particles;

  private Workload workload;
  private DataCenter dataCenter;

  public Swarm(DataCenter dataCenter, Workload workload, int nParticles, int minPosition, int maxPosition, int maxAbsoluteVelocity) {
    this.dataCenter = dataCenter;
    this.workload = workload;
    this.initializeSwarm(nParticles);
  }

  private void initializeSwarm(int nParticles) {
    ArrayList<Particle> particles = new ArrayList<Particle>();
    for (int i = 0; i < nParticles; i++) {
      Particle particle = new Particle(this.dataCenter, this.workload);
      particles.add(particle);
    }

    // TODO set global best position and objective value
  }

  public void runIteration() {
    // TODO calculate weight
    w = ...;
    int ss = 0;

    for (Particle p : this.particles) {
      ss += p.runIteration(w);
    }

    // TODO set global best position and objective value
    
    this.p_s = ss / (double) this.getNumberParticles();

    if (this.p_s <= 0) {
      this.p_s = 1;
    }

    return;
  }

  public int getNumberParticles() {
    return this.particles.size();
  }
}
