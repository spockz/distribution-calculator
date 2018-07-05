package me.alessandrovermeulen.distributioncalculator

import org.scalatest.Matchers

import scala.util.Random

class ProbabilityDistributionTest extends org.scalatest.FlatSpec with Matchers {

  it should "give back the max" in {
    val calculator = new ProbabilityDistribution(Map(0d -> "0", 1d -> "1"), 10000, new Random())
    calculator.getRandom() shouldBe "1"
  }


  it should "only a single 100.000" in {


    val resolution = 100000
    val nnthpercentile = 0.99d
    val expectedTenthPercentile = (0.1 * resolution).toInt
    val expectedNnthPercentile: Int = (0.99d * resolution).toInt - expectedTenthPercentile
    val expectedMaxPercentile: Int = resolution - expectedNnthPercentile - expectedTenthPercentile


    val calculator = new StatefulProbabilityDistribution(Map(0.1d -> "10%", 0.99d -> "99%", 1d -> "100.000"), 100000, new Random())
    val probes = 1 to 100000 map (_ => calculator.getRandom())

    println(calculator.concreteDistribution)
    println(probes.size)
    probes.count(_ == "10%") shouldBe expectedTenthPercentile
    probes.count(_ == "99%") shouldBe expectedNnthPercentile
    probes.count(_ == "100.000") shouldBe expectedMaxPercentile

  }
}
