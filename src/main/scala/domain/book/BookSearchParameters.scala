package com.haile.app
package domain.book

/**
 *
 * @param title   param to track specific Books
 * @param author  param to track specific Books
 */
case class BookSearchParameters(title:  Option[String] = None,
                                author: Option[String] = None)
