package syslab.cloudcomputing.simulation;

public class HcspDataCenter extends DataCenter {
  // HCSP Execution Time Data: https://www.fing.edu.uy/inco/grupos/cecal/hpc/HCSP/HCSP_down.htm
  // Makespan Comparisons: https://www.researchgate.net/figure/Makespan-comparison-on-HCSP-dataset_fig1_341589043

  @Override
  protected double getLoadExecutionTime(Task task, VirtualMachine virtualMachine) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getLoadExecutionTime'");
  }

  @Override
  public double computeMakespan() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'computeMakespan'");
  }
  
}
