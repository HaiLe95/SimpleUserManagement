name := "SimpleUserManagement"
version := "0.1"
scalaVersion := "2.13.5"

idePackagePrefix := Some("com.haile.app")

//TODO add assembly configuration, linter configs

libraryDependencies ++= Seq(
  // Spray basic
  "io.spray"            % "spray-can_2.11"        % "1.3.4",
  "io.spray"            % "spray-http_2.11"       % "1.3.4",
  "io.spray"            % "spray-routing_2.11"    % "1.3.4",
  // Akka
  "com.typesafe.akka"   % "akka-actor_2.13"       % "2.6.13",
  "com.typesafe.akka"   % "akka-slf4j_2.13"       % "2.6.13",
  // Lift-json
  "net.liftweb"         % "lift-json_2.11"        % "3.4.3",
  // Logger
  "ch.qos.logback"      %  "logback-classic"      % "1.2.3",
  // H2 database for simplicity
  "com.h2database"      %  "h2"                   % "1.4.200",
  // Slick ORM
  "com.typesafe.slick"  %  "slick_2.13"           % "3.3.3"
)
