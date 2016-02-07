package unit.models

import org.scalatestplus.play._
import testdata._
import play.api.libs.json._
import models.Trend

class TrendSpec extends PlaySpec {
  "A trend" can {
    "be read from json" in {      
      Json.parse(aTrend).validate[Trend].get must be(aTrendModel)
    }
    
    "be read from json array" in {
      (Json.parse(trendsResponse) \\ "trends")(0).validate[List[Trend]].get must be(trendsList)
    }
  }
}