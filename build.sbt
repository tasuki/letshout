name := "letshout"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % "10.1.4",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
