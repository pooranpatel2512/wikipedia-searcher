name := """WikipediaSearcher"""

version := "1.0-SNAPSHOT"

organization := "com.pooranpatel"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
	json,
	"org.apache.lucene" % "lucene-core" % "5.1.0",
	"org.apache.lucene" % "lucene-analyzers-common" % "5.1.0"
)
