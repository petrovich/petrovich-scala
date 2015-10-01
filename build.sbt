val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value) Some("snapshots" at s"${nexus}content/repositories/snapshots")
    else Some("releases" at s"${nexus}service/local/staging/deploy/maven2")
  },
  pomExtra := {
    <url>https://github.com/fomkin/petrovich-scala</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:fomkin/petrovich-scala.git</url>
      <connection>scm:git:git@github.com:fomkin/petrovich-scala.git</connection>
    </scm>
    <developers>
      <developer>
        <id>fomkin</id>
        <name>Aleksey Fomkin</name>
        <email>aleksey.fomkin@gmail.com</email>
      </developer>
    </developers>
  }
)


val commonSettings = publishSettings ++ Seq(
  scalaVersion := "2.11.7",
  organization := "com.github.fomkin",
  version := "0.1.0-SNAPSHOT",
  libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.0-M7" % "test",
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-Xfatal-warnings",
    "-language:postfixOps",
    "-language:implicitConversions"
  )
)

lazy val petrovich = (crossProject.crossType(CrossType.Pure) in file(".")).
  settings(commonSettings: _*).
  settings(
    normalizedName := "petrovich-scala",
    sourceGenerators in Compile <+= sourceManaged in Compile map GenRules
  )

lazy val petrovichJS = petrovich.js
lazy val petrovichJVM = petrovich.jvm

publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

publishArtifact := false
