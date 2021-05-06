package com.haile.app
package failure

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCode

/**
 * Service failure description
 * @param message     the error message
 * @param errorType   the error type
 */
case class Failure(message: String, errorType: FailureType.Value) {
  def getStatusCode(): StatusCode = {
    FailureType.withName(this.errorType.toString) match {
      case FailureType.BadRequest       => StatusCodes.BadRequest
      case FailureType.NotFound         => StatusCodes.NotFound
      case FailureType.Duplicate        => StatusCodes.Forbidden
      case FailureType.DatabaseFailure  => StatusCodes.InternalServerError
      case _                            => StatusCodes.InternalServerError
    }
  }
}

/**
 * The possible failure types
 */
object FailureType extends Enumeration {
  type Failure = Value

  val BadRequest:       Value = Value("bad_request")
  val NotFound:         Value = Value("not_found")
  val Duplicate:        Value = Value("entity_exist")
  val DatabaseFailure:  Value = Value("database_error")
  val InternalError:    Value = Value("internal_error")
}
