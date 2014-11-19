name := "Eve Corporation Contracts"

version := "0.1"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
			"org.scalatest" %% "scalatest" % "2.2.1" % "test",
			"com.beimin" % "eveapi" % "5.1.3",
			"com.typesafe" % "config" % "1.2.1",
			"org.slf4j" % "slf4j-simple" % "1.7.7",
			"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
			"io.reactivex" %% "rxscala" % "0.22.0",
			"com.github.tototoshi" %% "scala-csv" % "1.1.1"
)

resolvers += "eveapi" at "https://eveapi.googlecode.com/svn/m2/releases"