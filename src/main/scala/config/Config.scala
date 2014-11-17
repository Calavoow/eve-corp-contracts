package config

import com.typesafe.config.{ConfigFactory, Config}
import com.typesafe.scalalogging.LazyLogging

object Config extends LazyLogging {
	implicit class RichConfig(val underlying: Config) extends AnyVal {
		def getOptional[T](path: String, f: (String ⇒ T)) = if(underlying.hasPath(path)) {
			Some(f(path))
		} else {
			None
		}
	}

	def readApiConfig(location: String) : Option[(Int, String)]= {
		val config = ConfigFactory.load("api")
		val keyOption = config.getOptional("api.keyId", config.getInt)
		keyOption match {
			case None ⇒
				logger.error("You did not provide a keyId in the api.conf")
			case _ ⇒
		}
		val verifCodeOption = config.getOptional("api.verificationCode", config.getString)
		verifCodeOption match {
			case None ⇒
				logger.error("You did not provide a valid verificationCode in the api.conf")
			case _ ⇒
		}
		for(key ← keyOption; verif ← verifCodeOption) yield { (key, verif) }
	}
}
