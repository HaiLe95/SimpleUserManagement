package com.haile.app
package domain.book

/**
 *  The Book entity
 * @param id            the Primary key of the Entity;
 * @param title         name of the Book;
 * @param author        who wrote the Book;
 * @param isAvailable   defines if row is soft deleted.
 */
case class Book(id:     Option[Long],
                title:        String,
                author:       String,
                isAvailable:  Boolean)
