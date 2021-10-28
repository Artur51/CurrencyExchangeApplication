//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.10.18 at 10:39:16 AM EEST 
//


package com.currency.server.pojo.jaxb.eurofxref;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the _int.ecb.vocabulary._2002_08_01.eurofxref package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Cube_QNAME = new QName("http://www.ecb.int/vocabulary/2002-08-01/eurofxref", "Cube");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: _int.ecb.vocabulary._2002_08_01.eurofxref
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CurrencyExchange }
     * 
     */
    public CurrencyExchange createCurrencyExchange() {
        return new CurrencyExchange();
    }

    /**
     * Create an instance of {@link CurrencyExchangeTimeAndRates }
     * 
     */
    public CurrencyExchangeTimeAndRates createCurrencyExchangeTimeAndRates() {
        return new CurrencyExchangeTimeAndRates();
    }

    /**
     * Create an instance of {@link CurrencyExchangeRate }
     * 
     */
    public CurrencyExchangeRate createCurrencyExchangeRate() {
        return new CurrencyExchangeRate();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CurrencyExchange }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CurrencyExchange }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref", name = "Cube")
    public JAXBElement<CurrencyExchange> createCube(CurrencyExchange value) {
        return new JAXBElement<CurrencyExchange>(_Cube_QNAME, CurrencyExchange.class, null, value);
    }

}