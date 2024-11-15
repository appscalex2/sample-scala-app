name := "sample-scala-app"

version := "0.1"

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.2.7",
  "com.typesafe.akka" %% "akka-stream" % "2.6.18",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.7" 
)

