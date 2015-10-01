package petrovich.data

import petrovich.exceptions.PetrovichException

/**
 * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
 */
final class ListPersonPartOps(val self: List[PersonPart]) extends AnyVal {

  import PersonPart._

  def parts: List[NamePart] = self collect {
    case x: NamePart ⇒ x
  }

  def first: Option[String] = {
    val xs = self collect { case FirstName(value) ⇒ value }
    xs.headOption
  }

  def middle: Option[String] = {
    val xs = self collect { case MiddleName(value) ⇒ value }
    xs.headOption
  }

  def last: Option[String] = {
    val xs = self collect { case LastName(value) ⇒ value }
    xs.headOption
  }

  def gender: Gender = {
    val xs = self collect { case value: Gender ⇒ value }
    xs.headOption getOrElse {
      def cantDetectGender = new PetrovichException("Can't detect gender")
      middle.getOrElse(throw cantDetectGender).toLowerCase match {
        case s if s.endsWith("ич") ⇒ Gender.Male
        case s if s.endsWith("на") ⇒ Gender.Female
        case _ ⇒ Gender.Androgynous
      }
    }
  }

  def firstLast: String = {
    Seq(first, last).
      flatten.
      mkString(" ")
  }

  def lastFirst: String = {
    Seq(last, first).
      flatten.
      mkString(" ")
  }

  def lastFirstMiddle: String = {
    Seq(last, first, middle).
      flatten.
      mkString(" ")
  }

  def firstMiddleLast: String = {
    Seq(first, middle, last).
      flatten.
      mkString(" ")
  }

}
