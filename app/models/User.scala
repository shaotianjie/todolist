package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
/**
 * Created by stj on 2014/10/30.
 */
case class User(email: String, name: String, password: String) {}
object User {
    val simple = {
      get[String]("user.email") ~
        get[String]("user.name") ~
        get[String]("user.password") map {
        case email~name~password => User(email, name, password)
      }
    }
    def findByEmail(email: String): Option[User] = {
      DB.withConnection { implicit connection =>
        SQL("select * from user where email = {email}").on(
          'email -> email
        ).as(User.simple.singleOpt)
      }
    }
    def findAll: Seq[User] = {
      DB.withConnection { implicit connection =>
        SQL("select * from user").as(User.simple *)
      }
    }
    def authenticate(email: String, password: String): Option[User] = {
      DB.withConnection { implicit connection =>
        SQL(
          """
            select * from user
            where email = {email} and password = {password}
          """
        ).on(
            'email -> email,
            'password -> password
          ).as(User.simple.singleOpt)
      }
    }
    def create(user: User): User = {
      DB.withConnection { implicit connection =>
        SQL(
          """
          insert into user values (
            {email}, {name}, {password}
          )
          """
        ).on(
            'email -> user.email,
            'name -> user.name,
            'password -> user.password
          ).executeUpdate()
        user
      }
    }
}
