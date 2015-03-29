package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
/**
 * Created by stj on 2014/10/30.
 */
case class Task(id: Long, label: String, email: String){}
object Task {
  val task = {
    get[Long]("id") ~
      get[String]("label") ~
      get[String]("email") map {
      case id~label~email => Task(id,label,email)
    }
  }
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }
  def allByUser(email: String) : List[Task] = DB.withConnection { implicit c =>
    SQL(
      """
         select * from task
         where email = {email}
      """
    ).on(
      'email -> email
      ).as(Task.task *)
  }
  def create(label: String, email: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into task (label,email) values ( {label}, {email})").on(
          'label -> label,
          'email -> email
        ).executeUpdate()
    }
  }

  def delete(id: Long, user:String): Unit = {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id} and email = {email}").on(
      'id -> id,
      'email -> user
      ).executeUpdate()
    }
  }

}

