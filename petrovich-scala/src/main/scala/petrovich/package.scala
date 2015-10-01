import petrovich.data.PersonPart
import petrovich.data.PersonPart.NamePart
import petrovich.exceptions.PetrovichException

import scala.language.implicitConversions

package object petrovich {

  type Person = List[PersonPart]

  type FirstName = PersonPart.FirstName
  type LastName = PersonPart.LastName
  type MiddleName = PersonPart.MiddleName
  type Gender = PersonPart.Gender
  type Case = data.Case

  val FirstName = PersonPart.FirstName
  val LastName = PersonPart.LastName
  val MiddleName = PersonPart.MiddleName
  val Gender = PersonPart.Gender
  val Case = data.Case

  def petrovich(person: Person, gcase: Case): Person = {
    person.inflect(gcase)
  }

  def petrovich: Person = List.empty[PersonPart]

  implicit class PersonOps(val self: Person) extends AnyVal {

    def nameParts: List[NamePart] = self collect {
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

    def inflect(gcase: Case): Person = {
      // look over possible names of properties,
      // inflect them and add to result object
      gender :: {
        for (namePart ← nameParts)
          yield namePart.inflect(gender, gcase)
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

    def first(x: String): Person = FirstName(x) :: self

    def last(x: String): Person = LastName(x) :: self

    def middle(x: String): Person = MiddleName(x) :: self

    def male = Gender.Male :: self

    def female = Gender.Female :: self

    /**
     * Именительный
     */
    def nominative: Person = inflect(Case.Nominative)

    /**
     * Родительный
     */
    def genitive: Person = inflect(Case.Genitive)

    /**
     * Дательный
     */
    def dative: Person = inflect(Case.Dative)

    /**
     * Винительный
     */
    def accusative: Person = inflect(Case.Accusative)

    /**
     * Творительный
     */
    def instrumental: Person = inflect(Case.Instrumental)
    
    /**
     * Предложный
     */
    def prepositional: Person = inflect(Case.Prepositional)
  }

}
