package petrovich.data

sealed trait PersonPart {
  def ::(x: PersonPart): List[PersonPart] = List(x, this)
}

object PersonPart {

  sealed trait NamePart extends PersonPart {
    def transform(f: String ⇒ String): NamePart
    def tpe: NamePartType = this match {
      case _: FirstName ⇒ NamePartType.FirstName
      case _: MiddleName ⇒ NamePartType.MiddleName
      case _: LastName ⇒ NamePartType.LastName
    }
  }

  case class FirstName(value: String) extends NamePart {
    def transform(f: String ⇒ String): FirstName = FirstName(f(value))
  }

  case class MiddleName(value: String) extends NamePart {
    def transform(f: String ⇒ String): MiddleName = MiddleName(f(value))
  }

  case class LastName(value: String) extends NamePart {
    def transform(f: String ⇒ String): LastName = LastName(f(value))
  }

  sealed trait Gender extends PersonPart

  object Gender {
    case object Male extends Gender
    case object Female extends Gender
    case object Androgynous extends Gender
  }
}
