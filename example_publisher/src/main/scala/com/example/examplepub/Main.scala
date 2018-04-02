package com.example.examplepub

import java.util.Properties
import java.time.Instant
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.rogach.scallop.ScallopConf
import com.example.examplepub.event.ExampleRecord

object Main {
  //get kafka url and registry url or use default if not specified
  private class Args(args: Array[String]) extends ScallopConf(args) {
    val kafkaUrl = opt[String](required = true, default = Some("http://localhost:9092"))
    val registryUrl = opt[String](required = true, default = Some("http://localhost:8081"))
    verify()
  }

  def main(args: Array[String]): Unit = {
    //command line args
    val cl = new Args(args)

    //producer properties
    val producerProps: Properties = new Properties()

    //set key serializer
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)

    //set value serializer to avro serializer
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getCanonicalName)

    //set kafka url
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cl.kafkaUrl.toOption.get)

    //set schema registry url
    producerProps.put("schema.registry.url", cl.registryUrl.toOption.get)

    //create producer with type [String, ExampleRecord]
    implicit val producer = new KafkaProducer[String, ExampleRecord](producerProps)

    //publish 5 messages
    for (i <- 1 to 5) {
      val currentTime = Instant.now.getEpochSecond
      producer.send(new ProducerRecord("example-topic", new ExampleRecord(currentTime, "hello world " + i.toString)))
      println("publishing message "+ i.toString)
    }

    //close producer
    producer.close()
  }
}
