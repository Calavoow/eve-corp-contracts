package config

import com.typesafe.config.Config

object Config {
	implicit class RichConfig(val underlying: Config) extends AnyVal {
		def getOptional[T](path: String, f: (String ⇒ T)) = if(underlying.hasPath(path)) {
			Some(f(path))
		} else {
			None
		}
	}
}
