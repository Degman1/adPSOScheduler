# adPSOScheduler

## Introduction

This repository contains the software associated with the undergraduate honors thesis *Reinforcing Cloud System Security and Sustainability with Rust-based Particle Swarm Optimization Task Scheduling* completed by David Gerard under the advisement of Professor Timothy Richards, the UMass Undergraduate Systems Laboratory, and the UMass Commonwealth Honors College.

## Abstract

With the widespread popularity of cloud computing solutions in industry, the efficiency of data centers becomes critical in reducing carbon emissions. Cloud task scheduling algorithms such as Particle Swarm Optimization (PSO) are highly optimizable with influence on energy consumption and safety exposure. As such, efficient task scheduling is a barrier to the balance between sustainability, consumer interests, and provider interests. The growing popularity of the Rust programming language, boasting guaranteed safety without the cost of efficiency, poses a critical question: How does applying Rust to the PSO algorithm impact system-wide safety vulnerability, data exposure level, and runtime efficiency in cloud computing task scheduling? Java and Rust PSO algorithms are constructed and analyzed according to qualitative safety exposures and quantitative efficiency metrics. Following the same logic as the Java algorithm, the Rust algorithm patches vulnerabilities such as race conditions, discrete data leaks, and other concurrency-related issues. Rust constructs ultimately provoke deeper consideration of data flow design that is integral to the development of complex distributed systems. Although efficiency tends to be inversely related to safety guarantees in large-scale software systems, the Rust PSO algorithm exhibits a 2.5x efficiency increase over the Java version. These safety and performance improvements are catalysts for augmenting customer quality of service, business operation costs, and data center sustainability.

## Repository Struture

This repository has two main directories, `psoscheduler-java` and `psoscheduler-rust` that house the Java and Rust algorithms respectively. Both have a number of subdirectories that logically group together portions of the algorithm. The `pso` subdirectory houses the main algorithm including particle and swarm logic. The `simulation` subdirectory houses the data center and task load simulations. The `utils` subdirectory houses a variety of utility functions for data manipulation and transformation. In the Java version, the randomized test cases 1-11 can be found in the `schedule` subdirectory. In the Rust version, the test cases 1-6, 11 are found in `lib.rs`.

## Usage

A general purpose script `testPSO.sh` has been developed to easily test the Java and Rust Particle Swarm Optimization (PSO) algorithms with constructed test data sets as well as the standardized HCSP instances[^1] that are publicly available for download[^2].

**General Usage:** `/testJavaPSO.sh [language] [testType] [testID] ~[jdkPath]`

| Argument | Description |
| -------- | ----------- |
| `[language]` | "rust" OR "java" |
| `[testType]` | "custom" OR "HCSP" |
| `[testID]`   | The id of the custom test (1-11; However, tests 7-10 aren't available for the Rust version) OR The name of the hcsp data set file in quotes |
| `~[jdkPath]` | The string path where the java jdk bin is located |

**Note** that `~` implies optional arguments

## References

[^1]: Braun, T.—Siegel, H.—Beck, N.—Bol¨ oni, L.—Maheswaran, M.— ¨
Reuther, A.—Robertson, J.—Theys, M.—Yao, B.—Hensgen, D.—
Freund, R.: A Comparison of Eleven Static Heuristics for Mapping a Class of
Independent Tasks into Heterogeneous Distributed Computing Systems. J. Parallel
Distrib. Comput., Vol. 61, 2001, No. 6, pp. 810–837.
[^2]: Heterogeneous Computing Scheduling Problem. Retrieved December 6, 2023 from https://www.fing.edu.uy/inco/grupos/cecal/hpc/HCSP/index.htm