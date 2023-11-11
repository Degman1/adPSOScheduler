package syslab.cloudcomputing.simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HcspDataCenter extends DataCenter {
  // HCSP Execution Time Data: https://www.fing.edu.uy/inco/grupos/cecal/hpc/HCSP/HCSP_down.htm
  // Makespan Comparisons: https://www.researchgate.net/figure/Makespan-comparison-on-HCSP-dataset_fig1_341589043

  double[][] hcspDataSet;

  public HcspDataCenter(String pathname, int nTasks, int nVirtualMachines) {
    super();
    this.loadHcspDataSet(pathname, nTasks, nVirtualMachines);
  }

  private void loadHcspDataSet(String pathname, int nTasks, int nVirtualMachines) {
    System.out.print("Loading in hcsp data set from " + pathname + "... ");
    this.hcspDataSet = new double[nTasks][nVirtualMachines];

    int i = 0;
    int j = 0;

    try {
      File myObj = new File(pathname);
      Scanner myReader = new Scanner(myObj);

      while (myReader.hasNextLine()) {
        double data = Double.parseDouble(myReader.nextLine());

        this.hcspDataSet[i][j] = data;

        if (i >= nTasks - 1) {
          j = (j + 1) % nVirtualMachines;
        }
        i = (i + 1) % nTasks;
      }

      myReader.close();

      System.out.println("Completed");
    } catch (FileNotFoundException e) {
      e.printStackTrace(System.err);
    }

    // Add virtual machines to the data center
    for (i = 0; i < nVirtualMachines; i++) {
      // mips will not be used here, so just use 0 as a placeholder
      this.addVirtualMachine(new VirtualMachine(0));
    }
  }

  @Override
  protected double getLoadExecutionTime(Task task, VirtualMachine virtualMachine) {
    return this.hcspDataSet[task.getId() - 1][virtualMachine.getId() - 1];
  }
}
