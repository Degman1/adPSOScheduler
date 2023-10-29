package syslab.cloudcomputing.schedule;

import java.util.ArrayList;
import java.util.HashMap;

import syslab.cloudcomputing.pso.PSOSwarm;
import syslab.cloudcomputing.simulation.*;

public class Scheduler {
    public static void main(String[] args) {
        int nVms = 1;
        int nTasks = 1;
        int nParticles = 1;
        int maxAbsoluteVelocity = 5;

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

        PSOSwarm swarm = new PSOSwarm(dataCenter, workload, nParticles, maxAbsoluteVelocity);
        System.out.println(swarm.globalBestTaskVmMapping);
        // HashMap<Task, VirtualMachine> finalMapping = swarm.runPSOAlgorithm();

        // System.out.println(finalMapping);
    }
}
