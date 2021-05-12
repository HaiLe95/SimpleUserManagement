name := "SimpleUserManagement"
version := "0.1"
scalaVersion := "2.13.5"

idePackagePrefix := Some("com.haile.app")

// Assembly configurations
mainClass in assembly := Some("com.haile.app.Boot")
assemblyJarName in assembly := "SimpleUserManagement.jar"

libraryDependencies ++= {
  val akkaHttpVersion   = "10.2.4"
  val akkaVersion       = "2.6.14"
  val slickVersion      = "3.3.3"
  val akka              = "com.typesafe.akka"
  val slick             = "com.typesafe.slick"

  Seq(
    // Akka core
    akka  % "akka-actor-typed_2.13"     % akkaVersion,
    akka  % "akka-stream_2.13"          % akkaVersion,
    akka  % "akka-http_2.13"            % akkaHttpVersion,
    akka  % "akka-http-spray-json_2.13" % akkaHttpVersion,

    // Logger
    "ch.qos.logback"  % "logback-classic"       % "1.2.3",
    // H2 database for simplicity
    "com.h2database"  % "h2"                    % "1.4.200",
    // MySQL database for docker compose
    "mysql"           % "mysql-connector-java"  % "8.0.24",
    // Slick ORM
    slick             % "slick_2.13"            % slickVersion,
    slick             % "slick-hikaricp_2.13"   % slickVersion ,

  )
}
