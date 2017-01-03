package controllers

import play.api.i18n.I18nSupport
import play.api.mvc.Controller
import play.modules.reactivemongo.{MongoController, ReactiveMongoComponents}


abstract class FuelMeterController extends Controller with MongoController with ReactiveMongoComponents with I18nSupport