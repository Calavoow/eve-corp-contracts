package api

import com.beimin.eveapi.core.ApiAuthorization
import com.beimin.eveapi.corporation.contract.ContractsParser
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConversions._ // Convert Java objects to Scala objects implicitly

object Contract extends LazyLogging {
	def getContracts(auth: ApiAuthorization) = {
		logger.debug("Fetching new contracts")
		val parser = ContractsParser.getInstance()
		val response = parser.getResponse(auth)
		assert(response != null, "Unable to fetch contracts from EVE servers")
		val contracts  = response.getAll
		for(contract ← contracts) {
			val t = contract
			println(contract)
		}
		contracts
	}
}
