import java.text.SimpleDateFormat
import java.util.Date

import api.Contract
import com.beimin.eveapi.core.ApiAuthorization
import com.beimin.eveapi.shared.contract.{ContractStatus, EveContract}
import com.github.tototoshi.csv.CSVReader
import com.typesafe.scalalogging.LazyLogging
import config.Config

object Main extends LazyLogging {
	def main(args: Array[String]) {

		logger.info("This is the EVE Corporation Contract watcher.")
		logger.warn("CCP has put in restrictions for fetching contracts.\nRestart this program repeatedly at your own risk.")

		val config = Config.readApiConfig("api.conf")
		val (keyId, verifCode) = config match {
			case Some((k, v)) ⇒ (k, v)
			case None ⇒ return
		}
		val auth = new ApiAuthorization(keyId, verifCode)

		val stationMap = readStations("src/main/resources/staStations.csv")
		val contractsObservable = Contract.contractsObservable(auth)
		contractsObservable.subscribe(contracts ⇒ {
			val outstandingContracts = contracts.filter(_.getStatus == ContractStatus.OUTSTANDING)
			logger.info(contractsToString(outstandingContracts, stationMap))
		})

		logger.info("Press enter to exit.")
		System.in.read
	}

	def readStations(filename: String): Map[Int, String] = {
		val reader = CSVReader.open(filename)
		val withHeaders = reader.all().drop(1)
		withHeaders.map(list ⇒ {
			list(0).toInt → list(1)
		}).toMap
	}

	def contractsToString(contracts: Set[EveContract], stationMap: Map[Int, String]): String = {
		if(contracts.isEmpty) {
			"No outstanding contracts."
		} else {
			val sortedContracts = contracts.toList.sortBy(_.getDateIssued) // Earliest issued contracts first.
			val header = List("Station Name", "Issue time (Eve Time)")
			val simpleTime = new SimpleDateFormat("HH:mm")
			// Create the columns with [Station name, Issue Time]
			val columns = (for(contract ← sortedContracts) yield {
				val stationId = contract.getStartStationID
				val stationName = stationMap.getOrElse(stationId, stationId.toString)
				val dateIssued = contract.getDateIssued
				val eveDate = new Date(dateIssued.getTime - 1000L*60L*60L)
				List(stationName, simpleTime.format(eveDate))
			}).toList
			// Then align the columns nicely, so that it is human-readable.
			val paddedColumns = util.AlignColumns.align(header :: columns)
			"Available contracts:\n" + paddedColumns.mkString("\n")
		}
	}
}
