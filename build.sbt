import Deps._
import sbt.Keys.scalaVersion

lazy val root = (project in file(".")).
  aggregate(examplePublisher, exampleSubscriber).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      resolvers += "io.confluent" at "http://packages.confluent.io/maven/"
    )),
    name := "scala_kafka_avro_example"
  )
lazy val examplePublisher = (project in file ("example_publisher")).
  settings(
    name := "example_publisher",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("com.example.examplepub.Main"),
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      scalaLogging,
      scallop)
  )

lazy val exampleSubscriber = (project in file ("example_subscriber")).
  settings(
    name := "example_subscriber",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("com.example.examplesub.Main"),
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      scalaLogging,
      scallop)
  ).dependsOn(examplePublisher)


