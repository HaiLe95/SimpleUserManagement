package com.haile.app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import com.haile.app.domain.User
import com.haile.app.dao.UserDAO
import com.haile.app.failure.Failure
import com.haile.app.util.DatabaseFailureMapper
import com.haile.app.util.DateMarshalling._

import scala.concurrent.Future
import scala.util.{Success, Failure}



object Boot {

  implicit val system = ActorSystem(Behaviors.empty, "boot")
  implicit val executionContext = system.executionContext

  implicit val userFormat = jsonFormat4(User)

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

      )

    // starting server
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  }



}
