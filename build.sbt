name := """play-java-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, LauncherJarPlugin)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies ++= Seq(
  javaWs
)
libraryDependencies += javaJdbc
libraryDependencies ++= Seq(
    javaJdbc, 
    cache, 
    javaWs,
    "mysql" % "mysql-connector-java" % "5.1.18",
    "org.avaje" % "ebean" % "2.7.3",
    "javax.persistence" % "persistence-api" % "1.0.2"
)
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
// Test Database
libraryDependencies += "org.avaje" % "ebean" % "2.7.3"


// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "javax.persistence" % "persistence-api" % "1.0.2"



// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))


