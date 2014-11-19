import com.typesafe.sbt.SbtNativePackager._
import NativePackagerKeys._

name := "Eve Corporation Contracts"

version := "0.1"

scalaVersion := "2.11.4"

autoScalaLibrary := false

libraryDependencies ++= Seq(
			"org.scala-lang" % "scala-library" % scalaVersion.value,
			"org.scalatest" %% "scalatest" % "2.2.1" % "test",
			"com.beimin" % "eveapi" % "5.1.3",
			"com.typesafe" % "config" % "1.2.1",
			"org.slf4j" % "slf4j-simple" % "1.7.7",
			// Depends on scala-reflect which does not seem necessary for operation
			"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" intransitive(),
			"io.reactivex" %% "rxscala" % "0.22.0",
			"com.github.tototoshi" %% "scala-csv" % "1.1.1"
)

resolvers += "eveapi" at "https://eveapi.googlecode.com/svn/m2/releases"

packageArchetype.java_application

NativePackagerKeys.packageSummary in SbtNativePackager.Windows := "Eve Corporation Contracts"

NativePackagerKeys.packageDescription in SbtNativePackager.Windows := "Monitors the corporation contracts."

net.virtualvoid.sbt.graph.Plugin.graphSettings