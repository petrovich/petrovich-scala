import petrovich.data.{Case, ListPersonPartOps, PersonPart}
import petrovich.rules.{RuleSets, Tag}

import scala.language.implicitConversions

/**
 * @author Aleksey Fomkin <aleksey.fomkin@gmail.com>
 */
package object petrovich {

  import data.PersonPart._

  private val ComplexNameDelimiter = "-"
  
  type Person = List[PersonPart]

  private def inflect(gender: Gender, namePart: NamePart, gcase: Case): NamePart = {
    namePart transform { s ⇒
      val ruleSets: RuleSets = rules.ruleSetsByNamePartType(namePart.tpe)
      if (s.contains(ComplexNameDelimiter)) {
        // This is a complex name
        val complexNameParts = s.split('-').toList
        val firstPart = complexNameParts.head
        val res = ruleSets(gender, firstPart, List(Tag.FirstWord))(firstPart, gcase) :: {
          for (part ← complexNameParts.tail)
            yield ruleSets(gender, part, Nil)(part, gcase)
        }
        res.mkString(ComplexNameDelimiter)
      } else {
        // This is a simple name
        ruleSets(gender, s, Nil)(s, gcase)
      }
    }
  }

  def petrovich(person: Person, gcase: Case): Person = {
    val gender = person.gender
    // look over possible names of properties,
    // inflect them and add to result object
    gender :: {
      for (namePart ← person.parts)
        yield inflect(gender, namePart, gcase)
    }
  }

  implicit def toListPersonPartOps(x: List[PersonPart]): ListPersonPartOps = {
    new ListPersonPartOps(x)
  }
}
