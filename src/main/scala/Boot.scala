package com.haile.app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import controller.{BookController, UserController}

object Boot {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "boot")
    implicit val executionContext = system.executionContext

    val bookRoute: Route = BookController.route
    val userRoute: Route = UserController.route

    val mainRoute: Route = concat(bookRoute, userRoute)


    // starting server
    val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(mainRoute)

  }

}
