package syslab.cloudcomputing.schedule;

import java.util.ArrayList;
import java.util.HashMap;

import syslab.cloudcomputing.pso.PSOSwarm;
import syslab.cloudcomputing.simulation.*;

public class Scheduler {
    public static void main(String[] args) {
        int nVms = 2;
        int nTasks = 1;
        int nParticles = 2;
        int vmMipsHigh = 100;
        int vmMipsLow = 50;
        int taskMi = 10;

        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();

        for (int i = 0; i < nVms - 1; i++) {
            vms.add(new VirtualMachine(vmMipsLow));
        }
        vms.add(new VirtualMachine(vmMipsHigh));

        DataCenter dataCenter = new DataCenter(vms);

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (int i = 0; i < nTasks; i++) {
            tasks.add(new Task(taskMi));
        }

        Workload workload = new Workload(tasks);

        System.out.println(dataCenter);
        System.out.println(workload);

        PSOSwarm swarm = new PSOSwarm(dataCenter, workload, nParticles);
        System.out.println("Init results:");
        System.out.println("Global Best Position: " + swarm.globalBestPosition);
        System.out.println("Global Best Cost: " + swarm.globalBestObjectiveValue);
        System.out.println("Global Best Mapping: " + swarm.globalBestTaskVmMapping);
        System.out.println();

        HashMap<Task, VirtualMachine> finalMapping = swarm.runPSOAlgorithm();
        System.out.println("Global Best Position: " + swarm.globalBestPosition);
        System.out.println("Global Best Cost: " + swarm.globalBestObjectiveValue);
        System.out.println("Global Best Mapping: " + finalMapping);
    }
}
