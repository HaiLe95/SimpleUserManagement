package com.haile.app
package controller

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json._
import dao.UserDAO
import util.DatabaseFailureMapper
import util.DateMarshalling.DateFormat

import com.haile.app.domain.user.{User, UserSearchParameters}

import scala.concurrent.Future
import scala.util.Success

object UserController {

  implicit val userFormat:      RootJsonFormat[User]                  = jsonFormat5(User)
  implicit val userParamFormat: RootJsonFormat[UserSearchParameters]  = jsonFormat3(UserSearchParameters)

  val userDao: UserDAO = new UserDAO

  val route: Route =
    concat(

      // Get User by ID
      get {
        pathPrefix("user" / LongNumber) { id =>
          val maybeUser: Future[Option[User]] =
            userDao.get(id)

          onSuccess(maybeUser) {
            case Some(user) => complete(user)
            case None       => complete(
              DatabaseFailureMapper
                .notFoundError(id)
                .getStatusCode()
            )
          }
        }
      },

      // Create User
      post {
        path("user") {
          entity(as[User]) {
            user =>
              val saved: Future[Either[failure.Failure, User]] =
                userDao.create(user)

              onComplete(saved) {
                case Success(Right(_))      => complete("ok")
                case Success(Left(failure)) => complete(failure.getStatusCode())
                case scala.util.Failure(t)  => failWith(t)
              }
          }
        }
      },

      // Update User
      put {
        path("user" / LongNumber) {
          id =>
            entity(as[User]) {
              user =>
                val changed: Future[Int] =
                  userDao.update(id, user)

                onComplete(changed) {
                  case Success(1) => complete("updated")
                  case Success(_) => complete(
                    DatabaseFailureMapper
                      .notFoundError(id)
                      .getStatusCode()
                  )
                  case scala.util.Failure(t) => failWith(t)
                }

            }
        }
      },

      // Delete User
      delete {
        path("user" / LongNumber) {
          id =>
            val deleted: Future[Int] =
              userDao.delete(id)

            onComplete(deleted) {
              case Success(1) => complete("deleted")
              case Success(_) => complete(
                DatabaseFailureMapper
                  .notFoundError(id)
                  .getStatusCode()
              )
              case scala.util.Failure(t) => failWith(t)
            }
        }
      },

      // Search User by Parameters
      get {
        path("user") {
          entity(as[UserSearchParameters]) {
            userSearch =>
              val found: Future[Seq[User]] =
                userDao.search(userSearch)

              onComplete(found) {
                case Success(users)         => complete(users)
                case scala.util.Failure(t)  => failWith(t)
              }
          }
        }
      }
    )
}
