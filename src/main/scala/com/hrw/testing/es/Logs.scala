package com.hrw.testing.es

import org.slf4j.LoggerFactory

trait Logs {
  val log = LoggerFactory.getLogger(this.getClass)
}
