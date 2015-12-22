package com.hrw.testing.es

import com.sksamuel.elastic4s.ElasticDsl.{field, index, put, search, _}
import com.sksamuel.elastic4s.mappings.FieldType.{IntegerType, GeoPointType, StringType}
import com.sksamuel.elastic4s.source.ObjectSource
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

@RunWith(classOf[JUnitRunner])
class EmbededElasticsearchServerTest extends Specification with EmbeddedElasticsearch with Logs {
  sequential


  implicit class RichFuture[T](future: Future[T]) {
    def await(implicit duration: Duration = Duration.create("10 seconds")) = Await.result(future, duration)
  }

  step {
    elasticsearchServer.start()
    elasticsearchServer.createIndex("example")
    elasticsearchClient.execute {
      put mapping "example" / "profile" as Seq(
        field name "age" typed IntegerType,
        field name "id" typed StringType index "not_analyzed",
        field name "gender" typed IntegerType,
        field name "location" withType GeoPointType
      )
    }

  }
  "UserProfileIndexRepository" should {
    "return profile id correct" in {
      val userProfileIndex = User(id = "123:qq", name = "test",age = 0,gender = 0,location = Location(0.0,0.0))
      elasticsearchClient.execute {
        index into "example/profile" doc ObjectSource(userProfileIndex)
      }.await
      elasticsearchServer.refresh("example")

      val userProfileIndexList = elasticsearchClient.execute {
        search in "example/profile" query matchall
      }.await.getHits.getHits().map(data => {
        data.sourceAsString().fromJsonString[User]
      })
      userProfileIndexList(0).id shouldEqual "123:qq"
    }

  }

  step {
    elasticsearchServer.stop()
  }
}

case class User(id:String,name:String,age:Int,gender:Int,location:Location)
case class Location(lat: Double, lon: Double)
