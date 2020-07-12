package petrovich.data

import petrovich.rules
import petrovich.rules.{RuleSets, Tag}

sealed trait PersonPart {
  def ::(x: PersonPart): List[PersonPart] = List(x, this)
}

object PersonPart {

  sealed trait NamePart extends PersonPart {

    def transform(f: String => String): NamePart

    def tpe: NamePartType = this match {
      case _: FirstName => NamePartType.FirstName
      case _: MiddleName => NamePartType.MiddleName
      case _: LastName => NamePartType.LastName
    }
  }

  object NamePart {

    private val ComplexNameDelimiter = "-"

    implicit class NamePartOps(val self: NamePart) extends AnyVal {
      def inflect(gender: Gender, gcase: Case): NamePart = {
        self transform { s =>
          val ruleSets: RuleSets = rules.ruleSetsByNamePartType(self.tpe)
          if (s.contains(ComplexNameDelimiter)) {
            // This is a complex name
            val complexNameParts = s.split('-').toList
            val firstPart = complexNameParts.head
            val res = ruleSets(gender, firstPart, List(Tag.FirstWord))(firstPart, gcase) :: {
              for (part <- complexNameParts.tail)
                yield ruleSets(gender, part, Nil)(part, gcase)
            }
            res.mkString(ComplexNameDelimiter)
          }
          else {
            // This is a simple name
            ruleSets(gender, s, Nil)(s, gcase)
          }
        }
      }
    }

  }

  case class FirstName(value: String) extends NamePart {
    def transform(f: String => String): FirstName = FirstName(f(value))
  }

  case class MiddleName(value: String) extends NamePart {
    def transform(f: String => String): MiddleName = MiddleName(f(value))
  }

  case class LastName(value: String) extends NamePart {
    def transform(f: String => String): LastName = LastName(f(value))
  }

  sealed trait Gender extends PersonPart

  object Gender {

    case object Male extends Gender

    case object Female extends Gender

    case object Androgynous extends Gender

  }

}
