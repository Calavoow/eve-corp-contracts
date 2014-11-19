package api

import java.util.Date
import java.util.concurrent.TimeUnit

import com.beimin.eveapi.core.ApiAuthorization
import com.beimin.eveapi.corporation.contract.ContractsParser
import com.beimin.eveapi.exception.ApiException
import com.beimin.eveapi.shared.contract.EveContract
import com.typesafe.scalalogging.LazyLogging
import rx.functions.Action0
import rx.lang.scala.{Observable, Subscriber}
import rx.schedulers.Schedulers

import scala.collection.JavaConversions._

object Contract extends LazyLogging {
	def contractsObservable(auth: ApiAuthorization): Observable[Set[EveContract]] = {
		val parser = ContractsParser.getInstance()
		def getContracts(subscriber: Subscriber[Set[EveContract]]): Option[Long] = {
			logger.debug("Fetching new contracts")
			try {
				val response = parser.getResponse(auth)
				if(response == null) {
					subscriber.onError(new RuntimeException("Unable to fetch contracts from EVE servers"))
					None
				}
				else if(response.hasError) {
					logger.error(response.getError.toString)
					subscriber.onError(new RuntimeException(response.getError.toString))
					None
				} else {
					subscriber.onNext(response.getAll.toSet)
					Some(response.getCachedUntil.getTime - new Date().getTime)
				}
			} catch {
				case aex: ApiException ⇒
					logger.error("An error occurred when querying the EVE API.")
					logger.debug("ApiException: ", aex)
					subscriber.onError(aex)
					None
			}
		}

		Observable[Set[EveContract]](observer ⇒ {
			val worker = Schedulers.newThread().createWorker()
			def scheduleContracts(delay: Long) {
				worker.schedule(new Action0 {
					override def call(){
						val delay = getContracts(observer)
						delay match {
							// Reschedule a contract fetch after time d has passed.
							case Some(d) ⇒
								logger.debug(s"Rescheduling contract fetch in: ${d/1000} s")
								scheduleContracts(d)
							case _ ⇒
								// Otherwise do nothing
								logger.debug("Not rescheduling contract fetch.")
						}
					}
				}, delay, TimeUnit.MILLISECONDS)
			}
			scheduleContracts(0L)
		})
	}
}
