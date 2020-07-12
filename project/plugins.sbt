addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.1.1")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")

// Why I don't use my own library Pushka?
// Because I wasn't made a version for 2.10.*
libraryDependencies += "com.lihaoyi" %% "upickle" % "1.1.0"
