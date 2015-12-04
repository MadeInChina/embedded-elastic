package com.hrw.testing.es

import java.net._

object TestUtils {


  /**
   * Choose a number of random available ports
   */
  def choosePorts(count: Int): List[Int] = {
    val sockets =
      for (i <- 0 until count)
      yield new ServerSocket(0)
    val socketList = sockets.toList
    val ports = socketList.map(_.getLocalPort)
    socketList.map(_.close)
    ports
  }

  /**
   * Choose an available port
   */
  def choosePort(): Int = choosePorts(1).head
}