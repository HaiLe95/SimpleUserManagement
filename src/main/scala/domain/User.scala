package com.haile.app
package domain

case class User(val id: Long,
                val firstName: String,
                val lastName: String,
                val birthday: Option[java.util.Date])