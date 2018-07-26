/*
 * Copyright (c) 2013, 2015, Werner Keil and others by the @author tag.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.javamoney.shelter.bitcoin.provider;

import javax.money.CurrencyContext;
import javax.money.CurrencyContextBuilder;
import javax.money.CurrencyQuery;
import javax.money.CurrencyUnit;
import javax.money.spi.CurrencyProviderSpi;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.javamoney.moneta.CurrencyUnitBuilder;

/**
 *  A clueless attempt at a BitcoinCurrencyProvider based on (out of date?) code
 *  in the Moneta User's Guide.
 *
 * @author Sean Gilligan
 * @author Werner Keil
 */
public final class BitcoinCurrencyProvider implements CurrencyProviderSpi {

    final static int bitcoinFractionDigits = 8;

    // Not sure what to do here...
    private final CurrencyContext CONTEXT = CurrencyContextBuilder.of("BitcoinCurrencyContextProvider")
                                                .build();

    private Set<CurrencyUnit> bitcoinSet = new HashSet<>();

    public BitcoinCurrencyProvider() {
        CurrencyUnit btcUnit = CurrencyUnitBuilder.of("BTC", CONTEXT)
                                    .setDefaultFractionDigits(bitcoinFractionDigits)
                                    .build();
        bitcoinSet.add(btcUnit);
        bitcoinSet = Collections.unmodifiableSet(bitcoinSet);
    }

    @Override
    public String getProviderName(){
        return "bitcoin";
    }

    /**
     * Return a {@link CurrencyUnit} instances matching the given
     * {@link javax.money.CurrencyContext}.
     *
     * @param query the {@link javax.money.CurrencyQuery} containing the parameters determining the query. not null.
     * @return the corresponding {@link CurrencyUnit}s matching, never null.
     */
    @Override
    public Set<CurrencyUnit> getCurrencies(CurrencyQuery query){
        // only ensure BTC is the code, or it is a default query.
        if(query.getCurrencyCodes().contains("BTC") || query.getCurrencyCodes().isEmpty()){
            return bitcoinSet;
        }
        return Collections.emptySet();
    }

	@Override
	public boolean isCurrencyAvailable(CurrencyQuery query) {
		return !getCurrencies(query).isEmpty();
	}

}