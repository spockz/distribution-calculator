package me.alessandrovermeulen.distributioncalculator

import scala.collection.breakOut
import scala.util.Random

/**
  * Randomly picks a long within the resolution and returns the corresponding value based on the percentile times
  * resolution. Trades in precision for memory use. See {{StatefulProbabilityDistribution}} for a more accurate
  * distribution.
  *
  * @param distribution Map containing in-order keys describing the value belonging to a given percentile, keys in range of 0 to 1.
  * @param resolution   The higher the higher the accuracy.
  * @param random       The random generator
  * @tparam A Type of the return values.
  */
class ProbabilityDistribution[A](distribution: Map[Double, A], resolution: Long, random: Random) extends Iterable[A] {
  require(distribution.nonEmpty, "Distribution should be non-empty")
  require(distribution.keys.forall(p => p >= 0 && p <= 1), "Percentile keys should all be between 0 and 1")

  val concreteDistribution: Seq[(Long, A)] =
    distribution.map({ case (key, value) =>
      ((key * resolution).toLong, value)
    })(breakOut).sortBy(_._1)

  val aForMaxPercentile: A = concreteDistribution.maxBy(_._1)._2


  def getRandom(): A = {
    val pickedLong = ExtendedRandom.internalNextLong(random.self, 0, resolution)
    println(pickedLong)

    concreteDistribution.find(pickedLong <= _._1).map(_._2).getOrElse(aForMaxPercentile)
  }

  /**
    * @inheritdoc
    */
  override def iterator: Iterator[A] = new Iterator[A] {
    override def hasNext: Boolean = true

    override def next(): A = getRandom()
  }
}

/**
  * Returns each value in the distribution exactly as often as percentile * resolution per 'cycle'. The length of each
  * cycle is defined by the {{resolution}} argument.
  *
  * @param distribution Map containing in-order keys describing the value belonging to a given percentile, keys in range of 0 to 1.
  * @param resolution   The higher the higher the accuracy.
  * @param random       The random generator
  * @tparam A Type of the return values.
  */
class StatefulProbabilityDistribution[A](distribution: Map[Double, A], resolution: Long, random: Random) extends Iterable[A] {
  require(distribution.nonEmpty, "Distribution should be non-empty")
  require(distribution.keys.forall(p => p >= 0 && p <= 1), "Percentile keys should all be between 0 and 1")

  val concreteDistribution: Seq[(Long, A)] =
    distribution.map({ case (key, value) =>
      ((key * resolution).toLong, value)
    })(breakOut).sortBy(_._1)

  /**
    * @todo should not be necessary
    */
  val aForMaxPercentile: A = concreteDistribution.maxBy(_._1)._2


  val concreteValues: Seq[A] = 1l to resolution map (i =>
    concreteDistribution.find(i <= _._1).map(_._2).getOrElse(aForMaxPercentile)
    )

  private var responses: Seq[A] = shuffle()


  def getRandom(): A = responses match {
    case Seq(x) =>
      responses = shuffle()
      x
    case x +: xs =>
      responses = xs
      x
  }

  override def iterator: Iterator[A] = new Iterator[A] {
    override def hasNext: Boolean = true

    override def next(): A = getRandom()
  }

  private def shuffle(): Seq[A] = {
    random.shuffle(concreteValues)
  }

}
