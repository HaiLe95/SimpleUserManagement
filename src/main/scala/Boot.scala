package com.haile.app

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import controller.UserController



object Boot {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "boot")
    implicit val executionContext = system.executionContext

    val route: Route = UserController.route

    // starting server
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  }

}
