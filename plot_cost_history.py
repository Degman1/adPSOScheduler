import argparse
import matplotlib.pyplot as plt 
import numpy as np

def plot(particle_histories, path):
  plt.figure(figsize=(8, 5))
  i = 1
  for particle_history in particle_histories:
    plt.plot([i for i in range(len(particle_history))], particle_history, linewidth=1.0, label=f"Particle {i}")
    i += 1

  plt.title("Test 8: adPSO Cost History")
  plt.xlabel("Iteration")
  plt.ylabel("Cost (Throughput + (1 / Makespan))")
  plt.legend(bbox_to_anchor = (1.25, 0.5), loc='center right')
  plt.tight_layout()

  plt.savefig(path)

if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument("--data_path", default="output_data/cost_history.csv", help="path of PSO swarm cost history csv file")
  parser.add_argument("--fname", default="./plots/cost_history.jpg", help="File output name")
  args = parser.parse_args()

  print(f"Plotting cost history data from {args.data_path}... ", end="")

  csvData = open(args.data_path, 'rb')
  data = np.loadtxt(csvData, delimiter='\t')
  plot(data, args.fname)

  print(f"Completed (Saved at {args.fname})")