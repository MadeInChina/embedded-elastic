package com.hrw.testing.es

import java.io.File
import java.nio.file.Files

import com.sksamuel.elastic4s.ElasticClient
import org.apache.commons.io.FileUtils
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.NodeBuilder._


class EmbeddedElasticsearchServer(val clusterName: String) extends Logs {
  val dataDir = Files.createTempDirectory("embedded_es_data_").toFile

  val scriptDir = new File(dataDir, "config/scripts")
  scriptDir.mkdirs()
  val scriptFile = new File(scriptDir, "demo.groovy")
  val settings = ImmutableSettings.settingsBuilder
    .put("node.data", "true")
    .put("script.groovy.sandbox.enabled", true)
    .put("path.conf", scriptDir.toString)
    .put("path.data", dataDir.toString)
    .put("cluster.name", clusterName)
    .put("es.logger.level", "DEBUG")
    .build

  private lazy val node = nodeBuilder().settings(settings).build

  def client: Client = node.client

  val host = "localhost"
  val port = TestUtils.choosePort()
  val hosts = host + ":" + port

  def start(): Unit = {
    log.info("Starting embedded ES server")
    node.start()
  }

  def stop(): Unit = {
    log.info("Stopping embedded ES server")
    node.close()
    try {
      FileUtils.forceDelete(dataDir)
    } catch {
      case e: Exception => log.info("Unable to delete temporary data directory for ES.", e)
    }
  }

  def createIndex(index: String): Unit = {
    client.admin.indices.prepareCreate(index).execute.actionGet()
    client.admin.cluster.prepareHealth(index).setWaitForActiveShards(1).execute.actionGet()
  }


  def refresh(indexes: String*) {

    val i = indexes.size match {
      case 0 => Seq("_all")
      case _ => indexes
    }
    val listener = client.admin().indices().prepareRefresh(i: _*).execute()
    listener.actionGet()
  }
}

trait EmbeddedElasticsearch {


  val clusterName = "embedded-elasticsearch"

  val elasticsearchServer = new EmbeddedElasticsearchServer(clusterName)
  val elasticsearchClient: ElasticClient = new ElasticClient(elasticsearchServer.client)
}