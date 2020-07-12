package petrovich.rules

import petrovich.data.PersonPart.Gender
import petrovich.rules.Rule.RuleSet
import petrovich.rules.Tag.Tags

case class RuleSets(exceptions: Option[RuleSet], suffixes: RuleSet) {

  /**
   * Find groups of rules in exceptions or suffixes of given nametype
   */
  def apply(gender: Gender, name: String, tags: Tags = Nil): Option[Rule] = {
    exceptions.flatMap(_.searchRule(gender, name, matchWholeWord = true, tags)).
      fold(suffixes.searchRule(gender, name, matchWholeWord = false, tags))(Some(_))
  }
}
