package com.haile.app
package domain


/** The Official documentation by Hai Le, 07.04.21
 * What the hell in the world is this???
 * How does it even works???
 */

import spray.http.{StatusCodes, StatusCode}

/**
 * Service failure description
 * @param message     the error message
 * @param errorType   the error type
 */
case class Failure(message: String, errorType: FailureType.Value) {
  /**
   * Return corresponding HTTP status code for failure specified type
   * @return HTTP status code
   */
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
