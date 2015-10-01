package petrovich.rules

import petrovich.data.PersonPart.Gender
import petrovich.rules.Rule.RuleSet
import petrovich.rules.Tag.Tags

case class RuleSets(exceptions: Option[RuleSet], suffixes: RuleSet) {

  /**
   * Find groups of rules in exceptions or suffixes of given nametype
   */
  def apply(gender: Gender, name: String, tags: Tags = Nil): Option[Rule] = {
    exceptions.flatMap(_.search(gender, name, matchWholeWord = true, tags)).
      fold(suffixes.search(gender, name, matchWholeWord = false, tags))(Some[Rule])
  }
}
