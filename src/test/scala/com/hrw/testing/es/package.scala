package com.hrw.testing

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization

package object es {
  implicit class StringMapping(val data: String) {
    def fromJsonString[T](implicit m: Manifest[T]): T = {
      implicit val formats = Serialization.formats(NoTypeHints)
      parse(data).extract[T]
    }
  }


}
