package syslab.cloudcomputing.schedule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import syslab.cloudcomputing.utils.Utilities;

import syslab.cloudcomputing.pso.PSOSwarm;
import syslab.cloudcomputing.pso.Result;
import syslab.cloudcomputing.simulation.*;

public class Scheduler {
    public static void main(String[] args) {
        DataCenter dataCenter;
        Workload workload;

        if (args[0].equals("hcsp")) {
            dataCenter = hcsp_DataCenter();
            workload = hcsp_Workload();
        } else if (args[0].equals("custom")) {
            try {
                Class<?> c = Class.forName("syslab.cloudcomputing.schedule.Scheduler");
                Class<?>[] paramTypes = null;
                Object[] params = null;
                
                String getDataCenterMethodName = "test" + args[1] + "_DataCenter";
                Method getDataCenter = c.getDeclaredMethod(getDataCenterMethodName, paramTypes);
                getDataCenter.setAccessible(true);
                dataCenter = (DataCenter) getDataCenter.invoke(null, params);

                String getWorkloadMethodName = "test" + args[1] + "_Workload";
                Method getWorkload = c.getDeclaredMethod(getWorkloadMethodName, paramTypes);
                getWorkload.setAccessible(true);
                workload = (Workload) getWorkload.invoke(null, params);
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | 
                    IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace(System.err);
                return;
            }
        } else {
            System.err.println("Invalid argument: " + args[0]);
            return;
        }
        
        // System.out.println(dataCenter);
        // System.out.println(workload);

        // Scheduler.runRepeatedTest(dataCenter, workload, 20.0);
        Scheduler.runSingleTest(dataCenter, workload, true);
    }

    public static void runSingleTest(DataCenter dataCenter, Workload workload, Boolean recordObjectiveHistory) {
        PSOSwarm swarm = new PSOSwarm(dataCenter, workload);
        swarm.runPSOAlgorithm();
        
        if (recordObjectiveHistory) {
            // System.out.println("Global Best Position: " + swarm.globalBestPosition);
            System.out.println("Global Best Objective: " + swarm.globalBestObjectiveValue);
            // System.out.println("Global Best Mapping: " + swarm.globalBestTaskVmMapping);
            System.out.print("Saving objective history to objective_history.csv... ");
            
            ArrayList<ArrayList<Double>> history = swarm.getParticleObjectiveHistory();
            Utilities.writeObjectiveHistoryToCSV(history, "output_data/objective_history.csv");

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

    public static DataCenter hcsp_DataCenter() {
        return null;
    }

    public static Workload hcsp_Workload() {
        return null;
    }

    public static DataCenter test1_DataCenter() {
        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();
        vms.add(new VirtualMachine(100));
        DataCenter dataCenter = new CustomDataCenter(vms);
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
        DataCenter dataCenter = new CustomDataCenter(vms);
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

        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();

        for (int i = 0; i < nVms - 1; i++) {
            if (i == highVmId) {
                vms.add(new VirtualMachine(vmMipsHigh));
            } else {
                vms.add(new VirtualMachine(vmMipsLow));
            }
        }

        DataCenter dataCenter = new CustomDataCenter(vms);
        return dataCenter;
    }

    public static Workload test3_Workload() {
        return Scheduler.test1_Workload();
    }

    public static DataCenter test4_DataCenter() {
        int nVms = 36;
        int vmMipsHigh = 100;
        int vmMipsLow = 20;

        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();

        for (int i = 0; i < nVms; i++) {
            vms.add(new VirtualMachine(Utilities.getRandomInteger(vmMipsLow, vmMipsHigh)));
        }

        DataCenter dataCenter = new CustomDataCenter(vms);
        return dataCenter;
    }

    public static Workload test4_Workload() {
        return Scheduler.test1_Workload();
    }

    public static DataCenter test5_DataCenter() {
        return Scheduler.test4_DataCenter();
    }

    public static Workload test5_Workload() {
        int nTasks = 5;
        int taskMi = 10;

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (int i = 0; i < nTasks; i++) {
            tasks.add(new Task(taskMi));
        }
        
        Workload workload = new Workload(tasks);
        return workload;
    }

    public static DataCenter test6_DataCenter() {
        return Scheduler.test4_DataCenter();
    }

    public static Workload test6_Workload() {
        int nTasks = 5;
        int taskMiHigh = 20;
        int taskMiLow = 5;

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (int i = 0; i < nTasks; i++) {
            tasks.add(new Task(Utilities.getRandomInteger(taskMiLow, taskMiHigh)));
        }
        
        Workload workload = new Workload(tasks);
        return workload;
    }

    public static DataCenter test7_DataCenter() {
        return Scheduler.test4_DataCenter();
    }

    public static Workload test7_Workload() {
        int nTasks = 100;
        int taskMiHigh = 20;
        int taskMiLow = 5;

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (int i = 0; i < nTasks; i++) {
            tasks.add(new Task(Utilities.getRandomInteger(taskMiLow, taskMiHigh)));
        }
        
        Workload workload = new Workload(tasks);
        return workload;
    }

    public static DataCenter test8_DataCenter() {
        return Scheduler.test4_DataCenter();
    }

    public static Workload test8_Workload() {
        int nTasks = 500;
        int taskMiHigh = 20;
        int taskMiLow = 5;

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (int i = 0; i < nTasks; i++) {
            tasks.add(new Task(Utilities.getRandomInteger(taskMiLow, taskMiHigh)));
        }
        
        Workload workload = new Workload(tasks);
        return workload;
    }

    public static DataCenter test9_DataCenter() {
        int nVms = 36;
        int vmMipsInterval = 20;
        int mips = 50;

        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();

        for (int i = 0; i < nVms; i++) {
            vms.add(new VirtualMachine(mips));
            mips += vmMipsInterval;
        }

        DataCenter dataCenter = new CustomDataCenter(vms);
        return dataCenter;
    }

    public static Workload test9_Workload() {
        return Scheduler.test8_Workload();
    }
}
