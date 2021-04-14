package com.haile.app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import com.haile.app.domain.User
import com.haile.app.dao.UserDAO
import com.haile.app.failure.Failure
import com.haile.app.util.DateMarshalling._

import scala.io.StdIn
import scala.concurrent.Future


object Boot {

  implicit val system = ActorSystem(Behaviors.empty, "Spray Example")
  implicit val executionContext = system.executionContext


  implicit val userFormat = jsonFormat4(User)


  val userDao: UserDAO = new UserDAO

  def main(args: Array[String]): Unit = {

    val route: Route =
      concat(

        // Get by ID
        get {
          pathPrefix("user" / LongNumber) { id =>
            val maybeUser: Future[Option[User]] =
                userDao.get(id)

            onSuccess(maybeUser) {
              case Some(user) => complete(user)
              case None       => complete(StatusCodes.NotFound)
            }
          }
        }

      )

  }



}
