package com.haile.app
package dao

import domain.{Failure, FailureType, User, UserSearchParameters, UsersTable}
import config.Configuration

import slick.jdbc.H2Profile.api._

import java.sql.SQLException
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global



/**
 * Provides DAO for Users entities for H2 database
 */

class UserDAO extends Configuration {

  // Initializing database instance and importing UserTableQuery
  val database = Database.forConfig("h2mem1")
  val users = UsersTable.users

  /**
   * Creates a new row in Database //TODO prob can have some issues with
   * @param user is object representation of our row, but the ID won't be applied
   * @return the user's id or Failure if exception is trowed
   */
  def create(user: User): Future[Either[Failure, Long]] = {
    val query = users.returning(users.map(_.id)) +=
      User(None, user.firstName, user.lastName, user.birthday)
    database.run(
      query)
      .map(Right(_))
      .recoverWith {
        case e: SQLException => Future(Left(databaseError(e)))
      }
  }

  /**
   * Receive a specific user
   * @param id is used to track the right entity
   * @return the user it self, or if there's no row None instead
   */
  def get(id: Long): Future[Option[User]] = {
    val query = users.filter(_.id === id)
    database.run(
      query
        .result
        .headOption)
  }

  /**
   * Update the specific row with new entity
   * @param id that used to find the right row
   * @param user is a new data that we want to update our user
   * @return numbers of changed rows(in most cases will be 1), or exception
   */
  def update(id: Long, user: User): Future[Int] = {
    val query = users.filter(_.id === id)
    database.run(
      query
        .update(user))
  }

  /**
   * The delete specific row by id
   * @param id is used to find right user
   * @return numbers of deleted rows
   */
  def delete(id: Long): Future[Int] = {
    val query = users.filter(_.id == id)
    database.run(
      query
        .delete)
  }

  /**
   * Get all users with specified parameters
   * @param param all those fields to search in our database
   * @return the Sequence of Users
   */
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

  // Service method
  private def databaseError(exception: SQLException): Failure = {
    import domain.FailureType
    Failure("%d: %s"
      .format(exception.getErrorCode, exception.getMessage),
      FailureType.DatabaseFailure)
  }

  //service method TODO maybe should be deleted
  private def notFoundError(id: Long): Failure = {
    Failure("User with id=%id does not exist"
      .format(id),
      FailureType.NotFound)
  }
  //service method TODO maybe should be deleted
  private def notFoundError(msg: String): Failure = {
    Failure(msg, FailureType.NotFound)
  }
}
