import org.scalatest._
import petrovich._

class PetrovichSpec extends FlatSpec with Matchers {

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
  
  it should "convert complex names to dative case" in {
    val personN = LastName("Бонч-Бруевич") :: FirstName("Виктор") :: MiddleName("Леопольдович")
    val personD = LastName("Бонч-Бруевичу") :: FirstName("Виктору") :: MiddleName("Леопольдовичу")
    assert(petrovich(personN, Case.Dative).intersect(personD) == personD)
  }
}
