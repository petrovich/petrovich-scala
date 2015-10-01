import java.nio.charset.{StandardCharsets, Charset}

import sbt.File
import sbt._
import upickle.Js

import upickle.default._

object GenRules extends (File ⇒ Seq[File]) {
  
  case class Rule(gender: String, test: Seq[String], mods: Seq[String], tags: Seq[String]) {
    def gen: String = {
      val genGender = "Gender." + gender.charAt(0).toUpper + gender.substring(1)
      val genTest = test.map(x ⇒ s""""$x"""").mkString(", ")
      val genMods = mods.map(x ⇒ s""""$x"""").mkString(", ")
      val genTags = tags.map(x ⇒ s"""Tag("$x")""").mkString(", ")
      s"""        Rule(
         |          gender = $genGender,
         |          test = List($genTest),
         |          mods = Seq($genMods),
         |          tags = List($genTags)
         |        )""".stripMargin
      
    }
  }
  
  object Rule {
    implicit val rule2Reader = upickle.default.Reader[Rule] {
      case obj: Js.Obj =>
        val map = obj.value.toMap
        Rule(
          gender = readJs[String](map("gender")),
          test = readJs[Seq[String]](map("test")),
          mods = readJs[Seq[String]](map("mods")),
          tags = map.get("tags").fold(Seq.empty[String])(readJs[Seq[String]])
        )
    }
  }
  
  case class RuleSets(exceptions: Option[Seq[Rule]], suffixes: Seq[Rule]) {
    def gen: String = {
      def rs(xs: Seq[Rule]) = xs.map(_.gen).mkString(",\n")
      val es = exceptions match {
        case None ⇒ "None,"
        case Some(x) ⇒
          val rsx = rs(x)
          s"Some(List(\n$rsx)\n" +
          s"      ),"
      }
      val ss = "List(\n" + rs(suffixes)
      s"""RuleSets(
         |      exceptions = $es
         |      suffixes = $ss
         |      )      
         |    )""".stripMargin
    }
  }
  
  object RuleSets {
    implicit val ruleSets2Reader = upickle.default.Reader[RuleSets] {
      case obj: Js.Obj =>
        val map = obj.value.toMap
        RuleSets(
          map.get("exceptions").map(readJs[Seq[Rule]]),
          readJs[Seq[Rule]](map("suffixes"))
        )
    }
  }

  def apply(dir: File): Seq[File] = {
    val genFile = dir / "petrovich" / "rules" / "package.scala"
    val json = {
      val raw = IO.read(file("petrovich-rules") / "rules.json", StandardCharsets.UTF_8)
      upickle.json.read(raw) match {
        case x: Js.Obj ⇒ x.value.toMap
        case _ ⇒ fail("Invalid rules file")
      }
    }
    val ruleSetsByNamePartType = json map {
      case ("lastname", v) ⇒
        val rs = readJs[RuleSets](v)
        s"    NamePartType.LastName -> ${rs.gen}"
      case ("firstname", v) ⇒
        val rs = readJs[RuleSets](v)
        s"    NamePartType.FirstName -> ${rs.gen}"
      case ("middlename", v) ⇒
        val rs = readJs[RuleSets](v)
        s"    NamePartType.MiddleName -> ${rs.gen}"
      case (k, _) => fail(s"Invalid rules file (unknown name part: $k)")
    }
    IO.write(genFile,
      s"""
         |package petrovich
         |
         |import data._
         |import data.PersonPart._
         |
         |// This file was generated.
         |// See project/GenRules.scala
         |// To update code run `reload` in SBT console
         |package object rules {
         |  val ruleSetsByNamePartType: Map[NamePartType, RuleSets] = Map(
         |${ruleSetsByNamePartType.mkString(",\n")}  
         |  )  
         |}
      """.stripMargin
    )
    Seq(genFile)
  }
}
