import sbt._

object Deps {
  val avroVersion = "1.8.2"
  val kafkaVersion = "1.0.0"
  val kafkaStreamsScalaVersion = "0.1.0"
  val scalaArmVersion = "2.0"
  val scalaLoggingVersion = "3.7.2"
  val scalaTestVersion = "3.0.4"
  val scallopVersion = "3.1.1"
  val avroSerializerVersion = "4.0.0"
  val jacksonVersion = "2.9.4"

  lazy val jacksonCore = "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion
  lazy val jacksonScala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
  lazy val avro = "org.apache.avro" % "avro" % avroVersion
  lazy val avroSerializer = "io.confluent" % "kafka-avro-serializer" % avroSerializerVersion
  lazy val avroStreamsSerializer = "io.confluent" % "kafka-streams-avro-serde" % avroSerializerVersion
  lazy val kafkaClient = "org.apache.kafka" % "kafka-clients" % kafkaVersion
  lazy val kafkaStreams = "org.apache.kafka" % "kafka-streams" % kafkaVersion
  lazy val kafkaStreamsScala = "com.lightbend" %% "kafka-streams-scala" % kafkaStreamsScalaVersion
  lazy val scalaArm = "com.jsuereth" %% "scala-arm" % scalaArmVersion
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
  lazy val scallop = "org.rogach" %% "scallop" % scallopVersion

}
