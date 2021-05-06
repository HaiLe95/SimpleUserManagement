package com.haile.app
package domain.user

/**
 * Field that represent parameters to search a User
 *
 * @param firstName a param to search
 * @param lastName  a param to search
 * @param birthday  a param to search
 */
case class UserSearchParameters(firstName:  Option[String] = None,
                                lastName:   Option[String] = None,
                                birthday:   Option[java.sql.Date] = None)
