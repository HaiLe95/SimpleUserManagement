package com.haile.app
package domain.user

/**
 *
 * @param id          Primary Key and Id of our User;
 * @param firstName   Firstname of our User;
 * @param lastName    Lastname of our User;
 * @param birthday    wow the Birthday of User, Option[] because can be Null in DB;
 * @param isAvailable defines if our row is deleted, but in more soft way
 */
case class User(id: Option[Long],
                firstName: String,
                lastName: String,
                birthday: Option[java.sql.Date],
                isAvailable: Boolean)
