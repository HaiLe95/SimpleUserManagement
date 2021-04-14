package com.haile.app
package util

import failure.{Failure, FailureType}

import java.sql.SQLException

object DatabaseFailureMapper {

  def databaseError(exception: SQLException): Failure = {
    Failure("%d: %s"
      .format(
        exception.getErrorCode,
        exception.getMessage
      ),
      FailureType.DatabaseFailure)
  }

  def notFoundError(id: Long): Failure = {
    Failure("User with id=%id does not exist"
      .format(id),
      FailureType.NotFound)
  }
}
