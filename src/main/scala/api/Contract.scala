package api

import com.beimin.eveapi.core.ApiAuthorization
import com.beimin.eveapi.corporation.contract.ContractsParser

import scala.collection.JavaConversions._ // Convert Java objects to Scala objects implicitly

object Contract {
	def getContracts(auth: ApiAuthorization) = {
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
