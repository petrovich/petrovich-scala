package petrovich.rules

import petrovich.data.Case
import petrovich.data.PersonPart.Gender
import petrovich.rules.Tag._

import scala.annotation.tailrec

case class Rule(gender: Gender, test: List[String], mods: Seq[String], tags: Tags) {

  /**
   * Apply found rule to given name 
   */
  def apply(name: String, gcase: Case): String = gcase match {
    case Case.Nominative ⇒ name
    case _ ⇒
      @tailrec def rec(s: String, tl: List[Char]): String = tl match {
        case Nil ⇒ s
        case Rule.Dot :: _ ⇒ s
        case Rule.Dash :: xs ⇒ rec(s.substring(0, s.length - 1), xs)
        case x :: xs ⇒ rec(s + x, xs)
      }
      rec(name, mods(gcase.index).to[List])
  }
}

object Rule {
  
  private val Dot = ".".charAt(0)
  
  private val Dash = "-".charAt(0)
  
  type RuleSet = List[Rule]

  implicit class ToRuleSetOps(val self: RuleSet) extends AnyVal {

    /**
     * Local search in rulesets of exceptions or suffixes
     */
    def search(gender: Gender, name: String, matchWholeWord: Boolean, tags: Tags): Option[Rule] = {
      @tailrec def rec(tl: RuleSet): Option[Rule] = tl match {
        case Nil ⇒ None
        case rule :: xs if tags.intersect(rule.tags).isEmpty ⇒ rec(xs)
        case rule :: xs if rule.gender != Gender.Androgynous && gender != rule.gender ⇒ rec(xs)
        case rule :: xs ⇒
          val s = name.toLowerCase
          def matchSample(sample: String): Boolean = {
            if (matchWholeWord) s == sample
            else s.substring(s.length - sample.length) == sample            
          }
          if (rule.test.exists(matchSample)) Some(rule)
          else None
      }
      rec(self)
    }
  }
  
  implicit class ToOptionRuleOps(val self: Option[Rule]) extends AnyVal {
    def apply(name: String, gcase: Case): String = {
      self.fold(name)(_.apply(name, gcase))
    }
  }
}
