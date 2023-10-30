import argparse
import matplotlib as plt
import numpy as np

def plot(particle_histories):
  plt.figure()

  for particle_history in particle_histories:
    plt.plot([i for i in range(particle_history)], particle_history, '-o')

  plt.title("adPSO Cost History")
  plt.xlabel("Iteration")
  plt.ylabel("Cost (throughput + (1 / makespan))")

  plt.show()

if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument("--path", default="cost_history.csv", help="path of PSO swarm cost history csv file")
  args = parser.parse_args()

  csvData = open(args.path, 'rb')
  data = np.loadtxt(csvData, delimiter='\t')
  print(data)
  # plot(data)