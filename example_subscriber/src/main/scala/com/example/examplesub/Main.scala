package com.example.examplesub

import java.util
import java.util.Properties

import com.example.examplepub.event.ExampleRecord
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import org.rogach.scallop.ScallopConf

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

    //consumer properties
    val consumerProps: Properties = new Properties()

    //set deserializer to same classes as producer's serializer
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getCanonicalName)

    //set kafka url and schema registry url
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cl.kafkaUrl.toOption.get)
    consumerProps.put("schema.registry.url", cl.registryUrl.toOption.get)

    //set group id and set specific avro reader to true
    consumerProps.put("group.id", "example-consumer-group")
    consumerProps.put("specific.avro.reader", "true")

    //create new consumer
    val consumer = new  KafkaConsumer[String, ExampleRecord](consumerProps)

    //close consumer on ctrl-c
    Runtime.getRuntime.addShutdownHook(new Thread(() => consumer.close()))

    //subscribe to producer's topic
    consumer.subscribe(util.Arrays.asList("example-topic"))

    //poll for new messages every two seconds
    while(true) {
      val records = consumer.poll(2000)

      //print each received record
      records.forEach(record => {
        println("received message: " + record.value.msg + " timestamp: " + record.value.timestamp)
      })

      //commit offsets on last poll
      consumer.commitSync()
    }
  }
}
