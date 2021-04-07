package com.haile.app
package dao

import domain.{Failure, FailureType, User, UsersTable}
import config.Configuration

import slick.jdbc.H2Profile.api._

import java.sql.SQLException
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Provides DAO for Users entities for H2 database
 */

class UserDao extends Configuration{

  // Initializing database instance and importing UserTableQuery
  val database = Database.forConfig("h2mem1")
  val users = UsersTable.users

  // Check if Table exists and create it if not
  if(execute(users.result).toList.isEmpty) {
    execute(users.schema.create)
  }

  /**
   *
   * @param user is basically our User entity, that we want to save into DB
   * @return in case of Right answer we will be our user with his new ID, if not,
   *         something else happens, probably the Failure object with Database
   *         error as value.
   */
  def create(user: User): Either[Failure, User] = {
    try {
      val id : Long = execute(users returning users.map(_.id) += user)
      Right(user.copy(id = Some(id)))
    } catch {
      case e: SQLException => Left(databaseError(e))
    }
  }

  def get(id: Long): Either[Failure, User] = {
    try {
      val user : User = execute(users.filter(_.id === id).result).head
      Right(user)
    } catch {
      case e: SQLException => Left(notFoundError(id))
    }
  }

  def update(id: Long, user: User) /*: Either[Failure, User]*/ = {

  }

  def delete(id: Long): Either[Failure, User] = {
    try {
      val user: User = execute(users.filter(_.id === id).result).head
      execute(users.filter(_.id === id).delete)
      Right(user)
    } catch {
      case e: SQLException => Left(databaseError(e))
    }
  }

  def search() = {
    //TODO crate search method
  }

  // Private service method with side-effects, not safe to leave public. Also keep code DRY.
  private def execute[T](action: DBIO[T]): T = {
    Await.result(database.run(action), 2.seconds)
  }

  // Service method, another dry stuff
  protected def databaseError(exception: SQLException): Failure = {
    import domain.FailureType
    Failure("%d: %s".format(exception.getErrorCode, exception.getMessage), FailureType.DatabaseFailure)
  }

  protected def notFoundError(id: Long): Failure = {
    Failure("Customer with id=%id does not exist".format(id), FailureType.NotFound)
  }
}
