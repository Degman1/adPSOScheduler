package syslab.cloudcomputing.pso;

import java.util.ArrayList;
import java.util.HashMap;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;
import syslab.cloudcomputing.simulation.Workload;
import syslab.cloudcomputing.utils.Matrix;

public class PSOSwarm {
  private final double w1 = 0.9;
  private final double w2 = 0.4;

  private final int maxIterations = 600;
  private static final int nSwarms = 5;
  private static final int nParticles = 20;

  private double p_s = 1.0;

  private double w;

  public Matrix globalBestPosition;
  public double globalBestObjectiveValue = 0;
  private ArrayList<Double> objectiveHistory = new ArrayList<Double>();

  public HashMap<Task, VirtualMachine> globalBestTaskVmMapping = new HashMap<Task, VirtualMachine>();

  private ArrayList<Particle> particles;

  private Workload workload;
  private DataCenter dataCenter;

  public PSOSwarm(DataCenter dataCenter, Workload workload) {
    this.dataCenter = dataCenter;
    this.workload = workload;
    this.initializeSwarm(PSOSwarm.nParticles);
  }

  private void initializeSwarm(int nParticles) {
    this.particles = new ArrayList<Particle>();
    for (int i = 0; i < nParticles; i++) {
      Particle particle = new Particle(this.dataCenter, this.workload);
      this.particles.add(particle);
    }

    this.findGlobalBest();
    // TODO remove the below line when benchmarking
    // this.getGlobalBestTaskVmMapping();
  }

  private void updateTaskVmMapping() {
    this.globalBestTaskVmMapping.clear();

    for (int i = 0; i < this.globalBestPosition.getRowsCount(); i++) {
      Task task = workload.getTaskById(i);
      VirtualMachine virtualMachine = dataCenter.getVirtualMachineById((int) this.globalBestPosition.getIndexOfFirstNonZeroColumnForRow(i));
      this.globalBestTaskVmMapping.put(task, virtualMachine);
    }
  }

  public HashMap<Task,VirtualMachine> getGlobalBestTaskVmMapping() {
    this.updateTaskVmMapping();
    return this.globalBestTaskVmMapping;
  }

  public static Result runRepeatedPSOAlgorithm(DataCenter dataCenter, Workload workload) {
    HashMap<Task, VirtualMachine> bestMapping = new HashMap<Task, VirtualMachine>();
    double maxObjective = -1;
    PSOSwarm[] swarms = new PSOSwarm[PSOSwarm.nSwarms];

    for (int i = 0; i < PSOSwarm.nSwarms; i++) {
      PSOSwarm swarm = new PSOSwarm(dataCenter, workload);
      swarms[i] = swarm;
      HashMap<Task, VirtualMachine> mapping = swarm.runPSOAlgorithm();
      if (swarm.globalBestObjectiveValue > maxObjective) {
        maxObjective = swarm.globalBestObjectiveValue;
        bestMapping = mapping;
      }
    }

    return new Result(swarms, maxObjective, bestMapping);
  }

  public HashMap<Task, VirtualMachine> runPSOAlgorithm() {
    for (int iteration = 1; iteration <= this.maxIterations; iteration++) {
      this.runIteration(iteration);
    }

    return this.getGlobalBestTaskVmMapping();
  }

  private void runIteration(int iteration) {
    w = ((this.w1 - this.w2) / this.p_s) + ((this.maxIterations - (double) iteration) / this.maxIterations) * ((this.w1 - (this.w1 - this.w2)) / this.p_s);

    int ss = 0;

    for (Particle p : this.particles) {
      ss += p.runIteration(w);
    }

    this.findGlobalBest();

    this.objectiveHistory.add(this.globalBestObjectiveValue);
    
    this.p_s = ss / (double) this.getNumberParticles();

    if (this.p_s <= 0) {
      this.p_s = 1;
    }

    return;
  }

  private void findGlobalBest() {
    Boolean newBest = false;

    // Find the new global best
    for (Particle p : this.particles) {
      if (p.getPersonalBestObjectiveValue() > this.globalBestObjectiveValue) {
        this.globalBestObjectiveValue = p.getPersonalBestObjectiveValue();
        this.globalBestPosition = p.getPersonalBestPosition().copy();
        newBest = true;
      }
    }

    // If a new best was found, tell each of the particles
    if (newBest == true) {
      for (Particle p : this.particles) {
        p.setGlobalBestPosition(this.globalBestPosition);
      }
    }
  }

  public int getNumberParticles() {
    return this.particles.size();
  }

  public ArrayList<ArrayList<Double>> getParticleObjectiveHistory() {
    ArrayList<ArrayList<Double>> history = new ArrayList<ArrayList<Double>>();

    for (Particle p : this.particles) {
      history.add(p.getObjectiveHistory());
    }

    return history;
  }
}
