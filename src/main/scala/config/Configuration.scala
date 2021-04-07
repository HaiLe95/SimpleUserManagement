package com.haile.app
package config

import com.typesafe.config.ConfigFactory

import util.Try

trait Configuration {

  val config = ConfigFactory.load()

  lazy val serviceHost = Try(config.getString("service.host")).getOrElse("localhost")

  lazy val servicePort = Try(config.getInt("service.port")).getOrElse(8080)

  lazy val databaseHost = Try(config.getString("h2mem1.host")).getOrElse("localhost")

  lazy val databasePort = Try(config.getInt("h2mem1.port")).getOrElse(3306)

  lazy val databaseName = Try(config.getString("h2mem1.name")).getOrElse("rest")
}
