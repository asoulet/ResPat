# Reservoir Pattern Sampling in Data Streams

Many applications generate data streams where online analysis needs are essential. In this context, pattern mining is a complex task because it requires access to all data observations. To overcome this problem, the state of the art methods maintain a data sample or a compact data structure retaining only recent information on the main patterns. This paper addresses online pattern discovery in data streams based on pattern sampling techniques. Benefiting from reservoir sampling, we propose a generic algorithm, named **ResPat**, that uses a limited memory space and that integrates a wide spectrum of temporal biases simulating landmark window, sliding window or exponential damped window. For these three window models, we provide fast damping optimizations and we study their temporal complexity. Experiments show that the performance of **ResPat** algorithms is particularly good. Finally, we illustrate the interest of our approach with online outlier detection in data streams.

## Publication

This is the code source repository for the paper:

__Reservoir Pattern Sampling in Data Streams__ [Arnaud Giacometti](http://www.info.univ-tours.fr/~giaco/) and [Arnaud Soulet](http://www.info.univ-tours.fr/~soulet/), Full paper at [ECML PKDD 2021](https://2021.ecmlpkdd.org/) (research track).

## How to run the prototype?

ResPat is implemented in Java. It was running under the environment configured as follows:
- JRE System Library jdk1.8.0_201
- Apache Jena 3.8.0
- Eclipse IDE for Java Developers - Version: 2018-12 (4.10.0) - Build id: 20181214-0600

Execute the class FileStreamTest (in streamsamp package) with abalone.bin as dataset example
