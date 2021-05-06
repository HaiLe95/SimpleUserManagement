// Scala Linter
//addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.13")

// Easy IDEA configs
addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")

// Scala Style-checker
addSbtPlugin("com.beautiful-scala" % "sbt-scalastyle" % "1.5.0")

// Scala easy-going deploy and build tool
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")

// CompilerPlugin configurations
resolvers += Resolver.sonatypeRepo("snapshots")
