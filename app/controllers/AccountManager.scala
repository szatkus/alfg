package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object Accounts extends Controller {

    def register = Action { request =>
        request.method match {
            case "GET" => Ok(views.html.register())
        }
    }

}
