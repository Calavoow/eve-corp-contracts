import api.CharacterInfo
import com.beimin.eveapi.core.ApiAuthorization
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {
	def main(args: Array[String]) = {
		logger.info("This is the EVE Corproration Contract watcher.")
		val config = ConfigFactory.load("api")
		val keyId = config.getInt("api.keyId")
		val verificationCode = config.getString("api.verificationCode")
		val auth = new ApiAuthorization(keyId, verificationCode)

		CharacterInfo.getCharacterInfo(auth)
	}
}