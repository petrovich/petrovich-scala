package petrovich.data

sealed abstract class Case(val index: Int)

object Case {
  case object Nominative extends Case(-1)
  case object Genitive extends Case(0)
  case object Dative extends Case(1)
  case object Accusative extends Case(2)
  case object Instrumental extends Case(3)
  case object Prepositional extends Case(4)
}
