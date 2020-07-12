import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import petrovich._

class PetrovichSpec extends AnyFlatSpec with Matchers {

  "Petrovich" should "detect female" in {
    val person = FirstName("Светлана") :: MiddleName("Андреевна")
    person.gender shouldEqual Gender.Female
  }

  it should "detect male" in {
    val person = FirstName("Лев") :: MiddleName("Алексеевич")
    person.gender shouldEqual Gender.Male
  }

  it should "convert simple names to genitive case" in {
    val personN = LastName("Фомкин") :: FirstName("Алексей") :: MiddleName("Юрьевич")
    val personG = LastName("Фомкина") :: FirstName("Алексея") :: MiddleName("Юрьевича")
    assert(petrovich(personN, Case.Genitive).intersect(personG) == personG)
  }

  it should "convert complex names to genitive case" in {
    val personN = LastName("Ткач") :: FirstName("Антон") :: MiddleName("Вячеславович")
    val personD = LastName("Ткача") :: FirstName("Антона") :: MiddleName("Вячеславовича")
    assert(petrovich(personN, Case.Genitive).intersect(personD) == personD)
  }

  it should "convert complex names to dative case" in {
    val personN = LastName("Бонч-Бруевич") :: FirstName("Виктор") :: MiddleName("Леопольдович")
    val personD = LastName("Бонч-Бруевичу") :: FirstName("Виктору") :: MiddleName("Леопольдовичу")
    assert(petrovich(personN, Case.Dative).intersect(personD) == personD)
  }

  it should "convert name via alternative syntax" in {
    val expr = petrovich.
      male.
      first("Лев").
      last("Щаранский").
      prepositional.
      firstLast

    assert(expr == "Льве Щаранском")
  }
}
