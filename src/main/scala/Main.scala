import api.Contract
import com.beimin.eveapi.core.ApiAuthorization
import com.typesafe.scalalogging.LazyLogging
import config.Config

import scala.collection.JavaConversions._

object Main extends LazyLogging {
	def main(args: Array[String]) {

		logger.info("This is the EVE Corporation Contract watcher.")

		logger.debug("test")
		val config = Config.readApiConfig("api.conf")
		val (keyId, verifCode) = config match {
			case Some((k, v)) ⇒ (k, v)
			case None ⇒ return
		}
		val auth = new ApiAuthorization(keyId, verifCode)

		val contractsObservable = Contract.contractsObservable(auth)
		contractsObservable.subscribe(contract ⇒ {
			val contracts = contract.getAll
			for(contract ← contracts) {
				println(contract)
			}
		})

		logger.info("Press enter to exit.")
		System.in.read
	}
}
