name := "embedded-elastic"

version := "1.0"

scalaVersion in ThisBuild := "2.11.7"

scalacOptions in ThisBuild := Seq("-unchecked", "-deprecation", "-feature", "-language:postfixOps", "-language:implicitConversions", "-encoding", "utf8")

libraryDependencies ++= library.elasticsearch

libraryDependencies ++= library.utils

libraryDependencies ++= library.logs

libraryDependencies ++= library.test


    