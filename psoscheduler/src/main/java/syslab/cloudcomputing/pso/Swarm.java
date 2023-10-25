package syslab.cloudcomputing.pso;

import java.util.ArrayList;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Workload;
import syslab.cloudcomputing.utils.Matrix;

public class Swarm {
  final static double w1 = 0.9;
  final static double w2 = 0.4;

  private Matrix globalBestPosition;
  private double globalBestObjectiveValue;

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
      Particle particle = new Particle(this.dataCenter, this.workload, minPosition, maxPosition, maxAbsoluteVelocity);
      particles.add(particle);
    }
  }

  
}
