package com.haile.app
package dao

import com.haile.app.domain.User
import com.haile.app.domain.UserSearchParameters
import com.haile.app.database.UsersTable
import com.haile.app.failure.Failure
import com.haile.app.util.DatabaseFailureMapper
import slick.jdbc.H2Profile.api._

import java.sql.SQLException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global




/**
 * Provides DAO for Users entities for H2 database
 */

object UserDAO {

  // Initializing database instance and importing UserTableQuery
  val database = Database.forConfig("h2mem1")
  val users = UsersTable.users

  // check if table exists
  database.run(users.schema.createIfNotExists)

  /**
   * Creates a new row in Database
   * @param user is object representation of our row, but the ID won't be applied
   * @return the entity with new id or Failure if exception is thrown
   */
  def create(user: User): Future[Either[Failure, User]] = {
    val query = users.returning(users.map(_.id)) +=
      User(None, user.firstName, user.lastName, user.birthday, true)
    database.run(
      query)
      .map(newId =>
        Right(
          user
            .copy(id = Some(newId))
        )
      )
      .recoverWith {
        case e: SQLException =>
          Future(Left(
            DatabaseFailureMapper
              .databaseError(e)
          ))
      }
  }

  /**
   * Receive a specific user
   * @param id is used to track the right entity
   * @return the user it self, or if there's no row None instead
   */
  def get(id: Long): Future[Option[User]] = {
    val query = users
      .filter(_.id === id)
      .filter(_.isAvailable)
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
        .update(
          User(Some(id),
            user.firstName,
            user.lastName,
            user.birthday,
            user.isAvailable)
        ))
  }

  /**
   * The delete specific row by id
   * @param id is used to find right user
   * @return numbers of "deleted" users. The true is all of them become not available.
   */
  def delete(id: Long): Future[Int] = {
    val query = users
      .filter(_.id === id)
      .map(_.isAvailable)
    database.run(
      query
        .update(false))
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
      .filter(_.isAvailable)
    database.run(query.result)
  }

}
