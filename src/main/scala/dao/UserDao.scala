package com.haile.app
package dao

import com.haile.app.domain.UsersTable
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Provides DAL for Users entities for H2 database
 */

class UserDao {

  val database = Database.forConfig("h2mem1")
  val users = UsersTable.users

  // Check if Table exists and create it if not
  val tables = Await.result(database.run(users.result), 3.seconds).toList


}
