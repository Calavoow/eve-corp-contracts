package api

import com.beimin.eveapi.core.ApiAuthorization
import com.beimin.eveapi.corporation.contract.ContractsParser
import com.beimin.eveapi.exception.ApiException
import com.beimin.eveapi.shared.contract.EveContract
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConversions._ // Convert Java objects to Scala objects implicitly

object Contract extends LazyLogging {
	def getContracts(auth: ApiAuthorization): Set[EveContract] = {
		logger.debug("Fetching new contracts")
		val parser = ContractsParser.getInstance()
		try{
			val response = parser.getResponse(auth)
			assert(response != null, "Unable to fetch contracts from EVE servers")
			val contracts  = response.getAll.toSet
			contracts
		} catch {
			case aex: ApiException ⇒
				logger.error("An error occurred when querying the EVE API.")
				logger.debug("ApiException: ", aex)
				Set()
		}
	}
}
