name := "SimpleUserManagement"
version := "0.1"
scalaVersion := "2.13.5"

idePackagePrefix := Some("com.haile.app")

//TODO add assembly configuration, linter configs

libraryDependencies ++= {
  val akkaHttpVersion   = "10.2.4"
  val akkaVersion       = "2.6.14"

  Seq(
    // Akka core
    "com.typesafe.akka"   % "akka-actor-typed_2.13"     % akkaVersion,
    "com.typesafe.akka"   % "akka-stream_2.13"          % akkaVersion,
    "com.typesafe.akka"   % "akka-http_2.13"            % akkaHttpVersion,
    "com.typesafe.akka"   % "akka-http-spray-json_2.13" % akkaHttpVersion,

  // Logger
    "ch.qos.logback"      % "logback-classic"           % "1.2.3",
    // H2 database for simplicity
    "com.h2database"      % "h2"                        % "1.4.200",
    // Slick ORM
    "com.typesafe.slick"  % "slick_2.13"                % "3.3.3"
  )
}
