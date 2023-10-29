package syslab.cloudcomputing.schedule;

import java.util.ArrayList;
import java.util.HashMap;

import syslab.cloudcomputing.pso.PSOSwarm;
import syslab.cloudcomputing.simulation.*;

public class Scheduler {
    public static void main(String[] args) {
        int nVms = 1;
        int nTasks = 3;
        int nParticles = 1;

        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();

        for (int i = 0; i < nVms; i++) {
            vms.add(new VirtualMachine(100));
        }

        DataCenter dataCenter = new DataCenter(vms);

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (int i = 0; i < nTasks; i++) {
            tasks.add(new Task(10));
        }

        Workload workload = new Workload(tasks);

        System.out.println(dataCenter);
        System.out.println(workload);

        PSOSwarm swarm = new PSOSwarm(dataCenter, workload, nParticles);
        System.out.println("Init results:");
        System.out.println(swarm.globalBestPosition);
        System.out.println("Makespan:" + dataCenter.computeMakespan());
        System.out.println("Throughput:" + dataCenter.computeThroughput());
        System.out.println(swarm.globalBestObjectiveValue);
        System.out.println(swarm.globalBestTaskVmMapping);
        // HashMap<Task, VirtualMachine> finalMapping = swarm.runPSOAlgorithm();

        // System.out.println(finalMapping);
    }
}
