package com.haile.app
package dao

import domain.{Failure, FailureType, User, UserSearchParameters, UsersTable}
import config.Configuration

import slick.jdbc.H2Profile.api._

import java.sql.SQLException
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try


/**
 * Provides DAO for Users entities for H2 database
 */

class UserDAO extends Configuration {

  // Initializing database instance and importing UserTableQuery
  val database = Database.forConfig("h2mem1")
  val users = UsersTable.users


  def create(user: User): Future[Either[Failure, Long]] = {
    val query = users.returning(users.map(_.id)) += user
    database.run(
      query)
      .map(Right(_))
      .recoverWith {
        case e: SQLException => Future(Left(databaseError(e)))
      }
  }


  def get(id: Long): Future[Option[User]] = {
    val query = users.filter(_.id === id)
    database.run(query.result.headOption)
  }



  def update(id: Long, user: User): Future[Int] = {
    val query = users.filter(_.id === id)
    database.run(query.update(user))
  }


  def delete(id: Long): Future[Int] = {
    val query = users.filter(_.id == id)
    database.run(query.delete)
  }

  def search(param: UserSearchParameters): Future[Seq[User]] = {
    val query = users
      .filterOpt(param.firstName)((user, firstname) =>
      user.firstName === firstname)
      .filterOpt(param.lastName)((user, lastname)   =>
      user.lastName === lastname)
      .filterOpt(param.birthday)((user, birthday)   =>
      user.birthday === birthday)
    database.run(query.result)
  }


  // Service method, another dry stuff
  private def databaseError(exception: SQLException): Failure = {
    import domain.FailureType
    Failure("%d: %s"
      .format(exception.getErrorCode, exception.getMessage),
      FailureType.DatabaseFailure)
  }

  //service method
  private def notFoundError(id: Long): Failure = {
    Failure("User with id=%id does not exist"
      .format(id),
      FailureType.NotFound)
  }
  //service method
  private def notFoundError(msg: String): Failure = {
    Failure(msg, FailureType.NotFound)
  }
}
