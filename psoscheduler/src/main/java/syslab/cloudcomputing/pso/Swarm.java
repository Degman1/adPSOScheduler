package syslab.cloudcomputing.pso;

import java.util.ArrayList;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Workload;

public class Swarm {
  private ArrayList<Particle> particles;

  private Workload workload;
  private DataCenter dataCenter;

  public Swarm(DataCenter dataCenter, Workload workload, int nParticles, int minPosition, int maxPosition, int maxAbsoluteVelocity) {
    this.dataCenter = dataCenter;
    this.workload = workload;
    this.initializeSwarm();
  }

  private initializeSwarm(int nParticles, int minPosition, int maxPosition, int maxAbsoluteVelocity) {
    ArrayList<Particle> particles = new ArrayList<Particle>();
    for (int i = 0; i < nParticles; i++) {
      Particle partical = new Particle(this.dataCenter, this.workload, minPosition, maxPosition, maxAbsoluteVelocity);
      particles.add(particle);
    }
  }

  
}
