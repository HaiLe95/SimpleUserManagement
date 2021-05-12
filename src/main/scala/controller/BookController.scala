package com.haile.app
package controller

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json._
import dao.{BookDAO, UserDAO}
import util.DatabaseFailureMapper

import com.haile.app.domain.book.{Book, BookSearchParameters}

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure


object BookController {
  implicit val bookFormat:      RootJsonFormat[Book]                  = jsonFormat4(Book)
  implicit val bookParamFormat: RootJsonFormat[BookSearchParameters]  = jsonFormat2(BookSearchParameters)

  val bookDao: BookDAO = new BookDAO

  private val book = "book"

  val route: Route =
    concat(

      /**
       * Get Book
       */
      get {
        pathPrefix(book / LongNumber) { id =>
          val maybeBook : Future[Option[Book]] =
            bookDao.get(id)

          onComplete(maybeBook) {
            case Success(Some(book))  => complete(book)
            case Success(None)        => complete(
              DatabaseFailureMapper
              .notFoundError(id)
              .getStatusCode()
            )
            case Failure(exception)   => complete("Failure")
          }
        }
      },

      /**
       * Create Book
       */
      post {
        path(book) {
          entity(as[Book]) { book =>
              val maybeSaved: Future[Either[failure.Failure, Book]] =
                bookDao.create(book)

            onComplete(maybeSaved) {
              case Success(Right(_))      => complete("created")
              case Success(Left(fail))    => complete(fail.getStatusCode())
              case Failure(exception)     => complete("Failure")
            }
          }
        }
      },

      /**
       * Update Book
       */
      put {
        path(book / LongNumber) { id =>
          entity(as[Book]) {
            book =>
              val maybeUpdated: Future[Int] =
                bookDao.update(id, book)

              onComplete(maybeUpdated) {
                case Success(1)         => complete("updated")
                case Success(_)         => complete("not ok")
                case Failure(exception) => complete("Failure")
              }
            }
        }
      },

      /**
       * Soft Delete Book
       */
      delete {
        pathPrefix(book / LongNumber) { id =>
          val maybeDeleted: Future[Int] =
            bookDao.delete(id)

          onComplete(maybeDeleted) {
            case Success(1)         => complete("deleted")
            case Success(_)         => complete("not ok")
            case Failure(exception) => complete("Failure")
          }
        }
      }

    )
}
