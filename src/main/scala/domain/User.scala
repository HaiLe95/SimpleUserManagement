package com.haile.app
package domain

/**
 *
 * @param id          Primary Key and Id of our User; TODO prob could be Option[Long] instead of Long?
 * @param firstName   Firstname of our User;
 * @param lastName    Lastname of our User;
 * @param birthday    wow the Birthday of User, Option[] because can be Null in DB;
 */
case class User(id: Long,
                firstName: String,
                lastName: String,
                birthday: Option[java.util.Date])