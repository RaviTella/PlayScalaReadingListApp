name := """playscalareadinglist"""
organization := "com.ratella"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

libraryDependencies += ws

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.9.6"

libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.16.2-play27"



routesGenerator := InjectedRoutesGenerator

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ratella.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ratella.binders._"
