import argparse
import matplotlib.pyplot as plt 
import numpy as np

def plot(test_id, language, particle_histories, path):
  plt.figure(figsize=(8, 5))
  i = 1
  for particle_history in particle_histories:
    plt.plot([i for i in range(len(particle_history))], particle_history, linewidth=1.0, label=f"Particle {i}")
    i += 1

  plt.title(f"{language.capitalize()} Test {test_id}: adPSO Objective History")
  plt.xlabel("Iteration")
  plt.ylabel("Objective (Throughput + (1 / Energy Consumption))")
  plt.legend(bbox_to_anchor = (1.25, 0.5), loc='center right')
  plt.tight_layout()

  plt.savefig(path)

if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument("--test", "-t", required=True, help="ID number of the test that was run")
  parser.add_argument("--lang", "-l", required=True, help="The language of algorithm: java || rust")
  parser.add_argument("--data_path", default="output_data/objective_history.csv", help="Path of PSO swarm objective history csv file")
  args = parser.parse_args()

  print(f"Plotting objective history data from {args.data_path}... ", end="")

  csvData = open(args.data_path, 'rb')
  data = np.loadtxt(csvData, delimiter='\t')
  fname = f"./plots/{args.lang}_test_{args.test}_objective_history.jpg"
  plot(args.test, args.lang, data, fname)

  print(f"Completed")
  print(f"Plot saved at {fname}")