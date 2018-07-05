# distribution-calculator

This is a small example project showing how to select response times given a distribution expressed in percentiles. 
Percentiles are often used to describe the performance of software where, e.g. 99% of all requests completes within 10 millisecond and 99.99% complete within 1 second.

There is no release for this software yet.

## Usage

Currently, two variants are available `ProbabilityDistribution` and `StatefulProbabilityDistribution`. The first sacrifices accuracy in turn for less memory usage whereas the second is accurate but needs more memory to operate.


```scala
// Values here can be anything, e.g. Longs or Twitter or Scala Durations.
// In this case longs representing milliseconds are used.
val distribution = Map(0.1d -> 1l, 0.95d -> 10l, 0.999d -> 200l)

// Resolution is the amount of requests we used to produce the distribution above.
val resolution = 100000

// Alternatively the `ProbabilityDistribution` class could be used here.
val calculator = new StatefulProbabilityDistribution(distribution, resolution, new Random())

val randomLatencyFromDistribution = 
  calculator.getRandom
```
