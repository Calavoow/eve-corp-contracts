import api.CharacterInfo
import com.beimin.eveapi.core.ApiAuthorization
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import config.Config.RichConfig

object Main extends LazyLogging {
	def main(args: Array[String]) {
		logger.info("This is the EVE Corporation Contract watcher.")
		val config = ConfigFactory.load("api")
		val keyOption = config.getOptional("api.keyId", config.getInt)
		val keyId = keyOption match {
			case Some(key) ⇒ key
			case None ⇒
				logger.error("You did not provide a keyId in the api.conf")
				return
		}
		val verifCodeOption = config.getOptional("api.verificationCode", config.getString)
		val verifCode = verifCodeOption match {
			case Some(verif) ⇒ verif
			case None ⇒
				logger.error("You did not provide a valid verificationCode in the api.conf")
				return
		}
		val auth = new ApiAuthorization(keyId, verifCode)

		val characters = CharacterInfo.getCharacterInfo(auth)
		for(char ← characters) {
			logger.info(char.getName)
		}
	}
}
