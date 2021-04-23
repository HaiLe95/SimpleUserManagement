package com.haile.app
package database

import domain.User

import slick.jdbc.H2Profile.api._

object UsersTable {

  class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id:           Rep[Long]           = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def firstName:    Rep[String]         = column[String]("FIRST_NAME")
    def lastName:     Rep[String]         = column[String]("LAST_NAME")
    def birthday:     Rep[java.sql.Date]  = column[java.sql.Date]("BIRTHDAY")
    def isAvailable:  Rep[Boolean]        = column[Boolean]("IS_DELETED")

    override def * = (id.?, firstName, lastName, birthday.?, isAvailable).mapTo[User]
  }

  val users = TableQuery[UserTable]
}
