package api

import com.beimin.eveapi.account.characters.CharactersParser
import com.beimin.eveapi.core.ApiAuthorization

import scala.collection.JavaConversions._

object CharacterInfo {
	def getCharacterInfo(auth: ApiAuthorization) = {
		val parser = CharactersParser.getInstance()
		val response = parser.getResponse(auth)
		println(response)
		val infos = response.getAll
		for(info ← infos) {
			println(info)
			println(info.getName)
		}
		infos
	}
}
