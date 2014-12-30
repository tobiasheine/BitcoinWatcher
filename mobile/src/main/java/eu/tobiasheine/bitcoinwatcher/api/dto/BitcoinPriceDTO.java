package eu.tobiasheine.bitcoinwatcher.api.dto;

/**
 * {
 "time": {
 "updated": "Dec 28, 2014 10:56:00 UTC",
 "updatedISO": "2014-12-28T10:56:00+00:00",
 "updateduk": "Dec 28, 2014 at 10:56 GMT"
 },
 "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
 "bpi": {
 "USD": {
 "code": "USD",
 "symbol": "&#36;",
 "rate": "314.3260",
 "description": "United States Dollar",
 "rate_float": 314.326
 },
 "GBP": {
 "code": "GBP",
 "symbol": "&pound;",
 "rate": "202.0255",
 "description": "British Pound Sterling",
 "rate_float": 202.0255
 },
 "EUR": {
 "code": "EUR",
 "symbol": "&euro;",
 "rate": "257.9111",
 "description": "Euro",
 "rate_float": 257.9111
 }
 }
 }
 */
public class BitcoinPriceDTO {

    private BitcoinPriceUpdatedAtDTO time;
    private String disclaimer;
    private BitcoinPriceBpiDTO bpi;

    public BitcoinPriceUpdatedAtDTO getTime() {
        return time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public BitcoinPriceBpiDTO getBpi() {
        return bpi;
    }
}
