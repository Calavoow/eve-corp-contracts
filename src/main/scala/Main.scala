import api.{Contract, CharacterInfo}
import com.beimin.eveapi.core.ApiAuthorization
import com.typesafe.scalalogging.LazyLogging
import config.Config

object Main extends LazyLogging {
	def main(args: Array[String]) {
		logger.info("This is the EVE Corporation Contract watcher.")

		val config = Config.readApiConfig("api.conf")
		val (keyId, verifCode) = config match {
			case Some((k, v)) ⇒ (k, v)
			case None ⇒ return
		}
		val auth = new ApiAuthorization(keyId, verifCode)

		val characters = CharacterInfo.getCharacterInfo(auth)
		for(char ← characters) {
			logger.info(char.getName)
		}

		val contracts = Contract.getContracts(auth)
		for(contract ← contracts) {
			contract.getDateAccepted
		}
	}
}
