package com.haile.app
package dao

import com.haile.app.domain.book.{Book, BookSearchParameters}
import com.haile.app.database.BooksTable
import com.haile.app.failure.Failure
import com.haile.app.util.DatabaseFailureMapper
import slick.jdbc.MySQLProfile.api._

import java.sql.SQLException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 *  Provides DAO for Books in MySQL database
 */

class BookDAO {

  // configs db
  val database  = Database.forConfig("mysql")
  val books     = BooksTable.books

  // check if table exist, create if not
  database.run(books.schema.createIfNotExists)

  /**
   * Creates new book in Database;
   * @param book is object representation of row about to be created, id won't be applied;
   * @return the entity with new id or Failure if something went wrong.
   */
  def create(book: Book): Future[Either[Failure, Book]] = {
    val query = books.returning(books.map(_.id)) +=
      Book(None, book.title, book.author, true)
    database.run(
      query)
      .map(newId =>
        Right(
          book.
            copy(
              id = Some(newId))
        )
      )
      .recoverWith {
        case e: SQLException =>
          Future(Left(
            DatabaseFailureMapper.databaseError(e)
          ))
      }
  }

  /**
   * Receive specific book by id, ignore if it's unavailable;
   * @param id is used to track right book;
   * @return the entity, or None if there's no such element.
   */
  def get(id: Long): Future[Option[Book]] = {
    val query = books
      .filter(_.id === id)
      .filter(_.isAvailable)
    database.run(
      query
        .result
        .headOption)
  }

  /**
   * Update some specific row with new data;
   * @param id is used to track the targeted entity;
   * @param book is new data that's about to replace the old one;
   * @return number of updated rows, mostly 0 or 1.
   */
  def update(id: Long, book: Book): Future[Int] = {
    val query = books
      .filter(_.id === id)
    database.run(
      query
        .update(
          Book(
            Some(id),
            book.title,
            book.author,
            book.isAvailable)
        )
    )
  }


  /**
   * Delete the data with specific ID;
   * @param id is used to find right Book;
   * @return number of deleted rows, mostly 0 or 1.
   */
  def delete(id: Long): Future[Int] = {
    val query = books
      .filter(_.id === id)
      .map(_.isAvailable)
    database.run(
      query.update(false)
    )
  }

  /**
   * Get all books that matches with input parameters;
   * @param param our metadata to track specific Book.
   */
  def search(param: BookSearchParameters): Future[Seq[Book]] = {
    val query = books
      .filterOpt(param.title)((book, title) =>
        book.title === title)
      .filterOpt(param.author)((book, author) =>
        book.author === author)
      .filter(_.isAvailable)
    database.run(query.result)
  }

}
