import sbt._


object library {
  val logs = Seq(
    "org.slf4j" % "jul-to-slf4j" % "1.7.7",
    "org.slf4j" % "log4j-over-slf4j" % "1.7.7",
    "ch.qos.logback" % "logback-classic" % "1.1.3"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "2.2.5" % "test",
    "org.specs2" % "specs2_2.11" % "2.3.13" % "test"
  )

  val utils = Seq(
    "commons-io" % "commons-io" % "2.4",
    "org.json4s" %% "json4s-jackson" % "3.2.9",
    "org.codehaus.groovy" % "groovy-all" % "2.3.2",
    "org.apache.lucene" % "lucene-expressions" % "4.10.4"

  )


  val elasticsearch = Seq(
    "com.sksamuel.elastic4s" %% "elastic4s-core" % "1.7.4",
    "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "1.7.4",
    "com.sksamuel.elastic4s" %% "elastic4s-json4s" % "1.7.4",
    "org.elasticsearch" % "elasticsearch" % "1.7.3"
  ) map (_
    withSources())

}