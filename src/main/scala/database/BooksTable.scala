package com.haile.app
package database

import com.haile.app.domain.book.Book
import slick.jdbc.MySQLProfile.api._

object BooksTable {

  class BookTable(tag: Tag) extends Table[Book](tag, "BOOKS") {
    def id:           Rep[Long]     = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def title:        Rep[String]   = column[String]("TITLE")
    def author:       Rep[String]   = column[String]("AUTHOR")
    def isAvailable:  Rep[Boolean]  = column[Boolean]("NOT_DELETED")

    override def *  = (id.?, title, author, isAvailable).mapTo[Book]
  }

  val books = TableQuery[BookTable]
}
