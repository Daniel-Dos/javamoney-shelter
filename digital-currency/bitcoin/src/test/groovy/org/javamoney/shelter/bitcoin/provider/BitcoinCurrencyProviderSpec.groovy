package org.javamoney.shelter.bitcoin.provider

import org.javamoney.moneta.CurrencyUnitBuilder
import org.javamoney.moneta.Money
import spock.lang.Shared
import spock.lang.Specification

import javax.money.CurrencyQuery
import javax.money.CurrencyQueryBuilder
import javax.money.CurrencyUnit
import javax.money.MonetaryAmount
import javax.money.Monetary
import javax.money.UnknownCurrencyException

/**
 * Spock test specification for BitcoinCurrencyProvider

 * @author Sean Gilligan
 */
class BitcoinCurrencyProviderSpec extends Specification {

    @Shared BitcoinCurrencyProvider provider

    def setup() {
        provider = new BitcoinCurrencyProvider()
    }

    def "can create new instance"() {
        expect:
        provider != null
    }

    def "returns Bitcoin for empty query" () {
        when:
        CurrencyQuery query = CurrencyQueryBuilder.of().build()
        Set<CurrencyUnit> currencies = provider.getCurrencies(query)
        CurrencyUnit btc = (CurrencyUnit) currencies.toArray()[0]

        then:
        currencies.size() == 1
        btc.getCurrencyCode() == "BTC"
        btc.getDefaultFractionDigits() == 8
    }

    def "returns Bitcoin for BTC query" () {
        when:
        CurrencyQuery query = CurrencyQueryBuilder.of().setCurrencyCodes("BTC").build()
        Set<CurrencyUnit> currencies = provider.getCurrencies(query)
        CurrencyUnit btc = (CurrencyUnit) currencies.toArray()[0]

        then:
        currencies.size() == 1
        btc.getCurrencyCode() == "BTC"
        btc.getDefaultFractionDigits() == 8
    }

    def "returns empty for USD query" () {
        when:
        CurrencyQuery query = CurrencyQueryBuilder.of().setCurrencyCodes("USD").build()
        Set<CurrencyUnit> currencies = provider.getCurrencies(query)

        then:
        currencies.size() == 0
    }

    def "BTC can't be found via Monetary" () {
        when: "We try to get currency 'BTC' when BitcoinCurrencyProvider is not registered"
        CurrencyUnit btc = Monetary.getCurrency("BTC")

        then: "An UnknownCurrencyException is thrown"
        UnknownCurrencyException e = thrown()
    }

    def "we can create money with Money.of"() {
        when:
        CurrencyQuery query = CurrencyQueryBuilder.of().setCurrencyCodes("BTC").build()
        Set<CurrencyUnit> currencies = provider.getCurrencies(query)
        CurrencyUnit btc = (CurrencyUnit) currencies.toArray()[0]
        MonetaryAmount oneBitcoin = Money.of(1, btc)

        then:
        oneBitcoin.number.longValueExact() == 1L
        oneBitcoin.currency == btc
    }

    def "we can create money with Money.of without using a provider"() {
        when:
        CurrencyUnit btc = CurrencyUnitBuilder.of("BTC", "ad hoc btc provider").build()
        MonetaryAmount oneBitcoin = Money.of(1, btc)

        then:
        oneBitcoin.number.longValueExact() == 1L
        oneBitcoin.currency == btc
    }
}