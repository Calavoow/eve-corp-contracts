package api

import com.beimin.eveapi.account.characters.{EveCharacter, CharactersParser}
import com.beimin.eveapi.core.ApiAuthorization
import com.beimin.eveapi.exception.ApiException
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConversions._

object CharacterInfo extends LazyLogging {
	def getCharacterInfo(auth: ApiAuthorization): Set[EveCharacter] = {
		logger.debug("Fetching new character info")
		val parser = CharactersParser.getInstance()
		try {
			val response = parser.getResponse(auth)
			logger.trace("server response {}", response)
			val characterInfos = response.getAll.toSet
			logger.trace("Character infos {}", characterInfos)
			characterInfos
		} catch {
			case aex : ApiException ⇒
				logger.error("An error occurred when querying the EVE API: {}", aex)
				Set()
		}
	}
}
