package com.haile.app
package domain

import slick.jdbc.H2Profile.api._

object UsersTable {
  class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    // TODO potential bug >> UserTable Id long User id Option[Long]
    def id:         Rep[Long]           = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def firstName:  Rep[String]         = column[String]("FIRST_NAME")
    def lastName:   Rep[String]         = column[String]("LAST_NAME")
    def birthday:   Rep[java.sql.Date]  = column[java.sql.Date]("BIRTHDAY")

    override def * = (id, firstName, lastName, birthday).mapTo[User]
  }
  val users = TableQuery[UserTable]
}
