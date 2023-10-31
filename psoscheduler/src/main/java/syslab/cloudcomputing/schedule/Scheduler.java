package syslab.cloudcomputing.schedule;

import java.util.ArrayList;

import syslab.cloudcomputing.utils.Utilities;

import syslab.cloudcomputing.pso.PSOSwarm;
import syslab.cloudcomputing.pso.Result;
import syslab.cloudcomputing.simulation.*;

public class Scheduler {
    public static DataCenter test1_DataCenter() {
        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();
        vms.add(new VirtualMachine(100));
        DataCenter dataCenter = new DataCenter(vms);
        return dataCenter;
    }

    public static Workload test1_Workload() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task(10));
        Workload workload = new Workload(tasks);
        return workload;
    }

    public static DataCenter test2_DataCenter() {
        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();
        vms.add(new VirtualMachine(100));
        vms.add(new VirtualMachine(50));
        DataCenter dataCenter = new DataCenter(vms);
        return dataCenter;
    }

    public static Workload test2_Workload() {
        return Scheduler.test1_Workload();
    }

    public static DataCenter test3_DataCenter() {
        int nVms = 36;
        int vmMipsHigh = 100;
        int vmMipsLow = 50;

        int highVmId = Utilities.getRandomInteger(0, nVms - 1);
        System.out.println(highVmId);

        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();

        for (int i = 0; i < nVms - 1; i++) {
            if (i == highVmId) {
                vms.add(new VirtualMachine(vmMipsHigh));
            } else {
                vms.add(new VirtualMachine(vmMipsLow));
            }
        }

        DataCenter dataCenter = new DataCenter(vms);
        return dataCenter;
    }

    public static Workload test3_Workload() {
        return Scheduler.test1_Workload();
    }

    public static void runSingleTest(DataCenter dataCenter, Workload workload, Boolean recordCostHistory) {
        PSOSwarm swarm = new PSOSwarm(dataCenter, workload);
        swarm.runPSOAlgorithm();
        
        if (recordCostHistory) {
            System.out.println("Global Best Position: " + swarm.globalBestPosition);
            System.out.println("Global Best Cost: " + swarm.globalBestObjectiveValue);
            System.out.println("Global Best Mapping: " + swarm.globalBestTaskVmMapping);
            System.out.print("Saving cost history to cost_history.csv... ");
            
            ArrayList<ArrayList<Double>> history = swarm.getParticleObjectiveHistory();
            Utilities.writeCostHistoryToCSV(history, "output_data/cost_history.csv");

            System.out.println("Completed");
        }
    }

    // TODO Pupose=benchmarking
    public static void runRepeatedTest(DataCenter dataCenter, Workload workload, double handCalculatedBest) {
        int nRetries = 1000;

        Result[] results = new Result[nRetries];

        double success = 0.0;
        for (int i = 0; i < nRetries; i++) {
            results[i] = PSOSwarm.runRepeatedPSOAlgorithm(dataCenter, workload);
            if (results[i].getFinalObjective() >= handCalculatedBest) {
                success += 1.0;
            }
        }

        System.out.println(success / nRetries);
    }

    public static void main(String[] args) {
        DataCenter dataCenter = Scheduler.test3_DataCenter();
        Workload workload = Scheduler.test3_Workload();
        
        System.out.println(dataCenter);
        System.out.println(workload);

        // Scheduler.runRepeatedTest(dataCenter, workload, 20.0);
        Scheduler.runSingleTest(dataCenter, workload, true);
    }
}
