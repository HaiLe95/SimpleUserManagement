package com.haile.app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import com.haile.app.domain.{User, UserSearchParameters}
import com.haile.app.dao.UserDAO
import com.haile.app.failure.Failure
import com.haile.app.util.DatabaseFailureMapper
import com.haile.app.util.DateMarshalling._

import scala.concurrent.Future
import scala.util.{Failure, Success}



object Boot {

  implicit val system = ActorSystem(Behaviors.empty, "boot")
  implicit val executionContext = system.executionContext

  implicit val userFormat       = jsonFormat5(User)
  implicit val userParamFormat  = jsonFormat3(UserSearchParameters)

  val userDao = UserDAO

  def main(args: Array[String]): Unit = {

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


    // starting server
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  }



}
