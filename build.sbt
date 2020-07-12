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

lazy val scala213 = "2.13.2"
lazy val scala212 = "2.12.11"
lazy val supportedScalaVersions = List(scala213, scala212)

val commonSettings = publishSettings ++ Seq(
  scalaVersion := crossScalaVersions.value.head,
  crossScalaVersions := supportedScalaVersions,
  organization := "com.github.fomkin",
  version := "0.2.0-SNAPSHOT",
  libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.0" % Test,
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-Xfatal-warnings",
    "-language:postfixOps",
    "-language:implicitConversions"
  )
)

lazy val `petrovich-scala` = crossProject(JSPlatform, JVMPlatform).
  settings(commonSettings: _*).
  settings(
    normalizedName := "petrovich-scala",
    sourceGenerators in Compile += sourceManaged in Compile map GenRules
  )

lazy val petrovichJS = `petrovich-scala`.js
lazy val petrovichJVM = `petrovich-scala`.jvm

publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

publishArtifact := false
